package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.datamodels.DataSet;
import com.example.myapplication.datamodels.Resource;
import com.example.myapplication.utils.PrefixesManagerSingleton;
import com.example.myapplication.utils.RequestsManager;
import com.example.myapplication.utils.SparqlQueryBuilder;
import com.example.myapplication.utils.SparqlURIBuilder;
import com.example.myapplication.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    /**
     * ATTRIBUTES OF MAIN ACTIVITY
     */
    // Layout elements
    private Spinner dataSetSelector;            // Spinner to select a dataSet
    private ListView propertiesListView;        // ListView to display the properties of a dataSet
//    private ProgressDialog progressDialog;      // ProgressDialog to display while the app get data
//    private Spinner filterSelector;

    // Adapters for the layout elements
    PropertyListAdapter listDataAdapter = null; // Adapter for the properties ListView

    // Variables para guardar los datos obtenidos del portal opendata caceres
    List<DataSet> dataSets = new ArrayList<DataSet>();      // Datasets data
    ArrayList<Property> properties = new ArrayList<Property>();  // Properties data



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //CODIGO NUEVO PARA GESTIONAR EL SPINNER DE LOS DATASETS
        //Generate spinner from ArrayList
//        displayDataSetSelector();

        //CODIGO NUEVO PARA GESTIONAR LA LIST VIEW
        //Generate list View from ArrayList
//        displayPropertyListView();


        // Obtenemos los prefijos y los guardamos para su posterior consulta
        getAndSavePrefixes();

        // Get the datasets through HTTP request from opendata caceres
        getDataSets();

    }



    //METODO PARA GESTIONAR EL SPINNER DE LOS DATASETS
    private void displayDataSetSelector( List<DataSet> dataSets ){
        //Obtener el spinner del layout
        dataSetSelector = (Spinner) findViewById(R.id.dataSetSelector);

        //Rellenar unos datasets de ejemplo
        List<String> textDataSets = new ArrayList<String>();

        for( int i=0; i<dataSets.size(); i++ ) {
            textDataSets.add(dataSets.get(i).getFullName() );
        }
//        textDataSets.add("om:Arbol");
//        textDataSets.add("om:Cine");
//        textDataSets.add("om:BarCopas");
//        textDataSets.add("dbpedia-owl:Museum");
//        textDataSets.add("om:Farmacia");

        //Creamos un adaptador
        ArrayAdapter<String> dropdownDataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, textDataSets);

        dropdownDataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        //Asignamos el adaptador al spinner
        dataSetSelector.setAdapter(dropdownDataAdapter);

        // Spinner item selection Listener
