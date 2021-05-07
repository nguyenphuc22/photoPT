package nguyenphuc.vr.photo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nguyenphuc.vr.photo.dialog.NewPassword_Dialog;
import nguyenphuc.vr.photo.dialog.Password_Dialog;
import nguyenphuc.vr.photo.fragment.AlbumsFragment;
import nguyenphuc.vr.photo.fragment.PhotosFragment;
import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.Settings;

public class MainActivity extends AppCompatActivity implements Password_Dialog.Password_DialogListener {

    private static final int WRITE_PERMISSION_CODE = 202;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;


    private Toolbar actionBar;
    private BottomNavigationView bottomNavigationView;


    PhotosFragment photosFragment;

    private String currentPhotoPath;
    private String mode_Theme;

    private String action_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.LOCATION_HARDWARE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SET_WALLPAPER}, WRITE_PERMISSION_CODE);
        }

        loadTheme();
        loadModeView();
        createFile();
        createHiddenDir();
        init();

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        settingToolBar(actionBar);
        actionBar.setTitle(R.string.Image);
        photosFragment = new PhotosFragment(action_View);
        loadFragment(photosFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadModeView();
        photosFragment.newPicOrVideo(action_View);
    }

    private void loadModeView() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        this.action_View = sharedPref.getString(Settings.ACTION_VIEW, Settings.ACTION_VIEW_PUBLIC);
    }

    private void loadTheme() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        this.mode_Theme = sharedPref.getString(Settings.THEME, Settings.DARK_THEME);
        if (this.mode_Theme.equals(Settings.DARK_THEME)) {

            if (this.mode_Theme.equals(Settings.DARK_THEME)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.item_Image: {
                    actionBar.setTitle(R.string.Image);
                    fragment = new PhotosFragment(action_View);
                    loadFragment(fragment);
                    return true;
                }
                case R.id.item_Album: {
                    actionBar.setTitle(R.string.Album);
                    fragment = new AlbumsFragment();
                    loadFragment(fragment);
                    return true;
                }
            }
            return false;
        }
    };

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.item_TakePhoto: {
                openCamera();
                break;
            }
            case R.id.item_Recording: {
                openVideo();
                break;
            }
            case R.id.item_Theme: {
                changeTheme();
                break;
            }
            case R.id.item_slide: {
                Intent intent = new Intent(this, SlideActivity.class);
                intent.setAction(action_View);
                startActivity(intent);
                break;
            }
            case R.id.item_HiddenMode: {
                SharedPreferences sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                String password_HiddenDir = sharedPref.getString(Settings.PASSWORD_HIDDENDIR, Settings.PASS_NULL);
                Log.i("EditActivity", password_HiddenDir);
                if (password_HiddenDir.contains(Settings.PASS_NULL)) {
                    // show dialog input new pass word
                    NewPassword_Dialog newPassword_dialog = new NewPassword_Dialog(this, sharedPref, Settings.ACTION_HIDDEN_MODE);
                    newPassword_dialog.show(getSupportFragmentManager(), String.valueOf(R.string.newpassword));

                } else {
                    // show dialog input pass word
                    Password_Dialog password_dialog = new Password_Dialog(password_HiddenDir, this, Settings.ACTION_HIDDEN_MODE);
                    password_dialog.show(getSupportFragmentManager(), String.valueOf(R.string.password));
                }
            }
            case R.id.item_PublicMode: {
                loadModeView();
                changeModeViewPublic();
                photosFragment.newPicOrVideo(action_View);
                loadFragment(photosFragment);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeModeViewPublic () {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        action_View = Settings.ACTION_VIEW_PUBLIC;
        editor.putString(Settings.ACTION_VIEW, Settings.ACTION_VIEW_PUBLIC);
        editor.apply();
        if (!editor.commit()) {
            Log.i("MainActivity", "Fail");
        }
    }

    private void changeTheme () {

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (this.mode_Theme.equals(Settings.LIGHT_THEME)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putString(Settings.THEME, Settings.DARK_THEME);
            editor.apply();
            this.mode_Theme = Settings.DARK_THEME;
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putString(Settings.THEME, Settings.LIGHT_THEME);
            editor.apply();
            this.mode_Theme = Settings.LIGHT_THEME;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        MenuItem item_PublicMode = menu.findItem(R.id.item_PublicMode);
        MenuItem item_HiddenMode = menu.findItem(R.id.item_HiddenMode);
        if (action_View.equals(Settings.ACTION_VIEW_PUBLIC)) {
            item_HiddenMode.setVisible(true);
            item_PublicMode.setVisible(false);
        } else {
            item_HiddenMode.setVisible(false);
            item_PublicMode.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void init () {
        actionBar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void settingToolBar (Toolbar toolbar){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void loadFragment (Fragment fragment){
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openCamera () {
        Log.i("CLick Camera", "Click");
        dispatchTakePictureIntent();
    }

    public void openVideo () {
        Log.i("Click Video", "Click");
        dispatchTakeVideoIntent();
    }


    private void dispatchTakePictureIntent () {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri = FileProvider.getUriForFile(this.getBaseContext(),
                getPackageName() + ".provider",
                photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(currentPhotoPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String name = String.valueOf(System.currentTimeMillis());
            MediaStore.Images.Media.insertImage(getContentResolver(), image, name, "NickSeven");
            String[] split = currentPhotoPath.split("/");
            String file = "/" + split[1] + "/" + split[2] + "/" + split[3] + "/" + split[4] + "/" + name + ".jpg";
            try {
                ExifInterface exif = new ExifInterface(file);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                String time = df.format(c.getTime());
                exif.setAttribute(ExifInterface.TAG_DATETIME, time);
                exif.saveAttributes();
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                LocationListener ll = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {

                    }
                };
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                lm.requestLocationUpdates("gps", 2000, 10, ll);
                Location location = lm.getLastKnownLocation("gps");
                double longitude = 0;
                double latitude = 0;
                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }
                exif.setLatLong(latitude, longitude);
                exif.saveAttributes();

            } catch (IOException e) {
                e.printStackTrace();
            }
            photosFragment.newPicOrVideo(action_View);
        }
        photosFragment.newPicOrVideo(action_View);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakeVideoIntent () {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private File createImageFile () throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void createFile () {
        File Folder = new File(ImageGrallery.getDirMyFile());
        Log.i("MainActivity", Folder.getPath());

        if (Folder.mkdir()) {

            Log.i("MainActivity", "Success!");
            ImageGrallery.galleryAddPic(Folder.getPath(), this);
            File mediaFile = new File(ImageGrallery.getDirLike());
            mediaFile.mkdir();
            Log.i("MainActivity", mediaFile.getPath());

        }
    }


    private void createHiddenDir ()
    {
        File Folder = new File(ImageGrallery.getDirMyFile());
        Log.i("MainActivity", Folder.getPath());

        if (Folder.mkdir())
            return;
        if (!Folder.exists())
            return;

        File mediaFile = new File(ImageGrallery.getDirHidden());

        if (mediaFile.mkdir())
            Log.i("MainActivity", mediaFile.getPath());

        if (mediaFile.exists()) {
            Log.i("MainActivity", "Success!");
        }
    }


    @Override
    public void onClickOke () {
        loadModeView();
        photosFragment.newPicOrVideo(action_View);
        loadFragment(photosFragment);
    }


}