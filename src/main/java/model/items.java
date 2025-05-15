package model;

import java.time.LocalDateTime;

public class items {
    private int id;
    private String itemName;
    private int brandId;
    private double price;
    private LocalDateTime createdAt;
    private int createdBy;
    private LocalDateTime updatedAt;
    private int updatedBy;

    // Constructor
    public items() {}
    
    public items(String itemName, int brandId, double price, LocalDateTime createdAt, int createdBy, LocalDateTime updatedAt, int updatedBy) {
        setItemName(itemName);
        setBrandId(brandId);
        setPrice(price);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
    }

    // Getter dan Setter dengan validasi
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("ID tidak boleh negatif");
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama item tidak boleh kosong");
        }
        this.itemName = itemName;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        if (brandId <= 0) throw new IllegalArgumentException("Brand ID harus lebih dari 0");
        this.brandId = brandId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Harga tidak boleh negatif");
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) throw new IllegalArgumentException("CreatedAt tidak boleh null");
        this.createdAt = createdAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        if (createdBy <= 0) throw new IllegalArgumentException("CreatedBy harus lebih dari 0");
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        if (updatedAt == null) throw new IllegalArgumentException("UpdatedAt tidak boleh null");
        this.updatedAt = updatedAt;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        if (updatedBy <= 0) throw new IllegalArgumentException("UpdatedBy harus lebih dari 0");
        this.updatedBy = updatedBy;
    }

    // Validasi keseluruhan data
    public boolean isValid() {
        return itemName != null && !itemName.trim().isEmpty() &&
               brandId > 0 &&
               price >= 0 &&
               createdAt != null &&
               createdBy > 0 &&
               updatedAt != null &&
               updatedBy > 0;
    }

    // toString() untuk debugging
    @Override
    public String toString() {
        return "Items{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", brandId=" + brandId +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", createdBy=" + createdBy +
                ", updatedAt=" + updatedAt +
                ", updatedBy=" + updatedBy +
                '}';
    }
}
