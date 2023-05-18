package com.example.appbookinghotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddHotel extends AppCompatActivity {
    private EditText edtHotelDescription;
    private EditText edtHotelName;
    private EditText edtHotelLocation;
    private EditText edtsao, edtLat, edtLong;
    private Button btnAddHotel;
    private ProgressDialog progressDialog;

    private Button btnPickHotelImg;
    private ImageView ivHotel;
    private Uri imageUri;
    private static final int PICK_CODE = 1;
    private String linkAnhTrongStorage = "";

    private StorageReference storageReference;
    private StorageTask UploadTask;

    private FirebaseFirestore db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);
        getSupportActionBar().setTitle("Thêm Khách Sạn Mới");

        Init();
        btnPickHotelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

        btnAddHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UploadTask != null && UploadTask.isInProgress()) {
                    Toast.makeText(AddHotel.this, "Đang cập nhật dữ liệu!", Toast.LENGTH_SHORT).show();
                } else {
                    String hotelname = edtHotelName.getText().toString().trim();
                    String location = edtHotelLocation.getText().toString().trim();
                    String description = edtHotelDescription.getText().toString().trim();
                    String sao = edtsao.getText().toString().trim();
                    String latlng = edtLat.getText().toString().trim() + " " + edtLong.getText().toString().trim();
                    upLoadData(hotelname, location, latlng, description, sao);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Init(){
        edtLong = findViewById(R.id.edtLong);
        edtLat = findViewById(R.id.edtLat);

        progressDialog = new ProgressDialog(AddHotel.this);
        edtHotelDescription = findViewById(R.id.edtHotelDescription);
        edtHotelName = findViewById(R.id.edtRoomName);
        edtHotelLocation = findViewById(R.id.edtLocation);
        edtsao = findViewById(R.id.edtSao);

        storageReference = FirebaseStorage.getInstance().getReference("Image/Hotels");
        ivHotel = findViewById(R.id.ivRoom);
        btnPickHotelImg = findViewById(R.id.btnPickRoomImg);
        db = FirebaseFirestore.getInstance();
        btnAddHotel = findViewById(R.id.btnAddRoom);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
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
            imageUri = data.getData();

            //            ivHotel.setImageURI(imageUri);
            Picasso.get().load(imageUri).into(ivHotel);
        }
    }

    private void upLoadData(String hotelname, String location, String latlng, String description, String sao) {
        progressDialog.setTitle("Đang thêm dữ liệu..");
        progressDialog.show();


        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            UploadTask = fileReference.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<com.google.firebase.storage.UploadTask.TaskSnapshot> task) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.setProgress(0);
                                        }
                                    }, 2000);

                                    // Lấy đường dẫn để lưu ảnh từ Storage vào Firestore -> giúp tải ảnh để hiển thị trên di động
                                    linkAnhTrongStorage = uri.toString();
                                    upLoadFireStore(hotelname, location, latlng,description, sao);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddHotel.this, "Lỗi cập nhật ảnh", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setProgress((int) progress);
                        }
                    });
        }
    }

    private void upLoadFireStore(String hotelname, String location, String latlng,String description, String sao) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> hotel = new HashMap<>();
        hotel.put("id", id);
        hotel.put("hotelname", hotelname);
        hotel.put("location", location);
        hotel.put("latlng", latlng);
        hotel.put("description", description);
        hotel.put("star", sao);
        hotel.put("imageUrl", linkAnhTrongStorage);

        // Add a new document with a generated ID
        db.collection("Hotels").document(id).set(hotel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        edtHotelDescription.setText("");
                        edtHotelLocation.setText("");
                        edtLat.setText("");
                        edtLong.setText("");
                        edtHotelName.setText("");
                        edtsao.setText("");
                        ivHotel.setImageResource(R.drawable.ic_default_hotel);
                        Toast.makeText(AddHotel.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddHotel.this, "Lỗi Đường Truyền Khi Thêm", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}