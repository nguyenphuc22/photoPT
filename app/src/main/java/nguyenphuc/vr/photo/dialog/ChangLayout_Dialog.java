package nguyenphuc.vr.photo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.model.ImageGrallery;
import nguyenphuc.vr.photo.model.Settings;

public class ChangLayout_Dialog extends DialogFragment {

    int line_now;
    ArrayList<Integer> lines;
    Context context;

    Spinner spinner;
    ArrayAdapter arrayAdapter;


    public ChangLayout_Dialog(int line_now, ArrayList<Integer> lines, Context context) {
        this.line_now = line_now;
        this.lines = lines;
        this.context = context;
    }

    public ChangLayout_Dialog(int line_now, ArrayList<Integer> lines) {
        this.line_now = line_now;
        this.lines = lines;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogcopy,null);
        spinner = view.findViewById(R.id.spinner);

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,lines);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        builder.setView(view)
                .setTitle(R.string.column)
                .setNegativeButton(R.string.ok, null)
                .setPositiveButton(R.string.cancel,null);

        return builder.create();
    }

    @Override
    public void onResume() {

        AlertDialog dialog = (AlertDialog) getDialog();
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);


        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChangLayout_DialogListener)requireActivity()).onClickOke(Integer.parseInt(spinner.getSelectedItem().toString()));
                dismiss();
            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Password_dialog", getString(R.string.cancel));
                dismiss();
            }
        });

        super.onResume();
    }
    public interface ChangLayout_DialogListener {
        void onClickOke(int line);
    }
}
