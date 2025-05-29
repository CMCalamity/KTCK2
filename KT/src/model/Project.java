package model;

import java.time.LocalDate;

/**
 * Lớp entity đại diện cho một Dự án trong hệ thống.
 * Kế thừa từ BaseEntity để có trường ID.
 */
public class Project extends BaseEntity {
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private double budget;

    public Project() {
    }

    public Project(int id, String projectName, LocalDate startDate, LocalDate endDate, String status, double budget) {
        this.setId(id);
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.budget = budget;
    }

    // Getters and setters
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + getId() +
                ", projectName='" + projectName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", budget=" + budget +
                '}';
    }
}
