package nguyenphuc.vr.photo;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import ly.img.android.pesdk.PhotoEditorSettingsList;
import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic;
import ly.img.android.pesdk.assets.font.basic.FontPackBasic;
import ly.img.android.pesdk.assets.frame.basic.FramePackBasic;
import ly.img.android.pesdk.assets.overlay.basic.OverlayPackBasic;
import ly.img.android.pesdk.assets.sticker.emoticons.StickerPackEmoticons;
import ly.img.android.pesdk.assets.sticker.shapes.StickerPackShapes;
import ly.img.android.pesdk.backend.model.state.LoadSettings;
import ly.img.android.pesdk.backend.model.state.PhotoEditorSaveSettings;
import ly.img.android.pesdk.backend.model.state.manager.SettingsList;
import ly.img.android.pesdk.ui.activity.EditorBuilder;
import ly.img.android.pesdk.ui.model.state.UiConfigFilter;
import ly.img.android.pesdk.ui.model.state.UiConfigFrame;
import ly.img.android.pesdk.ui.model.state.UiConfigOverlay;
import ly.img.android.pesdk.ui.model.state.UiConfigSticker;
import ly.img.android.pesdk.ui.model.state.UiConfigText;
import nguyenphuc.vr.photo.dialog.Copy_Dialog;
import nguyenphuc.vr.photo.dialog.InFo_Dialog;
import nguyenphuc.vr.photo.dialog.NewPassword_Dialog;
import nguyenphuc.vr.photo.dialog.Password_Dialog;
import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.Photo;
import nguyenphuc.vr.photo.model.PhotoDetail;
import nguyenphuc.vr.photo.model.Settings;
import nguyenphuc.vr.photo.model.VideoViewUtils;

