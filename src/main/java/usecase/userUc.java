package usecase;

import model.user;
import repository.usersRepo;
import helper.currentUser;
import helper.validasi;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

public class userUc {
    private final usersRepo userRepo;

    public userUc(usersRepo userRepo) {
        this.userRepo = userRepo;
    }

    //register
    public boolean register(user u) {
        try {
            //validasi format email
            if (!validasi.isValidGmail(u.getEmail())) {
                System.out.println("Email must use the @gmail.com domain!");
                return false;
            }

            //validasi format no hp
            if (!validasi.isValidPhoneNumber(u.getPhoneNumber())) {
                System.out.println("Phone number must be at least 11 digits!");
                return false;
            }

            //validasi email uniq
            if (userRepo.isEmailExist(u.getEmail())) {
                System.out.println("Email already registered!");
                return false;
            }

            //validasi no hp uniq
            if (userRepo.isPhoneNumberExist(u.getPhoneNumber())) {
                System.out.println("Phone number already registered!");
                return false;
            }

            // Hash password
            String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
            u.setPassword(hashedPassword);
            u.setCretedAt(LocalDateTime.now());

            return userRepo.insertUser(u, currentUser.getId());

        } catch (SQLException e) {
            System.err.println("Register error: " + e.getMessage());
            return false;
        }
    }

    //login
    public user login(String email, String password) {
        try {
            user u = userRepo.findUserByEmail(email);
            if (u != null && BCrypt.checkpw(password, u.getPassword())) {
                currentUser.set(u.getId(), u.getUserName());
                return u;
            } else {
                System.out.println("Email or password doesn't match!");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }

    //list user
    public List<user> listUser(String search, int rangeDay) {
        try {
            return userRepo.listUser(search, rangeDay);
        } catch (SQLException e) {
            System.err.println("List user error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    //export list user
    public boolean exportUserListToExcel(String search, int rangeDay) {
        try {
            String fileName = "user-list-" + System.currentTimeMillis() + ".xlsx";
            List<user> users = userRepo.listUser(search, rangeDay);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Users");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Username", "Email", "Phone Number", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Isi data
            int rowNum = 1;
            for (user u : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(u.getId());
                row.createCell(1).setCellValue(u.getUserName());
                row.createCell(2).setCellValue(u.getEmail());
                row.createCell(3).setCellValue(u.getPhoneNumber());
                row.createCell(4).setCellValue(u.isStatus() ? "Aktif" : "Nonaktif"); // Status ditampilkan sebagai teks
            }

            // Autosize kolom
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            System.out.println("Excel berhasil dibuat: " + fileName);
            return true;

        } catch (Exception e) {
            System.err.println("Gagal export Excel: " + e.getMessage());
            return false;
        }
    }

    // Get user by ID
    public user getUserById(int id) {
        try {
            return userRepo.findUserById(id);
        } catch (SQLException e) {
            System.err.println("Get user by ID error: " + e.getMessage());
            return null;
        }
    }

    public boolean updateUser(int id, user u) {
        try {
            // Ambil data user yang sudah ada berdasarkan id
            user existingUser = userRepo.findUserById(id);
            
            if (existingUser == null) {
                System.out.println("User not found!");
                return false;
            }

            // validasi nomor telepon
            if (u.getPhoneNumber() != null && !u.getPhoneNumber().isEmpty()) {
                if (!validasi.isValidPhoneNumber(u.getPhoneNumber())) {
                    System.out.println("Phone number must be at least 11 digits!");
                    return false;
                }

                // hanya validasi unik jika user mengganti nomor telepon
                if (!u.getPhoneNumber().equals(existingUser.getPhoneNumber()) &&
                    userRepo.isPhoneNumberExist(u.getPhoneNumber())) {
                    System.out.println("Phone number already registered by another user!");
                    return false;
                }
            }

            // validasi email
            if (u.getEmail() != null && !u.getEmail().isEmpty()) {
                if (!validasi.isValidGmail(u.getEmail())) {
                    System.out.println("Email is not valid!");
                    return false;
                }

                // hanya validasi unik jika email diubah
                if (!u.getEmail().equals(existingUser.getEmail()) &&
                    userRepo.isEmailExist(u.getEmail())) {
                    System.out.println("Email already registered by another user!");
                    return false;
                }
            }

            // Menggunakan nilai existing jika input kosong
            if (u.getUserName() == null || u.getUserName().isEmpty()) {
                u.setUserName(existingUser.getUserName());
            }

            if (u.getEmail() == null || u.getEmail().isEmpty()) {
                u.setEmail(existingUser.getEmail());
            }

            if (u.getPhoneNumber() == null || u.getPhoneNumber().isEmpty()) {
                u.setPhoneNumber(existingUser.getPhoneNumber());
            }

            // Cek jika password diinput, jika tidak, biarkan password lama
            if (u.getPassword() == null || u.getPassword().isEmpty()) {
                u.setPassword(existingUser.getPassword());
            } else {
                // Hash password baru jika ada perubahan
                String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
                u.setPassword(hashedPassword);
            }

            // Set tanggal update
            u.setUpdatedAt(LocalDateTime.now()); // Set updated time
            u.setUpdatedBy(existingUser.getUpdatedBy()); // Set updated by

            // Update user data
            u.setId(id); // Set id ke user yang akan diupdate
            return userRepo.updateUser(u, currentUser.getId());

        } catch (SQLException e) {
            System.err.println("Update user error: " + e.getMessage());
            return false;
        }
    }

    // Soft delete user
    public boolean softDeleteUser(int id) {
        try {
            user existingUser = userRepo.findUserById(id);

            if (existingUser == null) {
                System.out.println("User not found!");
                return false;
            }

            return userRepo.softDeleteUser(id, currentUser.getId());
        } catch (SQLException e) {
            System.err.println("Soft delete user error: " + e.getMessage());
            return false;
        }
    }

}
