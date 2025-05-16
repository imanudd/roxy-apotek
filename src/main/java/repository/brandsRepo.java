package repository;

import config.DatabaseConfig;
import model.brands;
import model.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class brandsRepo {
    private final Connection conn;

    public brandsRepo(Connection conn) {
        this.conn = conn;
    }

    public List<brands> listBrands(String search) throws SQLException {
        String sql;
        boolean hasSearch = search != null && !search.trim().isEmpty();

        if (hasSearch) {
            sql = "SELECT * FROM brands WHERE brand_name ILIKE ? ORDER BY id ASC";
        } else {
            sql = "SELECT * FROM brands ORDER BY id ASC";
        }

        List<brands> brandList = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (hasSearch) {
                stmt.setString(1, "%" + search.trim() + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                brands b = new brands(
                    rs.getInt("id"),
                    rs.getString("brand_name"),
                    rs.getInt("supplier_id"),
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
            stmt.setBoolean(7, true);
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
        String sql = "SELECT * FROM brands WHERE id = ? and status = true";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new brands(
                    rs.getInt("id"),
                    rs.getString("brand_name"),
                    rs.getInt("supplier_id"),
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
    public boolean isBrandNameExists(String name, int excludeId){
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
