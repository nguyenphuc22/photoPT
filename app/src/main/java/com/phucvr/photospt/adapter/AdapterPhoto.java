package com.phucvr.photospt.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.phucvr.photospt.EditActivity;
import com.phucvr.photospt.R;
import com.phucvr.photospt.model.Photo;
import com.phucvr.photospt.model.Setting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.ImgViewHolder> {
    private ArrayList<Photo> data;
    private Context context;



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

        Log.i("Video", data.get(position).getPath());

        Glide.with(this.context)
                .load(data.get(position).getPath())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imgViewHolder.imgPhoto);

        if (!data.get(position).isImage()) {
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
                    intent.putExtra(Setting.PICKERITEM, (Parcelable) data.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

        }

    }




}
