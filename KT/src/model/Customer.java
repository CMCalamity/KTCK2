package model;

public class Customer extends BaseEntity {
    private String customerName;
    private String contactName;
    private String address;
    private String phone;
    private String email;

    public Customer() {
    }

    public Customer(int id, String customerName, String contactName, String address, String phone, String email) {
        this.setId(id);
        this.customerName = customerName;
        this.contactName = contactName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    // Getters and setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", customerName='" + customerName + '\'' +
                ", contactName='" + contactName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}