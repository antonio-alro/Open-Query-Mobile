package com.example.myapplication.datamodels;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by ANTONIO on 23/01/2016.
 */
public class Resource {

    private LinkedHashMap<String,String> propertiesValues;


    /**
     * DEFAULT CONSTRUCTOR for Resource class
     */
    public Resource() {
        super();
        this.setPropertiesValues(new LinkedHashMap<String,String>());
    }


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
        String key = (String) this.getPropertiesValues().keySet().toArray()[0];
        return this.getPropertiesValues().get( key );
    }

    /**
     * Method to get the label value of the resource
     * @return      the label of the resource ( label is the first property of the resource without the resource uri )
     */
    public String getLabel() {
        String key = (String) this.getPropertiesValues().keySet().toArray()[1];
        return this.getPropertiesValues().get( key );
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
            resource += key + ":" + this.getPropertiesValues().get(key) + ",";
        }

        resource += "]";
        return resource;

    }


}
