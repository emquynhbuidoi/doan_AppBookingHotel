package com.example.appbookinghotel;

import android.app.ProgressDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentXemHoaDonAdmin extends Fragment {
    private ArrayList<Order> dsOrder;
    private OrderAdapterAdmin orderAdapterAdmin;
    private ProgressDialog progressDialog;
    private RecyclerView rvXemHoaDonAdmin;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xem_hoa_don_admin, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        rvXemHoaDonAdmin = view.findViewById(R.id.rvThongTinKhachHangAdmin);
        progressDialog = new ProgressDialog(getContext());
        dsOrder = new ArrayList<>();
        orderAdapterAdmin = new OrderAdapterAdmin();

        readData();

        rvXemHoaDonAdmin.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvXemHoaDonAdmin.setAdapter(orderAdapterAdmin);

    }

    private void readData() {
        progressDialog.setTitle("Đang tải...");
        progressDialog.show();


        db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Order order = new Order(document.getString("id"),
                                        document.getString("emailuser"), document.getString("soluongphongvakhach"),
                                        document.getString("thoigiannhan"), document.getString("thoigiantra"),
                                        document.getString("roomname"), document.getString("hotelname"),
                                        document.getString("price") ,document.getString("loaction"));
                                dsOrder.add(order);
                            }
//                            Toast.makeText(getContext(), dsOrder.size() + "", Toast.LENGTH_SHORT).show();
                            orderAdapterAdmin.setData(dsOrder);

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}