package com.example.smarttravelplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravelPlanner.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "places_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "CITY";
    public static final String COL_3 = "PLACE_NAME";
    public static final String COL_4 = "DESCRIPTION";
    public static final String COL_5 = "WEB_URL";
    public static final String COL_6 = "IMAGE_URL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CITY TEXT, " +
                "PLACE_NAME TEXT, " +
                "DESCRIPTION TEXT, " +
                "WEB_URL TEXT, " +
                "IMAGE_URL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert Method (CREATE)
    public boolean insertPlace(String city, String place, String desc, String webUrl, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, city);
        contentValues.put(COL_3, place);
        contentValues.put(COL_4, desc);
        contentValues.put(COL_5, webUrl);
        contentValues.put(COL_6, imageUrl);
        long result = db.insert(TABLE_NAME, null, contentValues);
        // db.close(); // Optional: Connection close karna achi practice hai
        return result != -1;
    }

    // Fetch Method (READ)
    public Cursor getPlacesByCity(String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Case-insensitive search ke liye LOWER() use kiya gaya hai
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE LOWER(CITY) = LOWER(?)", new String[]{city});
    }

    // Delete Method (DELETE) - Requirement 4.4 (Extra Safety)
    public void deleteCityData(String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "CITY = ?", new String[]{city});
    }
}