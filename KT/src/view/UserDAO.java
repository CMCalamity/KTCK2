package view;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;
import utils.DatabaseConnection;

public class UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    private Connection conn;

    public UserDAO() throws SQLException {
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thiết lập kết nối trong UserDAO", e);
            throw e;
        }
    }

    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT id, username, role FROM users WHERE username = ? AND password = ? LIMIT 1";
        User user = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(rs.getInt("id"), rs.getString("username"), null, rs.getString("role"));
                }
            }
        }
        return user;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, role FROM Users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), null, rs.getString("role"));
                users.add(user);
            }
        }
        return users;
    }

    public void createUser(String username, String password, String role) throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        }
    }

    public void updateUser(int id, String username, String role) throws SQLException {
        String sql = "UPDATE users SET username = ?, role = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, role);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    } 

    public Map<String, Integer> getUserRoleCounts() throws SQLException {
        Map<String, Integer> roleCounts = new HashMap<>();
        String sql = "SELECT role, COUNT(*) AS count FROM users GROUP BY role";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String role = rs.getString("role");
                int count = rs.getInt("count");
                roleCounts.put(role, count);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy số lượng người dùng theo vai trò", e);
            throw e;
        }
        return roleCounts;
    }

    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}