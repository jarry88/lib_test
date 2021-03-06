package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.util.Util;

public class AreaItemView extends androidx.appcompat.widget.AppCompatTextView {
    int areaId;
    int depth;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    int status = Constant.STATUS_UNSELECTED;
    int twBlue;
    int twBlack;

    public AreaItemView(Context context) {
        this(context, null);
    }

    public AreaItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AreaItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setPadding(0, Util.dip2px(context, 6), 0, Util.dip2px(context, 6));

        setTextSize(15);
        twBlack = getResources().getColor(R.color.tw_black, null);
        twBlue = getResources().getColor(R.color.tw_blue, null);
    }

    public void setStatus(int status) {
        this.status = status;

        if (status == Constant.STATUS_SELECTED) {
            setTextColor(twBlue);
            setBackgroundResource(R.drawable.red_bottom_separator_box);
        } else {
            setTextColor(twBlack);
            setBackground(null);
        }
    }

    public int getStatus() {
        return status;
    }
}
