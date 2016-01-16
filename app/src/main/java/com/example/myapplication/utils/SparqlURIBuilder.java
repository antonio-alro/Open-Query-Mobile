package com.example.myapplication.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ANTONIO on 16/01/2016.
 */
public class SparqlURIBuilder {

    /**
     * ATTRIBUTES for SparqlURIBuilder class
     */
    private String preQuery;
    private String graph;
    private String sparqlQuery;
    private String format;


    /**
     * DEFAULT CONSTRUCTOR for SparqlURIBuilder class
     */
    public SparqlURIBuilder() {
        super();
        this.setPreQuery("http://opendata.caceres.es/sparql");
        this.setGraph("");
    }

    /**
     * PARAMETRIZED CONSTRUCTOR for SparqlURIBuilder class
     * @param graph     gragh for sparql query
     * @param query     the sparql query
     * @param format    format of response
     */
    public SparqlURIBuilder(String graph, String query, String format) {
        super();
        this.setPreQuery("http://opendata.caceres.es/sparql");
        this.setGraph(graph);
        this.setSparqlQuery(query);
        this.setFormat(format);

    }



    // METHODS TO BUILD THE QUERY URL
    /**
     * Method to get the full URL
     * @return  the full and encoded URL
     */
    public String getUri() {
        return getPreQuery() + getUriGraph() + getUriQuery() + getUriFormat();
    }

    /**
     * Method to get the graph part of the URL
     * @return  the graph part of the URL encoded
     */
    public String getUriGraph() {
        return "?default-graph-uri=" + encode(this.getGraph() );
    }

    /**
     * Method to get the query part of the URL
     * @return      the query part of the URL encoded
     */
    public String getUriQuery() {
        return "&query=" + encode( this.getSparqlQuery() );
    }

    /**
     * Method to get the format part of the URL
     * @return      the format part of the URL encoded
     */
    public String getUriFormat() {
        return "&format=" + encode(this.getFormat());
    }



    // METHODS TO ENCODE A STRING AS A URL
    /**
     * Methods to encode a string as a URL
     * @param text     string input
     * @return         encoded string as a URL
     */
    public  String encode( String text )  {
        String encodedText = "";

        encodedText = URLEncoder.encode( text );

        return encodedText;
    }



    //GETTERS and SETTERS METHODS FOR ATTRIBUTES OF THE CLASS
    /**
     * Get method for preQuery attribute
     * @return      preQuery attribute
     */
    public String getPreQuery() {
        return preQuery;
    }

    /**
     * Set method for preQuery attribute
     * @param preQuery      preQuery text
     */
    public void setPreQuery(String preQuery) {
        this.preQuery = preQuery;
    }

    /**
     * Get method for graph attribute
     * @return      graph attribute
     */
    public String getGraph() {
        return graph;
    }

    /**
     * Set method for graph attribute
     * @param graph     graph text
     */
    public void setGraph(String graph) {
        this.graph = graph;
    }

    /**
     * Get method for sparqlQuery attribute
     * @return      sparqlQuery attribute
     */
    public String getSparqlQuery() {
        return sparqlQuery;
    }

    /**
     * Set method for sparqlQuery attribute
     * @param sparqlQuery   sparqlQuery text
     */
    public void setSparqlQuery(String sparqlQuery) {
        this.sparqlQuery = sparqlQuery;
    }

    /**
     * Get method for format attribute
     * @return      format attribute
     */
    public String getFormat() {
        return format;
    }

    /**
     * Set method for format attribute
     * @param format    format text
     */
    public void setFormat(String format) {
        this.format = format;
    }
}
