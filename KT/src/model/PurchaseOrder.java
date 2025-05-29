package model;

import java.time.LocalDate;

public class PurchaseOrder extends BaseEntity {
    private int vendorId;
    private LocalDate orderDate;
    private double totalAmount;

    public PurchaseOrder() {
    }

    public PurchaseOrder(int id, int vendorId, LocalDate orderDate, double totalAmount) {
        this.setId(id);
        this.vendorId = vendorId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
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
        return "PurchaseOrder{" +
                "id=" + getId() +
                ", vendorId=" + vendorId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                '}';
    }
}