//        dataSetSelector.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
        dataSetSelector.setOnItemSelectedListener( this );
    }

    // Overrided methods of listener for the datasets selector
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        // Do a HTTP request to get the properties of selected item
        getProperties( item );
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }



    //METODO PARA GESTIONAR LA LIST VIEW
    private void displayPropertyListView( ArrayList<Property> properties ){
        //Obtener la LISTVIEW del layout
        propertiesListView = (ListView) findViewById(R.id.propertiesListView);
        //Rellenar unas propiedades de ejemplo
//        ArrayList<Property> properties = new ArrayList<Property>();
//        Property property = new Property(false, "geo:long");
//        properties.add(property);
//        property = new Property(false, "geo:lat");
//        properties.add(property);
//        property = new Property(false, "rdfs:label");
//        properties.add(property);

        //Creamos un ADAPTADOR desde un Property Array
        listDataAdapter = new PropertyListAdapter(this, R.layout.properties_list_item, properties);

        //Asignamos el ADAPTADOR a la LISTVIEW
        propertiesListView.setAdapter(listDataAdapter);

        propertiesListView.setOnItemClickListener(new PropertiesListViewOnItemClickListener());
    }



    //ADAPTER PARA LA LIST VIEW DE PROPIEDADES (Properties)
    private class PropertyListAdapter extends ArrayAdapter<Property> {

        private ArrayList<Property> properties;

        public PropertyListAdapter(Context context, int textViewResourceId,
                                   ArrayList<Property> properties) {
            super(context, textViewResourceId, properties);
            this.properties = new ArrayList<Property>();
            this.properties.addAll(properties);
        }

        private class ViewHolder {
            CheckBox selected;
            TextView name;
            CheckBox mandatory;
            TextView filterLabel;
            Spinner  filterSelector;
            EditText filterParam1;
            EditText filterParam2;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            // Obtenemos la property de la posicion que viene como parametro
            Property property = properties.get(position);

            if (convertView == null) {

                //Indicar el LAYOUT para inflar la LISTVIEW
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.properties_list_item, null);

                //Obtener los ELEMENTOS desde el LAYOUT
                holder = new ViewHolder();
                holder.selected       = (CheckBox) convertView.findViewById(R.id.selected);
                holder.name           = (TextView) convertView.findViewById(R.id.name);
                holder.mandatory      = (CheckBox) convertView.findViewById(R.id.mandatory);
                holder.filterLabel    = (TextView) convertView.findViewById(R.id.filterLabel);
                holder.filterSelector = (Spinner)  convertView.findViewById(R.id.filterSelector);
                holder.filterParam1   = (EditText) convertView.findViewById(R.id.filterParam1);
                holder.filterParam2   = (EditText) convertView.findViewById(R.id.filterParam2);
                convertView.setTag(holder);

                //LISTENER para el CHECKBOX 1 del elemento de la LISTVIEW
                holder.selected.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Property property = (Property) cb.getTag();
//                        Toast.makeText(v.getContext(),
//                                "Clicked on Checkbox: " + cb.getText() +
//                                        " is " + cb.isChecked(),
//                                Toast.LENGTH_LONG).show();
                        property.setSelected(cb.isChecked());
                    }
                });

                //LISTENER para el CHECKBOX 2 del elemento de la LISTVIEW
                holder.mandatory.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Property property = (Property) cb.getTag();
                        property.setMandatory(cb.isChecked());
                    }
                });

                //LISTENER para el SPINNER del elemento de la LISTVIEW
                holder.filterSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Spinner spinner = (Spinner) parent;
                        Property property = (Property) spinner.getTag();
                        property.setFilter( String.valueOf(spinner.getSelectedItem()) );
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
//                holder.filterSelector.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());

                //Rellenar el SPINNER con unos filtros de ejemplo
