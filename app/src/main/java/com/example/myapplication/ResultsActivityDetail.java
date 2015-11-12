package com.example.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ResultsActivityDetail extends AppCompatActivity {

    public static final String EXTRA_TEXT = "ELEMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Obtenemos una referencia al fragment de esta actividad mediante el fragment manager
        ResultsFragmentDetail FrgDetail =
                (ResultsFragmentDetail)getSupportFragmentManager()
                        .findFragmentById(R.id.FrgDetail);

        //Mostramos el parámetro del intent en el fragment de detalle
        FrgDetail.showDetail(getIntent().getStringExtra(EXTRA_TEXT));
    }
}
