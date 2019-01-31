package com.ubicua.nadia.serieslink.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ubicua.nadia.serieslink.model.Serie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by nadia on 2/05/17.
 */

public class PeticionHTTP {

    private Context ctx;

    public interface OnRequestResult {
        void onRequestResult(ArrayList<Serie> series);
    }

    public PeticionHTTP(Context ctx){
        this.ctx = ctx;
    }

    public StringRequest work(String url){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //TODO NoConnectionError
                        final ArrayList<Serie> series = new ArrayList<>();
                        try {
                            System.out.println(response);
                            final JSONArray resp = new JSONArray(response);
                            for ( int i = 0; i < resp.length(); i++) {
                                JSONObject respList = resp.getJSONObject(i);

                                if( respList.has("show")){
                                    JSONObject respuesta = respList.getJSONObject("show");
                                    System.out.println(respuesta);
                                    final Serie serie = new Serie();
                                    if (respuesta.has("summary")) {

                                        serie.setName(respuesta.getString("name"));
                                        serie.setDescription(respuesta.getString("summary"));
                                        serie.setLanguage(respuesta.getString("language"));

                                        if ( respuesta.has("genres") ){
                                            serie.setGenres(respuesta.getString("genres").replace("[", "").replace("]", ""));
                                        }
                                        serie.setStatus(respuesta.getString("status"));
                                        serie.setDate(respuesta.getString("premiered"));

                                        if(respuesta.has("rating")){
                                            JSONObject rating = respuesta.getJSONObject("rating");
                                            if ( rating.get("average") != "null"){
                                                serie.setAverage(rating.getString("average"));
                                            }
                                            else{
                                                serie.setAverage("");
                                            }
                                        }

                                        if (respuesta.has("image") && respuesta.getString("image") != "null") {
                                            JSONObject images = respuesta.getJSONObject("image");
                                            final String poster = images.getString("medium");
                                            serie.setPhotoLink(poster);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Bitmap image = getBitmapFromURL(poster);

                                                    if (image != null) {
                                                        serie.setPhoto(new SerialBitmap(image));
                                                        series.add(serie);
                                                        if ( series.size() == resp.length()){
                                                            ((OnRequestResult) ctx).onRequestResult(series);
                                                        }
                                                    }
                                                    else{
                                                        series.add(serie);
                                                        if ( series.size() == resp.length()){
                                                            ((OnRequestResult) ctx).onRequestResult(series);
                                                        }
                                                    }
                                                }
                                            }).start();
                                        }
                                        else{
                                            series.add(serie);
                                            if ( series.size() == resp.length()){
                                                ((OnRequestResult) ctx).onRequestResult(series);
                                            }
                                        }
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            //TODO controlar error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO error controlar
                System.out.println("Response: " + error.toString());
            }
        });
        return stringRequest;
    }
    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
