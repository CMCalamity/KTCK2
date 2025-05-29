package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.PurchaseOrder;

public class PurchaseOrderDAO extends BaseDAO<PurchaseOrder> {

    private static final Logger LOGGER = Logger.getLogger(PurchaseOrderDAO.class.getName());

    /**
     * Constructor cho PurchaseOrderDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public PurchaseOrderDAO() throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        super();
    }

    /**
     * Lấy tất cả các đơn đặt hàng mua từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng PurchaseOrder.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<PurchaseOrder> getAll() throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        String sql = "SELECT id, vendor_id, order_date, total_amount FROM Purchase_Orders";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PurchaseOrder po = new PurchaseOrder(
                        rs.getInt("id"),
                        rs.getInt("vendor_id"),
                        rs.getObject("order_date", LocalDate.class),
                        rs.getDouble("total_amount")
                );
                purchaseOrders.add(po);
            }
            LOGGER.info("Retrieved all purchase orders."); // Thêm log
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all purchase orders: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy tất cả đơn đặt hàng mua", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getAll(): " + e.getMessage(), e);
                // Log lỗi nhưng không ném lại để không che khuất lỗi gốc (nếu có)
            }
        }
        return purchaseOrders;
    }

    /**
     * Lấy một đơn đặt hàng mua theo ID.
     * @param id ID của đơn đặt hàng mua.
     * @return Đối tượng PurchaseOrder nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public PurchaseOrder getById(int id) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "SELECT id, vendor_id, order_date, total_amount FROM Purchase_Orders WHERE id = ?";
        PurchaseOrder purchaseOrder = null; // Khởi tạo null
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    purchaseOrder = new PurchaseOrder(
                            rs.getInt("id"),
                            rs.getInt("vendor_id"),
                            rs.getObject("order_date", LocalDate.class),
                            rs.getDouble("total_amount")
                    );
                }
            }
            // Thêm log để biết tìm thấy hay không
            if (purchaseOrder != null) {
                LOGGER.info("Retrieved purchase order with id: " + id);
            } else {
                LOGGER.info("Purchase order with id: " + id + " not found.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting purchase order by id: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy đơn đặt hàng mua theo ID", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getById(): " + e.getMessage(), e);
            }
        }
        return purchaseOrder;
    }

    /**
     * Thêm một đơn đặt hàng mua mới vào cơ sở dữ liệu.
     * @param purchaseOrder Đối tượng PurchaseOrder cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(PurchaseOrder purchaseOrder) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "INSERT INTO Purchase_Orders (vendor_id, order_date, total_amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, purchaseOrder.getVendorId());
            pstmt.setObject(2, purchaseOrder.getOrderDate());
            pstmt.setDouble(3, purchaseOrder.getTotalAmount());
            pstmt.executeUpdate();
            // ID chưa được set khi add, nên log theo vendorId hoặc thông tin khác
            LOGGER.info("Purchase order added for vendor ID: " + purchaseOrder.getVendorId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding purchase order: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm đơn đặt hàng mua", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau add(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Cập nhật thông tin đơn đặt hàng mua trong cơ sở dữ liệu.
     * @param purchaseOrder Đối tượng PurchaseOrder cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(PurchaseOrder purchaseOrder) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "UPDATE Purchase_Orders SET vendor_id = ?, order_date = ?, total_amount = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, purchaseOrder.getVendorId());
            pstmt.setObject(2, purchaseOrder.getOrderDate());
            pstmt.setDouble(3, purchaseOrder.getTotalAmount());
            pstmt.setInt(4, purchaseOrder.getId());
            pstmt.executeUpdate();
            LOGGER.info("Purchase order updated with id: " + purchaseOrder.getId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating purchase order: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật đơn đặt hàng mua", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau update(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Xóa một đơn đặt hàng mua khỏi cơ sở dữ liệu theo ID.
     * @param id ID của đơn đặt hàng mua cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int id) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "DELETE FROM Purchase_Orders WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Purchase order deleted with id: " + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting purchase order: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa đơn đặt hàng mua", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau delete(): " + e.getMessage(), e);
            }
        }
    }
}
