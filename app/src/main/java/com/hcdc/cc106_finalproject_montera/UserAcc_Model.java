package com.hcdc.cc106_finalproject_montera;

public class UserAcc_Model {

    private int user_id;
    private String name,status,password,email;

    public UserAcc_Model(int user_id,String name,String status,String password,String email){
        this.user_id = user_id;
        this.name = name;
        this.status = status;
        this.password = password;
        this.email= email;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
