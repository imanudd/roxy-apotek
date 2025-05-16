package repository;

import config.DatabaseConfig;
import model.items;
import helper.currentUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class itemsRepo {
    Connection conn = DatabaseConfig.connect();

    // Ambil semua data item
    public List<items> getAllItems() {
        List<items> list = new ArrayList<>();
        String query = "SELECT * FROM items";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                items itm = new items(
                    rs.getString("item_name"),
                    rs.getInt("brand_id"),
                    rs.getDouble("sell_price"), // perbaikan di sini
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("created_by"),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    rs.getInt("updated_by")
                );
                itm.setId(rs.getInt("id"));
                list.add(itm);
            }

        } catch (SQLException e) {
            System.out.println("Error getAllItems: " + e.getMessage());
        }

        return list;
    }

    // Tambah item baru
    public boolean createItem(items itm) {
        String query = "INSERT INTO items (item_name, brand_id, sell_price, created_at, created_by) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itm.getItemName());
            stmt.setInt(2, itm.getBrandId());
            stmt.setDouble(3, itm.getPrice());
            stmt.setTimestamp(4, Timestamp.valueOf(itm.getCreatedAt()));
            stmt.setInt(5, currentUser.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error createItem: " + e.getMessage());
            return false;
        }
    }

    // Update item
    public boolean updateItem(items itm) {
        String query = "UPDATE items SET item_name = ?, brand_id = ?, sell_price = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itm.getItemName());
            stmt.setInt(2, itm.getBrandId());
            stmt.setDouble(3, itm.getPrice());
            stmt.setTimestamp(4, Timestamp.valueOf(itm.getUpdatedAt()));
            stmt.setInt(5, currentUser.getId());
            stmt.setInt(6, itm.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error updateItem: " + e.getMessage());
            return false;
        }
    }

    // Hapus item
    public boolean deleteItem(items itm) {
        String query = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itm.getId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleteItem: " + e.getMessage());
            return false;
        }
    }

    // Validasi nama item
    public boolean isItemsNameExists(String name, int excludeId) {
        String query = excludeId > 0
                ? "SELECT COUNT(*) AS count FROM items WHERE item_name = ? AND id <> ?"
                : "SELECT COUNT(*) AS count FROM items WHERE item_name = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            if (excludeId > 0) ps.setInt(2, excludeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error isItemsNameExists: " + e.getMessage());
        }
        return false;
    }
}
