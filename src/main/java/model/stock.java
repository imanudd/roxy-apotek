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

public class stock {
    private int id;
    private int itemId;
    private int firstStock;
    private int stockIn;
    private int stockOut;
    private int remainingStock;
    private String itemName;
    private LocalDateTime createdAt;
    private int createdBy;
    private LocalDateTime updatedAt;
    private int updatedBy;

    public stock(){}
    
    public stock(int id, String itemName, int itemId, int firstStock, int stockIn, int stockOut, int remainingStock,LocalDateTime createdAt, int createdBy,  LocalDateTime updatedAt, int updatedBy){
        this.id=id;
        this.remainingStock=remainingStock;
        this.itemId=itemId;
        this.firstStock=firstStock;
        this.stockIn=stockIn;
        this.stockOut=stockOut;
        this.itemName=itemName;
        this.createdAt=createdAt;
        this.createdBy=createdBy;
        this.updatedAt=updatedAt;
        this.updatedBy=updatedBy;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getItemName(){
        return itemName;
    }
    public void setItemName(String itemName){
        this.itemName=itemName;
    }    
    public int getItemId(){
        return itemId;
    }
    public void setItemId(int itemId){
        this.itemId=itemId;
    }
    public int getFirstStock(){
        return firstStock;
    }
    public void setFirstStock(int firstStock){
        this.firstStock=firstStock;
    }
    public int getStockIn(){
        return stockIn;
    }
    public void setStockIn(int stockIn){
        this.stockIn=stockIn;
    }
    public int getStockOut(){
        return stockOut;
    }
    public void setStockOut(int stockOut){
        this.stockOut=stockOut;
    }
    public int getRemainingStock(){
        return remainingStock;
    }
    public void setRemainingStock(int remainingStock){
        this.remainingStock=remainingStock;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public int getCreatedBy(){
        return createdBy;
    }
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public int getUpdatedBy(){
        return updatedBy;
    }
    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }
}
