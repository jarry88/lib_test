package com.ftofs.twant.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.view.RefundViewHolder;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author gzp
 */
public class SellerReturnAdapter extends BaseQuickAdapter<OrderItem, BaseViewHolder> {

    public SellerReturnAdapter(int layoutResId, @Nullable List<OrderItem> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderItem item) {
        helper.setText(R.id.tv_goods_name, "测试");
    }
}
