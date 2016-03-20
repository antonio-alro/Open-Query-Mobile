package com.example.myapplication.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by ANTONIO on 23/01/2016.
 */
public class Resource implements Parcelable {

    private LinkedHashMap<String,String> propertiesValues;


    /**
     * DEFAULT CONSTRUCTOR for Resource class
     */
    public Resource() {
        super();
        this.setPropertiesValues( new LinkedHashMap<String, String>() );
    }

    /**
     * PARAMETRIZED CONSTRUCTOR for Resource class
     */
    public Resource( LinkedHashMap<String,String> propertiesValues ) {
        super();
        this.setPropertiesValues(propertiesValues);
    }


    /**
     * CONSTRUCTOR TO CREATE THE OBJECT FROM A PARCELABLE
     * @param in
     */
    protected Resource(Parcel in) {
        readFromParcel(in);
    }



    // METHOS OF PARCELABLE INTERFACE
    /**
     * Mandatory method of Parcelable interface
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method to write the object in a Parcel. The order of the attributes is important
     * @param dest      Parcel where it is going to write
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable( getPropertiesValues() );
    }

    /**
     * Method to read the object from a Parcel. It is important reading data in the same order that when they were written
     * @param in    Parcel with the data to read
     */
    private void readFromParcel(Parcel in) {
        setPropertiesValues( (LinkedHashMap<String, String>) in.readSerializable() );
    }

    /**
     * CREATOR to use the object in arrays
     */
    public static final Creator<Resource> CREATOR = new Creator<Resource>() {
        @Override
        public Resource createFromParcel(Parcel in) {
            return new Resource(in);
        }

        @Override
        public Resource[] newArray(int size) {
            return new Resource[size];
        }
    };



    //GETTERS and SETTERS METHODS FOR ATTRIBUTES OF THE CLASS
    /**
     * Get method for propertiesValues attribute
     * @return      the properties of the resource
     */
    public LinkedHashMap<String, String> getPropertiesValues() {
        return propertiesValues;
    }

    /**
     * Set method for propertiesValues attribute
     * @param propertiesValues      the properties of the resource
     */
    public void setPropertiesValues(LinkedHashMap<String, String> propertiesValues) {
        this.propertiesValues = propertiesValues;
    }



    // METHODS TO HANDLE PROPERTIES VALUES MAP
    /**
     * Method to add a new element to propertiesValues
     * @param propertyName
     * @param propertyValue
     */
    public void addPropertyValue( String propertyName, String propertyValue ) {
        this.getPropertiesValues().put( propertyName, propertyValue );
    }

    /**
     * Method to get the uri value of the resource
     * @return      the uri of the resource
     */
    public String getUri() {
        String uri = "";
        if ( this.getPropertiesValues().size() > 0 ) {
            String key = (String) this.getPropertiesValues().keySet().toArray()[0];
            uri = this.getPropertiesValues().get( key );
        }
        return uri;
    }

    /**
     * Method to get the label value of the resource
     * @return      the label of the resource ( label is the first property of the resource without the resource uri )
     */
    public String getLabel() {
        String label = "";

        if ( this.getPropertiesValues().size() >= 2 ) {

            String key = "label";
            if ( !this.getPropertiesValues().keySet().contains( key ) ) {
                key = (String) this.getPropertiesValues().keySet().toArray()[1];
                label = key + ": ";
            }
            label += this.getPropertiesValues().get( key );

        }
        return label;
    }

    /**
     * Method to get the latitude value of the resource if this exists
     * @return      the latitude value of resource
     */
    public String getLatitude() {
        return this.getPropertiesValues().get( "lat" );
    }

    /**
     * Method to get the longitude value of the resource if this exists
     * @return      the longitude value of resource
     */
    public String getLongitude() {
        return this.getPropertiesValues().get( "long" );
    }



    /**
     * Method to show RESOURCE class as String
     * @return      a string with the information of class
     */
    public String to_snippet(){

        String resource = "";

        Iterator it = this.getPropertiesValues().keySet().iterator();
        while(it.hasNext()){
            String key = (String) it.next();
            resource += key + ": " + this.getPropertiesValues().get(key) + "\n";
        }

        return resource;

    }



    /**
     * Method to show RESOURCE class as String
     * @return      a string with the information of class
     */
    public String to_s(){

        String resource = "[";

        Iterator it = this.getPropertiesValues().keySet().iterator();
        while(it.hasNext()){
            String key = (String) it.next();
            resource += key + " : " + this.getPropertiesValues().get(key) + "    ";
        }

        resource += "]";
        return resource;

    }


}
