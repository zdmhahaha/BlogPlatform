package blogplatform.entity;

public class Result {

    private String status;
    private String msg;
    private boolean isLogin;
    private Object data;

    public static Result failureStatus(String msg) {
        return new Result("fail", msg, false);
    }

    public static Result okStatusButNoLogin(String msg) {
        return new Result("ok", msg, false);
    }

    public static Result okStatusAndLogin(String msg, Object data) {
        return new Result("ok", msg, true, data);
    }

    private Result(String status, String msg, boolean isLogin) {
        this(status, msg, isLogin, null);
    }

    public Result(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public Object getData() {
        return data;
    }

}
