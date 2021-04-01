package com.phucvr.photospt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;


import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout tableLayout;
    ViewPager viewPager;
    ImageView imgProfile;
    Toolbar toolbar;

    private static final int _READ_PERMISSION_CODE = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkPermissionReadExternal()){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},_READ_PERMISSION_CODE);
        } else {

        }
        setContentView(R.layout.activity_main);
        init();
        settingToolBar(toolbar);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Create Fragment
        PhotosFragment photosFragment = new PhotosFragment("nick");
        PhotosFindFragment findFragment = new PhotosFindFragment("nick");
        PhotosLibaryFragment libaryFragment = new PhotosLibaryFragment("nick");

        adapter.addFragment(photosFragment);
        adapter.addFragment(findFragment);
        adapter.addFragment(libaryFragment);

        // Set adapter
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

        // Set Text And Icon
        tableLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_image_24);
        tableLayout.getTabAt(0).setText(getString(R.string.tab_1));
        tableLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_search_24);
        tableLayout.getTabAt(1).setText(getString(R.string.tab_2));
        tableLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_photo_library_24);
        tableLayout.getTabAt(2).setText(getString(R.string.tab_3));


    }

    private boolean checkPermissionReadExternal()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == _READ_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Read external stoage permission granted",Toast.LENGTH_SHORT).show();
            } else
            {
                Toast.makeText(this,"Read external stoage permission denied",Toast.LENGTH_SHORT).show();
            }
        }
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



}