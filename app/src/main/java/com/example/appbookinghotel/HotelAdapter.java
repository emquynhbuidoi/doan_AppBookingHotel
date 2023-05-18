package com.example.appbookinghotel;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {
    ArrayList<Hotel> dsHotel;
    LangNghe langNghe;

    public HotelAdapter(LangNghe langNghe) {
        this.langNghe = langNghe;
    }

    public void setData(ArrayList<Hotel> dsHotel) {
        this.dsHotel = dsHotel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_row, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Hotel hotelNe = dsHotel.get(position);

        holder.tenKhachSan.setText(hotelNe.getName());
        holder.diadiem.setText("Địa điểm:  " + hotelNe.getLocation());
        holder.sao.setText(hotelNe.getSao() + " sao");

        Picasso.get()
                .load(hotelNe.getImageUrl())
                .placeholder(R.drawable.ic_default_hotel)
                .fit()
                .centerCrop()
                .into(holder.ivImageHotel_admin);

        // hanh dong
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                langNghe.setOnClickItem(hotelNe);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dsHotel != null) {
            return dsHotel.size();
        }
        return 0;
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView tenKhachSan, diadiem, sao;
        ImageView ivImageHotel_admin;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            tenKhachSan = itemView.findViewById(R.id.tvRoomName);
            diadiem = itemView.findViewById(R.id.tvLocation);
            sao = itemView.findViewById(R.id.tvRoomRate);

            ivImageHotel_admin = itemView.findViewById(R.id.ivImageHotel_admin);
        }
    }


    interface LangNghe {
        void setOnClickItem(Hotel ht);
    }
}
