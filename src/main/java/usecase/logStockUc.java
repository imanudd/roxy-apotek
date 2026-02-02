package usecase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.LogStock;
import repository.LogStockRepo;
import helper.PdfGenerator;

public class logStockUc {
    private final LogStockRepo logStockRepo;

    public logStockUc(LogStockRepo logStockRepo) {
        this.logStockRepo = logStockRepo;
    }

    public boolean createLogStock(LogStock logStock) {
        try {
            return logStockRepo.insert(logStock);
        } catch (SQLException ex) {
            Logger.getLogger(logStockUc.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public List<LogStock> getList(String filter, int rangeDay, int limit, int offset) {
        try {
            return logStockRepo.getList(filter, rangeDay, limit, offset);
        } catch (SQLException e) {
            System.out.println("Error fetching log stocks: " + e.getMessage());
            return null;
        }
    }

    // export stock
    public boolean exportLogStock(String filter, int rangeDay, String filePath) {
        try {
            List<LogStock> logstocks = logStockRepo.getList(filter, rangeDay, 0, 0);

            String[] headers = { "ID", "Activity Name", "Item Name", "Jumlah", "Username", "Tanggal" };
            List<Object[]> data = new ArrayList<>();
            for (LogStock s : logstocks) {
                data.add(new Object[] {
                        s.getId(),
                        s.getActivityName(),
                        s.getItemName(),
                        s.getQty(),
                        s.getUsername(),
                        s.getCreatedAt()
                });
            }

            boolean success = PdfGenerator.generateFormalReport("Laporan Mutasi Stok", headers, data, filePath);
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
