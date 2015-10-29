package com.example.myapplication;

/**
 * Created by ANTONIO on 21/10/2015.
 */
public class Property {

    boolean selected = false;
    String name = null;
    String filter = null;

    public Property(boolean selected, String name) {
        super();
        this.name = name;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFilter() {
        return filter;
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }





}
