package com.example.benshanahan.knowyourworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import static android.R.attr.button;

/**
 * Created by Ben Shanahan on 12/11/2016.
 */

public class CountryPage extends Activity {
    public String CounName; //needs to be declared publically
    public String CounCode;
    public String CounCodeLower;
    public String CounNameR;
    public String refineSearchtext;
    public EditText refineSearch;

    public String FlagURL;
    public String url;



    public Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_page);

        Intent myIntent = getIntent(); // gets intent previously created in MainActivity
        CounName = myIntent.getStringExtra("counName"); //retrives string sent in intent
        CounCode = myIntent.getStringExtra("counCode");

        CounCodeLower = CounCode.toLowerCase();
        //LoadImageFromWebOperations(url, CounCodeLower);
/*
        FlagURL = ("http://flagpedia.net/data/flags/normal/" + CounCodeLower + ".png");
        try {
            ImageView CCodeImageView = (ImageView) findViewById(R.id.CCode);
            CCodeImageView.setImageURI(Uri.parse(FlagURL));
        }
        catch (Exception e){
            TextView ErrorFlag = (TextView) findViewById(R.id.errorflag);
            ErrorFlag.setText("No Flag found");
        }
*/
        ImageView InternetPic= (ImageView) findViewById(R.id.CCode);
        Bitmap bm = null; //
        try {
            URL flagU = new URL("http://flagpedia.net/data/flags/mini/" + CounCodeLower + ".png"); //states the desired url
            URLConnection conn = flagU.openConnection(); //states the desired connection to our url
            conn.connect();//starts connection
            InputStream is = conn.getInputStream();//InputStream reads from connection conn
            BufferedInputStream bis = new BufferedInputStream(is);//stores stream in buffer
            bm = BitmapFactory.decodeStream(bis);//creates bitmap object from stream
            InternetPic.setImageBitmap(bm);
            bis.close();
            is.close();

        } catch (Exception e) {
            Log.v("EXCEPTION", "Error getting bitmap", e);
        }



        //refineSearchtext = null;




        TextView CNameTextView = (TextView) findViewById(R.id.CName); //displays country name
        CNameTextView.setText(CounName);

        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();


            }
        });
/*
        final EditText refineSearch = new EditText(CountryPage.this);
        //EditText refineSearch = (EditText) findViewById(R.id.refine);
        refineSearch.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        //CounNameR = refineSearchtext + " " + CounName;

        //String selection = refineSearch.getText().toString();
        String refineSearchtext = refineSearch.getText().toString();
        CounNameR = refineSearchtext.concat(CounName);
        Log.d("CounNameR",CounNameR);
        Log.d("refineSearchtext",refineSearchtext);
*/

        refineSearch = (EditText)findViewById(R.id.refine);






        final Button homePicsButton = (Button) findViewById(R.id.FindButton);
        homePicsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){



                refineSearchtext = refineSearch.getText().toString();

                Toast.makeText(getApplicationContext(),
                        refineSearchtext, Toast.LENGTH_SHORT).show();
                String CounNameRefine = " " + CounName;

                CounNameR = refineSearchtext.concat(CounNameRefine);

                Toast.makeText(getApplicationContext(),
                        CounNameR, Toast.LENGTH_SHORT).show();

                String uri = String.format(Locale.ENGLISH, "https://www.google.ie/maps/place/%s",CounNameR);

                //on Button click goes to URL link, %s is replaced with CounName which is sent when country is chosen
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });



    }

}
