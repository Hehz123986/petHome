package org.nb.petHome.utils;
import org.nb.petHome.net.NetResult;
import org.springframework.util.StringUtils;


public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    public static NetResult genSuccessResult() {
        NetResult result = new NetResult();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;
    }

    public static NetResult genSuccessResult(String message) {
        NetResult result = new NetResult();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(message);
        return result;
    }

    public static NetResult genSuccessResult(Object data) {
        NetResult result = new NetResult();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    public static NetResult genFailResult(String message) {
        NetResult result = new NetResult();
        result.setResultCode(RESULT_CODE_SERVER_ERROR);
        if (!StringUtils.hasLength(message)) {
            result.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            result.setMessage(message);
        }
        return result;
    }

    public static NetResult genErrorResult(int code, String message) {
        NetResult result = new NetResult();
        result.setResultCode(code);
        result.setMessage(message);
        return result;
    }
}
