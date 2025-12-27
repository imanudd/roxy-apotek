/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import helper.currentUser;
import model.LogStock;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogStockRepo {
    private final Connection conn;

    // Constructor menerima Connection
    public LogStockRepo(Connection conn) {
        this.conn = conn;
    }

    public List<LogStock> getList(String filter, int rangeDay, int limit, int offset) throws SQLException {
        StringBuilder query = new StringBuilder(
                "SELECT s.id, s.activity_name, s.item_id, i.item_name, s.ref_id, u.username, " +
                        "s.qty, s.created_at, s.created_by, s.updated_at, s.updated_by " +
                        "FROM log_stocks s " +
                        "LEFT JOIN users u ON s.created_by = u.id " +
                        "LEFT JOIN items i ON s.item_id = i.id");

        List<LogStock> list = new ArrayList<>();
        int paramIndex = 1;
        boolean hasFilter = filter != null && !filter.trim().isEmpty();
        LocalDateTime from = null;
        boolean hasWhere = false;

        // Bangun WHERE clause
        if (hasFilter) {
            query.append(" WHERE s.activity_name = ?");
            hasWhere = true;
        }

        if (rangeDay > 0) {
            from = LocalDateTime.now().minusDays(rangeDay);
            query.append(hasWhere ? " AND" : " WHERE");
            query.append(" s.created_at >= ?");
        }

        query.append(" ORDER BY s.id ASC");

        if (limit > 0) {
            query.append(" LIMIT ? OFFSET ?");
        }

        try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            if (hasFilter) {
                stmt.setString(paramIndex++, filter.trim());
            }
            if (rangeDay > 0 && from != null) {
                stmt.setTimestamp(paramIndex++, Timestamp.valueOf(from));
            }
            if (limit > 0) {
                stmt.setInt(paramIndex++, limit);
                stmt.setInt(paramIndex++, offset);
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
                            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime()
                                    : null,
                            rs.getInt("created_by"),
                            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime()
                                    : null,
                            rs.getInt("updated_by"));
                    list.add(log);
                }
            }
        }

        return list;
    }

    // Insert
    public boolean insert(LogStock log) throws SQLException {
        validateLogStock(log, false);

        String query = "INSERT INTO log_stocks (activity_name, item_id, ref_id, qty, created_at, created_by) VALUES (?, ?, ? ,?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, log.getActivityName());
        stmt.setInt(2, log.getItemId());
        stmt.setInt(3, log.getRefId());
        stmt.setInt(4, log.getQty());
        stmt.setTimestamp(5, Timestamp.valueOf(log.getCreatedAt()));
        stmt.setInt(6, currentUser.getId());
        int rowsInserted = stmt.executeUpdate();

        return rowsInserted > 0;
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
            if (log.getUpdatedAt() == null) {
                throw new IllegalArgumentException("Data update harus diisi.");
            }
        } else {
            if (log.getCreatedAt() == null) {
                throw new IllegalArgumentException("Data created harus diisi.");
            }
        }
    }
}