//                List<String> filters = new ArrayList<String>();
//                filters.add("Ninguno");
//                filters.add("=");
//                filters.add("<");


                //LISTENER para el EDITTEXT 1 del elemento de la LISTVIEW
                holder.filterParam1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            EditText filterParam1 = (EditText) v;
                            Property property = (Property) filterParam1.getTag();
                            property.setFilterParam1(filterParam1.getText().toString());
                        }
                    }
                });

                //LISTENER para el EDITTEXT 2 del elemento de la LISTVIEW
                holder.filterParam2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            EditText filterParam2 = (EditText) v;
                            Property property = (Property) filterParam2.getTag();
                            property.setFilterParam2(filterParam2.getText().toString());
                        }
                    }
                });

            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            //Rellenar el LAYOUT con los datos de la PROPERTY correspondiente
            holder.selected.setChecked(property.isSelected());
            holder.selected.setTag(property);

            holder.name.setText(property.getName());

            holder.mandatory.setChecked(property.isMandatory());
            holder.mandatory.setTag(property);


            ArrayList<String> filters = property.getAllowedFilters();

            //Creamos un ADAPTADOR para el SPINNER del elemento de la LISTVIEW
            ArrayAdapter<String> filterDropdownDataAdapter = new ArrayAdapter<String>
                    (this.getContext(), android.R.layout.simple_spinner_item, filters);

            filterDropdownDataAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);

            holder.filterSelector.setAdapter( filterDropdownDataAdapter );
            holder.filterSelector.setTag(property);


            holder.filterParam1.setText(property.getFilterParam1());
            holder.filterParam1.setTag(property);

            holder.filterParam2.setText(property.getFilterParam2());
            holder.filterParam2.setTag(property);


            return convertView;

        }



    }




    /**
     * Método que se ejecuta cuando el usuario toca el botón de ayuda
     * @param view
     */
    public void clickHelpButton( View view ) {
        // Do something in response to button click
        String title   = getResources().getString( R.string.help_message_title_properties_list );               //"Ayuda";
        String message = getResources().getString( R.string.help_message_content_properties_list_part_1 )       //"Marque en la casilla de la izquierda las propiedades " +
                         + "\n"                                                                                 //"que desea en la consulta y seleccione los filtros de consulta sobre los datos.\n" +
                         + getResources().getString( R.string.help_message_content_properties_list_part_2 );    //"Si marca la casilla 'Obligatorio' no se mostraran aquellos datos que " +
                                                                                                                //"no posean esa propiedad.";
        showDialogMessage( view, title, message );

    }


    /**
     * Method to show a dialog with a message
     * @param view
     * @param title     title for the dialog
     * @param message   message gor the dialog
     */
    public void showDialogMessage(View view, String title, String message ) {

        // CONSTRUIR Y MOSTRAR UN DIALOG
        // Obtener el FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Crear un HelpMessageDialogFragment
        HelpMessageDialogFragment messageDialog = new HelpMessageDialogFragment();
        // Al crearlo le pasamos dos argumentos (el título y el mensaje)
        Bundle args = new Bundle();
        args.putString( messageDialog.ARG_TITLE, title );
        args.putString( messageDialog.ARG_MESSAGE, message );
        messageDialog.setArguments(args);
        // Mostrar el diálogo
        messageDialog.show( fragmentManager, "tag"+title );
    }




    // METODOS PARA CREAR Y EJECUTAR PETICIONES HTTP (de tipo string y JsonObject)

    /**
     * Método que ejecuta una petición HTTP que devuelve una respuesta en texto plano
     * @param url      URL to execute the request
     */
    public void createStringRequest( String url ) {
        // PETICION CON LIBRERIA VOLLEY
        //String url = "http://opendata.caceres.es/sparql?nsdecl";

        final ProgressDialog progressDialog = new ProgressDialog( MainActivity.this );

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse the response
                        PrefixesManagerSingleton.getInstance().parseResponseHTML(response);
                        // Close the progress Dialog
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Close the progress Dialog
                progressDialog.dismiss();
            }
        });

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue( stringRequest );

        // Initialize the progress dialog and show it
        //progressDialog = new ProgressDialog( MainActivity.this );     //Indicamos la activity como contexto
        progressDialog.setTitle( getResources().getString( R.string.progress_dialog_title ) );      //"Obteniendo datos..."
        progressDialog.setMessage( getResources().getString( R.string.progress_dialog_message ) ); //"Espere un momento..."
        progressDialog.show();
    }


    /**
     * Método que ejecuta una petición HTTP para obtener los datasets. Devuelve una respuesta en formato JSON
     * @param url   URL to execute the request
     */
    public void createJSONResquestDataSets( String url ) {
        // PETICION CON LIBRERIA VOLLEY
        // String url = "http://opendata.caceres.es/sparql?default-graph-uri=&query=select+distinct+%3Fprop+Min%28%3Fy%29+%0D%0A+++where+{%0D%0A++++++%3Fx+a+om%3ACafeBar.%0D%0A++++++%3Fx+%3Fprop+%3Fy.+%0D%0A+++}&format=json&timeout=0&debug=on";

        final ProgressDialog progressDialog = new ProgressDialog( MainActivity.this );

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Get the dataSets
                        dataSets.clear();
                        dataSets = RequestsManager.parseJSONDataSets( response );
                        // Close the progress Dialog
                        progressDialog.dismiss();
                        // Fill the layout with the data
                        displayDataSetSelector(dataSets);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Close the progress Dialog
                        progressDialog.dismiss();
                    }
                });

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue( jsObjRequest );

        // Initialize the progress dialog and show it
//        progressDialog = new ProgressDialog( MainActivity.this );   //Indicamos la activity como contexto
        progressDialog.setTitle( getResources().getString( R.string.progress_dialog_title ) );    //"Obteniendo datos..."
        progressDialog.setMessage( getResources().getString( R.string.progress_dialog_message ) ); //"Espere un momento..."
        progressDialog.show();
    }


    /**
     * Método que ejecuta una petición HTTP para obtener las propiedades del dataset seleccionado. Devuelve una respuesta en formato JSON
     * @param url   URL to execute the request
     */
    public void createJSONResquestProperties( String url ) {
        // PETICION CON LIBRERIA VOLLEY
        // String url = "http://opendata.caceres.es/sparql?default-graph-uri=&query=select+distinct+%3Fprop+Min%28%3Fy%29+%0D%0A+++where+{%0D%0A++++++%3Fx+a+om%3ACafeBar.%0D%0A++++++%3Fx+%3Fprop+%3Fy.+%0D%0A+++}&format=json&timeout=0&debug=on";

        final ProgressDialog progressDialog = new ProgressDialog( MainActivity.this );

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Get the Properties of selected dataSets
                        properties.clear();
                        properties = RequestsManager.parseJSONProperties(response);
                        // Close the progress Dialog
                        progressDialog.dismiss();
                        // Fill the layout with the data
                        displayPropertyListView( properties );

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Close the progress Dialog
                        progressDialog.dismiss();
                    }
                });

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue( jsObjRequest );

        // Initialize the progress dialog and show it
