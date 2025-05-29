package model;

import java.time.LocalDate;

/**
 * Lớp entity đại diện cho một Công việc (Task) trong hệ thống quản lý dự án.
 * Kế thừa từ BaseEntity để có trường ID.
 */
public class Task extends BaseEntity {
    private int projectId;
    private String taskName;
    private int assignedTo; // employee_id
    private LocalDate dueDate;
    private String status;

    public Task() {
    }

    public Task(int id, int projectId, String taskName, int assignedTo, LocalDate dueDate, String status) {
        this.setId(id);
        this.projectId = projectId;
        this.taskName = taskName;
        this.assignedTo = assignedTo;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and setters
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", projectId=" + projectId +
                ", taskName='" + taskName + '\'' +
                ", assignedTo=" + assignedTo +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }
}
