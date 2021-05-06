package nguyenphuc.vr.photo.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.adapter.AdapterPhotos;
import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.ItemView;
import nguyenphuc.vr.photo.model.Photos;
import nguyenphuc.vr.photo.model.Settings;

public class PhotosFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterPhotos adapterPhotos;
    private ArrayList<ItemView> dataImage;
    private String action = Settings.ACTION_VIEW_PUBLIC;

    public PhotosFragment(int contentLayoutId, String action) {
        super(contentLayoutId);
        this.action = action;
    }

    public PhotosFragment()
    {

    }

    public PhotosFragment(String action) {
        this.action = action;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (action.equals(Settings.ACTION_VIEW_PUBLIC))
        {
            dataImage = ImageGrallery.getAlbum(getActivity());
        } else {
            dataImage = ImageGrallery.getAlbumHidden(getActivity());
        }

        recyclerView = getView().findViewById(R.id.recyclerPhotos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        adapterPhotos = new AdapterPhotos(dataImage,getActivity());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterPhotos);
    }

    @Override
    public void onResume() {
        super.onResume();
        newPicOrVideo(action);
    }

    public void newPicOrVideo(String action_View)
    {
        if (action_View.equals(Settings.ACTION_VIEW_HIDDEN))
        {
            dataImage = ImageGrallery.getAlbumHidden(getActivity());
            adapterPhotos = new AdapterPhotos(dataImage,getActivity());
            recyclerView.setAdapter(adapterPhotos);
            adapterPhotos.notifyDataSetChanged();
        } else {
            dataImage = ImageGrallery.getAlbum(getActivity());
            adapterPhotos = new AdapterPhotos(dataImage,getActivity());
            recyclerView.setAdapter(adapterPhotos);
            adapterPhotos.notifyDataSetChanged();
        }

        Log.i("PhotosFragment",action_View);

    }



}