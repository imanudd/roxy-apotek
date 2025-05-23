package usecase;

import helper.currentUser;
import model.LogStock;
import model.stock;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.stock;
import model.items;
import repository.itemsRepo;
import repository.stocksRepo;

public class stockUc {
    private final stocksRepo stocksRepo;
    private final itemsRepo itemsRepo;

    public stockUc(stocksRepo stocksRepo, itemsRepo itemsRepo) {
        this.stocksRepo = stocksRepo;
        this.itemsRepo = itemsRepo;
    }
    
    //list stock
    public List<stock> getList(String search){
        try {
            return stocksRepo.getList(search);
        } catch (SQLException e) {
            System.out.println("Error fetching stocks: " + e.getMessage());
            return null;
        }
    }
    
    public boolean updateStockIn(stock stc, int currentUserId){
        try {
            return stocksRepo.updateIncreaseStock(stc, currentUserId);
        } catch (SQLException ex) {
            Logger.getLogger(stockUc.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
        
    public boolean updateStock(stock stc, int id){
        return true;
    }
    
    public boolean deleteStock(int id){
        return true;
    }
    //get stock by item id
    public stock getStockByItemId(int itemId){
        try {
            return stocksRepo.getStockById(itemId);
        } catch (SQLException e) {
            System.out.println("Error fetching stocks: " + e.getMessage());
            return null;
        }
    }

    //export stock
    public boolean exportStock(String search){
        try {
            String fileName = "stock-list-" + System.currentTimeMillis() + ".xlsx";
            List<stock> stocks = stocksRepo.getList(search);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Stocks");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Item Name", "first stock", "Stock in", "Stock out", "Remaining stock"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Isi data
            int rowNum = 1;
            for (stock s : stocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getItemName());
                row.createCell(2).setCellValue(s.getFirstStock());
                row.createCell(3).setCellValue(s.getStockIn());
                row.createCell(4).setCellValue(s.getStockOut());
                row.createCell(5).setCellValue(s.getRemainingStock());
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

}
