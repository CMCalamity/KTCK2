package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.Project;
import utils.DatabaseConnection;

/**
 * Lớp DAO (Data Access Object) cho đối tượng Project.
 * Lớp này chịu trách nhiệm tương tác với bảng 'Projects' trong cơ sở dữ liệu.
 * Kế thừa từ BaseDAO để sử dụng các phương thức CRUD chung.
 */
public class ProjectDAO extends BaseDAO<Project> {

    private static final Logger LOGGER = Logger.getLogger(ProjectDAO.class.getName());

    /**
     * Constructor cho ProjectDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public ProjectDAO() throws DataAccessException {
        super();
        LOGGER.info("ProjectDAO đã được khởi tạo.");
    }

    /**
     * Lấy tất cả các dự án từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng Project.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<Project> getAll() throws DataAccessException {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT id, project_name, start_date, end_date, status, budget FROM Projects";
        LOGGER.info("Đang thực hiện truy vấn: " + sql);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Project project = new Project(
                        rs.getInt("id"),
                        rs.getString("project_name"),
                        rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                        rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                        rs.getString("status"),
                        rs.getDouble("budget")
                );
                projects.add(project);
            }
            LOGGER.info("Đã lấy thành công " + projects.size() + " dự án.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy tất cả dự án: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy tất cả dự án", e);
        } finally {
            closeQuietly();
        }
        return projects;
    }

    /**
     * Lấy một dự án theo ID từ cơ sở dữ liệu.
     * @param id ID của dự án cần lấy.
     * @return Đối tượng Project nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public Project getById(int id) throws DataAccessException {
        String sql = "SELECT id, project_name, start_date, end_date, status, budget FROM Projects WHERE id = ?";
        Project project = null;
        LOGGER.info("Đang thực hiện truy vấn getById cho Project ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    project = new Project(
                            rs.getInt("id"),
                            rs.getString("project_name"),
                            rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                            rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                            rs.getString("status"),
                            rs.getDouble("budget")
                    );
                    LOGGER.info("Đã tìm thấy Project với ID: " + id);
                } else {
                    LOGGER.info("Không tìm thấy Project với ID: " + id);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy dự án theo ID: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy dự án theo ID", e);
        } finally {
            closeQuietly();
        }
        return project;
    }

    /**
     * Thêm một dự án mới vào cơ sở dữ liệu.
     * @param project Đối tượng Project cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(Project project) throws DataAccessException {
        String sql = "INSERT INTO Projects (project_name, start_date, end_date, status, budget) VALUES (?, ?, ?, ?, ?)";
        LOGGER.info("Đang thực hiện thêm Project: " + project.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getProjectName());
            pstmt.setDate(2, project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null);
            pstmt.setDate(3, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            pstmt.setString(4, project.getStatus());
            pstmt.setDouble(5, project.getBudget());
            pstmt.executeUpdate();
            LOGGER.info("Dự án đã được thêm: " + project.getProjectName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi thêm dự án: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm dự án", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Cập nhật thông tin dự án trong cơ sở dữ liệu.
     * @param project Đối tượng Project cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(Project project) throws DataAccessException {
        String sql = "UPDATE Projects SET project_name = ?, start_date = ?, end_date = ?, status = ?, budget = ? WHERE id = ?";
        LOGGER.info("Đang thực hiện cập nhật Project: " + project.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getProjectName());
            pstmt.setDate(2, project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null);
            pstmt.setDate(3, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            pstmt.setString(4, project.getStatus());
            pstmt.setDouble(5, project.getBudget());
            pstmt.setInt(6, project.getId());
            pstmt.executeUpdate();
            LOGGER.info("Dự án ID " + project.getId() + " đã được cập nhật.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi cập nhật dự án: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật dự án", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Xóa một dự án khỏi cơ sở dữ liệu theo ID.
     * @param id ID của dự án cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int id) throws DataAccessException {
        String sql = "DELETE FROM Projects WHERE id = ?";
        LOGGER.info("Đang thực hiện xóa Project ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Dự án có ID " + id + " đã được xóa.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi xóa dự án: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa dự án", e);
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
            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối trong ProjectDAO: " + e.getMessage(), e);
        }
    }
}
