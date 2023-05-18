package com.example.appbookinghotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowSearchListActivity extends AppCompatActivity implements HotelAdapter.LangNghe {
    EditText edtSearchShowList;
    String locationDaTimKiem;
    ImageView icSearch;
    RecyclerView rvShowSearchList;
    ArrayList<Hotel> dsHotels;
    HotelAdapter adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search_list);

        Intent intent = getIntent();
        Hotel hotel = (Hotel) intent.getSerializableExtra("location");
        dsHotels = (ArrayList<Hotel>) intent.getSerializableExtra("dsHotels");
        locationDaTimKiem = hotel.getLocation();

        edtSearchShowList = findViewById(R.id.edtSearchShowList);
        edtSearchShowList.setText(locationDaTimKiem);

        edtSearchShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("locationTraVe", locationDaTimKiem);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        icSearch = findViewById(R.id.icSearch);
        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("locationTraVe", locationDaTimKiem);
                setResult(RESULT_OK, i);
                finish();
            }
        });


        setAdapter_RecyclerView();
    }

    private void setAdapter_RecyclerView() {
        rvShowSearchList = findViewById(R.id.rvShowSearchList);
        adap = new HotelAdapter(ShowSearchListActivity.this);

        ArrayList<Hotel> dsThoaMan = new ArrayList<>();
        for (Hotel row : dsHotels) {
            if (row.getLocation().toLowerCase().equals(locationDaTimKiem.toLowerCase())) {
                dsThoaMan.add(row);
            }
        }
        adap.setData(dsThoaMan);

        rvShowSearchList.setLayoutManager(new LinearLayoutManager(ShowSearchListActivity.this, LinearLayoutManager.VERTICAL, false));

        rvShowSearchList.setAdapter(adap);

    }


    @Override
    public void setOnClickItem(Hotel ht) {
        //lam them hotel detail adap ter
        Intent intent = new Intent(ShowSearchListActivity.this, ShowListRoomFromHotelUser.class);
        intent.putExtra("hotelDangXet", ht);
        startActivity(intent);
    }
}