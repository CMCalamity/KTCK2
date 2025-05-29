package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.LeaveRequest;
import utils.DatabaseConnection;

/**
 * Lớp DAO (Data Access Object) cho đối tượng LeaveRequest.
 * Lớp này chịu trách nhiệm tương tác với bảng 'Leave_Requests' trong cơ sở dữ liệu.
 * Kế thừa từ BaseDAO để sử dụng các phương thức CRUD chung.
 */
public class LeaveRequestDAO extends BaseDAO<LeaveRequest> {

    private static final Logger LOGGER = Logger.getLogger(LeaveRequestDAO.class.getName());

    /**
     * Constructor cho LeaveRequestDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public LeaveRequestDAO() throws DataAccessException {
        super();
        LOGGER.info("LeaveRequestDAO đã được khởi tạo.");
    }

    /**
     * Lấy tất cả các đơn xin nghỉ phép từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng LeaveRequest.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<LeaveRequest> getAll() throws DataAccessException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String sql = "SELECT id, employee_id, start_date, end_date, leave_type, reason, status FROM Leave_Requests";
        LOGGER.info("Đang thực hiện truy vấn: " + sql);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getString("leave_type"),
                        rs.getString("reason"),
                        rs.getString("status")
                );
                leaveRequests.add(request);
            }
            LOGGER.info("Đã lấy thành công " + leaveRequests.size() + " đơn xin nghỉ phép.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy tất cả đơn xin nghỉ phép: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy tất cả đơn xin nghỉ phép", e);
        } finally {
            closeQuietly();
        }
        return leaveRequests;
    }

    /**
     * Lấy một đơn xin nghỉ phép theo ID từ cơ sở dữ liệu.
     * @param id ID của đơn xin nghỉ phép cần lấy.
     * @return Đối tượng LeaveRequest nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public LeaveRequest getById(int id) throws DataAccessException {
        String sql = "SELECT id, employee_id, start_date, end_date, leave_type, reason, status FROM Leave_Requests WHERE id = ?";
        LeaveRequest request = null;
        LOGGER.info("Đang thực hiện truy vấn getById cho LeaveRequest ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    request = new LeaveRequest(
                            rs.getInt("id"),
                            rs.getInt("employee_id"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getString("leave_type"),
                            rs.getString("reason"),
                            rs.getString("status")
                    );
                    LOGGER.info("Đã tìm thấy LeaveRequest với ID: " + id);
                } else {
                    LOGGER.info("Không tìm thấy LeaveRequest với ID: " + id);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy đơn xin nghỉ phép theo ID: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy đơn xin nghỉ phép theo ID", e);
        } finally {
            closeQuietly();
        }
        return request;
    }

    /**
     * Thêm một đơn xin nghỉ phép mới vào cơ sở dữ liệu.
     * @param leaveRequest Đối tượng LeaveRequest cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(LeaveRequest leaveRequest) throws DataAccessException {
        String sql = "INSERT INTO Leave_Requests (employee_id, start_date, end_date, leave_type, reason, status) VALUES (?, ?, ?, ?, ?, ?)";
        LOGGER.info("Đang thực hiện thêm LeaveRequest: " + leaveRequest.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, leaveRequest.getEmployeeId());
            pstmt.setDate(2, Date.valueOf(leaveRequest.getStartDate()));
            pstmt.setDate(3, Date.valueOf(leaveRequest.getEndDate()));
            pstmt.setString(4, leaveRequest.getLeaveType());
            pstmt.setString(5, leaveRequest.getReason());
            pstmt.setString(6, leaveRequest.getStatus());
            pstmt.executeUpdate();
            LOGGER.info("Đơn xin nghỉ phép đã được thêm cho nhân viên ID: " + leaveRequest.getEmployeeId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi thêm đơn xin nghỉ phép: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm đơn xin nghỉ phép", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Cập nhật thông tin đơn xin nghỉ phép trong cơ sở dữ liệu.
     * @param leaveRequest Đối tượng LeaveRequest cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(LeaveRequest leaveRequest) throws DataAccessException {
        String sql = "UPDATE Leave_Requests SET employee_id = ?, start_date = ?, end_date = ?, leave_type = ?, reason = ?, status = ? WHERE id = ?";
        LOGGER.info("Đang thực hiện cập nhật LeaveRequest: " + leaveRequest.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, leaveRequest.getEmployeeId());
            pstmt.setDate(2, Date.valueOf(leaveRequest.getStartDate()));
            pstmt.setDate(3, Date.valueOf(leaveRequest.getEndDate()));
            pstmt.setString(4, leaveRequest.getLeaveType());
            pstmt.setString(5, leaveRequest.getReason());
            pstmt.setString(6, leaveRequest.getStatus());
            pstmt.setInt(7, leaveRequest.getId());
            pstmt.executeUpdate();
            LOGGER.info("Đơn xin nghỉ phép ID " + leaveRequest.getId() + " đã được cập nhật.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi cập nhật đơn xin nghỉ phép: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật đơn xin nghỉ phép", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Xóa một đơn xin nghỉ phép khỏi cơ sở dữ liệu theo ID.
     * @param id ID của đơn xin nghỉ phép cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int id) throws DataAccessException {
        String sql = "DELETE FROM Leave_Requests WHERE id = ?";
        LOGGER.info("Đang thực hiện xóa LeaveRequest ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Đơn xin nghỉ phép có ID " + id + " đã được xóa.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi xóa đơn xin nghỉ phép: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa đơn xin nghỉ phép", e);
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
            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối trong LeaveRequestDAO: " + e.getMessage(), e);
        }
    }
}
