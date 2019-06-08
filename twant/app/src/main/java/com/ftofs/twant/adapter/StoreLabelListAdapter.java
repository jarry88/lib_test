package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.util.Util;

import java.util.List;

/**
 * 分類項的Adapter
 * @author zwm
 */
public class StoreLabelListAdapter extends BaseQuickAdapter<StoreLabel, BaseViewHolder> {
    Context context;
    int twBlack;
    OnSelectedListener onSelectedListener;

    public StoreLabelListAdapter(Context context, int layoutResId, @Nullable List<StoreLabel> data, OnSelectedListener onSelectedListener) {
        super(layoutResId, data);
        this.context = context;
        this.onSelectedListener = onSelectedListener;

        twBlack = context.getColor(R.color.tw_black);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreLabel item) {
        LinearLayout llInnerItemContainer = helper.getView(R.id.ll_inner_item_container);
        helper.setText(R.id.tv_outer_title, item.getStoreLabelName() + "(" + item.getStoreLabelList().size() + ")");

        if (item.getIsFold() == Constant.ONE) {
            llInnerItemContainer.setVisibility(View.GONE);
            helper.setImageResource(R.id.img_folding_status, R.drawable.expand_button);
        } else {
            llInnerItemContainer.setVisibility(View.VISIBLE);
            helper.setImageResource(R.id.img_folding_status, R.drawable.btn_expanded);
        }

        llInnerItemContainer.removeAllViews();
        List<StoreLabel> storeLabelList = item.getStoreLabelList();
        int count = 0;
        LinearLayout llHorizontalContainer = null;
        for (StoreLabel storeLabel : storeLabelList) {
            if (count % 2 == 0) {
                // 添加水平方向的容器
                llHorizontalContainer = new LinearLayout(context);
                llHorizontalContainer.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(Util.dip2px(context, 15), 0, Util.dip2px(context, 15), Util.dip2px(context, 5));
                llInnerItemContainer.addView(llHorizontalContainer, params);
            }

            // 添加每一个按钮项
            TextView textView = new TextView(context);
            textView.setPadding(Util.dip2px(context, 15), Util.dip2px(context, 8), 0, Util.dip2px(context, 8));
            textView.setText(storeLabel.getStoreLabelName());
            textView.setTextColor(twBlack);
            textView.setTextSize(14);
            textView.setBackgroundResource(R.drawable.shop_category_goods_item_bg);
            final int storeLabelId = storeLabel.getStoreLabelId();
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectedListener.onSelected(0, storeLabelId, null);
                }
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            layoutParams.setMargins(0, 0, Util.dip2px(context, 7.5f), 0);
            llHorizontalContainer.addView(textView, layoutParams);

            ++count;
        }

        // 如果是奇數個，在后面補上占位的View
        if (count % 2 == 1) {
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            layoutParams.setMargins(0, 0, Util.dip2px(context, 7.5f), 0);
            llHorizontalContainer.addView(textView, layoutParams);
        }

        helper.addOnClickListener(R.id.btn_switch_folding_status);
    }
}