package model;

/**
 * Lớp entity đại diện cho một mặt hàng tồn kho trong hệ thống.
 * Kế thừa từ BaseEntity để có trường ID.
 */
public class InventoryItem extends BaseEntity {
    private String itemName;
    private String description;
    private double unitPrice; // Đổi tên từ 'price' thành 'unitPrice' để khớp với DB
    private int quantityInStock; // Đổi tên từ 'quantity' thành 'quantityInStock' để khớp với DB

    public InventoryItem() {
    }

    public InventoryItem(int id, String itemName, String description, double unitPrice, int quantityInStock) {
        this.setId(id);
        this.itemName = itemName;
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
    }

    // Getters and setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() { // Getter cho unitPrice
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) { // Setter cho unitPrice
        this.unitPrice = unitPrice;
    }

    public int getQuantityInStock() { // Getter cho quantityInStock
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) { // Setter cho quantityInStock
        this.quantityInStock = quantityInStock;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + getId() +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantityInStock=" + quantityInStock +
                '}';
    }
}
