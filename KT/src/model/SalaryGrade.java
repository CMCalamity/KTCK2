package model;

public class SalaryGrade extends BaseEntity {
    private String gradeName; // Đổi tên biến
    private double minSalary;
    private double maxSalary;

    public SalaryGrade() {
    }

    public SalaryGrade(String gradeName, int id, double minSalary, double maxSalary) {
        this.gradeName = gradeName;
        this.setId(id);
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    public String getGradeName() { // Đổi tên getter
        return gradeName;
    }

    public void setGradeName(String gradeName) { // Đổi tên setter
        this.gradeName = gradeName;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(double minSalary) {
        this.minSalary = minSalary;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(double maxSalary) {
        this.maxSalary = maxSalary;
    }

    @Override
    public String toString() {
        return "SalaryGrade{" +
                "id=" + getId() +
                ", gradeName='" + gradeName + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                '}';
    }
}