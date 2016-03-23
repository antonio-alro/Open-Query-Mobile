package com.example.myapplication.utils;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by ANTONIO on 23/03/2016.
 */
public class DataSourceSingleton {

    /**
     * ATTRIBUTES of DataSourceSingleton Class
     */
    private static DataSourceSingleton mInstance;   // Instance of Singleton Class
    private ArrayList<String> mDataSources;         // Array of data sources


    /**
     * CONSTRUCTOR for PrefixesManagerSingleton Class
     */
    private DataSourceSingleton() {
        mDataSources = getDataSources();
    }


    /**
     * METHOD to get the instance of PrefixesManagerSingleton Class
     * @return mInstance
     */
    public static synchronized DataSourceSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new DataSourceSingleton();
        }
        return mInstance;
    }


    /**
     * METHOD to get the data sources list of the class
     * @return mDataSources
     */
    public ArrayList getDataSources() {
        if (mDataSources == null) {
            mDataSources = new ArrayList<String>();
        }
        return mDataSources;
    }


    /**
     * Method to get the size of data sources list
     * @return the number of data sources
     */
    public int countDataSources() {
        return getDataSources().size();
    }

    /**
     * METHOD to add a new pair (prefix, URI) to the prefixes map of the class
     * @param dataSource    data source url
     */
    public void addToDataSources( String dataSource ) {
        getDataSources().add( dataSource );
    }









}
