package com.phucvr.photospt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;


import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout tableLayout;
    ViewPager viewPager;
    ImageView imgProfile;
    Toolbar toolbar;

    private static final int _WRITE_PERMISSION_CODE = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, _WRITE_PERMISSION_CODE);
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

    private boolean checkPermissionReadExternal()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }



    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == _WRITE_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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

    public void onClickCamera(View view)
    {
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }

}