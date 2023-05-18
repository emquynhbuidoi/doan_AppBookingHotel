package com.example.appbookinghotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivityAdmin extends AppCompatActivity  {


    FragmentXemHoaDonAdmin fragmentXemHoaDon = new FragmentXemHoaDonAdmin();
    FragmentThemKhachSan fragmentThemKhachSan = new FragmentThemKhachSan();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerAdmin, fragmentXemHoaDon).commit();


        getSupportActionBar().setTitle(R.string.admin);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_themKhachSan:
                getSupportFragmentManager().beginTransaction().replace(R.id.containerAdmin, fragmentThemKhachSan).commit();
                break;
            case R.id.item_xemHoaDon:
                getSupportFragmentManager().beginTransaction().replace(R.id.containerAdmin, fragmentXemHoaDon).commit();
                break;
            case R.id.item_dangxuat:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivityAdmin.this, Login.class);
                startActivity(i);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}