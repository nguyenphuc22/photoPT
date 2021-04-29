package nguyenphuc.vr.photo.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.adapter.AdapterAlbums;
import nguyenphuc.vr.photo.model.Album;
import nguyenphuc.vr.photo.model.ImageGrallery;

public class AlbumsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterAlbums adapterAlbums;
    private ArrayList<Album> albums;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy albums
        ImageGrallery.getAllAlbum(getContext());

        String path = Environment.getExternalStorageDirectory().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Albums_Files", "FileName:" + files[i].getName());
            Log.d("Albums_ISFILE", String.valueOf(files[i].isFile()));
            Log.d("Albums_ISDIR", String.valueOf(files[i].isDirectory()));
            
        }


    }

}