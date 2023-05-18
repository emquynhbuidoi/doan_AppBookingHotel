package com.example.appbookinghotel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentThemKhachSan extends Fragment implements HotelAdapter.LangNghe{
    ArrayList<Hotel> dshotel;
    RecyclerView rv;
    HotelAdapter hotelAdapter;
    ProgressDialog progressDialog;
    FirebaseFirestore db;

    FloatingActionButton fabAddHotel;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_them_khach_san_admin, container, false);
        progressDialog = new ProgressDialog(getContext());

        fabAddHotel = v.findViewById(R.id.fabAddRoom);
        fabAddHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddHotel.class));
            }
        });

        setAdapter_RecyclerView(v);

        return v;
    }

    private void setAdapter_RecyclerView(View view) {
        rv = view.findViewById(R.id.rvShowListRoomAdmin);
        hotelAdapter = new HotelAdapter(FragmentThemKhachSan.this);

        readData();

        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(hotelAdapter);
    }

    private void readData(){
        progressDialog.setTitle("Đang tải...");
        progressDialog.show();
        dshotel = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("Hotels")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String star = document.getString("star");
                                if(star.equals("")){
                                    star = "0";
                                }
                                Hotel hotel = new Hotel(document.getString("id"),
                                        document.getString("hotelname"),
                                        Integer.parseInt(star),
                                        document.getString("location"),
                                        document.getString("latlng"),
                                        document.getString("imageUrl"),
                                        document.getString("description"));
                                dshotel.add(hotel);

                            }
                            hotelAdapter.setData(dshotel);
                        } else {
                            Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void setOnClickItem(Hotel ht) {
        Intent intent = new Intent(getContext(), ShowListRoomFromHotelAdmin.class);
        intent.putExtra("hotel", ht);
        startActivity(intent);
    }
}