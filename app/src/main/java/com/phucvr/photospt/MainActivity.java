package com.phucvr.photospt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;


import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ly.img.android.pesdk.backend.model.constant.Directory;
import ly.img.android.pesdk.backend.model.state.CameraSettings;
import ly.img.android.pesdk.backend.model.state.manager.SettingsList;
import ly.img.android.pesdk.ui.activity.CameraPreviewBuilder;
import ly.img.android.pesdk.ui.utils.PermissionRequest;

public class MainActivity extends AppCompatActivity  {

    TabLayout tableLayout;
    ViewPager viewPager;
    ImageView imgProfile;
    Toolbar toolbar;
    PhotosFragment photosFragment;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;

    private static final int _WRITE_PERMISSION_CODE = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, _WRITE_PERMISSION_CODE);
        } else {

        }

        setContentView(R.layout.activity_main);
        init();
        settingToolBar(toolbar);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Create Fragment
        photosFragment = new PhotosFragment("nick");
        PhotosFindFragment findFragment = new PhotosFindFragment("nick");
        PhotosLibaryFragment libaryFragment = new PhotosLibaryFragment("nick");

        adapter.addFragment(photosFragment);
        adapter.addFragment(findFragment);
        adapter.addFragment(libaryFragment);

        // Set adapter
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

        // Set Text And Icon
        tableLayout.getTabAt(0).setText(getString(R.string.tab_1));
        tableLayout.getTabAt(1).setText(getString(R.string.tab_2));
        tableLayout.getTabAt(2).setText(getString(R.string.tab_3));


        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == _WRITE_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            MediaStore.Images.Media.insertImage(getContentResolver(),imageBitmap,"IMG" + System.currentTimeMillis(),"Dit me google");
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            videoUri.getAuthority();
        }
        // Cập nhât lại danh mục sao khi chụp ảnh mới.
        photosFragment.newPicOrVideo();

    }

    private void settingToolBar(Toolbar toolbar)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void init()
    {
        tableLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolBar);
        imgProfile = findViewById(R.id.imgProfile);
    }

    public void onClickCamera(View view) {
        Log.i("CLick Camera","Click");
        dispatchTakePictureIntent();
    }

    public void onClickVideo(View view)
    {
        Log.i("Click Video","Click");
        dispatchTakeVideoIntent();
    }
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


}