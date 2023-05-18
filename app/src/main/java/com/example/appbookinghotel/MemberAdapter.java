package com.example.appbookinghotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    ArrayList<Member> dsMember;

    public void setData(ArrayList<Member> dsMember){
        this.dsMember = dsMember;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_row, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = dsMember.get(position);

        holder.tvEmailUser_admin.setText(member.getEmail());
    }

    @Override
    public int getItemCount() {
        if(dsMember != null){
            return dsMember.size();
        }
        return 0;
    }

    class  MemberViewHolder extends RecyclerView.ViewHolder {
        EditText tvEmailUser_admin;
        CircleImageView ivUserAvatar;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvEmailUser_admin = itemView.findViewById(R.id.tvEmailUser_admin);
        }
    }
}
