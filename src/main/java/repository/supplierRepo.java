package repository;

import model.suppliers;
import helper.currentUser;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import helper.currentUser;

public class supplierRepo {
    private final Connection conn;

    public supplierRepo(Connection conn) {
        this.conn = conn;
    }

    // Mendapatkan list supplier yang belum dihapus
    public List<suppliers> ListSupplier(String search) throws SQLException {
        String sql;
        boolean hasSearch = search != null && !search.isEmpty();

        if (hasSearch) {
            sql = "SELECT * FROM suppliers WHERE supplier_name ILIKE ? order by id asc";
        } else {
            sql = "SELECT * FROM suppliers order by id asc";
        }

        List<suppliers> suppliers = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            if (hasSearch) {
                stmt.setString(1, "%" + search.trim() + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                suppliers spl = new suppliers(
                        rs.getInt("id"),
                        rs.getString("supplier_name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getInt("updated_by"),
                        rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                        rs.getInt("deleted_by"),
                        rs.getBoolean("status")
                );
                suppliers.add(spl);
            }
        
        }
        return suppliers;
    }

    // create supplier 
    public boolean createSupplier(suppliers spl,int currentUser) throws SQLException {
        String query = "INSERT INTO suppliers(supplier_name, address, phone, created_at, created_by, status) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, spl.getSupplierName());
            stmt.setString(2, spl.getAddress());
            stmt.setString(3, spl.getPhone());
            stmt.setTimestamp(4, Timestamp.valueOf(spl.getCreatedAt()));
            stmt.setInt(5, currentUser);
            stmt.setBoolean(6, true);

            return stmt.executeUpdate() > 0;    
        }
    }

    // Update supplier
    public boolean updateSupplier(suppliers spl, int currentUser) throws SQLException {
        String query = "UPDATE suppliers SET supplier_name=?, address=?, phone=?, updated_at=?, updated_by=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, spl.getSupplierName());
            stmt.setString(2, spl.getAddress());
            stmt.setString(3, spl.getPhone());
            stmt.setTimestamp(4, Timestamp.valueOf(spl.getUpdatedAt()));
            stmt.setInt(5, currentUser);
            stmt.setInt(6, spl.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    // Soft delete supplier
    public boolean deleteSupplier(suppliers spl, int currentUser) throws SQLException {
        String query = "UPDATE suppliers SET deleted_at = ?, deleted_by = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, currentUser);
            stmt.setBoolean(3, false);
            stmt.setInt(4, spl.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } 
    }

    // Cek nama supplier sudah digunakan
    public boolean isSupplierNameExists(String name, int excludeId) {
        String sql = excludeId > 0
                ? "SELECT COUNT(*) AS count FROM suppliers WHERE supplier_name = ? AND id <> ? AND deleted_at IS NULL"
                : "SELECT COUNT(*) AS count FROM suppliers WHERE supplier_name = ? AND deleted_at IS NULL";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

    //get supplier by id
    public suppliers getSupplierById(int id) throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new suppliers(
                            rs.getInt("id"),
                            rs.getString("supplier_name"),
                            rs.getString("address"),
                            rs.getString("phone"),
                            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                            rs.getInt("created_by"),
                            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                            rs.getInt("updated_by"),
                            rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                            rs.getInt("deleted_by"),
                            rs.getBoolean("status")
                    );
                }
            }
        }    
        return null;
    }
}
