package com.example.appbookinghotel;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import java.util.ArrayList;


public class FragmentNoiLuuTru extends Fragment implements LocationAdapter.onClick, RoomAdapter.LangNgheRoom {

    Button btnTim;
    private RecyclerView rvTimGanDay;
    private LocationAdapter hotelAdapter;
    private ArrayList<Hotel> dsHotelTimKiemGanDay;
    private RecyclerView rvNoiXemGanDay;
    private ArrayList<Room> dsRoomNoiBanXemGanDay;
    private RoomAdapter roomAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_noi_luu_tru, container, false);

        init(v);
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        set();
    }

    private void set(){
        dsHotelTimKiemGanDay = SearchActivity.dsHoteDaTimKiem;
        hotelAdapter = new LocationAdapter(FragmentNoiLuuTru.this);
        hotelAdapter.setData(dsHotelTimKiemGanDay);
        rvTimGanDay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvTimGanDay.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvTimGanDay.setAdapter(hotelAdapter);

        dsRoomNoiBanXemGanDay = ShowListRoomFromHotelUser.dsRoomDaXem;
        roomAdapter = new RoomAdapter(FragmentNoiLuuTru.this);
        roomAdapter.setData(dsRoomNoiBanXemGanDay);
        rvNoiXemGanDay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNoiXemGanDay.setAdapter(roomAdapter);

    }
    private void init(View v) {
        rvTimGanDay = v.findViewById(R.id.rvTimGanDay);
        rvNoiXemGanDay = v.findViewById(R.id.rvNoiXemGanDay);
        btnTim = v.findViewById(R.id.btnTim);
        dsRoomNoiBanXemGanDay = new ArrayList<>();
        //set Thong Bao
        AHNotification notification = new AHNotification.Builder()
                .setText(MainActivity.dsOrder.size() + "")
                .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                .setTextColor(ContextCompat.getColor(getContext(), R.color.white))
                .build();
        MainActivity.bottomNavigation.setNotification(notification, 2);
        MainActivity.bottomNavigation.setTranslucentNavigationEnabled(true);
    }

    @Override
    public void nhanVaoLocation(Hotel hotelLocation) {
        Intent intent = new Intent(getContext(), ShowSearchListActivity.class);
        intent.putExtra("location", hotelLocation);
        intent.putExtra("dsHotels", MainActivity.dsHotel);
//        ActivityLaunch.launch(intent);
        startActivity(intent);
    }

    @Override
    public void OnNhanLau(Room room) {

    }

    @Override
    public void OnClick(Room room) {
        Intent intent = new Intent(getContext(), DatNgay.class);
        intent.putExtra("roomDangXet", room);
        intent.putExtra("hotelDangXet", DatNgay.hotelDangXet);
        startActivity(intent);
    }
}