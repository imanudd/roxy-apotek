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
import model.suppliers;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class supplierRepo {
    Connection conn = DatabaseConfig.connect();
    
    public List<suppliers> getList(){
        List<suppliers> list = new ArrayList<>();
        String query = "SELECT * FROM suppliers";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                suppliers spl = new suppliers(
                    rs.getString("supplier_name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("created_by"),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    rs.getInt("updated_by")
            );
            spl.setId(rs.getInt("id")); // jika ada setId
            list.add(spl);
        }
        }catch (SQLException e) {
            System.out.println("Error getAllSuppliers: " + e.getMessage());
        }
        return list;
    }
    
    public boolean createSupplier(suppliers spl){
        String query = "INSERT INTO suppliers(supplier_name, address, phone, created_at, created_by, updated_at, updated_by)"+"VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement stmt=conn.prepareStatement(query)){
            if(conn == null){
                System.out.println("Koneksi null. Gagal menyimpan Supplier.");
            }
            
            stmt.setString(1, spl.getSupplierName());
            stmt.setString(2, spl.getAddress());
            stmt.setString(3, spl.getPhone());
            stmt.setTimestamp(4, Timestamp.valueOf(spl.getCreatedAt()));
            stmt.setInt(5, spl.getCreatedBy());
            stmt.setTimestamp(6, Timestamp.valueOf(spl.getUpdatedAt()));
            stmt.setInt(7, spl.getUpdatedBy());
            
            int rows = stmt.executeUpdate();
            return rows>0;
        }catch(SQLException e){
            System.out.println("Error query: "+ e.getMessage());
            return false;
        }
    }
    
    public boolean updateSupplier(suppliers spl){
        String query = "UPDATE suppliers SET supplier_Name=?, address=?, phone=?, updated_at=?, updated_by=? WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, spl.getSupplierName());
            stmt.setString(2, spl.getAddress());
            stmt.setString(3, spl.getPhone());
            stmt.setTimestamp(4, Timestamp.valueOf(spl.getUpdatedAt()));
            stmt.setInt(5, spl.getUpdatedBy());
            stmt.setInt(6,spl.getId());
            
            int rows = stmt.executeUpdate();
            return rows>0;
        }catch(SQLException e){
            System.out.println("Error query: "+ e.getMessage());
            return false;
        }
    }
    
    public boolean deleteSuplier(suppliers spl){
        String query ="DELETE FROM suppliers WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, spl.getId());
            int rows = stmt.executeUpdate();
            return rows>0;
        } catch(SQLException e){
           System.out.println("Error query: "+ e.getMessage());
            return false; 
        }
    }
    
    public boolean isSupplierNameExists(String name, int excludeId) {
    String sql = excludeId > 0 
        ? "SELECT COUNT(*) AS count FROM suppliers WHERE supplier_name = ? AND id <> ?"
        : "SELECT COUNT(*) AS count FROM suppliers WHERE supplier_name = ?";

    try (Connection conn = DatabaseConfig.connect();  // ini penting!
         PreparedStatement ps = conn.prepareStatement(sql)) {
         
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
