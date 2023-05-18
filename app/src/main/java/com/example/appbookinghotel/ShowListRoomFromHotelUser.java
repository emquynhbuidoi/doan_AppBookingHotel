package com.example.appbookinghotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowListRoomFromHotelUser extends AppCompatActivity implements RoomAdapter.LangNgheRoom {
    private ArrayList<Room> dsRoom;
    private RoomAdapter roomAdapter;
    private ProgressDialog progressDialog;
    private Hotel hotelDangXet;
    private RecyclerView rvShowListRoomUser;
    private TextView tvEmpty;

    public static ArrayList<Room> dsRoomDaXem;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_room_from_hotel_user);

        dsRoomDaXem = new ArrayList<>();
        tvEmpty = findViewById(R.id.tvEmpty);
        progressDialog = new ProgressDialog(ShowListRoomFromHotelUser.this);
        Intent intent = getIntent();
        hotelDangXet = (Hotel) intent.getSerializableExtra("hotelDangXet");

        getSupportActionBar().setTitle(hotelDangXet.getName());

        setAdapter_RecyclerView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setAdapter_RecyclerView() {
        rvShowListRoomUser = findViewById(R.id.rvShowListRoomUser);
        roomAdapter = new RoomAdapter(ShowListRoomFromHotelUser.this);

        readData();

        rvShowListRoomUser.setLayoutManager(new LinearLayoutManager(ShowListRoomFromHotelUser.this, LinearLayoutManager.VERTICAL, false));
        rvShowListRoomUser.setAdapter(roomAdapter);
    }


    private void readData() {
        progressDialog.setTitle("Đang tải...");
        progressDialog.show();

        dsRoom = new ArrayList<>();

        ArrayList<Room> dsAllRoom = MainActivity.dsRoom;
        for(int i = 0; i < dsAllRoom.size(); i++){
            if(dsAllRoom.get(i).getHotelID().equals(hotelDangXet.getId())){
//                dsAllRoom.get(i).setLocation(hotelDangXet.getLocation());
                dsRoom.add(dsAllRoom.get(i));
            }
        }
        if(dsRoom.size() != 0){
            progressDialog.dismiss();
            tvEmpty.setVisibility(View.GONE);
            roomAdapter.setData(dsRoom);
        }
        else{
            progressDialog.dismiss();
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnNhanLau(Room room) {

    }

    @Override
    public void OnClick(Room room) {
        dsRoomDaXem.add(room);
//        Toast.makeText(ShowListRoomFromHotelUser.this, "Room: " + dsRoomDaXem.size(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ShowListRoomFromHotelUser.this, DatNgay.class);
        intent.putExtra("roomDangXet", room);
        intent.putExtra("hotelDangXet", hotelDangXet);
        startActivity(intent);
    }
}