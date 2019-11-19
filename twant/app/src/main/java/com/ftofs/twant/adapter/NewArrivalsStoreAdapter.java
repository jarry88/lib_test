package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.order.NewArrivalsStoreItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 最新想要店列表Adapter
 * @author zwm
 */
public class NewArrivalsStoreAdapter extends BaseQuickAdapter<NewArrivalsStoreItem, BaseViewHolder> {
    public NewArrivalsStoreAdapter(int layoutResId, @Nullable List<NewArrivalsStoreItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewArrivalsStoreItem item) {
        ImageView imgStoreFigure = helper.getView(R.id.img_store_figure);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.storeFigure)).centerCrop().into(imgStoreFigure);

        helper.setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_store_class, item.storeClass);

        helper.addOnClickListener(R.id.goods_image_left_container, R.id.goods_image_middle_container, R.id.goods_image_right_container);

        // 店鋪的3個商品展示
        int index = 0;
        LinearLayout llGoodsImageContainer = null;
        for (Goods goods : item.goodsList) {
            String imageSrc = StringUtil.normalizeImageUrl(goods.imageUrl);

            if (index == 0) {
                ImageView goodsImageLeft = helper.getView(R.id.goods_image_left);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) goodsImageLeft.getLayoutParams();
                goodsImageLeft.setLayoutParams(layoutParams);
                Glide.with(mContext).load(imageSrc).centerCrop().into(goodsImageLeft);
                llGoodsImageContainer = helper.getView(R.id.goods_image_left_container);
            } else if (index == 1) {
                ImageView goodsImageMiddle = helper.getView(R.id.goods_image_middle);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) goodsImageMiddle.getLayoutParams();
                goodsImageMiddle.setLayoutParams(layoutParams);
                Glide.with(mContext).load(imageSrc).centerCrop().into(goodsImageMiddle);
                llGoodsImageContainer = helper.getView(R.id.goods_image_middle_container);
            } else if (index == 2) {
                ImageView goodsImageRight = helper.getView(R.id.goods_image_right);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) goodsImageRight.getLayoutParams();
                goodsImageRight.setLayoutParams(layoutParams);
                Glide.with(mContext).load(imageSrc).centerCrop().into(goodsImageRight);
                llGoodsImageContainer = helper.getView(R.id.goods_image_right_container);
            }

            llGoodsImageContainer.setVisibility(View.VISIBLE);
            ++index;
        }
    }
}
