package com.hcdc.cc106_finalproject_montera;

public class UserSessionStats_Model {

    private int userstats_id,user_id,user_kg,count_sessions;

    public UserSessionStats_Model(int userstats_id,int user_id,int user_kg,int count_sessions){
        this.userstats_id = userstats_id;
        this.user_id = user_id;
        this.user_kg = user_kg;
        this.count_sessions = count_sessions;
    }

    public int getUserstats_id() {
        return userstats_id;
    }

    public void setUserstats_id(int userstats_id) {
        this.userstats_id = userstats_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_kg() {
        return user_kg;
    }

    public void setUser_kg(int user_kg) {
        this.user_kg = user_kg;
    }

    public int getCount_sessions() {
        return count_sessions;
    }

    public void setCount_sessions(int count_sessions) {
        this.count_sessions = count_sessions;
    }



}
