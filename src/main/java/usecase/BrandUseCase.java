package usecase;

import model.brands;
import repository.brandsRepo;

import java.util.List;

public class BrandUseCase {
    private final brandsRepo brandRepo;

    public BrandUseCase(brandsRepo brandRepo) {
        this.brandRepo = brandRepo;
    }

    public List<brands> getAllBrands() {
        return brandRepo.getAllBrands();
    }

    public boolean createBrand(brands brd) {
        // Contoh validasi sederhana
        if (brd.getBrandName() == null || brd.getBrandName().isEmpty()) {
            System.out.println("Brand name tidak boleh kosong");
            return false;
        }

        return brandRepo.createBrands(brd);
    }

    public boolean updateBrand(brands brd) {
        if (brd.getId() <= 0) {
            System.out.println("ID tidak valid");
            return false;
        }

        return brandRepo.updateBrand(brd);
    }

    public boolean deleteBrand(int id) {
        if (id <= 0) {
            System.out.println("ID tidak valid");
            return false;
        }

        brands brd = new brands(null, id, null, id, null, id); // hanya butuh id untuk hapus
        brd.setId(id);
        return brandRepo.deleteBrand(brd);
    }
}
