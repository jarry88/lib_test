package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.ImStoreOrderItem;
import com.ftofs.twant.util.StringUtil;

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
        ImageView goodsImage = helper.getView(R.id.goods_image);

        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.goodsImg)).centerCrop().into(goodsImage);

        String orderNum = mContext.getString(R.string.text_order) + ": " + item.ordersSn;
        helper.setText(R.id.tv_order_num, orderNum);

        helper.setText(R.id.tv_goods_name, item.goodsName);
    }
}
