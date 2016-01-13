package com.example.myapplication.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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


    public static List<DataSet> parseJSON( JSONObject jsonObject, int option ) {
        // Variables locales
        JSONArray vars;         // Guarda las variables utilizadas en la consulta SPARQL
        JSONObject results;     // Guarda el objeto que contiene los resultados de la consulta
        JSONArray bindings;     // Guarda la coleccion de resultados
        List<DataSet> dataSets = new ArrayList();

        try {
            // Obtener el array con las variables de la consulta
//            vars = jsonObject.getJSONArray( "vars" );

            // Obtener el objeto json donde estan los resultados
            results = jsonObject.getJSONObject( "results" );

            // Obtener el array del objeto que contiene los resultados
            bindings = results.getJSONArray( "bindings" );

            for(int i=0; i<bindings.length(); i++){
                try {
                    // Obtener cada item de bindings
                    JSONObject item = bindings.getJSONObject(i);

                    switch ( option ){
                        case 1:
                            // Obtener el nombre de la propiedad del item
                            JSONObject concept = item.getJSONObject( "concept" );

                            // Creamos un objeto DataSet
                            DataSet dataSet = new DataSet(
                                                    getNameFromURI( concept.getString( "value" ) ),
                                                    getPrefixFromURI(concept.getString( "value" ) )
                                            );

                            // Añadimos el objeto a la lista
                            dataSets.add( dataSet );
                            break;

                        case 2:
                            break;

                        case 3:
                            break;

                        default:
                            break;
                    }

                } catch (JSONException e) {
                    Log.e("PARSE JSON", "Error de parsing: " + e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.e("PARSE JSON", "Resultado: " + dataString);
        return dataSets;
    }




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

    public static String getNameFromURI( String uri ){

        if ( uri.contains("#") ){
            return uri.substring( uri.indexOf( "#" ) + 1, uri.length() );
        }
        else {
            return uri.substring( uri.lastIndexOf( "/" ) + 1, uri.length() );
        }

    }




}
