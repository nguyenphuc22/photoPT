package nguyenphuc.vr.photo.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class ImageGrallery {
    public static ArrayList<ItemView> getAllAlbum(Context context)
    {
        // which image properties are we querying
        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };

// content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

// Make the query.
        Cursor cur = context.getContentResolver().
                query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        Log.i("ListingImages"," query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String bucket;
            String date;
            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            do {
                // Get the field values
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);

                // Do something with the values.
                Log.i("ListingImages", " bucket=" + bucket
                        + "  date_taken=" + date);
            } while (cur.moveToNext());

        }
        return null;
    }

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
}
