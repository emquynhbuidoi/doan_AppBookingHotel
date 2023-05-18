package com.example.appbookinghotel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FragmentcaNhan extends Fragment {

    private TextView tvEmail;
    private EditText edtUserName;
    private Button btnDoiAnh, btnThayDoiTen;
    private ImageView ivUserAvatar;

    private Button btnDangXuat;
    private String name;
    private ProgressDialog progressDialog;
    private TextView tvChangePassWord;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ca_nhan, container, false);

        progressDialog = new ProgressDialog(getActivity());
        tvChangePassWord = v.findViewById(R.id.tvChangePassWord);

        edtUserName = v.findViewById(R.id.edtUserName);
        tvEmail = v.findViewById(R.id.tvUserEmail);
        btnDoiAnh = v.findViewById(R.id.btnDoiAnh);
        btnThayDoiTen = v.findViewById(R.id.btnThayDoiTen);
        ivUserAvatar = v.findViewById(R.id.ivUserAvatar);

        showUserInformation();

        btnDangXuat = v.findViewById(R.id.btnDangXuat);
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getContext(), Login.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        btnDoiAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });


        btnThayDoiTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                name =  edtUserName.getText().toString().trim();

                progressDialog.show();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Update name success", Toast.LENGTH_SHORT).show();
                                    showUserInformation();
                                }
                            }
                        });
            }
        });


        tvChangePassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangePassActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

    private void onClickRequestPermission() {
        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity == null)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mainActivity.openGalary();
            return;
        }
        mainActivity.openGalary();
    }

    public void setBitMapImageView(Bitmap bitmap) {
        ivUserAvatar.setImageBitmap(bitmap);
    }

    public void uploadFireBase(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            return;

        progressDialog.show();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(name)
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Update avatar success", Toast.LENGTH_SHORT).show();
                            showUserInformation();
                        }
                    }
                });

    }


    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        } else {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            edtUserName.setText(name);
            tvEmail.setText(email);
            Glide.with(this).load(photoUrl).error(R.drawable.ic_default_avatar).into(ivUserAvatar);

        }

    }

}