//        progressDialog = new ProgressDialog( MainActivity.this );   //Indicamos la activity como contexto
        progressDialog.setTitle( getResources().getString( R.string.progress_dialog_title ) );    //"Obteniendo datos..."
        progressDialog.setMessage( getResources().getString( R.string.progress_dialog_message ) ); //"Espere un momento..."
        progressDialog.show();
    }



    // METODOS PARA OBTENER LOS DATOS DESDE EL PORTAL OPENDATA CACERES

    /**
     * Get the prefixes and theirs URI and save all in a map
     */
    public void getAndSavePrefixes() {
        String url = "http://opendata.caceres.es/sparql?nsdecl";
        createStringRequest(url);
    }

    /**
     * Get the datasets to display them in the layout
     */
    public void getDataSets() {
        // CONSULTA SPARQL PARA OBTENER LOS DATASETS

//        String url = "http://opendata.caceres.es/sparql?default-graph-uri=&query=select+distinct+%3Fconcept%0D%0A+++where{%0D%0A++++++%3FURI+rdf%3Atype+%3Fconcept.%0D%0A+++}%0D%0A+++order+by+%28%3Fconcept%29&format=json&timeout=0&debug=on";

        String sparqlQuery = "select distinct ?concept " +
                                "where { " +
                                    "?URI rdf:type ?concept. " +
                                "} order by (?concept)";

        SparqlURIBuilder uriBuilder = new SparqlURIBuilder( "", sparqlQuery, "json" );  //graph, sparql query and format
        String url = uriBuilder.getUri();

        createJSONResquestDataSets(url);
    }

    /**
     * Get the properties of the selected dataset by the user to display them in the layout
     * @param dataset  selected dataset by user
     */
    public void getProperties( String dataset ) {
        // CONSULTA SPARQL PARA OBTENER LAS PROPERTIES DE UN DATASET

//        String url = "http://opendata.caceres.es/sparql?default-graph-uri=&query=select+distinct+%3Fp+%3Fproperties+Min%28%3Fy%29%0D%0A++++++++++++++++where+{%0D%0A++++++++++++++++++++{%0D%0A+++++++++++++++++++++++++%3Fu+a+om%3ACafeBar.%0D%0A+++++++++++++++++++++++++%3Fu+%3Fproperties+%3Fy.%0D%0A+++++++++++++++++++++++++FILTER+isLiteral%28%3Fy%29%0D%0A++++++++++++++++++++}%0D%0A++++++++++++++++++++UNION%0D%0A++++++++++++++++++++{%0D%0A+++++++++++++++++++++++++%3Fu+a+om%3ACafeBar.%0D%0A+++++++++++++++++++++++++%3Fu+%3Fp+_%3ABlankNode.%0D%0A+++++++++++++++++++++++++_%3ABlankNode+%3Fproperties+%3Fy.%0D%0A++++++++++++++++++++}%0D%0A+++++++++++++++}%0D%0A+++++++++++++++order+by+%28%3Fp%29&format=json&timeout=0&debug=on";

        String sparqlQuery = "select distinct ?p ?properties Min(?y) " +
                                "where { " +
                                    "{ " +
                                        "?u a " + dataset + ". " +
                                        "?u ?properties ?y. " +
                                        "FILTER isLiteral(?y) " +
                                    "} " +
                                    "UNION { " +
                                        "?u a " + dataset + ". " +
                                        "?u ?p _:BlankNode. " +
                                        "_:BlankNode ?properties ?y. " +
                                    "} " +
                                "} order by (?p)";

        SparqlURIBuilder uriBuilder = new SparqlURIBuilder( "", sparqlQuery, "json" );  //graph, sparql query and format
        String url = uriBuilder.getUri();

        createJSONResquestProperties(url);

    }







    /**
     * Método que se ejecuta cuando el usuario toca el botón flotante
     * @param view
     */
    public void doSparqlConsult(View view) {
        //Obtenemos el dataset seleccionado
        String dataSet = String.valueOf(dataSetSelector.getSelectedItem());

        //Obtenemos las properties seleccionadas
        String properties_text = new String();
        ArrayList<Property> propertiesOfDataSet;

        if (listDataAdapter == null) {
            propertiesOfDataSet = new ArrayList<Property>();
//            showDialogMessage( view,
//                               getResources().getString( R.string.error_message_title_properties_list ),      //"Error"
//                               getResources().getString( R.string.error_message_content_properties_list ) );  //"Los datos no se han cargado correctamente. Vuelva a intentarlo de nuevo."
        }

        else {
            propertiesOfDataSet = listDataAdapter.properties;
        }

        Log.d( "PROPERTIES OF DATASET", String.valueOf( propertiesOfDataSet.size() ));
        int selectedPropertiesCount = 0;

        for (int i = 0; i < properties.size(); i++) {
            Property property = propertiesOfDataSet.get(i);
            if (property.isSelected()) {
                properties_text += property.to_s();
                selectedPropertiesCount++;
            }
        }

        if ( selectedPropertiesCount == 0 ){
            showDialogMessage( view,
                               getResources().getString( R.string.warning_message_title_execute_query ),      //"Advertencia"
                               getResources().getString(R.string.warning_message_content_execute_query ) );  //"Seleccione al menos una propiedad de la lista para realizar la consulta."
            return;
        }

        else {
            Log.d("SELECTED PROPERTIES: ", properties_text);

            // Construimos la consulta Sparql correspondiente
            DataSet data_set = new DataSet(dataSet.substring(dataSet.indexOf(":") + 1, dataSet.length()),
                    dataSet.substring(0, dataSet.indexOf(":")));

            Log.d("---- MAIN BUILDER ----", String.valueOf(properties.size()));
            SparqlQueryBuilder builder = new SparqlQueryBuilder(data_set, properties);
            Log.d("---- MAIN BUILDER ----", builder.to_s());
            builder.buildSparqlQuery();
            Log.d("---- SPARQL QUERY ----", builder.getSparqlQuery());

            //Lanzamos la actividad que muestra los resultados de la consulta
//        Intent intent_results = new Intent(this, ResultsActivity.class);
            Intent intent_results = new Intent(this, TabsActivity.class);
            // Pasamos el nombre del dataset y la consulta sparql como argumentos
            intent_results.putExtra("DATASET", dataSet);
            intent_results.putExtra("SPARQL QUERY", builder.getSparqlQuery());
            // Lanzamos la actividad de destino
            startActivity(intent_results);

        }
    }

