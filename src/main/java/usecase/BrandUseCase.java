package usecase;

import model.brands;
import repository.brandsRepo;
import helper.currentUser;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BrandUseCase {
    private final brandsRepo brandRepo;

    public BrandUseCase(brandsRepo brandRepo) {
        this.brandRepo = brandRepo;
    }

    // List semua brand
    public List<brands> listBrands(String search) {
        try {
            return brandRepo.listBrands(search);
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
        try {
            // Validasi ID
            if (brd.getId() <= 0) {
                System.out.println("ID tidak valid");
                return false;
            }

            // Ambil data existing berdasarkan ID
            brands existingBrand = brandRepo.getBrandById(id);

            if (existingBrand == null) {
                System.out.println("Brand tidak ditemukan");
                return false;
            }

            // Jika nama brand kosong, gunakan data lama
            if (brd.getBrandName() == null || brd.getBrandName().isEmpty()) {
                brd.setBrandName(existingBrand.getBrandName());
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

            return brandRepo.softDeleteBrand(id, currentUser.getId());
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
}
