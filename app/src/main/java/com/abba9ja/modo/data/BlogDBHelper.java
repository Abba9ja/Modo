package com.abba9ja.modo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.abba9ja.modo.data.BlogContract.BlogEntry;
/**
 * Created by Abba9ja on 11/20/2017.
 */

public class BlogDBHelper extends SQLiteOpenHelper {


    // The name of the database
    private static final String DATABASE_NAME = "blogDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 3;

    // Constructor
    BlogDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_BLOG_TABLE = "CREATE TABLE "  + BlogEntry.TABLE_NAME + " (" +
                BlogEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BlogEntry.COLUMN_ID + " TEXT NOT NULL, " +
                BlogEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                BlogEntry.COLUMN_DESC + " TEXT NOT NULL, " +
                BlogEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                BlogEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                BlogEntry.COLUMN_USER_EMAIL + " TEXT NOT NULL, " +
                BlogEntry.COLUMN_USERID + " TEXT NOT NULL, " +
                BlogEntry.COLUMN_USERPIC + " TEXT NOT NULL, "+
                BlogEntry.COLUMN_PTIME + " TEXT NOT NULL, " +
                BlogEntry.COLUMN_ITEM_PRICE + " TEXT NOT NULL, " +
                BlogEntry.COLUMN_NEGO + " TEXT NOT NULL);";

        db.execSQL(CREATE_BLOG_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BlogEntry.TABLE_NAME);
        onCreate(db);
    }
}
