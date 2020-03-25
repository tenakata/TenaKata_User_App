package com.tenakata.Models;

import java.util.List;

public class GraphModel {


    /**
     * status : 200
     * message : Graph  list !!
     * result : [{"created_at":"2020-03-16 07:11:04","amount":"1000"},{"created_at":"2020-03-19 10:49:03","amount":"100"},{"created_at":"2020-03-19 12:28:54","amount":"90000"},{"created_at":"2020-03-19 12:37:30","amount":"90000"},{"created_at":"2020-03-19 12:56:33","amount":"4646"},{"created_at":"2020-03-20 05:55:57","amount":"100"},{"created_at":"2020-03-20 14:41:36","amount":"90000"},{"created_at":"2020-03-21 07:44:34","amount":"90000"}]
     */

    private String status;
    private String message;
    private String filter;
    private List<ResultBean> result;

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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * created_at : 2020-03-16 07:11:04
         * amount : 1000
         */

        private String created_at;
        private double amount;

        public String getcreated_at() {
            return created_at;
        }

        public void setcreated_at(String created_at) {
            this.created_at = created_at;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}
