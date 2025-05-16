/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author User
 */
import config.DatabaseConfig;
import helper.currentUser;
import model.LogStock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogStockRepo {
    private Connection conn = DatabaseConfig.connect();

    // Get list
    public List<LogStock> getList() {
        List<LogStock> list = new ArrayList<>();
        String query = "SELECT ls.id, ls.activity_name, i.id, ls.ref_id, ls.qty, ls.created_at, ls.created_by, ls.updated_at, ls.updated_by" + 
                "FROM log_stocks ls"+
                "JOIN items i ON ls.item_id=i.id";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LogStock log = new LogStock();
                log.setId(rs.getInt("id"));
                log.setActivityName(rs.getString("activity_name"));
                log.setItemId(rs.getInt("item_id"));
                log.setRefId(rs.getInt("ref_id"));
                log.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                log.setCreatedBy(rs.getInt("created_by"));
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    log.setUpdatedAt(updatedAt.toLocalDateTime());
                }
                log.setUpdatedBy(rs.getInt("updated_by"));
                list.add(log);
            }
        } catch (SQLException e) {
            System.out.println("Error getList: " + e.getMessage());
        }

        return list;
    }

    // Insert
    public void insert(LogStock log) throws SQLException {
        validateLogStock(log, false);

        String query = "INSERT INTO log_stocks (activity_name, item_id, ref_id, created_at, created_by) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, log.getActivityName());
            stmt.setInt(2, log.getItemId());
            stmt.setInt(3, log.getRefId());
            stmt.setTimestamp(4, Timestamp.valueOf(log.getCreatedAt()));
            stmt.setInt(5, currentUser.getId());
            stmt.executeUpdate();
        }
    }

    // Update
    public void update(LogStock log) throws SQLException {
        validateLogStock(log, true);

        String query = "UPDATE log_stocks SET activity_name=?, item_id=?, ref_id=?, updated_at=?, updated_by=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, log.getActivityName());
            stmt.setInt(2, log.getItemId());
            stmt.setInt(3, log.getRefId());
            stmt.setTimestamp(4, Timestamp.valueOf(log.getUpdatedAt()));
            stmt.setInt(5, currentUser.getId());
            stmt.setInt(6, log.getId());
            stmt.executeUpdate();
        }
    }

    // Delete
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM log_stocks WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Validasi log
    private void validateLogStock(LogStock log, boolean isUpdate) throws SQLException {
        if (log.getActivityName() == null || log.getActivityName().isEmpty()) {
            throw new IllegalArgumentException("Activity name tidak boleh kosong.");
        }

        if (!log.getActivityName().equals("stock_in") && !log.getActivityName().equals("stock_out")) {
            throw new IllegalArgumentException("Activity name harus 'stock_in' atau 'stock_out'.");
        }

        if (log.getItemId() <= 0) {
            throw new IllegalArgumentException("Item ID tidak valid.");
        }

        if (log.getRefId() <= 0) {
            throw new IllegalArgumentException("Ref ID tidak valid.");
        }

        if (log.getActivityName().equals("stock_in")) {
            // cek ref_id ada di suppliers
            String check = "SELECT COUNT(*) FROM suppliers WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(check)) {
                stmt.setInt(1, log.getRefId());
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        throw new IllegalArgumentException("Ref ID tidak ditemukan di tabel suppliers.");
                    }
                }
            }
        } else if (log.getActivityName().equals("stock_out")) {
            // cek ref_id ada di transactions
            String check = "SELECT COUNT(*) FROM transactions WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(check)) {
                stmt.setInt(1, log.getRefId());
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        throw new IllegalArgumentException("Ref ID tidak ditemukan di tabel transactions.");
                    }
                }
            }
        }

        if (isUpdate) {
            if (log.getId() <= 0) {
                throw new IllegalArgumentException("ID tidak valid untuk update.");
            }
            if (log.getUpdatedAt() == null || log.getUpdatedBy() == 0) {
                throw new IllegalArgumentException("Data update harus diisi.");
            }
        } else {
            if (log.getCreatedAt() == null || log.getCreatedBy() == 0) {
                throw new IllegalArgumentException("Data created harus diisi.");
            }
        }
    }
}
