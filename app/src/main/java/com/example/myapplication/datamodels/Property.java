package com.example.myapplication.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ANTONIO on 21/10/2015.
 */
public class Property implements Parcelable {


    /**
     * ATTRIBUTES of Property class
     */
    private boolean selected     = false;
    private String  name         = null;
    private boolean mandatory    = false;
    private String  filter       = null;
    private String  filterParam1 = "";
    private String  filterParam2 = "";
    private String  datatype     = "";



    /**
     * PARAMETRIZED CONSTRUCTOR for Property class
     * @param selected
     * @param name
     */
    public Property( boolean selected, String name, String datatype ) {
        super();
        this.name     = name;
        this.selected = selected;
        this.datatype = datatype;
    }


    /**
     * CONSTRUCTOR TO CREATE THE OBJECT FROM A PARCELABLE
     * @param in
     */
    protected Property(Parcel in) {
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
        dest.writeByte( (byte) ( isSelected() == true ? 1 : 0 ) );
        dest.writeString( getName() );
        dest.writeByte( (byte) ( isMandatory() == true ? 1 : 0 ) );
        dest.writeString( getFilter() );
        dest.writeString( getFilterParam1() );
        dest.writeString( getFilterParam2() );
        dest.writeString( getDatatype() );
    }

    /**
     * Method to read the object from a Parcel. It is important reading data in the same order that when they were written
     * @param in    Parcel with the data to read
     */
    private void readFromParcel(Parcel in) {
        setSelected( in.readByte() == 1 ? true : false );
        setName( in.readString() );
        setMandatory( in.readByte() == 1 ? true : false );
        setFilter( in.readString() );
        setFilterParam1( in.readString() );
        setFilterParam2( in.readString() );
        setDatatype( in.readString() );
    }

    /**
     * CREATOR to use the object in arrays
     */
    public static final Parcelable.Creator<Property> CREATOR = new Parcelable.Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };





    //GETTERS and SETTERS METHODS FOR ATTRIBUTES OF THE CLASS
    /**
     * Get method for SELECTED attribute
     * @return
     */
    public boolean isSelected() {
        return selected;
    }
    /**
     * Set method for SELECTED attribute
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    /**
     * Get method for NAME attribute
     * @return
     */
    public String getName() {
        return name;
    }
    /**
     * Set method for NAME attribute
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Get method for MANDATORY attribute
     * @return
     */
    public boolean isMandatory() {
        return mandatory;
    }
    /**
     * Set method for MANDATORY attribute
     * @param mandatory
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * Get method for FILTER attribute
     * @return
     */
    public String getFilter() {
        return filter;
    }
    /**
     * Set method for FILTER attribute
     * @param filter
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }


    /**
     * Get method for FILTERPARAMETER1 attribute
     * @return
     */
    public String getFilterParam1(){
        return filterParam1;
    }
    /**
     * Set method for FILTERPARAMETER1 attribute
     * @param param1
     */
    public void setFilterParam1(String param1){
        this.filterParam1 = param1;
    }

    /**
     * Get method for FILTERPARAMETER2 attribute
     * @return
     */
    public String getFilterParam2(){
        return filterParam2;
    }
    /**
     * Set method for FILTERPARAMETER1 attribute
     * @param param2
     */
    public void setFilterParam2(String param2){
        this.filterParam2 = param2;
    }

    /**
     * Get method for DATATYPE attribute
     * @return
     */
    public String getDatatype() {
        return datatype;
    }
    /**
     * Set method for DATATYPE attribute
     * @param datatype
     */
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }



    /**
     * Method to get the ALLOWED FILTERS of Property from its DATATYPE
     * @return
     */
    public ArrayList<String> getAllowedFilters() {
        ArrayList<String> allowedFilters = new ArrayList<String>();
        String datatype = this.getDatatype();

        switch ( datatype ) {
            case "xsd:double":
            case "xsd:float":
            case "xsd:int":
            case "xsd:decimal":
            case "xsd:date":
                allowedFilters.add( "Ninguno" );
                allowedFilters.add( "=" );
                allowedFilters.add( "<" );
                allowedFilters.add( ">" );
                allowedFilters.add( "<=" );
                allowedFilters.add( ">=" );
                allowedFilters.add( "Rango(x,y)" );
                break;

            case "literal":
                allowedFilters.add( "Ninguno" );
                allowedFilters.add( "que contenga" );
                allowedFilters.add( "=" );
                break;

            default:
                allowedFilters.add( "Ninguno" );
                break;
        }

        return allowedFilters;
    }



    /**
     * Method to show PROPERTY class as String
     * @return      a string with the information of class
     */
    public String to_s(){
        return "[" + this.getName()         + ","
                   + this.getDatatype()     + ","
                   + this.isMandatory()     + ","
                   + this.getFilter()       + ","
                   + this.getFilterParam1() + ","
                   + this.getFilterParam2() +
                "] ";
    }



}
