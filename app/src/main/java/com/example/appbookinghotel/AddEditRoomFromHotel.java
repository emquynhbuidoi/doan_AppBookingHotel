package com.example.appbookinghotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AddEditRoomFromHotel extends AppCompatActivity {
    EditText edtRoomName, edtPrice;
    Button btnAddRoom;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    String hotelID;
    int check;
    Room updateRoom;

    Button btnPickRoomImg;
    ImageView ivRoom;
    private static final int PICK_CODE = 1;
    private Uri imageUriRoom;
    int check_pickPhoto = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room_from_hotel);
        progressDialog = new ProgressDialog(AddEditRoomFromHotel.this);

        edtRoomName = findViewById(R.id.edtRoomName);
        edtPrice = findViewById(R.id.edtLocation);
        btnAddRoom = findViewById(R.id.btnAddRoom);

        btnPickRoomImg = findViewById(R.id.btnPickRoomImg);
        ivRoom = findViewById(R.id.ivRoom);
        btnPickRoomImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });


        Intent i = getIntent();
        check = i.getIntExtra("check", 0);

        if (check == 1) {
            hotelID = i.getStringExtra("hotelID");
            getSupportActionBar().setTitle("Thêm Phòng");
            btnAddRoom.setText("Thêm");

        } else if (check == 2) {
            updateRoom = (Room) i.getSerializableExtra("roomUpdate");
            getSupportActionBar().setTitle("Thay đổi " + updateRoom.getRoomename());
            edtRoomName.setText(updateRoom.getRoomename());
            edtPrice.setText(updateRoom.getPrice());
            Picasso.get()
                    .load(updateRoom.getImageUri())
                    .placeholder(R.drawable.bed)
                    .fit()
                    .centerCrop()
                    .into(ivRoom);

            btnAddRoom.setText("Sửa");
        }

        db = FirebaseFirestore.getInstance();
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomname = edtRoomName.getText().toString().trim();
                String price = edtPrice.getText().toString().trim();
                String rate = "0";
                Intent intent = new Intent();

                if (check == 1) {
                    if(check_pickPhoto == 0){
                        Room room = new Room("0", roomname, price, rate, "0", hotelID, "0");
                        intent.putExtra("room", room);
                        intent.putExtra("check", 1);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else{
                        Room room = new Room("0", roomname, price, rate, imageUriRoom.toString(), hotelID, "0");
                        intent.putExtra("room", room);
                        intent.putExtra("check", 1);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else if (check == 2) {
                    if(check_pickPhoto == 0){
                        Room room = new Room(updateRoom.getId(), roomname, price, rate, updateRoom.getImageUri(), updateRoom.getHotelID(), "0");
                        intent.putExtra("room", room);
                        intent.putExtra("check", 2);
                        intent.putExtra("check_pickPhoto", 0);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else{
                        //Xoá ảnh cũ trong Storage khi update lại ảnh mới
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Image/Rooms");
                        StorageReference imageRef = storageReference.getStorage().getReferenceFromUrl(updateRoom.getImageUri());
                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddEditRoomFromHotel.this, "!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Room room = new Room(updateRoom.getId(), roomname, price, rate, imageUriRoom.toString(), updateRoom.getHotelID(), "0");
                        intent.putExtra("room", room);
                        intent.putExtra("check", 2);
                        intent.putExtra("check_pickPhoto", 1);

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_CODE
                && data != null && data.getData() != null) {
            imageUriRoom = data.getData();
            Picasso.get().load(imageUriRoom).into(ivRoom);
            check_pickPhoto = 1;

        }
    }




    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}