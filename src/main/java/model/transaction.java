package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class transaction {
    private int id;
    private LocalDate date;
    private double grandTotal;
    private int totalItem;
    private LocalDateTime createdAt;
    private int createdBy;
    private LocalDateTime updatedAt;
    private int updatedBy;

    // Constructor
    public transaction() {}
    
    public transaction(LocalDate date, double grandTotal, int totalItem, LocalDateTime createdAt, int createdBy, LocalDateTime updatedAt, int updatedBy) {
        setDate(date);
        setGrandTotal(grandTotal);
        setTotalItem(totalItem);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
    }

    // Getter & Setter dengan validasi
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("ID tidak boleh negatif");
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Tanggal tidak boleh null");
        this.date = date;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        if (grandTotal < 0) throw new IllegalArgumentException("Grand total tidak boleh negatif");
        this.grandTotal = grandTotal;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        if (totalItem < 0) throw new IllegalArgumentException("Total item tidak boleh negatif");
        this.totalItem = totalItem;
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
        return date != null &&
               grandTotal >= 0 &&
               totalItem >= 0 &&
               createdAt != null &&
               updatedAt != null &&
               createdBy > 0 &&
               updatedBy > 0;
    }

    // toString() untuk debug
    @Override
    public String toString() {
        return "Transaction{" +
               "id=" + id +
               ", date=" + date +
               ", grandTotal=" + grandTotal +
               ", totalItem=" + totalItem +
               ", createdAt=" + createdAt +
               ", createdBy=" + createdBy +
               ", updatedAt=" + updatedAt +
               ", updatedBy=" + updatedBy +
               '}';
    }
}
