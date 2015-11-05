package com.example.myapplication;

/**
 * Created by ANTONIO on 21/10/2015.
 */
public class Property {

    boolean selected = false;
    String name = null;
    String filter = null;
    String filterParam1 = "param1";
    String filterParam2 = "param2";

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

    public String getFilterParam1(){
        return filterParam1;
    }
    public void setFilterParam1(String param1){
        this.filterParam1 = param1;
    }

    public String getFilterParam2(){
        return filterParam2;
    }
    public void setFilterParam2(String param2){
        this.filterParam2 = param2;
    }

    public String to_s(){
        return "[" + this.getName() + "," + this.getFilter() + ","
                   + this.getFilterParam1() + "," + this.getFilterParam2() + "] ";
    }



}
