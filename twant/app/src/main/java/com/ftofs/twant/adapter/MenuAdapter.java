package com.ftofs.twant.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.Menu;
import com.ftofs.twant.interfaces.OnItemClickListener;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<Menu> mMenuList;

    public int lastSelectedPosition = 0; // 最近一次選中的菜單的Position

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        public ViewHolder(View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tv_category);
        }
    }

    public MenuAdapter(List<Menu> menuList) {
        mMenuList = menuList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_menu, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Menu menu = mMenuList.get(position);
        holder.tvCategory.setText("分類_" + menu.category);
        holder.tvCategory.setTextColor(menu.isSelected ? Color.RED : Color.BLACK);
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
        return mMenuList.size();
    }
}

