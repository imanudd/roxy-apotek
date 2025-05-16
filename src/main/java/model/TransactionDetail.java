/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author User
 */
import java.time.LocalDateTime;
public class TransactionDetail {
    private int id;
    private int transactionId;
    private int itemId;
    private int qty;
    private double price;
    private LocalDateTime createdAt;
    private int createdBy;
    private LocalDateTime updatedAt;
    private int updatedBy;
    
    private String itemName;          // dari table items
    private String transactionDate;   // dari table transactions
    
    // Constructors
    public TransactionDetail() {}

    public TransactionDetail(int id, int transactionId, int itemId, int qty, double price, String itemName, String transactionDate, LocalDateTime createdAt, int createdBy,  LocalDateTime updatedAt, int updatedBy) {
        setId(id);
        setTransactionId(transactionId);
        setItemId(itemId);
        setQty(qty);
        setPrice(price);
        setItemName(itemName);
        setTransactionDate(transactionDate);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
    }

    // Getters and Setters
public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("ID tidak boleh negatif");
        this.id = id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        if (transactionId <= 0) throw new IllegalArgumentException("Transaction ID harus lebih dari 0");
        this.transactionId = transactionId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        if (itemId <= 0) throw new IllegalArgumentException("Item ID harus lebih dari 0");
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Jumlah qty harus lebih dari 0");
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Harga tidak boleh negatif");
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        if (itemName == null || itemName.trim().isEmpty())
            throw new IllegalArgumentException("Nama item tidak boleh kosong");
        this.itemName = itemName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        if (transactionDate == null || transactionDate.trim().isEmpty())
            throw new IllegalArgumentException("Tanggal transaksi tidak boleh kosong");
        this.transactionDate = transactionDate;
    }
        public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
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
        this.updatedAt = updatedAt;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        if (updatedBy <= 0) throw new IllegalArgumentException("UpdatedBy harus lebih dari 0");
        this.updatedBy = updatedBy;
    }

    // ================================
    // Optional Validation Method
    // ================================
    public boolean isValid() {
        return transactionId > 0 &&
               itemId > 0 &&
               qty > 0 &&
               price >= 0 &&
               itemName != null && !itemName.trim().isEmpty() &&
               transactionDate != null && !transactionDate.trim().isEmpty();
    }

    // ================================
    // Optional: toString() for debugging
    // ================================
    @Override
    public String toString() {
        return "TransactionDetail{" +
                "id=" + id +
                ", transactionId=" + transactionId +
                ", itemId=" + itemId +
                ", qty=" + qty +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", createdBy=" + createdBy +
                ", updatedAt=" + updatedAt +
                ", updatedBy=" + updatedBy +
                ", itemName='" + itemName + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                '}';
    }
}
