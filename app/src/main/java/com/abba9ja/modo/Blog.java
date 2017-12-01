package com.abba9ja.modo;

import java.util.ArrayList;

/**
 * Created by Abba9ja on 11/19/2017.
 */

public class Blog {

    private String title, desc, user_name, user_email, user_pic, user_id;
    private ArrayList<String> p_image;

    public Blog(){

    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public Blog(String title, String desc, String user_name, String user_email, String user_pic, String user_id, ArrayList<String> p_image) {
        this.title = title;
        this.desc = desc;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_pic = user_pic;
        this.user_id = user_id;
        this.p_image = p_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<String> getP_image() {
        return p_image;
    }

    public void setP_image(ArrayList<String> p_image) {
        this.p_image = p_image;
    }

}
