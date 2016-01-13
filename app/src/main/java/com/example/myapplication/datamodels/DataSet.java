package com.example.myapplication.datamodels;

/**
 * Created by ANTONIO on 13/01/2016.
 */
public class DataSet {

    /**
     * Attributes of DataSet class
     */
    String name   = "";
    String prefix = "";


    /**
     * Constructor for Property class
     * @param name      name of the dataset
     * @param prefix    prefix of the dataset
     */
    public DataSet( String name, String prefix ) {
        super();
        this.name = name;
        this.prefix = prefix;

    }


    /**
     * Get method for Name attribute
     * @return  the name of the dataset
     */
    public String getName() {
        return name;
    }

    /**
     * Set method for NAME attribute
     * @param name  name for the dataset
     */
    public void setName( String name ) {
        this.name = name;
    }


    /**
     * Get method for Name attribute
     * @return  the prefix of the dataset
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Set method for NAME attribute
     * @param prefix  prefix for the dataset
     */
    public void setPrefix( String prefix ) {
        this.prefix = prefix;
    }


    /**
     * Method to get the full name (prefix + name) of the dataset
     * @return  the full name of the dataset
     */
    public String getFullName() {
        return getPrefix() + ":" + getName();
    }




}
