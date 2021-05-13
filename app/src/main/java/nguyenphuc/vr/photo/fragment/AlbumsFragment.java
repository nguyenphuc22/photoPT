package nguyenphuc.vr.photo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.adapter.AdapterPhotos;
import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.ItemView;
import nguyenphuc.vr.photo.model.Settings;

public class AlbumsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterPhotos adapterAlbums;
    private ArrayList<ItemView> albums;

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
        albums = ImageGrallery.getAllAlbum(getActivity());

        recyclerView = getView().findViewById(R.id.recyclerPhotos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        adapterAlbums = new AdapterPhotos(albums,getActivity(),loadLine());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterAlbums);

    }

    private int loadLine()
    {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int line;
        line = sharedPref.getInt(Settings.LINE, Settings.LINE_DEFAULT);

        return line;
    }

    @Override
    public void onResume() {
        super.onResume();
        albums = ImageGrallery.getAllAlbum(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        adapterAlbums = new AdapterPhotos(albums,getActivity(),loadLine());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterAlbums);
    }
}