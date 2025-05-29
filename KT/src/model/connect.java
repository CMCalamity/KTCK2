package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class connect {
    private static final String DB_USER = "root"; // Nên lấy từ biến môi trường
    private static final String DB_PASSWORD = "951753654"; // Nên lấy từ biến môi trường
    private static final String DB_NAME = "erp_system";
    private static final String MYSQL_SERVER_URL = "jdbc:mysql://localhost:3306/";

    private static final Logger LOGGER = Logger.getLogger(connect.class.getName());

    public static Connection getConnection() throws SQLException {
        try {
            String url = MYSQL_SERVER_URL + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Lỗi: MySQL JDBC Driver không tìm thấy!", e);
            throw new SQLException("MySQL JDBC Driver không tìm thấy!", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi kết nối với database '" + DB_NAME + "'", e);
            throw e;
        }
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                LOGGER.info("Đã đóng kết nối cơ sở dữ liệu.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối", e);
        }
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = getConnection()) {
            LOGGER.info("Kết nối đến cơ sở dữ liệu thành công!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi trong main", e);
        }
    }
}