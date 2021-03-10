package com.phucvr.photospt;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    int numberFragment = 0;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int number) {
        super(fragmentActivity);
        this.numberFragment = number;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
            {
                return new PhotosFragment("Photos");
            }
            case 1:
            {
                return new PhotosFindFragment("Photos Find");
            }
            case 2:
            {
                return new PhotosLibaryFragment("Photos Libary");
            }
            default:
                return new PhotosLibaryFragment("Photos");
        }
    }

    @Override
    public int getItemCount() {
        return numberFragment;
    }
}
