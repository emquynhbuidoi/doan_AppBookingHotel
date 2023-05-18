package com.example.appbookinghotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    ArrayList<Order> dsOrder;
    LangNghe langNghe;
    public OrderAdapter(LangNghe langNghe) {
        this.langNghe = langNghe;
    }
    public void setData(ArrayList<Order> dsOrder){
        this.dsOrder = dsOrder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = dsOrder.get(position);

        holder.tvRoomName_order.setText(order.getRoomname());
        holder.tvLocation_order.setText(order.getLocation());
        holder.tvHotelName_order.setText(order.getHotelname());
        holder.tvSoLuongPhongVaKhach_order.setText(order.getSoluongphongvakhach());
        holder.tvThoiGianNhan_order.setText(order.getThoigiannhan());
        holder.tvThoiGianTra_order.setText(" -> " + order.getThoigiantra());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                langNghe.setOnClickItem(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(dsOrder != null){
            return dsOrder.size();
        }
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView tvRoomName_order, tvThoiGianNhan_order, tvThoiGianTra_order, tvHotelName_order, tvLocation_order,
                tvSoLuongPhongVaKhach_order;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName_order = itemView.findViewById(R.id.tvRoomName_order_admin);
            tvThoiGianNhan_order = itemView.findViewById(R.id.tvThoiGianNhan_order_admin);
            tvThoiGianTra_order = itemView.findViewById(R.id.tvThoiGianTra_order_admin);
            tvHotelName_order = itemView.findViewById(R.id.tvHotelName_order_admin);
            tvLocation_order = itemView.findViewById(R.id.tvLocation_order_admin);
            tvSoLuongPhongVaKhach_order = itemView.findViewById(R.id.tvSoLuongPhongVaKhach_order_admin);
        }
    }
    interface LangNghe{
        void setOnClickItem(Order order);
    }
}
