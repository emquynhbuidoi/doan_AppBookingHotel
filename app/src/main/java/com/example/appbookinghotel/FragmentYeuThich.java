package com.example.appbookinghotel;

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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentYeuThich extends Fragment implements RoomAdapter.LangNgheRoom {
    private ProgressDialog progressDialog;
    private RoomAdapter roomAdapter;
    private RecyclerView rvYeuThich;

    private ArrayList<Room> dsRoomYeuThich;
    private ArrayList<YeuThich> dsYeuThich;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_yeu_thich, container, false);
        Init(v);



        return v;
    }

    private void Init(View view) {
        progressDialog = new ProgressDialog(getContext());
        rvYeuThich = view.findViewById(R.id.rvYeuThich);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        dsRoomYeuThich = new ArrayList<>();
        dsYeuThich = new ArrayList<>();
        readDatadsYeuThichVaRoomYeuThich();
    }


    private void readDatadsYeuThichVaRoomYeuThich() {
        progressDialog.setTitle("Đang tải...");
        progressDialog.show();

        db.collection("dsYeuThich")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("emailuser").equals(user.getEmail())){
                                    YeuThich yeuThich = new YeuThich(document.getString("id"),
                                            document.getString("emailuser"));
                                    dsYeuThich.add(yeuThich);
                                }
                            }
                            //Lấy dsRoomYeuThich từ dsYeuThich RoomID
                            ArrayList<Room> dsAllRoom = MainActivity.dsRoom;
                            for(int i = 0; i < dsYeuThich.size(); ++i){
                                for(int j = 0; j < dsAllRoom.size(); ++j){
                                    if(dsYeuThich.get(i).getID().equals(dsAllRoom.get(j).getId())){
                                        dsRoomYeuThich.add(dsAllRoom.get(j));
                                    }
                                }
                            }
                            setRV();
//                            Toast.makeText(getContext(), "DSYEUTHICH: " + dsYeuThich.size(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Lỗi kết nối dsYeuThich!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setRV() {
        roomAdapter = new RoomAdapter(FragmentYeuThich.this);
        roomAdapter.setData(dsRoomYeuThich);
        rvYeuThich.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvYeuThich.setAdapter(roomAdapter);
    }

    @Override
    public void OnNhanLau(Room room) {

    }

    @Override
    public void OnClick(Room room) {
//        Toast.makeText(getContext(), "Click" + room.getRoomename(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), DatNgay.class);
        intent.putExtra("roomDangXet", room);
        ArrayList<Hotel> dsHotel = MainActivity.dsHotel;
        Hotel hotelDangXet = new Hotel();
        for(int i = 0; i <dsHotel.size(); ++i){
            if(dsHotel.get(i).getId().equals(room.getHotelID())){
                hotelDangXet = dsHotel.get(i);
            }
        }
        intent.putExtra("hotelDangXet", hotelDangXet);
        startActivity(intent);
    }
}