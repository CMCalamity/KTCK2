package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.Employee;

public class EmployeeDAO extends BaseDAO<Employee> {

    private static final Logger LOGGER = Logger.getLogger(EmployeeDAO.class.getName());
    private static final String EMPLOYEE_ID_COLUMN_NAME = "id"; // Tên cột ID của nhân viên

    /**
     * Constructor cho EmployeeDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public EmployeeDAO() throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        super();
    }

    /**
     * Lấy tất cả các nhân viên từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng Employee.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<Employee> getAll() throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        List<Employee> employees = new ArrayList<>();
        // Xóa salary_grade khỏi SELECT
        String sql = "SELECT " + EMPLOYEE_ID_COLUMN_NAME + ", first_name, last_name, email, department_id FROM employees";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Constructor của Employee cần được cập nhật để không nhận salary_grade
                Employee emp = new Employee(
                        rs.getInt(EMPLOYEE_ID_COLUMN_NAME),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getInt("department_id")
                );
                employees.add(emp);
            }
            LOGGER.info("Retrieved all employees."); // Thêm log
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách nhân viên: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy danh sách nhân viên", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getAll(): " + e.getMessage(), e);
                // Log lỗi nhưng không ném lại để không che khuất lỗi gốc (nếu có)
            }
        }
        return employees;
    }

    /**
     * Lấy một nhân viên theo ID.
     * @param employeeIdParam ID của nhân viên.
     * @return Đối tượng Employee nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public Employee getById(int employeeIdParam) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        // Xóa salary_grade khỏi SELECT
        String sql = "SELECT " + EMPLOYEE_ID_COLUMN_NAME + ", first_name, last_name, email, department_id FROM employees WHERE " + EMPLOYEE_ID_COLUMN_NAME + " = ?";
        Employee emp = null; // Khởi tạo null
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            LOGGER.log(Level.INFO, "Getting employee by ID: {0}", employeeIdParam);
            pstmt.setInt(1, employeeIdParam);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Constructor của Employee cần được cập nhật
                    emp = new Employee(
                            rs.getInt(EMPLOYEE_ID_COLUMN_NAME),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getInt("department_id")
                    );
                    LOGGER.log(Level.INFO, "Employee found: {0}", emp);
                } else {
                    LOGGER.log(Level.INFO, "Employee not found with ID: {0}", employeeIdParam);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy nhân viên theo ID: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy nhân viên theo ID", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getById(): " + e.getMessage(), e);
            }
        }
        return emp;
    }

    /**
     * Thêm một nhân viên mới vào cơ sở dữ liệu.
     * @param employee Đối tượng Employee cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(Employee employee) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        if (employee == null) {
            throw new IllegalArgumentException("Employee không được null");
        }
        // Xóa salary_grade khỏi INSERT
        String sql = "INSERT INTO employees (first_name, last_name, email, department_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String firstName = employee.getFirstName();
            String lastName = employee.getLastName();
            String email = employee.getEmail();
            int departmentId = employee.getDepartmentId();

            LOGGER.log(Level.INFO, "Adding employee: {0}, {1}, {2}, {3}", new Object[]{firstName, lastName, email, departmentId});

            pstmt.setString(1, firstName != null ? firstName : "");
            pstmt.setString(2, lastName != null ? lastName : "");
            pstmt.setString(3, email != null ? email : "");
            pstmt.setInt(4, departmentId);

            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Employee added successfully");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm nhân viên: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm nhân viên", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau add(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Cập nhật thông tin nhân viên trong cơ sở dữ liệu.
     * @param employee Đối tượng Employee cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(Employee employee) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        if (employee == null) {
            throw new IllegalArgumentException("Employee không được null");
        }
        // Xóa salary_grade khỏi UPDATE
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, department_id = ? WHERE " + EMPLOYEE_ID_COLUMN_NAME + " = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String firstName = employee.getFirstName();
            String lastName = employee.getLastName();
            String email = employee.getEmail();
            int departmentId = employee.getDepartmentId();

            LOGGER.log(Level.INFO, "Updating employee: {0}, {1}, {2}, {3}, ID: {4}",
                    new Object[]{firstName, lastName, email, departmentId, employee.getId()});

            pstmt.setString(1, firstName != null ? firstName : "");
            pstmt.setString(2, lastName != null ? lastName : "");
            pstmt.setString(3, email != null ? email : "");
            pstmt.setInt(4, departmentId);
            pstmt.setInt(5, employee.getId()); // Index của ID giờ là 5 (nếu salary_grade bị loại bỏ)
            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Employee updated successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật nhân viên: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật nhân viên", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau update(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Xóa một nhân viên khỏi cơ sở dữ liệu theo ID.
     * @param employeeId ID của nhân viên cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int employeeId) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "DELETE FROM employees WHERE " + EMPLOYEE_ID_COLUMN_NAME + " = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            LOGGER.log(Level.INFO, "Deleting employee with ID: {0}", employeeId);
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Employee deleted successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa nhân viên: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa nhân viên", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau delete(): " + e.getMessage(), e);
            }
        }
    }
}
