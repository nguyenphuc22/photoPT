package nguyenphuc.vr.photo.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoViewUtils {
    public static final String LOG_TAG= "AndroidVideoView";


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
