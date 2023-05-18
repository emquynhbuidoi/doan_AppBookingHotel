package com.example.appbookinghotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {
    private TextInputEditText tieEmail, tiePass;
    private TextInputLayout tilEmail, tilPass;
    private Button mbtnDANGKY;
    private ProgressDialog progressDialog;
    private String email, pass;
    private TextView tvForgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);

        mbtnDANGKY = findViewById(R.id.btnDANGKY);
        tieEmail = findViewById(R.id.tieEmail);
        tiePass = findViewById(R.id.tiePass);
        tilEmail = findViewById(R.id.tilEmal);
        tilPass = findViewById(R.id.tilPass);

        tvForgotPass = findViewById(R.id.tvForgotPass);
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassWordActivity.class);
                startActivity(intent);
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = 0;
                email = tieEmail.getText().toString().trim();
                pass = tiePass.getText().toString().trim();
                if (email.equals("")) {
                    tieEmail.setError("Not null");
                    tilEmail.setError(" ");
                }
                else{
                    tieEmail.setError(null);
                    tilEmail.setError(null);
                    check++;
                }
                if (pass.equals("")) {
                    tiePass.setError("Not null");
                    tilPass.setError(" ");
                }
                else{
                    tieEmail.setError(null);
                    tilPass.setError(null);
                    check++;
                }

                if(check == 2){
                    onClickLogion();
                }

            }
        });


        mbtnDANGKY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iDangKy = new Intent(Login.this, Register.class);
                startActivity(iDangKy);
            }
        });
    }

    private void onClickLogion() {
        progressDialog.show();
        progressDialog.setTitle("Đăng nhập...");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            if(email.equals("admin@gmail.com")){
                                Intent i = new Intent(Login.this, MainActivityAdmin.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Intent i = new Intent(Login.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Sai thông tin, hoặc lỗi mạng.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}