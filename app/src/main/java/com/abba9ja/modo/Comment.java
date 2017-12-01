package com.abba9ja.modo;

/**
 * Created by Abba9ja on 11/24/2017.
 */

public class Comment {
    String user_id, user_name, user_pic, user_post_coment, post_time;

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public Comment(){


    }

    public Comment(String user_id, String user_name, String user_pic, String user_post_coment, String post_time) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_pic = user_pic;
        this.user_post_coment = user_post_coment;
        this.post_time = post_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getUser_post_coment() {
        return user_post_coment;
    }

    public void setUser_post_coment(String user_post_coment) {
        this.user_post_coment = user_post_coment;
    }
}
