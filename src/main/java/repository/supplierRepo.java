package repository;

import model.suppliers;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class supplierRepo {
    private final Connection conn;

    public supplierRepo(Connection conn) {
        this.conn = conn;
    }

    // Mendapatkan list supplier yang belum dihapus
    public List<suppliers> getList() {
        List<suppliers> list = new ArrayList<>();
        String query = "SELECT * FROM suppliers WHERE deleted_at IS NULL";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers spl = new suppliers(
                        rs.getString("supplier_name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getInt("updated_by"),
                        rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                        rs.getInt("deleted_by")
                );
                spl.setId(rs.getInt("id"));
                Timestamp deletedTs = rs.getTimestamp("deleted_at");
                if (deletedTs != null) {
                    spl.setDeletedAt(deletedTs.toLocalDateTime());
                }
                list.add(spl);
            }
        } catch (SQLException e) {
            System.out.println("Error getAllSuppliers: " + e.getMessage());
        }
        return list;
    }

    // Menambahkan supplier baru
    public boolean createSupplier(suppliers spl) {
        String query = "INSERT INTO suppliers(supplier_name, address, phone, created_at, created_by, updated_at, updated_by) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, spl.getSupplierName());
            stmt.setString(2, spl.getAddress());
            stmt.setString(3, spl.getPhone());
            stmt.setTimestamp(4, Timestamp.valueOf(spl.getCreatedAt()));
            stmt.setInt(5, spl.getCreatedBy());
            stmt.setTimestamp(6, Timestamp.valueOf(spl.getUpdatedAt()));
            stmt.setInt(7, spl.getUpdatedBy());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error query: " + e.getMessage());
            return false;
        }
    }

    // Memperbarui data supplier
    public boolean updateSupplier(suppliers spl) {
        String query = "UPDATE suppliers SET supplier_name=?, address=?, phone=?, updated_at=?, updated_by=? WHERE id=? AND deleted_at IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, spl.getSupplierName());
            stmt.setString(2, spl.getAddress());
            stmt.setString(3, spl.getPhone());
            stmt.setTimestamp(4, Timestamp.valueOf(spl.getUpdatedAt()));
            stmt.setInt(5, spl.getUpdatedBy());
            stmt.setInt(6, spl.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error query: " + e.getMessage());
            return false;
        }
    }

    // Soft delete supplier (hanya update deleted_at)
    public boolean deleteSupplier(suppliers spl) {
        String query = "UPDATE suppliers SET deleted_at = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, spl.getId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error soft delete supplier: " + e.getMessage());
            return false;
        }
    }

    // Cek nama supplier sudah digunakan (tidak termasuk yang sudah dihapus)
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
}
