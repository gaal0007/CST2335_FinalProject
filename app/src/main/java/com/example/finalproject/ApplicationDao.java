package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ApplicationDao extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "FavouritesDB";
    protected static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "favourite";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_DATE = "date";
    public static final String COL_IMAGE = "image";
    public static final String COL_ID = "_id";

    public ApplicationDao(Context ctx) { super(ctx, DATABASE_NAME, null, VERSION_NUM); }

    // Creates the database if it does not already exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_LATITUDE + " text,"
                + COL_LONGITUDE + " text,"
                + COL_DATE + " text,"
                + COL_IMAGE + " text);");
    }

    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
