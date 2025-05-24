package usecase;

 import dto.trx;
 import dto.trxDetail;
 import helper.currentUser;
 import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 }
