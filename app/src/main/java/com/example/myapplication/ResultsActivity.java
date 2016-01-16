package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ResultsActivity extends AppCompatActivity implements ResultsFragmentList.ResultsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View v = (View) findViewById(R.id.toolbar_layout);

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
        ResultsFragmentList frgList
                = (ResultsFragmentList)getSupportFragmentManager()
                .findFragmentById(R.id.FrgList);
        //Le ponemos el Listener al fragment
        frgList.setResultsListener(this);

    }

    /**
     * Método del interfaz de ResultListener
     * @param element
     */
    @Override
    public void onElementSelected(String element) {
        Intent i = new Intent(this, ResultsActivityDetail.class);
        i.putExtra( ResultsActivityDetail.EXTRA_TEXT, element );
        startActivity(i);
    }
}