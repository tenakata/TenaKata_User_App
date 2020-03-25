package com.tenakata.Models;

import java.util.List;

public class GraphModel {


    /**
     * status : 200
     * message : Graph  list !!
     * filter : week
     * result : [{"created_at ":"2020-03-24","amount":"0"},{"created_at ":"2020-03-23","amount":"0"},{"created_at ":"2020-03-22","amount":"0"},{"created_at ":"2020-03-21","amount":"0"},{"created_at":"2020-03-20","amount":"90000"},{"created_at ":"2020-03-19","amount":"0"},{"created_at ":"2020-03-18","amount":"0"}]
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
         * created_at  : 2020-03-24
         * amount : 0
         * created_at : 2020-03-20
         */

        private String created_at;
        private String amount;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
