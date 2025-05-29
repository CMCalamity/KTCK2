
package model;

public class Vendor extends BaseEntity {
    private String vendorName;
    private String contactPerson;
    private String phone;
    private String email;

    public Vendor() {
    }

    public Vendor(int id, String vendorName, String contactPerson, String phone, String email) {
        this.setId(id);
        this.vendorName = vendorName;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
    }

    // Getters and setters
    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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
        return "Vendor{" +
                "id=" + getId() +
                ", vendorName='" + vendorName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}