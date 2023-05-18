package com.example.appbookinghotel;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class Hotel implements Serializable {
    String id;
    String name;
    int sao;
    String location;
    String latlng;
    String imageUrl;
    String description;

    public Hotel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSao() {
        return sao;
    }

    public void setSao(int sao) {
        this.sao = sao;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Hotel(String id, String name, int sao, String location, String latlng, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.sao = sao;
        this.location = location;
        this.latlng = latlng;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static ArrayList<Hotel> getDataFromFireStore(){
        ArrayList<Hotel> dshotel = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Hotels")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                        } else {
//                            Toast.makeText(MainActivityAdmin.this, "Show Data Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return  dshotel;
    }
}
