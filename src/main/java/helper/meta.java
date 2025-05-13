package helper;

public class meta {
    public int page;
    public int pageSize;
    public long totalData;
    public long totalPage;

    public meta(int page, int pageSize, long totalData) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalData = totalData;
        this.totalPage = (long) Math.ceil((double) totalData / pageSize);
    }
}
