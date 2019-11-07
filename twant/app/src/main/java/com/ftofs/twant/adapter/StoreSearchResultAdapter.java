package com.ftofs.twant.adapter;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreSearchItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class StoreSearchResultAdapter extends BaseMultiItemQuickAdapter<StoreSearchItem, BaseViewHolder> implements Animation.AnimationListener  {
    Animation animation;
    ImageView animatingImageView;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public StoreSearchResultAdapter(List<StoreSearchItem> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.store_search_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint_new);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreSearchItem item) {
        if (item.getItemType() == Constant.ITEM_TYPE_NORMAL) {
            ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);
            SLog.info("item.storeAvatarUrl[%s]", item.storeAvatarUrl);
            String storeAvatarUrl = StringUtil.normalizeImageUrl(item.storeAvatarUrl);
            if ("https://192.168.5.29/public/img/default_store_avatar.png".equals(storeAvatarUrl)) {
                Glide.with(mContext).load(R.drawable.default_store_avatar).into(imgStoreAvatar);
            } else {
                Glide.with(mContext).load(item.storeAvatarUrl).into(imgStoreAvatar);
            }


            helper.setText(R.id.tv_store_name, item.storeName);
            String mainBusiness = "";
            if (!StringUtil.isEmpty(item.mainBusiness)) {
                mainBusiness = mContext.getString(R.string.text_main_business) + ": " + item.mainBusiness;
            }
            helper.setText(R.id.tv_main_business, mainBusiness);

            if (StringUtil.isEmpty(item.className)) {
                helper.setGone(R.id.tv_store_class, false);
            } else {
                helper.setText(R.id.tv_store_class, item.className);
                helper.setGone(R.id.tv_store_class, true);
            }


            ImageView imgStoreFigure = helper.getView(R.id.img_store_figure);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.storeFigureImage)).centerCrop().into(imgStoreFigure);

            TextView tvDistance = helper.getView(R.id.tv_distance);
            if (item.distance < Constant.STORE_DISTANCE_THRESHOLD) {
                // 如果distance為0，則隱藏距離信息
                tvDistance.setVisibility(View.GONE);
            } else {
                String distanceText = item.distance + "km";
                tvDistance.setText(distanceText);
            }

            helper.setText(R.id.tv_shop_open_day, String.valueOf(item.shopDay));
            helper.setText(R.id.tv_goods_common_count, String.valueOf(item.goodsCommonCount));
            helper.setText(R.id.tv_like_count, String.valueOf(item.likeCount));

            for (int i = 0; i < item.goodsImageList.size(); i++) {
                String url = item.goodsImageList.get(i);
                if (i == 0) {
                    ImageView goodsImageLeft = helper.getView(R.id.goods_image_left);
                    Glide.with(mContext).load(url).centerCrop().into(goodsImageLeft);
                    helper.setGone(R.id.goods_image_left_container, true);
                } else if (i == 1) {
                    ImageView goodsImageMiddle = helper.getView(R.id.goods_image_middle);
                    Glide.with(mContext).load(url).centerCrop().into(goodsImageMiddle);
                    helper.setGone(R.id.goods_image_middle_container, true);
                } else {
                    ImageView goodsImageRight = helper.getView(R.id.goods_image_right);
                    Glide.with(mContext).load(url).centerCrop().into(goodsImageRight);
                    helper.setGone(R.id.goods_image_right_container, true);
                }
            }
        } else {
            //  加載發表想要的入口，不需要做特別處理
            if (animation == null) {
                animation = AnimationUtils.loadAnimation(mContext, R.anim.takewant_message);
                animation.setAnimationListener(this);
            }
            animatingImageView = helper.getView(R.id.img_load_end_hint_bubble);
            if (item.animShowStatus == Constant.ANIM_SHOWING) {
                item.animShowStatus = Constant.ANIM_SHOWN;
                animatingImageView.startAnimation(animation);
            }
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        SLog.info("onAnimationEnd");
        if (animatingImageView != null) {
            animatingImageView.setVisibility(View.VISIBLE);
            animatingImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animatingImageView.setVisibility(View.INVISIBLE);
                }
            }, 1500);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

