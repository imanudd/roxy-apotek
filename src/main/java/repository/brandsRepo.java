package repository;

import model.brands;
import model.optionBrands;
import model.optionSupplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class brandsRepo {
    private final Connection conn;

    public brandsRepo(Connection conn) {
        this.conn = conn;
    }

    // List semua brand
    public List<brands> listBrands(String search,Integer supplierId) throws SQLException {
       List<brands> brandList = new ArrayList<>(); 
       List<Object> params = new ArrayList<>();
       
       String sql = " SELECT b.*, s.supplier_name FROM brands b LEFT JOIN suppliers s ON b.supplier_id = s.id WHERE TRUE ";
        
       if (search != null && !search.trim().isEmpty()){
           sql += " AND b.brand_name ILIKE ? ";
           params.add( "%" + search.trim() + "%");
       }
       
       if (supplierId != 0){
           sql += " AND b.supplier_id = ? ";
           params.add(supplierId);
       }
       
       sql += " ORDER BY b.id ASC ";

       PreparedStatement stmt = conn.prepareStatement(sql);
        
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            if (param instanceof String) {
                stmt.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                stmt.setInt(i + 1, (Integer) param);
            }
        }

        
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            brands b = new brands(
                rs.getInt("id"),
                rs.getString("brand_name"),
                rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("created_by"),
                    rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                    rs.getInt("updated_by"),
                    rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                    rs.getInt("deleted_by"),
                    rs.getBoolean("status")
                );
                brandList.add(b);
            }
       

        return brandList;
    }

    // CREATE brand
    public boolean insertBrand(brands b, int currentUser) throws SQLException {
        String sql = "INSERT INTO brands (brand_name, supplier_id, created_at, created_by, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getBrandName());
            stmt.setInt(2, b.getSupplierId());
            stmt.setTimestamp(3, Timestamp.valueOf(b.getCreatedAt()));
            stmt.setInt(4, currentUser);
            stmt.setBoolean(5, true);
            return stmt.executeUpdate() > 0;
        }
    }

    // UPDATE brand
    public boolean updateBrand(brands b, int currentUser) throws SQLException {
        String sql = "UPDATE brands SET brand_name = ?, supplier_id = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getBrandName());
            stmt.setInt(2, b.getSupplierId());
            stmt.setTimestamp(3, Timestamp.valueOf(b.getUpdatedAt()));
            stmt.setInt(4, currentUser);
            stmt.setInt(5, b.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    // SOFT DELETE brand
    public boolean softDeleteBrand(int id, int currentUser) throws SQLException {
        String sql = "UPDATE brands SET status = false, deleted_at = now(), deleted_by = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, currentUser);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // SOFT DELETE brand by id supplier
    public boolean deleteByIdSupplier(int id, int currentUser) throws SQLException {
        String sql = "UPDATE brands SET status = false, deleted_at = now(), deleted_by = ? WHERE supplierid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, currentUser);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // get brand by id
    public brands getBrandById(int id) throws SQLException {
        String sql = "SELECT b.*, s.supplier_name FROM brands b LEFT JOIN suppliers s ON b.supplier_id = s.id WHERE b.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new brands(
                    rs.getInt("id"),
                    rs.getString("brand_name"),
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("created_by"),
                    rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                    rs.getInt("updated_by"),
                    rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                    rs.getInt("deleted_by"),
                    rs.getBoolean("status")
                );
            }
        }
        return null;
    }

    public brands getBrandsBySupplier (int id) throws SQLException {
        String sql = "SELECT * FROM brands WHERE supplier_id = ? and status = true";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new brands(
                    rs.getInt("id"),
                    rs.getString("brand_name"),
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("created_by"),
                    rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                    rs.getInt("updated_by"),
                    rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                    rs.getInt("deleted_by"),
                    rs.getBoolean("status")
                );
            }
        }
        return null;
    }
//validasi brand
    public boolean isBrandNameExists(String name, int excludeId)throws SQLException {
    String query = excludeId>0
            ? "SELECT COUNT (*) AS count FROM brands WHERE brand_name=? AND id<>?"
            : "SELECT COUNT (*) AS count FROM brands WHERE brand_name=?";
            
        try (PreparedStatement ps = conn.prepareStatement(query)) {
         
            ps.setString(1, name);
            if (excludeId > 0) ps.setInt(2, excludeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    public List<optionBrands> OptionBrands(int supplierId) throws SQLException{
        String sql;
        sql = "SELECT * FROM brands where status = true and supplier_id = " + supplierId + " order by id asc";

        List<optionBrands> optionbBrands = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)){

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                optionBrands ob = new optionBrands(
                        rs.getInt("id"),
                        rs.getString("brand_name")
                );
                optionbBrands.add(ob);
            }
        
        }
        return optionbBrands;  
    }
}
