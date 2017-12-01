package com.abba9ja.modo;

/**
 * Created by Abba9ja on 11/25/2017.
 */

public class UserPost {

    private String trash, title, desc, post_image, user_name, user_email, user_id, user_pic, post_time;

    public UserPost(){

    }

    public UserPost(String trash, String title, String desc, String post_image, String user_name, String user_email, String user_id, String user_pic, String post_time) {
        this.trash = trash;
        this.title = title;
        this.desc = desc;
        this.post_image = post_image;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_id = user_id;
        this.user_pic = user_pic;
        this.post_time = post_time;
    }

    public String getTrash() {
        return trash;
    }

    public void setTrash(String trash) {
        this.trash = trash;
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

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }
}
