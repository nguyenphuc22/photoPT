package nguyenphuc.vr.photo.model;

import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

public class Album {
    //Thumbnail lấy đường dẫn tới cái hình đầu tiên của album là được rồi.
    private String thumbnail;
    //Tên cài album
    private String name;


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album(String thumbnail, String name) {
        this.thumbnail = thumbnail;
        this.name = name;
    }


}
