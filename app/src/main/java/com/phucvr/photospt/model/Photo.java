package com.phucvr.photospt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Photo implements Parcelable {

    protected String path;
    protected long  time;
    protected long duration;
    protected long size;
    protected String displayName;
    protected boolean isImage;

    public Photo(String path, long time, long duration, long size, String displayName, String typePhoto) {
        this.path = path;
        this.time = time;
        this.duration = duration;
        this.size = size;
        this.displayName = displayName;
        setImage(typePhoto);
    }

    public Photo(String path)
    {
        this.path = path;
        this.time = 0;
        this.duration = 0;
        this.size = 0;
        this.displayName = path;
        setImage(TypePhoto.IMAGE);
    }

    protected Photo(Parcel in) {
        path = in.readString();
        time = in.readLong();
        duration = in.readLong();
        size = in.readLong();
        displayName = in.readString();
        isImage = in.readByte() != 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public String getMonthYear()
    {
        return new SimpleDateFormat("yyyy-MM").format(new Date( TimeUnit.SECONDS.toMillis(this.time)));
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(String typePhotoString) {

        if (TypePhoto.IMAGE.contains(typePhotoString))
        {
            this.isImage = true;
        } else
        {
            this.isImage = false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeLong(time);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(displayName);
        dest.writeByte((byte) (isImage ? 1 : 0));
    }
}
