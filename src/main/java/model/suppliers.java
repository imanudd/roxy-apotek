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

public class suppliers {
    private int id;
    private String supplierName;
    private String address;
    private String phone;
    private LocalDateTime createdAt;
    private int createdBy;
    private LocalDateTime updatedAt;
    private int updatedBy;
    private LocalDateTime deletedAt;
    private int deletedBy;
    private boolean status;

    public suppliers() {
    }
    
    public suppliers(int id,String supplierName, String address, String phone,LocalDateTime createdAt, int createdBy, LocalDateTime updatedAt, int updatedBy, LocalDateTime deletedAt, int deletedBy, boolean status){
        this.id=id;
        this.supplierName=supplierName;
        this.address=address;
        this.phone=phone;
        this.createdAt=createdAt;
        this.createdBy=createdBy;
        this.updatedAt=updatedAt;
        this.updatedBy=updatedBy;
        this.deletedAt=deletedAt;
        this.deletedBy=deletedBy;
        this.status=status;
    }
    public suppliers(Object object, String name, String address2, String phone2, int i, Object object2, int j,
            LocalDateTime now, int userId) {
        //TODO Auto-generated constructor stub
    }

    public int getId(){
        return id;
    }
    public String getSupplierName(){
        return supplierName;
    }
    public String getAddress(){
        return address;
    }
    public String getPhone(){
        return phone;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public int getCreatedBy(){
        return createdBy;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public int getUpdatedBy(){
        return updatedBy;
    }
    public LocalDateTime getDeletedAt(){
        return deletedAt;
    }
    public int getDeletedBy(){
        return deletedBy;
    }
    public boolean getStatus(){
        return status;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setSupplierName(String supplierName){
        this.supplierName=supplierName;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
      public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
    public void setDeletedBy(int deletedBy) {
        this.deletedBy = deletedBy;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
