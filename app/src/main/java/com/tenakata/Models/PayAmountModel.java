package com.tenakata.Models;

public class PayAmountModel {


    /**
     * status : 200
     * message : Payment Successfully!!
     * result : {"transaction_id":"1","amount_paid":"100","payment_option":"credit","sales_purchase":"sales","naration":"test","updated_at":"2020-03-06 09:03:49"}
     */

    private String status;
    private String message;
    private ResultBean result;

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

    public static class ResultBean {
        /**
         * transaction_id : 1
         * amount_paid : 100
         * payment_option : credit
         * sales_purchase : sales
         * naration : test
         * updated_at : 2020-03-06 09:03:49
         */

        private String transaction_id;
        private String amount_paid;
        private String payment_option;
        private String sales_purchase;
        private String naration;
        private String updated_at;

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getAmount_paid() {
            return amount_paid;
        }

        public void setAmount_paid(String amount_paid) {
            this.amount_paid = amount_paid;
        }

        public String getPayment_option() {
            return payment_option;
        }

        public void setPayment_option(String payment_option) {
            this.payment_option = payment_option;
        }

        public String getSales_purchase() {
            return sales_purchase;
        }

        public void setSales_purchase(String sales_purchase) {
            this.sales_purchase = sales_purchase;
        }

        public String getNaration() {
            return naration;
        }

        public void setNaration(String naration) {
            this.naration = naration;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
