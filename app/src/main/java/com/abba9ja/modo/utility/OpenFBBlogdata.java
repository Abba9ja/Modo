package com.abba9ja.modo.utility;

import android.content.ContentValues;

import com.abba9ja.modo.data.BlogContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Abba9ja on 11/20/2017.
 */

public class OpenFBBlogdata {

    private static DatabaseReference mDatabase, mmDb;
    private static ContentValues[] parsedBlogData;

    public static ContentValues[] getBlogData(){

        mDatabase = FirebaseDatabase.getInstance().getReference("Blog");
        mmDb = FirebaseDatabase.getInstance().getReference("Blog");

        final String OMCOLUMN_ID;
        final String OMCOLUMN_TITLE = "title";
        final String OMCOLUMN_DESC = "desc";
        final String OMCOLUMN_IMAGE = "p_image0";
        final String OMCOLUMN_USERNAME = "user_name";
        final String OMCOLUMN_USER_EMAIL = "user_email";
        final String OMCOLUMN_USERID= "user_id";
        final String OMCOLUMN_USERPIC = "user_pic";
        final String OMCOLUMN_PTIME = "post_time";
        final String OMCOLUMN_ITEM_PRICE = "item_price";
        final String OMCOLUMN_NEGO = "nego_stats";




        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int i = 0;
                parsedBlogData = new ContentValues[(int) dataSnapshot.getChildrenCount()];
                for(DataSnapshot data: dataSnapshot.getChildren()){

                    if (data != null){
                        String blog_post_id, post_title, post_desc, post_img, post_name, post_email, psot_userid, post_userpic, post_time, item_price, nego_stats;
                        blog_post_id = post_title = post_desc = post_img = post_name =  post_email = psot_userid = post_userpic = post_time = item_price = nego_stats = " ";
                        if (data.child(OMCOLUMN_TITLE).getValue() != null){
                            post_title = String.valueOf(data.child(OMCOLUMN_TITLE).getValue());
                        }
                        if (data.child(OMCOLUMN_DESC).getValue() != null){
                            post_desc = String.valueOf(data.child(OMCOLUMN_DESC).getValue());
                        }
                        if (data.child(OMCOLUMN_IMAGE).getValue() != null){
                            post_img = String.valueOf(data.child(OMCOLUMN_IMAGE).getValue());
                        }
                        if (data.child(OMCOLUMN_USERNAME).getValue() != null){
                            post_name = String.valueOf(data.child(OMCOLUMN_USERNAME).getValue());
                        }
                        if (data.child(OMCOLUMN_USER_EMAIL) != null){
                            post_email = String.valueOf(data.child(OMCOLUMN_USER_EMAIL).getValue());
                        }

                        if (data.child(OMCOLUMN_USERID).getValue() != null){
                            psot_userid = String.valueOf(data.child(OMCOLUMN_USERID).getValue());
                        }

                        if (data.child(OMCOLUMN_USERPIC).getValue() != null){
                            post_userpic = String.valueOf(data.child(OMCOLUMN_USERPIC).getValue());
                        }
                        if (data.child(OMCOLUMN_PTIME).getValue() != null){
                            post_time = String.valueOf(data.child(OMCOLUMN_PTIME).getValue());
                        }if (data.child(OMCOLUMN_ITEM_PRICE).getValue() != null){
                            item_price = String.valueOf(data.child(OMCOLUMN_ITEM_PRICE).getValue());
                        }if (data.child(OMCOLUMN_NEGO).getValue() != null){
                            nego_stats = String.valueOf(data.child(OMCOLUMN_NEGO).getValue());
                        }

                        blog_post_id = OMCOLUMN_USERID + OMCOLUMN_TITLE + OMCOLUMN_IMAGE;

                        ContentValues blogValues = new ContentValues();

                        blogValues.put(BlogContract.BlogEntry.COLUMN_ID, blog_post_id);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_TITLE, post_title);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_DESC, post_desc);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_IMAGE, post_img);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_USERNAME, post_name);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_USER_EMAIL, post_email);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_USERID, psot_userid);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_USERPIC, post_userpic);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_PTIME, post_time);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_ITEM_PRICE, item_price);
                        blogValues.put(BlogContract.BlogEntry.COLUMN_NEGO, nego_stats);

                        parsedBlogData[i] = blogValues;
                        i++;
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return parsedBlogData;
    }
}
