package com.ftofs.twant.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.entity.CategoryMenu;
import com.gzp.lib_common.base.callback.OnSelectedListener;

import java.util.List;

/**
 * 分類菜單adapter
 * @author zwm
 */
public class CategoryMenuAdapter extends RecyclerView.Adapter<CategoryMenuAdapter.ViewHolder> {
    // 分類類型
    int categoryType;
    private OnSelectedListener onSelectedListener;
    private int selectedIndex = 0;
    private List<CategoryMenu> categoryMenuList;

    private int twBlue;
    private int twBlack;
    private int twGray;
    private int twWhite;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        View vwIndicator;
        TextView tvCategoryNameChinese;
        TextView tvCategoryNameEnglish;

        public ViewHolder(View view) {
            super(view);
            vwIndicator = view.findViewById(R.id.vw_indicator);
            tvCategoryNameChinese = view.findViewById(R.id.tv_category_name_chinese);
            tvCategoryNameEnglish = view.findViewById(R.id.tv_category_name_english);
            ivCategoryIcon = view.findViewById(R.id.category_icon);
        }
    }

    public CategoryMenuAdapter(Context context, int categoryType, List<CategoryMenu> categoryMenuList,
                               OnSelectedListener onSelectedListener) {
        this.categoryType = categoryType;
        this.categoryMenuList = categoryMenuList;
        this.onSelectedListener = onSelectedListener;

        twBlue = context.getResources().getColor(R.color.tw_blue, null);
        twBlack = context.getResources().getColor(R.color.tw_black, null);
        twGray = context.getResources().getColor(R.color.tw_grey_F3F3, null);
        twWhite = context.getResources().getColor(R.color.tw_white, null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (categoryType == Constant.CATEGORY_TYPE_SHOP) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_shop_menu_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_commodity_brand_menu_item, parent, false);
        }
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CategoryMenu categoryMenu = categoryMenuList.get(position);

        holder.tvCategoryNameChinese.setText(categoryMenu.categoryNameChinese);
        if (categoryType == Constant.CATEGORY_TYPE_SHOP) {
            holder.tvCategoryNameEnglish.setText(categoryMenu.categoryNameEnglish);
//            SLog.info("id %d,name %s",categoryMenu.categoryId,categoryMenu.categoryNameChinese);
            if (categoryMenu.categoryId == 75) {
                holder.ivCategoryIcon.setVisibility(View.VISIBLE);
                holder.tvCategoryNameEnglish.setVisibility(View.GONE);
            } else {
                holder.ivCategoryIcon.setVisibility(View.GONE);
                holder.tvCategoryNameEnglish.setVisibility(View.VISIBLE);
            }
        }

        changeItemStatus(holder, position == selectedIndex);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int preSelectedIndex = selectedIndex;
                selectedIndex = position;
                notifyItemChanged(preSelectedIndex);
                notifyItemChanged(position);

                if (onSelectedListener != null) {
                    onSelectedListener.onSelected(PopupType.DEFAULT, categoryMenu.categoryId, null);
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
            holder.vwIndicator.setVisibility(View.VISIBLE);
            holder.tvCategoryNameChinese.setTextColor(twBlue);
            if (categoryType == Constant.CATEGORY_TYPE_SHOP) {
                holder.tvCategoryNameEnglish.setTextColor(twBlue);
            }
            holder.itemView.setBackgroundColor(twWhite);
        } else {
            holder.vwIndicator.setVisibility(View.INVISIBLE);
            holder.tvCategoryNameChinese.setTextColor(twBlack);
            if (categoryType == Constant.CATEGORY_TYPE_SHOP) {
                holder.tvCategoryNameEnglish.setTextColor(twBlack);
            }
            holder.itemView.setBackgroundColor(twGray);

        }
    }
}
