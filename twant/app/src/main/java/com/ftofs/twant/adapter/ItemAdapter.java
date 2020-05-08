package com.ftofs.twant.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.Item;
import com.ftofs.twant.interfaces.OnItemClickListener;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> mItemList;

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;

        public ViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tv_id);
        }
    }

    public ItemAdapter(List<Item> itemList) {
        mItemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mItemList.get(position);
        if (item.isCategoryTitle) {
            holder.tvId.setText("分類_" + item.category);
            holder.tvId.setTextSize(22);
        } else {
            holder.tvId.setText("分類_" + item.category + "_項目_" + item.id);
            holder.tvId.setTextSize(15);
        }

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position, holder.itemView);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}

