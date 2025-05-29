package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import model.User;
import view.UserDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorMessageLabel;

    private static final Logger LOGGER = Logger.getLogger(LoginFrame.class.getName());

    public LoginFrame() {
        setTitle("Đăng Nhập Hệ Thống ERP");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Kích thước chuẩn cho ô nhập
        Dimension inputSize = new Dimension(200, 30);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("ERP SYSTEM LOGIN");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Username panel
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.setBackground(Color.WHITE);
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setPreferredSize(new Dimension(100, 30));
        usernameField = new JTextField();
        usernameField.setPreferredSize(inputSize);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        // Password panel
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setBackground(Color.WHITE);
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setPreferredSize(new Dimension(100, 30));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(inputSize);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Error message
        errorMessageLabel = new JLabel("", SwingConstants.CENTER);
        errorMessageLabel.setForeground(Color.RED);
        errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(errorMessageLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton loginButton = new JButton("Đăng Nhập");
        loginButton.setBackground(new Color(0, 153, 76));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this::performLogin);

        JButton createUserButton = new JButton("Tạo Tài Khoản");
        createUserButton.setBackground(new Color(0, 102, 204));
        createUserButton.setForeground(Color.WHITE);
        createUserButton.addActionListener(e -> new controller.CreateUserForm(this).setVisible(true));

        buttonPanel.add(loginButton);
        buttonPanel.add(createUserButton);
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Xử lý đăng nhập
     */
    private void performLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        LOGGER.info("Đang cố gắng đăng nhập với Username: " + username);
        errorMessageLabel.setText("");

        if (username.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Tên đăng nhập và mật khẩu không được để trống.");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.authenticate(username, password);

            if (user != null) {
                LOGGER.info("Đăng nhập thành công! Chuyển đến Dashboard.");
                new DashboardFrame(user);
                dispose();
            } else {
                LOGGER.warning("Đăng nhập thất bại: Sai tài khoản hoặc mật khẩu.");
                errorMessageLabel.setText("Sai tài khoản hoặc mật khẩu.");
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi truy cập dữ liệu: " + ex.getMessage(), ex);
            errorMessageLabel.setText("Lỗi truy cập dữ liệu: " + ex.getMessage());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Đã xảy ra lỗi không mong muốn!", ex);
            errorMessageLabel.setText("Lỗi không mong muốn: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
