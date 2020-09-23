package com.ftofs.twant.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.lib_net.model.StoreLabel;
import com.gzp.lib_common.base.callback.OnSelectedListener;
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
        List<StoreLabel> storeLabelList = item.getStoreLabelList();
        LinearLayout llInnerItemContainer = helper.getView(R.id.ll_inner_item_container);
        int subItemCount = storeLabelList.size(); // 二級分類的項數
        String labelName = item.getStoreLabelName();
        if (subItemCount > 0) {
            // 如果存在二級分類，顯示項數(0 不顯示是為了防止用戶誤解)
            labelName += "(" + subItemCount + ")";
        }

        helper.setText(R.id.tv_outer_title, labelName);

        llInnerItemContainer.removeAllViews();
        int count = 0;
        LinearLayout llHorizontalContainer = null;
        for (StoreLabel storeLabel : storeLabelList) {
            if (count % 2 == 0) {
                // 添加水平方向的容器
                llHorizontalContainer = new LinearLayout(context);
                llHorizontalContainer.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(Util.dip2px(context, 15), 0, Util.dip2px(context, 15), Util.dip2px(context, 15));
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
                    onSelectedListener.onSelected(PopupType.DEFAULT, storeLabelId, null);
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
