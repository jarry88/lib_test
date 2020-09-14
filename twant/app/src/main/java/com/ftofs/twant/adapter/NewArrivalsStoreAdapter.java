package com.ftofs.twant.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreItem;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class NewArrivalsStoreAdapter extends BaseMultiItemQuickAdapter<StoreItem, BaseViewHolder> {
    public NewArrivalsStoreAdapter(@Nullable List<StoreItem> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.store_view);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreItem item) {
        int itemType = item.getItemType();
        if (itemType == Constant.ITEM_TYPE_NORMAL) {
            helper.addOnClickListener(R.id.goods_image_left_container, R.id.goods_image_middle_container,
                    R.id.goods_image_right_container);
            helper.setText(R.id.tv_store_name, item.storeName)
                    .setText(R.id.tv_store_class, item.storeClass);


            ImageView imgStoreFigure = helper.getView(R.id.img_store_figure);
            if (StringUtil.isEmpty(item.storeFigureImage)) {
                Glide.with(mContext).load(R.drawable.store_figure_default).centerCrop().into(imgStoreFigure);
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.storeFigureImage)).centerCrop().into(imgStoreFigure);
            }
            // 商店的3個產品展示
            SLog.info("name%s,goodsList%s,goodsList[%d]", item.storeName, item.goodsList.toString(),item.goodsList.size());

            ImageView goodsImageLeft = helper.getView(R.id.goods_image_left);
            ImageView goodsImageMiddle = helper.getView(R.id.goods_image_middle);
            ImageView goodsImageRight = helper.getView(R.id.goods_image_right);

            if (item.storeName.equals("測試拒絕開店")) {
                SLog.info("here++++++21323");
            }

            helper.setGone(R.id.goods_image_left_container, false)
                .setGone(R.id.goods_image_middle_container, false)
                    .setGone(R.id.goods_image_right_container, false);
            for (int i=0;i<item.goodsList.size();i++ ) {
                SLog.info("name%saa", item.storeName);
                String imageSrc = StringUtil.normalizeImageUrl(item.goodsList.get(i).imageUrl);

                if (i == 0) {
                    if (item.storeName.equals("測試拒絕開店")) {
                        SLog.info("here21323");
                    }
                        helper.getView(R.id.goods_image_left_container).setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(imageSrc).centerCrop().into(goodsImageLeft);
                }
                else if (i== 1) {
                    helper.getView(R.id.goods_image_middle_container).setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(imageSrc).centerCrop().into(goodsImageMiddle);
                } else if (i == 2) {
                    helper.getView(R.id.goods_image_right_container).setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(imageSrc).centerCrop().into(goodsImageRight);
                }


            }
        } else if (itemType == Constant.ITEM_TYPE_LOAD_END_HINT) {

        }

    }
}
