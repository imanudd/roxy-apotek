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
    
    public boolean createItem(items itm){
        String query="INSERT INTO items(item_name, brand_id, sell_price, created_at, created_by, updated_at, updated_by)"+"VALUES(?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement stmt=conn.prepareStatement(query)){
            stmt.setString(1, itm.getItemName());
            stmt.setInt(2, itm.getBrandId());
            stmt.setInt(3, itm.getPrice());
            stmt.setTimestamp(4, Timestamp.valueOf(itm.getCreatedAt()));
            stmt.setInt(5, itm.getCreatedBy());
            stmt.setTimestamp(6, Timestamp.valueOf(itm.getUpdatedAt()));
            stmt.setInt(7, itm.getUpdatedBy());
            
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
            stmt.setInt(5, itm.getUpdatedBy());
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
}
