package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.SalesOrder;
import utils.DatabaseConnection;

/**
 * Lớp DAO (Data Access Object) cho đối tượng SalesOrder.
 * Lớp này chịu trách nhiệm tương tác với bảng 'Sales_Orders' trong cơ sở dữ liệu.
 * Kế thừa từ BaseDAO để sử dụng các phương thức CRUD chung.
 */
public class SalesOrderDAO extends BaseDAO<SalesOrder> {

    private static final Logger LOGGER = Logger.getLogger(SalesOrderDAO.class.getName());

    /**
     * Constructor cho SalesOrderDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public SalesOrderDAO() throws DataAccessException {
        super();
        LOGGER.info("SalesOrderDAO đã được khởi tạo.");
    }

    /**
     * Lấy tất cả các đơn đặt hàng bán từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng SalesOrder.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<SalesOrder> getAll() throws DataAccessException {
        List<SalesOrder> salesOrders = new ArrayList<>();
        String sql = "SELECT id, customer_id, order_date, total_amount FROM Sales_Orders";
        LOGGER.info("Đang thực hiện truy vấn: " + sql);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                SalesOrder order = new SalesOrder(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getDouble("total_amount")
                );
                salesOrders.add(order);
            }
            LOGGER.info("Đã lấy thành công " + salesOrders.size() + " đơn đặt hàng bán.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy tất cả đơn đặt hàng bán: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy tất cả đơn đặt hàng bán", e);
        } finally {
            closeQuietly();
        }
        return salesOrders;
    }

    /**
     * Lấy một đơn đặt hàng bán theo ID từ cơ sở dữ liệu.
     * @param id ID của đơn đặt hàng bán cần lấy.
     * @return Đối tượng SalesOrder nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public SalesOrder getById(int id) throws DataAccessException {
        String sql = "SELECT id, customer_id, order_date, total_amount FROM Sales_Orders WHERE id = ?";
        SalesOrder order = null;
        LOGGER.info("Đang thực hiện truy vấn getById cho SalesOrder ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    order = new SalesOrder(
                            rs.getInt("id"),
                            rs.getInt("customer_id"),
                            rs.getDate("order_date").toLocalDate(),
                            rs.getDouble("total_amount")
                    );
                    LOGGER.info("Đã tìm thấy SalesOrder với ID: " + id);
                } else {
                    LOGGER.info("Không tìm thấy SalesOrder với ID: " + id);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy đơn đặt hàng bán theo ID: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy đơn đặt hàng bán theo ID", e);
        } finally {
            closeQuietly();
        }
        return order;
    }

    /**
     * Thêm một đơn đặt hàng bán mới vào cơ sở dữ liệu.
     * @param salesOrder Đối tượng SalesOrder cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(SalesOrder salesOrder) throws DataAccessException {
        String sql = "INSERT INTO Sales_Orders (customer_id, order_date, total_amount) VALUES (?, ?, ?)";
        LOGGER.info("Đang thực hiện thêm SalesOrder: " + salesOrder.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, salesOrder.getCustomerId());
            pstmt.setDate(2, Date.valueOf(salesOrder.getOrderDate()));
            pstmt.setDouble(3, salesOrder.getTotalAmount());
            pstmt.executeUpdate();
            LOGGER.info("Đơn đặt hàng bán đã được thêm cho khách hàng ID: " + salesOrder.getCustomerId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi thêm đơn đặt hàng bán: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm đơn đặt hàng bán", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Cập nhật thông tin đơn đặt hàng bán trong cơ sở dữ liệu.
     * @param salesOrder Đối tượng SalesOrder cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(SalesOrder salesOrder) throws DataAccessException {
        String sql = "UPDATE Sales_Orders SET customer_id = ?, order_date = ?, total_amount = ? WHERE id = ?";
        LOGGER.info("Đang thực hiện cập nhật SalesOrder: " + salesOrder.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, salesOrder.getCustomerId());
            pstmt.setDate(2, Date.valueOf(salesOrder.getOrderDate()));
            pstmt.setDouble(3, salesOrder.getTotalAmount());
            pstmt.setInt(4, salesOrder.getId());
            pstmt.executeUpdate();
            LOGGER.info("Đơn đặt hàng bán ID " + salesOrder.getId() + " đã được cập nhật.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi cập nhật đơn đặt hàng bán: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật đơn đặt hàng bán", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Xóa một đơn đặt hàng bán khỏi cơ sở dữ liệu theo ID.
     * @param id ID của đơn đặt hàng bán cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int id) throws DataAccessException {
        String sql = "DELETE FROM Sales_Orders WHERE id = ?";
        LOGGER.info("Đang thực hiện xóa SalesOrder ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Đơn đặt hàng bán có ID " + id + " đã được xóa.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi xóa đơn đặt hàng bán: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa đơn đặt hàng bán", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Đóng kết nối một cách an toàn và ghi log nếu có lỗi.
     */
    private void closeQuietly() {
        try {
            closeConnection();
        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối trong SalesOrderDAO: " + e.getMessage(), e);
        }
    }
}
