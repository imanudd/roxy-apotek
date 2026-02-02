package usecase;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import helper.currentUser;
import helper.PdfGenerator;
import model.transaction;
import model.TransactionDetail;
import model.stock;
import model.LogStock;

import repository.LogStockRepo;
import repository.transactionsRepo;
import repository.transactionsDetailRepo;
import repository.stocksRepo;

public class transactionUc {
    private final LogStockRepo logStockRepo;
    private final transactionsRepo transactionsRepo;
    private final transactionsDetailRepo transactionsDetailRepo;
    private final stocksRepo stocksRepo;

    public transactionUc(transactionsRepo transactionsRepo, transactionsDetailRepo transactionsDetailRepo,
            stocksRepo stocksRepo, LogStockRepo logStockRepo) {
        this.transactionsRepo = transactionsRepo;
        this.transactionsDetailRepo = transactionsDetailRepo;
        this.stocksRepo = stocksRepo;
        this.logStockRepo = logStockRepo;
    }

    public Integer createTransaction(transaction trx) {
        try {
            return transactionsRepo.insert(trx);
        } catch (SQLException e) {
            System.err.println("Create transaction error: " + e.getMessage());
            return 0;
        }
    }

    public boolean createDetailTransaction(TransactionDetail transactionDetail) {
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
            if (!sLg) {
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

    public List<transaction> getTransactionList(int rangeDay, int limit, int offset) {
        try {
            return transactionsRepo.getAllTransaction(rangeDay, limit, offset);
        } catch (SQLException e) {
            System.err.println("List transaction error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean exportTransactionList(int rangeDay, String filePath) {
        try {
            List<transaction> transactions = transactionsRepo.getAllTransaction(rangeDay, 0, 0);

            String[] headers = { "ID", "Grand Total", "Total Item", "Username" };
            List<Object[]> data = new ArrayList<>();
            for (transaction s : transactions) {
                data.add(new Object[] {
                        s.getId(),
                        s.getGrandTotal(),
                        s.getTotalItem(),
                        s.getUsername()
                });
            }

            boolean success = PdfGenerator.generateFormalReport("Laporan Data Transaksi", headers, data, filePath);
            if (success) {
                System.out.println("PDF berhasil dibuat: " + filePath);
            }
            return success;

        } catch (Exception e) {
            System.err.println("Gagal export PDF: " + e.getMessage());
            return false;
        }
    }

}
