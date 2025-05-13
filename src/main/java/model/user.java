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

public class user {
    private int id;
    private String email;
    private String password;
    private String username;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int createdBy;
    private int updatedBy;
    private LocalDateTime deletedAt;
    private int deletedBy;
    
    public user(int id,String username, String email, String password, String phoneNumber, LocalDateTime createdAt, int createdBy, LocalDateTime updatedAt, int updatedBy, LocalDateTime deletedAt, int deletedBy){
        this.id=id;
        this.email=email;
        this.password=password;
        this.username=username;
        this.phoneNumber=phoneNumber;
        this.createdAt=createdAt;
        this.createdBy=createdBy;
        this.updatedAt=updatedAt;
        this.updatedBy=updatedBy;
        this.deletedAt=deletedAt;
        this.deletedBy=deletedBy;
    }
    public int getId() {
        return id;
    }
    
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getUserName(){
        return username;
    }
    public String getPhoneNumber(){
        return phoneNumber;
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
    
    public void setPassword(String password) {
        this.password = password;
    }

    public int getDeletedBy() {
        return deletedBy;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}