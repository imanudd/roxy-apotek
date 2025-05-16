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
    Connection conn = DatabaseConfig.connect();
    
    //list stock 
    public List<stock> getList(){
        List<stock> list = new ArrayList<>();
        String query = "SELECT s.id, i.item_name, s.first_stock, s.stock_in, s.stock_out, s.remaining_stock"+
                "FROM stocks s"+
                "JOINT items i ON s.item_id=i.id";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()){
            stock sItem = new stock(
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
        }catch (SQLException e) {
            System.out.println("Error getAllSuppliers: " + e.getMessage());
        }
        return list;
    }
    //create stock
    public boolean createStock(stock sItem, int itemId) {
    String query = "INSERT INTO stocks (item_id, first_stock, remaining_stock, created_at, created_by,) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, itemId);
            stmt.setInt(2, sItem.getFirstStock());
            stmt.setInt(3, sItem.getFirstStock());
            stmt.setTimestamp(4, Timestamp.valueOf(sItem.getCreatedAt()));
            stmt.setInt(5, currentUser.getId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //update stock
    public boolean updateStock(stock sItem) {
        String selectQuery = "SELECT first_stock, stock_in, stock_out FROM stocks WHERE item_id = ?";
        String updateQuery = "UPDATE stocks SET stock_in = ?, stock_out = ?, remaining_stock = ?, updated_at = ?, updated_by = ? WHERE item_id = ?";

        try (
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery)
        ) {
            selectStmt.setInt(1, sItem.getItemId());
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int firstStock = rs.getInt("first_stock");
                int stockIn = sItem.getStockIn() != -1 ? sItem.getStockIn() : rs.getInt("stock_in");
                int stockOut = sItem.getStockOut() != -1 ? sItem.getStockOut() : rs.getInt("stock_out");

                int remainingStock = firstStock + stockIn - stockOut;

                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, stockIn);
                    updateStmt.setInt(2, stockOut);
                    updateStmt.setInt(3, remainingStock);
                    updateStmt.setTimestamp(4, Timestamp.valueOf(sItem.getUpdatedAt()));
                    updateStmt.setInt(5, currentUser.getId());
                    updateStmt.setInt(6, sItem.getItemId());

                    return updateStmt.executeUpdate() > 0;
                }

            } else {
                System.out.println("Stock dengan item_id tidak ditemukan.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    //delete stock
    public boolean deleteStock(int stockId) {
    String query = "DELETE FROM stocks WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, stockId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
       
    public boolean isItemsNameExists(String name, int excludeId){
    String query = excludeId>0
            ? "SELECT COUNT (*) AS count FROM stocks WHERE item_id=? AND id<>?"
            : "SELECT COUNT (*) AS count FROM stocks WHERE item_id=?";
            
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
