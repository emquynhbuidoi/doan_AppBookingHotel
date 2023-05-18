package com.example.appbookinghotel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShowListRoomFromHotelAdmin extends AppCompatActivity implements RoomAdapter.LangNgheRoom {
    ArrayList<Room> dsRoom;
    RecyclerView rv;
    RoomAdapter roomAdapter;
    private ProgressDialog progressDialog;
    FirebaseFirestore db;
    FloatingActionButton fabAddRoom;
    Hotel hotelDangXet;


    private StorageReference storageReference;
    private String linkAnhTrongStorage = "0";


    ActivityResultLauncher<Intent> ActivityLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData().getIntExtra("check", 0) == 1) {
                    //add
                    Room upLoadRoom = (Room) result.getData().getSerializableExtra("room");
                    if (!upLoadRoom.getImageUri().equals("0")) {
                        upLoadStorage_Update(Uri.parse(upLoadRoom.getImageUri()), "0", upLoadRoom.getRoomename(), upLoadRoom.getPrice(), upLoadRoom.getHotelID());
                    } else {
                        upLoadData(upLoadRoom.getRoomename(), upLoadRoom.getPrice(), upLoadRoom.getHotelID());
                    }
                } else if (result.getData().getIntExtra("check", 0) == 2) {
                    //update
                    Room updateRoom = (Room) result.getData().getSerializableExtra("room");
                    if (!updateRoom.getImageUri().equals("0") && result.getData().getIntExtra("check_pickPhoto", 0) != 0) {
                        upLoadStorage_Update(Uri.parse(updateRoom.getImageUri()), updateRoom.getId(), updateRoom.getRoomename(), updateRoom.getPrice(), updateRoom.getHotelID());
                    } else {
                        linkAnhTrongStorage = updateRoom.getImageUri();
                        updateRoom(updateRoom.getId(), updateRoom.getRoomename(), updateRoom.getPrice());
                    }
                }
            }
        }
    });

    private void upLoadStorage_Update(Uri imageUri, String roomID, String roomname, String price, String hotelID) {
        progressDialog.setTitle("Đang cập nhật...");
        progressDialog.show();


        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
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
                            }, 5000);

                            // Lấy đường dẫn để lưu ảnh từ Storage vào Firestore -> giúp tải ảnh để hiển thị trên di động
                            linkAnhTrongStorage = uri.toString();
                            if (roomID.equals("0")) {
                                upLoadData(roomname, price, hotelID);
                            } else {
                                updateRoom(roomID, roomname, price);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ShowListRoomFromHotelAdmin.this, "Lỗi cập nhật ảnh", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setProgress((int) progress);
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
    }

    private void upLoadData(String roomname, String price, String hotelID) {
        progressDialog.setTitle("Đang thêm phòng mới..");
        progressDialog.show();

        Map<String, Object> room = new HashMap<>();
        String id = UUID.randomUUID().toString();
        room.put("id", id);
        room.put("roomname", roomname);
        room.put("price", price);
        room.put("rate", "0");
        room.put("hotelID", hotelID);
        room.put("location", hotelDangXet.getLocation());
        room.put("imageUrl", linkAnhTrongStorage);

        db.collection("Rooms").document(id).set(room)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(ShowListRoomFromHotelAdmin.this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ShowListRoomFromHotelAdmin.this, "Lỗi thêm phòng", Toast.LENGTH_SHORT).show();

                    }
                });

        dsRoom.clear();
        readData();
        dsRoom.addAll(dsRoom);
        roomAdapter.notifyDataSetChanged();
        linkAnhTrongStorage = "0";
    }


    private void updateRoom(String id, String roomname, String price) {
        progressDialog.setTitle("Đang cập nhật...");
        progressDialog.show();

        db.collection("Rooms").document(id).update("roomname", roomname, "price", price, "imageUrl", linkAnhTrongStorage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(ShowListRoomFromHotelAdmin.this, "Cập nhật xong", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ShowListRoomFromHotelAdmin.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();

            }
        });
        dsRoom.clear();
        readData();
        dsRoom.addAll(dsRoom);
        roomAdapter.notifyDataSetChanged();
        linkAnhTrongStorage = "0";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_room_from_hotel);
        storageReference = FirebaseStorage.getInstance().getReference("Image/Rooms");

        progressDialog = new ProgressDialog(ShowListRoomFromHotelAdmin.this);
        Intent intent = getIntent();
        hotelDangXet = (Hotel) intent.getSerializableExtra("hotel");
        getSupportActionBar().setTitle(hotelDangXet.getName());

        setAdapter_RecyclerView();


        fabAddRoom = findViewById(R.id.fabAddRoom);
        fabAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowListRoomFromHotelAdmin.this, AddEditRoomFromHotel.class);
                i.putExtra("check", 1);
                i.putExtra("hotelID", hotelDangXet.getId());
                ActivityLaunch.launch(i);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setAdapter_RecyclerView() {
        rv = findViewById(R.id.rvShowListRoomAdmin);
        roomAdapter = new RoomAdapter(ShowListRoomFromHotelAdmin.this);

        readData();

        rv.setLayoutManager(new LinearLayoutManager(ShowListRoomFromHotelAdmin.this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(roomAdapter);
    }

    private void readData() {
        progressDialog.setTitle("Tải dữ liệu...");
        progressDialog.show();

        dsRoom = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("Rooms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getString("hotelID").equals(hotelDangXet.getId())) {
                            Room room = new Room(document.getString("id"), document.getString("roomname"), document.getString("price"), document.getString("rate"), document.getString("imageUrl"), document.getString("hotelID"), hotelDangXet.getLocation());
                            dsRoom.add(room);
                        }
                    }
                    roomAdapter.setData(dsRoom);
                } else {
                    Toast.makeText(ShowListRoomFromHotelAdmin.this, "Show Data Fail!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void OnNhanLau(Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowListRoomFromHotelAdmin.this);
        String[] options = {"Update", "Delete"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(ShowListRoomFromHotelAdmin.this, AddEditRoomFromHotel.class);
                    intent.putExtra("check", 2);
                    intent.putExtra("roomUpdate", room);
                    ActivityLaunch.launch(intent);
                } else {
                    deleteData(room);
                }
            }
        }).create().show();

    }

    @Override
    public void OnClick(Room room) {

    }

    private void deleteData(Room room) {
        AlertDialog.Builder bd = new AlertDialog.Builder(ShowListRoomFromHotelAdmin.this);
        bd.setTitle(R.string.deleteroom);
        bd.setIcon(getDrawable(R.drawable.ic_delete));
        bd.setMessage("Bạn có chắc muốn xoá ?");
        bd.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setTitle("Xoá phòng...");
                progressDialog.show();
                db.collection("Rooms").document(room.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Xoá ảnh trong Storage
                        if (!room.getImageUri().equals("0")) {
                            StorageReference imageRef = storageReference.getStorage().getReferenceFromUrl(room.getImageUri());
                            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    deleteRoom();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ShowListRoomFromHotelAdmin.this, "Lỗi xoá ảnh!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            deleteRoom();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ShowListRoomFromHotelAdmin.this, "Deleting Failed", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        bd.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        bd.setCancelable(false);
        bd.create().show();
    }

    private void deleteRoom() {
        progressDialog.dismiss();
        dsRoom.clear();
        readData();
        dsRoom.addAll(dsRoom);
        roomAdapter.notifyDataSetChanged();
        Toast.makeText(ShowListRoomFromHotelAdmin.this, "Xoá thành công!", Toast.LENGTH_SHORT).show();
    }


}