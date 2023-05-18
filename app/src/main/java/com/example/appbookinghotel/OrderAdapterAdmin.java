package com.example.appbookinghotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapterAdmin extends RecyclerView.Adapter<OrderAdapterAdmin.OrderViewHolderAdmin>{
    ArrayList<Order> dsOrder;

    public void setData(ArrayList<Order> dsOrder){
        this.dsOrder = dsOrder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolderAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row_admin, parent, false);
        return new OrderViewHolderAdmin(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolderAdmin holder, int position) {
        Order order = dsOrder.get(position);

        holder.tvRoomName_order_admin.setText(order.getRoomname());
        holder.tvLocation_order_admin.setText(order.getLocation());
        holder.tvHotelName_order_admin.setText(order.getHotelname());
        holder.tvSoLuongPhongVaKhach_order_admin.setText(order.getSoluongphongvakhach());
        holder.tvThoiGianNhan_order_admin.setText(order.getThoigiannhan());
        holder.tvThoiGianTra_order_admin.setText(" -> " +order.getThoigiantra());
        holder.tvEmailUser.setText(order.getEmailuser());
    }

    @Override
    public int getItemCount() {
        if(dsOrder != null){
            return dsOrder.size();
        }
        return 0;
    }


    class OrderViewHolderAdmin extends RecyclerView.ViewHolder {
        TextView tvRoomName_order_admin, tvThoiGianNhan_order_admin, tvThoiGianTra_order_admin,
                tvEmailUser, tvHotelName_order_admin, tvLocation_order_admin, tvSoLuongPhongVaKhach_order_admin;
        public OrderViewHolderAdmin(@NonNull View itemView) {
            super(itemView);
            tvRoomName_order_admin = itemView.findViewById(R.id.tvRoomName_order_admin);
            tvThoiGianNhan_order_admin = itemView.findViewById(R.id.tvThoiGianNhan_order_admin);
            tvThoiGianTra_order_admin = itemView.findViewById(R.id.tvThoiGianTra_order_admin);
            tvEmailUser = itemView.findViewById(R.id.tvEmailUser);
            tvHotelName_order_admin = itemView.findViewById(R.id.tvHotelName_order_admin);
            tvLocation_order_admin = itemView.findViewById(R.id.tvLocation_order_admin);
            tvSoLuongPhongVaKhach_order_admin = itemView.findViewById(R.id.tvSoLuongPhongVaKhach_order_admin);

        }
    }
}
