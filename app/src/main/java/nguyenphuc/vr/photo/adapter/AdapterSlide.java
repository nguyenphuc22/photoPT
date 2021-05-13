package nguyenphuc.vr.photo.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.SlideActivity;
import nguyenphuc.vr.photo.model.ItemView;
import nguyenphuc.vr.photo.model.Photo;
import nguyenphuc.vr.photo.model.Photos;
import nguyenphuc.vr.photo.model.Type;

public class AdapterSlide extends SliderViewAdapter<AdapterSlide.SliderAdapterViewHolder>{

    // list for storing urls of images.
    private final ArrayList<String> mSliderItems;
    private Context context;

    public AdapterSlide(Context context,ArrayList<ItemView> mSliderItems) {
        this.mSliderItems = new ArrayList<>();
        this.context = context;
        for (ItemView itemView : mSliderItems)
        {
            if (itemView.getType() == Type.ALBUM)
            {
                Photos album = (Photos) itemView;
                for (Photo photo : album.getAlbum())
                {
                    this.mSliderItems.add(photo.getPath());
                }
            }
        }

    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemslide, null);
        return new SliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {
        Glide.with(viewHolder.itemView)
                .load(this.mSliderItems.get(position))
                .fitCenter()
                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return this.mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }
}
