package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.Vendor;

public class VendorDAO extends BaseDAO<Vendor> {

    private static final Logger LOGGER = Logger.getLogger(VendorDAO.class.getName());

    /**
     * Constructor cho VendorDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public VendorDAO() throws DataAccessException {
        super();
    }

    /**
     * Lấy tất cả các nhà cung cấp từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng Vendor.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<Vendor> getAll() throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        List<Vendor> vendors = new ArrayList<>();
        String sql = "SELECT id, vendor_name, contact_person, phone, email FROM Vendors";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vendor vendor = new Vendor(
                        rs.getInt("id"),
                        rs.getString("vendor_name"),
                        rs.getString("contact_person"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                vendors.add(vendor);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy tất cả nhà cung cấp: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy tất cả nhà cung cấp", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getAll(): " + e.getMessage(), e);
                // Log lỗi nhưng không ném lại để không che khuất lỗi gốc (nếu có)
            }
        }
        return vendors;
    }

    /**
     * Lấy một nhà cung cấp theo ID.
     * @param id ID của nhà cung cấp.
     * @return Đối tượng Vendor nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public Vendor getById(int id) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "SELECT id, vendor_name, contact_person, phone, email FROM Vendors WHERE id = ?";
        Vendor vendor = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    vendor = new Vendor(
                            rs.getInt("id"),
                            rs.getString("vendor_name"),
                            rs.getString("contact_person"),
                            rs.getString("phone"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy nhà cung cấp theo ID: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy nhà cung cấp theo ID", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getById(): " + e.getMessage(), e);
            }
        }
        return vendor;
    }

    /**
     * Thêm một nhà cung cấp mới vào cơ sở dữ liệu.
     * @param vendor Đối tượng Vendor cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(Vendor vendor) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "INSERT INTO Vendors (vendor_name, contact_person, phone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vendor.getVendorName());
            pstmt.setString(2, vendor.getContactPerson());
            pstmt.setString(3, vendor.getPhone());
            pstmt.setString(4, vendor.getEmail());
            pstmt.executeUpdate();
            LOGGER.info("Vendor added: " + vendor.getVendorName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm nhà cung cấp: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm nhà cung cấp", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau add(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Cập nhật thông tin nhà cung cấp trong cơ sở dữ liệu.
     * @param vendor Đối tượng Vendor cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(Vendor vendor) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "UPDATE Vendors SET vendor_name = ?, contact_person = ?, phone = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vendor.getVendorName());
            pstmt.setString(2, vendor.getContactPerson());
            pstmt.setString(3, vendor.getPhone());
            pstmt.setString(4, vendor.getEmail());
            pstmt.setInt(5, vendor.getId());
            pstmt.executeUpdate();
            LOGGER.info("Vendor updated: " + vendor.getVendorName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật nhà cung cấp: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật nhà cung cấp", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau update(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Xóa một nhà cung cấp khỏi cơ sở dữ liệu theo ID.
     * @param id ID của nhà cung cấp cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int id) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "DELETE FROM Vendors WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Vendor deleted with id: " + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa nhà cung cấp: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa nhà cung cấp", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau delete(): " + e.getMessage(), e);
            }
        }
    }
}
