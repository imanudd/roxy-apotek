package repository;

import model.brands;
import model.optionBrands;
import model.optionSupplier;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class brandsRepo {
    private final Connection conn;

    public brandsRepo(Connection conn) {
        this.conn = conn;
    }

    // List semua brand
    public List<brands> listBrands(String search, int supplierId, int rangeDay, int limit, int offset)
            throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT b.*, s.supplier_name FROM brands b LEFT JOIN suppliers s ON b.supplier_id = s.id");
        List<brands> brandList = new ArrayList<>();
        int paramIndex = 1;

        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasSupplierId = supplierId > 0;
        LocalDateTime from = null;

        boolean hasCondition = false;

        // Bangun query
        if (hasSearch) {
            sql.append(" WHERE b.brand_name ILIKE ?");
            hasCondition = true;
        }

        if (hasSupplierId) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" b.supplier_id = ?");
            hasCondition = true;
        }

        if (rangeDay > 0) {
            from = LocalDateTime.now().minusDays(rangeDay);
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" b.created_at >= ?");
        }

        sql.append(" ORDER BY b.id ASC");

        if (limit > 0) {
            sql.append(" LIMIT ? OFFSET ?");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            if (hasSearch) {
                stmt.setString(paramIndex++, "%" + search.trim() + "%");
            }
            if (hasSupplierId) {
                stmt.setInt(paramIndex++, supplierId);
            }
            if (rangeDay > 0 && from != null) {
                stmt.setTimestamp(paramIndex++, Timestamp.valueOf(from));
            }
            if (limit > 0) {
                stmt.setInt(paramIndex++, limit);
                stmt.setInt(paramIndex++, offset);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                brands b = new brands(
                        rs.getInt("id"),
                        rs.getString("brand_name"),
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getInt("updated_by"),
                        rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                        rs.getInt("deleted_by"),
                        rs.getBoolean("status"));
                brandList.add(b);
            }
        }

        return brandList;
    }

    // CREATE brand
    public boolean insertBrand(brands b, int currentUser) throws SQLException {
        String sql = "INSERT INTO brands (brand_name, supplier_id, created_at, created_by, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getBrandName());
            stmt.setInt(2, b.getSupplierId());
            stmt.setTimestamp(3, Timestamp.valueOf(b.getCreatedAt()));
            stmt.setInt(4, currentUser);
            stmt.setBoolean(5, true);
            return stmt.executeUpdate() > 0;
        }
    }

    // UPDATE brand
    public boolean updateBrand(brands b, int currentUser) throws SQLException {
        String sql = "UPDATE brands SET brand_name = ?, supplier_id = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getBrandName());
            stmt.setInt(2, b.getSupplierId());
            stmt.setTimestamp(3, Timestamp.valueOf(b.getUpdatedAt()));
            stmt.setInt(4, currentUser);
            stmt.setInt(5, b.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    // SOFT DELETE brand
    public boolean softDeleteBrand(int id, int currentUser) throws SQLException {
        String sql = "UPDATE brands SET status = false, deleted_at = now(), deleted_by = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, currentUser);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // SOFT DELETE brand by id supplier
    public boolean deleteByIdSupplier(int id, int currentUser) throws SQLException {
        String sql = "UPDATE brands SET status = false, deleted_at = now(), deleted_by = ? WHERE supplierid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, currentUser);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // get brand by id
    public brands getBrandById(int id) throws SQLException {
        String sql = "SELECT b.*, s.supplier_name FROM brands b LEFT JOIN suppliers s ON b.supplier_id = s.id WHERE b.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new brands(
                        rs.getInt("id"),
                        rs.getString("brand_name"),
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getInt("updated_by"),
                        rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                        rs.getInt("deleted_by"),
                        rs.getBoolean("status"));
            }
        }
        return null;
    }

    public brands getBrandsBySupplier(int id) throws SQLException {
        String sql = "SELECT * FROM brands WHERE supplier_id = ? and status = true";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new brands(
                        rs.getInt("id"),
                        rs.getString("brand_name"),
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getInt("updated_by"),
                        rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                        rs.getInt("deleted_by"),
                        rs.getBoolean("status"));
            }
        }
        return null;
    }

    // validasi brand
    public boolean isBrandNameExists(String name, int excludeId) throws SQLException {
        String query = excludeId > 0
                ? "SELECT COUNT (*) AS count FROM brands WHERE brand_name=? AND id<>?"
                : "SELECT COUNT (*) AS count FROM brands WHERE brand_name=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            if (excludeId > 0)
                ps.setInt(2, excludeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    public List<optionBrands> OptionBrands(int supplierId) throws SQLException {
        String sql;
        sql = "SELECT * FROM brands where status = true and supplier_id = " + supplierId + " order by id asc";

        List<optionBrands> optionbBrands = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                optionBrands ob = new optionBrands(
                        rs.getInt("id"),
                        rs.getString("brand_name"));
                optionbBrands.add(ob);
            }

        }
        return optionbBrands;
    }
}
