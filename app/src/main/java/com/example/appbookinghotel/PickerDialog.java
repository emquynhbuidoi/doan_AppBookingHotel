package com.example.appbookinghotel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PickerDialog extends AppCompatDialogFragment {
    EditText edtSoPhong, edtSoNguoiLon, edtSoTreEm;
    DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_picker, null);

        builder.setView(view)
                .setTitle("Chọn Số Lượng")
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String soPhong = edtSoPhong.getText().toString();
                        String soNguoiLon = edtSoNguoiLon.getText().toString();
                        String soTreEm = edtSoTreEm.getText().toString();
                        listener.applyTexts(soPhong, soNguoiLon, soTreEm);
                    }
                });
        edtSoPhong = view.findViewById(R.id.edtSoPhong);
        edtSoNguoiLon = view.findViewById(R.id.edtSoNguoiLon);
        edtSoTreEm = view.findViewById(R.id.edtSoTreEm);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (DialogListener) context;
    }

    public interface DialogListener{
        void applyTexts(String soPhong, String soNguoiLon, String soTreEm);
    }
}
