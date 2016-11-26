package com.example.benshanahan.knowyourworld;

/**
 * Created by Ben Shanahan on 12/11/2016.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.sql.Types.NULL;

public class DbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CODE = "code";
    public static final String KEY_NAME = "name";
    //private static final Boolean KEY_FAVOURITED = FALSE;
    //public static final String KEY_FAVOURITED = "0";



    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "WhereInTheWorld";
    private static final String SQLITE_TABLE = "Country";
    private static final String SQLITE_TABLE_F = "Favourites";
    private static final int DATABASE_VERSION = 2;



    public static final String KEY_ROWID_F = "_id";
    public static final String KEY_CODE_F = "code";
    public  static final String KEY_NAME_F = "name";

    private final Context context;


    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_NAME + "," +
                    //KEY_FAVOURITED + "," +
                    " UNIQUE (" + KEY_CODE +"));";

    private static final String DATABASE_CREATE_F =
            "CREATE TABLE if not exists " + SQLITE_TABLE_F + " (" +
                    KEY_ROWID_F + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE_F + "," +
                    KEY_NAME_F + "," +
                    //KEY_FAVOURITED + "," +
                    " UNIQUE (" + KEY_CODE_F +"));";




    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //String[] CArray;


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
            Log.w(TAG, DATABASE_CREATE_F);
            db.execSQL(DATABASE_CREATE_F);



        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_F);
            onCreate(db);
        }
    }

    public DbAdapter(Context context) {
        this.context = context;
    }

    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long create(String code, String name/*, Boolean favourited*/) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CODE, code);
        initialValues.put(KEY_NAME, name);
        //initialValues.put(KEY_FAVOURITED, favourited);


        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }
/*
    public long create_f(String code, String name) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CODE_F, code);
        initialValues.put(KEY_NAME_F, name);
        //initialValues.put(KEY_FAVOURITED, favourited);

        //return mDb.insert_f(SQLITE_TABLE_F,null, initialValues);
        return NULL;
    }
*/

    public boolean delete(long id) {

        String string =String.valueOf(id);
        mDb.execSQL("DELETE FROM "+ SQLITE_TABLE_F + " WHERE _id = '" + string + "'");
        return true;
    }

    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_NAME/*, KEY_FAVOURITED*/},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_NAME/*,KEY_FAVOURITED*/},
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
                        KEY_CODE, KEY_NAME/*, KEY_FAVOURITED*/},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchCountriesByNameF(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_F, new String[] {KEY_ROWID_F,
                            KEY_CODE_F, KEY_NAME_F/*, KEY_FAVOURITED*/},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_F, new String[] {KEY_ROWID_F,
                            KEY_CODE_F, KEY_NAME_F/*,KEY_FAVOURITED*/},
                    KEY_NAME_F + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchF() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_F, new String[] {KEY_ROWID_F,
                        KEY_CODE_F, KEY_NAME_F/*, KEY_FAVOURITED*/},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public void insert() {

        String [] CArray = context.getResources().getStringArray(R.array.country_array);
        String [] COArray = context.getResources().getStringArray(R.array.code_array);

        int i=0;
        while(i<CArray.length)
        {
            create(COArray[i],CArray[i]/*,FALSE*/);
            i++;
        }
    }

    public void insert_f(int i) {

        String [] CArray = context.getResources().getStringArray(R.array.country_array);
        String [] COArray = context.getResources().getStringArray(R.array.code_array);

            //create_f(COArray[i],CArray[i]/*,FALSE*/);
            //create(i,i,FALSE);

            mDb.execSQL("INSERT INTO "+SQLITE_TABLE_F+"(code,name) VALUES ('" + COArray[i] + "','" + CArray[i] + "')");

    }

}