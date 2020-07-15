package com.tenakata.Models;


import java.util.List;

public class ViewDetailsModel {


    /**
     * status : 200
     * message : Show All list !!
     * result : [{"id":"84","name":"xbd","phone":"98989898989","id_no":"Maji","date":"2020-05-18","attach_recepit":"http://res.cloudinary.com/tecorb-technologies/image/upload/v1589792436/sznab1hwxsopdcho3tgw.jpg","item_list":"dhd","amount":10,"sales_purchases":"purchase"}]
     */

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
        /**
         * id : 84
         * name : xbd
         * phone : 98989898989
         * id_no : Maji
         * date : 2020-05-18
         * attach_recepit : http://res.cloudinary.com/tecorb-technologies/image/upload/v1589792436/sznab1hwxsopdcho3tgw.jpg
         * item_list : dhd
         * amount : 10
         * sales_purchases : purchase
         */

        private String id;
        private String name;
        private String phone;
        private String id_no;
        private String date;
        private String attach_recepit;
        private String item_list;
        private int amount;
        private String sales_purchases;
        private String bussiness_name;

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

        public String getId_no() {
            return id_no;
        }

        public void setId_no(String id_no) {
            this.id_no = id_no;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getSales_purchases() {
            return sales_purchases;
        }

        public void setSales_purchases(String sales_purchases) {
            this.sales_purchases = sales_purchases;
        }

        public String getBussiness_name() {
            return bussiness_name;
        }

        public void setBussiness_name(String bussiness_name) {
            this.bussiness_name = bussiness_name;
        }
    }
}





