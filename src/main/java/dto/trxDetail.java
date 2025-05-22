package dto;

public class trxDetail {
    public int itemId;
    public int qty;
    public double price;

    public trxDetail () {}

    public trxDetail(int itemId, int qty, double price) {
        this.itemId = itemId;
        this.qty = qty;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }   

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
