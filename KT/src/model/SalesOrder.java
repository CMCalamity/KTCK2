package model;

import java.time.LocalDate;

public class SalesOrder extends BaseEntity {
    private int customerId;
    private LocalDate orderDate;
    private double totalAmount;

    public SalesOrder() {
    }

    public SalesOrder(int id, int customerId, LocalDate orderDate, double totalAmount) {
        this.setId(id);
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "SalesOrder{" +
                "id=" + getId() +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                '}';
    }
}