package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.BaseDAO.DataAccessException;
import model.Task;
import utils.DatabaseConnection;

/**
 * Lớp DAO (Data Access Object) cho đối tượng Task.
 * Lớp này chịu trách nhiệm tương tác với bảng 'Tasks' trong cơ sở dữ liệu.
 * Kế thừa từ BaseDAO để sử dụng các phương thức CRUD chung.
 */
public class TaskDAO extends BaseDAO<Task> {

    private static final Logger LOGGER = Logger.getLogger(TaskDAO.class.getName());

    /**
     * Constructor cho TaskDAO.
     * Khởi tạo kết nối cơ sở dữ liệu thông qua constructor của lớp cha BaseDAO.
     * @throws DataAccessException Nếu có lỗi trong quá trình thiết lập kết nối.
     */
    public TaskDAO() throws DataAccessException {
        super();
        LOGGER.info("TaskDAO đã được khởi tạo.");
    }

    /**
     * Lấy tất cả các công việc từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng Task.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public List<Task> getAll() throws DataAccessException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, project_id, task_name, assigned_to, due_date, status FROM Tasks";
        LOGGER.info("Đang thực hiện truy vấn: " + sql);
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getInt("project_id"),
                        rs.getString("task_name"),
                        rs.getInt("assigned_to"),
                        rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null,
                        rs.getString("status")
                );
                tasks.add(task);
            }
            LOGGER.info("Đã lấy thành công " + tasks.size() + " công việc.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy tất cả công việc: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy tất cả công việc", e);
        } finally {
            closeQuietly();
        }
        return tasks;
    }

    /**
     * Lấy một công việc theo ID từ cơ sở dữ liệu.
     * @param id ID của công việc cần lấy.
     * @return Đối tượng Task nếu tìm thấy, ngược lại là null.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public Task getById(int id) throws DataAccessException {
        String sql = "SELECT id, project_id, task_name, assigned_to, due_date, status FROM Tasks WHERE id = ?";
        Task task = null;
        LOGGER.info("Đang thực hiện truy vấn getById cho Task ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    task = new Task(
                            rs.getInt("id"),
                            rs.getInt("project_id"),
                            rs.getString("task_name"),
                            rs.getInt("assigned_to"),
                            rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null,
                            rs.getString("status")
                    );
                    LOGGER.info("Đã tìm thấy Task với ID: " + id);
                } else {
                    LOGGER.info("Không tìm thấy Task với ID: " + id);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi lấy công việc theo ID: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi lấy công việc theo ID", e);
        } finally {
            closeQuietly();
        }
        return task;
    }

    /**
     * Thêm một công việc mới vào cơ sở dữ liệu.
     * @param task Đối tượng Task cần thêm.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void add(Task task) throws DataAccessException {
        String sql = "INSERT INTO Tasks (project_id, task_name, assigned_to, due_date, status) VALUES (?, ?, ?, ?, ?)";
        LOGGER.info("Đang thực hiện thêm Task: " + task.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setInt(3, task.getAssignedTo());
            pstmt.setDate(4, task.getDueDate() != null ? Date.valueOf(task.getDueDate()) : null);
            pstmt.setString(5, task.getStatus());
            pstmt.executeUpdate();
            LOGGER.info("Công việc đã được thêm: " + task.getTaskName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi thêm công việc: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi thêm công việc", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Cập nhật thông tin công việc trong cơ sở dữ liệu.
     * @param task Đối tượng Task cần cập nhật.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void update(Task task) throws DataAccessException {
        String sql = "UPDATE Tasks SET project_id = ?, task_name = ?, assigned_to = ?, due_date = ?, status = ? WHERE id = ?";
        LOGGER.info("Đang thực hiện cập nhật Task: " + task.toString() + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setInt(3, task.getAssignedTo());
            pstmt.setDate(4, task.getDueDate() != null ? Date.valueOf(task.getDueDate()) : null);
            pstmt.setString(5, task.getStatus());
            pstmt.setInt(6, task.getId());
            pstmt.executeUpdate();
            LOGGER.info("Công việc ID " + task.getId() + " đã được cập nhật.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi cập nhật công việc: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi cập nhật công việc", e);
        } finally {
            closeQuietly();
        }
    }

    /**
     * Xóa một công việc khỏi cơ sở dữ liệu theo ID.
     * @param id ID của công việc cần xóa.
     * @throws DataAccessException Nếu có lỗi khi truy cập dữ liệu.
     */
    @Override
    public void delete(int id) throws DataAccessException {
        String sql = "DELETE FROM Tasks WHERE id = ?";
        LOGGER.info("Đang thực hiện xóa Task ID: " + id + " SQL: " + sql);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Công việc có ID " + id + " đã được xóa.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi xóa công việc: " + e.getMessage(), e);
            throw new DataAccessException("Lỗi khi xóa công việc", e);
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
            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối trong TaskDAO: " + e.getMessage(), e);
        }
    }
}
