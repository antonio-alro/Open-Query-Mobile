package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Parts of email
                String to        = getResources().getString( R.string.about_us_contact_value );
                String signature = "\n\n\n" +
                                   "----------------------------------------------" +
                                   "\n" +
                                   getResources().getString( R.string.about_us_email_signature );

                Intent mailIntent = new Intent ( Intent.ACTION_VIEW , Uri.parse( "mailto:" + to ) );
                mailIntent.putExtra( Intent.EXTRA_TEXT, signature );
                startActivity( mailIntent );

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
