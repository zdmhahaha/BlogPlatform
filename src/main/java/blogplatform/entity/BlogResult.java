package blogplatform.entity;

public class BlogResult {

    private String status;
    private String msg;
    private Integer total;
    private Integer page;
    private Integer totalPage;
    private Object data;

    public static BlogResult allBlogs(Integer total, Integer page, Integer totalPage, Object data) {
        return new BlogResult("ok", "获取成功", total, page, totalPage, data);
    }

    public static BlogResult Success(Integer total, String msg, Integer page, Integer totalPage, Object data) {
        return new BlogResult("ok", msg, total, page, totalPage, data);
    }

    public static BlogResult failure(String msg) {
        return new BlogResult("fail", msg, 0, 0, 0, null);
    }

    public static BlogResult failureBlogStyle(String msg) {
        return new BlogResult("fail", msg, 0, 0, 0, null);
    }

    public BlogResult(String status, String msg, Integer total, Integer page, Integer totalPage, Object data) {
        this.status = status;
        this.msg = msg;
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
