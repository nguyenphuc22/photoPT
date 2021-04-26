package nguyenphuc.vr.photo.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.SlideActivity;
import nguyenphuc.vr.photo.model.ItemView;
import nguyenphuc.vr.photo.model.Photo;
import nguyenphuc.vr.photo.model.Photos;
import nguyenphuc.vr.photo.model.Type;

public class SlideAsyncTask extends AsyncTask<ArrayList<ItemView>, Photo, Void> {

    SlideActivity contextParent;

    public SlideAsyncTask(SlideActivity contextParent) {
        this.contextParent = contextParent;
    }

    @Override
    protected Void doInBackground(ArrayList<ItemView>... arrayLists) {
        for (int i = 0 ; i < arrayLists[0].size() ; i++) {
            if (arrayLists[0].get(i).getType() == Type.ALBUM)
            {
                Photos album = (Photos) arrayLists[0].get(i);
                for (Photo photo : album.getAlbum())
                {
                    publishProgress(photo);

                    SystemClock.sleep(SlideActivity.DURATION);
                }
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Hàm này sẽ chạy đầu tiên khi AsyncTask này được gọi
        //Ở đây mình sẽ thông báo quá trình load bắt đâu "Start"
        Toast.makeText(contextParent, R.string.Start, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Photo... values) {
        super.onProgressUpdate(values);

        Photo photo = values[0];

        ImageView imgA;

        imgA = contextParent.findViewById(R.id.imagePhotoA);

        Glide.with(contextParent)
                .load(photo.getPath())
                .centerCrop()
                .into(imgA);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Hàm này được thực hiện khi tiến trình kết thúc
        //Ở đây mình thông báo là đã "Finshed" để người dùng biết
        Toast.makeText(contextParent, R.string.Finished, Toast.LENGTH_SHORT).show();
    }
}
