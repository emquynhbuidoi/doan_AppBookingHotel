package com.example.appbookinghotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMap extends AppCompatActivity implements OnMapReadyCallback {
    Hotel hotelDangXet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        hotelDangXet = (Hotel) i.getSerializableExtra("hotel");

        getSupportActionBar().setTitle("Vị trí: " + hotelDangXet.getName());

    }

    @Override
    public void onMapReady(@NonNull com.google.android.gms.maps.GoogleMap googleMap) {
        LatLng latLng;
        if(!hotelDangXet.getLatlng().equals(" ")){
            String[] toado = hotelDangXet.getLatlng().split(" ");
            latLng = new LatLng(Double.parseDouble(toado[0]), Double.parseDouble(toado[1]));
        }
        else{
            Toast.makeText(GoogleMap.this, "duoi:" + hotelDangXet.getLatlng(), Toast.LENGTH_SHORT).show();
            latLng = new LatLng(16.47694,107.5739587);
        }
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(hotelDangXet.getLocation())
                .snippet(hotelDangXet.getName())
        );
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

    }
}