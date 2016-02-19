package com.example.myapplication;


import android.app.ProgressDialog;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;


import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.AdapterView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.datamodels.Resource;
import com.example.myapplication.utils.RequestsManager;
import com.example.myapplication.utils.SparqlURIBuilder;
import com.example.myapplication.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Activity para mostrar las distintas vistas con los resultados de la consulta realizada
 * Incluye una clase que es el adaptador de fragmentos para el ViewPager
 */
public class TabsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    /**
     * Attribute to save the dataset name of the sparql query
     */
    private String datasetName = "";

    /**
     * Attribute to save the sparql query
     */
    private String sparqlQuery = "";

    /**
     * Atributo para guardar los recursos obtenidos del portal opendata caceres
     */
    ArrayList<Resource> resources = new ArrayList<Resource>();

    /**
     * Atributo para guardar el recurso seleccionado para ver su vista de detalle
     */
    Resource detailResource = new Resource();


    /**
     * Atributo para indicar el número de páginas que va a tener el ViewPager de esta activity
     */
    static final int NUM_PAGES = 4;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        //Obtener la toolbar desde el layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Indicar que la toolbar actúe como action bar
        setSupportActionBar(toolbar);
        // Habilitar la navegación hacia arriba
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the parameters of the intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            datasetName = bundle.getString("DATASET");
            sparqlQuery = bundle.getString("SPARQL QUERY");
        }


        // Get the resources through HTTP request from opendata caceres
        getRequestedResources();



    }


    /**
     * Method to display the ViewPager
     */
    public void displayViewPager() {
        // Crear el adaptador de fragmentos que retornará un fragment por cada una de las
        // 3 secciones primarias de la actvity
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Obtener el ViewPager desde el layout
        mViewPager = (ViewPager) findViewById(R.id.container);
        // Indicar el adaptador para el ViewPager
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Indicar la pestaña a mostrar por defecto (la lista de recursos)
        mViewPager.setCurrentItem(2);

    }


    /**
     * Get the resources to display them in the layout
     */
    public void getRequestedResources() {

        SparqlURIBuilder uriBuilder = new SparqlURIBuilder("", sparqlQuery, "json");  //graph, sparql query and format
        String url = uriBuilder.getUri();

        createJSONResquestResources(url);
    }

    /**
     * Método que ejecuta una petición HTTP para obtener los recursos que cumplan con la consulta solicitada. Devuelve una respuesta en formato JSON
     * @param url   URL to execute the request
     */
    public void createJSONResquestResources(String url) {
        // PETICION CON LIBRERIA VOLLEY

        // Initialize the progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(TabsActivity.this);

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Get the requested Resources in the aparql query and save them
                        resources = RequestsManager.parseJSONResources(response);

                        // Display ViewPager of the Activity
                        displayViewPager();

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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

        // Parametrize the progress dialog and show it
        progressDialog.setTitle(getResources().getString(R.string.progress_dialog_title));
        progressDialog.setMessage(getResources().getString(R.string.progress_dialog_message));
        progressDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
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


    /**
     * LISTENER FOR RESOURCES LIST (LIST FRAGMENT)
     * Método a ejecutar cuando se pulsa sobre un elemento de la lista de Resources
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Resource resource = (Resource) parent.getItemAtPosition(position);
//        Toast.makeText(parent.getContext(),
//                "Clicked on Row: " + position,
//                Toast.LENGTH_LONG).show();

        // Obtener y guardar el Resource de la posicion seleccionada de la lista
        String _class = parent.getItemAtPosition(position).getClass().toString();
        String className = _class.substring(_class.lastIndexOf(".") + 1, _class.length());

        if (className.equals(getResources().getString(R.string.resource_name_class))) {       //"Resource"
            detailResource = (Resource) parent.getItemAtPosition(position);
            mViewPager.setCurrentItem(3);
            mViewPager.setCurrentItem(1);
        }

    }

    /**
     * LISTENER FOR FLOATING_ACTION_BUTTON OF DETAIL FRAGMENT
     * @param view
     */
    @Override
    public void onClick(View view) {
        // Show the MapsFragment with the resource on which we are showing the details
        mViewPager.setCurrentItem(2);
        mViewPager.setCurrentItem(0);
    }





    /**
     * ADAPTADOR DE FRAGMENTOS PARA EL VIEWPAGER
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        /**
         * Constructor del FragmentPagerAdapter para recibir el FragmentManager
         * @param fm
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Método para indicar la cantidad de elementos que tendrá el Pager
         * @return
         */
        @Override
        public int getCount() {
            // Show NUM_PAGES ( in this case 3 ) total pages.
            return NUM_PAGES;
        }

        /**
         * Método que permite obtener el título de cada pestaña
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.title_tab_map);
                case 1:
                    return getResources().getString(R.string.title_tab_detail);
                case 2:
                    return getResources().getString(R.string.title_tab_list);
                case 3:
                    return getResources().getString(R.string.title_tab_map);
            }
            return null;
        }

        /**
         * Método que fabrica cada uno de los fragmentos que van a formar parte del Pager
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            // getItem es llamado para instanciar el fragment para una página dada
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    // Create a list with the resource on which you want show details
                    ArrayList<Resource> mapResources = new ArrayList<>();
                    mapResources.add(detailResource);
                    // Return a TabsMapsFragment
                    return TabsMapsFragment.newInstance(datasetName, mapResources);

                case 1:
                    // Return a TabsDetailFragment
                    return TabsDetailFragment.newInstance(datasetName, detailResource);

                case 2:
                    // Return a TabsListFragment
                    return TabsListFragment.newInstance(datasetName, resources);

                case 3:
                    // Return a TabsMapsFragment
                    return TabsMapsFragment.newInstance(datasetName, resources);
                // Return a PlaceholderFragment (defined as a static inner class below).
//                    return PlaceholderFragment.newInstance(position + 1);

            }
            return null;
        }
    }


    /**
     * Método que se ejecuta cuando el usuario toca el botón de ayuda
     * @param view
     */
    public void clickHelpButton(View view) {
        // Do something in response to button click

        String title = getResources().getString(R.string.info_message_title_resources_list);
        String message = getResources().getString(R.string.info_message_content_resources_list);

        showDialogMessage(view, title, message);

    }


    /**
     * Method to show a dialog with a message
     * @param view
     * @param title     title for the dialog
     * @param message   message gor the dialog
     */
    public void showDialogMessage(View view, String title, String message) {

        // CONSTRUIR Y MOSTRAR UN DIALOG
        // Obtener el FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Crear un HelpMessageDialogFragment
        HelpMessageDialogFragment messageDialog = new HelpMessageDialogFragment();
        // Al crearlo le pasamos dos argumentos (el título y el mensaje)
        Bundle args = new Bundle();
        args.putString(messageDialog.ARG_TITLE, title);
        args.putString(messageDialog.ARG_MESSAGE, message);
        messageDialog.setArguments(args);
        // Mostrar el diálogo
        messageDialog.show(fragmentManager, "tag" + title);
    }





    /**
     * Método que se ejecuta cuando el usuario toca el FloatingActionButton
     * @param view
     */
    public void calculateRoute(View view) {

        if (Build.VERSION.SDK_INT >= 23 &&
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            Log.d("---- LOCATION ----", "calculateRoute 2");
            return;
        }

    }
















}
