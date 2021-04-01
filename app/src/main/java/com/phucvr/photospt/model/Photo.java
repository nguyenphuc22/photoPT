package com.phucvr.photospt.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Photo {

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
}
