package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.BizCircleItem;

import java.util.List;


/**
 * 商圈菜單
 * @author zwm
 */
public class BizCircleMenuAdapter extends BaseQuickAdapter<BizCircleItem, BaseViewHolder> {


    private final int twBlack;
    private final int twWhite;
    private final int twGray;
    private int twBlue;

    public BizCircleMenuAdapter(int layoutResId, @Nullable List<BizCircleItem> data, Context context) {
        super(layoutResId, data);
        twBlack = context.getColor(R.color.tw_black);
        twBlue = context.getColor(R.color.tw_blue);
        twWhite = context.getColor(R.color.tw_white);
        twGray = context.getColor(R.color.tw_slight_grey);
    }

    @Override
    protected void convert(BaseViewHolder helper, BizCircleItem item) {
        helper.setText(R.id.tv_name, item.name);
        helper.setVisible(R.id.vw_indicator, !item.selected);
        helper.setTextColor(R.id.tv_name, item.selected?twBlue:twBlack);
        helper.setBackgroundColor(R.id.ll_container,item.selected?twWhite:twGray);
    }
}
