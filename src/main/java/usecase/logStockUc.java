package usecase;

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

import model.LogStock;
import model.stock;
import repository.LogStockRepo;

public class logStockUc {
    private final LogStockRepo logStockRepo;
    public logStockUc(LogStockRepo logStockRepo) {
        this.logStockRepo = logStockRepo;
    }
    
    public boolean createLogStock(LogStock logStock){
        try {
            return logStockRepo.insert(logStock);
        } catch (SQLException ex) {
            Logger.getLogger(logStockUc.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }  

    }

    public List<LogStock> getList(String filter, int rangeDay) {
        try {
            return logStockRepo.getList(filter, rangeDay);
        } catch (SQLException e) {
            System.out.println("Error fetching log stocks: " + e.getMessage());
            return null;
        }
    }

    //export stock
    public boolean exportLogStock(String filter, int rangeDay) {
        try {
            String fileName = "log-stock-list-" + System.currentTimeMillis() + ".xlsx";
            List<LogStock> logstocks = logStockRepo.getList(filter, rangeDay);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Log Stocks");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Activity Name", "Item Name", "Jumlah", "Username","Tanggal"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Isi data
            int rowNum = 1;
            for (LogStock s : logstocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getActivityName());
                row.createCell(2).setCellValue(s.getItemName());
                row.createCell(3).setCellValue(s.getQty());
                row.createCell(4).setCellValue(s.getUsername());
                row.createCell(5).setCellValue(s.getCreatedAt());
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
