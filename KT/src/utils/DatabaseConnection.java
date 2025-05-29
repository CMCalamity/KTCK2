package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp tiện ích để quản lý kết nối đến cơ sở dữ liệu MySQL.
 * Lớp này sử dụng biến môi trường để lấy thông tin đăng nhập cơ sở dữ liệu
 * nhằm tăng cường bảo mật.
 */
public class DatabaseConnection {

    // URL kết nối đến cơ sở dữ liệu MySQL.
    // Đảm bảo tên database là 'erp_system' như trong script SQL của bạn.
    private static final String URL = "jdbc:mysql://localhost:3306/erp_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    /**
     * Lấy một đối tượng Connection đến cơ sở dữ liệu.
     * Thông tin đăng nhập (DB_USER, DB_PASSWORD) được lấy từ biến môi trường.
     * @return Đối tượng Connection đã được thiết lập.
     * @throws SQLException Nếu có lỗi trong quá trình kết nối cơ sở dữ liệu
     * hoặc không tìm thấy biến môi trường.
     */
    public static Connection getConnection() throws SQLException {
        // Lấy thông tin đăng nhập từ biến môi trường
        // Để chạy ứng dụng này, bạn cần thiết lập biến môi trường DB_USER và DB_PASSWORD
        // Ví dụ trên Windows: setx DB_USER "root"
        //                     setx DB_PASSWORD "your_password"
        // Ví dụ trên Linux/macOS: export DB_USER="root"
        //                          export DB_PASSWORD="your_password"
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (user == null || user.isEmpty() || password == null || password.isEmpty()) {
            LOGGER.severe("Thông tin đăng nhập cơ sở dữ liệu (DB_USER, DB_PASSWORD) không được tìm thấy trong biến môi trường. Vui lòng thiết lập chúng.");
            throw new SQLException("Thông tin đăng nhập cơ sở dữ liệu không được tìm thấy trong biến môi trường. Vui lòng thiết lập chúng.");
        }

        try {
            // Tải driver JDBC của MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Thiết lập kết nối
            Connection conn = DriverManager.getConnection(URL, user, password);
            LOGGER.info("Kết nối đến cơ sở dữ liệu thành công.");
            return conn;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Lỗi: MySQL JDBC Driver không tìm thấy! Đảm bảo bạn đã thêm thư viện MySQL Connector/J vào classpath.", e);
            throw new SQLException("MySQL JDBC Driver không tìm thấy!", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kết nối đến cơ sở dữ liệu tại URL: " + URL, e);
            throw e;
        }
    }

    /**
     * Đóng một đối tượng Connection.
     * @param conn Đối tượng Connection cần đóng.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                LOGGER.info("Đã đóng kết nối cơ sở dữ liệu.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối", e);
            }
        }
    }

    /**
     * Phương thức main để kiểm tra kết nối cơ sở dữ liệu.
     * @param args Đối số dòng lệnh.
     */
    public static void main(String[] args) {
        // Thiết lập biến môi trường tạm thời cho mục đích kiểm tra
        // TRONG MÔI TRƯỜNG THỰC TẾ, KHÔNG NÊN LÀM THẾ NÀY!
        // Chỉ để kiểm tra nhanh trong IDE nếu bạn không muốn thiết lập biến môi trường hệ thống.
        // System.setProperty("DB_USER", "root");
        // System.setProperty("DB_PASSWORD", "your_password"); // Thay your_password bằng mật khẩu MySQL của bạn

        try (Connection conn = getConnection()) {
            LOGGER.info("Kiểm tra kết nối: Kết nối đến cơ sở dữ liệu thành công!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Kiểm tra kết nối: Thất bại khi kết nối đến cơ sở dữ liệu.", e);
        }
    }
}
