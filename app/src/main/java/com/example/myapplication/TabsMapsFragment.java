package com.example.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import com.example.myapplication.datamodels.Resource;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by ANTONIO on 02/02/2016.
 */
public class TabsMapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

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
     * Devuelve una nueva instancia de este FRAGMENT para la página correspondiente
     */
    public static TabsMapsFragment newInstance( String dataSetName, ArrayList<Resource> resources ) {

        // Crear instancia del FRAGMENT que muestra la lista de recursos
        TabsMapsFragment fragment = new TabsMapsFragment();

        // Le pasamos los argumentos necesarios (en este caso, el detalle del recurso)
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CONTENT_MAP, resources);
        args.putString(ARG_DATASETNAME, dataSetName );
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

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync( this );

//        map = mapView.getMapAsync( new OnMapReadyCallback() {
//            @Override public void onMapReady( GoogleMap googleMap ) {
//                if (googleMap != null) {
//                    // Indicamos la configuración de la interfaz gráfica
//                    googleMap.getUiSettings().setAllGesturesEnabled(true);
//                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//                    // Indicamos el centro del mapa
//                    LatLng center = new LatLng( 39.473995, -6.374444 );
//
//                    // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
//                    try {
//                        MapsInitializer.initialize( this.getActivity() );
//                    } catch ( GooglePlayServicesNotAvailableException e ) {
//                        e.printStackTrace();
//                    }
//
//                    // Updates the location and zoom of the MapView
//                    CameraPosition cameraPosition = new CameraPosition.Builder().target( center ).zoom( 15.0f ).build();
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//                    googleMap.moveCamera( cameraUpdate );
//
////                    // Updates the location and zoom of the MapView
////                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(39.473995, -6.374444), 10);
////                    googleMap.animateCamera(cameraUpdate);
//
//                }
//
//            }
//        });

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

        if ( googleMap != null ) {
            // Indicamos la configuración de la interfaz gráfica
            map.getUiSettings().setAllGesturesEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            //Indicamos un Adapter para mostrar InfoWindows personalizadas en el mapa
            map.setInfoWindowAdapter( new MyInfoWindowAdapter() );

            // Indicamos el centro del mapa
            LatLng center = new LatLng( 39.473995, -6.374444 );

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
                                String title, String snippet ) {
        googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(title)
                        .snippet(snippet)
        );
    }

    /**
     * Method to add markers to a GoogleMap from the resources found in the sparql query
     * @param googleMap     the map on which to paint the markers
     * @param resources     the resources of the done sparql
     * @param dataSetName   the dataset name of the done sparql
     */
    public void addMarkersToMap( GoogleMap googleMap, ArrayList<Resource> resources, String dataSetName ) {
        for( int i=0; i<resources.size(); i++ ) {
            String latitude  = resources.get( i ).getLatitude();
            String longitude = resources.get( i ).getLongitude();
            // Check if the resource has latitude and longitude values
            if ( ( latitude != null )  &&  ( longitude != null ) ) {
                addMarkerToMap( googleMap,
                                Double.parseDouble( latitude ),
                                Double.parseDouble( longitude ),
                                dataSetName,
                                resources.get( i ).to_snippet()
                            );
            }
        }
    }

    // Método que se ejecutará cuando se pulse sobre el mapa
    @Override
    public void onMapClick(LatLng latLng) {
        // En este caso, servirá para cerrar la InfoWindow del Marker
    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.info_window_google_map, null);

        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());

//            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
//            tvSnippet.setText(marker.getSnippet());

            // Obtener el Layout que va a mostrar la información del recurso
            LinearLayout infoWindowLayout = (LinearLayout) myContentsView.findViewById( R.id.infoWindowLayout );
            // Vaciar el Layout --- BORRAR DESPUES
//            if ( infoWindowLayout.getChildCount() > 0 ){
//                infoWindowLayout.removeAllViews();
//            }

            // Crear TextViews por cada par propiedad-valor y añadirlo al layout
            String[] values = marker.getSnippet().split( "\n" );

            for (int i=0; i<values.length; i++){
                TextView textView = new TextView( infoWindowLayout.getContext() );
                textView.setLayoutParams( new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f ) );
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                textView.setText( values[ i ] );

                infoWindowLayout.addView( textView );
            }

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

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
