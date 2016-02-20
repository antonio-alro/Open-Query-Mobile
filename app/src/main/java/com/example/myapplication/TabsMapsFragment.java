package com.example.myapplication;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.utils.RequestsManager;
import com.example.myapplication.utils.VolleySingleton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import com.example.myapplication.datamodels.Resource;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by ANTONIO on 02/02/2016.
 */
public class TabsMapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, AdapterView.OnItemSelectedListener {

    /**
     * El argumento del fragment representa la información de detalle del recurso
     */
    private static final String ARG_CONTENT_MAP = "content_map";

    private static final String ARG_DATASETNAME = "dataSetName";

    /**
     * Los recursos obtenidos de la consulta realizadas
     */
    private ArrayList<Resource> resources;

    /**
     * El nombre del dataset consultados
     */
    private String dataSetName;

    /**
     * La vista que va contener un mapa
     */
    MapView mapView;

    /**
     * El Google Map que va a tener el fragment
     */
    GoogleMap map;

    /**
     * El selector para el tipo de mapa
     */
    private Spinner mapTypeSelector;

    /**
     * Atributo para guardar la posicion GPS del dispositivo
     */
    private Location mLastLocation = null;

    /**
     * Atributo para guardar la ruta
     */
    List<List<HashMap<String, String>>> routes = null;

    /**
     * Devuelve una nueva instancia de este FRAGMENT para la página correspondiente
     */
    public static TabsMapsFragment newInstance(String dataSetName, ArrayList<Resource> resources) {

        // Crear instancia del FRAGMENT que muestra la lista de recursos
        TabsMapsFragment fragment = new TabsMapsFragment();

        // Le pasamos los argumentos necesarios (en este caso, el detalle del recurso)
        Bundle args = new Bundle();
        args.putParcelableArrayList( ARG_CONTENT_MAP, resources );
        args.putString( ARG_DATASETNAME, dataSetName );
        fragment.setArguments(args);

        // Devolver el FRAGMENT
        return fragment;
    }


    /**
     * CONSTRUCTOR
     */
    public TabsMapsFragment() {
    }


    /**
     * Método "equivalente” al onCreate() de las ACTIVITIES
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Obtener el LAYOUT que va a mostrar el FRAGMENT
        View rootView = inflater.inflate(R.layout.fragment_results_map, container, false);

        // Obtenemos los datos de la lista con los datos que vienen en los argumentos
        resources = getArguments().getParcelableArrayList( ARG_CONTENT_MAP );

        //Obtenemos el nombre del dataset cosultado a partir de los argumentos
        dataSetName = getArguments().getString( ARG_DATASETNAME );


        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Show the spinner to select the map type
        displayMapTypeSelector(rootView);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);


        // Get the FloatingActionButton of layout and set the listener
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        // Show the button (by default invisible)
        if ( resources.size() == 1 ) {
            fab.setVisibility( View.VISIBLE );

            // Not show the layout of distance filter
            ( (LinearLayout) rootView.findViewById( R.id.filterDistanceLayout ) ).setVisibility( View.INVISIBLE );
        }
        // Set the listener to the button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //Calcular la ruta
                calculateRoute();
            }
        });

        // Get the EditText with the distance filter and set a TextWatcher to detect when text change
        EditText editTextDistanceFilter = (EditText) rootView.findViewById( R.id.editTextFilterDistance );
        editTextDistanceFilter.addTextChangedListener( textWatcherFilterDistance );


        // Devolver la Vista inflada con el Layout
        return rootView;
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (googleMap != null) {
            // Indicamos la configuración de la interfaz gráfica
            map.getUiSettings().setAllGesturesEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            //Indicamos un Adapter para mostrar InfoWindows personalizadas en el mapa
            map.setInfoWindowAdapter( new MyInfoWindowAdapter() );

            // Indicamos el centro del mapa
            LatLng center = new LatLng( 39.473995, -6.374444 );
            if ( resources.size() == 1 ) {
                Double latitude  = Double.valueOf( resources.get( 0 ).getLatitude() );
                Double longitude = Double.valueOf( resources.get( 0 ).getLongitude() );
                center = new LatLng( latitude, longitude );
            }

            // Add the markers to the map (a marker for each resource)
            addMarkersToMap(map, resources, dataSetName);

            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(this.getActivity());

            // Updates the location and zoom of the MapView
            CameraPosition cameraPosition = new CameraPosition.Builder().target( center ).zoom( 15.0f ).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.moveCamera( cameraUpdate );

//            // Updates the location and zoom of the MapView
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(39.473995, -6.374444), 10);
//            googleMap.animateCamera(cameraUpdate);

                }
    }


    //METHODS TO MANAGE THE GOOGLE MAP

    /**
     * Method to add marker to a Google Map in the given coordinates
     * @param googleMap     the map on which to paint a marker
     * @param latitude      latitude value of geographic point
     * @param longitude     longitude value of geographic point
     */
    public void addMarkerToMap( GoogleMap googleMap, double latitude, double longitude,
                                String title, String snippet, Boolean customIcon ) {

        if ( googleMap != null ){
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(title)
                    .snippet(snippet);

            if ( customIcon ) {
                options.icon( BitmapDescriptorFactory.fromResource( R.drawable.icon_location ) );
            }

            googleMap.addMarker(options);
        }

    }

