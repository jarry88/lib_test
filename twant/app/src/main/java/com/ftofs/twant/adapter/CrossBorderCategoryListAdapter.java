package com.ftofs.twant.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CrossBorderCategoryItem;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.utils.SLog;

import java.util.List;

public class CrossBorderCategoryListAdapter extends BaseQuickAdapter<CrossBorderCategoryItem, BaseViewHolder> {
    Context context;

    int screenWidth;
    private int selectedIndex = 0;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public CrossBorderCategoryListAdapter(Context context, int layoutResId, @Nullable List<CrossBorderCategoryItem> data) {
        super(layoutResId, data);

        this.context = context;
        screenWidth = Util.getScreenDimension(context).first;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CrossBorderCategoryItem item) {
        int visibleItemCount = getItemCount(); // 分類切換Tab最多顯示多少個Item
        if (visibleItemCount < 1) {
            visibleItemCount = 1;
        }
        if (visibleItemCount > 5) {
            visibleItemCount = 5;
        }

        int position = helper.getAdapterPosition();
        boolean selected = (position == selectedIndex); // 是否選中狀態
        SLog.info("position[%d], selected[%s]", position, selected);


        ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
        layoutParams.width = screenWidth / visibleItemCount;
        helper.itemView.setLayoutParams(layoutParams);

        TextView tvCategoryName= helper.getView(R.id.tv_category_name);
        tvCategoryName.setText(item.catName);

        // 中文字体加粗
        TextPaint paint = tvCategoryName.getPaint();
        paint.setFakeBoldText(selected);
    }
}
