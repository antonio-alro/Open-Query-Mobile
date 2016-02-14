package com.example.myapplication.utils;

import android.util.Log;

import com.example.myapplication.Property;
import com.example.myapplication.datamodels.DataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ANTONIO on 17/01/2016.
 */
public class SparqlQueryBuilder {

    /**
     * ATTRIBUTES of SparqlQueryBuilder class
     */
    private String sparqlQuery;
    private DataSet dataset;
    private ArrayList<Property> properties;
    private String orderType;
    private String orderProperty;
    private String limitValue;
    private String offsetValue;


    /**
     * DEFAULT CONSTRUCTOR for SparqlQueryBuilder class
     */
    public SparqlQueryBuilder() {
        super();
    }

    /**
     * PARAMETRIZED CONSTRUCTOR for SparqlQueryBuilder class
     */
    public SparqlQueryBuilder( DataSet dataset,  ArrayList<Property> properties, String orderType, String orderProperty, String limitValue, String offsetValue ) {
        super();
        this.sparqlQuery   = "";
        this.dataset       = dataset;
        this.properties    = properties;
        this.orderType     = orderType;
        this.orderProperty = orderProperty;
        this.limitValue    = limitValue;
        this.offsetValue   = offsetValue;
    }


    //METHODS TO PARSE A PROPERTY NAME (prefix and name in property of level one and property of level two)
    public Boolean isPropertyOfLevelTwo( String propertyName ) {
        if ( propertyName.contains(" - ") )
            return true;
        else
            return false;
    }

    public HashMap<String, String> parseName( String propertyName ) {
        HashMap<String, String> result = new HashMap<String, String>();

        if ( isPropertyOfLevelTwo(propertyName) ){
            String prop1 = propertyName.substring( 0, propertyName.indexOf( " - " ) );
            String prop2 = propertyName.substring( propertyName.indexOf( " - " )+3, propertyName.length() );

            String variableName1    = prop1.substring( prop1.indexOf(":")+1, prop1.length() );
            String variableName2    = prop2.substring( prop2.indexOf(":")+1, prop2.length() );

            result.put( "variableName1", variableName1 );
            result.put( "variableType1", prop1 );
            result.put( "variableName2", variableName1 + "_" + variableName2 );
            result.put( "variableType2", prop2 );
        }
        else {
            String variableName = propertyName.substring( propertyName.indexOf(":")+1, propertyName.length() );

            result.put( "variableName1", "" );
            result.put( "variableType1", "" );
            result.put( "variableName2", variableName );
            result.put( "variableType2", propertyName );
        }

        return result;
    }



    //METHODS TO BUILD A SYNTAX OF SPARQL QUERY
    public void addSelectStatement( String selectStatement ) {
        sparqlQuery += selectStatement;
    }

    public void addWhereStatement( String whereStatement ) {
        sparqlQuery += whereStatement;
    }



    // SELECT
    public String addVariableToSelectStatement( String variableName ) {

        String selectVariableStatement = "?" + variableName + " ";
        return selectVariableStatement;

    }

    //WHERE
    public void addOpeningWhereStatement() {
        sparqlQuery += "WHERE { ";
    }

    public void addClosingWhereStatement() {
        sparqlQuery += "} ";
    }

    public void addTypeWhereStatement() {
        sparqlQuery += "?uri a " + dataset.getFullName() + ". ";
    }

    public String addSingleVariableWhereStatement( String variableName, String type, Boolean mandatoryProperty ) {

        String variableWhereStatement = "?uri " + type + " ?" + variableName + ". ";

        if ( !mandatoryProperty ) {
            variableWhereStatement = addOptionalStatement( "?uri " + type + " ?" + variableName + ". " );
        }

        return variableWhereStatement;
    }

    public String addCompoundVariableWhereStatement( String variableName1, String type1, String variableName2, String type2, Boolean mandatoryProperty ) {

        String variableWhereStatement = "";
        String statement1 = "?uri "                    + type1 +  " ?" + variableName1 + ". ";
        String statement2 = "?" + variableName1 + " "  + type2 +  " ?" + variableName2 + ". ";

        if ( !mandatoryProperty ) {
            statement1 = addOptionalStatement( "?uri "                    + type1 + " ?" + variableName1 + ". " );
            statement2 = addOptionalStatement( "?" + variableName1 + " "  + type2 + " ?" + variableName2 + ". " );
        }

        variableWhereStatement = statement1 + statement2;

        return variableWhereStatement;
    }

    //OPTIONAL
    public String addOptionalStatement( String whereStatement ) {
        return "OPTIONAL { " + whereStatement + "}. ";
    }

