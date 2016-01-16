package com.example.myapplication.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Property;
import com.example.myapplication.datamodels.DataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANTONIO on 13/01/2016.
 */
public class RequestsManager {

    /**
     * Method to get the prefix of the uri
     * @param uri   uri of resource
     * @return      the prefix
     */
    public static String getPrefixFromURI( String uri ){

        String prefixUri;

        if ( uri.contains("#") ){
            prefixUri = uri.substring( 0, uri.indexOf( "#" ) ) + "#";
        }
        else {
            prefixUri = uri.substring( 0, uri.lastIndexOf("/") ) + "/";
        }
        return (String) PrefixesManagerSingleton.getInstance().getPrefixes().get( prefixUri );
    }

    /**
     * Method to get the property name of the uri
     * @param uri   uri of resource
     * @return      the property name
     */
    public static String getNameFromURI( String uri ){

        if ( uri.contains("#") ){
            return uri.substring( uri.indexOf( "#" ) + 1, uri.length() );
        }
        else {
            return uri.substring( uri.lastIndexOf( "/" ) + 1, uri.length() );
        }

    }


    public static JSONArray parseJSONGeneral( JSONObject jsonObject ) {
        // Variables locales
        JSONArray vars;         // Guarda las variables utilizadas en la consulta SPARQL
        JSONObject results;     // Guarda el objeto que contiene los resultados de la consulta
        JSONArray bindings;     // Guarda la coleccion de resultados

        try {
            // Obtener el array con las variables de la consulta
//            vars = jsonObject.getJSONArray( "vars" );

            // Obtener el objeto json donde estan los resultados
            results = jsonObject.getJSONObject( "results" );

            // Obtener el array del objeto que contiene los resultados
            bindings = results.getJSONArray( "bindings" );

        } catch (JSONException e) {
            e.printStackTrace();
            bindings = null;
        }

        //Log.e("PARSE JSON", "Resultado: " + dataString);
        return bindings;
    }




    public static List<DataSet> parseJSONDataSets( JSONObject jsonObject ) {
        // Variables locales
        JSONArray bindings;     // Guarda la coleccion de resultados
        List<DataSet> dataSets = new ArrayList();

        // Obtener los resultados (bindings)
        bindings = parseJSONGeneral( jsonObject );

        if ( bindings != null ) {
            for (int i = 0; i < bindings.length(); i++) {
                try {
                    // Obtener cada item de bindings
                    JSONObject item = bindings.getJSONObject(i);

                    // Obtener el nombre de la propiedad del item
                    JSONObject concept = item.getJSONObject("concept");

                    // Creamos un objeto DataSet
                    DataSet dataSet = new DataSet(
                            getNameFromURI(concept.getString("value")),
                            getPrefixFromURI(concept.getString("value"))
                    );

                    // Añadimos el objeto a la lista
                    dataSets.add(dataSet);

                } catch (JSONException e) {
                    Log.e("PARSE JSON", "Error de parsing: " + e.getMessage());
                }
            }
        }

        //Log.e("PARSE JSON", "Resultado: " + dataString);
        return dataSets;
    }


