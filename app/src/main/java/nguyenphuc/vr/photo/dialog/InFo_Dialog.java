package nguyenphuc.vr.photo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import nguyenphuc.vr.photo.R;
import nguyenphuc.vr.photo.model.PhotoDetail;

public class InFo_Dialog extends DialogFragment {

    PhotoDetail photoDetail;
    TextView txtDate,txtSize,txtPixel,txtPath,txtName,txtLocation;
    public InFo_Dialog(PhotoDetail photoDetail) {
        this.photoDetail = photoDetail;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialoginfo,null);

        builder.setView(view)
                .setTitle(R.string.info)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        txtDate = view.findViewById(R.id.txtDate);
        txtLocation = view.findViewById(R.id.txtLocation);
        txtName = view.findViewById(R.id.txtName);
        txtPath = view.findViewById(R.id.txtPath);
        txtPixel = view.findViewById(R.id.txtPixel);
        txtSize = view.findViewById(R.id.txtSize);

        txtSize.setText(getString(R.string.size) + ":" + photoDetail.getSize());
        txtPixel.setText(getString(R.string.pixel) + ":" + photoDetail.getPixel());
        txtName.setText(getString(R.string.name) + ":" + photoDetail.getName());
        txtLocation.setText(getString(R.string.location) + ":" + photoDetail.getLocation());
        txtDate.setText(getString(R.string.date) + ":" + photoDetail.getDate());
        txtPath.setText(getString(R.string.path) + ":" + photoDetail.getPath());

        return builder.create();
    }
}
