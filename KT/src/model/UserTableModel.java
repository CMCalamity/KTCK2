package model;

import model.User; // Đảm bảo import đúng lớp User
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.awt.Component;
import java.util.ArrayList; // Thêm import này

public class UserTableModel extends AbstractTableModel {
    private List<User> users;
    private final String[] columnNames = {"ID", "Tên đăng nhập", "Vai trò"};

    public UserTableModel() {
        this.users = new ArrayList<>();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        fireTableDataChanged(); // Thông báo cho JTable rằng dữ liệu đã thay đổi
    }

    public User getUserAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < users.size()) {
            return users.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getId();
            case 1: return user.getUsername();
            case 2: return user.getRole();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Có thể cho phép chỉnh sửa trực tiếp trên bảng nếu muốn,
        // nhưng thường thì chỉnh sửa sẽ thông qua một form riêng.
        // Ở đây, chúng ta sẽ không cho phép chỉnh sửa trực tiếp.
        return false;
    }

	public Component getComponent() {
		// TODO Auto-generated method stub
		return null;
	}
}
