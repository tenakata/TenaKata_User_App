package com.tenakata.Models;


import java.util.List;

public class ViewDetailsModel {

    private String status;
    private String message;
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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String id;
        private String name;
        private String phone;
        private String attach_recepit;
        private String item_list;
        private String amount;
        private String sales_purchases;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAttach_recepit() {
            return attach_recepit;
        }

        public void setAttach_recepit(String attach_recepit) {
            this.attach_recepit = attach_recepit;
        }






        public String getItem_list() {
            return item_list;
        }

        public void setItem_list(String item_list) {
            this.item_list = item_list;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getSales_purchases() {
            return sales_purchases;
        }

        public void setSales_purchases(String sales_purchases) {
            this.sales_purchases = sales_purchases;
        }
    }


}





