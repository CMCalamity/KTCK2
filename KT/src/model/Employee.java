package model;

public class Employee {
    private int id; // Hoặc tên cột ID của bạn, ví dụ employee_id nếu đã sửa
    private String firstName;
    private String lastName;
    private String email;
    private int departmentId;
    // private String salaryGrade; // Xóa trường này

    // Cập nhật constructor để không nhận salaryGrade
    public Employee(int id, String firstName, String lastName, String email, int departmentId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.departmentId = departmentId;
        // this.salaryGrade = salaryGrade; // Xóa dòng này
    }

    // Getters và Setters (xóa getSalaryGrade và setSalaryGrade)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    // public String getSalaryGrade() { // Xóa getter này
    //     return salaryGrade;
    // }

    // public void setSalaryGrade(String salaryGrade) { // Xóa setter này
    //     this.salaryGrade = salaryGrade;
    // }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", departmentId=" + departmentId +
                // ", salaryGrade='" + salaryGrade + '\'' + // Xóa khỏi toString
                '}';
    }
}