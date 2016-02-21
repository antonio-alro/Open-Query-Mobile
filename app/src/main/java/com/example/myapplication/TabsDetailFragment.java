package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.datamodels.ItemValue;
import com.example.myapplication.datamodels.Resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by ANTONIO on 12/11/2015.
 */
public class TabsDetailFragment extends Fragment {

    /**
     * El argumento del fragment representa la información de detalle del recurso
     */
    private static final String ARG_CONTENT_DETAIL = "content_detail";
    private static final String ARG_DATASETNAME = "dataSetName";

    /**
     * El recurso del cual se va a mostrar la informacion de detalle
     */
    private Resource detailResource;


    /**
     * Devuelve una nueva instancia de este FRAGMENT para la página correspondiente
     */
    public static TabsDetailFragment newInstance( String dataSetName, Resource resource) {

        // Crear instancia del FRAGMENT que muestra la vista de detalle de un recurso
        TabsDetailFragment fragment = new TabsDetailFragment();

        // Le pasamos los argumentos necesarios (en este caso, el detalle del recurso)
        Bundle args = new Bundle();
        args.putParcelable( ARG_CONTENT_DETAIL, resource );
        args.putString(ARG_DATASETNAME, dataSetName);
        fragment.setArguments(args);

        // Devolver el FRAGMENT
        return fragment;
    }


    /**
     * CONSTRUCTOR
     */
    public TabsDetailFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_results_detail, container, false);

        // Obtenemos del layout el textView que muestra el tipo de recurso consultado
        TextView resourceType = (TextView) rootView.findViewById( R.id.textView_resource_detail_type );
        resourceType.setText( getArguments().getString( ARG_DATASETNAME ) );


        // Obtener el recurso seleccionado
        detailResource = (Resource) getArguments().getParcelable( ARG_CONTENT_DETAIL );

        // Inflamos la lista con la información de detalle de un Resource
        displayListDetail( detailResource, container.getContext(), rootView);


        // Get the FloatingActionButton of layout and set the listener
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener( (View.OnClickListener) this.getActivity() );


        // Devolver la Vista inflada con el Layout
        return rootView;
    }


    /**
     * Method to inflate the list with resource data
     * @param resource
     * @param context
     * @param rootView
     */
    public void displayListDetail( Resource resource, Context context, View rootView ) {
        // Obtener la LISTVIEW del Layout y rellenarlo con los datos que vienen en los argumentos

        // Obtenemos los datos de la lista con los datos que vienen en los argumentos
        LinkedHashMap<String,String> propertiesValues = resource.getPropertiesValues();

        // Los almacenamos en una unica lista para que sean manejados por el adaptador
        ArrayList<ItemValue> itemValues = new ArrayList<>();
        for( int i=0; i<propertiesValues.keySet().size(); i++ ) {
            String label = (String) propertiesValues.keySet().toArray()[ i ];
            String value = propertiesValues.get( label );
            itemValues.add(new ItemValue(label, value));
        }

        //Creamos un ADAPTADOR desde un Property Array
        DetailValuesListAdapter listDataAdapter = new DetailValuesListAdapter( context, R.layout.fragment_results_detail_value_list_item, itemValues );

        //Creamos una LISTVIEW y le asginamos el ADAPTADOR
        ListView detailValuesList = (ListView) rootView.findViewById(R.id.detailValuesList);
        detailValuesList.setAdapter(listDataAdapter);

        //Indicamos que el propio fragment es el listener para cuando se haga click un elemento de la lista
        detailValuesList.setOnItemClickListener( (AdapterView.OnItemClickListener) this.getActivity() );

        // Asignamos un layout por defecto para cuando la lista este vacía
        detailValuesList.setEmptyView( rootView.findViewById( R.id.emptyElementResultsDetail ) );
    }





    public class DetailValuesListAdapter extends ArrayAdapter<ItemValue> {

        private ArrayList<ItemValue> itemValues;
        Context context;

        public DetailValuesListAdapter(Context context, int textViewResourceId,
                                  ArrayList<ItemValue> itemValues) {
            super(context, textViewResourceId, itemValues);
            this.context = context;
            this.itemValues = new ArrayList<ItemValue>();
            this.itemValues.addAll( itemValues );
        }

        private class ViewHolder{
            TextView label;
            TextView value;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            // Obtenemos el resource de la posicion que viene como parametro
            ItemValue itemValue = itemValues.get(position);

            if (convertView == null) {

                //Indicar el LAYOUT para inflar la LISTVIEW
                LayoutInflater vi = (LayoutInflater)context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.fragment_results_detail_value_list_item, null);

                //Obtener los ELEMENTOS desde el LAYOUT
                holder = new ViewHolder();
                holder.label   = (TextView) convertView.findViewById(R.id.itemDetailLabel);
                holder.value   = (TextView) convertView.findViewById(R.id.itemDetailValue);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            //Rellenar el LAYOUT con los datos de la PROPERTY correspondiente
            holder.label.setText( itemValue.getLabel() );
            holder.value.setText( itemValue.getValue() );

            return convertView;
        }

    }




}
