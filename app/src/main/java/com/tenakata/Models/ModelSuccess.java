
package com.tenakata.Models;

public class ModelSuccess {

    private String status;
    private String mMessage;

    public String getCode() {
        return status;
    }

    public void setCode(String code) {
        status = code;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

}
