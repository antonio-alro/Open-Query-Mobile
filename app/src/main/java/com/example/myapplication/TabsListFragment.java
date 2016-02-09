package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.datamodels.Resource;

import java.util.ArrayList;

/**
 * Created by ANTONIO on 23/01/2016.
 */
public class TabsListFragment extends Fragment {

    /**
     * El argumento del fragment representa la información de la lista de recursos
     */
    private static final String ARG_CONTENT_LIST = "content_list";
    private static final String ARG_DATASETNAME = "dataSetName";



    /**
     * Devuelve una nueva instancia de este FRAGMENT para la página correspondiente
     */
    public static TabsListFragment newInstance( String dataSetName, ArrayList<Resource> resources ) {

        // Crear instancia del FRAGMENT que muestra la lista de recursos
        TabsListFragment fragment = new TabsListFragment();

        // Le pasamos los argumentos necesarios (en este caso, el detalle del recurso)
        Bundle args = new Bundle();
        args.putParcelableArrayList( ARG_CONTENT_LIST, resources );
        args.putString( ARG_DATASETNAME, dataSetName );
        fragment.setArguments(args);

        // Devolver el FRAGMENT
        return fragment;
    }


    /**
     * CONSTRUCTOR
     */
    public TabsListFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_results_list, container, false);

        // Obtenemos los datos de la lista con los datos que vienen en los argumentos
        ArrayList<Resource> resources = getArguments().getParcelableArrayList(ARG_CONTENT_LIST);

        // Obtenemos del layout el textView que muestra el tipo de recurso consultado
        TextView resultsType = (TextView) rootView.findViewById( R.id.result_list_type );
        resultsType.setText( getArguments().getString( ARG_DATASETNAME ) );

        // Obtenemos del layout el textView que muestra el numero de recursos encontrados
        TextView resultsCounter = (TextView) rootView.findViewById( R.id.resultsListHeader );
        resultsCounter.setText( String.valueOf( resources.size() ) + " "
                                + getResources().getString( R.string.tab_list_end_header_title ) );



        // Obtener la LISTVIEW del Layout y rellenarlo con los datos que vienen en los argumentos
        //Creamos un ADAPTADOR desde un Property Array
        ResultsListAdapter listDataAdapter = new ResultsListAdapter( container.getContext(), R.layout.fragment_results_list_item, resources );

        //Creamos una LISTVIEW y le asginamos el ADAPTADOR
        ListView resultsList = (ListView) rootView.findViewById(R.id.resultsList);
        resultsList.setAdapter(listDataAdapter);

        //Indicamos que el propio fragment es el listener para cuando se haga click un elemento de la lista
        resultsList.setOnItemClickListener((AdapterView.OnItemClickListener) this.getActivity());

        // Devolver la Vista inflada con el Layout
        return rootView;
    }




    public class ResultsListAdapter extends ArrayAdapter<Resource>{

        private ArrayList<Resource> resources;
        Context context;

        public ResultsListAdapter(Context context, int textViewResourceId,
                                  ArrayList<Resource> resources) {
            super(context, textViewResourceId, resources);
            this.context = context;
            this.resources = new ArrayList<Resource>();
            this.resources.addAll( resources );
        }

//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return resources.size();
//        }
//
//        @Override
//        public long getItemId(int arg0) {
//            // TODO Auto-generated method stub
//            return arg0;
//        }

        private class ViewHolder{
            TextView uri;
            TextView label;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            // Obtenemos el resource de la posicion que viene como parametro
            Resource resource = resources.get(position);

            if (convertView == null) {

                //Indicar el LAYOUT para inflar la LISTVIEW
                LayoutInflater vi = (LayoutInflater)context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.fragment_results_list_item, null);

                //Obtener los ELEMENTOS desde el LAYOUT
                holder = new ViewHolder();
                holder.uri   = (TextView) convertView.findViewById(R.id.resourceUri);
                holder.label = (TextView) convertView.findViewById(R.id.resourceLabel);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            //Rellenar el LAYOUT con los datos de la PROPERTY correspondiente
            holder.uri.setText( resource.getUri() );
            holder.label.setText( resource.getLabel() );

            return convertView;
        }

    }



}
