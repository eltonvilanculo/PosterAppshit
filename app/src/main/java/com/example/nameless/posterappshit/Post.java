package com.example.nameless.posterappshit;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by SUtui on 5/4/2018.
 */
@IgnoreExtraProperties
public class  Post {
    private String text;
    private long date;
    private String sender;
    private String receptor;
    private String photoUri;


    public Post(String text, String sender, String receptor, String photoUri) {
        this.text = text;
        this.sender = sender;
        this.receptor = receptor;
        this.photoUri = photoUri;
    }

    public Post(String text, long date, String sender, String photoUri) {
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.photoUri = photoUri;
    }

    public Post() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }
}
