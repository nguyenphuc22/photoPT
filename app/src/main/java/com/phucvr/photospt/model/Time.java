package com.phucvr.photospt.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Time extends ItemView {
    long duration;

    public Time(long duration,Type type) {
        super(type);
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getMonYear()
    {
        return new SimpleDateFormat("yyyy-MM").format(new Date( TimeUnit.SECONDS.toMillis(this.duration)));
    }

    public void setDuration(long duration) {
        this.duration = duration;
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
