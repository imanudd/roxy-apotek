package usecase;

import model.suppliers;
import repository.supplierRepo;

import java.util.List;

public class supplierUc {
    private final supplierRepo supplierRepo;

    // Constructor
    public supplierUc(supplierRepo supplierRepo) {
        this.supplierRepo = supplierRepo;
    }

    // Methods get list supplier    
    public List<suppliers> getSuppliersList() {
        return supplierRepo.getList();
    }

    // Methods create supplier
    public boolean createSupplier(suppliers spl) {
        if (supplierRepo.isSupplierNameExists(spl.getSupplierName(), 0)) {
            System.out.println("Supplier name already exists.");
            return false;
        }
        return supplierRepo.createSupplier(spl);
    }

    // Methods update supplier
    public boolean updateSupplier(suppliers spl) {
        if (supplierRepo.isSupplierNameExists(spl.getSupplierName(), spl.getId())) {
            System.out.println("Supplier name already exists.");
            return false;
        }
        return supplierRepo.updateSupplier(spl);
    }

    // Methods delete supplier
    public boolean deleteSupplier(suppliers spl) {
        return supplierRepo.deleteSupplier(spl);
    }
}
