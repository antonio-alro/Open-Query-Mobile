package com.example.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ANTONIO on 12/11/2015.
 */
public class TabsDetailFragment extends Fragment {

    /**
     * El argumento del fragment representa la información de detalle del recurso
     */
    private static final String ARG_CONTENT_DETAIL = "content_detail";


    /**
     * Devuelve una nueva instancia de este FRAGMENT para la página correspondiente
     */
    public static TabsDetailFragment newInstance(String content_detail) {

        // Crear instancia del FRAGMENT que muestra la vista de detalle de un recurso
        TabsDetailFragment fragment = new TabsDetailFragment();

        // Le pasamos los argumentos necesarios (en este caso, el detalle del recurso)
        Bundle args = new Bundle();
        args.putString(ARG_CONTENT_DETAIL, content_detail);
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

        // Obtener el TEXTVIEW del Layout y rellenarlo con los datos que vienen en los argumentos
        TextView resultsDetail = (TextView) rootView.findViewById(R.id.resultsDetail);
        resultsDetail.setText(getArguments().getString(ARG_CONTENT_DETAIL));

        // Devolver la Vista inflada con el Layout
        return rootView;
    }


}
