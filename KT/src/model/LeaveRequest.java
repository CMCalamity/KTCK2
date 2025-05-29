package model;

import java.time.LocalDate;

/**
 * Lớp entity đại diện cho một Đơn xin nghỉ phép trong hệ thống.
 * Kế thừa từ BaseEntity để có trường ID.
 */
public class LeaveRequest extends BaseEntity {
    private int employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType;
    private String reason;
    private String status;

    public LeaveRequest() {
    }

    public LeaveRequest(int id, int employeeId, LocalDate startDate, LocalDate endDate, String leaveType, String reason, String status) {
        this.setId(id);
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
        this.reason = reason;
        this.status = status;
    }

    // Getters and setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
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

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + getId() +
                ", employeeId=" + employeeId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", leaveType='" + leaveType + '\'' +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
