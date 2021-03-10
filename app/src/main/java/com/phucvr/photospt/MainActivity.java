package com.phucvr.photospt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageView;


import com.google.android.material.tabs.TabLayout;


import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout tableLayout;
    ViewPager2 viewPager;
    ImageView imgProfile;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        settingToolBar(toolbar);


        viewPager.setAdapter(createCardAdapter());
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

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this,3);
        return adapter;
    }

}