package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class trx {
    public LocalDateTime date;
    public int totalItem;
    public double grandTotal;
    public List<trxDetail> details = new ArrayList<>();

    public trx() {}

    public trx(LocalDateTime date, int totalItem, double grandTotal, List<trxDetail> details) {
        this.date = date;
        this.totalItem = totalItem;
        this.grandTotal = grandTotal;
        this.details = details;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public List<trxDetail> getDetails() {
        return details;
    }

    public void setDetails(List<trxDetail> details) {
        this.details = details;
    }
}
