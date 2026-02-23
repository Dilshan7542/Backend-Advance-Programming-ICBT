package lk.icbt.resort.model.entity;

public class Customer {
    private int customerId;
    private String fullName;
    private String phone;
    private String email;
    private String nic;

    public Customer() {}

    public Customer(int customerId, String fullName, String phone, String email, String nic) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.nic = nic;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
}
