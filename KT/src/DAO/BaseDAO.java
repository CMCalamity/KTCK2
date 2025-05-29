package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.DatabaseConnection;

public abstract class BaseDAO<T> {

    protected Connection conn;
    private static final Logger LOGGER = Logger.getLogger(BaseDAO.class.getName());

    public BaseDAO() throws DataAccessException { // Thay SQLException bằng DataAccessException
        try {
            conn = DatabaseConnection.getConnection(); // Sử dụng DatabaseConnection
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Không thể thiết lập kết nối đến cơ sở dữ liệu.");
                throw new DataAccessException("Không thể kết nối đến cơ sở dữ liệu.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thiết lập kết nối: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thiết lập kết nối", e);
        }
    }

    public void closeConnection() throws DataAccessException { // Thêm throws DataAccessException
        try {
            DatabaseConnection.closeConnection(conn); // Sử dụng DatabaseConnection.closeConnection()
            conn = null;
            LOGGER.info("Đã đóng kết nối cơ sở dữ liệu.");
        } catch (Exception e) { // Catch tất cả các Exception có thể xảy ra khi đóng kết nối
            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi đóng kết nối", e);
        }
    }

    public abstract List<T> getAll() throws DataAccessException; // Thay SQLException bằng DataAccessException

    public abstract T getById(int id) throws DataAccessException;

    public abstract void add(T t) throws DataAccessException;

    public abstract void update(T t) throws DataAccessException;

    public abstract void delete(int id) throws DataAccessException;

    // Định nghĩa custom exception
    public static class DataAccessException extends Exception {
        public DataAccessException(String message) {
            super(message);
        }

        public DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}