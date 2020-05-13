package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.PayWayItem;

import java.util.List;

/**
 * 支付方式Adapter
 * @author zwm
 */
public class PayWayAdapter extends BaseQuickAdapter<PayWayItem, BaseViewHolder> {

    // 選中高亮文本的顏色
    int highlightedTextColor;
    int twBlack;

    public PayWayAdapter(Context context, int layoutResId, @Nullable List<PayWayItem> data) {
        super(layoutResId, data);

        highlightedTextColor = context.getColor(R.color.tw_blue);
        twBlack = context.getColor(R.color.tw_black);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayWayItem item) {
        TextView tvPayWayName = helper.getView(R.id.tv_pay_way_name);
        ImageView checkedImage = helper.getView(R.id.checked_image);

        tvPayWayName.setText(item.payWayName);
        helper.setText(R.id.tv_pay_way_desc, item.payWayDesc);

        if (item.isSelected) {  // 選中的高亮顯示
            helper.setImageResource(R.id.img_icon, item.selectedIconResId);
            tvPayWayName.setTextColor(highlightedTextColor);
            checkedImage.setImageResource(R.drawable.icon_checked);
        } else {
            helper.setImageResource(R.id.img_icon, item.unselectedIconResId);
            tvPayWayName.setTextColor(twBlack);
            checkedImage.setImageResource(R.drawable.icon_cart_item_unchecked);
        }
    }
}
