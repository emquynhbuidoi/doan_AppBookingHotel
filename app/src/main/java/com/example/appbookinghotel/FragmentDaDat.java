package com.example.appbookinghotel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentDaDat extends Fragment implements OrderAdapter.LangNghe {
    private OrderAdapter orderAdapter;
    private RecyclerView rv;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_da_dat, container, false);


        init(view);
        return view;
    }

    private void init(View view) {
        rv = view.findViewById(R.id.rvOrder);
        orderAdapter = new OrderAdapter(FragmentDaDat.this);
        orderAdapter.setData(MainActivity.dsOrder);

        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(orderAdapter);
    }



    @Override
    public void setOnClickItem(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Hoá Đơn");
        builder.setIcon(R.drawable.ic_mony);
        builder.setMessage("Khách sạn " + order.getHotelname() +"\n"+ order.getRoomname()+ " Price: " + order.getPrice() + "VNĐ");
        builder.create().show();
//        Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
    }
}