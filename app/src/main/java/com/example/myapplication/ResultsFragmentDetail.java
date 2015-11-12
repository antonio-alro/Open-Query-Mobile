package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by ANTONIO on 12/11/2015.
 */
public class ResultsFragmentDetail extends Fragment {

    //Método "equivalente” al onCreate() de las ACTIVIDADES
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_results_detail, container, false);
    }


    /**
     * Método para mostrar el detalle de un elemento de la lista de resultados
     * @param text
     */
    public void showDetail(String text) {
        TextView resultsDetail = (TextView)getView().findViewById(R.id.resultsDetail);

        resultsDetail.setText(text);
    }

}
