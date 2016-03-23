package com.example.myapplication.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.example.myapplication.datamodels.Property;
import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * Created by ANTONIO on 13/02/2016.
 */
public class SparqlPreferencesActivity extends AppCompatActivity {

    /**
     * Selected properties by the user
     */
    static ArrayList<Property> selectedProperties;



    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparql_preferences);

        //Obtener la toolbar desde el layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Indicar que la toolbar actúe como action bar
        setSupportActionBar(toolbar);
        // Habilitar la navegación hacia arriba
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtenemos los datos de la lista con los datos que vienen en los argumentos
        // Get the parameters of the intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if ( bundle != null ){
            selectedProperties = bundle.getParcelableArrayList( "SELECTED PROPERTIES" );
        }




        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
//                .replace( android.R.id.content, new SparqlPreferencesFragment() )
                .replace(R.id.sparql_preferences_content_frame, new SparqlPreferencesFragment())
                .commit();

    }


    public static class SparqlPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);

            // Load the preferences from an XML resource
            addPreferencesFromResource( R.xml.pref_sparql );


            // Get the listPreference of order_by_property to set the options of the list
            final ListPreference listPreference = (ListPreference) findPreference( "order_by_property" );

            String defaultValue = getResources().getString( R.string.default_value_order_property );

            // THIS IS REQUIRED IF YOU DON'T HAVE 'entries' and 'entryValues' in your XML
            setListPreferenceData( listPreference, selectedProperties, defaultValue );

        }


        /**
         * Method to set listpreference options
         * @param listPreference    listPreference to set the data
         * @param data              the options of listPreference
         */
        protected static void setListPreferenceData( ListPreference listPreference, ArrayList<Property> data, String defaultValue ) {

            CharSequence[] entries;
            CharSequence[] entryValues;

            if ( data.size() > 0 ) {
                entries     = new CharSequence[ data.size() ];
                entryValues = new CharSequence[ data.size() ];

                for( int i=0; i<data.size(); i++ ) {
                    entries[ i ]     = data.get( i ).getName();
                    entryValues[ i ] = data.get( i ).getName();
                }

            }

            else {
                entries     = new CharSequence[ 1 ];
                entryValues = new CharSequence[ 1 ];

                entries[ 0 ]     = defaultValue;
                entryValues[ 0 ] = defaultValue;
            }


            // Set entries and entryvalues to listPreference and set the default value
            listPreference.setEntries( entries );
            listPreference.setDefaultValue( entryValues[ 0 ] );
            listPreference.setEntryValues( entryValues );
        }



    }


}
