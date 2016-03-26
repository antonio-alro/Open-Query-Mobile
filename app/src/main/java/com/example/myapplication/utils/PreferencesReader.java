package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by ANTONIO on 26/03/2016.
 */
public class PreferencesReader {

    /**
     * Context of application
     */
    private Context context;



    /**
     * PARAMETRIZED CONSTRUCTOR
     * @param context
     */
    public PreferencesReader( Context context ) {
        this.context = context;
    }



    /**
     * Method to read the sparql preferences from the App Preferences
     * @return  the sparql preferences ( orderType, orderProperty, limitValue, offsetValue)
     */
    public ArrayList<String> readSparqlPreferences() {

        ArrayList<String> preferences = new ArrayList<>();
        String orderType     = "";
        String orderProperty = "";
        String limitValue    = "0";
        String offsetValue   = "0";

        // Get the preferences
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );

        Boolean orderEnabled  = sharedPreferences.getBoolean("order_by_check", false);
        Boolean limitEnabled  = sharedPreferences.getBoolean( "limit_check", false );
        Boolean offsetEnabled = sharedPreferences.getBoolean( "offset_check", false );

        if ( orderEnabled ) {
            if ( sharedPreferences.getString( "order_by_type", "" ).equals( "Ascendentemente" ) ) {
                orderType = "ASC";
            }
            else if ( sharedPreferences.getString( "order_by_type", "" ).equals( "Descendentemente" ) ) {
                orderType = "DESC";
            }
            orderProperty = sharedPreferences.getString( "order_by_property", "" );
        }

        if ( limitEnabled ) {
            limitValue = sharedPreferences.getString( "limit", "" );
        }

        if ( offsetEnabled ) {
            offsetValue = sharedPreferences.getString( "offset", "" );;
        }

        preferences.add( orderType );
        preferences.add( orderProperty );
        preferences.add( limitValue );
        preferences.add( offsetValue );

        return preferences;
    }



    /**
     * Method to read the data source preferences from the App Preferences
     * @return the url of data source
     */
    public String readDataSourcePreferences(){

        String dataSourceURL = "";

        // Get the preferences
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );

        Boolean dataSourceEnabled  = sharedPreferences.getBoolean("data_source_check", false);
        String dataSourcePreference = sharedPreferences.getString( "data_source", "" );

        if ( dataSourceEnabled ) {
            Log.d("DATA SOURCE", sharedPreferences.getString("data_source", ""));
            if ( dataSourcePreference.equals( "Ninguna" ) ) {
                dataSourceURL = "http://opendata.caceres.es/sparql";    // default value
            }
            else {
                dataSourceURL = dataSourcePreference;
            }
        }
        else {
            dataSourceURL = "http://opendata.caceres.es/sparql";        // default value
        }

        return dataSourceURL;
    }











}
