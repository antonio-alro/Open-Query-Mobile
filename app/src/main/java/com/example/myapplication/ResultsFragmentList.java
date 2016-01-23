package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANTONIO on 12/11/2015.
 */
public class ResultsFragmentList extends Fragment {

    //Datos de ejemplo para rellenar la vista
//    private String[] results = new String[] { new String("Recurso 1"),
//                                              new String("Recurso 2"),
//                                              new String("Recurso 3"),
//                                              new String("Recurso 4"),
//                                              new String("Recurso 5")
//                                            };

    //Atributo para almacenar los datos de ejemplo para la ListView
    ArrayList<String> results = new ArrayList<String>();

    //Atributo para la ListView que muestra los resultados en el Layout
    private ListView resultsList;

    //Listener para la Lista de Resultados
    private ResultsListener listener;


    //Método "equivalente” al onCreate() de las ACTIVIDADES
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Asignamos el layout al fragment que va a mostrar la lista con los resultados
        return inflater.inflate(R.layout.fragment_results_list, container, false);
    }

    //Método que se ejecutará cuando la actividad contenedora del fragment esté completamente creada
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        //Obtenemos la lista de resultados (ListView) desde el Layout
        resultsList = (ListView)getView().findViewById(R.id.resultsList);

        //Especificamos un Adaptador para la ListView
        resultsList.setAdapter(new ResultsAdapter(this));

        //Indicamos un Listener para cuando se pulse un elemento de la lista
        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                if (listener != null) {
                    listener.onElementSelected(
                            (String) resultsList.getAdapter().getItem(pos));
                }
            }
        });

        //Indicamos unos datos de ejemplo para rellenar la ListView posteriormente
        buildDataForList();
    }

    /**
     * Interfaz con un método para cuando se ha detectado la pulsación sobre un elemento de la lista
     */
    public interface ResultsListener {
        void onElementSelected(String element);
    }

    /**
     * Método setter para el listener de la lista
     * @param listener
     */
    public void setResultsListener(ResultsListener listener) {
        this.listener=listener;
    }

    /**
     * Método para indicar datos de ejemplo que rellenaran despues la ListView
     */
    public void buildDataForList(){
        results.add("Recurso 1");
        results.add("Recurso 2");
        results.add("Recurso 3");
        results.add("Recurso 4");
        results.add("Recurso 5");
        results.add("Recurso 6");
        results.add("Recurso 7");
    }

    // ADAPTADOR PARA LA LISTA DE RESULTADOS
    class ResultsAdapter extends ArrayAdapter<String> {

        Activity context;

        ResultsAdapter(Fragment context) {
            super(context.getActivity(), R.layout.fragment_results_list_item, results);
            this.context = context.getActivity();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.fragment_results_list_item, null);

            TextView resultsItemLabel = (TextView)item.findViewById(R.id.resourceUri);
            resultsItemLabel.setText( results.get( position ) );

            TextView resultsItemContent = (TextView)item.findViewById(R.id.resourceLabel);
            resultsItemContent.setText( results.get( position ) );

            return(item);
        }
    }



}
