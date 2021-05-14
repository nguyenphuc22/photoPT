package nguyenphuc.vr.photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.model.ItemView;
import nguyenphuc.vr.photo.model.Photos;
import nguyenphuc.vr.photo.model.Type;

public class AdapterPhotos extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<ItemView> mArrayList;
    Context mContext;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    int line;

    public AdapterPhotos(ArrayList<ItemView> mArrayList, Context mContext, AdapterView.OnItemClickListener mOnItemClickListener, int line) {
        this.mArrayList = mArrayList;
        this.mContext = mContext;
        this.mOnItemClickListener = mOnItemClickListener;
        this.line = line;
    }

    public AdapterPhotos(ArrayList<ItemView> mArrayList, Context mContext, int line) {
        this.mArrayList = mArrayList;
        this.mContext = mContext;
        this.line = line;
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

            view = inflater.inflate(R.layout.itemtitle,parent,false);
            return new TitleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0)
        {
            AlbumViewHolder albumViewHolder = (AlbumViewHolder) holder;
            Photos mPhotos = (Photos) this.mArrayList.get(position);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mContext,line);
            AdapterPhoto adapterPhoto = new AdapterPhoto(this.mContext, mPhotos.getAlbum());
            albumViewHolder.recyclerView.setLayoutManager(gridLayoutManager);
            albumViewHolder.recyclerView.setAdapter(adapterPhoto);
        } else
        {
            TitleViewHolder timeViewHolder = (TitleViewHolder) holder;
            timeViewHolder.txtTitle.setText(this.mArrayList.get(position).getTitle());
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

    class TitleViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }

}
