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
import com.ftofs.twant.entity.JobInfoItem;
import com.ftofs.twant.entity.StoreSearchItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.text.DecimalFormat;
import java.util.List;

public class StoreSearchResultAdapter extends BaseMultiItemQuickAdapter<StoreSearchItem, BaseViewHolder> implements Animation.AnimationListener  {
    Animation animation;
    TextView animatingTextView;

    private boolean showJobInfo;

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
        addItemType(Constant.ITEM_TYPE_NO_DATA, R.layout.ic_placeholder_no_data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreSearchItem item) {
        if (item.getItemType() == Constant.ITEM_TYPE_NORMAL) {
            helper.addOnClickListener( R.id.btn_shop_hr);
            SLog.info("item.storeAvatarUrl[%s]", item.storeAvatarUrl);
            String storeAvatarUrl = StringUtil.normalizeImageUrl(item.storeAvatarUrl);
            ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);
            if ("https://192.168.5.29/public/img/default_store_avatar.png".equals(storeAvatarUrl)) {
                Glide.with(mContext).load(R.drawable.default_store_avatar).into(imgStoreAvatar);
            } else {
                Glide.with(mContext).load(item.storeAvatarUrl).into(imgStoreAvatar);
            }

            ImageView imgCustomerAvatar = helper.getView(R.id.btn_customer);
            if(item.staff != null){
                helper.addOnClickListener(R.id.btn_customer);
                imgCustomerAvatar.setVisibility(View.VISIBLE);
            } else {
                imgCustomerAvatar.setVisibility(View.GONE);
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
            if(StringUtil.isEmpty(item.storeFigureImage)){
                Glide.with(mContext).load(R.drawable.store_figure_default).centerCrop().into(imgStoreFigure);
            }
            else{
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.storeFigureImage)).centerCrop().into(imgStoreFigure);
            }

            if (StringUtil.isEmpty(item.storeVideoUrl)) {
                helper.setGone(R.id.btn_play, false);
            } else {
                helper.setGone(R.id.btn_play, true);
                helper.addOnClickListener(R.id.btn_play);
            }

            TextView tvDistance = helper.getView(R.id.tv_distance);
            if (item.distance < Constant.STORE_DISTANCE_THRESHOLD) {
                // 如果distance為0，則隱藏距離信息
                tvDistance.setVisibility(View.GONE);
            } else {
                DecimalFormat df = new DecimalFormat("#.#");
                String distanceText = df.format(item.distance/1000) + "km";
                tvDistance.setText(distanceText);
                // tvDistance.setVisibility(View.VISIBLE);  距離不要
            }

            helper.setText(R.id.tv_shop_open_day, String.valueOf(item.shopDay));
            helper.setText(R.id.tv_goods_common_count, String.valueOf(item.goodsCommonCount));
            helper.setText(R.id.tv_like_count, String.valueOf(item.likeCount));
            helper.setText(R.id.tv_shop_follow_count, String.valueOf(item.followCount));
            String storeViewCountText = StringUtil.formatPostView(item.storeView);
            helper.setText(R.id.tv_shop_view_count, storeViewCountText);

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
            if (item.listCount==0) {
                helper.setGone(R.id.btn_shop_hr, false);
            } else{
                helper.setVisible(R.id.btn_shop_hr, true);
            }
                if (item.showJobInfo && item.listCount>0) {

                helper.setGone(R.id.ll_recruitment_list, true)
                        .setGone(R.id.img_store_job_list_mask, true);

                JobInfoItem jobInfoItem = item.jobList.get(0);

                helper.setText(R.id.tv_job_title1, jobInfoItem.postName)
                        .setText(R.id.tv_salary1, jobInfoItem.salaryRange + "/" + jobInfoItem.salaryType);

                if (item.jobList.size() >= 2) {
                    jobInfoItem = item.jobList.get(1);
                    helper.setGone(R.id.rl_recruitment2, true)
                            .setText(R.id.tv_job_title2, jobInfoItem.postName)
                            .setText(R.id.tv_salary2, jobInfoItem.salaryRange + "/" + jobInfoItem.salaryType);
                } else {
                    helper.setGone(R.id.rl_recruitment2, false);
                }
            } else {
                helper.setGone(R.id.ll_recruitment_list, false)
                        .setGone(R.id.img_store_job_list_mask, false);
            }
        } else if (item.getItemType() == Constant.ITEM_TYPE_NO_DATA){

        } else{
                //  加載發表想要的入口，不需要做特別處理
                if (animation == null) {
                    animation = AnimationUtils.loadAnimation(mContext, R.anim.takewant_message);
                    animation.setAnimationListener(this);
                }
                animatingTextView = helper.getView(R.id.tv_load_end_hint_bubble);
                if (item.animShowStatus == Constant.ANIM_SHOWING) {
                    item.animShowStatus = Constant.ANIM_SHOWN;
                    animatingTextView.startAnimation(animation);
                }

                ImageView iconPublishWantPost = helper.getView(R.id.icon_publish_want_post);
                Glide.with(mContext).load("file:///android_asset/christmas/publish_want_post_dynamic.gif").centerCrop().into(iconPublishWantPost);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        SLog.info("onAnimationEnd");
        if (animatingTextView != null) {
            animatingTextView.setVisibility(View.VISIBLE);
            animatingTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animatingTextView.setVisibility(View.INVISIBLE);
                }
            }, 1500);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

