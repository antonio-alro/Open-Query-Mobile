package com.example.myapplication.preferences;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.utils.DataSourceSingleton;

import java.util.ArrayList;

public class DataSourcePreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_source_preferences);

        //Obtener la toolbar desde el layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Indicar que la toolbar actúe como action bar
        setSupportActionBar(toolbar);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
//                .replace( android.R.id.content, new SparqlPreferencesFragment() )
                .replace(R.id.data_source_preferences_content_frame, new DataSourcePreferencesFragment())
                .commit();
    }




    public static class DataSourcePreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        private ListPreference listPreference;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);

            // Load the preferences from an XML resource
            addPreferencesFromResource( R.xml.pref_data_source );


            // Get the listPreference of data_source to set the options of the list
//            final ListPreference listPreference = (ListPreference) findPreference( "data_source" );
            listPreference = (ListPreference) findPreference( "data_source" );

            String defaultValue = getResources().getString( R.string.default_value_data_source );
            ArrayList<String> dataSources = DataSourceSingleton.getInstance().getDataSources();

            // THIS IS REQUIRED IF YOU DON'T HAVE 'entries' and 'entryValues' in your XML
            setListPreferenceData( listPreference, dataSources, defaultValue );


            //Get the editTextPreference of new_data_source to save the new input
            final EditTextPreference editTextPreference = (EditTextPreference) findPreference( "new_data_source" );
            //Set the listener to detect when the text change
            editTextPreference.setOnPreferenceChangeListener(this);



        }



        /**
         * Method to set listpreference options
         * @param listPreference    listPreference to set the data
         * @param data              the options of listPreference
         */
        protected static void setListPreferenceData( ListPreference listPreference, ArrayList<String> data, String defaultValue ) {

            CharSequence[] entries;
            CharSequence[] entryValues;

            if ( data.size() > 0 ) {
                entries     = new CharSequence[ data.size() ];
                entryValues = new CharSequence[ data.size() ];

                for( int i=0; i<data.size(); i++ ) {
                    entries[ i ]     = data.get( i );
                    entryValues[ i ] = data.get( i );
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


        /**
         * Method that is executed when the preference value changes
         * @param preference    the preference
         * @param newValue      the new value of the preference
         * @return
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            // Save the new value of the preference
            String value = (String) newValue;
            DataSourceSingleton.getInstance().addToDataSources( value );

            // Update data for the listPreference
            String defaultValue           = getResources().getString( R.string.default_value_data_source );
            ArrayList<String> dataSources = DataSourceSingleton.getInstance().getDataSources();
            setListPreferenceData( listPreference, dataSources, defaultValue );

            return false;
        }
    }


}
