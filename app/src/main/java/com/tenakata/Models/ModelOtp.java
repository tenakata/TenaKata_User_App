package com.tenakata.Models;

public class ModelOtp {

    /**
     * status : 200
     * message : Show Otp!!
     * otp : 4954
     */

    private String status;
    private String message;
    private int otp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }
}
