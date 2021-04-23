package com.phucvr.photospt;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.phucvr.photospt.model.Album;
import com.phucvr.photospt.model.Photos;
import com.phucvr.photospt.model.ItemView;
import com.phucvr.photospt.model.Photo;
import com.phucvr.photospt.model.Time;
import com.phucvr.photospt.model.Type;
import com.phucvr.photospt.model.TypePhoto;

import java.util.ArrayList;

public class ImageGrallery {


    public static ArrayList<ItemView> getAlbum(Context context)
    {
        Uri uri;
        Cursor cursor;

        int     column_index_data,
                column_index_added,
                column_index_size,
                column_index_duration,
                column_index_displayName,
                column_index_mediaType;

        ArrayList<ItemView> result = new ArrayList<>();

        ArrayList<Photo> dataImage = new ArrayList<>();

        Time oldTime = new Time(0,Type.TIME);
        Time newTime = new Time(0,Type.TIME);

        uri = MediaStore.Files.getContentUri("external");
        Log.i("Image_Path",uri.getPath());
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Files.FileColumns.DURATION,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
        };

        String orderBy = MediaStore.Video.Media.DATE_ADDED;
        cursor = context.getContentResolver().
                query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_added = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
        column_index_duration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
        column_index_size = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
        column_index_displayName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
        column_index_mediaType = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);

        String path,displayName;
        long time,duration,size;
        int media_type;
        boolean isFirst = true;

        if (cursor.moveToNext())
        {
            path = cursor.getString(column_index_data);
            
            time = cursor.getLong(column_index_added);
            duration = cursor.getLong(column_index_duration);
            displayName = cursor.getString(column_index_displayName);
            size = cursor.getLong(column_index_size);
            media_type = cursor.getInt(column_index_mediaType);

            switch (media_type)
            {
                case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                {
                    dataImage.add(new Photo(path,time,duration,size,displayName, TypePhoto.IMAGE));
                    break;
                }
                case  MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                {
                    dataImage.add(new Photo(path,time,duration,size,displayName, TypePhoto.VIDEO));
                    break;
                }
            }

            oldTime.setDuration(time);
        }

        while (cursor.moveToNext()) {
            try {
                path = cursor.getString(column_index_data);
                time = cursor.getLong(column_index_added);
                duration = cursor.getLong(column_index_duration);
                displayName = cursor.getString(column_index_displayName);
                size = cursor.getLong(column_index_size);
                media_type = cursor.getInt(column_index_mediaType);

                Log.i("ImageGra Date", cursor.getString(column_index_added));
                Log.i("ImageGra Size", cursor.getString(column_index_size));
                Log.i("ImageGra Duration", Long.toString(cursor.getLong(column_index_duration)));
                Log.i("ImageGra DisplayName", cursor.getString(column_index_displayName));
                Log.i("ImageGra MimeType", Integer.toString(media_type));

                newTime.setDuration(time);


                if (!oldTime.getMonYear().equals(newTime.getMonYear())) {

                    result.add(new Time(oldTime.getDuration(), Type.TIME));
                    result.add(new Photos(dataImage, Type.ALBUM));

                    dataImage = new ArrayList<>();

                    oldTime.setDuration(time);

                    Log.i("ImageGra Time Old", oldTime.getMonYear());
                    Log.i("ImageGra Time New", newTime.getMonYear());
                }

                switch (media_type) {
                    case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE: {
                        dataImage.add(new Photo(path, time, duration, size, displayName, TypePhoto.IMAGE));
                        break;
                    }
                    case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO: {
                        dataImage.add(new Photo(path, time, duration, size, displayName, TypePhoto.VIDEO));
                        break;
                    }
                }
            }catch (Exception e)
            {
                Log.i(e.toString(),e.getMessage());
            }
            /*
            if (isFirst)
            {
                oldTime.setDuration(time);
                Log.i("First Time",oldTime.getMonYear());
                isFirst = false;
            }
            */


        }
        if (dataImage.size() != 0)
        {
            result.add(new Time(oldTime.getDuration(), Type.TIME));
            result.add(new Photos(dataImage, Type.ALBUM));
        }
        return result;
    }

    public static ArrayList<Photo> getFiles(Context context)
    {
        Uri uri;
        Cursor cursor;

        int     column_index_data,
                column_index_added,
                column_index_size,
                column_index_duration,
                column_index_displayName,
                column_index_mediaType;

        ArrayList<Photo> dataImage = new ArrayList<>();
        uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_INTERNAL);

        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Files.FileColumns.DURATION,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };

        String orderBy = MediaStore.Video.Media.DATE_ADDED;
        cursor = context.getContentResolver().
                query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_added = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
        column_index_duration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
        column_index_size = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
        column_index_displayName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
        column_index_mediaType = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);

        String path,displayName;
        long time,duration,size;
        int media_type;

        while (cursor.moveToNext()){
            path = cursor.getString(column_index_data);
            time = cursor.getLong(column_index_added);
            duration = cursor.getLong(column_index_duration);
            displayName = cursor.getString(column_index_displayName);
            size = cursor.getLong(column_index_size);
            media_type = cursor.getInt(column_index_mediaType);

            Log.i("Path",cursor.getString(column_index_data));
            Log.i("Date",cursor.getString(column_index_added));
            Log.i("Size",cursor.getString(column_index_size));
            Log.i("Duration",Long.toString(cursor.getLong(column_index_duration)));
            Log.i("DisplayName",cursor.getString(column_index_displayName));
            Log.i("MimeType", Integer.toString(media_type));

            switch (media_type)
            {
                case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                {
                    dataImage.add(new Photo(path,time,duration,size,displayName, TypePhoto.IMAGE));
                    break;
                }
                case  MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                {
                    dataImage.add(new Photo(path,time,duration,size,displayName, TypePhoto.VIDEO));
                    break;
                }
            }

        }
        return dataImage;
    }
}
