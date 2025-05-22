package repository;

import config.DatabaseConfig;
import model.TransactionDetail;
import helper.currentUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class transactionsDetailRepo {
    private final Connection conn;
    
    public transactionsDetailRepo(Connection conn) {
        this.conn = conn;
    }

    // Get List of TransactionDetails
    public List<TransactionDetail> getList() throws SQLException {
        List<TransactionDetail> transactionDetails = new ArrayList<>();
        String sql = "SELECT dt.id, dt.transaction_id, dt.item_id, dt.qty, dt.price, " +
                     "i.item_name, t.date AS transaction_date, dt.created_at, dt.created_by, dt.updated_at, dt.updated_by " +
                     "FROM transaction_details dt " +
                     "JOIN transactions t ON dt.transaction_id = t.id " +
                     "JOIN items i ON dt.item_id = i.id";
        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                TransactionDetail dt = new TransactionDetail();
                dt.setId(rs.getInt("id"));
                dt.setTransactionId(rs.getInt("transaction_id"));
                dt.setItemId(rs.getInt("item_id"));
                dt.setQty(rs.getInt("qty"));
                dt.setPrice(rs.getDouble("price"));
                dt.setItemName(rs.getString("item_name"));
                dt.setTransactionDate(rs.getString("transaction_date"));
                dt.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                dt.setCreatedBy(rs.getInt("created_by"));
                dt.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                dt.setUpdatedBy(rs.getInt("updated_by"));
                transactionDetails.add(dt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionDetails;
    }

    // Insert a new TransactionDetail
    public boolean insert(TransactionDetail dt) throws SQLException {
        String sql = "INSERT INTO transaction_details (transaction_id, item_id, qty, price, created_at, created_by) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dt.getTransactionId());
            stmt.setInt(2, dt.getItemId());
            stmt.setInt(3, dt.getQty());
            stmt.setDouble(4, dt.getPrice());
            stmt.setTimestamp(5, Timestamp.valueOf(dt.getCreatedAt()));
            stmt.setInt(6, currentUser.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    // Update an existing TransactionDetail
    public boolean update(TransactionDetail dt) throws SQLException {
        String sql = "UPDATE transaction_details SET transaction_id = ?, item_id = ?, qty = ?, price = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dt.getTransactionId());
            stmt.setInt(2, dt.getItemId());
            stmt.setInt(3, dt.getQty());
            stmt.setDouble(4, dt.getPrice());
            stmt.setTimestamp(5, Timestamp.valueOf(dt.getUpdatedAt()));
            stmt.setInt(6, currentUser.getId());
            stmt.setInt(7, dt.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete a TransactionDetail
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM transaction_details WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
