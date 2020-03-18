package com.tenakata.Models;

public class AddSaleCashCreditModel {


    /**
     * status : 200
     * message : Cash Add  Successfully !!
     * result : {"business_user_id":"2","amount":"90000","date":"19/02/2020","narration":"test","payment_type":"cash","sales_purchases":"sales","attach_book":"http://res.cloudinary.com/tecorb-technologies/image/upload/v1582796820/mmxxpq408yejdepurdfe.png","public_id":"mmxxpq408yejdepurdfe","updated_at":"2020-02-27 09:02:47"}
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
         * business_user_id : 2
         * amount : 90000
         * date : 19/02/2020
         * narration : test
         * payment_type : cash
         * sales_purchases : sales
         * attach_book : http://res.cloudinary.com/tecorb-technologies/image/upload/v1582796820/mmxxpq408yejdepurdfe.png
         * public_id : mmxxpq408yejdepurdfe
         * updated_at : 2020-02-27 09:02:47
         */

        private String business_user_id;
        private String amount;
        private String date;
        private String item_list;
        private String payment_type;
        private String sales_purchases;
        private String attach_recepit;
        private String public_id;
        private String updated_at;
        private String name;
        private String id_no;
        private String phone;

        public String getBusiness_user_id() {
            return business_user_id;
        }

        public void setBusiness_user_id(String business_user_id) {
            this.business_user_id = business_user_id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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



        public String getPublic_id() {
            return public_id;
        }

        public void setPublic_id(String public_id) {
            this.public_id = public_id;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId_no() {
            return id_no;
        }

        public void setId_no(String id_no) {
            this.id_no = id_no;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
