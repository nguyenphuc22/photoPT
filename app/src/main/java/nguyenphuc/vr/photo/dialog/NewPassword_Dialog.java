package nguyenphuc.vr.photo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.Photo;
import nguyenphuc.vr.photo.model.Settings;

public class NewPassword_Dialog extends DialogFragment {

    EditText edtPassword,edtPasswordAgain;
    Context context;
    SharedPreferences sharedPreferences;
    Photo mPhoto;
    String action;

    public NewPassword_Dialog(Context context, SharedPreferences sharedPreferences, Photo mPhoto, String action) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.mPhoto = mPhoto;
        this.action = action;
    }

    public NewPassword_Dialog(Context context, SharedPreferences sharedPreferences, String action) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.action = action;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialoginputnewpassword,null);
        edtPassword = view.findViewById(R.id.edtNewPassword);
        edtPasswordAgain = view.findViewById(R.id.edtNewPasswordAgain);

        builder.setView(view)
                .setTitle(R.string.newpassword)
                .setNegativeButton(R.string.ok,null)
                .setPositiveButton(R.string.cancel,null);


        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog dialog = (AlertDialog) getDialog();
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password,passwordAgain;

                password = edtPassword.getText().toString();
                passwordAgain = edtPasswordAgain.getText().toString();

                if (password.equals(passwordAgain))
                {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Settings.PASSWORD_HIDDENDIR, password);
                    Log.i("NewPassword_Dialog",password);
                    editor.apply();
                    if (action == Settings.ACTION_HIDDEN) {
                        try {
                            ImageGrallery.copy(new File(mPhoto.getPath()), new File(ImageGrallery.getDirHidden() + "/" + mPhoto.getDisplayName()));
                            ImageGrallery.galleryAddPic(ImageGrallery.getDirHidden() + "/" + mPhoto.getDisplayName(), context);
                            Log.i("NewPassword_Dialog", ImageGrallery.getDirHidden() + "/" + mPhoto.getDisplayName());
                            ((NewPassword_DialogListener)requireActivity()).onClickOke_Copy(mPhoto.getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                        }
                    }

                    dialog.dismiss();

                } else {

                    edtPassword.setError(getString(R.string.errorNotSame));
                    edtPasswordAgain.setError(getString(R.string.errorNotSame));

                }
            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    public interface NewPassword_DialogListener {
        void onClickOke_Copy(String path);
    }
}
