package nguyenphuc.vr.photo.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ly.img.android.events.$EventCall_ProgressState_PREVIEW_BUSY;
import nguyenphuc.vr.photo.R;

public class ImageGrallery {

    public static String EMULATED = "emulated";
    public static String FILENAME_LIKE = "Like";
    public static String MyFile = "myDir";
    public static String NOMEDIA= ".nomedia";

    public static String getDirHidden()
    {
        return Environment.getExternalStorageDirectory() + "/" + MyFile +"/" + ImageGrallery.NOMEDIA;
    }

    public static String getDirMyFile()
    {
       return Environment.getExternalStorageDirectory() + "/" + ImageGrallery.MyFile;
    }

    public static String getDirLike()
    {
        return Environment.getExternalStorageDirectory() + "/" + MyFile +"/" + ImageGrallery.FILENAME_LIKE;
    }

    public static String getPath(Context context,String dir)
    {
        Uri uri;
        Cursor cursor;

        int     column_index_dir;

        uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        String[] projection = {
                MediaStore.Files.FileColumns.DATA,
        };

        cursor = context.getContentResolver().query(uri,projection,null,null,null);
        String path;
        while (cursor.moveToNext())
        {
            column_index_dir = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            path = cursor.getString(column_index_dir);

            if (path.contains(dir) && path.contains(EMULATED))
            {
                return path;
            }
        }
        return null;
    }


    public static ArrayList<String> getDir(Context context)
    {
        Uri uri;
        Cursor cursor;

        int     column_index_dir;

        uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        String[] projection = {
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
        };

        cursor = context.getContentResolver().query(uri,projection,null,null,null);

        String dir;
        ArrayList<String> dirs = new ArrayList<>();

        while (cursor.moveToNext())
        {
            column_index_dir = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME);
            dir = cursor.getString(column_index_dir);
            if (dir != null)
            Log.i("ImageGrallery_AllDir",dir);
            if (dir != null && !dirs.contains(dir))
            {
                Log.i("ImageGrallery_Dir",dir);

                dirs.add(dir);
            }

        }
        return dirs;
    }


    public static ArrayList<ItemView> getAllAlbum(Context context)
    {
        Uri uri;
        Cursor cursor;

        int     column_index_data,
                column_index_added,
                column_index_size,
                column_index_duration,
                column_index_displayName,
                column_index_mediaType,
                column_index_dir;

        ArrayList<ItemView> result = new ArrayList<>();

        ArrayList<Photo> dataImage = new ArrayList<>();

        Album oldTime = new Album("nick",Type.TITLE);
        Album newTime = new Album("nick",Type.TITLE);
        uri = MediaStore.Files.getContentUri("external");
        Log.i("Image_Path",uri.getPath());
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Files.FileColumns.DURATION,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
        };

        String orderBy = MediaStore.Video.Media.BUCKET_DISPLAY_NAME;
        String orderByTime = MediaStore.Video.Media.DATE_ADDED;
        cursor = context.getContentResolver().
                query(uri, projection, null, null, orderBy + " DESC"
                + ", " + orderByTime + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_added = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
        column_index_duration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
        column_index_size = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
        column_index_displayName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
        column_index_mediaType = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);
        column_index_dir = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME);

        String path,displayName,dir,parent;
        long time,duration,size;
        int media_type;
        boolean isFirst = true;

        if (cursor.moveToNext())
        {
            path = cursor.getString(column_index_data);
            dir = cursor.getString(column_index_dir);
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

            oldTime.setName(dir);
        }

        while (cursor.moveToNext()) {
            try {

                path = cursor.getString(column_index_data);
                time = cursor.getLong(column_index_added);
                duration = cursor.getLong(column_index_duration);
                displayName = cursor.getString(column_index_displayName);
                size = cursor.getLong(column_index_size);
                media_type = cursor.getInt(column_index_mediaType);
                dir = cursor.getString(column_index_dir);

                if (dir == null)
                    continue;
                Log.i("ImageGra Date", cursor.getString(column_index_added));
                Log.i("ImageGra Size", cursor.getString(column_index_size));
                Log.i("ImageGra Duration", Long.toString(cursor.getLong(column_index_duration)));
                Log.i("ImageGra DisplayName", cursor.getString(column_index_displayName));
                Log.i("ImageGra MimeType", Integer.toString(media_type));
                Log.i("ImageGra Dir",cursor.getString(column_index_dir));
                Log.i("ImageGra Path",path);

                newTime.setName(dir);


                if (!oldTime.getName().equals(newTime.getName())) {

                    if (!dataImage.isEmpty()) {
                        result.add(new Album(oldTime.getName() + "      " + dataImage.size() + "    " + context.getString(R.string.photo), Type.TITLE));
                        result.add(new Photos(dataImage, Type.ALBUM));
                    }
                    dataImage = new ArrayList<>();

                    oldTime.setName(dir);

                    Log.i("ImageGra Time Old", oldTime.getName());
                    Log.i("ImageGra Time New", newTime.getName());
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
        }
        if (dataImage.size() != 0)
        {
            result.add(new Album(oldTime.getName() + "      " + dataImage.size() + "    " + context.getString(R.string.photo) , Type.TITLE));
            result.add(new Photos(dataImage, Type.ALBUM));
        }
        return result;
    }

    public static ArrayList<ItemView> getAlbumHidden(Context context)
    {
        ArrayList<ItemView> arrayList = new ArrayList<>();
        File dir = new File(ImageGrallery.getDirHidden());
        File[] list = dir.listFiles();

        arrayList.add(new Album(context.getString(R.string.hidden),Type.TITLE));

        ArrayList<Photo> photos = new ArrayList<>();
        try
        {
            for (File file : list)
            {
                if (file != null)
                    photos.add(new Photo(file.getPath()));
            }

        } catch (Exception e)
        {

        }

        arrayList.add(new Photos(photos,Type.ALBUM));

        return arrayList;
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

        Time oldTime = new Time(0,Type.TITLE);
        Time newTime = new Time(0,Type.TITLE);
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
                Log.i("ImageGra Path",path);

                newTime.setDuration(time);


                if (!oldTime.getMonYear().equals(newTime.getMonYear())) {

                    result.add(new Time(oldTime.getDuration(), Type.TITLE));
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
        if (!dataImage.isEmpty())
        {
            result.add(new Time(oldTime.getDuration(), Type.TITLE));
            result.add(new Photos(dataImage, Type.ALBUM));
        }
        return result;
    }

    public static void galleryAddPic(String path,Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }
}
