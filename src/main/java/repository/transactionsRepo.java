package repository;

import model.transaction;
import helper.currentUser;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class transactionsRepo {
    private final Connection conn;
    
    public transactionsRepo(Connection conn) {
        this.conn = conn;
    }

    // Ambil semua transaksi
    public List<transaction> getAllTransaction() {
        List<transaction> list = new ArrayList<>();
        String query = "SELECT * FROM transactions";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transaction t = new transaction(
                    rs.getDate("date").toLocalDate(),
                    rs.getDouble("grand_total"),
                    rs.getInt("total_item"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("created_by"),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    rs.getInt("updated_by")
                );
                t.setId(rs.getInt("id"));
                list.add(t);
            }

        } catch (SQLException e) {
            System.out.println("Error getAllTransaction: " + e.getMessage());
        }

        return list;
    }

    // Tambah transaksi baru
    public void insert(transaction t) throws SQLException {
        String sql = "INSERT INTO transactions (date, grand_total, total_item, created_at, created_by, updated_at, updated_by) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(t.getDate()));
            stmt.setDouble(2, t.getGrandTotal());
            stmt.setInt(3, t.getTotalItem());
            stmt.setTimestamp(4, Timestamp.valueOf(t.getCreatedAt()));
            stmt.setInt(5, currentUser.getId());
            stmt.setTimestamp(6, Timestamp.valueOf(t.getUpdatedAt()));
            stmt.setInt(7, currentUser.getId());
            stmt.executeUpdate();
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
