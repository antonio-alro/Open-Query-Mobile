package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner dataSetSelector;
    private ListView propertiesListView;
    private Spinner filterSelector;

    PropertyListAdapter listDataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtenemos el dataset seleccionado
                String dataSet = String.valueOf(dataSetSelector.getSelectedItem());
                String result1 = "Dataset seleccionado: " + dataSet + "\n";

                //Obtenemos las properties seleccionadas
                String properties_text = new String();
                ArrayList<Property> properties = listDataAdapter.properties;
                for (int i = 0; i < properties.size(); i++) {
                    Property property = properties.get(i);
                    if (property.isSelected()) {
                        properties_text += ("[" + property.getName() + "," + property.getFilter() + "] ");
                    }
                }
                String result2 = "Seleccionadas: " + properties_text;

                //Concatenamos ambas cosas
                String result = result1 + result2;
                //Mostramos el resultado en snackbar
                Snackbar.make(view, result, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //CODIGO NUEVO PARA GESTIONAR EL SPINNER DE LOS DATASETS
        //Generate spinner from ArrayList
        displayDataSetSelector();

        //CODIGO NUEVO PARA GESTIONAR LA LIST VIEW
        //Generate list View from ArrayList
        displayPropertyListView();


    }


    //METODO PARA GESTIONAR EL SPINNER DE LOS DATASETS
    private void displayDataSetSelector(){
        //Obtener el spinner del layout
        dataSetSelector = (Spinner) findViewById(R.id.dataSetSelector);

        //Rellenar unos datasets de ejemplo
        List<String> dataSets = new ArrayList<String>();
        dataSets.add("om:Arbol");
        dataSets.add("om:Cine");
        dataSets.add("om:BarCopas");
        dataSets.add("dbpedia-owl:Museum");
        dataSets.add("om:Farmacia");

        //Creamos un adaptador
        ArrayAdapter<String> dropdownDataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, dataSets);

        dropdownDataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        //Asignamos el adaptador al spinner
        dataSetSelector.setAdapter(dropdownDataAdapter);

        // Spinner item selection Listener
        dataSetSelector.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
    }


    //METODO PARA GESTIONAR LA LIST VIEW
    private void displayPropertyListView(){
        //Obtener la list view del layout
        propertiesListView = (ListView) findViewById(R.id.propertiesListView);
        //Rellenar unas propiedades de ejemplo
        ArrayList<Property> properties = new ArrayList<Property>();
        Property property = new Property(false, "geo:long");
        properties.add(property);
        property = new Property(false, "geo:lat");
        properties.add(property);
        property = new Property(false, "rdfs:label");
        properties.add(property);

        //Creamos un adaptador desde un Prpoerty array
        listDataAdapter = new PropertyListAdapter(this, R.layout.properties_list_item, properties);

        //Asignamos el adaptador a la list view
        propertiesListView.setAdapter(listDataAdapter);

        propertiesListView.setOnItemClickListener(new PropertiesListViewOnItemClickListener());
    }


    //ADAPTER PARA LA LIST VIEW DE PROPIEDADES
    private class PropertyListAdapter extends ArrayAdapter<Property> {

        private ArrayList<Property> properties;

        public PropertyListAdapter(Context context, int textViewResourceId,
                                   ArrayList<Property> properties) {
            super(context, textViewResourceId, properties);
            this.properties = new ArrayList<Property>();
            this.properties.addAll(properties);
        }

        private class ViewHolder {
            CheckBox selected;
            TextView name;
            TextView filterLabel;
            Spinner  filterSelector;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.properties_list_item, null);

                //Obtener los elementos desde el layout
                holder = new ViewHolder();
                holder.selected = (CheckBox) convertView.findViewById(R.id.checkBox1);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.filterLabel = (TextView) convertView.findViewById(R.id.filterLabel);
                holder.filterSelector = (Spinner) convertView.findViewById(R.id.filterSelector);
                convertView.setTag(holder);

                //Listener para el checbox del elemento
                holder.selected.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Property property = (Property) cb.getTag();
//                        Toast.makeText(v.getContext(),
//                                "Clicked on Checkbox: " + cb.getText() +
//                                        " is " + cb.isChecked(),
//                                Toast.LENGTH_LONG).show();
                        property.setSelected(cb.isChecked());
                    }
                });

                //Listener para el spinner del elemento
                holder.filterSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Spinner spinner = (Spinner) parent;
                        Property property = (Property) spinner.getTag();
                        property.setFilter( String.valueOf(spinner.getSelectedItem()) );
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
//                holder.filterSelector.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());

                //Rellenar el spinner con unos filtros de ejemplo
                List<String> filters = new ArrayList<String>();
                filters.add("Ninguno");
                filters.add("<");
                filters.add("<");
                filters.add(">");
                filters.add("<=");
                filters.add(">=");
                filters.add("Rango(x,y)");
                filters.add("que contenga");

                //Creamos un adaptador para el spinner del elemento
                ArrayAdapter<String> filterDropdownDataAdapter = new ArrayAdapter<String>
                        (this.getContext(), android.R.layout.simple_spinner_item, filters);

                filterDropdownDataAdapter.setDropDownViewResource
                        (android.R.layout.simple_spinner_dropdown_item);

                //Asignamos el adaptador al spinner
                holder.filterSelector.setAdapter(filterDropdownDataAdapter);

            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Property property = properties.get(position);
            holder.selected.setChecked(property.isSelected());
            holder.selected.setTag(property);
            holder.name.setText(property.getName());
            holder.filterSelector.setTag(property);


            return convertView;

        }



    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
