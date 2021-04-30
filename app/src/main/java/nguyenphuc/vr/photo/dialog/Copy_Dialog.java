package nguyenphuc.vr.photo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.Photo;

public class Copy_Dialog extends DialogFragment {

    ArrayList<String> dirs;
    ArrayAdapter arrayAdapter;
    Spinner spinner;
    private static String COPY = "copy";
    Photo photo;

    public Copy_Dialog(Photo photo) {
        this.photo = photo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogcopy,null);
        spinner = view.findViewById(R.id.spinner);
        dirs = ImageGrallery.getDir(getContext());

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,dirs);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);


        builder.setView(view)
                .setTitle(R.string.selectPath)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dir = spinner.getSelectedItem().toString();
                        String path = ImageGrallery.getPath(getActivity(),dir);
                        Log.i("Copy_Dialog", path);
                        Log.i("Copy_Dialog", photo.getPath());
                        Log.i("Copy_Dialog", photo.getDisplayName());

                        if (path != null)
                        {
                            String name = COPY + "_" + photo.getDisplayName();

                            Log.i("Copy_Dialog_PathResult",path + "/" + name);

                            try {
                                copy(new File(photo.getPath()), new File(path + "/" + name));
                                galleryAddPic(path + "/" + name);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.i("Copy_Dialog_Error","Can't copy");
                                Toast.makeText(getActivity(),R.string.fail,Toast.LENGTH_SHORT).show();
                            }
                        }
                        dismiss();
                    }
                })
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        ;

        return builder.create();
    }

    private static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }




}
