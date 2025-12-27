package repository;

import model.transaction;
import helper.currentUser;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class transactionsRepo {
    private final Connection conn;

    public transactionsRepo(Connection conn) {
        this.conn = conn;
    }

    // Ambil semua transaksi
    public List<transaction> getAllTransaction(int range, int limit, int offset) throws SQLException {
        List<transaction> list = new ArrayList<>();
        String query = "SELECT t.*, u.username FROM transactions t LEFT JOIN users u ON t.created_by = u.id ";

        LocalDateTime from = null;
        if (range > 0) {
            query += "WHERE t.created_at >= ? ";
            from = LocalDateTime.now().minusDays(range); // gunakan LocalDateTime sesuai range
        }

        query += "ORDER BY id ASC";

        if (limit > 0) {
            query += " LIMIT ? OFFSET ? ";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int paramIndex = 1;
            if (range > 0 && from != null) {
                stmt.setTimestamp(paramIndex++, Timestamp.valueOf(from)); // ✅ atur parameter SEBELUM executeQuery
            }
            if (limit > 0) {
                stmt.setInt(paramIndex++, limit);
                stmt.setInt(paramIndex++, offset);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transaction t = new transaction(
                            rs.getInt("id"),
                            rs.getDate("date").toLocalDate(),
                            rs.getDouble("grand_total"),
                            rs.getInt("total_item"),
                            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime()
                                    : null,
                            rs.getInt("created_by"),
                            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime()
                                    : null,
                            rs.getInt("updated_by"),
                            rs.getString("username"));
                    list.add(t);
                }
            }
        }

        return list;
    }

    // Tambah transaksi baru
    public Integer insert(transaction t) throws SQLException {
        String sql = "INSERT INTO transactions (date, grand_total, total_item, created_at, created_by) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setDate(1, Date.valueOf(t.getDate()));
        stmt.setDouble(2, t.getGrandTotal());
        stmt.setInt(3, t.getTotalItem());
        stmt.setTimestamp(4, Timestamp.valueOf(t.getCreatedAt()));
        stmt.setInt(5, currentUser.getId());

        int rows = stmt.executeUpdate();

        if (rows == 0) {
            throw new SQLException("Insert failed, no rows affected.");
        }

        ResultSet generatedKeys = stmt.getGeneratedKeys();

        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Insert succeeded but no ID obtained.");
        }

    }

    // Update transaksi
    public void update(transaction t) throws SQLException {
        String sql = "UPDATE transactions SET date=?, grand_total=?, total_item=?, updated_at=?, updated_by=? WHERE id=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(t.getDate()));
            stmt.setDouble(2, t.getGrandTotal());
            stmt.setInt(3, t.getTotalItem());
            stmt.setTimestamp(4, Timestamp.valueOf(t.getUpdatedAt()));
            stmt.setInt(5, currentUser.getId());
            stmt.setInt(6, t.getId());
            stmt.executeUpdate();
        }
    }

    // Hapus transaksi
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
