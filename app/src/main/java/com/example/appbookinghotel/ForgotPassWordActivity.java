package com.example.appbookinghotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassWordActivity extends AppCompatActivity {
    private EditText edtEmailCu;
    private Button btnXacNhan;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_word);
        progressDialog = new ProgressDialog(ForgotPassWordActivity.this);
        getSupportActionBar().setTitle(R.string.quenmatkhau);

        edtEmailCu = findViewById(R.id.edtEmailCu);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEmailCu.getText().toString().equals("")){
                    edtEmailCu.setError("Not null");
                }
                else{
                    onClickXacNhan();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void onClickXacNhan() {
        progressDialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // !! email đăng nhập phải là email thực
        String emailAddress = edtEmailCu.getText().toString().trim();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassWordActivity.this, "Kiểm tra Email để xác thực", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ForgotPassWordActivity.this, "Email chưa đăng ký", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}