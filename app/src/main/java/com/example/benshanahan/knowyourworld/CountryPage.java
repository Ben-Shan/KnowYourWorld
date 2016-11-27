package com.example.benshanahan.knowyourworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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
    public boolean noFlag = FALSE;



    public Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_page);

        Intent myIntent = getIntent(); // gets intent previously created in MainActivity
        CounName = myIntent.getStringExtra("counName"); //retrives string sent in intent
        CounCode = myIntent.getStringExtra("counCode");

        CounCodeLower = CounCode.toLowerCase();//url requires country code in lowercase

        new DownloadImageTask((ImageView) findViewById(R.id.CCode))
                //.execute calls the AsyncTask function 'PostOnExecute, which after a successful pull of image, will hold the flag
                .execute("http://flagpedia.net/data/flags/normal/" + CounCodeLower + ".png");//website containing most flags of the world differing only by country code

        if(noFlag == TRUE)
        {
            Toast.makeText(getApplicationContext(), "No flag", Toast.LENGTH_LONG).show();
        }

        TextView CNameTextView = (TextView) findViewById(R.id.CName); //displays country name
        CNameTextView.setText(CounName);//dynamically changes text depending on name sent to intent

        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();//back button ends intent when clicked


            }
        });

         refineSearch = (EditText)findViewById(R.id.refine); //saves user inputted word, as a edit text



        final Button homePicsButton = (Button) findViewById(R.id.FindButton);
        homePicsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                refineSearchtext = refineSearch.getText().toString();//converts inputted text into string

                //if user has entered a word, the search is updated to include it in the search
                if(refineSearchtext != null) {
                    String CounNameRefine = " " + CounName;

                    CounNameR = refineSearchtext.concat(CounNameRefine);
                }
                //else, the input is ignored
                else{
                    CounNameR = CounName;
                }

                Toast.makeText(getApplicationContext(),
                        CounNameR, Toast.LENGTH_SHORT).show(); //toasts the city & country or just the country, depending on the if/else

                String uri = String.format(Locale.ENGLISH, "https://www.google.ie/maps/place/%s",CounNameR);
                //saves the url including the city/country string as a string to be used to search for the country

                //on Button click goes to URL link, %s is replaced with CounNameR which is sent when country is chosen
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                //opens a new intent, if user has Google Maps on phone it will ask if they want to use the internet or the app
                //using this instead of an imbedded map means the user can still use this function on an outdated software as it will work if they only have a web browser
                startActivity(intent);
            }
        });



    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        //AsyncTask allows a short term thread to be run at the same time as the currnet one
        ImageView bmImage;//sets imageview that will hold flag

        public DownloadImageTask(ImageView flagPos) {
            //reads in imageview to hold flag
            this.bmImage = flagPos; //declares it within ckass
        }

        protected Bitmap doInBackground(String... urls) {
            //String ... is similar to String[] however can omit creation of array, which is needed if theres no flag
            //doInBackground is a AsyncTask function used to run a thread in the background

            String urldisplay = urls[0];//takes first item of array, i.e: the inputted url
            Bitmap flagIc = null;
            //declares bitmap, location flag will be temporarily stored
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                //inputStream takes in a stream of bytes
                //java.net.URL is used to access whatever is in the inputted url, in the context it is taken and saved within the InputStream
                flagIc = BitmapFactory.decodeStream(in);
                //BitmapFactory creates bitmap object from source, i.e: the decoded stream which has been created from the inputted url
            } catch (Exception e) {
                noFlag = TRUE;

            }
            return flagIc;

        }

        protected void onPostExecute(Bitmap flagReturn) {
            //onPostExecute also runs on the AsyncTask thread and automatically takes in the return value of the doInBackgroud, i.e: flagIc
            bmImage.setImageBitmap(flagReturn);//converts bitmap to image
        }
    }

}
