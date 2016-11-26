package com.example.benshanahan.knowyourworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Ben Shanahan on 25/11/2016.
 */

public class Welcome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView welcome = (TextView) findViewById(R.id.welcometext); //post welcome message
        TextView welcomeTitle = (TextView) findViewById(R.id.welcomeTitle);//posts welcome title


        RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.welcomelayout);//new relative layoit
        rlayout.setOnClickListener(new View.OnClickListener() { //listens for relative layout to be clicked

            @Override
            public void onClick(View v) {

                finish();//when relative layout is clicked, the intent closes
            }

        });





    }
}
