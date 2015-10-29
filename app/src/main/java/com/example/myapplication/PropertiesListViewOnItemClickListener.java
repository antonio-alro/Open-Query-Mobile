package com.example.myapplication;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by ANTONIO on 21/10/2015.
 */
public class PropertiesListViewOnItemClickListener implements AdapterView.OnItemClickListener {

    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        // When clicked, show a toast with the TextView text
//        Property property = (Property) parent.getItemAtPosition(position);
//        Toast.makeText(parent.getContext(),
//                "Clicked on Row: " + property.getName(),
//                Toast.LENGTH_LONG).show();
    }

}
