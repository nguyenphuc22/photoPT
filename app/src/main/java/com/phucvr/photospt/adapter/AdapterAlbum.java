package com.phucvr.photospt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phucvr.photospt.R;
import com.phucvr.photospt.model.Album;
import com.phucvr.photospt.model.ItemView;
import com.phucvr.photospt.model.Time;
import com.phucvr.photospt.model.Type;

import java.util.ArrayList;

public class AdapterAlbum extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ItemView> mArrayList;
    Context mContext;

    public AdapterAlbum(ArrayList<ItemView> mArrayList, Context mContext) {
        this.mArrayList = mArrayList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0)
        {
            view = inflater.inflate(R.layout.itemphotos,parent,false);
            return new AlbumViewHolder(view);
        } else {

            view = inflater.inflate(R.layout.itemtime,parent,false);
            return new TimeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0)
        {
            AlbumViewHolder albumViewHolder = (AlbumViewHolder) holder;
            Album mAlbum = (Album) this.mArrayList.get(position);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mContext,4);
            AdapterPhotos adapterPhotos = new AdapterPhotos(this.mContext,mAlbum.getAlbum());
            albumViewHolder.recyclerView.setLayoutManager(gridLayoutManager);
            albumViewHolder.recyclerView.setAdapter(adapterPhotos);
        } else
        {
            TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
            Time time = (Time) this.mArrayList.get(position);
            timeViewHolder.txtTime.setText(time.getMonYear());
        }
    }

    @Override
    public int getItemCount() {
        return this.mArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (this.mArrayList.get(position).getType() == Type.ALBUM)
        {
            return 0;
        } else {
            return 1;
        }
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

    class TimeViewHolder extends RecyclerView.ViewHolder{
        TextView txtTime;
        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }

}
