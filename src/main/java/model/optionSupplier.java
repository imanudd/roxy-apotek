package model;

public class optionSupplier {
    private int id;
    private String supplierName;

    public optionSupplier(int id, String supplierName) {
        this.id = id;
        this.supplierName = supplierName;
    }

    public int getId() {
        return id;
    }

    public String getSupplierName() {
        return supplierName;
    }
}
