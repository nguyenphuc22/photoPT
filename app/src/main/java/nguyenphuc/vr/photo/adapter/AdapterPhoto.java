package nguyenphuc.vr.photo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nguyenphuc.vr.photo.EditActivity;
import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.model.Photo;
import nguyenphuc.vr.photo.model.Settings;

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.ImgViewHolder>{
    private ArrayList<Photo> data;
    private Context context;
    private String VIDEO = "mp4";


    public AdapterPhoto(Context context, ArrayList<Photo> data) {
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public ImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.itemphoto, parent, false);
        return new ImgViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgViewHolder holder, int position) {
        ImgViewHolder imgViewHolder = (ImgViewHolder) holder;

        Log.i("Adapter Photo Path", data.get(position).getPath());

        Glide.with(this.context)
                .load(data.get(position).getPath())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imgViewHolder.imgPhoto);

        if (!data.get(position).isImage() && data.get(position).getPath().contains(VIDEO)) {
            imgViewHolder.txtDuration.setText(
                    new SimpleDateFormat("mm:ss").format(new Date(data.get(position).getDuration())));
        } else {
            imgViewHolder.txtDuration.setText(" ");
        }
    }


    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ImgViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgPhoto;
        TextView txtDuration;
        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtDuration = itemView.findViewById(R.id.txtDuration);

            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), EditActivity.class);
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Settings.PICKERITEM, (Parcelable) data.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

        }

    }
}
