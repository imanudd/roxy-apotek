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

public class LogStock {
    private int id;
    private String activityName; // "stock_in" atau "stock_out"
    private int itemId;
    private String itemName;
    private String username;    
    private int refId; // supplier_id (stock_in) atau transaction_id (stock_out)
    private int qty;
    private LocalDateTime createdAt;
    private int createdBy;
    private LocalDateTime updatedAt;
    private int updatedBy;

    public LogStock() {}
    
    public LogStock(int id, String activityName, int itemId, String itemName, int refId, String username, int qty, LocalDateTime createdAt, int createdBy, LocalDateTime updatedAt, int updatedBy) {
        this.id = id;
        this.activityName = activityName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.refId = refId;
        this.username = username;
        this.qty = qty;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getRefId() { return refId; }
    public void setRefId(int refId) { this.refId = refId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(int updatedBy) { this.updatedBy = updatedBy; }
}
