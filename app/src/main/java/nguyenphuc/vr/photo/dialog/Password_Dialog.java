package nguyenphuc.vr.photo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.fragment.PhotosFragment;
import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.Photo;
import nguyenphuc.vr.photo.model.Settings;

public class Password_Dialog extends DialogFragment {

    private EditText editTextPassword;
    private String systemPassword;
    private Photo mPhoto;
    private Context context;
    private String action;
    SharedPreferences sharedPreferences;

    public Password_Dialog(String systemPassword, Photo mPhoto, Context context, String action) {
        this.systemPassword = systemPassword;
        this.mPhoto = mPhoto;
        this.context = context;
        this.action = action;
    }

    public Password_Dialog(String systemPassword, Context context, String action) {
        this.systemPassword = systemPassword;
        this.context = context;
        this.action = action;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialoginputpassword,null);
        editTextPassword = view.findViewById(R.id.edtPassword);
        builder.setView(view)
                .setTitle(R.string.password)
                .setNegativeButton(R.string.ok, null)
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
                Log.i("Password_Dialog",systemPassword);
                Log.i("Password_Dialog",editTextPassword.getText().toString());

                if (editTextPassword.getText().toString().equals(systemPassword))
                {
                    if (action.equals(Settings.ACTION_HIDDEN)) {

                        try {
                            ImageGrallery.copy(new File(mPhoto.getPath()), new File(ImageGrallery.getDirHidden() + "/" + mPhoto.getDisplayName()));
                            ImageGrallery.galleryAddPic(ImageGrallery.getDirHidden() + "/" + mPhoto.getDisplayName(), context);
                            ((Password_DialogListenerActionHidden)requireActivity()).onClickOke(mPhoto.getPath());
                            Log.i("Password_Dialog", ImageGrallery.getDirHidden() + "/" + mPhoto.getDisplayName());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        sharedPreferences = context.getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Settings.ACTION_VIEW, Settings.ACTION_VIEW_HIDDEN);
                        editor.apply();

                        ((Password_DialogListener) requireActivity()).onClickOke();
                    }

                    dismiss();

                } else {

                    Log.i("Password_dialog", getString(R.string.errorNotSame));
                    editTextPassword.setError(getString(R.string.errorNotTrue));

                }
            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Password_dialog", getString(R.string.cancel));
                dismiss();
            }
        });

    }

    public interface Password_DialogListener {
        void onClickOke();
    }
    public interface Password_DialogListenerActionHidden {
        void onClickOke(String path);
    }
}
