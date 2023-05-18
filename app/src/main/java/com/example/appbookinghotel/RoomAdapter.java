package com.example.appbookinghotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    ArrayList<Room> dsRoom;
    LangNgheRoom langNghe;

    public RoomAdapter(LangNgheRoom langNghe) {
        this.langNghe = langNghe;
    }

    public void setData(ArrayList<Room> dsRoom){
        this.dsRoom = dsRoom;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_row, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room roomNe = dsRoom.get(position);

        holder.roomname.setText(roomNe.getRoomename());
        holder.roomprice.setText("VNĐ:  " + roomNe.getPrice());
        holder.rate.setText(roomNe.getRate() + " rate(%)");
        holder.location.setText("Địa điểm: " + roomNe.getLocation());

        if(roomNe.getImageUri().equals("0")){
            holder.ivHinh.setImageResource(R.drawable.trucanh);
        }else{
            Picasso.get()
                    .load(roomNe.getImageUri())
                    .placeholder(R.drawable.bed)
                    .fit()
                    .centerCrop()
                    .into(holder.ivHinh);

        }



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                langNghe.OnNhanLau(roomNe);
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                langNghe.OnClick(roomNe);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(dsRoom != null){
            return dsRoom.size();
        }
        return 0;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder{
        TextView rate, location, roomname, roomprice;
        ImageView ivHinh;


        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            rate = itemView.findViewById(R.id.tvRoomRate);
            location = itemView.findViewById(R.id.tvLocation);
            roomname = itemView.findViewById(R.id.tvRoomName);
            roomprice = itemView.findViewById(R.id.tvRoomPrice);
            ivHinh = itemView.findViewById(R.id.ivHinh);
        }
    }

    interface LangNgheRoom{
        void OnNhanLau(Room room);
        void OnClick(Room room);
    }
}
