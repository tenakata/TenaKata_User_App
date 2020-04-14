package com.tenakata.Models;

public class LoginModel {


    /**
     * status : 200
     * message : Login Successfully !!
     * result : {"id":"1","name":"ankit tiwari","phone":"7836048635","country_code":"91","role":"user","token":"Ug6N4YOfibcwRIm80Sdp"}
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
         * id : 1
         * name : ankit tiwari
         * phone : 7836048635
         * country_code : 91
         * role : user
         * token : Ug6N4YOfibcwRIm80Sdp
         */

        private String id;
        private String name;
        private String phone;
        private String country_code;
        private String role;
        private String token;
        private String image;
        private String email;
        private String user_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getUserId() {
            return user_id;
        }

        public void setUserId(String id) {
            this.user_id = user_id;
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

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
