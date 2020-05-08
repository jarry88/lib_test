package com.ftofs.twant.adapter;


import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
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
        LinearLayout rootView;
        public ViewHolder(View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tv_category);
            rootView = view.findViewById(R.id.ll_container);
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
        TextView tvCategory =holder.tvCategory;
        holder.tvCategory.setText(menu.categoryName);
//        holder.tvCategory.setTextColor(menu.isSelected ? Color.RED : Color.BLACK);
        boolean selected = menu.isSelected;
        if (selected) {
            tvCategory.setTextColor(Color.parseColor("#2A292A"));
//            tvCategory.setBackgroundColor(Color.parseColor("#ffffffff"));
            holder.rootView.setBackgroundColor(Color.argb(0, 0, 0, 0));

        } else {
            tvCategory.setTextColor(Color.parseColor("#999999"));

//            tvCategory.setBackgroundColor(Color.argb(0, 0, 0, 0));
            holder.rootView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        }
        tvCategory.setTypeface(Typeface.defaultFromStyle(selected ?Typeface.BOLD:Typeface.NORMAL));
//            tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
        tvCategory.setMaxLines(2);
//        tvCategory.setMaxEms(8);
        tvCategory.setEllipsize(TextUtils.TruncateAt.END);
        tvCategory.setFocusable(selected);
        tvCategory.setFocusableInTouchMode(selected);
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

