package com.ftofs.twant.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;

public class RefundViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvName;
    private final View mView;

    public RefundViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        tvName = itemView.findViewById(R.id.tv_goods_name);
    }
    @NonNull
    public RefundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RefundViewHolder(mView);
    }


    public void onBindViewHolder(@NonNull RefundViewHolder holder, int position) {
        holder.tvName.setText(position);

    }

    public int getItemCount() {
        return 10;
    }

    public void setTvName(String name) {
        tvName.setText(name);
    }
}
