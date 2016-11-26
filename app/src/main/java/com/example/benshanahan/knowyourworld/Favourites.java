package com.example.benshanahan.knowyourworld;
/**
 * Created by Ben Shanahan on 15/11/2016.
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.benshanahan.knowyourworld.CountryPage;
import com.example.benshanahan.knowyourworld.DbAdapter;
import com.example.benshanahan.knowyourworld.R;

public class Favourites extends Activity {

    private DbAdapter dbHelper;
    private DbAdapter db;
    private SimpleCursorAdapter dataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        db=new DbAdapter(this);

        dbHelper = new DbAdapter(this);
        dbHelper = new DbAdapter(this);
        dbHelper.open();

        //Clean all data
        //dbHelper.delete();
        //Add some data
        dbHelper.insert();

        //Generate ListView from SQLite Database
        displayListView();

        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();
            }
        });

    }


    private void displayListView() {


        Cursor cursor = dbHelper.fetchF();

        // The desired columns to be bound
        String[] columns = new String[] {
                DbAdapter.KEY_CODE_F,
                DbAdapter.KEY_NAME_F,
                // DbAdapter.KEY_FAVOURITED,
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.code,
                R.id.name,
        };


        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.country,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryName =
                        cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String countryCode =
                        cursor.getString(cursor.getColumnIndexOrThrow("code"));

                Toast.makeText(getApplicationContext(),
                        countryName, Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(Favourites.this, CountryPage.class);
                myIntent.putExtra("counName",countryName);
                myIntent.putExtra("counCode",countryCode);
                startActivity(myIntent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View view,int pos, long id)
            {

                Toast.makeText(getApplicationContext(), "Deleted to Travel List", Toast.LENGTH_LONG).show();

                int position = av.getPositionForView(view);
                Log.d("arrayList number sent:",String.valueOf(position));

                dbHelper.delete(id);
                refresh();

                return true;
            }
        });

/*
        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchCountriesByNameF(constraint.toString());
            }
        });
*/
    }

    private void refresh()
    {
        Favourites.this.finish();
        Intent refresh=new Intent(this,Favourites.class);
        startActivity(refresh);
        //deletes, then recalls intent to update list
    }
/*
    void deleteItem(long item)
    {
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this);
        final long valuetodelete=item;
        builder1.setMessage("Do you really want to delete value: " + valuetodelete);
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                DbAdapter db=new DbAdapter(getApplicationContext());
                try
                {
                    db.open();
                    db.delete(valuetodelete);
                    Toast.makeText(Favourites.this, "Value: " + valuetodelete + " was deleted.", Toast.LENGTH_LONG).show();
                    //refresh();
                }
                catch (Exception ex)
                {
                    Log.d("SensitvityLV","OPEN FAILED");
                }
            }
        });

        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

        android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    */
}