package com.ftofs.twant.seller.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecListItem;
import com.ftofs.twant.util.Util;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

public class SellerSpecListAdapter extends BaseQuickAdapter<SellerSpecListItem, BaseViewHolder> {
    Context context;

    public SellerSpecListAdapter(Context context, int layoutResId, @Nullable List<SellerSpecListItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerSpecListItem item) {
        TextView tvSpecName = helper.getView(R.id.tv_spec_name);
        tvSpecName.setText(item.specName);

        helper.addOnClickListener(R.id.btn_edit, R.id.btn_delete);

        if (item.specType == SellerSpecListItem.SPEC_TYPE_SYSTEM) {
            tvSpecName.setBackgroundResource(R.color.tw_red);

            helper.setText(R.id.tv_spec_type, "平臺預設規格");
        } else {
            tvSpecName.setBackgroundResource(R.color.tw_blue);

            helper.setText(R.id.tv_spec_type, "自定義規格");
        }

        FlowLayout flSpecValueContainer = helper.getView(R.id.fl_spec_value_container);
        for (SellerSpecItem sellerSpecItem : item.sellerSpecItemList) {

            TextView button = new TextView(context);

            button.setText(sellerSpecItem.name);
            button.setTextSize(14);
            button.setBackgroundResource(R.drawable.spec_item_unselected_bg);
            button.setPadding(Util.dip2px(context, 16), 0, Util.dip2px(context, 16), 0);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Util.dip2px(context, 32));
            button.setGravity(Gravity.CENTER);
            flSpecValueContainer.addView(button, layoutParams);
        }

        int position = helper.getAdapterPosition();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == 0) {
            // 第一項，設置大一點的topMargin
            layoutParams.topMargin = Util.dip2px(context, 15);
        } else {
            layoutParams.topMargin = 0;
        }
    }
}
