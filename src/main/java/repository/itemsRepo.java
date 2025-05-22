package repository;

import model.items;
import model.optionItems;
import helper.currentUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class itemsRepo {
    private final Connection conn;

    public itemsRepo(Connection conn) {
        this.conn = conn;
    }

    // Ambil semua data item
    public List<items> getAllItems(String search) throws SQLException {
        List<items> list = new ArrayList<>();

        String query = "SELECT i.*, b.brand_name, s.first_stock, s.stock_in, s.stock_out, s.remaining_stock FROM items i LEFT JOIN brands b ON i.brand_id=b.id LEFT JOIN stocks s ON i.id = s.item_id";
        boolean hasSearch = search != null && !search.trim().isEmpty();

        if (hasSearch) {
            query += " WHERE b.brand_name ILIKE ?";
        }

        query += " ORDER BY b.id ASC";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (hasSearch) {
                stmt.setString(1, "%" + search.trim() + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp createdAtTs = rs.getTimestamp("created_at");
                    Timestamp updatedAtTs = rs.getTimestamp("updated_at");
                    Timestamp deletedAtTs = rs.getTimestamp("deleted_at");

                    items itm = new items(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getInt("brand_id"),
                        rs.getString("brand_name"),
                        rs.getDouble("sell_price"),
                        (createdAtTs != null) ? createdAtTs.toLocalDateTime() : null,
                        rs.getInt("created_by"),
                        (updatedAtTs != null) ? updatedAtTs.toLocalDateTime() : null,
                        rs.getInt("updated_by"),
                        rs.getBoolean("status"),
                        rs.getInt("deleted_by"),
                        (deletedAtTs != null) ? deletedAtTs.toLocalDateTime() : null,
                        rs.getInt("first_stock"),
                        rs.getInt("stock_in"),
                        rs.getInt("stock_out"),
                        rs.getInt("remaining_stock")
                    );
                    list.add(itm);
                }
            }
        }

    return list;
}

    //get item by id
    public items getItemById(int id) throws SQLException {
        String query = "SELECT i.*, b.brand_name FROM items i LEFT JOIN brands b ON i.brand_id=b.id WHERE i.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp createdAtTs = rs.getTimestamp("created_at");
                    Timestamp updatedAtTs = rs.getTimestamp("updated_at");
                    Timestamp deletedAtTs = rs.getTimestamp("deleted_at");
                    items itm = new items(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getInt("brand_id"),
                        rs.getString("brand_name"),
                        rs.getDouble("sell_price"),
                        (createdAtTs != null) ? createdAtTs.toLocalDateTime() : null,
                        rs.getInt("created_by"),
                        (updatedAtTs != null) ? updatedAtTs.toLocalDateTime() : null,
                        rs.getInt("updated_by"),
                        rs.getBoolean("status"),
                        rs.getInt("deleted_by"),
                        (deletedAtTs != null) ? deletedAtTs.toLocalDateTime() : null,
                        0,
                        0,
                        0,
                        0
                    );
                    return itm;
                }
            }
        }
        return null;
    }

    // Tambah item baru
    public Integer createItem(items itm, int currentUser) throws SQLException {
        String query = "INSERT INTO items (item_name, brand_id, sell_price,status, created_at, created_by) VALUES (?, ?, ?, ?, ?, ?)";
        
        PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS); 
        stmt.setString(1, itm.getItemName());
        stmt.setInt(2, itm.getBrandId());
        stmt.setDouble(3, itm.getPrice());
        stmt.setBoolean(4, itm.getStatus());
        stmt.setTimestamp(5, Timestamp.valueOf(itm.getCreatedAt()));
        stmt.setInt(6, currentUser);

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

    // Update item
    public boolean updateItem(items itm, int currentUser, int id) throws SQLException {
        String query = "UPDATE items SET item_name = ?, brand_id = ?, sell_price = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itm.getItemName());
            stmt.setInt(2, itm.getBrandId());
            stmt.setDouble(3, itm.getPrice());
            stmt.setTimestamp(4, Timestamp.valueOf(itm.getUpdatedAt()));
            stmt.setInt(5, currentUser);
            stmt.setInt(6, id);

            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    // soft delete item berdasarkan ID
    public boolean deleteItem(int id, int currentUser) throws SQLException {
        String query = "UPDATE items SET deleted_at = now(), deleted_by = ?, status = false WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, currentUser);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    // Validasi nama item
    public boolean isItemsNameExists(String name, int excludeId) throws SQLException {
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
        }
        return false;
    }

    //delete item by brand
    public boolean deleteItemsByBrand(int brandId)throws SQLException {
        String query = "DELETE FROM items WHERE brand_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, brandId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } 
    }

    // Get items by brand ID
    public List<items> getItemByBrandId(int brandId) throws SQLException {
        String query = "SELECT * FROM items WHERE brand_id = ?";
        List<items> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, brandId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items itm = new items(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getInt("brand_id"),
                        rs.getString("brand_name"),
                        rs.getDouble("sell_price"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        rs.getInt("updated_by"),
                        rs.getBoolean("status"),
                        rs.getInt("deleted_by"),
                        rs.getTimestamp("deteled_at").toLocalDateTime(),
                        0,
                        0,
                        0,
                        0
                    );
                    itm.setId(rs.getInt("id"));
                    list.add(itm);
                }
            }
        }
        return list; 
    }
    // Get option items by brand ID
    public List<optionItems> optionItems(int brandId) throws SQLException {
        String query = "SELECT * FROM items WHERE status = true AND brand_id = ?";
        List<optionItems> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, brandId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    optionItems itm = new optionItems(
                        rs.getInt("id"),
                        rs.getString("item_name")
                    );
                    itm.setId(rs.getInt("id"));
                    list.add(itm);
                }
            }
        }
        return list; 
    }

}
