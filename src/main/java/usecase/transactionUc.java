package usecase;

 import dto.trx;
 import dto.trxDetail;
 import helper.currentUser;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.LogStock;
 import model.transaction;
 import model.TransactionDetail;
 import model.items;
 import model.stock;
 import model.suppliers;
import repository.LogStockRepo;
 import repository.itemsRepo;
 import repository.supplierRepo;
 import repository.transactionsRepo;
 import repository.transactionsDetailRepo;
 import repository.stocksRepo;

 public class transactionUc {
     private final LogStockRepo logStockRepo;
     private final transactionsRepo transactionsRepo;
     private final transactionsDetailRepo transactionsDetailRepo;
     private final stocksRepo stocksRepo;

     public transactionUc( transactionsRepo transactionsRepo, transactionsDetailRepo transactionsDetailRepo, stocksRepo stocksRepo, LogStockRepo logStockRepo) {
         this.transactionsRepo = transactionsRepo;
         this.transactionsDetailRepo = transactionsDetailRepo;
         this.stocksRepo = stocksRepo;
         this.logStockRepo = logStockRepo;
     }

     public Integer createTransaction (transaction trx) {
         try {
             return transactionsRepo.insert(trx);
         } catch (SQLException e) {
             System.err.println("Create transaction error: " + e.getMessage());
             return 0;
         }
     }
     
     public boolean createDetailTransaction(TransactionDetail transactionDetail){
         try {
             boolean success = transactionsDetailRepo.insert(transactionDetail);
             if (!success) {
                System.out.println(" err when insert transactiion detail");
                return false;
             }
             
             stock sc = new stock();
             sc.setItemId(transactionDetail.getItemId());
             sc.setStockOut(transactionDetail.getQty());
             sc.setUpdatedAt(LocalDateTime.now());
 
             boolean sLg = stocksRepo.updateDecreaseStock(sc, 1);
             if (!sLg){
                System.out.println(" err when update stocks");
                return false;
             }
             
             LogStock ls = new LogStock();
             ls.setActivityName("stock_out");
             ls.setItemId(transactionDetail.getItemId());
             ls.setQty(transactionDetail.getQty());
             ls.setRefId(transactionDetail.getTransactionId());
             ls.setCreatedAt(LocalDateTime.now());
             
             return logStockRepo.insert(ls);
         } catch (SQLException ex) {
             Logger.getLogger(transactionUc.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         }
     }

     public List<transaction> getTransactionList(int rangeDay) {
        try {
            return transactionsRepo.getAllTransaction(rangeDay);
        } catch (SQLException e) {
            System.err.println("List transaction error: " + e.getMessage());
            return new ArrayList<>();
        }
    } 

    public boolean exportTransactionList(int rangeDay) {
        try {
            String fileName = "transaction-list-" + System.currentTimeMillis() + ".xlsx";
            List<transaction> transactions = transactionsRepo.getAllTransaction(rangeDay);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Transactions");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Grand total", "Total item", "Username"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Isi data
            int rowNum = 1;
            for (transaction s : transactions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getGrandTotal());
                row.createCell(2).setCellValue(s.getTotalItem());
                row.createCell(3).setCellValue(s.getUsername());
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
