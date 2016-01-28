package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.myapplication.datamodels.Resource;
import com.example.myapplication.utils.RequestsManager;
import com.example.myapplication.utils.SparqlURIBuilder;
import com.example.myapplication.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Activity para mostrar las distintas vistas con los resultados de la consulta realizada
 * Incluye una clase que es el adaptador de fragmentos para el ViewPager
 */
public class TabsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

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
     * Atributo para indicar el número de páginas que va a tener esta activity
     */
    static final int NUM_PAGES = 3;

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
        if ( bundle != null ){
            datasetName = bundle.getString( "DATASET" );
            sparqlQuery = bundle.getString( "SPARQL QUERY" );
        }


        // Get the resources through HTTP request from opendata caceres
        getRequestedResources();


//        // Crear el adaptador de fragmentos que retornará un fragment por cada una de las
//        // 3 secciones primarias de la actvity
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager() );
//
//        // Obtener el ViewPager desde el layout
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById( R.id.container );
//        // Indicar el adaptador para el ViewPager
//        mViewPager.setAdapter( mSectionsPagerAdapter );
//        // Indicar la pestaña a mostrar por defecto
//        mViewPager.setCurrentItem( 1 );


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    public void displayViewPager() {
        // Crear el adaptador de fragmentos que retornará un fragment por cada una de las
        // 3 secciones primarias de la actvity
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager() );

        // Obtener el ViewPager desde el layout
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById( R.id.container );
        // Indicar el adaptador para el ViewPager
        mViewPager.setAdapter( mSectionsPagerAdapter );
        // Indicar la pestaña a mostrar por defecto
        mViewPager.setCurrentItem( 1 );

    }




    /**
     * Get the resources to display them in the layout
     */
    public void getRequestedResources() {

        SparqlURIBuilder uriBuilder = new SparqlURIBuilder( "", sparqlQuery, "json" );  //graph, sparql query and format
        String url = uriBuilder.getUri();

        createJSONResquestResources( url );
    }

    /**
     * Método que ejecuta una petición HTTP para obtener los recursos que cumplan con la consulta solicitada. Devuelve una respuesta en formato JSON
     * @param url   URL to execute the request
     */
    public void createJSONResquestResources( String url ) {
        // PETICION CON LIBRERIA VOLLEY

        // Initialize the progress dialog
        final ProgressDialog progressDialog = new ProgressDialog( TabsActivity.this );

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
        progressDialog.setTitle("Obteniendo datos...");
        progressDialog.setMessage("Espere un momento...");
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

    // Método a ejecutar cuando se pulsa sobre un elemento de la lista de Resources
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Resource resource = (Resource) parent.getItemAtPosition(position);
//        Toast.makeText(parent.getContext(),
//                "Clicked on Row: " + position,
//                Toast.LENGTH_LONG).show();

        // Obtener y guardar el Resource de la posicion seleccionada de la lista
        String _class = parent.getItemAtPosition(position).getClass().toString();
        String className = _class.substring( _class.lastIndexOf(".") + 1, _class.length() );
        Log.d("CLASS ", className + " " + className.length());

        if ( className.equals( "Resource" ) ) {
            detailResource = (Resource) parent.getItemAtPosition(position);
            mViewPager.setCurrentItem(2);
            mViewPager.setCurrentItem(0);
            Log.d( "RESOURCE ", ((Resource) parent.getItemAtPosition(position)).to_s() );
        }

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
                    return "DETALLE";
                case 1:
                    return "LISTADO";
                case 2:
                    return "MAPA";
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
                    Log.d( "DETALLE ", "Pasando al Fragment el Recurso " + detailResource.to_s() );
                    // Devuelve un TabsDetailFragment
                    return TabsDetailFragment.newInstance( detailResource );

                case 1:
                    // Return a PlaceholderFragment (defined as a static inner class below).
                    return TabsListFragment.newInstance( resources );

                case 2:
                    // Return a PlaceholderFragment (defined as a static inner class below).
                    return PlaceholderFragment.newInstance(position + 1);

            }
            return null;
        }
    }






    // BORRAR DESPUES. ES SOLO PARA PRUEBAS
    public ArrayList<Resource> fillExampleData() {
        Resource res1 = new Resource();
        res1.addPropertyValue( "uri", "http://opendata.caceres.es/recurso/salud/farmacias/Farmacia/117-obdulia-andrade-iglesias" );
        res1.addPropertyValue("long", "-6.366742");
        res1.addPropertyValue("lat", "39.482367");

        Resource res2 = new Resource();
        res2.addPropertyValue( "uri", "http://opendata.caceres.es/recurso/salud/farmacias/Farmacia/147-jose-luis-rufo-ruiz" );
        res2.addPropertyValue( "long", "-6.38141" );
        res2.addPropertyValue( "lat", "39.468874" );

        Resource res3 = new Resource();
        res3.addPropertyValue( "uri", "http://opendata.caceres.es/recurso/salud/farmacias/Farmacia/166-mari-isabel-torres-perez" );
        res3.addPropertyValue( "long", "-6.366742" );
        res3.addPropertyValue( "lat", "39.482367" );

        Resource res4 = new Resource();
        res4.addPropertyValue( "uri", "http://opendata.caceres.es/recurso/salud/farmacias/Farmacia/180-hernandez-cb" );
        res4.addPropertyValue( "long", "-6.366742" );
        res4.addPropertyValue( "lat", "39.482367" );

        Resource res5 = new Resource();
        res5.addPropertyValue( "uri", "http://opendata.caceres.es/recurso/salud/farmacias/Farmacia/183-julian-saavedra-pavon"  );
        res5.addPropertyValue("long", "-6.366742");
        res5.addPropertyValue("lat", "39.482367");

        Resource res6 = new Resource();
        res6.addPropertyValue( "uri", "http://opendata.caceres.es/recurso/salud/farmacias/Farmacia/274-pedro-antonio-claros-vicario" );
        res6.addPropertyValue( "long", "-6.38141" );
        res6.addPropertyValue("lat", "39.468874");

        ArrayList<Resource> resources = new ArrayList<Resource>();
        resources.add( res1 );
        resources.add( res2 );
        resources.add( res3 );
        resources.add( res4 );
        resources.add( res5 );
        resources.add( res6 );

        return resources;
    }













    // BORRAR DESPUES. ES SOLO PARA PRUEBAS
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabs, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }





}
