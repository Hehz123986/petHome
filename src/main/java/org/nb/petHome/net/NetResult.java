package org.nb.petHome.net;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
public class NetResult<T> {
    private static final long serialVersionUID = 1L;
    private int resultCode;
    private String message;
    private T data;

    public NetResult() {
    }

    public NetResult(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NetResult{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
