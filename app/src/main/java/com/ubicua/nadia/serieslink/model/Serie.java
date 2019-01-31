package com.ubicua.nadia.serieslink.model;

import android.graphics.Bitmap;

import com.ubicua.nadia.serieslink.utils.SerialBitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nadia on 25/04/17.
 */

public class Serie implements Serializable{

    private Long id;
    private String name;
    private String description;
    private String photoLink;
    private SerialBitmap photo;

    private String language;
    private String status;
    private String average;
    private String genres;
    private String date;

    public Serie() {
    }

    public Serie(Long id, String name, String description, String photoLink, SerialBitmap photo,
                 String language, String status, String average, String genres, String date) {
        this.id = id;
        this.name = name == null || name.equals("")? "No data": name;
        this.description = description == null || description.equals("")? "No data": description;
        this.photoLink = photoLink == null || photoLink.equals("")? "No data": photoLink;
        this.photo = photo;
        this.language = language == null || language.equals("")? "No data": language;
        this.status = status == null || status.equals("")? "No data": status;
        this.average = average == null || average.equals("")? "No data": average;
        this.genres = genres == null || genres.equals("")? "No data": genres;
        this.date = date == null || date.equals("")? "No data": date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null || name.equals("")? "No data": name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null || description.equals("")? "No data": description;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink == null || photoLink.equals("")? "No data": photoLink;
    }

    public SerialBitmap getPhoto() {
        return photo;
    }

    public void setPhoto(SerialBitmap photo) {
        this.photo = photo;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null || language.equals("")? "No data": language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null || status.equals("")? "No data": status;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average == null || average.equals("")? "No data": average;;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres == null || genres.equals("")? "No data": genres;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null || date.equals("")? "No data": date;;
    }

    @Override
    public String toString() {
        return "Serie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", photoLink='" + photoLink + '\'' +
                ", photo=" + photo +
                ", language='" + language + '\'' +
                ", status='" + status + '\'' +
                ", average='" + average + '\'' +
                ", genres='" + genres + '\'' +
                ", date=" + date +
                '}';
    }
}