    // FILTERS
    public String addVariableToFilterStatemnt( Property property, String variableName ){

        String filterVariableStatement = "";

        switch (property.getDatatype()) {
            case "xsd:double":
            case "xsd:float":
            case "xsd:int":
            case "xsd:decimal":

                if (property.getFilter() == "Rango(x,y)") {
                    filterVariableStatement += "FILTER ( ?" + variableName + " > " + "\"" + property.getFilterParam1() + "\"^^xsd:decimal" + " ). ";
                    filterVariableStatement += "FILTER ( ?" + variableName + " < " + "\"" + property.getFilterParam2() + "\"^^xsd:decimal" + " ). ";
                } else {
                    filterVariableStatement += "FILTER ( ?" + variableName + " " + property.getFilter() + " " + "\"" + property.getFilterParam1() + "\"^^xsd:decimal" + " ). ";
                }
                break;

            case "xsd:date":

                if (property.getFilter() == "Rango(x,y)") {
                    filterVariableStatement += "FILTER ( xsd:date(?" + variableName + ") > " + " xsd:date(\"" + property.getFilterParam1() + "\") ). ";
                    filterVariableStatement += "FILTER ( xsd:date(?" + variableName + ") < " + " xsd:date(\"" + property.getFilterParam2() + "\") ). ";
                } else {
                    filterVariableStatement += "FILTER ( xsd:date(?" + variableName + ") " + property.getFilter() + " xsd:date(\"" + property.getFilterParam1() + "\") ). ";
                }
                break;

            case "literal":

                if (property.getFilter() == "=") {
                    filterVariableStatement += "FILTER ( str(?" + variableName + ") " + property.getFilter() + " \"" + property.getFilterParam1() + "\" ). ";

                } else if (property.getFilter() == "que contenga") {
                    filterVariableStatement += "FILTER REGEX ( ?" + variableName + ", \"" + property.getFilterParam1() + "\", \"i\" ). ";
                }
                break;
            default:

                break;
        }

        return filterVariableStatement;
    }

    public void addFilterStatement( String filterStatement ) {
        sparqlQuery += filterStatement;
    }

    // ORDER_BY
    public String addVariableToOrderByStatement( String orderType, String variableName ) {

        String orderByVariableStatement = "";

        if ( !variableName.isEmpty() ){
            orderByVariableStatement = "ORDER BY " + orderType + "( ?" + variableName + " )";
        }
        return orderByVariableStatement;
    }

    public void addOrderByStatement( String orderByStatement ){
        sparqlQuery += orderByStatement;
    }

    // LIMIT
    public void addLimitStatement( String limit ) {
        if ( Integer.parseInt( limit ) > 0 )
            sparqlQuery += "LIMIT " + limit + " ";
        sparqlQuery += "";
    }

    // OFFSET
    public void addOffsetStatement( String offset ) {
        if ( Integer.parseInt( offset ) > 0 )
            sparqlQuery += "OFFSET " + offset + " ";
        sparqlQuery += "";
    }

