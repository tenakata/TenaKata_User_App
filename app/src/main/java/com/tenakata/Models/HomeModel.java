package com.tenakata.Models;

public class HomeModel {


    /**
     * status : 200
     * message : Show  list !!
     * result : {"cash_amount":"70043","credit_amount":"1005376"}
     * total_average : 537709.5
     * percentage : 8.94
     * arraow : true
     * total : 1075419
     * name : db
     */

    private String status;
    private String message;
    private ResultBean result;
    private Double total_average;
    private String percentage;
    private Boolean arraow;
    private int total;
    private String name;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Double getTotal_average() {
        return total_average;
    }

    public void setTotal_average(Double total_average) {
        this.total_average = total_average;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Boolean getArraow() {
        return arraow;
    }

    public void setArraow(Boolean arraow) {
        this.arraow = arraow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class ResultBean {
        /**
         * cash_amount : 70043
         * credit_amount : 1005376
         */

        private double cash_amount;
        private double credit_amount;

        public double getCash_amount() {
            return cash_amount;
        }

        public void setCash_amount(double cash_amount) {
            this.cash_amount = cash_amount;
        }

        public double getCredit_amount() {
            return credit_amount;
        }

        public void setCredit_amount(double credit_amount) {
            this.credit_amount = credit_amount;
        }
    }
}
