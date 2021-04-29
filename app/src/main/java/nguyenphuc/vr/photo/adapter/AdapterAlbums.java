package nguyenphuc.vr.photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.model.Album;

public class AdapterAlbums extends RecyclerView.Adapter<AdapterAlbums.ViewHolderAlbums> {

    ArrayList<Album> mAlbums;
    Context mContext;

    public AdapterAlbums(ArrayList<Album> mAlbums, Context mContext) {
        this.mAlbums = mAlbums;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolderAlbums onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemalbum,parent,false);
        return new ViewHolderAlbums(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAlbums holder, int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(this.mAlbums.get(position).getThumbnail())
                .centerCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);

        holder.txtName.setText(this.mAlbums.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.mAlbums.size();
    }

    public void clear() {
        mAlbums.clear();
        notifyDataSetChanged();
    }

    public class ViewHolderAlbums extends RecyclerView.ViewHolder {

        SelectableRoundedImageView imageView;
        TextView txtName,txtNumber;

        public ViewHolderAlbums(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageAlbum);
            txtName = itemView.findViewById(R.id.txtName);
            txtNumber = itemView.findViewById(R.id.txtNumber);
        }
    }


}
