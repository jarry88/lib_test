package com.ftofs.twant.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.ImStoreGoodsItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class ImStoreGoodsListAdapter extends BaseMultiItemQuickAdapter<ImStoreGoodsItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ImStoreGoodsListAdapter(List<ImStoreGoodsItem> data) {
        super(data);

        addItemType(ImStoreGoodsItem.ITEM_TYPE_HEADER, R.layout.im_store_goods_header);
        addItemType(ImStoreGoodsItem.ITEM_TYPE_ITEM, R.layout.im_store_goods_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImStoreGoodsItem item) {
        int itemType = item.itemType;

        if (itemType == ImStoreGoodsItem.ITEM_TYPE_HEADER) {
            helper.setText(R.id.tv_header, item.goodsName);
        } else {
            ImageView goodsImg = helper.getView(R.id.goods_image);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.goodsImg)).centerCrop().into(goodsImg);

            helper.setText(R.id.tv_goods_name, item.goodsName);
        }
    }
}
