package com.phucvr.photospt.model;

import java.util.ArrayList;

public class Album extends ItemView{

    ArrayList<Photo> Album;

    public Album(ArrayList<Photo> album,Type type) {
        super(type);
        Album = album;
    }

    public ArrayList<Photo> getAlbum() {
        return Album;
    }

    public void setAlbum(ArrayList<Photo> album) {
        Album = album;
    }

    public void add(Photo photo)
    {
        this.Album.add(photo);
    }

    public void addAll(ArrayList<Photo> photos)
    {
        this.Album.addAll(photos);
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return this.type;
    }
}
