package com.ubicua.nadia.serieslink.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubicua.nadia.serieslink.R;
import com.ubicua.nadia.serieslink.model.Serie;

public class SerieViewActivity extends AppCompatActivity {
    Serie serie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_view);
        Bundle data= getIntent().getExtras();
        serie = (Serie) data.get("serie");

        TextView name = (TextView) findViewById(R.id.name);
        TextView descripcion = (TextView) findViewById(R.id.description);
        TextView puntuacion = (TextView) findViewById(R.id.avg);
        TextView estado = (TextView) findViewById(R.id.status);
        TextView date = (TextView) findViewById(R.id.date);
        TextView genres = (TextView) findViewById(R.id.genres);

        // Populate the data into the template view using the data object
        name.setText(serie.getName());
        descripcion.setText(setDescription(serie.getDescription()));
        puntuacion.setText(serie.getAverage());
        estado.setText(serie.getStatus());
        date.setText(serie.getDate());
        genres.setText(serie.getGenres());
        descripcion.setMovementMethod(new ScrollingMovementMethod());

        if(serie.getPhoto()!=null){
            final ImageView poster = (ImageView)findViewById(R.id.poster);
            poster.setImageBitmap(serie.getPhoto().bitmap);
        }
    }

    public void volver(View v) {
        setResult(RESULT_CANCELED);
        // Finalizamos la Activity para volver a la anterior
        finish();
    }
    public void eliminar(View v) {
        eliminaSerie();
    }

    private void eliminaSerie(){
        new AlertDialog.Builder(this)
                .setTitle("Delete Tv Show")
                .setMessage("Are you sure you want to delete this Tv Show? "+serie.getName())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = getIntent();
                        i.putExtra("serie", serie);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                })
                .setNegativeButton("No", null).show();
    }
    @TargetApi(Build.VERSION_CODES.CUR_DEVELOPMENT)
    private Spanned setDescription(String html ){
        Spanned resultado;
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.CUR_DEVELOPMENT) {
            resultado = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        }
        else{
            resultado = Html.fromHtml( html );
        }
        return resultado;
    }
}
