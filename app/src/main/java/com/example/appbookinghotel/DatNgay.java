package com.example.appbookinghotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DatNgay extends AppCompatActivity implements PickerDialog.DialogListener {
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private boolean isExpanded = true;
    private ImageView imgAnh;
    private TextView tvTenPhong, tvGia, tvNhanPhong, tvTraPhong, tvSoLuongPhongVaKhach, tvMoTa;
    private Room roomDangXet;
    public static Hotel hotelDangXet;
    private String thoiGianNhanPhong, thoiGianTraPhong, soLuongPhongVaKhach;
    private int ngay, thang, nam;
    private int checkNhanPhong = 1;
    private int checkTraPhong = 1;
    private int checkSoLuongPhong = 1;
    private Button btnDatNgay;

    //yeu thich
    private ArrayList<YeuThich> dsYeuThich;

    private int check_yeuthich = 0;

    //Map
    private ImageView ivMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_ngay);

        Intent intent = getIntent();
        roomDangXet = (Room) intent.getSerializableExtra("roomDangXet");
        hotelDangXet = (Hotel) intent.getSerializableExtra("hotelDangXet");

        initUi();
        initToolBar();
        initToolBarAnimation();
        setThongTin();

        tvNhanPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNhanPhong = 2;
                tvNhanPhong.setError(null);

                Calendar lich = Calendar.getInstance();
                ngay = lich.get(Calendar.DAY_OF_MONTH);
                thang = lich.get(Calendar.MONTH);
                nam = lich.get(Calendar.YEAR);


                DatePickerDialog datePickerDialog = new DatePickerDialog(DatNgay.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (year < nam) {
                            checkNhanPhong = 0;
                        } else {
                            if (month + 1 < thang + 1) {
                                checkNhanPhong = 0;
                            } else {
                                if (dayOfMonth < ngay) {
                                    checkNhanPhong = 0;
                                }
                            }
                        }
                        if (checkNhanPhong == 2) {
                            String time = (String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month + 1) + "/" + year).trim();
                            tvNhanPhong.setText(time);
                            thoiGianNhanPhong = time;
                        } else {
                            Toast.makeText(DatNgay.this, "Ngày đặt sai !", Toast.LENGTH_SHORT).show();
                            tvNhanPhong.setError("Lỗi đặt ngày");
                            tvNhanPhong.requestFocus();
                        }
                    }
                }, nam, thang, ngay);
                datePickerDialog.show();
            }
        });

        tvTraPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTraPhong = 2;
                tvTraPhong.setError(null);
                Calendar lich = Calendar.getInstance();
                ngay = lich.get(Calendar.DAY_OF_MONTH);
                thang = lich.get(Calendar.MONTH);
                nam = lich.get(Calendar.YEAR);


                DatePickerDialog datePickerDialog = new DatePickerDialog(DatNgay.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        if (dayOfMonth < ngay) {
                        }
                        if (year < nam) {
                            checkTraPhong = 0;
                        } else {
                            if (month + 1 < thang + 1) {
                                checkTraPhong = 0;
                            } else {
                                if (dayOfMonth < ngay) {
                                    checkTraPhong = 0;
                                }
                            }
                        }
                        if (checkTraPhong == 2) {
                            String time = (String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month + 1) + "/" + year).trim();
                            tvTraPhong.setText(time);
                            thoiGianTraPhong = time;
                        } else {
                            Toast.makeText(DatNgay.this, "Ngày trả sai !", Toast.LENGTH_SHORT).show();
                            tvTraPhong.setError("Lỗi trả ngày");
                            tvTraPhong.requestFocus();
                        }
                    }
                }, nam, thang, ngay);
                datePickerDialog.show();
            }
        });

        tvSoLuongPhongVaKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        btnDatNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNhanPhong != 2 || checkSoLuongPhong != 2 || checkTraPhong != 2) {
                    Toast.makeText(DatNgay.this, "Sai thông tin đặt", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DatNgay.this);
                    builder.setTitle("Thông báo");
                    builder.setIcon(R.drawable.ic_notice);
                    builder.setMessage("Bạn có chắc chắn đặt phòng");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DatNgay.this);
                            builder.setTitle("Thành công");
                            builder.setIcon(R.drawable.ic_sucssec);
                            builder.setMessage("Đặt phòng thành công");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    saveDataOrder(user.getEmail(), soLuongPhongVaKhach, thoiGianNhanPhong, thoiGianTraPhong);

                                }
                            });
                            builder.setCancelable(false);
                            builder.create().show();

                        }
                    });
                    builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                }
            }
        });


        //GGMAP
        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatNgay.this, GoogleMap.class);
                intent.putExtra("hotel", hotelDangXet);
                startActivity(intent);
            }
        });
    }

    private void saveDataOrder(String emailuser, String soLuongPhongVaKhach, String thoiGianNhanPhong, String thoiGianTraPhong) {
        progressDialog.setTitle("Lưu thông tin đặt phòng");
        progressDialog.show();
        db = FirebaseFirestore.getInstance();

        String id = UUID.randomUUID().toString();

        Map<String, Object> order = new HashMap<>();
        order.put("id", id);
        order.put("roomname", roomDangXet.getRoomename());
        order.put("hotelname", hotelDangXet.getName());
        order.put("loaction", hotelDangXet.getLocation());
        order.put("soluongphongvakhach", soLuongPhongVaKhach);
        order.put("thoigiannhan", thoiGianNhanPhong);
        order.put("thoigiantra", thoiGianTraPhong);
        order.put("emailuser", emailuser);
        order.put("price", roomDangXet.getPrice());


        db.collection("Orders").document(id).set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();

                        Toast.makeText(DatNgay.this, "Đặt phòng thành công", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(DatNgay.this, MainActivity.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(DatNgay.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void openDialog() {
        PickerDialog pickerDialog = new PickerDialog();
        pickerDialog.show(getSupportFragmentManager(), "Chọn Số Lượng Nè");
    }

    private void initUi() {
        progressDialog = new ProgressDialog(DatNgay.this);
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        toolbar = findViewById(R.id.toolbar);

        imgAnh = findViewById(R.id.ivAnhChiTiet);
        tvGia = findViewById(R.id.tvGiaPhong);
        tvTenPhong = findViewById(R.id.tvTenPhong);
        tvNhanPhong = findViewById(R.id.tvNhanPhong);
        tvTraPhong = findViewById(R.id.tvTraPhong);
        tvSoLuongPhongVaKhach = findViewById(R.id.tvSoLuongPhongVaKhach);
        tvMoTa = findViewById(R.id.tvMoTa);

        btnDatNgay = findViewById(R.id.btnDatNgay);
        db = FirebaseFirestore.getInstance();

        ivMap = findViewById(R.id.ivMap);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            collapsingToolbarLayout.setTitle("Ten Khach San");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // btn Back
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initToolBarAnimation() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trucanh);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                int myColor = palette.getVibrantColor(getResources().getColor(R.color.blue));

                collapsingToolbarLayout.setContentScrimColor(myColor);   // mau cua toolBar
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 570) {
                    isExpanded = false;
                } else {
                    isExpanded = true;
                }
                invalidateOptionsMenu();
            }
        });
    }

    private Menu menuNe;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_yeuthich, menu);
        this.menuNe = menu;

