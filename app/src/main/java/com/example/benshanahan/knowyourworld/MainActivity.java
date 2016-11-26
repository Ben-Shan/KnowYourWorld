package com.example.benshanahan.knowyourworld;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//import static com.example.benshanahan.knowyourworld.DbAdapter.KEY_FAVOURITED;


public class MainActivity extends Activity {

    private DbAdapter dbHelper;
    private DbAdapter db;
    private SimpleCursorAdapter dataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DbAdapter(this);

        dbHelper = new DbAdapter(this);
        //dbHelper = new DbAdapter(this);
        dbHelper.open();

        dbHelper.insert();

        //Generate ListView from SQLite Database
        displayListView();

        Intent myIntent = new Intent(MainActivity.this, Welcome.class);
        startActivity(myIntent);

        ImageButton Favourites = (ImageButton) findViewById(R.id.favouritesButton);
        Favourites.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, Favourites.class);
                startActivity(myIntent);

            }
        });

    }


    private void displayListView() {


        Cursor cursor = dbHelper.fetch();

        // The desired columns to be bound
        String[] columns = new String[] {
                DbAdapter.KEY_CODE,
                DbAdapter.KEY_NAME,
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

                Intent myIntent = new Intent(MainActivity.this, CountryPage.class);
                myIntent.putExtra("counName",countryName);
                myIntent.putExtra("counCode",countryCode);
                startActivity(myIntent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
        //When the user holds down the item

            @Override
            public boolean onItemLongClick(AdapterView<?> av, View view,int pos, long id)
            {

                Toast.makeText(getApplicationContext(), "Added to Travel List", Toast.LENGTH_LONG).show();

                int position = av.getPositionForView(view); //saves the position for the selected item
                Log.d("arrayList number sent:",String.valueOf(position)); //logcat message

                dbHelper.insert_f(position);//calls the insert function, in DbAdap

                return true;

            }

        });
/*
        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchCountriesByName(constraint.toString());
            }
        });
*/
    }
}