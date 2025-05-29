package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.SalaryGrade;

public class SalaryGradeDAO extends BaseDAO<SalaryGrade> {

    private static final Logger LOGGER = Logger.getLogger(SalaryGradeDAO.class.getName());

    /**
     * Constructor cho SalaryGradeDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public SalaryGradeDAO() throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        super();
    }

    /**
     * Lấy tất cả các bậc lương từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng SalaryGrade.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<SalaryGrade> getAll() throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        List<SalaryGrade> salaryGrades = new ArrayList<>();
        String sql = "SELECT id, grade_name, min_salary, max_salary FROM Salary_Grades";
        try (Statement stmt = conn.createStatement(); // Bỏ cast (Statement) conn - đã được sửa
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                SalaryGrade grade = new SalaryGrade(
                        rs.getString("grade_name"),
                        rs.getInt("id"),
                        rs.getDouble("min_salary"),
                        rs.getDouble("max_salary")
                );
                salaryGrades.add(grade);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách bậc lương: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy danh sách bậc lương", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getAll(): " + e.getMessage(), e);
                // Log lỗi nhưng không ném lại để không che khuất lỗi gốc (nếu có)
            }
        }
        return salaryGrades;
    }

    /**
     * Lấy một bậc lương theo ID.
     * @param id ID của bậc lương.
     * @return Đối tượng SalaryGrade nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public SalaryGrade getById(int id) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "SELECT id, grade_name, min_salary, max_salary FROM Salary_Grades WHERE id = ?";
        SalaryGrade grade = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    grade = new SalaryGrade(
                            rs.getString("grade_name"),
                            rs.getInt("id"),
                            rs.getDouble("min_salary"),
                            rs.getDouble("max_salary")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy bậc lương theo ID: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy bậc lương theo ID", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau getById(): " + e.getMessage(), e);
            }
        }
        return grade;
    }

    /**
     * Thêm một bậc lương mới vào cơ sở dữ liệu.
     * @param salaryGrade Đối tượng SalaryGrade cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(SalaryGrade salaryGrade) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "INSERT INTO Salary_Grades (grade_name, min_salary, max_salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, salaryGrade.getGradeName());
            pstmt.setDouble(2, salaryGrade.getMinSalary());
            pstmt.setDouble(3, salaryGrade.getMaxSalary());
            pstmt.executeUpdate();
            LOGGER.info("Bậc lương đã được thêm: " + salaryGrade.getGradeName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm bậc lương: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm bậc lương", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau add(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Cập nhật thông tin bậc lương trong cơ sở dữ liệu.
     * @param salaryGrade Đối tượng SalaryGrade cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(SalaryGrade salaryGrade) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "UPDATE Salary_Grades SET grade_name = ?, min_salary = ?, max_salary = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, salaryGrade.getGradeName());
            pstmt.setDouble(2, salaryGrade.getMinSalary());
            pstmt.setDouble(3, salaryGrade.getMaxSalary());
            pstmt.setInt(4, salaryGrade.getId());
            pstmt.executeUpdate();
            LOGGER.info("Bậc lương đã được cập nhật: " + salaryGrade.getGradeName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật bậc lương: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật bậc lương", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau update(): " + e.getMessage(), e);
            }
        }
    }

    /**
     * Xóa một bậc lương khỏi cơ sở dữ liệu theo ID.
     * @param id ID của bậc lương cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int id) throws DataAccessException { // Thay đổi throws SQLException thành throws DataAccessException
        String sql = "DELETE FROM Salary_Grades WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Bậc lương có ID " + id + " đã được xóa.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa bậc lương: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa bậc lương", e); // Bọc SQLException vào DataAccessException
        } finally {
            try {
                closeConnection(); // Đảm bảo đóng kết nối
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối sau delete(): " + e.getMessage(), e);
            }
        }
    }
}
