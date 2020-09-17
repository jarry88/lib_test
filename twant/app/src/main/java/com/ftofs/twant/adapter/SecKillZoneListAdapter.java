package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.SecKillZoneItem;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;

import java.util.List;

public class SecKillZoneListAdapter extends BaseQuickAdapter<SecKillZoneItem, BaseViewHolder> {
    int unselectedColor;
    int screenWidth;
    private int selectedIndex = -1;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public SecKillZoneListAdapter(Context context, int layoutResId, @Nullable List<SecKillZoneItem> data) {
        super(layoutResId, data);

        unselectedColor = Color.parseColor("#FFE6C0");
        screenWidth = Util.getScreenDimension(context).first;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SecKillZoneItem item) {
        int position = helper.getAdapterPosition();
        boolean selected = (position == selectedIndex); // 是否選中狀態
        SLog.info("position[%d], selected", position, selected);


        ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
        layoutParams.width = screenWidth / 5;
        helper.itemView.setLayoutParams(layoutParams);

        TextView tvStartTime = helper.getView(R.id.tv_start_time);
        TextView tvStatusText = helper.getView(R.id.tv_status_text);

        tvStartTime.setText(item.startTime);
        tvStatusText.setText(item.statusText);

        tvStartTime.setTypeface(Typeface.defaultFromStyle(selected ? Typeface.BOLD : Typeface.NORMAL));
        tvStartTime.setTextColor(selected ? Color.WHITE : unselectedColor);

        // 中文字体加粗
        TextPaint paint = tvStatusText.getPaint();
        paint.setFakeBoldText(selected);
        tvStatusText.setTextColor(selected ? Color.WHITE : unselectedColor);
    }
}


