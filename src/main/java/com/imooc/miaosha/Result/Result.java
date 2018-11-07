package com.imooc.miaosha.Result;

/**
 * @author yifan
 * @date 2018/7/16 下午9:02
 */
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功时候
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    /**
     * 失败时候电泳
     * @param cm
     * @param <T>
     */
    public static <T> Result<T> error (CodeMsg cm) {
        return new Result<T>(cm);
    }

    private Result (CodeMsg cm) {
        if (cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }


    private Result(T data) {
        this.code = 0;
        this.msg = "sucess";
        this.data = data;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
