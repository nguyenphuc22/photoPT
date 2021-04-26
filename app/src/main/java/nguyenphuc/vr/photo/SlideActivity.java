package nguyenphuc.vr.photo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.ItemView;
import nguyenphuc.vr.photo.model.Photo;
import nguyenphuc.vr.photo.model.Photos;
import nguyenphuc.vr.photo.model.Type;
import nguyenphuc.vr.photo.task.SlideAsyncTask;

public class SlideActivity extends AppCompatActivity {

    private ArrayList<ItemView> dataImage;
    public static long DURATION = 3000;
    private SlideAsyncTask slideAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        dataImage = ImageGrallery.getAlbum(this);

        slideAsyncTask = new SlideAsyncTask(this);

        slideAsyncTask.execute(dataImage);

    }

}