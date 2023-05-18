package com.example.appbookinghotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextAcivity();
            }
        }, 1000);
    }
    private void nextAcivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            //chua login
            Intent i = new Intent(SplashActivity.this, Login.class);
            startActivity(i);
        }
        else{
            // da login
            if(user.getEmail().equals("admin@gmail.com")){
                Intent i = new Intent(SplashActivity.this, MainActivityAdmin.class);
                startActivity(i);
            }
            else{
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }
        }
        finish();
    }
}