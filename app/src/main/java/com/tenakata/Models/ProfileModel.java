package com.tenakata.Models;

public class ProfileModel {


    /**
     * status : 200
     * message : Profile list!!
     * result : {"id":"1","name":"ankit t","email":"ankit.t@gmail.com","role":"user","image":"http://res.cloudinary.com/tecorb-technologies/image/upload/v1584698184/a8sntp0nghtwrmvgejnp.jpg","public_id":"a8sntp0nghtwrmvgejnp","updated_at":"2020-03-20 09:03:56"}
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
         * name : ankit t
         * email : ankit.t@gmail.com
         * role : user
         * image : http://res.cloudinary.com/tecorb-technologies/image/upload/v1584698184/a8sntp0nghtwrmvgejnp.jpg
         * public_id : a8sntp0nghtwrmvgejnp
         * updated_at : 2020-03-20 09:03:56
         */

        private String id;
        private String name;
        private String email;
        private String role;
        private String image;
        private String public_id;
        private String updated_at;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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
    }
}