//    public void doSparqlConsult(View view) {
//        //Obtenemos el dataset seleccionado
//        String dataSet = String.valueOf(dataSetSelector.getSelectedItem());
//        String result1 = "Dataset seleccionado: " + dataSet + "\n";
//
//        //Obtenemos las properties seleccionadas
//        String properties_text = new String();
//        ArrayList<Property> propertiesOfDataSet = listDataAdapter.properties;
//        for (int i = 0; i < properties.size(); i++) {
//            Property property = propertiesOfDataSet.get(i);
//            if (property.isSelected()) {
//                properties_text += property.to_s();
//            }
//        }
//        String result2 = "Seleccionadas: " + properties_text;
//        Log.d( "SELECTED PROPERTIES: ", properties_text);
//
//        //Concatenamos ambas cosas
//        String result = result1 + result2;
//        //Mostramos el resultado en snackbar
//        Snackbar.make(view, result, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//
//        // Construimos la consulta Sparql correspondiente
//        DataSet data_set = new DataSet( dataSet.substring( dataSet.indexOf( ":" )+1, dataSet.length() ),
//                dataSet.substring( 0, dataSet.indexOf( ":" ) )
//        );
//        Log.d( "---- MAIN BUILDER ----", String.valueOf( properties.size() ) );
//        SparqlQueryBuilder builder = new SparqlQueryBuilder( data_set, properties );
//        Log.d( "---- MAIN BUILDER ----", builder.to_s() );
//        builder.buildSparqlQuery();
//        Log.d( "---- SPARQL QUERY ----", builder.getSparqlQuery() );
//
//        //Lanzamos la actividad que muestra los resultados de la consulta
////        Intent intent_results = new Intent(this, ResultsActivity.class);
//        Intent intent_results = new Intent(this, TabsActivity.class);
//        startActivity(intent_results);
//    }













    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
