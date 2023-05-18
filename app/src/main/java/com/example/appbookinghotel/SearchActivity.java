package com.example.appbookinghotel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements LocationAdapter.onClick {
    private RecyclerView rvDsLocation;
    private LocationAdapter adapter;
    private TextView tv;
    private String locationTimKiem = null;
    private SearchView searchView;
    public static ArrayList<Hotel> dsHoteDaTimKiem;

    ActivityResultLauncher<Intent> ActivityLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        locationTimKiem = result.getData().getStringExtra("locationTraVe");
                        searchView.setQuery(locationTimKiem, false);
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dsHoteDaTimKiem = new ArrayList<>();

        tv = findViewById(R.id.tv);

        setAdapter_RecyclerView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setAdapter_RecyclerView() {
        rvDsLocation = findViewById(R.id.rvSearch);
        adapter = new LocationAdapter(SearchActivity.this);
        adapter.setData(MainActivity.dsHotel);
        rvDsLocation.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
        rvDsLocation.addItemDecoration(new DividerItemDecoration(SearchActivity.this, DividerItemDecoration.VERTICAL));
        rvDsLocation.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.mnnuSearch));
        searchView.setIconified(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText.trim());
                if (adapter.getItemCount() > 0) {
                    tv.setVisibility(View.GONE);
                } else {
                    tv.setVisibility(View.VISIBLE);
                }
                if (newText.isEmpty()) {
                    tv.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void nhanVaoLocation(Hotel hotelLocation) {
        dsHoteDaTimKiem.add(hotelLocation);

        Intent intent = new Intent(SearchActivity.this, ShowSearchListActivity.class);
        intent.putExtra("location", hotelLocation);
        intent.putExtra("dsHotels", MainActivity.dsHotel);
        ActivityLaunch.launch(intent);

    }


}