import config.DatabaseConfig;
import repository.usersRepo;
import ui.Login;
import usecase.userUc;
import model.user;

import javax.swing.*;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== APLIKASI INVENTORY APOTEK ROXY ===");
        System.out.println("1. Buka GUI Login");
        System.out.println("2. Test List User (Terminal)");
        System.out.println("3. Export List User ke PDF (Terminal)");
        System.out.println("4. Get user by ID (Terminal)");
        System.out.println("5. Update User (Terminal)");
        System.out.print("Pilih mode : ");
        String choice = input.nextLine();

        try {
            // Membuat koneksi database hanya sekali
            Connection conn = DatabaseConfig.connect();

            if (choice.equals("1")) {
                // Mode GUI
                javax.swing.SwingUtilities.invokeLater(() -> {
                    try {
                        JFrame frame = new JFrame("LOGIN - INVENTORY APOTEK ROXY");
                        frame.setContentPane(new Login(conn));
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Gagal koneksi ke database!");
                    }
                });

            } else if (choice.equals("2")) {
                // Mode Terminal
                usersRepo userRepo = new usersRepo(conn);
                userUc userUseCase = new userUc(userRepo);

                System.out.print("Masukkan username untuk dicari (kosongkan untuk semua): ");
                String search = input.nextLine();

                List<user> users = userUseCase.listUser(search);

                if (users.isEmpty()) {
                    System.out.println("Tidak ada data user ditemukan.");
                } else {
                    for (user u : users) {
                        System.out.println("ID: " + u.getId());
                        System.out.println("Username: " + u.getUserName());
                        System.out.println("Email: " + u.getEmail());
                        System.out.println("Phone: " + u.getPhoneNumber());
                        System.out.println("------------------------------");
                    }
                }
            }else if (choice.equals("3")) {
                usersRepo userRepo = new usersRepo(conn);
                userUc userUseCase = new userUc(userRepo);

                System.out.print("Masukkan username untuk filter (kosongkan untuk semua): ");
                String search = input.nextLine();

                boolean success = userUseCase.exportUserListToExcel(search);
                if (!success) {
                    System.out.println("Export PDF gagal.");
                }
            }else if(choice.equals("4")){
                usersRepo userRepo = new usersRepo(conn);
                userUc userUseCase = new userUc(userRepo);

                System.out.print("Masukkan ID user: ");
                int id = Integer.parseInt(input.nextLine());

                user user = userUseCase.getUserById(id);

                if (user != null) {
                    System.out.println("ID: " + user.getId());
                    System.out.println("Username: " + user.getUserName());
                    System.out.println("Email: " + user.getEmail());
                    System.out.println("Phone: " + user.getPhoneNumber());
                } else {
                    System.out.println("User dengan ID " + id + " tidak ditemukan.");
                }
            }else if (choice.equals("5")) {
            usersRepo userRepo = new usersRepo(conn);
            userUc userUseCase = new userUc(userRepo);

            // Get the user ID to update
            System.out.print("Masukkan ID user: ");
            int id = Integer.parseInt(input.nextLine());

            // Fetch the existing user details by ID (for updating fields)
            user existingUser = userRepo.findUserById(id); // Assuming you have a method to find user by ID

            if (existingUser == null) {
                System.out.println("User dengan ID " + id + " tidak ditemukan.");
                return;
            }

            // Prompt for the new values or keep existing values if no input is given
            System.out.print("Masukkan username (kosongkan untuk tidak mengubah): ");
            String newUsername = input.nextLine();
            if (!newUsername.trim().isEmpty()) {
                existingUser.setUserName(newUsername);
            }

            System.out.print("Masukkan email (kosongkan untuk tidak mengubah): ");
            String newEmail = input.nextLine();
            if (!newEmail.trim().isEmpty()) {
                existingUser.setEmail(newEmail);
            }

            System.out.print("Masukkan phone number (kosongkan untuk tidak mengubah): ");
            String newPhoneNumber = input.nextLine();
            if (!newPhoneNumber.trim().isEmpty()) {
                existingUser.setPhoneNumber(newPhoneNumber);
            }

            // Update the user in the database
            boolean updated = userUseCase.updateUser(id, existingUser);

            if (updated) {
                System.out.println("User berhasil diupdate.");
            } else {
                System.out.println("Gagal memperbarui user.");
            }
        }else if(choice.equals("6")) {
            usersRepo userRepo = new usersRepo(conn);
            userUc userUseCase = new userUc(userRepo);
            System.out.print("Masukkan ID user: ");
            int id = Integer.parseInt(input.nextLine());
            boolean deleted = userUseCase.softDeleteUser(id);
            if (deleted) {
                System.out.println("User berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus user.");
            }
        }else {
                System.out.println("Pilihan tidak valid!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gagal koneksi ke database!");
        }
    }
}
