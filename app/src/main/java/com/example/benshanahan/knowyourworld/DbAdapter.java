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


public class DbAdapter {
    //items of 1st table in database
    public static final String KEY_ROWID = "_id";
    public static final String KEY_CODE = "code";
    public static final String KEY_NAME = "name";
    public static final String KEY_FAV = "active";




    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String DATABASE_NAME = "WhereInTheWorld4";
    private static final String SQLITE_TABLE = "Country";
    private static final String SQLITE_TABLE_F = "Favourites";
    private static final int DATABASE_VERSION = 2;


    //2ns table items

    public static final String KEY_ROWID_F = "_id";
    public static final String KEY_CODE_F = "code";
    public  static final String KEY_NAME_F = "name";
    public static final String KEY_DESC_F = "description";

    private final Context context;

    //creates database tables, if they dont exist using SQL
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_NAME + "," +
                    KEY_FAV + "," +
                    " UNIQUE (" + KEY_CODE +"));";

    private static final String DATABASE_CREATE_F =
            "CREATE TABLE if not exists " + SQLITE_TABLE_F + " (" +
                    KEY_ROWID_F + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE_F + "," +
                    KEY_NAME_F + "," +
                    KEY_DESC_F + "," +
                    " UNIQUE (" + KEY_CODE_F +"));";




    private static class DatabaseHelper extends SQLiteOpenHelper {
        //a class that has the functionality of the SQLiteOpenHelper Lib
        //a library which allows for database creation and management

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        //sets context to these variables, the bracketed items are now the class's 'context',
        // instead of continually calling themthe user can just use context


        @Override
        public void onCreate(SQLiteDatabase db) {
            //calls database creation code
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
            Log.w(TAG, DATABASE_CREATE_F);
            db.execSQL(DATABASE_CREATE_F);



        }

        @Override
        //upgrades database from old to new, depending what is imported, NOT USED
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
    //DbAdapter now holds the information the information declared earlier

    public DbAdapter open() throws SQLException {
        //opens the previously declared Database, using context
        mDbHelper = new DatabaseHelper(context);//mDbHelper now has the ability (through SQLiteOpenHelper) to manage our database (through context)
        mDb = mDbHelper.getWritableDatabase();//opens database to be read or written
        return this;
    }
/*
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
*/
    public long create(String code, String name, String favourite) {
    //populates the 1st table, requires code and name to be inputted
        //1st table is populated from the start and so initial values are entered
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CODE, code);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_FAV, favourite);


        return mDb.insert(SQLITE_TABLE, null, initialValues);//calls insert function and passes table and values in
    }


    public boolean delete(long id, String na) {

    //used to delete an item from array
        String string =String.valueOf(id);
        //item to be deleted is read in from where its being called from and converted to string
        Log.d("country to delete", na);
        mDb.execSQL("DELETE FROM "+ SQLITE_TABLE_F + " WHERE _id = '" + string + "'");//SQL required to delete desired item

        mDb.execSQL("UPDATE " + SQLITE_TABLE +" SET " + KEY_FAV + " = ' ' WHERE name = '" + na + "';" ); //updates the 'favourited' tag of the item on the original list
        return true;
    }

    public void update(int i){

        int updated = i+1;
        mDb.execSQL("UPDATE "+ SQLITE_TABLE +" SET " + KEY_FAV + " = 'favourited' WHERE _id = " + updated + ";");//adds a 'favourited' tag to the item in the original list
    }

    //function called to send information in database to whereever its needed
    public Cursor fetch() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_CODE, KEY_NAME, KEY_FAV},
                null, null, null, null, null);//layout of database, will be filled later, currently empty

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //same as fetch() but for the favourites, uses fav table, which is selective
    public Cursor fetchF() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_F, new String[] {KEY_ROWID_F,
                        KEY_CODE_F, KEY_NAME_F},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public void insert() {
    //called when inserting data into database
        String [] CArray = context.getResources().getStringArray(R.array.country_array);
        String [] COArray = context.getResources().getStringArray(R.array.code_array);
        //array of strings created using arrays stored in array.xml

        int i=0;
        while(i<CArray.length)
        {
            create(COArray[i],CArray[i]," ");
            i++;
            //loops till end of arrays entering in information into database from arrays
        }
    }

    public void insert_f(int i) {

            int inc = i+1;
            Log.d("i in insert_f",String.valueOf(i));
            //inserts into favourite table (code variable and name variable) selected from original table from the column
            // where id is the same as the one sent in from whereever the function is being called from
            mDb.execSQL("INSERT INTO " + SQLITE_TABLE_F + " (code,name) SELECT code,name FROM " + SQLITE_TABLE + " WHERE _id = '" + inc + "'");
            update(i);//calls update function to add the 'favourited' tag to the selected country


    }

}