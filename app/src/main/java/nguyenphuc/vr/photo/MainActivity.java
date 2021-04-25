package nguyenphuc.vr.photo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import nguyenphuc.vr.photo.fragment.AlbumsFragment;
import nguyenphuc.vr.photo.fragment.PhotosFragment;
import nguyenphuc.vr.photo.model.Setting;

public class MainActivity extends AppCompatActivity {

    private static final int WRITE_PERMISSION_CODE = 202;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;

    private Toolbar actionBar;
    private BottomNavigationView bottomNavigationView;

    PhotosFragment photosFragment;

    private String mode_Theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, WRITE_PERMISSION_CODE);
        }

        loadTheme();


        init();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        settingToolBar(actionBar);
        actionBar.setTitle(R.string.Image);
        photosFragment = new PhotosFragment();
        loadFragment(photosFragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        photosFragment.newPicOrVideo();
    }

    private void loadTheme() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        this.mode_Theme = sharedPref.getString(Setting.THEME, Setting.DARK_THEME);
        if (this.mode_Theme.equals(Setting.DARK_THEME))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.item_Image:
                {
                    actionBar.setTitle(R.string.Image);
                    fragment = new PhotosFragment();
                    loadFragment(fragment);
                    return true;
                }
                case R.id.item_Album:
                {
                    actionBar.setTitle(R.string.Album);
                    fragment = new AlbumsFragment();
                    loadFragment(fragment);
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_TakePhoto:
            {
                openCamera();
                break;
            }
            case R.id.item_Recording:
            {
                openVideo();
                break;
            }
            case R.id.item_Theme:
            {
                changeTheme();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeTheme() {

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (this.mode_Theme.equals(Setting.LIGHT_THEME))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putString(Setting.THEME, Setting.DARK_THEME);
            editor.apply();
            this.mode_Theme = Setting.DARK_THEME;
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putString(Setting.THEME, Setting.LIGHT_THEME);
            editor.apply();
            this.mode_Theme = Setting.LIGHT_THEME;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    private void init()
    {
        actionBar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void settingToolBar(Toolbar toolbar)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openCamera() {
        Log.i("CLick Camera","Click");
        dispatchTakePictureIntent();
    }

    public void openVideo()
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