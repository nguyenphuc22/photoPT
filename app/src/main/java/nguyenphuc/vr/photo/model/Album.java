package nguyenphuc.vr.photo.model;

import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

public class Album extends ItemView{
    //Tên cài album
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album(String name, Type type) {
        super(type);
        this.name = name;
    }


    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public String getTitle() {
        return this.getName();
    }
}
