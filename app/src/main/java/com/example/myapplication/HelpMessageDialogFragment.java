package com.example.myapplication;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by ANTONIO on 12/12/2015.
 */
public class HelpMessageDialogFragment extends DialogFragment {

    //TAGS para el paso de argumentos al crear un HelpMessageDialogFragment
    public static final String ARG_TITLE = "title";
    public static final String ARG_MESSAGE = "message";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());

        // Obtener los argumentos indicados en la llamada de creación de un HelpMessageDialogFragment
        Bundle args = getArguments();

        builder.setMessage(args.getString(ARG_MESSAGE))
                .setTitle(args.getString(ARG_TITLE))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
