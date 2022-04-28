package com.hcdc.cc106_finalproject_montera;

public class UserStats_Model {

    private int session_id,user_id,calories_burned,steps_count,distance;
    private String created_at;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    private String duration;

    public UserStats_Model(int session_id,int user_id,int calories_burned,int steps_count,int distance,String created_at,String duration){
        this.session_id = session_id; //
        this.user_id = user_id;
        this.calories_burned = calories_burned; //
        this.steps_count = steps_count; //
        this.distance = distance; //
        this.created_at = created_at; //
        this.duration = duration; //
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCalories_burned() {
        return calories_burned;
    }

    public void setCalories_burned(int calories_burned) {
        this.calories_burned = calories_burned;
    }

    public int getSteps_count() {
        return steps_count;
    }

    public void setSteps_count(int steps_count) {
        this.steps_count = steps_count;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