    /**
     * Method to add markers to a GoogleMap from the resources found in the sparql query
     * @param googleMap     the map on which to paint the markers
     * @param resources     the resources of the done sparql
     * @param dataSetName   the dataset name of the done sparql
     */
    public void addMarkersToMap( GoogleMap googleMap, ArrayList<Resource> resources, String dataSetName ) {

        // Clean the map
        if ( googleMap != null ) {
            googleMap.clear();
        }

        for( int i=0; i<resources.size(); i++ ) {

            String latitude  = resources.get( i ).getLatitude();
            String longitude = resources.get( i ).getLongitude();

            // Check if the resource has latitude and longitude values
            if ( ( latitude != null )  &&  ( longitude != null ) ) {
                addMarkerToMap( googleMap,
                                Double.parseDouble( latitude ),
                                Double.parseDouble( longitude ),
                                dataSetName,
                                resources.get( i ).to_snippet(),
                                false
                            );
            }
        }
    }


    // Método que se ejecutará cuando se pulse sobre el mapa
    @Override
    public void onMapClick(LatLng latLng) {
        // En este caso, servirá para cerrar la InfoWindow del Marker
    }


    // Adapter for the infoWindows in the map
    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.info_window_google_map, null);

        }

        @Override
        public View getInfoContents(Marker marker) {

            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
//            return null;
            //Obtener el TextView del layout para poner un título a la infoWindow del marker
            TextView tvTitle = ( (TextView) myContentsView.findViewById( R.id.title ) );
            tvTitle.setText( marker.getTitle() );

            // Obtener el Layout que va a mostrar la información del recurso
            LinearLayout infoWindowLayout = (LinearLayout) myContentsView.findViewById( R.id.infoWindowLayout );
            // Vaciar el Layout si tiene contenido
            if ( infoWindowLayout.getChildCount() > 0 ){
                infoWindowLayout.removeAllViews();
            }

            // Crear TextViews por cada par propiedad-valor y añadirlo al layout
            String[] values = marker.getSnippet().split( "\n" );

            for (int i=0; i<values.length; i++){
                TextView textView = new TextView( infoWindowLayout.getContext() );

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                                        1f
                                                                                    );

                textView.setLayoutParams( layoutParams );

                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                textView.setTextColor(Color.parseColor("#FFFFFF"));

                textView.setText( values[ i ] );

                infoWindowLayout.addView( textView );
            }

            return myContentsView;
        }

    }

    //METHODS TO MANAGE THE SPINNER TO SELECT THE GOOGLE MAP TYPE
    /**
     * Method to display the map type selector
     * @param view
     */
    private void displayMapTypeSelector( View view ) {
        //Obtener el spinner del layout
        mapTypeSelector = (Spinner) view.findViewById( R.id.mapTypeSelector );

        //Rellenar el array con los posibles tipos de mapas
        List<String> mapTypes = new ArrayList<String>();

        mapTypes.add( getResources().getString( R.string.tab_map_type_normal ) );       //"Normal"
        mapTypes.add( getResources().getString( R.string.tab_map_type_hybrid ) );       //"Híbrido"
        mapTypes.add( getResources().getString( R.string.tab_map_type_satellite ) );    //"Satélite"
        mapTypes.add( getResources().getString( R.string.tab_map_type_terrain ) );      //"Terreno"


        //Creamos un adaptador
        ArrayAdapter<String> dropdownDataAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item, mapTypes);

        dropdownDataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        //Asignamos el adaptador al spinner
        mapTypeSelector.setAdapter(dropdownDataAdapter);

        // Spinner item selection Listener
        mapTypeSelector.setOnItemSelectedListener( this );
    }


    /**
     * Overrided method of listener for the map type selector
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String mapType = parent.getItemAtPosition( position ).toString();

        if ( map != null ){     // Si esta inicializado
            switch ( mapType ) {
                case "Normal":
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case "Híbrido":
                case "Hybrid":
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case "Satélite":
                case "Satellite":
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case "Terreno":
                case "Terrain":
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                default:
                    break;
            }
        }

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + mapType, Toast.LENGTH_LONG).show();

    }

    /**
     * Overrided method of listener for the map type selector
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }






    // METHODS TO MANAGE THE DISTANCE FILTER
    /**
     * TextWatcher to detect when filter distance text changes
     */
    private TextWatcher textWatcherFilterDistance = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d( "", "El valor ha cambiado" );

            ArrayList<Resource> filteredResources = new ArrayList<>();

            if ( s.toString().isEmpty() ) {
                filteredResources = resources;
            }
            else {
                // Get the device current location
                getLocation();

                // Get the filtered resources by distance value
                if ( mLastLocation != null ) {
                    Double distanceFilter = Double.valueOf( s.toString() );
                    filteredResources = filteredResourcesByDistance( distanceFilter );
                }
            }

            // Add the markers of resources to map
            addMarkersToMap( map, filteredResources, dataSetName);

            // Add the marker in current location
            if ( mLastLocation != null ) {
                addMarkerToMap(map, mLastLocation.getLatitude(), mLastLocation.getLongitude(), "", "", true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * Method to calculate the distance (in meters) between two geographic points
     * Courtesy: https://www.mapanet.eu/resources/Script-Distance.htm
     * @param latitude1         latitude of point one
     * @param longitude1        longitude of point one
     * @param latitude2         latitude of point two
     * @param longitude2        longitude of point two
     */
    public Double distanceBetweenPoints( Double latitude1, Double longitude1, Double latitude2, Double longitude2 ) {

        Double R     = 6378.137;                                    // Radio (ecuatorial) de la Tierra en km
        Double dLat  = ( latitude2 - latitude1 ) * Math.PI / 180;   // Diferencia de latitud en radianes
        Double dLong = ( longitude2 - longitude1 ) * Math.PI / 180; // Diferencia de longitud en radianes

        Double radLat1 = latitude1 * Math.PI / 180;     // Latitude 1 en radianes
        Double radLat2 = latitude2 * Math.PI / 180;     // Latitude 2 en radianes

        // Aplicación de la Fórmula de Haversine - http://www.genbetadev.com/cnet/como-calcular-la-distancia-entre-dos-puntos-geograficos-en-c-formula-de-haversine
        Double a = Math.sin( dLat / 2 ) * Math.sin( dLat / 2 ) + Math.cos( radLat1 ) * Math.cos( radLat2 ) * Math.sin( dLong / 2 )* Math.sin( dLong / 2 );
        Double c = 2 * Math.atan2( Math.sqrt( a ), Math.sqrt( 1 - a ) );
        Double d = R * c;               // Distance in km (kilometers)

        return Math.abs(d * 1000);    // Distance in m (meters)

    }

    /**
     * Method to get the resources that are at less distance than the distance given by the user
     * @param filterDistance
     * @return
     */
    public ArrayList<Resource> filteredResourcesByDistance( Double filterDistance ) {

        ArrayList<Resource> filteredResources = new ArrayList<>();

        for( int i=0; i<resources.size(); i++ ) {
            // Get the current resource
            Resource resource = resources.get( i );

            // Calculate the distance between device position and resource position
            Double distance = distanceBetweenPoints( mLastLocation.getLatitude(),
                                                     mLastLocation.getLongitude(),
                                                     Double.valueOf( resource.getLatitude() ),
                                                     Double.valueOf( resource.getLongitude() )
                                                    );

            // Check if the calculated distance is less than distance of the filter
            if ( distance <= filterDistance ) {
                filteredResources.add( resource );
            }
        }

        return filteredResources;

    }






    //METHOD TO MANAGE LOCATION AND CALCULATE ROUTE
    /**
     * Método que calcula la ruta entre la posición actual del dispositivo y la del recurso mostrado en el mapa
     */
    public void calculateRoute() {

        // Get the current location of device (origin location of route)
        getLocation();

        if ( resources.size() == 1 ) {
            // Get the destination of route
            String destinationLatitude  = resources.get( 0 ).getLatitude();
            String destinationLongitude = resources.get( 0 ).getLongitude();

            // Get directions of route and draw them in the map
            if ( mLastLocation != null ) {
                getDirections(String.valueOf(mLastLocation.getLatitude()),
                        String.valueOf(mLastLocation.getLongitude()),
                        destinationLatitude,
                        destinationLongitude
                );
            }
        }

    }

    /**
     * Method to get the current location of the device
     */
    public void getLocation() {

        // Get the location manager
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService( getContext().LOCATION_SERVICE );

        // Check if GPS is enabled and allow enable it
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

//        Toast.makeText( getContext(), "Obteniendo localización", Toast.LENGTH_SHORT ).show();

        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        try {
            // Obtener la última localización conocida
            Location location = locationManager.getLastKnownLocation(provider);

            // Mostrar la localización
            //showPosition( location );

            // Guardar la localización y mostrarla sobre el mapa
            mLastLocation = location;
            if ( location != null ) {
                addMarkerToMap( map, location.getLatitude(), location.getLongitude(), "", "", true);
            }

            // Establecer el intervalo de tiempo para obtener las actualizaciones de la localización
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 30000, 0, locationListener );

        }
        catch ( SecurityException e ){
            Log.e("", "Error creating location service: " + e.getMessage());
        }

    }


    /**
     * Method to show a toast with the Location
     * @param location
     */
    public void showPosition( Location location ) {
        if ( location != null ) {
            String position = String.valueOf( location.getLatitude() ) + " / " + String.valueOf( location.getLongitude() );
            //Toast.makeText( getContext(), position, Toast.LENGTH_SHORT).show();
            //Snackbar.make(getView(), position, Snackbar.LENGTH_LONG).show();

        }
    }


    /**
     * Nos registramos para recibir actualizaciones de la posición
     */
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged( Location location ) {
            Log.i("onLocationChanged", String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));
        }
        public void onProviderDisabled( String provider ){
            Log.i( "", "Provider OFF" );
        }
        public void onProviderEnabled( String provider ){
            Log.i("", "Provider ON");
        }
        public void onStatusChanged( String provider, int status, Bundle extras ){
            Log.i("", "Provider Status: " + status);
        }
    };


    /**
     * Method to show a dialog that allows the user to enable the GPS
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder( this.getContext() );

        builder.setTitle( getResources().getString( R.string.tab_map_title_dialog_check_gps ) )
                .setMessage(getResources().getString(R.string.tab_map_message_dialog_check_gps))
                .setCancelable(false)

                .setPositiveButton( getResources().getString( R.string.tab_map_positive_button_text_dialog_check_gps ),
                                    new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })

                .setNegativeButton( getResources().getString( R.string.tab_map_negative_button_text_dialog_check_gps ),
                                    new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }


    /**
     * Method to execute the request to get the route
     */
    public void getDirections( String originLatitude, String originLongitude, String destinationLatitude, String destinationLongitude ) {

        // Format of response
        String format = "json";

        // Parameters of request
        String parameters = "origin="      + originLatitude      + "," + originLongitude + "&" +
                            "destination=" + destinationLatitude + "," + destinationLongitude + "&" +
                            "sensor=false";

        String url = "https://maps.googleapis.com/maps/api/directions/" + format + "?" + parameters;

        createJSONResquestDirections( url );

    }




    /**
     * Método que ejecuta una petición HTTP para obtener los pasos de la ruta. Devuelve una respuesta en formato JSON
     * @param url   URL to execute the request
     */
    public void createJSONResquestDirections( String url ) {
        // PETICION CON LIBRERIA VOLLEY

        final ProgressDialog progressDialog = new ProgressDialog( getActivity() );

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Get the routes
                        routes = RequestsManager.parseJSONDirections( response );
                        // Close the progress Dialog
                        progressDialog.dismiss();
                        // Fill the layout with the data
                        drawRouteInMap( map, routes );

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Close the progress Dialog
                        progressDialog.dismiss();
                    }
                });

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance( getContext() ).addToRequestQueue( jsObjRequest );

        // Initialize the progress dialog and show it
        progressDialog.setTitle( getResources().getString( R.string.progress_dialog_title ) );    //"Obteniendo datos..."
        progressDialog.setMessage( getResources().getString( R.string.progress_dialog_message ) ); //"Espere un momento..."
        progressDialog.show();
    }



    /**
     * Method to draw polylines of the route in a map
     * @param googleMap     the map on which to paint a marker
     * @param routes        the rout to paint
     */
    private void drawRouteInMap( GoogleMap googleMap, List<List<HashMap<String, String>>> routes ) {

        ArrayList<LatLng> points    = null;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for( int i=0; i<routes.size(); i++ ) {
            points      = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = routes.get( i );

            // Fetching all the points in i-th route
            for( int j=0; j<path.size(); j++ ) {
                HashMap<String,String> point = path.get( j );

                double lat = Double.parseDouble( point.get( "lat" ) );
                double lng = Double.parseDouble( point.get( "lng" ) );
                LatLng position = new LatLng( lat, lng );

                points.add( position );
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll( points );
            lineOptions.width( 10 );
            lineOptions.color( Color.rgb( 33, 150, 243 ) );
        }

        // Drawing polyline in the Google Map for the i-th route
        googleMap.addPolyline( lineOptions );


    }







    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
