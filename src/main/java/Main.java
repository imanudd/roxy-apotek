import config.DatabaseConfig;
import repository.brandsRepo;
import repository.supplierRepo;
import repository.usersRepo;
import ui.Login;
import usecase.supplierUc;
import usecase.userUc;
import model.optionSupplier;
import model.suppliers;
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
        System.out.println("6. Soft Delete User (Terminal)");
        System.out.println("7. Register (Terminal)");
        System.out.println("8. create supplier (Terminal)");
        System.out.println("9. update supplier (Terminal)");
        System.out.println("10. list supplier (Terminal)");
        System.out.println("11. delete supplier (Terminal)");
        System.out.println("12. delete supplier (Terminal)");
        System.out.println("13. export supplier (Terminal)");
        System.out.println("14. option supplier (Terminal)");
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
        }else if (choice.equals("7")) {
            usersRepo userRepo = new usersRepo(conn);
            userUc userUseCase = new userUc(userRepo);

            //register
            user u = new user();
            System.out.println("Masukkan username: ");
            u.setUserName(input.nextLine());
            System.out.println("Masukkan email: ");
            u.setEmail(input.nextLine());
            System.out.println("Masukkan phone number: ");
            u.setPhoneNumber(input.nextLine());
            System.out.println("Masukkan password: ");
            u.setPassword(input.nextLine());
            boolean success = userUseCase.register(u);
            if (success) {
                System.out.println("Registrasi berhasil!");
            } else {
                System.out.println("Registrasi gagal!");
            }
        }else if (choice.equals("8")) {
            supplierRepo supplierRepo = new supplierRepo(conn);
            brandsRepo brandRepo = new brandsRepo(conn);
            supplierUc supplierUseCase = new supplierUc(supplierRepo, brandRepo);

            //register supplier
            suppliers spl = new suppliers();
            System.out.println("Masukkan nama supplier: ");
            spl.setSupplierName(input.nextLine());
            System.out.println("Masukkan address: ");
            spl.setAddress(input.nextLine());
            System.out.println("Masukkan phone number: ");
            spl.setPhone(input.nextLine());
            boolean success = supplierUseCase.createSupplier(spl);
            if (success) {
                System.out.println("Registrasi supplier berhasil!");
            } else {
                System.out.println("Registrasi supplier gagal!");
            }
        }else if (choice.equals("9")) {
            supplierRepo supplierRepo = new supplierRepo(conn);
            brandsRepo brandRepo = new brandsRepo(conn);
            supplierUc supplierUseCase = new supplierUc(supplierRepo, brandRepo);

            //update supplier
            suppliers spl = new suppliers();
            System.out.println("Masukkan ID supplier: ");
            spl.setId(Integer.parseInt(input.nextLine()));
            System.out.println("Masukkan nama supplier: ");
            spl.setSupplierName(input.nextLine());
            System.out.println("Masukkan address: ");
            spl.setAddress(input.nextLine());
            System.out.println("Masukkan phone number: ");
            spl.setPhone(input.nextLine());
            boolean success = supplierUseCase.updateSupplier(spl);
            if (success) {
                System.out.println("Update supplier berhasil!");
            } else {
                System.out.println("Update supplier gagal!");
            }
        }else if (choice.equals("10")) {
            supplierRepo supplierRepo = new supplierRepo(conn);
            brandsRepo brandRepo = new brandsRepo(conn);
            supplierUc supplierUseCase = new supplierUc(supplierRepo, brandRepo);

            //list supplier
            System.out.println("Masukkan nama supplier: ");
            String search = input.nextLine();

            List<suppliers> supplierList = supplierUseCase.getSuppliersList(search);
            if (supplierList.isEmpty()) {
                System.out.println("Tidak ada supplier yang ditemukan.");
            }else {
                for (suppliers supplier : supplierList) {
                    System.out.println("ID: " + supplier.getId() + ", Nama Supplier: " + supplier.getSupplierName() + ", Alamat: " + supplier.getAddress() + ", Nomor Telepon: " + supplier.getPhone());
                }
            }
        }else if (choice.equals("11")) {
            supplierRepo supplierRepo = new supplierRepo(conn);
            brandsRepo brandRepo = new brandsRepo(conn);
            supplierUc supplierUseCase = new supplierUc(supplierRepo, brandRepo);

            // delete supplier
            System.out.println("Masukkan ID supplier: ");
            int id = Integer.parseInt(input.nextLine());
            boolean deleted = supplierUseCase.DeleteSupplier(id);
            if (deleted) {
                System.out.println("Supplier berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus supplier.");
            }
        }else if (choice.equals("12")) {
            supplierRepo supplierRepo = new supplierRepo(conn);
            brandsRepo brandsRepo = new brandsRepo(conn);
            supplierUc supplierUseCase = new supplierUc(supplierRepo, brandsRepo);

            //get supplier by id
            System.out.println("Masukkan ID supplier: ");
            int id = Integer.parseInt(input.nextLine());
            suppliers supplier = supplierUseCase.getSupplierById(id);
            if (supplier != null) {
                System.out.println("ID: " + supplier.getId() + ", Nama Supplier: " + supplier.getSupplierName() + ", Alamat: " + supplier.getAddress() + ", Nomor Telepon: " + supplier.getPhone());
            }else {
                System.out.println("Supplier tidak ditemukan.");
            }
        }else if (choice.equals("13")) {
            supplierRepo supplierRepo = new supplierRepo(conn);
            brandsRepo brandsRepo = new brandsRepo(conn);
            supplierUc supplierUseCase = new supplierUc(supplierRepo, brandsRepo);

            //export list supplier
            System.out.println("Masukkan nama supplier: ");
            String search = input.nextLine();
            supplierUseCase.exportSupplierList(search);

            if (true) {
                System.out.println("Export supplier berhasil!");
            }else {
                System.out.println("Export supplier gagal!");
            }
        }else if (choice.equals("14")) {
            supplierRepo supplierRepo = new supplierRepo(conn);
            brandsRepo brandsRepo = new brandsRepo(conn);
            supplierUc supplierUseCase = new supplierUc(supplierRepo, brandsRepo);

            //option supplier
            List<optionSupplier> optionSupplierList = supplierUseCase.OptionSupplier();
            if (!optionSupplierList.isEmpty()) {
                for (optionSupplier optionSupplier : optionSupplierList) {
                    System.out.println("ID: " + optionSupplier.getId() + ", Nama Supplier: " + optionSupplier.getSupplierName());
                }
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
