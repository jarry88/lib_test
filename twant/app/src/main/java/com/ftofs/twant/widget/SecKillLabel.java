package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ftofs.twant.R;

public class SecKillLabel extends LinearLayout {
    int scheduleId;
    TextView tvStartTime;
    TextView tvStatusText;

    int unselectedColor = Color.parseColor("#FFE6C0");

    public SecKillLabel(Context context) {
        this(context, null);
    }

    public SecKillLabel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecKillLabel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View root = LayoutInflater.from(context).inflate(R.layout.sec_kill_label, this, true);
        tvStartTime = root.findViewById(R.id.tv_start_time);
        tvStatusText = root.findViewById(R.id.tv_status_text);
    }

    public void setData(int scheduleId, String startTime, String statusText) {
        this.scheduleId = scheduleId;
        tvStartTime.setText(startTime);
        tvStatusText.setText(statusText);
    }

    /**
     * 设置Label的选中状态
     * @param selected
     */
    public void setLabelSelected(boolean selected) {
        tvStartTime.setTypeface(Typeface.defaultFromStyle(selected ? Typeface.BOLD : Typeface.NORMAL));
        tvStartTime.setTextColor(selected ? Color.WHITE : unselectedColor);

        // 中文字体加粗
        TextPaint paint = tvStatusText.getPaint();
        paint.setFakeBoldText(selected);
        tvStatusText.setTextColor(selected ? Color.WHITE : unselectedColor);
    }
}



