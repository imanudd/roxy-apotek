package usecase;

import model.brands;
import model.items;
import model.optionItems;
import repository.itemsRepo;

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
import model.stock;
import repository.stocksRepo;

public class itemsUc {
    private final itemsRepo itemRepo;
    private final stocksRepo stockRepo;

    public itemsUc(itemsRepo itemRepo, stocksRepo stockRepo) {
        this.itemRepo = itemRepo;
        this.stockRepo = stockRepo;
    }

    //list item
    public List<items> getAllItems(String search, Integer brandId) {
        try{
            return itemRepo.getAllItems(search, brandId);
        }catch (SQLException e){
            System.err.println("List items error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // create item
    public boolean createItem(items itm, stock stc) {
        try{
        if (itm.getItemName() == null || itm.getItemName().isEmpty()) {
            System.out.println("Nama item tidak boleh kosong");
            return false;
        }

        if (itm.getPrice() <= 0) {
            System.out.println("Harga item tidak boleh nol atau negatif");
            return false;
        }

        itm.setCreatedAt(LocalDateTime.now());

        Integer itemId = itemRepo.createItem(itm, currentUser.getId());
       
        stc.setItemId(itemId);
        stc.setCreatedAt(LocalDateTime.now());
        
        boolean success = stockRepo.createStock(stc, currentUser.getId());
        if (!success){
            System.err.println("create stock error : ");
            return false;
        }
        
        }catch (SQLException e){
            System.err.println("Create item error: " + e.getMessage());
            return false;
        }
        
        return true;
    }


    // update item
    public boolean updateItem(items itm, int id) {
        System.out.println("ID: " + id);
        try{
            // Set id ke object itm
            // itm.setId(id);

            if (id <= 0) {
                System.out.println("ID item tidak valid");
                return false;
            }

            items existingiItems = itemRepo.getItemById(id);
            System.out.println(existingiItems);
            if (existingiItems == null) {
                System.out.println("Item tidak ditemukan");
                return false;
            }

            //gunakan existing apabila inputan kosong
            if (itm.getItemName() == null || itm.getItemName().isEmpty()) {
                itm.setItemName(existingiItems.getItemName());
            }

            if (itm.getPrice() <= 0) {
                System.out.println("Harga item tidak boleh nol atau negatif");
                return false;
            }

            itm.setUpdatedAt(LocalDateTime.now());
            return itemRepo.updateItem(itm, currentUser.getId(), id);
        }catch (SQLException e){
            System.err.println("Update item error: " + e.getMessage());
            return false;
        }
    }

    // delete item
    public boolean deleteItem(int id) {
        try{
            if (id <= 0) {
                System.out.println("ID item tidak valid");
                return false;
            }
            return itemRepo.deleteItem(id, currentUser.getId());
        }catch (SQLException e){
            System.err.println("Delete item error: " + e.getMessage());
            return false;
        }
    }

    //get item by id
    public items getItemById (int id){
        try{
            return itemRepo.getItemById(id);
        }catch (SQLException e){
            System.err.println("Get item by ID error: " + e.getMessage());
            return null;
        }
    }

    //export excel
    public boolean exportItemsList(String search) {
        try {
            String fileName = "items-list-" + System.currentTimeMillis() + ".xlsx";
            List<items> items = itemRepo.getAllItems(search, 0);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Items");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID","Item Name", "Brand Name", "Sell Price", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Isi data
            int rowNum = 1;
            for (items i : items) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(i.getId());
                row.createCell(1).setCellValue(i.getItemName());
                row.createCell(2).setCellValue(i.getBrandName());
                row.createCell(3).setCellValue(i.getPrice());
                row.createCell(4).setCellValue(i.getStatus() ? "Aktif" : "Nonaktif");
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

    //option item
    public List<optionItems> optionItems(int brandId) {
        try{
            return itemRepo.optionItems(brandId);
        }catch (SQLException e){
            System.err.println("Option item error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