//        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(@NonNull MenuItem item) {
//                // Yeu thich
//                if (check_yeuthich == 0) {
//                    Toast.makeText(DatNgay.this, "Đã yêu thích", Toast.LENGTH_SHORT).show();
//                    menuNe.getItem(0).setIcon(ContextCompat.getDrawable(DatNgay.this, R.drawable.favorite));
//                    check_yeuthich = 1;
//                } else if (check_yeuthich == 1) {
//                    Toast.makeText(DatNgay.this, "Bỏ yêu thích", Toast.LENGTH_SHORT).show();
//                    menuNe.getItem(0).setIcon(ContextCompat.getDrawable(DatNgay.this, R.drawable.favorite_border));
//                    check_yeuthich = 0;
//                }
//                item.setOnMenuItemClickListener()
//                return true;
//            }
//        });

        if (isExpanded == false) {
            //collap menu
            collapsingToolbarLayout.setTitle(hotelDangXet.getName());
        } else {
            //Expanded
            collapsingToolbarLayout.setTitle(" ");

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.yeuthich_bar:
                if(check_yeuthich == 0){
                    ThemVaoYeuThich();
                    check_yeuthich = 1;
                }
                else{
                    XoaKhoiYeuThich();
                    check_yeuthich = 0;
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void XoaKhoiYeuThich() {
        db.collection("dsYeuThich").document(roomDangXet.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                menuNe.getItem(0).setIcon(R.drawable.favorite_border);
                Toast.makeText(DatNgay.this, "Bỏ yêu thích", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DatNgay.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Firebase
    private void ThemVaoYeuThich() {
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Map<String, Object> yeuthich = new HashMap<>();
        yeuthich.put("id", roomDangXet.getId());
        yeuthich.put("emailuser", user.getEmail());

        // Add a new document
        db.collection("dsYeuThich").document(roomDangXet.getId()).set(yeuthich)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
//                        menuNe.getItem(0).setIcon(R.drawable.favorite);
                        Toast.makeText(DatNgay.this, "Đã yêu thích", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(DatNgay.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    private void setThongTin() {
        tvTenPhong.setText(roomDangXet.getRoomename());
        tvGia.setText(roomDangXet.getPrice() + " VNĐ");
        tvMoTa.setText(hotelDangXet.getDescription());
        Picasso.get()
                .load(roomDangXet.getImageUri())
                .placeholder(R.drawable.trucanh)
                .fit()
                .centerCrop()
                .into(imgAnh);
    }

    @Override
    public void applyTexts(String soPhong, String soNguoiLon, String soTreEm) {
        checkSoLuongPhong = 2;
        tvSoLuongPhongVaKhach.setError(null);
        if (soPhong.equals("") || Integer.parseInt(soPhong) < 1 || soNguoiLon.equals("") || soTreEm.equals("")) {
            checkSoLuongPhong = 0;
            tvSoLuongPhongVaKhach.setError("Not null");
            Toast.makeText(DatNgay.this, "Số lượng phòng và khách không đúng", Toast.LENGTH_SHORT).show();
        } else {
            String soluong = (soPhong + " Phòng " + soNguoiLon + " Người lớn " + soTreEm + " Trẻ em ").trim();
            tvSoLuongPhongVaKhach.setText(soluong);
            soLuongPhongVaKhach = soluong;
        }
    }
}