/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import helper.currentUser;
import model.LogStock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogStockRepo {
    private final Connection conn;

    // Constructor menerima Connection
    public LogStockRepo(Connection conn) {
        this.conn = conn;
    }

    // Get list
    public List<LogStock> getList(String filter) throws SQLException {
        List<LogStock> list = new ArrayList<>();
        boolean hasFilter = filter != null && !filter.trim().isEmpty();

        String query = "SELECT s.activity_name, s.item_id, i.item_name , s.ref_id, u.username, s.qty FROM log_stocks s LEFT JOIN users u ON s.created_by = u.id LEFT JOIN items i ON s.item_id = i.id";

        if (hasFilter) {
            query += " WHERE s.activity_name = ?";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)){
            if (hasFilter) {
                stmt.setString(1, filter.trim());
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LogStock log = new LogStock(
                        rs.getInt("id"),
                        rs.getString("activity_name"),
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getInt("ref_id"),
                        rs.getString("username"),
                        rs.getInt("qty"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getInt("updated_by")
                    );
                    list.add(log);
                }
            }
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