public class EditActivity extends AppCompatActivity implements
        NewPassword_Dialog.NewPassword_DialogListener ,
        Password_Dialog.Password_DialogListenerActionHidden {
    public static int PESDK_RESULT = 1;

    private Toolbar toolbar;
    private VideoView videoView;
    private AppCompatImageView imageView;

    private Photo mPhoto;
    private MediaController mediaController;

    private int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        settingToolBar(toolbar);
        Intent intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_SEND)) {
            mPhoto = intent.getParcelableExtra(Settings.PICKERITEM);
        } else {
            mPhoto = new Photo(intent.getData().toString());
        }
        if (mPhoto != null)
        {
            toolbar.setTitle(mPhoto.getDisplayName());
            if (mPhoto.isImage()) {

                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                Glide.with(this)
                        .asBitmap()
                        .load(mPhoto.getPath())
                        .error(R.drawable.ic_launcher_foreground)
                        .apply(new RequestOptions())
                        .into(imageView);

            } else {



                imageView.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.VISIBLE);


                VideoViewUtils.playLocalVideo(EditActivity.this, videoView, mPhoto.getPath());

                mediaController = new MediaController(EditActivity.this);


                videoView.setMediaController(mediaController);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoView.seekTo(position);
                        if (position == 0)
                        {
                            videoView.start();
                        }
                        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            }
                        });
                    }
                });


            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item_like = menu.findItem(R.id.item_like);
        MenuItem item_unLike = menu.findItem(R.id.item_unLike);
        if (mPhoto.getPath().contains(ImageGrallery.FILENAME_LIKE))
        {
            item_like.setVisible(false);
            item_unLike.setVisible(true);
        } else
        {
            item_like.setVisible(true);
            item_unLike.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_edit:
            {
                if (mPhoto != null)
                {
                    if (mPhoto.isImage())
                    {
                        openEditor(Uri.fromFile(new File(mPhoto.getPath())));
                    }
                }
                break;
            }
            case R.id.item_delete:
            {
                //Gọi hàm xóa ở đây nha ông
                //Data
                DeletePhoto(mPhoto.getPath());
                break;
            }
            case R.id.item_inFor:
            {
                //Gọi hàm xem infor ở đây nha ông
                //Data
                try {
                    showDetail(mPhoto.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.item_Share:
            {
                shareImageOrVideo();
                break;
            }
            case R.id.item_defaultImage:
            {
                setDefaultImage(mPhoto);
                break;
            }
            case R.id.item_copy:
            {
                Copy_Dialog copy_dialog = new Copy_Dialog(mPhoto);
                copy_dialog.show(getSupportFragmentManager(),getString(R.string.selectPath));
                break;
            }
            case R.id.item_like:
            {
                try {
                    Copy_Dialog.copy(new File(mPhoto.getPath()),new File(ImageGrallery.getDirLike() + "/" + mPhoto.getDisplayName()));
                    Log.i("EditActivity",ImageGrallery.getDirLike() + "/" + mPhoto.getDisplayName());
                    ImageGrallery.galleryAddPic(ImageGrallery.getDirLike() + "/" + mPhoto.getDisplayName(),this);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this,getString(R.string.fail),Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.item_unLike:
            {
                DeletePhoto(mPhoto.getPath());
                break;
            }
            case R.id.item_hidden:
            {
                SharedPreferences sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                String password_HiddenDir = sharedPref.getString(Settings.PASSWORD_HIDDENDIR,Settings.PASS_NULL);

                Log.i("EditActivity",password_HiddenDir);
                if (password_HiddenDir.contains(Settings.PASS_NULL))
                {
                    // show dialog input new pass word
                    NewPassword_Dialog newPassword_dialog = new NewPassword_Dialog(this,sharedPref,mPhoto,Settings.ACTION_HIDDEN);
                    newPassword_dialog.show(getSupportFragmentManager(), String.valueOf(R.string.newpassword));

                } else {
                    // show dialog input pass word
                    Password_Dialog password_dialog = new Password_Dialog(password_HiddenDir,mPhoto,this,Settings.ACTION_HIDDEN);
                    password_dialog.show(getSupportFragmentManager(), String.valueOf(R.string.password));
                }



            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDefaultImage(Photo photo) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();//wallaper
        Bitmap image = BitmapFactory.decodeFile(photo.getPath(),bmOptions);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this.getApplicationContext());
        try {
            wallpaperManager.setBitmap(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }

    private void openEditor(Uri inputImage) {
        SettingsList settingsList = createPesdkSettingsList();

        // Set input image
        settingsList.getSettingsModel(LoadSettings.class).setSource(inputImage);


        settingsList.getSettingsModel(PhotoEditorSaveSettings.class).setOutputToGallery(Environment.DIRECTORY_DCIM);

        new EditorBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, PESDK_RESULT);

    }

    private SettingsList createPesdkSettingsList() {

        // Create a empty new SettingsList and apply the changes on this referance.
        PhotoEditorSettingsList settingsList = new PhotoEditorSettingsList();

        // If you include our asset Packs and you use our UI you also need to add them to the UI,
        // otherwise they are only available for the backend
        // See the specific feature sections of our guides if you want to know how to add our own Assets.

        settingsList.getSettingsModel(UiConfigFilter.class).setFilterList(
                FilterPackBasic.getFilterPack()
        );

        settingsList.getSettingsModel(UiConfigText.class).setFontList(
                FontPackBasic.getFontPack()
        );

        settingsList.getSettingsModel(UiConfigFrame.class).setFrameList(
                FramePackBasic.getFramePack()
        );

        settingsList.getSettingsModel(UiConfigOverlay.class).setOverlayList(
                OverlayPackBasic.getOverlayPack()
        );

        settingsList.getSettingsModel(UiConfigSticker.class).setStickerLists(
                StickerPackEmoticons.getStickerCategory(),
                StickerPackShapes.getStickerCategory()
        );

        return settingsList;
    }

    public void showDetail(String path) throws IOException {

        ExifInterface exif= new ExifInterface(path);
        PhotoDetail result= new PhotoDetail();

        result.setDate(exif.getAttribute(ExifInterface.TAG_DATETIME));
        String[] split=path.split("/");
        result.setName(split[split.length-1]);
        result.setPath(path);
        result.setPixel(exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)+"x"+exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));

        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        double[] LatLong = exif.getLatLong();

        if(LatLong==null)
            result.setLocation("Không có vị trí");
        else {
            addresses = geocoder.getFromLocation(LatLong[0],LatLong[1],1);
            result.setLocation(addresses.get(0).getAddressLine(0) + addresses.get(0).getLocality());
        }
        File file=new File(path);
        long size_File= file.length();
        if(size_File < PhotoDetail.MB ) {
            result.setSize(Long.toString(size_File /PhotoDetail.KB)+" KB");
        }
        else {
            result.setSize((Long.toString(size_File / (PhotoDetail.MB)))+" MB");
        }

        InFo_Dialog inFo_dialog = new InFo_Dialog(result);
        inFo_dialog.show(getSupportFragmentManager(), String.valueOf(R.string.info));
    }

    public void DeletePhoto(String path)  {
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "= ?" , new String[]{ path });

        contentResolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "= ?" , new String[]{ path });
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentPosition",videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }

    private void init()
    {
        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.img);
        videoView = findViewById(R.id.video);
    }

    private void settingToolBar(Toolbar toolbar)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void shareImageOrVideo()
    {
        Intent shareIntent = new Intent();//share
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        shareIntent.setAction(Intent.ACTION_SEND);
        Log.i("EditActivity",getPackageName() + ".provider");
        Uri imageUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider", //(use your app signature + ".provider" )
                new File(mPhoto.getPath()));
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        this.grantUriPermission("android", imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share File"));
        finish();
    }

    @Override
    public void onClickOke_Copy(String path) {
        DeletePhoto(path);
    }

    @Override
    public void onClickOke(String path) {
        DeletePhoto(path);
    }
}