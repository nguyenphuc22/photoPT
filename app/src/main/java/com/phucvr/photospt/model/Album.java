package com.phucvr.photospt.model;

import java.util.ArrayList;

public class Album {

    private String name;
    private int number;
    private String thumbNail;
    private ArrayList<ItemView> mPhotos;

    public ArrayList<ItemView> getmPhotos() {
        return mPhotos;
    }

    public void setmPhotos(ArrayList<ItemView> mPhotos) {
        this.mPhotos = mPhotos;
    }

    public Album(String name, int number, ArrayList<ItemView> mPhotos) {
        this.name = name;
        this.number = number;
        this.mPhotos = mPhotos;
    }

    public Album(String name, int number, String thumbNail, ArrayList<ItemView> mPhotos) {
        this.name = name;
        this.number = number;
        this.thumbNail = thumbNail;
        this.mPhotos = mPhotos;
    }

    public Album(String name, int number, String thumbNail) {
        this.name = name;
        this.number = number;
        this.thumbNail = thumbNail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }
}
