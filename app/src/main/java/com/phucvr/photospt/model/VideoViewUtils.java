package com.phucvr.photospt.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoViewUtils {
    public static final String LOCAL_VIDEO_SAMPLE ="/storage/emulated/0/DCIM/Camera/VID_20180212_195520.mp4";

    public static final String LOG_TAG= "AndroidVideoView";

    public static void playURLVideo(Context context, VideoView videoView, String videoURL)  {
        try {
            Log.i(LOG_TAG, "Video URL: "+ videoURL);

            Uri uri= Uri.parse( videoURL );

            videoView.setVideoURI(uri);
            videoView.requestFocus();

        } catch(Exception e) {
            Log.e(LOG_TAG, "Error Play URL Video: "+ e.getMessage());
            Toast.makeText(context,"Error Play URL Video: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void playLocalVideo(Context context, VideoView videoView, String localPath)  {
        try {

            videoView.setVideoURI(Uri.parse(localPath));
            videoView.requestFocus();

        } catch(Exception e) {
            Log.e(LOG_TAG, "Error Play Local Video: "+ e.getMessage());
            Toast.makeText(context,"Error Play Local Video: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
