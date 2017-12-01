package com.abba9ja.modo.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.abba9ja.modo.data.BlogContract;
import com.abba9ja.modo.utility.OpenFBBlogdata;

/**
 * Created by Abba9ja on 11/20/2017.
 */

class BlogSyncTask {

    synchronized public static void syncBlog(Context context) {

        try {
            ContentValues[] blogValues = OpenFBBlogdata.getBlogData();

            if (blogValues != null && blogValues.length != 0) {
                ContentResolver blogContentResolver = context.getContentResolver();

                blogContentResolver.delete(
                        BlogContract.BlogEntry.CONTENT_URI,
                        null,
                        null);

                blogContentResolver.bulkInsert(
                        BlogContract.BlogEntry.CONTENT_URI,
                        blogValues);


            }

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }

}
