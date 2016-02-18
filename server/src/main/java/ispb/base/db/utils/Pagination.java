package ispb.base.db.utils;


public class Pagination {

    private int start;
    private int limit;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isValid(){
        return start >= 0 && limit >= 1;
    }
}
