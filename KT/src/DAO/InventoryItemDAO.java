package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.InventoryItem;
import utils.DatabaseConnection;

/**
 * Lớp DAO (Data Access Object) cho đối tượng InventoryItem.
 * Lớp này chịu trách nhiệm tương tác với bảng 'Inventory_Items' trong cơ sở dữ liệu.
 * Kế thừa từ BaseDAO để sử dụng các phương thức CRUD chung.
 */
public class InventoryItemDAO extends BaseDAO<InventoryItem> {

    private static final Logger LOGGER = Logger.getLogger(InventoryItemDAO.class.getName());

    /**
     * Constructor cho InventoryItemDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public InventoryItemDAO() throws DataAccessException {
        super();
        LOGGER.info("InventoryItemDAO đã được khởi tạo.");
    }

    /**
     * Lấy tất cả các mặt hàng tồn kho từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng InventoryItem.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<InventoryItem> getAll() throws DataAccessException {
        List<InventoryItem> inventoryItems = new ArrayList<>();
        // Sửa tên cột từ 'price' thành 'unit_price' và 'quantity' thành 'quantity_in_stock'
        String sql = "SELECT id, item_name, description, unit_price, quantity_in_stock FROM Inventory_Items";
        LOGGER.info("Đang thực hiện truy vấn: " + sql);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                InventoryItem item = new InventoryItem(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getString("description"),
                        rs.getDouble("unit_price"), // Lấy từ cột unit_price
                        rs.getInt("quantity_in_stock") // Lấy từ cột quantity_in_stock
                );
                inventoryItems.add(item);
            }
            LOGGER.info("Đã lấy thành công " + inventoryItems.size() + " mặt hàng tồn kho.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy tất cả mặt hàng tồn kho: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy tất cả mặt hàng tồn kho", e);
        } finally {
            closeQuietly();
        }
        return inventoryItems;
    }

    /**
     * Lấy một mặt hàng tồn kho theo ID từ cơ sở dữ liệu.
     * @param id ID của mặt hàng tồn kho cần lấy.
     * @return Đối tượng InventoryItem nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public InventoryItem getById(int id) throws DataAccessException {
        String sql = "SELECT id, item_name, description, unit_price, quantity_in_stock FROM Inventory_Items WHERE id = ?";
        InventoryItem item = null;
        LOGGER.info("Đang thực hiện truy vấn getById cho InventoryItem ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    item = new InventoryItem(
                            rs.getInt("id"),
                            rs.getString("item_name"),
                            rs.getString("description"),
                            rs.getDouble("unit_price"),
                            rs.getInt("quantity_in_stock")
                    );
                    LOGGER.info("Đã tìm thấy InventoryItem với ID: " + id);
                } else {
                    LOGGER.info("Không tìm thấy InventoryItem với ID: " + id);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy mặt hàng tồn kho theo ID: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy mặt hàng tồn kho theo ID", e);
        } finally {
            closeQuietly();
        }
        return item;
    }

    /**
     * Thêm một mặt hàng tồn kho mới vào cơ sở dữ liệu.
     * @param inventoryItem Đối tượng InventoryItem cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(InventoryItem inventoryItem) throws DataAccessException {
        // Sửa tên cột từ 'price' thành 'unit_price' và 'quantity' thành 'quantity_in_stock'
        String sql = "INSERT INTO Inventory_Items (item_name, description, unit_price, quantity_in_stock) VALUES (?, ?, ?, ?)";
        LOGGER.info("Đang thực hiện thêm InventoryItem: " + inventoryItem.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, inventoryItem.getItemName());
            pstmt.setString(2, inventoryItem.getDescription());
            pstmt.setDouble(3, inventoryItem.getUnitPrice()); // Sử dụng getUnitPrice()
            pstmt.setInt(4, inventoryItem.getQuantityInStock()); // Sử dụng getQuantityInStock()
            pstmt.executeUpdate();
            LOGGER.info("Mặt hàng tồn kho đã được thêm: " + inventoryItem.getItemName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi thêm mặt hàng tồn kho: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm mặt hàng tồn kho", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Cập nhật thông tin mặt hàng tồn kho trong cơ sở dữ liệu.
     * @param inventoryItem Đối tượng InventoryItem cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(InventoryItem inventoryItem) throws DataAccessException {
        // Sửa tên cột từ 'price' thành 'unit_price' và 'quantity' thành 'quantity_in_stock'
        String sql = "UPDATE Inventory_Items SET item_name = ?, description = ?, unit_price = ?, quantity_in_stock = ? WHERE id = ?";
        LOGGER.info("Đang thực hiện cập nhật InventoryItem: " + inventoryItem.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, inventoryItem.getItemName());
            pstmt.setString(2, inventoryItem.getDescription());
            pstmt.setDouble(3, inventoryItem.getUnitPrice()); // Sử dụng getUnitPrice()
            pstmt.setInt(4, inventoryItem.getQuantityInStock()); // Sử dụng getQuantityInStock()
            pstmt.setInt(5, inventoryItem.getId());
            pstmt.executeUpdate();
            LOGGER.info("Mặt hàng tồn kho ID " + inventoryItem.getId() + " đã được cập nhật.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi cập nhật mặt hàng tồn kho: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật mặt hàng tồn kho", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Xóa một mặt hàng tồn kho khỏi cơ sở dữ liệu theo ID.
     * @param id ID của mặt hàng tồn kho cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int id) throws DataAccessException {
        String sql = "DELETE FROM Inventory_Items WHERE id = ?";
        LOGGER.info("Đang thực hiện xóa InventoryItem ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Mặt hàng tồn kho có ID " + id + " đã được xóa.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi xóa mặt hàng tồn kho: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa mặt hàng tồn kho", e);
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
            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối trong InventoryItemDAO: " + e.getMessage(), e);
        }
    }
}
