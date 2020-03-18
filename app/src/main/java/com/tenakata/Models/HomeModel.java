package com.tenakata.Models;

public class HomeModel {


    /**
     * status : 200
     * message : Show  list !!
     * result : {"cash_amount":"270286.89","credit_amount":"128.52"}
     * total_average : 135207.705
     * name : Hafijur Rahman
     */

    private String status;
    private String message;
    private ResultBean result;
    private double total_average;
    private String name;
<<<<<<< HEAD
=======
    private String amount;
>>>>>>> e164d108f317e3766cbed78c30d90c5e8c2f64b4

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

    public double getTotal_average() {
        return total_average;
    }

    public void setTotal_average(double total_average) {
        this.total_average = total_average;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

<<<<<<< HEAD
=======
    public String getAmount() {
        return amount;
    }

>>>>>>> e164d108f317e3766cbed78c30d90c5e8c2f64b4
    public static class ResultBean {
        /**
         * cash_amount : 270286.89
         * credit_amount : 128.52
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
