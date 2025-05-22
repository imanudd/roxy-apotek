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
import model.stock;
import helper.currentUser;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
public class stocksRepo {
    private final Connection conn;

    // Constructor menerima Connection
    public stocksRepo(Connection conn) {
        this.conn = conn;
    }

    
    //list stock 
    public List<stock> getList(String search) throws SQLException{
        List<stock> list = new ArrayList<>();
        boolean hasSearch = search != null && !search.isEmpty();

        String query = "SELECT s.id, s.item_id, i.item_name, s.first_stock, s.stock_in, s.stock_out, " +
                   "s.remaining_stock, s.created_at, s.created_by, s.updated_at, s.updated_by " +
                   "FROM stocks s " +
                   "JOIN items i ON s.item_id = i.id";

        if (search != null && !search.trim().isEmpty()) {
            query += " WHERE i.item_name ILIKE ?";
        }

        query += " ORDER BY s.id ASC";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            if (hasSearch) {
                stmt.setString(1, "%" + search.trim() + "%");
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stock stock = new stock(
                    rs.getInt("id"),
                    rs.getString("item_name"),
                    rs.getInt("item_id"),
                    rs.getInt("first_stock"),
                    rs.getInt("stock_in"),
                    rs.getInt("stock_out"),
                    rs.getInt("remaining_stock"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("created_by"),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    rs.getInt("updated_by")
                );
                list.add(stock);
            }
        }
        return list;
    }

    //create stock
    public boolean createStock(stock sItem, int currentUser) throws SQLException {   
    String query = "INSERT INTO stocks (item_id, first_stock,stock_in, stock_out, remaining_stock, created_at, created_by) " +
                   "VALUES (?, ?, ? , ?, ?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, sItem.getItemId());
            stmt.setInt(2, sItem.getFirstStock());
            stmt.setInt(3, sItem.getStockIn());
            stmt.setInt(4, sItem.getStockOut());
            stmt.setInt(5, sItem.getRemainingStock());
            stmt.setTimestamp(6, Timestamp.valueOf(sItem.getCreatedAt()));
            stmt.setInt(7, currentUser);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } 
    }

    //update stock
    public boolean updateStock(stock sItem, int currentUser) throws SQLException {
        String query = "UPDATE stocks SET stock_in = ?, stock_out = ?, remaining_stock = ?, updated_at = ?, updated_by = ? WHERE item_id = ?";

        try (
            PreparedStatement selectStmt = conn.prepareStatement(query)
        ) {
            selectStmt.setInt(1, sItem.getStockIn());
            selectStmt.setInt(2, sItem.getStockOut());
            selectStmt.setInt(3, sItem.getRemainingStock());
            selectStmt.setTimestamp(4, Timestamp.valueOf(sItem.getUpdatedAt()));
            selectStmt.setInt(5, currentUser);
            selectStmt.setInt(6, sItem.getItemId());

            int rows = selectStmt.executeUpdate();
            return rows > 0;

        } 
    }
    
    public boolean deleteStockByItemId(int itemId) throws SQLException {

        String query = "DELETE FROM stocks WHERE item_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    //delete stock by item id
    public boolean deleteStock(int stockId) throws SQLException {

        String query = "DELETE FROM stocks WHERE item_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, stockId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
    
       
    //get stock by item id
    public stock getStockById(int id) throws SQLException {
        String query = "SELECT s.*, i.item_name FROM stocks s LEFT JOIN items i ON s.item_id = i.id WHERE s.item_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new stock(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getInt("item_id"),
                        rs.getInt("first_stock"),
                        rs.getInt("stock_in"),
                        rs.getInt("stock_out"),
                        rs.getInt("remaining_stock"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        rs.getInt("updated_by")
                    );
                }
            }
        }    
        return null;
    }
}
