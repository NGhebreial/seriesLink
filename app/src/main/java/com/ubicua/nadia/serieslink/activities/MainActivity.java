package com.ubicua.nadia.serieslink.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ubicua.nadia.serieslink.R;
import com.ubicua.nadia.serieslink.dbManager.DBHandler;
import com.ubicua.nadia.serieslink.model.Serie;
import com.ubicua.nadia.serieslink.utils.PeticionHTTP;
import com.ubicua.nadia.serieslink.utils.SerieAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.R.id.list;

public class MainActivity extends ListActivity implements PeticionHTTP.OnRequestResult {
    SerieAdapter adapterSerie;
    DBHandler db;
    ArrayList<Serie> series;

    private RequestQueue mRequestQueue;

    private static int indexPreviewSerie = 0;
    private static int indexViewSerie = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //API series
        mRequestQueue = getRequestQueue();
        //DB
        db = new DBHandler(this);
        series = db.getAllSeries();
        adapterSerie = new SerieAdapter(this, series);

        //Necesario para ir poniendo nuevas series
        setContentView(R.layout.activity_main);

        //Adaptador en base al fchero xml que contiene el renderizado de cada elemento
        //Recojo la vista listItem
        ListView lista = (ListView) findViewById(list);
        //Seteo el adaptador a la vista lista
        lista.setAdapter(adapterSerie);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Serie serie = series.get(position);
                Intent i = new Intent(getApplicationContext(), SerieViewActivity.class);
                i.putExtra("serie", serie);

                startActivityForResult(i, indexViewSerie);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int position, long id) {
                Serie serie = series.get(position);
                Intent i = new Intent(getApplicationContext(), SerieViewActivity.class);

                i.putExtra("serie", serie);

                startActivityForResult(i, indexViewSerie);
                return true;
            }
        });
    }
    public  RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    //Nuevo elemento a la lista de series
    public void addItems(View v) {
        EditText nuevaSerie = (EditText)findViewById(R.id.nuevaSerie);
        if(nuevaSerie.getText() == null || nuevaSerie.getText().toString().trim().equals("")){
            muestraAlerta("Put a valid name for the Tv Show");
        }
        else{
            String name = nuevaSerie.getText().toString();

            try {
                String url = "http://api.tvmaze.com/search/shows?q="+URLEncoder.encode(name, "utf-8");
                peticionHttp(url);
            } catch (UnsupportedEncodingException e) {
                muestraAlerta("The Tv Show is not valid");
                e.printStackTrace();
            }
        }
        nuevaSerie.setText("");
    }


    private void peticionHttp(  String url ){

        PeticionHTTP http = new PeticionHTTP(this);
        addToRequestQueue(http.work(url));

    }
    private void refrescaVista(Serie serie){
        serie.setId(db.addSerie(serie));
        adapterSerie.add(serie);
        adapterSerie.notifyDataSetChanged();
    }


    private void muestraAlerta(String mensaje){
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestResult(final ArrayList<Serie> seriesReq) {
        if ( seriesReq.size() > 1){
            Intent i = new Intent(this, SeriePreviewActivity.class);
            i.putExtra("series", seriesReq);

            startActivityForResult(i, indexPreviewSerie);
        }
        else if( seriesReq.size() == 1){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refrescaVista(seriesReq.get(0));
                }
            });
        }

    }

    @Override
    protected void onActivityResult(final int requestCode,  int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
        if (resultCode == RESULT_CANCELED) {
            if ( requestCode == indexPreviewSerie) {
                muestraAlerta("Canceled");
            }
            // Si es así mostramos mensaje de cancelado por pantalla.
        }
        else if ( resultCode == RESULT_OK){
            final Serie serie = (Serie) data.getExtras().get("serie");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ( requestCode == indexPreviewSerie) {
                        refrescaVista(serie);
                    }
                    else if( requestCode == indexViewSerie){

                        for( int i = 0; i < series.size(); i++){
                            db.deleteSerie(serie.getId());
                            if ( series.get(i).getId().equals(serie.getId()) ){
                                series.remove(i);
                                adapterSerie.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }
}
