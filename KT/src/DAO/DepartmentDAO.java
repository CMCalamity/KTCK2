package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.Department;

/**
 * Lớp DAO (Data Access Object) cho đối tượng Department.
 * Lớp này chịu trách nhiệm tương tác với bảng 'Departments_Types' trong cơ sở dữ liệu.
 * Kế thừa từ BaseDAO để sử dụng các phương thức CRUD chung.
 */
public class DepartmentDAO extends BaseDAO<Department> {

    private static final Logger LOGGER = Logger.getLogger(DepartmentDAO.class.getName());

    /**
     * Constructor cho DepartmentDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public DepartmentDAO() throws DataAccessException {
        super();
    }

    /**
     * Lấy tất cả các phòng ban từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng Department.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<Department> getAll() throws DataAccessException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT id, department_name FROM Departments_Types";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Department department = new Department(
                        rs.getInt("id"),
                        rs.getString("department_name")
                );
                departments.add(department);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy tất cả phòng ban: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy tất cả phòng ban", e);
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getAll(): " + e.getMessage(), e);
            }
        }
        return departments;
    }

    /**
     * Lấy một phòng ban theo ID từ cơ sở dữ liệu.
     * @param id ID của phòng ban cần lấy.
     * @return Đối tượng Department nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public Department getById(int id) throws DataAccessException {
        String sql = "SELECT id, department_name FROM Departments_Types WHERE id = ?";
        Department department = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    department = new Department(
                            rs.getInt("id"),
                            rs.getString("department_name")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy phòng ban theo ID: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy phòng ban theo ID", e);
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getById(): " + e.getMessage(), e);
            }
        }
        return department;
    }

    /**
     * Thêm một phòng ban mới vào cơ sở dữ liệu.
     * @param department Đối tượng Department cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(Department department) throws DataAccessException {
        String sql = "INSERT INTO Departments_Types (department_name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department.getDepartmentName());
            pstmt.executeUpdate();
            LOGGER.info("Phòng ban đã được thêm: " + department.getDepartmentName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm phòng ban: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm phòng ban", e);
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau add(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Cập nhật thông tin phòng ban trong cơ sở dữ liệu.
     * @param department Đối tượng Department cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(Department department) throws DataAccessException {
        String sql = "UPDATE Departments_Types SET department_name = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department.getDepartmentName());
            pstmt.setInt(2, department.getId());
            pstmt.executeUpdate();
            LOGGER.info("Phòng ban đã được cập nhật: " + department.getDepartmentName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật phòng ban: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật phòng ban", e);
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau update(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Xóa một phòng ban khỏi cơ sở dữ liệu theo ID.
     * @param id ID của phòng ban cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int id) throws DataAccessException {
        String sql = "DELETE FROM Departments_Types WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Phòng ban có ID " + id + " đã được xóa.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa phòng ban: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa phòng ban", e);
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau delete(): " + e.getMessage(), e);
            }
        }
    }

	public Department getDepartmentByName(String deptName) {
		// TODO Auto-generated method stub
		return null;
	}
}
