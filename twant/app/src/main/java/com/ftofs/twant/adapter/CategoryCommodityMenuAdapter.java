package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.CategoryMenu;
import com.ftofs.twant.interfaces.OnSelectedListener;

import java.util.List;

/**
 * 商品分類菜單adapter
 * @author zwm
 */
public class CategoryCommodityMenuAdapter extends RecyclerView.Adapter<CategoryCommodityMenuAdapter.ViewHolder> {
    private Context context;
    private OnSelectedListener onSelectedListener;
    private int selectedIndex = 0;
    private List<CategoryMenu> categoryMenuList;

    private int twRed;
    private int twBlack;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View vwIndicator;
        TextView tvCategoryNameChinese;

        public ViewHolder(View view) {
            super(view);
            vwIndicator = view.findViewById(R.id.vw_indicator);
            tvCategoryNameChinese = view.findViewById(R.id.tv_category_name_chinese);
        }
    }

    public CategoryCommodityMenuAdapter(Context context, List<CategoryMenu> categoryMenuList, OnSelectedListener onSelectedListener) {
        this.context = context;
        this.categoryMenuList = categoryMenuList;
        this.onSelectedListener = onSelectedListener;

        twRed = context.getResources().getColor(R.color.tw_red, null);
        twBlack = context.getResources().getColor(R.color.tw_black, null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_commodity_menu_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CategoryMenu categoryMenu = categoryMenuList.get(position);

        holder.tvCategoryNameChinese.setText(categoryMenu.categoryNameChinese);

        changeItemStatus(holder, position == selectedIndex);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int preSelectedIndex = selectedIndex;
                selectedIndex = position;
                notifyItemChanged(preSelectedIndex);
                notifyItemChanged(position);

                if (onSelectedListener != null) {
                    onSelectedListener.onSelected(categoryMenu.categoryId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryMenuList.size();
    }

    /**
     * 改變某一項的狀態
     * @param holder
     * @param select true -- 選中  false -- 取消選中
     */
    private void changeItemStatus(ViewHolder holder, boolean select) {
        if (select) {
            holder.tvCategoryNameChinese.setTextColor(twRed);
            holder.vwIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.tvCategoryNameChinese.setTextColor(twBlack);
            holder.vwIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
