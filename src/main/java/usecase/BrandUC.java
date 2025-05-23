package usecase;

import model.brands;
import model.items;
import model.optionBrands;
import repository.brandsRepo;
import repository.itemsRepo;
import helper.currentUser;

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

public class BrandUC {
    private final brandsRepo brandRepo;
    private final itemsRepo itemRepo;

    public BrandUC(brandsRepo brandRepo, itemsRepo itemRepo) {
        this.brandRepo = brandRepo;
        this.itemRepo = itemRepo;
    }

    // List semua brand
    public List<brands> listBrands(String search, Integer supplierId, Integer rangeDay) {
        try {
            return brandRepo.listBrands(search, supplierId, rangeDay);
        } catch (SQLException e) {
            System.err.println("List brands error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Create brand baru
    public boolean createBrand(brands brd) {
        try {
            // Validasi nama brand
            if (brd.getBrandName() == null || brd.getBrandName().isEmpty()) {
                System.out.println("Brand name tidak boleh kosong");
                return false;
            }

            // Set created info
            brd.setCreatedAt(LocalDateTime.now());

            return brandRepo.insertBrand(brd, currentUser.getId());
        } catch (SQLException e) {
            System.err.println("Create brand error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateBrand(int id, brands brd) {
        System.out.println("ID: " + id);
        try {
            brd.setId(id);
            if (brd.getId() <= 0) {
                System.out.println("ID tidak valid");
                return false;
            }
            
            if (brd.getBrandName().isEmpty()){
                System.out.println("Nama Brand tidak boleh kosong");
                return false;
            }

            brands existingBrand = brandRepo.getBrandById(id);
            if (existingBrand == null) {
                System.out.println("Brand tidak ditemukan");
                return false;
            }
            
            if (existingBrand.getStatus() == false){
                System.out.println("Brand sudah tidak aktif");
                return false;
            }

            // Set waktu update dan user
            brd.setUpdatedAt(LocalDateTime.now());

            return brandRepo.updateBrand(brd, currentUser.getId());
        } catch (SQLException e) {
            System.err.println("Update brand error: " + e.getMessage());
            return false;
        }
    }


    // Delete brand (soft delete)
    public boolean deleteBrand(int id) {
        try {
            if (id <= 0) {
                System.out.println("ID tidak valid");
                return false;
            }

            brands existingBrand = brandRepo.getBrandById(id);
            if (existingBrand == null) {
                System.out.println("Brand tidak ditemukan");
                return false;
            }

            // Ambil semua item yang terkait dengan brand ini
            List<items> itemsWithBrand = itemRepo.getItemByBrandId(id);
            if (itemsWithBrand != null && !itemsWithBrand.isEmpty()) {
                for (items item : itemsWithBrand) {
                    try {
                        boolean deleted = itemRepo.deleteItem(item.getId(), currentUser.getId());
                        if (!deleted) {
                            System.out.println("Gagal menghapus item dengan ID: " + item.getId());
                        }
                    } catch (SQLException e) {
                        System.out.println("Error saat menghapus item ID " + item.getId() + ": " + e.getMessage());
                        // lanjut ke item berikutnya
                    }
                }
            }

            // Hapus brand setelah item dihapus (jika ada)
            boolean brandDeleted = brandRepo.softDeleteBrand(id, currentUser.getId());
            if (!brandDeleted) {
                System.out.println("Gagal menghapus brand dengan ID: " + id);
                return false;
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Delete brand error: " + e.getMessage());
            return false;
        }
    }



    // Get brand by ID
    public brands getBrandById(int id) {
        try {
            return brandRepo.getBrandById(id);
        } catch (SQLException e) {
            System.err.println("Get brand by ID error: " + e.getMessage());
            return null;
        }
    }

    // export brands
    public boolean exportBrandsList(String search, int supplierId, int rangeDay) {
        try {
            String fileName = "brands-list-" + System.currentTimeMillis() + ".xlsx";
            List<brands> brands = brandRepo.listBrands(search, supplierId, rangeDay);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Brands");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID","Brand Name", "Supplier Name", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Isi data
            int rowNum = 1;
            for (brands b : brands) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(b.getId());
                row.createCell(1).setCellValue(b.getBrandName());
                row.createCell(2).setCellValue(b.getSupplierName());
                row.createCell(3).setCellValue(b.getStatus() ? "Aktif" : "Nonaktif");
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

    //option brand
    public List<optionBrands> optionBrands(int supplierId) {
        try {
            return brandRepo.OptionBrands(supplierId);
        } catch (SQLException e) {
            System.err.println("List brands error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
