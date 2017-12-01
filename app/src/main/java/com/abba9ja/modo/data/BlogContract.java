package com.abba9ja.modo.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Abba9ja on 11/20/2017.
 */

public class BlogContract {


    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.abba9ja.modo";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final String PATH_BLOG = "blog";


    public static final class BlogEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BLOG).build();


        // Favorites table and column names
        public static final String TABLE_NAME = "blog";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the row below
        public static final String COLUMN_ID = "blog_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESC = "desc";
        public static final String COLUMN_IMAGE = "p_image";
        public static final String COLUMN_USERNAME = "user_name";
        public static final String COLUMN_USER_EMAIL = "user_email";
        public static final String COLUMN_USERID= "user_id";
        public static final String COLUMN_USERPIC = "user_pic";
        public static final String COLUMN_PTIME = "post_time";
        public static final String COLUMN_ITEM_PRICE = "item_price";
        public static final String COLUMN_NEGO = "nego_stats";

    }


}
