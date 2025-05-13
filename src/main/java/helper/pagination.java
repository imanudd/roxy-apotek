package helper;

public class pagination {
    public int page;
    public int pageSize;
    public String keyword;
    public String orderBy;
    public String orderType;

    public int getOffset() {
        return (page - 1) * pageSize;
    }
}
