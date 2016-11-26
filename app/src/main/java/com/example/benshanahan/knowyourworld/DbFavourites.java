package com.example.benshanahan.knowyourworld;

/**
 * Created by Ben Shanahan on 15/11/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DbFavourites {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CODE = "code";
    public static final String KEY_NAME = "name";
    public static final String KEY_CONTINENT = "continent";
    public static final String KEY_REGION = "region";

    private static final String TAG = "DbFavourites";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "KnowYourWorldFavourites";
    private static final String SQLITE_TABLE = "Country";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_NAME + "," +
                    KEY_CONTINENT + "," +
                    KEY_REGION + "," +
                    " UNIQUE (" + KEY_CODE +"));";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public DbFavourites(Context context) {
        this.context = context;
    }

    public DbFavourites open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long create(String code, String name,
                       String continent, String region) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CODE, code);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_CONTINENT, continent);
        initialValues.put(KEY_REGION, region);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean delete() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_NAME, KEY_CONTINENT, KEY_REGION},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_NAME, KEY_CONTINENT, KEY_REGION},
                    KEY_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetch() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_CODE, KEY_NAME, KEY_CONTINENT, KEY_REGION},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //CSV reading attempt




    public void insert(String Code,String Name) {



        create(Code,Name,"Asia","Southern and Central Asia");


    }

}