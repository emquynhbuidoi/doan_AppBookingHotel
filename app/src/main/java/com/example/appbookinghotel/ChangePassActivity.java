package com.example.appbookinghotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    private Button btnChangePass;
    private EditText edtMkCu, edtMkMoi, edtMkMoi2;
    private ProgressDialog progressDialog;
    private String mkcu, mkmoi, mkmoi2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        progressDialog = new ProgressDialog(ChangePassActivity.this);

        edtMkCu = findViewById(R.id.edtMkCu);
        edtMkMoi = findViewById(R.id.edtMkMoi);
        edtMkMoi2 = findViewById(R.id.edtMkMoi2);

        btnChangePass = findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mkcu = edtMkCu.getText().toString().trim();
                mkmoi = edtMkMoi.getText().toString().trim();
                mkmoi2 = edtMkMoi2.getText().toString().trim();

                if (!mkmoi2.equals(mkmoi)) {
                    Toast.makeText(ChangePassActivity.this, "Mk mới không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    reAuthenticate();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void onClickChangePass() {

        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(mkmoi)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassActivity.this, "User password update", Toast.LENGTH_SHORT).show();
                        }
//                            else{
//                                // neu da dang nhap 1 thoi gian lau
//                                reAuthenticate();
//                            }
                    }
                });

    }


    //Xac thuc lai nguoi dung, neu muon doi mk
    private void reAuthenticate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), mkcu);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onClickChangePass();
                        } else {
                            Toast.makeText(ChangePassActivity.this, "MK cũ không đúng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}