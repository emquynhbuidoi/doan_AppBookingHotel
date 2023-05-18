package com.example.appbookinghotel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;

public class Register extends AppCompatActivity {
    Button mbtnRegister;
    TextInputEditText mtieEmailDK2, mtiePassDK2, mtieRePassDK2, mtieTenDK2, mtieSdtDK2;
    TextInputLayout mtieEmlDK, mtiePsDK, mtieRPsDK, mtieTnDK, mtiesdtDK;
    CheckBox mchkHienMK;
    String email, pass, repass, ten, sdt;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        mchkHienMK = findViewById(R.id.chkHienMK);
        mchkHienMK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mtiePassDK2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mtieRePassDK2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mtiePassDK2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mtieRePassDK2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mbtnRegister = (Button) findViewById(R.id.btnRegister);
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mtieEmailDK2.getText().toString().trim();
                pass = mtiePassDK2.getText().toString().trim();
                repass = mtieRePassDK2.getText().toString().trim();
                ten = mtieTenDK2.getText().toString();
                sdt = mtieSdtDK2.getText().toString();
                int check = 0;
                if (ten.equals("")) {
                    mtieTenDK2.setError("Not null");
                    mtieTnDK.setError(" ");
                } else {
                    check++;
                    mtieTnDK.setError(null);
                }
                if (email.equals("")) {
                    mtieEmailDK2.setError("Not null");
                    mtieEmlDK.setError(" ");
                } else {
                    check++;
                    mtieEmlDK.setError(null);
                }
                if (pass.equals("") || pass.length() < 6) {
                    mtiePassDK2.setError("MK phai co 6 so");
                    mtiePsDK.setError(" ");
                } else {
                    check++;
                    mtiePsDK.setError(null);
                }
                if (pass.equals("")) {
                    mtieRePassDK2.setError("Not null");
                    mtieRPsDK.setError(" ");
                } else {
                    check++;
                    mtieRPsDK.setError(null);
                }
                if (sdt.length() != 10) {
                    mtieSdtDK2.setError("Vui lòng nhập đủ số điện thoại");
                    mtiesdtDK.setError(" ");
                    mtieSdtDK2.setText("");
                } else {
                    check++;
                    mtiesdtDK.setError(null);
                }
                if (!pass.equals(repass)) {
                    mtieRePassDK2.setError("Vui lòng nhập lại mật khẩu");
                    mtieRPsDK.setError(" ");
                    mtieRePassDK2.setText("");
                } else {
                    check++;
                    mtieRPsDK.setError(null);
                }

                if (check == 6) {
                    onClickRegister();

                }
            }
        });


    }


    private void init() {
        mtieEmlDK = findViewById(R.id.tieEmailDK);
        mtiePsDK = findViewById(R.id.tiePassDK);
        mtieRPsDK = findViewById(R.id.tieRePassDK);
        mtieTnDK = findViewById(R.id.tieTenDK);
        mtiesdtDK = findViewById(R.id.tieSdtDK);
        mtieEmailDK2 = findViewById(R.id.tieEmailDK2);
        mtiePassDK2 = findViewById(R.id.tiePassDK2);
        mtieRePassDK2 = findViewById(R.id.tieRePassDK2);
        mtieTenDK2 = findViewById(R.id.tieTenDK2);
        mtieSdtDK2 = findViewById(R.id.tieSdtDK2);

        progressDialog = new ProgressDialog(Register.this);

    }

    private void onClickRegister() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(ten)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent i = new Intent(Register.this, Login.class);
                                    startActivity(i);
                                    finishAffinity();
                                }
                            });
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(Register.this, "Email đã có, hoặc lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
