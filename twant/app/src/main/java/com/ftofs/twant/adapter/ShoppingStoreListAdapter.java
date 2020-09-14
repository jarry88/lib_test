package com.ftofs.twant.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.StoreItem;
import com.ftofs.twant.util.StringUtil;


import java.util.List;

import static android.view.View.GONE;

/**
 * @author gzp
 */
public class ShoppingStoreListAdapter extends BaseQuickAdapter<StoreItem, BaseViewHolder> {
    public ShoppingStoreListAdapter(int layoutResId, @Nullable List<StoreItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreItem item) {
//        SLog.info("name[%s]",item.storeName);

        helper.addOnClickListener(R.id.goods_image_left_container, R.id.goods_image_middle_container, R.id.goods_image_right_container);

        ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);

        String storeAvatarUrl =StringUtil.normalizeImageUrl(item.storeAvatar);
        if ("https://192.168.5.29/public/img/default_store_avatar.png".equals(storeAvatarUrl)||StringUtil.isEmpty(storeAvatarUrl)) {
            Glide.with(mContext).load(R.drawable.default_store_avatar).into(imgStoreAvatar);
        } else {
            Glide.with(mContext).load(storeAvatarUrl).into(imgStoreAvatar);
        }
        LinearLayout goodsImageLeftContainer = helper.getView(R.id.goods_image_left_container);
        LinearLayout goodsImageMiddleContainer = helper.getView(R.id.goods_image_middle_container);
        LinearLayout goodsImageRightContainer = helper.getView(R.id.goods_image_right_container);

        ImageView goodsImageLeft = helper.getView(R.id.goods_image_left);
        ImageView goodsImageMiddle = helper.getView(R.id.goods_image_middle);
        ImageView goodsImageRight = helper.getView(R.id.goods_image_right);

        goodsImageLeftContainer.setVisibility(GONE);
        goodsImageMiddleContainer.setVisibility(GONE);
        goodsImageRightContainer.setVisibility(GONE);
        List<Goods> goodsList = item.zoneGoodsVoList;
        for (int i = 0; i < goodsList.size(); i++) {
            String imageSrc = StringUtil.normalizeImageUrl(goodsList.get(i).goodsImage, "?x-oss-process=image/resize,w_300");

            if (i == 0) {
                goodsImageMiddleContainer.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(imageSrc).centerCrop().into(goodsImageMiddle);
            } else if (i == 1) {
                goodsImageLeftContainer.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(imageSrc).centerCrop().into(goodsImageLeft);
            } else if (i == 2) {
                goodsImageRightContainer.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(imageSrc).centerCrop().into(goodsImageRight);
            }
        }

        TextView tvStoreName = helper.getView(R.id.tv_store_name);
        TextView tvStoreClass = helper.getView(R.id.tv_store_class);
        ImageView imgStoreFigure = helper.getView(R.id.img_store_figure);
        tvStoreName.setText(item.storeName);
        tvStoreClass.setText(item.className);
//        helper.addOnClickListener()
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.storeFigureImage)).centerCrop().into(imgStoreFigure);
    }
}
