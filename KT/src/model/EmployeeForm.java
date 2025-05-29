package model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DAO.DepartmentDAO;
import DAO.EmployeeDAO;
import DAO.BaseDAO.DataAccessException;

import java.awt.*;
import java.awt.event.ActionEvent; // Giữ lại import này vì đang dùng lambda expression cho ActionListener
import java.sql.SQLException; // Có thể bỏ nếu không còn catch SQLException trực tiếp
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeForm {
    private JPanel panel;
    private JTable employeeTable;
    private JButton addButton, editButton, deleteButton;
    private DefaultTableModel tableModel;
    private EmployeeDAO employeeDAO;
    private DepartmentDAO departmentDAO;
    private static final Logger LOGGER = Logger.getLogger(EmployeeForm.class.getName());

    public EmployeeForm() {
        initDAOs();
        initComponents();
        loadEmployees();
    }

    /**
     * Khởi tạo các đối tượng DAO.
     * Xử lý DataAccessException nếu có lỗi trong quá trình khởi tạo kết nối.
     */
    private void initDAOs() {
        try {
            employeeDAO = new EmployeeDAO();
            departmentDAO = new DepartmentDAO();
        } catch (DataAccessException e) { // Thay đổi SQLException thành DataAccessException
            LOGGER.log(Level.SEVERE, "Lỗi khởi tạo DAOs: " + e.getMessage(), e);
            showError("Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
            // Có thể thoát ứng dụng hoặc vô hiệu hóa các chức năng nếu không có DB
            // System.exit(1);
        }
    }

    /**
     * Khởi tạo các thành phần giao diện người dùng.
     */
    private void initComponents() {
        panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Quản lý nhân viên", SwingConstants.CENTER), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "Email", "Department"}, 0);
        employeeTable = new JTable(tableModel);
        panel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Thêm");
        editButton = new JButton("Sửa");
        deleteButton = new JButton("Xóa");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
			try {
				openEmployeeDialog(null);
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        editButton.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row == -1) {
                showInfo("Chọn một nhân viên để sửa.");
                return;
            }
            int employeeId = (int) tableModel.getValueAt(row, 0);
            try {
                Employee emp = employeeDAO.getById(employeeId);
                if (emp != null) {
                    openEmployeeDialog(emp);
                } else {
                    showError("Không tìm thấy nhân viên với ID đã chọn.");
                }
            } catch (DataAccessException ex) { // Thay đổi SQLException thành DataAccessException
                LOGGER.log(Level.SEVERE, "Không thể lấy thông tin nhân viên: " + ex.getMessage(), ex);
                showError("Lỗi truy vấn: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> deleteSelectedEmployee());
    }

    /**
     * Tải danh sách nhân viên từ cơ sở dữ liệu và hiển thị lên bảng.
     */
    private void loadEmployees() {
        tableModel.setRowCount(0);
        try {
            List<Employee> employees = employeeDAO.getAll();
            for (Employee emp : employees) {
                String deptName = getDepartmentName(emp.getDepartmentId());
                tableModel.addRow(new Object[]{
                        emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getEmail(), deptName
                });
            }
        } catch (DataAccessException e) { // Thay đổi SQLException thành DataAccessException
            LOGGER.log(Level.SEVERE, "Lỗi khi tải danh sách nhân viên: " + e.getMessage(), e);
            showError("Không thể tải danh sách nhân viên.");
        }
    }

    /**
     * Lấy tên phòng ban dựa trên ID.
     * @param deptId ID của phòng ban.
     * @return Tên phòng ban hoặc "Không rõ" nếu không tìm thấy/có lỗi.
     */
    private String getDepartmentName(int deptId) {
        Department dept = null;
		try {
			dept = departmentDAO.getById(deptId);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dept != null ? dept.getDepartmentName() : "Không rõ";
    }

    /**
     * Mở hộp thoại thêm/sửa nhân viên.
     * @param employee Đối tượng Employee cần sửa (null nếu là thêm mới).
     * @throws DataAccessException 
     */
    private void openEmployeeDialog(Employee employee) throws DataAccessException {
        boolean isEdit = employee != null;
        String title = isEdit ? "Sửa nhân viên" : "Thêm nhân viên";

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel), title, true);
        JPanel dialogPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField firstNameField = new JTextField(isEdit ? employee.getFirstName() : "");
        JTextField lastNameField = new JTextField(isEdit ? employee.getLastName() : "");
        JTextField emailField = new JTextField(isEdit ? employee.getEmail() : "");
        JComboBox<String> departmentComboBox = new JComboBox<>();

        List<Department> departments; // Thay đổi kiểu dữ liệu từ int thành List<Department>
        departments = departmentDAO.getAll(); // Sửa từ getId() sang getAll()
		for (Department dept : departments) {
		    departmentComboBox.addItem(dept.getDepartmentName());
		}
		if (isEdit) {
		    // Đặt giá trị mặc định cho ComboBox khi sửa
		    String currentDeptName = getDepartmentName(employee.getDepartmentId());
		    departmentComboBox.setSelectedItem(currentDeptName);
		}

        dialogPanel.add(new JLabel("First Name:"));
        dialogPanel.add(firstNameField);
        dialogPanel.add(new JLabel("Last Name:"));
        dialogPanel.add(lastNameField);
        dialogPanel.add(new JLabel("Email:"));
        dialogPanel.add(emailField);
        dialogPanel.add(new JLabel("Department:"));
        dialogPanel.add(departmentComboBox);

        JButton saveButton = new JButton("Lưu");
        saveButton.addActionListener(e -> {
            String first = firstNameField.getText();
            String last = lastNameField.getText();
            String email = emailField.getText();
            String deptName = (String) departmentComboBox.getSelectedItem();

            try {
                Department dept = departmentDAO.getDepartmentByName(deptName);
                if (dept == null) {
                    showError("Không tìm thấy phòng ban đã chọn.");
                    return;
                }

                if (isEdit) {
                    employee.setFirstName(first);
                    employee.setLastName(last);
                    employee.setEmail(email);
                    employee.setDepartmentId(dept.getId());
                    employeeDAO.update(employee);
                    showInfo("Cập nhật nhân viên thành công!"); // Thêm thông báo thành công
                } else {
                    Employee newEmp = new Employee(0, first, last, email, dept.getId());
                    employeeDAO.add(newEmp);
                    showInfo("Thêm nhân viên thành công!"); // Thêm thông báo thành công
                }

                loadEmployees(); // Tải lại dữ liệu sau khi thêm/sửa
                dialog.dispose();

            } catch (DataAccessException ex) { // Thay đổi SQLException thành DataAccessException
                LOGGER.log(Level.SEVERE, "Lỗi khi lưu nhân viên: " + ex.getMessage(), ex);
                showError("Lỗi khi lưu nhân viên: " + ex.getMessage());
            } catch (IllegalArgumentException ex) { // Bắt lỗi IllegalArgumentException từ DAO
                LOGGER.log(Level.WARNING, "Dữ liệu nhân viên không hợp lệ: " + ex.getMessage(), ex);
                showError("Dữ liệu không hợp lệ: " + ex.getMessage());
            }
        });

        JButton cancelButton = new JButton("Hủy");
        cancelButton.addActionListener(e -> dialog.dispose());

        dialogPanel.add(saveButton);
        dialogPanel.add(cancelButton);

        dialog.add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(panel);
        dialog.setVisible(true);
    }

    /**
     * Xóa nhân viên được chọn từ bảng.
     */
    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            showInfo("Chọn một nhân viên để xóa.");
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                employeeDAO.delete(employeeId);
                loadEmployees();
                showInfo("Nhân viên đã được xóa.");
            } catch (DataAccessException e) { // Thay đổi SQLException thành DataAccessException
                LOGGER.log(Level.SEVERE, "Lỗi khi xóa nhân viên: " + e.getMessage(), e);
                showError("Không thể xóa nhân viên: " + e.getMessage());
            }
        }
    }

    /**
     * Hiển thị hộp thoại lỗi.
     * @param message Thông báo lỗi.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(panel, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Hiển thị hộp thoại thông tin.
     * @param message Thông báo thông tin.
     */
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(panel, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Trả về JPanel chứa giao diện quản lý nhân viên.
     * @return JPanel của EmployeeForm.
     */
    public JPanel getPanel() {
        return panel;
    }
}
