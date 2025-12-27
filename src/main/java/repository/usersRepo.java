package repository;

import model.user;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import helper.currentUser;

public class usersRepo {
    private final Connection conn;

    // Constructor menerima Connection
    public usersRepo(Connection conn) {
        this.conn = conn;
    }

    // create user
    public boolean insertUser(user u, int currentUser) throws SQLException {
        String sql = "INSERT INTO users(username, email, password, phone_number, created_at, created_by, status) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getUserName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getPhoneNumber());
            stmt.setTimestamp(5, Timestamp.valueOf(u.getCreatedAt()));
            stmt.setInt(6, currentUser);
            stmt.setBoolean(7, true); // status aktif saat register

            return stmt.executeUpdate() > 0;
        }
    }

    // find user by email
    public user findUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new user(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
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

    public List<user> listUser(String search, int rangeDay, int limit, int offset) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM users");
        List<user> users = new ArrayList<>();
        int paramIndex = 1;

        LocalDateTime from = null;
        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasWhere = false;

        if (hasSearch) {
            sql.append(" WHERE username ILIKE ?");
            hasWhere = true;
        }

        if (rangeDay > 0) {
            from = LocalDateTime.now().minusDays(rangeDay);
            if (hasWhere) {
                sql.append(" AND");
            } else {
                sql.append(" WHERE");
            }
            sql.append(" created_at >= ?");
        }

        sql.append(" ORDER BY id ASC");

        if (limit > 0) {
            sql.append(" LIMIT ? OFFSET ?");
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            if (hasSearch) {
                stmt.setString(paramIndex++, "%" + search.trim() + "%");
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
                user u = new user(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getInt("updated_by"),
                        rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                        rs.getInt("deleted_by"),
                        rs.getBoolean("status"));
                users.add(u);
            }
        }

        return users;
    }

    // check email
    public boolean isEmailExist(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ? AND deleted_at IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        }
    }

    // check phone
    public boolean isPhoneNumberExist(String phoneNumber) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE phone_number = ? AND deleted_at IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phoneNumber);
            return stmt.executeQuery().next();
        }
    }

    // find user by id
    public user findUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ? and status = true";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new user(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        rs.getInt("created_by"),
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getInt("updated_by"),
                        rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                        rs.getInt("deleted_by"),
                        rs.getBoolean("status"));
            }
        }
        return null; // return null if no user found
    }

    // update user
    public boolean updateUser(user u, int currentUser) throws SQLException {
        String sql = "UPDATE users SET username = ?, email = ?, phone_number = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getUserName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPhoneNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(u.getUpdatedAt()));
            stmt.setInt(5, currentUser);
            stmt.setInt(6, u.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // return true if update was successful
        }
    }

    public boolean softDeleteUser(int id, int currentUser) throws SQLException {

        String sql = "UPDATE users SET status = false, deleted_at = now(), deleted_by = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, currentUser); // perbaikan di sini
            stmt.setInt(2, id); // dan di sini
            return stmt.executeUpdate() > 0;
        }
    }

}
