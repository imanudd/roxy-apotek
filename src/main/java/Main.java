import config.DatabaseConfig;
import repository.LogStockRepo;
import repository.brandsRepo;
import repository.supplierRepo;
import repository.transactionsDetailRepo;
import repository.transactionsRepo;
import repository.usersRepo;
import repository.itemsRepo;
import repository.stocksRepo;
import ui.Login1;
import usecase.BrandUC;
import usecase.itemsUc;
import usecase.logStockUc;
import usecase.stockUc;
import usecase.supplierUc;
import usecase.transactionUc;
import usecase.userUc;
import model.optionSupplier;
import model.stock;
import model.suppliers;
import model.transaction;
import model.user;
import model.LogStock;
import model.brands;
import model.items;
import model.optionBrands;
import model.optionItems;

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
        System.out.println("15. create brand (Terminal)");
        System.out.println("16. update brand (Terminal)");
        System.out.println("17. delete brand (Terminal)");
        System.out.println("18. get brand by id (Terminal)");
        System.out.println("19. list brand (Terminal)");
        System.out.println("20. option brand (Terminal)");
        System.out.println("21. export brand (Terminal)");
        System.out.println("22. create item (Terminal)");
        System.out.println("23. update item (Terminal)");
        System.out.println("24. delete item (Terminal)");
        System.out.println("25. get item by id (Terminal)");
        System.out.println("26. export item (Terminal)");
        System.out.println("27. list item (Terminal)");
        System.out.println("28. List stock (Terminal)");
        System.out.println("29. get stock by item id (Terminal)");
        System.out.println("30. export stock (Terminal)");
        System.out.println("31. option item (Terminal)");
        System.out.println("32. list log stock (Terminal)");
        System.out.println("33. export log stock (Terminal)");
        System.out.println("34. get list transaksi (Terminal)");
        System.out.println("35. export list transaksi (Terminal)");
        System.out.print("Pilih mode : ");
        String choice = input.nextLine();

        try {
            // Membuat koneksi database hanya sekali
            Connection conn = DatabaseConfig.connect();

            if (choice.equals("1")) {
                // Mode GUI
                javax.swing.SwingUtilities.invokeLater(() -> {
                    try {
                        Login1 login = new Login1(conn);
                        login.setLocationRelativeTo(null); // agar di tengah
                        login.setVisible(true);
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

                System.out.print("Masukkan jumlah hari untuk filter: ");
                String rangeInput = input.nextLine();
                int rangeDay = 0;

                if (rangeInput != null && !rangeInput.trim().isEmpty()) {
                    try {
                        rangeDay = Integer.parseInt(rangeInput.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, menggunakan filter 0 hari.");
                        rangeDay = 0;
                    }
                }

                List<user> users = userUseCase.listUser(search, rangeDay);

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

                System.out.print("Masukkan jumlah hari untuk filter: ");
                int rangeDay = Integer.parseInt(input.nextLine());

                boolean success = userUseCase.exportUserListToExcel(search, rangeDay);
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

            System.out.println("Masukkan range hari: ");
            String rangeInput = input.nextLine();
                int rangeDay = 0;

                if (rangeInput != null && !rangeInput.trim().isEmpty()) {
                    try {
                        rangeDay = Integer.parseInt(rangeInput.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, menggunakan filter 0 hari.");
                        rangeDay = 0;
                    }
                }

            List<suppliers> supplierList = supplierUseCase.getSuppliersList(search, rangeDay );
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

            System.out.println("Masukkan range hari: ");
            String rangeInput = input.nextLine();
                int rangeDay = 0;

                if (rangeInput != null && !rangeInput.trim().isEmpty()) {
                    try {
                        rangeDay = Integer.parseInt(rangeInput.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, menggunakan filter 0 hari.");
                        rangeDay = 0;
                    }
                }
            supplierUseCase.exportSupplierList(search, rangeDay);

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
        }else if (choice.equals("15")) {
            brandsRepo brandsRepo = new brandsRepo(conn);
            itemsRepo itemRepo = new itemsRepo(conn);
            BrandUC brandUseCase = new BrandUC(brandsRepo, itemRepo);

            //create brand
            brands brd = new brands();
            System.out.println("Masukkan nama brand: ");
            brd.setBrandName(input.nextLine());
            System.out.println("Masukkan id supplier: ");
            brd.setSupplierId(Integer.parseInt(input.nextLine()));
            boolean success = brandUseCase.createBrand(brd);
            if (success) {
                System.out.println("Create brand berhasil!");
            }else {
                System.out.println("Create brand gagal!");
            }
        }else if (choice.equals("16")){
            brandsRepo brandsRepo = new brandsRepo(conn);
            itemsRepo itemRepo = new itemsRepo(conn);
            BrandUC brandUseCase = new BrandUC(brandsRepo, itemRepo);

            //update brand
            System.out.println("Masukkan ID brand: ");
            int id = Integer.parseInt(input.nextLine());
            brands brd = new brands();
            System.out.println("Masukkan nama brand: ");
            brd.setBrandName(input.nextLine());
            System.out.println("Masukkan id supplier: ");
            brd.setSupplierId(Integer.parseInt(input.nextLine()));
            boolean success = brandUseCase.updateBrand(id, brd);
            if (success) {
                System.out.println("Update brand berhasil!");
            }else {
                System.out.println("Update brand gagal!");
            }
        }else if (choice.equals("17")) {
            brandsRepo brandsRepo = new brandsRepo(conn);
            itemsRepo itemRepo = new itemsRepo(conn);
            BrandUC brandUseCase = new BrandUC(brandsRepo, itemRepo);

            //delete brand
            System.out.println("Masukkan ID brand: ");
            int id = Integer.parseInt(input.nextLine());
            boolean deleted = brandUseCase.deleteBrand(id);
            if (deleted) {
                System.out.println("Brand berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus brand.");
            }
        }else if (choice.equals("18")) {
            brandsRepo brandsRepo = new brandsRepo(conn);
            itemsRepo itemRepo = new itemsRepo(conn);
            BrandUC brandUseCase = new BrandUC(brandsRepo, itemRepo);

            //get brand by id
            System.out.println("Masukkan ID brand: ");
            int id = Integer.parseInt(input.nextLine());
            brands brand = brandUseCase.getBrandById(id);
            if (brand != null) {
                System.out.printf("ID: " + brand.getId() + ", Nama Brand: " + brand.getBrandName() + ", ID Supplier: " + brand.getSupplierId() +",Supplier: " + brand.getSupplierName(), ", Status: " + brand.getStatus());
            }else {
                System.out.println("Brand tidak ditemukan.");
            }
        } else if (choice.equals("19")){
            brandsRepo brandsRepo = new brandsRepo(conn);
            itemsRepo itemRepo = new itemsRepo(conn);
            BrandUC brandUseCase = new BrandUC(brandsRepo, itemRepo);

            // list brand
            System.out.println("Masukkan nama brand: ");
            String search = input.nextLine();
            System.out.println("Masukkan range hari: ");
            String rangeInput = input.nextLine();
            int rangeDay = 0;

                if (rangeInput != null && !rangeInput.trim().isEmpty()) {
                    try {
                        rangeDay = Integer.parseInt(rangeInput.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, menggunakan filter 0 hari.");
                        rangeDay = 0;
                    }
                }
            List<brands> list = brandUseCase.listBrands(search, 0, rangeDay);
            if (!list.isEmpty()) {
                for (brands brand : list) {
                    System.out.printf("ID: " + brand.getId() + ", Nama Brand: " + brand.getBrandName() + ", ID Supplier: " + brand.getSupplierId() +",Supplier: " + brand.getSupplierName(), ", Status: " + brand.getStatus());
                }
            }
        } else if (choice.equals("20")) {
            brandsRepo brandsRepo = new brandsRepo(conn);
            itemsRepo itemRepo = new itemsRepo(conn);
            BrandUC brandUseCase = new BrandUC(brandsRepo, itemRepo);

            //option 
            System.out.println("Masukkan id supplier: ");
            int supplierId = Integer.parseInt(input.nextLine());
            List<optionBrands> optionBrandsList = brandUseCase.optionBrands(supplierId);
            if (!optionBrandsList.isEmpty()) {
                for (optionBrands optionBrands : optionBrandsList) {
                    System.out.println("ID: " + optionBrands.getId() + ", Nama Brand: " + optionBrands.getBrandName());
                }
            }
        } else if (choice.equals("21")) {
            brandsRepo brandsRepo = new brandsRepo(conn);
            itemsRepo itemRepo = new itemsRepo(conn);
            BrandUC brandUseCase = new BrandUC(brandsRepo, itemRepo);

            //export brand
            System.out.println("Masukkan nama brand: ");
            String search = input.nextLine();
            System.out.println("Masukkan range hari: ");
            String rangeInput = input.nextLine();
            int rangeDay = 0;

                if (rangeInput != null && !rangeInput.trim().isEmpty()) {
                    try {
                        rangeDay = Integer.parseInt(rangeInput.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, menggunakan filter 0 hari.");
                        rangeDay = 0;
                    }
                }
            boolean success = brandUseCase.exportBrandsList(search, 0, rangeDay);
            if (success) {
                System.out.println("Export brand berhasil!");
            }else {
                System.out.println("Export brand gagal!");
            }
        }else if (choice.equals("22")){
            itemsRepo itemsRepo = new itemsRepo(conn);
//            itemsUc itemsUC = new itemsUc(itemsRepo);

            //create item
            items item = new items();
            System.out.println("Masukkan nama item: ");
            item.setItemName(input.nextLine());
            System.out.println("Masukkan harga item: ");
            item.setPrice(Double.parseDouble(input.nextLine()));
            System.out.println("Masukkan id brand: ");
            item.setBrandId(Integer.parseInt(input.nextLine()));
//            boolean success = itemsUC.createItem(item);
//            if (success) {
//                System.out.println("Create item berhasil!");
//            }else {
//                System.out.println("Create item gagal!");
//            }
        }else if (choice.equals("23")){
            itemsRepo itemsRepo = new itemsRepo(conn);
//            itemsUc itemsUC = new itemsUc(itemsRepo);

            //update item
            System.out.println("Masukkan ID item: ");
            int id = Integer.parseInt(input.nextLine());
            items item = new items();
            if (item != null) {
                System.out.println("Masukkan nama item: ");
                item.setItemName(input.nextLine());
                System.out.println("Masukkan harga item: ");
                item.setPrice(Double.parseDouble(input.nextLine()));
                System.out.println("Masukkan id brand: ");
                item.setBrandId(Integer.parseInt(input.nextLine()));
//                boolean success = itemsUC.updateItem(item, id);
//                if (success) {
//                    System.out.println("Update item berhasil!");
//                }else {
//                    System.out.println("Update item gagal!");
//                }
            }else {
                System.out.println("ID item tidak valid!");
        }
        }else if (choice.equals("24")){
            itemsRepo itemsRepo = new itemsRepo(conn);
//            itemsUc itemsUC = new itemsUc(itemsRepo);    

            //delete item
            System.out.println("Masukkan ID item: ");
            int id = Integer.parseInt(input.nextLine());
//            boolean success = itemsUC.deleteItem(id);
//            if (success) {
//                System.out.println("Delete item berhasil!");
//            }else {
//                System.out.println("Delete item gagal!");
//            }
        }else if (choice.equals("25")){
            itemsRepo itemsRepo = new itemsRepo(conn);
//            itemsUc itemsUC = new itemsUc(itemsRepo);

            //get item by id
            System.out.println("Masukkan ID item: ");
            int id = Integer.parseInt(input.nextLine());
//            items item = itemsUC.getItemById(id);
//            if (item != null) {
//                System.out.println("ID: " + item.getId() + ", Nama Item: " + item.getItemName() + ", Harga: " + item.getPrice() + ", ID Brand: " + item.getBrandId() + ", Nama Brand: " + item.getBrandName());
//            }

        }else if (choice.equals("26")){
            itemsRepo itemsRepo = new itemsRepo(conn);
//            itemsUc itemsUC = new itemsUc(itemsRepo);

            //export item
            System.out.println("Masukkan nama item: ");
            String search = input.nextLine();
//            boolean success = itemsUC.exportItemsList(search);
//            if (success) {
//                System.out.println("Export item berhasil!");
//            }else {
//                System.out.println("Export item gagal!");
//            }
        }else if (choice.equals("27")){
            itemsRepo itemsRepo = new itemsRepo(conn);
//            itemsUc itemsUC = new itemsUc(itemsRepo);

            //list items
            System.out.println("masukan nama item: ");
            String search = input.nextLine();
//            List<items> list = itemsUC.getAllItems(search);
//            if (!list.isEmpty()) {
//                for (items item : list) {
//                    System.out.println("ID: " + item.getId() + ", Nama Item: " + item.getItemName() + ", Harga: " + item.getPrice() + ", ID Brand: " + item.getBrandId() + ", Nama Brand: " + item.getBrandName());
//                }
//            }
        
        }else if (choice.equals("28")) {
            stocksRepo stocksRepo = new stocksRepo(conn);
             itemsRepo itemsRepo = new itemsRepo(conn);
            stockUc stockUC = new stockUc(stocksRepo, itemsRepo);

            //list stock 
            System.out.println("Masukkan nama item: ");
            String search = input.nextLine();
            List<stock> list = stockUC.getList(search);
            if (!list.isEmpty()) {
                for (stock stock : list) {
                    System.out.println("ID: " + stock.getId() + ", Nama Item: " + stock.getItemName() + ", Stok: " + stock.getRemainingStock() + ", Stock in: " + stock.getStockIn() + ", Stock out: " + stock.getStockOut());
                }
            }
        }else if (choice.equals("29")){
            stocksRepo stocksRepo = new stocksRepo(conn);
            itemsRepo itemsRepo = new itemsRepo(conn);
            stockUc stockUC = new stockUc(stocksRepo, itemsRepo);

            // get stock by item id
            System.out.println("Masukkan ID item: ");
            int id = Integer.parseInt(input.nextLine());
            stock stock = stockUC.getStockByItemId(id);
            if (stock != null) {
                System.out.println(choice + "id Stock: " + stock.getId() + ", id Item: " + stock.getItemId() + ", Stok: " + stock.getRemainingStock() + ", Stock in: " + stock.getStockIn() + ", Stock out: " + stock.getStockOut());
            }
        }else if (choice.equals("30")){
            stocksRepo stocksRepo = new stocksRepo(conn);
            itemsRepo itemsRepo = new itemsRepo(conn);
            stockUc stockUC = new stockUc(stocksRepo, itemsRepo);

            //export stock
            System.out.println("Masukkan nama item: ");
            String search = input.nextLine();
            boolean success = stockUC.exportStock(search);
            if (success) {
                System.out.println("Export stock berhasil!");
            }
        }else if (choice.equals("31")){
            itemsRepo itemsRepo = new itemsRepo(conn);
//            itemsUc itemsUC = new itemsUc(itemsRepo);

            //option item
            System.out.println("Masukkan ID brand: ");
            int brandId = Integer.parseInt(input.nextLine());
//
//            List<optionItems> list = itemsUC.optionItems(brandId);
//            if (!list.isEmpty()) {
//                for (optionItems item : list) {
//                    System.out.println("ID: " + item.getId() + ", Nama Item: " + item.getItemName());
//                }
//            }
        }else if (choice.equals("32")){
            LogStockRepo logStockRepo = new LogStockRepo(conn);
            logStockUc logStockUC = new logStockUc(logStockRepo);

            //list log stock
            System.out.println("Filter : ");
            String filter = input.nextLine();
            System.out.println("Range day: ");
            String rangeInput = input.nextLine();
            int rangeDay = 0;

                if (rangeInput != null && !rangeInput.trim().isEmpty()) {
                    try {
                        rangeDay = Integer.parseInt(rangeInput.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, menggunakan filter 0 hari.");
                        rangeDay = 0;
                    }
                }
            List<LogStock> list = logStockUC.getList(filter, rangeDay);
            if (!list.isEmpty()) {
                for (LogStock log : list) {
                    System.out.println("ID: " + log.getId() + ", activity name: " + log.getActivityName() + "item_id" + log.getItemId() + "ref_id" + log.getRefId() + "username" + log.getUsername());
                }
            }
        }else if (choice.equals("33")){
            LogStockRepo logStockRepo = new LogStockRepo(conn);
            logStockUc logStockUC = new logStockUc(logStockRepo);   

            //export log stock
            System.out.println("Filter: ");
            String filter = input.nextLine();
            System.out.println("Range day: ");
            String rangeInput = input.nextLine();
            int rangeDay = 0;

                if (rangeInput != null && !rangeInput.trim().isEmpty()) {
                    try {
                        rangeDay = Integer.parseInt(rangeInput.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, menggunakan filter 0 hari.");
                        rangeDay = 0;
                    }
                }
            boolean success = logStockUC.exportLogStock(filter, rangeDay);
            if (success) {
                System.out.println("Export log stock berhasil!");
            }
//            List<optionItems> list = itemsUC.optionItems(brandId);
//            if (!list.isEmpty()) {
//                for (optionItems item : list) {
//                    System.out.println("ID: " + item.getId() + ", Nama Item: " + item.getItemName());
//                }
//            }
        } else if (choice.equals("34")){
            transactionsRepo transactionsRepo = new transactionsRepo(conn);
            LogStockRepo logStockRepo = new LogStockRepo(conn);
            transactionsDetailRepo transactionsDetailRepo = new transactionsDetailRepo(conn);
            stocksRepo stocksRepo = new stocksRepo(conn);
            transactionUc transactionsUc = new transactionUc(transactionsRepo, transactionsDetailRepo, stocksRepo, logStockRepo);

            //list transaction
            System.out.println("Range day: ");
            String rangeInput = input.nextLine();
            int rangeDay = 0;

                if (rangeInput != null && !rangeInput.trim().isEmpty()) {
                    try {
                        rangeDay = Integer.parseInt(rangeInput.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, menggunakan filter 0 hari.");
                        rangeDay = 0;
                    }
                }
            List<transaction> list = transactionsUc.getTransactionList(rangeDay);
            if (!list.isEmpty()) {
                for (transaction trans : list) {
                    System.out.println("ID: " + trans.getId() + ", grand total: " + trans.getGrandTotal() + "total item" + trans.getTotalItem() + "username" + trans.getUsername());
                }
            }
        } else if (choice.equals("35")){
            transactionsRepo transactionsRepo = new transactionsRepo(conn);
            LogStockRepo logStockRepo = new LogStockRepo(conn);
            transactionsDetailRepo transactionsDetailRepo = new transactionsDetailRepo(conn);
            stocksRepo stocksRepo = new stocksRepo(conn);
            transactionUc transactionsUc = new transactionUc(transactionsRepo, transactionsDetailRepo, stocksRepo, logStockRepo);

            //Export list transaction
            System.out.println("Range day: ");
            String rangeInput = input.nextLine();
            int rangeDay = 0;

                if (rangeInput != null && !rangeInput.trim().isEmpty()) {
                    try {
                        rangeDay = Integer.parseInt(rangeInput.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, menggunakan filter 0 hari.");
                        rangeDay = 0;
                    }
                }
            boolean success = transactionsUc.exportTransactionList(rangeDay);
            if (success) {
                System.out.println("Export transaction berhasil!");
            }
            
        } else {
                System.out.println("Pilihan tidak valid!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gagal koneksi ke database!");
        }
    }
}
