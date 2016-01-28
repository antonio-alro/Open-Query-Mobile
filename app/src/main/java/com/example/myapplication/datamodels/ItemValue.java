package com.example.myapplication.datamodels;

/**
 * Created by ANTONIO on 28/01/2016.
 */
public class ItemValue {

    private String label = "";

    private String value = "";


    /**
     * DEFAULT CONSTRUCTOR for Resource class
     */
    public ItemValue() {
        super();
    }

    /**
     * PARAMETRIZED CONSTRUCTOR for Resource class
     */
    public ItemValue( String label, String value ) {
        super();
        this.setLabel( label );
        this.setValue( value );
    }


    //GETTERS and SETTERS METHODS FOR ATTRIBUTES OF THE CLASS
    /**
     *
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
