package model;

import java.time.LocalDateTime;

public class items {
    private int id;
    private String itemName;
    private int brandId;
    private String brandName;
    private double price;
    private LocalDateTime createdAt;
    private int createdBy;
    private LocalDateTime updatedAt;
    private int updatedBy;
    private boolean status;
    private int deletedBy;
    private LocalDateTime deletedAt;

    // Constructor
    public items() {}
    
    public items( int id, String itemName, int brandId, String brandName, double price, LocalDateTime createdAt, int createdBy, LocalDateTime updatedAt, int updatedBy, boolean status, int deleteBy, LocalDateTime deletedAt) {
        setId(id);
        setItemName(itemName);
        setBrandId(brandId);
        setBrandName(brandName);
        setPrice(price);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
        setStatus(status);
        setDeletedBy(deletedBy);
        setDeleteAt(deletedAt);
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        if (brandName == null || brandName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama brand tidak boleh kosong");
        }
        this.brandName = brandName;
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
    
    public boolean getStatus(){
        return status;
    }
    
    
    public void setStatus(boolean status){
        this.status=status;
    }
    
    public int getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(int deletedBy) {
        if (deletedBy <= 0) throw new IllegalArgumentException("DeletedBy harus lebih dari 0");
        this.deletedBy = deletedBy;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        if (deletedAt == null) throw new IllegalArgumentException("UpdatedAt tidak boleh null");
        this.deletedAt = deletedAt;
    }
    
    // Validasi keseluruhan data
    public boolean isValid() {
        return itemName != null && !itemName.trim().isEmpty() &&
               brandId > 0 &&
               price >= 0 &&
               createdAt != null &&
               createdBy > 0 &&
               updatedAt != null &&
               updatedBy > 0 &&
               deletedBy >0 &&
               deletedAt != null;
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
                ", deletedBy=" + deletedBy +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
