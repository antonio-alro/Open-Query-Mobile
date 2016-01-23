package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.datamodels.Resource;
import com.example.myapplication.dummy.DummyContent;

import java.util.ArrayList;

/**
 * Created by ANTONIO on 23/01/2016.
 */
public class TabsListFragment extends Fragment {

    /**
     * El argumento del fragment representa la información de la lista de recursos
     */
    private static final String ARG_CONTENT_LIST = "content_list";


    /**
     * Devuelve una nueva instancia de este FRAGMENT para la página correspondiente
     */
    public static TabsListFragment newInstance(String content_list) {

        // Crear instancia del FRAGMENT que muestra la lista de recursos
        TabsListFragment fragment = new TabsListFragment();

        // Le pasamos los argumentos necesarios (en este caso, el detalle del recurso)
        Bundle args = new Bundle();
        args.putString(ARG_CONTENT_LIST, content_list);
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

        // Obtener la LISTVIEW del Layout y rellenarlo con los datos que vienen en los argumentos
//        TextView resultsDetail = (TextView) rootView.findViewById(R.id.resultsDetail);
//        resultsDetail.setText(getArguments().getString(ARG_CONTENT_LIST));

        // DATOS DE EJEMPLO
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
        res6.addPropertyValue( "lat", "39.468874" );

        ArrayList<Resource> resources = new ArrayList<Resource>();
        resources.add( res1 );
        resources.add( res2 );
        resources.add( res3 );
        resources.add( res4 );
        resources.add( res5 );
        resources.add( res6 );


        //Creamos un ADAPTADOR desde un Property Array
        ResultsListAdapter listDataAdapter = new ResultsListAdapter( container.getContext(), R.layout.fragment_results_list_item, resources );

        //Creamos una LISTVIEW y le asginamos el ADAPTADOR
        ListView resultsList = (ListView) rootView.findViewById(R.id.resultsList);
        resultsList.setAdapter( listDataAdapter );

        //Indicamos que el propio fragment es el listener para cuando se haga click un elemento de la lista
        resultsList.setOnItemClickListener((AdapterView.OnItemClickListener) this.getActivity());

        // Devolver la Vista inflada con el Layout
        return rootView;
    }




    public class ResultsListAdapter extends ArrayAdapter<Resource>{

        private ArrayList<Resource> resources;

        private LayoutInflater mInflater;

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

                //Indicar el LAYOUT para inflar la LISTVIEW
//                convertView = mInflater.inflate( R.layout.fragment_results_list_item, null );

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
