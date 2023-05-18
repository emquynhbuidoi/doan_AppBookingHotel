package com.example.appbookinghotel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static AHBottomNavigation bottomNavigation;

    FragmentcaNhan caiDatFraggment = new FragmentcaNhan();
    FragmentYeuThich fragmentYeuThichFragment = new FragmentYeuThich();
    FragmentNoiLuuTru noiLuuTruFragment = new FragmentNoiLuuTru();
    FragmentDaDat fragmentDaDatFragment = new FragmentDaDat();


    AHBottomNavigationItem item1;
    AHBottomNavigationItem item2;
    AHBottomNavigationItem item3;
    AHBottomNavigationItem item4;


    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    public static ArrayList<Order> dsOrder;
    public static ArrayList<Hotel> dsHotel;
    public static ArrayList<Room> dsRoom;
    private FirebaseUser user;

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) {
                            return;
                        }
                        Uri uri = intent.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            caiDatFraggment.setBitMapImageView(bitmap);
                            caiDatFraggment.uploadFireBase(uri);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        bottomNavigation = findViewById(R.id.bnvDieuHuong);
        progressDialog = new ProgressDialog(MainActivity.this);
        dsOrder = new ArrayList<>();
        dsRoom = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        readDataHotels();
        readDataRooms();
        readDataOrders();

    }


    private void readDataRooms() {
        db.collection("Rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Room room = new Room(document.getString("id"),
                                        document.getString("roomname"),
                                        document.getString("price"),
                                        document.getString("rate"),
                                        document.getString("imageUrl"),
                                        document.getString("hotelID"),
                                        document.getString("location"));
                                dsRoom.add(room);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi kết nối ds rooms!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void readDataHotels() {
        progressDialog.setTitle("Đang tải...");
        progressDialog.show();
        dsHotel = Hotel.getDataFromFireStore();
    }

    private void readDataOrders() {
        db.collection("Orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("emailuser").equals(user.getEmail())) {
                                    Order order = new Order(document.getString("id"),
                                            document.getString("emailuser"), document.getString("soluongphongvakhach"),
                                            document.getString("thoigiannhan"), document.getString("thoigiantra"),
                                            document.getString("roomname"), document.getString("hotelname"),
                                            document.getString("price"), document.getString("loaction"));
                                    dsOrder.add(order);
                                }
                            }
                            ahBottomNavbar();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Lỗi kết nối dsOrder!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void ahBottomNavbar() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, noiLuuTruFragment).commit();
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.noiluutru, R.drawable.bed, R.color.yellow);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.yeuthich, R.drawable.favorite, R.color.blue);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.dadat, R.drawable.ic_reserved, R.color.green);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.canhan, R.drawable.ic_person, R.color.red);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        bottomNavigation.setColored(false);
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.blue));
        bottomNavigation.setAccentColor(getResources().getColor(R.color.yellow));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.white));

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE_FORCE);


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                switch (position) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, noiLuuTruFragment).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentYeuThichFragment).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentDaDatFragment).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, caiDatFraggment).commit();
                        break;
                }

                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
            }
        });
    }

    public void openGalary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

}