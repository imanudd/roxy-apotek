package usecase;

import model.brands;
import model.optionSupplier;
import model.suppliers;
import repository.supplierRepo;
import repository.brandsRepo;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import helper.currentUser;

public class supplierUc {
    private final supplierRepo supplierRepo;
    private brandsRepo brandsRepo;

    // Constructor
    public supplierUc(supplierRepo supplierRepo, brandsRepo brandsRepo) {
        this.supplierRepo = supplierRepo;
        this.brandsRepo = brandsRepo;
    }

    // Methods get list supplier    
    public List<suppliers> getSuppliersList(String search, int rangeDay) {
        try {
            return supplierRepo.listSupplier(search, rangeDay);
        } catch (SQLException e) {
            System.err.println("List supplier error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Methods create supplier
    public boolean createSupplier(suppliers spl) {
        if (spl.getSupplierName() == null || spl.getSupplierName().trim().isEmpty()) {
            System.out.println("Supplier name is required.");
            return false;
        }
        if (spl.getAddress() == null || spl.getAddress().trim().isEmpty()) {
            System.out.println("Address is required.");
            return false;
        }
        if (spl.getPhone() == null || spl.getPhone().trim().isEmpty()) {
            System.out.println("Phone number is required.");
            return false;
        }

        if (supplierRepo.isSupplierNameExists(spl.getSupplierName(), 0)) {
            System.out.println("Supplier name already exists.");
            return false;
        }

        try {
            spl.setCreatedAt(LocalDateTime.now());
            return supplierRepo.createSupplier(spl, currentUser.getId());
        } catch (SQLException e) {
            System.err.println("Create supplier error: " + e.getMessage());
            return false;
        }
    }

    //update supplier
    public boolean updateSupplier(suppliers spl) {
        try {
            // Ambil data existing
            suppliers existing = supplierRepo.getSupplierById(spl.getId());
            if (existing == null) {
                System.out.println("Supplier not found.");
                return false;
            }

            // Gunakan data existing jika field kosong
            String newName = (spl.getSupplierName() == null || spl.getSupplierName().trim().isEmpty())
                    ? existing.getSupplierName() : spl.getSupplierName();
            String newAddress = (spl.getAddress() == null || spl.getAddress().trim().isEmpty())
                    ? existing.getAddress() : spl.getAddress();
            String newPhone = (spl.getPhone() == null || spl.getPhone().trim().isEmpty())
                    ? existing.getPhone() : spl.getPhone();

            // Validasi nama supplier jika berubah
            if (!newName.equals(existing.getSupplierName()) &&
                supplierRepo.isSupplierNameExists(newName, spl.getId())) {
                System.out.println("Supplier name already exists.");
                return false;
            }

            // Set nilai yang sudah dilengkapi kembali ke object
            spl.setSupplierName(newName);
            spl.setAddress(newAddress);
            spl.setPhone(newPhone);
            spl.setUpdatedAt(LocalDateTime.now());

            return supplierRepo.updateSupplier(spl, currentUser.getId());
        } catch (SQLException e) {
            System.err.println("Update supplier error: " + e.getMessage());
            return false;
        }
    }


    //get supplier by id
    public suppliers getSupplierById(int id) {
        try {
            return supplierRepo.getSupplierById(id);
        } catch (SQLException e) {
            System.err.println("Get supplier by ID error: " + e.getMessage());
            return null;
        }
    }

    // delete supplier
   public boolean DeleteSupplier(int id) {
        try {
            // Cek apakah supplier ada
            suppliers existingSupplier = supplierRepo.getSupplierById(id);
            if (existingSupplier == null) {
                System.out.println("Supplier not found!");
                return false;
            }

            // Cek apakah ada brand yang terkait
            brands existingBrands = brandsRepo.getBrandsBySupplier(id);
            if (existingBrands != null) {
                // Hapus brand jika ada
                boolean isBrandDeleted = brandsRepo.deleteByIdSupplier(id, currentUser.getId());
                if (!isBrandDeleted) {
                    System.out.println("Failed to delete associated brand(s).");
                    return false;
                }
            } else {
                System.out.println("Tidak ada brand terkait, lanjut hapus supplier.");
            }

            // Hapus supplier
            boolean isSupplierDeleted = supplierRepo.deleteSupplier(existingSupplier, currentUser.getId());
            if (!isSupplierDeleted) {
                System.out.println("Failed to delete supplier.");
                return false;
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Soft delete supplier error: " + e.getMessage());
            return false;
        }
    }


    // export supplier
    public boolean exportSupplierList(String search, int rangeDay) {
        try {
            String fileName = "supplier-list-" + System.currentTimeMillis() + ".xlsx";
            List<suppliers> suppliers = supplierRepo.listSupplier(search, rangeDay);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Suppliers");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Supplier Name", "address", "Phone Number", "status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Isi data
            int rowNum = 1;
            for (suppliers s : suppliers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getSupplierName());
                row.createCell(2).setCellValue(s.getAddress());
                row.createCell(3).setCellValue(s.getPhone());
                row.createCell(4).setCellValue(s.getStatus() ? "Aktif" : "Nonaktif");
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

    //option supplier
    public List<optionSupplier> OptionSupplier() {
        try {
            return supplierRepo.OptionSupplier();
        } catch (SQLException e) {
            System.err.println("List supplier error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
