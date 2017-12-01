package com.abba9ja.modo.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Abba9ja on 11/20/2017.
 */

public class BlogProvider  extends ContentProvider {

    // Define final integer constants for the directory of favorites and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int BLOG = 100;
    public static final int BLOG_WITH_ID = 101;


    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        uriMatcher.addURI(BlogContract.AUTHORITY, BlogContract.PATH_BLOG, BLOG);
        uriMatcher.addURI(BlogContract.AUTHORITY, BlogContract.PATH_BLOG + "/#", BLOG_WITH_ID);

        return uriMatcher;
    }


    // Member variable for a TaskDbHelper that's initialized in the onCreate() method
    private BlogDBHelper blogDBHelper;



    @Override
    public boolean onCreate() {
       //Complete onCreate() and initialize a TaskDbhelper on startup
        // [Hint] Declare the DbHelper as a global variable
        Context context = getContext();
        blogDBHelper = new BlogDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = blogDBHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case BLOG: {
                retCursor = db.query(BlogContract.BlogEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case BLOG_WITH_ID:{

                String idString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{idString};

                retCursor = db.query(BlogContract.BlogEntry.TABLE_NAME,
                        projection,
                        BlogContract.BlogEntry.COLUMN_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                break;
            }// Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        // Return the desired Cursor
        return retCursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = blogDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        if (null == selection) selection = "1";
        switch (match) {

            case BLOG:{
                tasksDeleted = db.delete(
                        BlogContract.BlogEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = blogDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case BLOG:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(BlogContract.BlogEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        blogDBHelper.close();
        super.shutdown();
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
