package model;

/**
 * Lớp entity đại diện cho một Phòng ban trong hệ thống.
 * Kế thừa từ BaseEntity để có trường ID.
 * Lớp này chỉ chứa các thuộc tính và phương thức getter/setter cho Department.
 * Các thành phần UI và logic xử lý UI đã được di chuyển ra khỏi lớp này.
 */
public class Department extends BaseEntity {
    private String departmentName;

    /**
     * Constructor mặc định.
     */
    public Department() {
        // Constructor mặc định
    }

    /**
     * Constructor đầy đủ tham số để tạo đối tượng Department với ID và tên phòng ban.
     * @param id ID của phòng ban.
     * @param departmentName Tên của phòng ban.
     */
    public Department(int id, String departmentName) {
        this.setId(id); // Set ID từ BaseEntity
        this.departmentName = departmentName;
    }

    /**
     * Constructor chỉ với tên phòng ban, hữu ích khi thêm phòng ban mới
     * mà ID sẽ được tự động tạo bởi cơ sở dữ liệu.
     * @param departmentName Tên của phòng ban.
     */
    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * Lấy tên của phòng ban.
     * @return Tên của phòng ban.
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * Đặt tên cho phòng ban.
     * @param departmentName Tên mới của phòng ban.
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * Trả về biểu diễn chuỗi của đối tượng Department.
     * @return Chuỗi mô tả đối tượng Department.
     */
    @Override
    public String toString() {
        return "Department{" +
               "id=" + getId() +
               ", departmentName='" + departmentName + '\'' +
               '}';
    }
}
