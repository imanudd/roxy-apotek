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
import model.items;
import helper.currentUser;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class itemsRepo {
    Connection conn = DatabaseConfig.connect();
    
        public List<items> getAllItems() {
        List<items> list = new ArrayList<>();
        String query = "SELECT * FROM items";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                items itm = new items(
                    rs.getString("item_name"),
                    rs.getInt("brand_id"),
                    rs.getInt("price"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("created_by"),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    rs.getInt("updated_by")
                );
                itm.setId(rs.getInt("id")); // jika kamu punya setter ID
                list.add(itm);
            }

        } catch (SQLException e) {
            System.out.println("Error getAllItems: " + e.getMessage());
        }

        return list;
    }
    //create item dan validasi stock untuk CRUD stock by intputan dari itemsRepo    
    public boolean createItem(items itm){
        String query="INSERT INTO items(item_name, brand_id, sell_price, created_at, created_by)"+"VALUES(?, ?, ?, ?, ?)";
        try(PreparedStatement stmt=conn.prepareStatement(query)){
            stmt.setString(1, itm.getItemName());
            stmt.setInt(2, itm.getBrandId());
            stmt.setInt(3, itm.getPrice());
            stmt.setTimestamp(4, Timestamp.valueOf(itm.getCreatedAt()));
            stmt.setInt(5, currentUser.getId());
            
            int rows=stmt.executeUpdate();
            return rows>0;
        }catch (SQLException e){
            System.out.println("Error query: "+ e.getMessage());
            return false;
        }
    }
    
    public boolean updateItem(items itm){
        String query="UPDATE items SET item_name=?, brand_id=?, sell_price=?, updated_at=?, updated_by=? WHERE id=?";
        try(PreparedStatement stmt=conn.prepareStatement(query)){
            stmt.setString(1, itm.getItemName());
            stmt.setInt(2, itm.getBrandId());
            stmt.setInt(3, itm.getPrice());
            stmt.setTimestamp(4, Timestamp.valueOf(itm.getUpdatedAt()));
            stmt.setInt(5, currentUser.getId());
            stmt.setInt(6, itm.getId());
            
            int rows=stmt.executeUpdate();
            return rows>0;
        }catch (SQLException e){
            System.out.println("Error query: "+ e.getMessage());
            return false;
        }
    }
    
    public boolean deleteItem(items itm){
        String query="DELETE FROM items WHERE id=?";
        try(PreparedStatement stmt=conn.prepareStatement(query)){
            stmt.setInt(1, itm.getId());
            int rows=stmt.executeUpdate();
            return rows>0;
        }catch (SQLException e){
            System.out.println("Error query: "+ e.getMessage());
            return false;
        }
    }
    
    public boolean isItemsNameExists(String name, int excludeId){
    String query = excludeId>0
            ? "SELECT COUNT (*) AS count FROM items WHERE item_name=? AND id<>?"
            : "SELECT COUNT (*) AS count FROM items WHERE item_name=?";
            
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
