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
    private int firstStock;
    private int stockIn;
    private int stockOut;
    private int remainingStock;

    // Constructor
    public items() {}
    
    public items( int id, String itemName, int brandId, String brandName, double price, LocalDateTime createdAt, int createdBy, LocalDateTime updatedAt, int updatedBy, boolean status, int deleteBy, LocalDateTime deletedAt,int firstStock,int stockIn,int stockOut,int remainingStock) {
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
        setFirstStock(firstStock);
        setStockIn(stockIn);
        setStockOut(stockOut);
        setRemainingStock(remainingStock);
    }

    // Getter dan Setter dengan validasi
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
        this.deletedBy = deletedBy;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deletedAt = deletedAt;
    }
    
    public int getFirstStock(){
        return firstStock;
    }
    
    public void setFirstStock(int firstStock){
        this.firstStock = firstStock;
    }
    
    public int getStockIn(){
        return stockIn;
    }
    
    public void setStockIn(int stockIn){
        this.stockIn = stockIn;
    }
    
    public int getStockOut(){
        return stockOut;
    }
    
    public void setStockOut(int stockOut){
        this.stockOut = stockOut;
    }
    
    public int getRemainingStock(){
        return remainingStock;
    }
    
    public void setRemainingStock(int remainingStock){
        this.remainingStock = remainingStock;
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
}
