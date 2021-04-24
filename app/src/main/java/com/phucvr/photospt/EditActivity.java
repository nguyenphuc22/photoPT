package com.phucvr.photospt;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.phucvr.photospt.model.Photo;
import com.phucvr.photospt.model.PhotoDetail;
import com.phucvr.photospt.model.Setting;
import com.phucvr.photospt.model.VideoViewUtils;

import java.io.File;
import java.io.IOException;

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

public class EditActivity extends AppCompatActivity {

    public static int PESDK_RESULT = 1;
    Photo mPhoto;
    AppCompatImageView imagePicker;
    VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        imagePicker = findViewById(R.id.img);
        videoView = findViewById(R.id.video);

        Intent intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_SEND)) {

            mPhoto = intent.getParcelableExtra(Setting.PICKERITEM);

        } else {

            mPhoto = new Photo(intent.getData().toString());

        }

        if (mPhoto != null)
        {
            if (mPhoto.isImage()) {

                imagePicker.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                Glide.with(this)
                        .asBitmap()
                        .load(mPhoto.getPath())
                        .error(R.drawable.ic_launcher_foreground)
                        .apply(new RequestOptions())
                        .into(imagePicker);

            } else {



                imagePicker.setVisibility(View.INVISIBLE);
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

                    } else {

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

                PhotoDetail a=new PhotoDetail();
                try {
                    a=showDetail(mPhoto.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return super.onOptionsItemSelected(item);
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
    public static PhotoDetail showDetail(String path) throws IOException {
        ExifInterface exif= new ExifInterface(path);
        PhotoDetail result= new PhotoDetail();
        result.date=exif.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
        String[] split=path.split("/");
        result.name=split[split.length-1];
        result.path=path;
        result.pixel=exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)+"x"+exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
        result.location=exif.getAttribute(ExifInterface.TAG_SUBJECT_LOCATION);
        File file=new File(path);
        long l= file.length();
        if(l< 1024*1024) {
            result.size=Long.toString(l/1024)+" KB";
        }
        else
            result.size= (Long.toString(l/(1024*1024)))+" MB";
        return result;
    }
    public void DeletePhoto(String path)  {
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?" , new String[]{ path });

    }
}


