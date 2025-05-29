package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.UserDAO;

/**
 * Lớp CreateUserForm tạo một hộp thoại (JDialog) cho phép quản trị viên
 * tạo tài khoản người dùng mới cho ứng dụng.
 */
public class CreateUserForm extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton createButton;
    private JButton cancelButton;
    private JLabel errorMessageLabel;

    private static final Logger LOGGER = Logger.getLogger(CreateUserForm.class.getName());

    /**
     * Constructor cho CreateUserForm.
     *
     * @param parent JFrame cha mà hộp thoại này sẽ hiển thị trên.
     */
    public CreateUserForm(JFrame parent) {
        super(parent, "Create New User", true);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Tên đăng nhập:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Mật khẩu:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Vai trò:"));
        roleComboBox = new JComboBox<>(new String[]{"user", "admin"});
        add(roleComboBox);

        createButton = new JButton("Tạo");
        cancelButton = new JButton("Hủy");
        add(createButton);
        add(cancelButton);

        errorMessageLabel = new JLabel("", SwingConstants.CENTER);
        errorMessageLabel.setForeground(Color.RED);
        add(errorMessageLabel);
        add(new JLabel(""));

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createUser();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    /**
     * Phương thức xử lý logic tạo người dùng mới.
     * Nó lấy thông tin từ các trường nhập liệu, kiểm tra tính hợp lệ cơ bản,
     * và gọi UserDAO để lưu người dùng vào cơ sở dữ liệu.
     */
    private void createUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            errorMessageLabel.setText("Tên đăng nhập và mật khẩu không được để trống.");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO(); // Tạo UserDAO mới
            userDAO.createUser(username, password, role);

            JOptionPane.showMessageDialog(this, "Người dùng đã được tạo thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            LOGGER.info("Người dùng đã được tạo: " + username);

        } catch (SQLException ex) {
            if (ex.getSQLState() != null && ex.getSQLState().startsWith("23")) {
                errorMessageLabel.setText("Lỗi: Tên đăng nhập '" + username + "' đã tồn tại. Vui lòng chọn tên khác.");
            } else {
                errorMessageLabel.setText("Lỗi khi tạo người dùng: " + ex.getMessage());
            }
            LOGGER.log(Level.SEVERE, "Lỗi cơ sở dữ liệu khi tạo người dùng", ex);
        }
    }

    /**
     * Phương thức main để chạy thử hộp thoại CreateUserForm độc lập.
     * (Bạn có thể xóa hoặc comment phần này khi tích hợp vào ứng dụng chính).
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Parent Frame (for testing)");
        frame.setSize(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> new CreateUserForm(frame));
    }
}