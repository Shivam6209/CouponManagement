package com.couponManagement.VO;

import com.couponManagement.constants.ApiResponseCode;

public class ResultVO {

    private String message;
    private int code;
    private Object result;
    
    public ResultVO() {
    	
    }

    public ResultVO(ApiResponseCode responseCode, String message, Object result) {
        this.code = responseCode.getCode();
        this.message = message;
        this.result = result;
    }

    public ResultVO(ApiResponseCode responseCode, String message) {
        this.code = responseCode.getCode();
        this.message = message;
        this.result = null;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", result=" + result +
                '}';
    }
}

