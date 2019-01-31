package com.ubicua.nadia.serieslink.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ubicua.nadia.serieslink.R;
import com.ubicua.nadia.serieslink.model.Serie;
import com.ubicua.nadia.serieslink.utils.SerieAdapter;

import java.util.ArrayList;
import static android.R.id.list;

/**
 * Created by nadia on 20/05/17.
 */

public class SeriePreviewActivity extends ListActivity {
    SerieAdapter adapterSerie;
    ArrayList<Serie> series;
    Serie serie = null;
    View selectedView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data= getIntent().getExtras();
        series = (ArrayList<Serie>) data.get("series");

        adapterSerie = new SerieAdapter(this, series);
        setContentView(R.layout.activity_serie_preview);

        final ListView lista = (ListView) findViewById(list);

        lista.setAdapter(adapterSerie);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                quitaColorFondo();
                view.setBackgroundColor(Color.LTGRAY);
                selectedView = view;
                serie = series.get(position);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int position, long id) {

                quitaColorFondo();
                view.setBackgroundColor(Color.LTGRAY);
                selectedView = view;
                serie = series.get(position);

                return true;
            }
        });

    }

    public void quitaColorFondo( ){
        if ( selectedView != null ){
            selectedView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void volver(View v) {
        setResult(RESULT_CANCELED);
        // Finalizamos la Activity para volver a la anterior
        finish();
    }
    public void seleccionado(View v) {
        if ( serie == null ){
            muestraAlerta("Choose a Tv Show");
        }
        else{
            Intent i = getIntent();
            i.putExtra("serie", serie);
            setResult(RESULT_OK, i);
            finish();
        }
    }
    private void muestraAlerta(String mensaje){
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

}
