package com.phucvr.photospt.model;

import java.util.ArrayList;

public class Photos extends ItemView{

    ArrayList<Photo> Album;
    String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Photos(Type type, ArrayList<Photo> album, String name) {
        super(type);
        Album = album;
        Name = name;
    }

    public Photos(ArrayList<Photo> album, Type type) {
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
