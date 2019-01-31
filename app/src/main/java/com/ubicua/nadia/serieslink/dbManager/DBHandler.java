package com.ubicua.nadia.serieslink.dbManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ubicua.nadia.serieslink.model.Serie;
import com.ubicua.nadia.serieslink.utils.SerialBitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadia on 20/04/17.
 */

public class DBHandler extends SQLiteOpenHelper{

    private static final String DB_NAME ="seriesLinkDB";
    private static final String TABLE_SERIE ="serie";
    private static final String ID ="id";
    private static final String NAME ="name";
    private static final String DESCRIPTION ="description";
    private static final String PHOTO_LINK ="photoLink";
    private static final String PHOTO ="photo";
    private static final String DATE ="date";

    private static final String LANGUAJE ="language";
    private static final String STATUS ="status";
    private static final String RATING_AVG ="average";
    private static final String GENRES ="genres";


    public DBHandler(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating table series
        String CREATE_SERIE_TABLE = "CREATE TABLE " + TABLE_SERIE + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + NAME + " TEXT,"+ PHOTO_LINK + " TEXT,"
                + DESCRIPTION + " TEXT, " + LANGUAJE + " TEXT, "+ STATUS + " TEXT, "+ RATING_AVG + " TEXT, "
                + GENRES + " TEXT, " + DATE + " TEXT, " + PHOTO + " BLOB)";
        db.execSQL(CREATE_SERIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIE);

        // Create tables again
        onCreate(db);
    }

    public Long addSerie(Serie serie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, serie.getName());
        values.put(DESCRIPTION, serie.getDescription());
        values.put(PHOTO_LINK, serie.getPhotoLink());

        values.put(LANGUAJE, serie.getLanguage());
        values.put(STATUS, serie.getStatus());
        values.put(RATING_AVG, serie.getAverage());
        values.put(GENRES, serie.getGenres());
        values.put(DATE, serie.getDate());
        if(serie.getPhoto()!=null)
            values.put(PHOTO, getBytes(serie.getPhoto().bitmap));
        Long id = db.insert(TABLE_SERIE, null, values);
        db.close();
        return id;
    }
    // convert from bitmap to byte array
    private static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    private static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public Serie getSerie(Long id){
        Serie serie = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SERIE, new String[]{ ID, NAME, DESCRIPTION, PHOTO_LINK, PHOTO, LANGUAJE, STATUS, RATING_AVG, GENRES, DATE},
                ID +"=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            if(cursor.getBlob(4)!=null){
                Bitmap image = getImage(cursor.getBlob(4));
                serie = new Serie(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), new SerialBitmap(image), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9));
            }
            else
                serie = new Serie(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), null, cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9));
        }
        db.close();
        return serie;
    }

    public ArrayList<Serie> getAllSeries(){
        ArrayList<Serie> series = new ArrayList<>();
        String selectQuery = "SELECT "+ID+", "+NAME+", "+ DESCRIPTION+", "+ PHOTO_LINK+", "+PHOTO+
                ", "+LANGUAJE+", "+STATUS+", "+RATING_AVG+", "+GENRES+", "+DATE+
                " FROM " + TABLE_SERIE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while(cursor.moveToNext()){
            if(cursor.getBlob(4)!=null){
                Bitmap image = getImage(cursor.getBlob(4));
                series.add(new Serie(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), new SerialBitmap(image), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9)));
            }
            else
                series.add(new Serie(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), null, cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9)));
        }
        db.close();
        return series;
    }

    public void updateSerie(Serie serie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, serie.getName());
        values.put(DESCRIPTION, serie.getDescription());
        values.put(PHOTO_LINK, serie.getPhotoLink());
        db.update(TABLE_SERIE, values, ID +"=?", new String[]{ String.valueOf( serie.getId() ) });
        db.close();
    }

    public void deleteSerie(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERIE, ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}
