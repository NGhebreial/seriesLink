package com.ubicua.nadia.serieslink.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubicua.nadia.serieslink.R;
import com.ubicua.nadia.serieslink.model.Serie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nadia on 25/04/17.
 */

public class SerieAdapter extends ArrayAdapter<Serie> {

    public SerieAdapter(Context context, ArrayList<Serie> series) {
        super(context, 0, series);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Serie serie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.nombreSerie);
        TextView descripcion = (TextView) convertView.findViewById(R.id.descripcion);

        // Populate the data into the template view using the data object
        name.setText(serie.getName());
        descripcion.setText(setDescription(serie.getDescription()));

        if(serie.getPhoto()!=null){
            final ImageView poster = (ImageView)convertView.findViewById(R.id.poster);
            poster.setImageBitmap(serie.getPhoto().bitmap);
        }

        // Return the completed view to render on screen
        return convertView;
    }
    @TargetApi(Build.VERSION_CODES.CUR_DEVELOPMENT)
    private Spanned setDescription( String html ){
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
