package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    /**
     * Atributo para guardar la URL de la página web que se va a mostrar
     */
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_web );

        //Obtener la toolbar desde el layout
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );

        // Indicar que la toolbar actúe como action bar
        setSupportActionBar(toolbar);


        // Obtenemos la url pasada en los argumentos
        // Get the parameters of the intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if ( bundle != null ){
            url = bundle.getString( "URL" );
        }

        // Get the webView from layout
        WebView webView = (WebView) this.findViewById( R.id.webView );

        // Config the webView
        // ZoomControls through gestures
        webView.getSettings().setBuiltInZoomControls( true );
        webView.getSettings().setDisplayZoomControls(false);
        // Fit the webView to screen size
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        // Load the url in the webView
        webView.loadUrl( url );


    }
}
