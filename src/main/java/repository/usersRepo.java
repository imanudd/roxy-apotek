package repository;

import config.DatabaseConfig;
import model.user;

import java.sql.*;

public class usersRepo {

    Connection conn = DatabaseConfig.connect();
    //create user
    public boolean insertUser(user u) throws SQLException {
        String sql = "INSERT INTO users(username, email, password, phone_number, created_at, created_by, updated_at, updated_by) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getUserName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword()); // Sudah di-hash oleh usecase
            stmt.setString(4, u.getPhoneNumber());
            stmt.setTimestamp(5, Timestamp.valueOf(u.getCreatedAt()));
            stmt.setInt(6, u.getCreatedBy());
            stmt.setTimestamp(7, Timestamp.valueOf(u.getUpdatedAt()));
            stmt.setInt(8, u.getUpdatedBy());
            return stmt.executeUpdate() > 0;
        }
    }
    //Get user by email
    public user findUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new user(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone_number"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("created_by"),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    rs.getInt("updated_by")
                );
            }
        }
        return null;
    }
    //login by email
    public boolean isEmailExist(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        }
    }

    //get user by no hp
    public boolean isPhoneNumberExist(String phoneNumber) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE phone_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phoneNumber);
            return stmt.executeQuery().next();
        }
    }
}