    //BUILD SPARQL QUERY
    public void buildSparqlQuery() {

        String selectStatement = "SELECT ?uri ";
        String whereStatement  = "";
        String filterStatement = "";
        //String orderByStatement = "ORDER BY ASC(?";

        String propertyName;
        HashMap<String, String> parsedName;

//        Log.d("-PROPERTIES OF BUILDER-", String.valueOf(getProperties().size()));

        for( int i=0; i<properties.size(); i++ ) {
            if ( properties.get(i).isSelected() ) {
                propertyName = properties.get(i).getName();
                parsedName = parseName(propertyName);

                Log.d( "---- PROPERTY NAME ----", propertyName );
                Log.d( "---- PARSED NAME ----", "Var1: "  + parsedName.get("variableName1") +
                                                "Type1: " + parsedName.get("variableType1") +
                                                "Var2: "  + parsedName.get("variableName2") +
                                                "Type2: " + parsedName.get("variableType2")
                        );

                // Add the variable name of the current property to SELECT STATEMENT
                selectStatement += addVariableToSelectStatement( parsedName.get( "variableName2" ) );
                Log.d( "---- SELECT ----", selectStatement );

                // Add the variable statement in where of the current property to WHERE STATEMENT
                if (isPropertyOfLevelTwo(propertyName)) {
                    whereStatement += addCompoundVariableWhereStatement( parsedName.get("variableName1"),
                                                                         parsedName.get("variableType1"),
                                                                         parsedName.get("variableName2"),
                                                                         parsedName.get("variableType2"),
                                                                         properties.get(i).isMandatory()
                                                                       );
                } else {
                    whereStatement += addSingleVariableWhereStatement( parsedName.get("variableName2"),
                                                                       parsedName.get("variableType2"),
                                                                       properties.get(i).isMandatory()
                                                                     );
                }

                // Add the variable statement in filter of the current property to FILTER STATEMENT
                if ( properties.get(i).getFilter() != "Ninguno" ) {
                    filterStatement += addVariableToFilterStatemnt(properties.get(i), parsedName.get("variableName2"));
                }
                Log.d("------ FILTERS ------", properties.get(i).getName() + " __ " + properties.get(i).getDatatype());
                Log.d("------ FILTERS ------", properties.get(i).getAllowedFilters().toString() );
            }
        }

//        Log.d( "---- QUERY ----", sparqlQuery );

        // Add SELECT STATEMENT to SPARQL QUERY
        addSelectStatement(selectStatement);              // SELECT ....

        // Add WHERE OPENING STATEMENT to SPARQL QUERY
        addOpeningWhereStatement();                             // WHERE {

        // Add TYPE STATEMENT OF WHERE to SPARQL QUERY
        addTypeWhereStatement();                                    // ?uri a {dataset}

        // Add FULL WHERE STATEMENT to SPARQL QUERY
        addWhereStatement(whereStatement);                        // ?uri {type} ?{variable}

        //Add FULL FILTER STATMENT to SPARQL QUERY
        addFilterStatement( filterStatement );                    // FILTER ({variable} {operador} {valor})

        // Add WHERE CLOSING STATEMENT to SPARQL QUERY
        addClosingWhereStatement();                             // }

        // Add ORDER_BY STATEMENT to SPARQL QUERY               // ORDER_BY ...
        String orderByStatement = "";
        String variableName = parseName( this.orderProperty ).get( "variableName2" );
        orderByStatement = addVariableToOrderByStatement( this.getOrderType(), variableName );
        Log.d( "---- ORDER BY ----", orderByStatement );

        addOrderByStatement( orderByStatement );

        // Add LIMIT STATEMENT to SPARQL QUERY
        addLimitStatement(this.getLimitValue() );             // LIMIT ...

        // Add OFFSET STATEMENT to SPARQL QUERY
        addOffsetStatement( this.getOffsetValue() );           // OFFSET ...

    }






















    //GETTERS and SETTERS METHODS FOR ATTRIBUTES OF THE CLASS
    /**
     * Get method for sparqlQuery attribute
     * @return      text of a sparql query
     */
    public String getSparqlQuery() {
        return sparqlQuery;
    }
    /**
     * Set method for sparqlQuery attribute
     * @param sparqlQuery       text of a sparql query
     */
    public void setSparqlQuery( String sparqlQuery ) {
        this.sparqlQuery = sparqlQuery;
    }


    /**
     * Get method for dataset attribute
     * @return      dataset attribute
     */
    public DataSet getDataset() {
        return dataset;
    }
    /**
     * Set method for dataset attribute
     * @param dataset       a dataset
     */
    public void setDataSet( DataSet dataset ) {
        this.dataset = dataset;
    }


    /**
     * Get method for properties attribute
     * @return   properties attribute
     */
    public List<Property> getProperties() {
        return properties;
    }
    /**
     * Set method for properties attribute
     * @param properties    list of properties
     */
    public void setProperties( ArrayList<Property> properties ) {
        this.properties = properties;
    }


    /**
     * Get method for orderType attribute
     * @return  the order type (ASC o DESC)
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * Set method for orderType attribute
     * @param orderType
     */
    public void setOrderType( String orderType ) {
        this.orderType = orderType;
    }


    /**
     * Get method for orderProperty attribute
     * @return  the property to order
     */
    public String getOrderProperty() {
        return orderProperty;
    }

    /**
     * Set method for orderProperty attribute
     * @param orderProperty
     */
    public void setOrderProperty( String orderProperty ) {
        this.orderProperty = orderProperty;
    }

    /**
     * Get method for limit_value attribute
     * @return  the limit value
     */
    public String getLimitValue() {
        return limitValue;
    }

    /**
     * Set method for limit_value attribute
     * @param limitValue
     */
    public void setLimitValue( String limitValue ) {
        this.limitValue = limitValue;
    }


    /**
     * Get method for offset_value attribute
     * @return  the offset value
     */
    public String getOffsetValue() {
        return offsetValue;
    }

    /**
     * Set method for offset_value attribute
     * @param offsetValue
     */
    public void setOffsetValue( String offsetValue ) {
        this.offsetValue = offsetValue;
    }


    /**
     * Method to show SPARQLQUERYBUILDER class as String
     * @return       a string with the information of class
     */
    public String to_s(){
        String propiedades = "";
        for ( int i=0; i<this.getProperties().size(); i++ ) {
            if ( this.getProperties().get( i ).isSelected() ) {
                propiedades += this.getProperties().get(i).getName() + " ";
            }
        }
        return this.getDataset().getFullName() + "\n" + propiedades;
    }


}
