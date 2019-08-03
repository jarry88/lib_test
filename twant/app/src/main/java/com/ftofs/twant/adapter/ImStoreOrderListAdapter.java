package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.ImStoreOrderItem;

import java.util.List;

/**
 * Im聊天訂單彈窗列表Adapter
 * @author zwm
 */
public class ImStoreOrderListAdapter extends BaseQuickAdapter<ImStoreOrderItem, BaseViewHolder> {

    public ImStoreOrderListAdapter(int layoutResId, @Nullable List<ImStoreOrderItem> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, ImStoreOrderItem item) {

    }
}
