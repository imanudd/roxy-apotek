package repository;

import model.user;
import java.sql.*;

public class usersRepo {
    private final Connection conn;

    // Constructor menerima Connection
    public usersRepo(Connection conn) {
        this.conn = conn;
    }

    public boolean insertUser(user u) throws SQLException {
        String sql = "INSERT INTO users(username, email, password, phone_number, created_at, created_by, updated_at, updated_by) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getUserName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getPhoneNumber());
            stmt.setTimestamp(5, Timestamp.valueOf(u.getCreatedAt()));
            stmt.setInt(6, u.getCreatedBy());
            stmt.setTimestamp(7, Timestamp.valueOf(u.getUpdatedAt()));
            stmt.setInt(8, u.getUpdatedBy());
            return stmt.executeUpdate() > 0;
        }
    }

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
                    rs.getInt("deleted_by")
                );
            }
        }
        return null;
    }

    public boolean isEmailExist(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ? AND deleted_at IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        }
    }

    public boolean isPhoneNumberExist(String phoneNumber) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE phone_number = ? AND deleted_at IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phoneNumber);
            return stmt.executeQuery().next();
        }
    }
}