    public static ArrayList<Property> parseJSONProperties( JSONObject jsonObject ) {

        JSONArray bindings;     // Guarda la coleccion de resultados
        JSONObject propertyLevelOne = null;
        JSONObject propertyLevelTwo = null;
        String propertyLevelOneName = "";
        String propertyLevelTwoName = "";
        ArrayList<Property> properties = new ArrayList();

        // Obtener los resultados (bindings)
        bindings = parseJSONGeneral( jsonObject );

        if ( bindings != null ) {
            for (int i = 0; i < bindings.length(); i++) {
                try {
                    // Obtener cada item de bindings
                    JSONObject item = bindings.getJSONObject(i);

                    // Reiniciar los nombres para el nuevo item
                    propertyLevelOneName = "";
                    propertyLevelTwoName = "";

                    // Obtener el objeto properties del item (que indica propiedad de 2º nivel)
                    propertyLevelTwo = item.getJSONObject( "properties" );

                    // Construir el nombre de la property de 2º nivel
                    if ( propertyLevelTwo != null ) {
                        propertyLevelTwoName += getPrefixFromURI( propertyLevelTwo.getString( "value" ) ) + ":" +
                                                getNameFromURI( propertyLevelTwo.getString( "value" ) );
                    }

                    // Obtener el objeto p del item (que indica propiedad de 1º nivel)
                    propertyLevelOne = item.getJSONObject( "p" );

                    if ( propertyLevelOne != null ) {
                        propertyLevelOneName += getPrefixFromURI( propertyLevelOne.getString( "value" ) ) + ":" +
                                                getNameFromURI( propertyLevelOne.getString( "value" ) ) +
                                                " - ";
                    }


                } catch (JSONException e) {
                    Log.e("PARSE JSON", "Error de parsing: " + e.getMessage());
                }

                // Creamos un objeto Property
                Property property = new Property( false, propertyLevelOneName + propertyLevelTwoName);

                // Añadimos el objeto a la lista
                properties.add(property);
            }
        }

        //Log.e("PARSE JSON", "Resultado: " + dataString);
        return properties;
    }




//    public static List<DataSet> parseJSON( JSONObject jsonObject, int option ) {
//        // Variables locales
//        JSONArray vars;         // Guarda las variables utilizadas en la consulta SPARQL
//        JSONObject results;     // Guarda el objeto que contiene los resultados de la consulta
//        JSONArray bindings;     // Guarda la coleccion de resultados
//        List<DataSet> dataSets = new ArrayList();
//
//        try {
//            // Obtener el array con las variables de la consulta
////            vars = jsonObject.getJSONArray( "vars" );
//
//            // Obtener el objeto json donde estan los resultados
//            results = jsonObject.getJSONObject( "results" );
//
//            // Obtener el array del objeto que contiene los resultados
//            bindings = results.getJSONArray( "bindings" );
//
//            for(int i=0; i<bindings.length(); i++){
//                try {
//                    // Obtener cada item de bindings
//                    JSONObject item = bindings.getJSONObject(i);
//
//                    switch ( option ){
//                        case 1:
//                            // Obtener el nombre de la propiedad del item
//                            JSONObject concept = item.getJSONObject( "concept" );
//
//                            // Creamos un objeto DataSet
//                            DataSet dataSet = new DataSet(
//                                                    getNameFromURI( concept.getString( "value" ) ),
//                                                    getPrefixFromURI( concept.getString( "value" ) )
//                                            );
//
//                            // Añadimos el objeto a la lista
//                            dataSets.add( dataSet );
//                            break;
//
//                        case 2:
//                            // Obtener el objeto p del item (que indica propiedad de 1º nivel)
//                            JSONObject propertyLevelOne = item.getJSONObject( "p" );
//
//                            // Obtener el objeto properties del item (que indica propiedad de 2º nivel)
//                            JSONObject propertyLevelTwo = item.getJSONObject("properties");
//
//                            // Creamos un objeto Property
//                            String name = getPrefixFromURI( propertyLevelOne.getString( "value" ) ) +
//                                          getNameFromURI( propertyLevelOne.getString( "value" ) ) +
//                                          " - " +
//                                          getPrefixFromURI( propertyLevelTwo.getString( "value" ) ) +
//                                          getNameFromURI( propertyLevelTwo.getString( "value" ) );
//
//                            Property property = new Property( false, name);
//
//                            // Añadimos el objeto a la lista
//                            break;
//
//                        case 3:
//                            break;
//
//                        default:
//                            break;
//                    }
//
//                } catch (JSONException e) {
//                    Log.e("PARSE JSON", "Error de parsing: " + e.getMessage());
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        //Log.e("PARSE JSON", "Resultado: " + dataString);
//        return dataSets;
//    }









}
