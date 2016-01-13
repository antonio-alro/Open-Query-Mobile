package com.example.myapplication.utils;

import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ANTONIO on 10/01/2016.
 */
public class PrefixesManagerSingleton {

    /**
     * ATTRIBUTES of PrefixesManagerSingleton Class
     */
    private static PrefixesManagerSingleton mInstance;  // Instance of Singleton Class
    private TreeMap mPrefixes;                          // Map of prefixes


    /**
     * CONSTRUCTOR for PrefixesManagerSingleton Class
     */
    private PrefixesManagerSingleton() {
        mPrefixes = getPrefixes();
    }


    /**
     * METHOD to get the instance of PrefixesManagerSingleton Class
     * @return mInstance
     */
    public static synchronized PrefixesManagerSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new PrefixesManagerSingleton();
        }
        return mInstance;
    }


    /**
     * METHOD to get the prefixes map of the class
     * @return mPrefixes
     */
    public TreeMap getPrefixes() {
        if (mPrefixes == null) {
            mPrefixes = new TreeMap();
        }
        return mPrefixes;
    }


    public int countPrefixes() {
        return getPrefixes().size();
    }


    /**
     * METHOD to add a new pair (prefix, URI) to the prefixes map of the class
     * @param prefix    prefix name
     * @param uri       prefix URI
     */
    public void addToPrefixes( String prefix, String uri ) {
        getPrefixes().put(prefix, uri);
    }


    /**
     * Parse the HTML code of response and save the prefixes and theirs URI in a map
     * @param response HTML code of response
     */
    public void parseResponseHTML( String response){
        String[] temp;
        String prefix;
        String URI;

        //Get the rows of tableresult HTML
        String prefixes_source[] = response.split( "<tr><td>" );

        for (int i=1; i<prefixes_source.length; i++){
            //Split the row
            temp = prefixes_source[i].split( "</td><td>" );
            //Get the prefix
            prefix = temp[ 0 ];
            //Get the URI for the prefix
            URI = temp[ 1 ].substring( 0 , temp[1].indexOf("</td></tr>") );

            //Save the prefixes and its URI in a map
            addToPrefixes( URI, prefix );
        }
    }


    //
    // BORRAR DESPUES PORQUE SOLO SE UTILIZA EN PRUEBAS
    public String getTreeMapString(){
        String results = "";
        Iterator it = getPrefixes().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            results = results.concat( e.getKey() + "\n" + e.getValue() + "\n\n" );
        }

        return results;
    }

}
