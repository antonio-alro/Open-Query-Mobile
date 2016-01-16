package com.example.myapplication;

/**
 * Created by ANTONIO on 21/10/2015.
 */
public class Property {


    /**
     * ATTRIBUTES of Property class
     */
    private boolean selected     = false;
    private String  name         = null;
    private boolean mandatory    = false;
    private String  filter       = null;
    private String  filterParam1 = "";
    private String  filterParam2 = "";


    
    /**
     * PARAMETRIZED CONSTRUCTOR for Property class
     * @param selected
     * @param name
     */
    public Property(boolean selected, String name) {
        super();
        this.name = name;
        this.selected = selected;
    }



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
     * Set method for NMAE attribute
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
     * Method to show PROPERTY class as String
     * @return
     */
    public String to_s(){
        return "[" + this.getName() + "," + this.isMandatory() + "," + this.getFilter() + ","
                   + this.getFilterParam1() + "," + this.getFilterParam2() + "] ";
    }



}
