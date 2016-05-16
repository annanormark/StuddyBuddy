package com.mikaela.hhtimer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class dbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DatesOfDeadlines.db";
    public static final String TABLE_PRODUCTS = "dbProduct";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PRODUCTNAME = "productname";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_WORK = "work";

    public dbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCTNAME + " TEXT, " +
                COLUMN_DATE + " LONG, " +
                COLUMN_WORK + " INT" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Add a new roe to the database
    public void addProduct(dbProduct product) {
        ContentValues values = new ContentValues();

        //gets values from productclass
        values.put(COLUMN_PRODUCTNAME, product.get_productname());
        values.put(COLUMN_DATE, product.get_date());
        values.put(COLUMN_WORK, product.get_work());

        //adds values to database
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    //Delete product from database
    public void deleteProduct(String productname) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + "=\'" + productname + "\';");
    }

    //Print out the database as a string
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1;";

        //Cursor point to location in your result
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your result
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("productname")) != null) {
                dbString += c.getString(c.getColumnIndex("productname"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;

    }

    public String eventsOfDatesToString(Long Millisec) {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_DATE + "=" + Millisec.toString() + ";";

        //Cursor point to location in your result
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your result
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("productname")) != null) {
                dbString += c.getString(c.getColumnIndex("productname"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;

    }

    public List<Long> getDates() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT DISTINCT " + COLUMN_DATE + " FROM " + TABLE_PRODUCTS + " WHERE 1;";
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your result
        c.moveToFirst();
        List<Long> result = new ArrayList<>();
        while (!c.isAfterLast()) {
            result.add(c.getLong(c.getColumnIndex(COLUMN_DATE)));
            c.moveToNext();
        }
        return result;
    }

    public String[] getTitles(Long dateInMilliSec) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_PRODUCTNAME + " FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_DATE + "=" + dateInMilliSec.toString() + ";";
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your result
        c.moveToFirst();

        String[] result = new String[c.getCount()];
        int i = 0;
        while (!c.isAfterLast()) {
            result[i] = c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME));
            c.moveToNext();
            i++;
        }
        return result;
    }

    public List<Integer> getWork(Long dateInMilliSec) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_WORK + " FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_DATE + "=" + dateInMilliSec.toString() + ";";
        Cursor c = db.rawQuery(query, null);

        //Move to the first row in your result
        c.moveToFirst();
        List<Integer> result = new ArrayList<>();
        while (!c.isAfterLast()) {
            result.add(c.getInt(c.getColumnIndex(COLUMN_WORK)));
            c.moveToNext();
        }
        return result;
    }

    public Cursor getAll(Long dateInMilliSec) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_WORK + ", " + COLUMN_PRODUCTNAME + ", " + COLUMN_DATE + " FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_DATE + "=" + dateInMilliSec.toString() + ";";
        Cursor c = db.rawQuery(query, null);

        //Move to the first row in your result
        c.moveToFirst();
        return c;
    }
}
