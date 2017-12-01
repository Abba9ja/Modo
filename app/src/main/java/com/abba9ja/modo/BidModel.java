package com.abba9ja.modo;

/**
 * Created by Abba9ja on 11/27/2017.
 */

public class BidModel {

    String user_id, user_name, user_pic, bid_amount, bit_range, bid_time, email_address;

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public BidModel(){

    }

    public BidModel(String user_id, String user_name, String user_pic, String bid_amount, String bit_range, String bid_time, String email_address) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_pic = user_pic;
        this.bid_amount = bid_amount;
        this.bit_range = bit_range;
        this.bid_time = bid_time;
        this.email_address = email_address;
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

    public String getBid_amount() {
        return bid_amount;
    }

    public void setBid_amount(String bid_amount) {
        this.bid_amount = bid_amount;
    }

    public String getBit_range() {
        return bit_range;
    }

    public void setBit_range(String bit_range) {
        this.bit_range = bit_range;
    }

    public String getBid_time() {
        return bid_time;
    }

    public void setBid_time(String bid_time) {
        this.bid_time = bid_time;
    }
}
