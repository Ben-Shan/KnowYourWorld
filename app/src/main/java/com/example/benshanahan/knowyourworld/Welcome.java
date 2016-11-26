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

        TextView welcome = (TextView) findViewById(R.id.welcometext);
        TextView welcomeTitle = (TextView) findViewById(R.id.welcomeTitle);


        RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.welcomelayout);
        rlayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }

        });





    }
}
