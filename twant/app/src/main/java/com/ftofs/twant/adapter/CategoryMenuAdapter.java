package com.ftofs.twant.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.CategoryMenu;
import com.ftofs.twant.interfaces.OnSelectedListener;

import java.util.List;

/**
 * 分類菜單adapter
 * @author zwm
 */
public class CategoryMenuAdapter extends RecyclerView.Adapter<CategoryMenuAdapter.ViewHolder> {
    private Context context;
    // 分類類型
    int categoryType;
    private OnSelectedListener onSelectedListener;
    private int selectedIndex = 0;
    private List<CategoryMenu> categoryMenuList;

    private int twBlue;
    private int twBlack;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View vwIndicator;
        TextView tvCategoryNameChinese;
        TextView tvCategoryNameEnglish;

        public ViewHolder(View view) {
            super(view);
            vwIndicator = view.findViewById(R.id.vw_indicator);
            tvCategoryNameChinese = view.findViewById(R.id.tv_category_name_chinese);
            tvCategoryNameEnglish = view.findViewById(R.id.tv_category_name_english);
        }
    }

    public CategoryMenuAdapter(Context context, int categoryType, List<CategoryMenu> categoryMenuList,
                               OnSelectedListener onSelectedListener) {
        this.context = context;
        this.categoryType = categoryType;
        this.categoryMenuList = categoryMenuList;
        this.onSelectedListener = onSelectedListener;

        twBlue = context.getResources().getColor(R.color.tw_blue, null);
        twBlack = context.getResources().getColor(R.color.tw_black, null);
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
        } else {
            holder.vwIndicator.setVisibility(View.INVISIBLE);
            holder.tvCategoryNameChinese.setTextColor(twBlack);
            if (categoryType == Constant.CATEGORY_TYPE_SHOP) {
                holder.tvCategoryNameEnglish.setTextColor(twBlack);
            }
        }
    }
}
