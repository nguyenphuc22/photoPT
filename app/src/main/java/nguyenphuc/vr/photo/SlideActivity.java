package nguyenphuc.vr.photo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import nguyenphuc.vr.photo.adapter.AdapterSlide;
import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.ItemView;
import nguyenphuc.vr.photo.model.Settings;

public class SlideActivity extends AppCompatActivity {

    private ArrayList<ItemView> dataImage;
    public static int DURATION = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        Intent intent = getIntent();

        if (intent.getAction().equals(Settings.ACTION_VIEW_PUBLIC))
        {
            dataImage = ImageGrallery.getAlbum(this);
        } else {
            dataImage = ImageGrallery.getAlbumHidden(this);
        }


        SliderView sliderView = findViewById(R.id.slider);

        // passing this array list inside our adapter class.
        AdapterSlide adapter = new AdapterSlide(this, dataImage);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(DURATION);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

    }

}