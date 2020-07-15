package com.tenakata.Models;

import java.util.List;

public class CashSalesCreditModel {


    /**
     * status : 200
     * message : Show All list !!
     * result : [{"id":"106","business_user_id":"230","date":"2020-05-26","amount":10,"phone":"9865986598","country_code":"91","id_no":"hd","name":"Stocks","item_list":"dbdhd","attach_recepit":"http://res.cloudinary.com/tecorb-technologies/image/upload/v1590485843/f8a0osnhfn3cvhchyxcw.jpg","public_id":"f8a0osnhfn3cvhchyxcw","payment_type":"credit","sales_purchases":"purchase","created_at":"2020-05-26","updated_at":"2020-05-26 09:05:37","bussiness_name":"business nm"}]
     * total_amount : 10
     */

    private String status;
    private String message;
    private Double total_amount;
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

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 106
         * business_user_id : 230
         * date : 2020-05-26
         * amount : 10
         * phone : 9865986598
         * country_code : 91
         * id_no : hd
         * name : Stocks
         * item_list : dbdhd
         * attach_recepit : http://res.cloudinary.com/tecorb-technologies/image/upload/v1590485843/f8a0osnhfn3cvhchyxcw.jpg
         * public_id : f8a0osnhfn3cvhchyxcw
         * payment_type : credit
         * sales_purchases : purchase
         * created_at : 2020-05-26
         * updated_at : 2020-05-26 09:05:37
         * bussiness_name : business nm
         */


        private String id;
        private String business_user_id;
        private String date;
        private String amount;
        private String phone;
        private String country_code;
        private String id_no;
        private String name;
        private String item_list;
        private String attach_recepit;
        private String public_id;
        private String payment_type;
        private String sales_purchases;
        private String created_at;
        private String updated_at;
        private String bussiness_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBusiness_user_id() {
            return business_user_id;
        }

        public void setBusiness_user_id(String business_user_id) {
            this.business_user_id = business_user_id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String  getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getId_no() {
            return id_no;
        }

        public void setId_no(String id_no) {
            this.id_no = id_no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getItem_list() {
            return item_list;
        }

        public void setItem_list(String item_list) {
            this.item_list = item_list;
        }

        public String getAttach_recepit() {
            return attach_recepit;
        }

        public void setAttach_recepit(String attach_recepit) {
            this.attach_recepit = attach_recepit;
        }

        public String getPublic_id() {
            return public_id;
        }

        public void setPublic_id(String public_id) {
            this.public_id = public_id;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        public String getSales_purchases() {
            return sales_purchases;
        }

        public void setSales_purchases(String sales_purchases) {
            this.sales_purchases = sales_purchases;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getBussiness_name() {
            return bussiness_name;
        }

        public void setBussiness_name(String bussiness_name) {
            this.bussiness_name = bussiness_name;
        }
    }
}

























