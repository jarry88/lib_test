package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SlantedWidget;

import java.util.List;

/**
 * 產品搜索結果Adapter
 * @author zwm
 */
public class GoodsSearchResultAdapter extends BaseMultiItemQuickAdapter<GoodsSearchItemPair, BaseViewHolder> implements Animation.AnimationListener {
    Context context;
    String currencyTypeSign;

    Animation animation;
    TextView animatingTextView;
    private int mode=Constant.MODE_VIEW;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GoodsSearchResultAdapter(Context context, List<GoodsSearchItemPair> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_DOUBLE_ELEVEN_BANNER, R.layout.double_eleven_header);
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.goods_search_item_pair);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint_new);

        this.context = context;
        currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
    }


    @Override
    protected void convert(BaseViewHolder helper, GoodsSearchItemPair item) {
        switchMode(helper,item);
        int itemType = item.getItemType();
        if (itemType == Constant.ITEM_TYPE_NORMAL) {
            if (item.left != null) {
                ImageView goodsImage = helper.getView(R.id.goods_image_left);
                Glide.with(context).load(StringUtil.normalizeImageUrl(item.left.imageSrc)).centerCrop().into(goodsImage);

                ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar_left);
                if (StringUtil.isEmpty(item.left.storeAvatarUrl)) {
                    Glide.with(context).load(R.drawable.grey_default_avatar).centerCrop().into(imgStoreAvatar);
                } else {
                    Glide.with(context).load(StringUtil.normalizeImageUrl(item.left.storeAvatarUrl)).centerCrop().into(imgStoreAvatar);
                }


                if (!StringUtil.isEmpty(item.left.nationalFlag)) {
                    ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag_right);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(item.left.nationalFlag)).centerCrop().into(imgGoodsNationalFlag);
                    imgGoodsNationalFlag.setVisibility(View.VISIBLE);
                }

                helper.setText(R.id.tv_store_name_left, item.left.storeName);
                helper.setText(R.id.tv_goods_name_left, item.left.goodsName);
                helper.setText(R.id.tv_goods_jingle_left, item.left.jingle);
                helper.setText(R.id.tv_goods_price_left, StringUtil.formatPrice(context, item.left.price, 1,false));

                helper.setGone(R.id.tv_freight_free_left, item.left.isFreightFree)
                        .setGone(R.id.tv_gift_left, item.left.hasGift)
                        .setGone(R.id.tv_discount_left, item.left.hasDiscount);

                SlantedWidget slantedWidget = helper.getView(R.id.slanted_widget_left);
                if (item.left.showDiscountLabel) {
                    slantedWidget.setVisibility(View.VISIBLE);
                    slantedWidget.setDiscountInfo(context,item.left.extendPrice0, item.left.batchPrice0);
                } else {
                    slantedWidget.setVisibility(View.GONE);
                }

                helper.addOnClickListener(R.id.btn_goto_store_left, R.id.cl_container_left);
            }

            if (item.right != null) {
                ImageView goodsImage = helper.getView(R.id.goods_image_right);
                Glide.with(context).load(StringUtil.normalizeImageUrl(item.right.imageSrc)).centerCrop().into(goodsImage);

                ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar_right);
                if (StringUtil.isEmpty(item.right.storeAvatarUrl)) {
                    Glide.with(context).load(R.drawable.grey_default_avatar).centerCrop().into(imgStoreAvatar);
                } else {
                    Glide.with(context).load(StringUtil.normalizeImageUrl(item.right.storeAvatarUrl)).centerCrop().into(imgStoreAvatar);
                }


                if (!StringUtil.isEmpty(item.right.nationalFlag)) {
                    ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag_right);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(item.right.nationalFlag)).centerCrop().into(imgGoodsNationalFlag);
                    imgGoodsNationalFlag.setVisibility(View.VISIBLE);
                }

                helper.setText(R.id.tv_store_name_right, item.right.storeName);
                helper.setText(R.id.tv_goods_name_right, item.right.goodsName);
                helper.setText(R.id.tv_goods_jingle_right, item.right.jingle);
                helper.setText(R.id.tv_goods_price_right, StringUtil.formatPrice(context, item.right.price, 1,false));

                helper.setGone(R.id.tv_freight_free_right, item.right.isFreightFree)
                        .setGone(R.id.tv_gift_right, item.right.hasGift)
                        .setGone(R.id.tv_discount_right, item.right.hasDiscount);

                SlantedWidget slantedWidget = helper.getView(R.id.slanted_widget_right);
                if (item.right.showDiscountLabel) {
                    slantedWidget.setVisibility(View.VISIBLE);
                    slantedWidget.setDiscountInfo(context, item.right.extendPrice0, item.right.batchPrice0);
                } else {
                    slantedWidget.setVisibility(View.GONE);
                }

                helper.addOnClickListener(R.id.btn_goto_store_right, R.id.cl_container_right);
            }


            // 設置右邊item的可見性
            boolean rightHandSideVisible = (item.right != null);
            helper.setGone(R.id.cl_container_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_name_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_jingle_right, rightHandSideVisible)
                    .setGone(R.id.vw_right_bottom_separator, rightHandSideVisible)
                    .setGone(R.id.btn_goto_store_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_price_right, rightHandSideVisible);

        } else if (itemType == Constant.ITEM_TYPE_DOUBLE_ELEVEN_BANNER) {
            helper.addOnClickListener(R.id.btn_play_game)
                    .addOnClickListener(R.id.btn_back);
            long now = System.currentTimeMillis();
            long doubleElevenTimestamp = Jarbon.parse("2019-11-11").getTimestampMillis();

            if (now > doubleElevenTimestamp) {
                helper.setGone(R.id.btn_play_game, true);
            }
        } else {
            // 顯示即可，不用特別處理
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
            Glide.with(context).load("file:///android_asset/christmas/publish_want_post_dynamic.gif").centerCrop().into(iconPublishWantPost);
        }

        int position = helper.getAdapterPosition();
        int itemCount = getItemCount();
        if (itemType == Constant.ITEM_TYPE_NORMAL && position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
            if (position == itemCount - 1) {
                layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
            } else {
                layoutParams.bottomMargin = 0;
            }
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
            animatingTextView.postDelayed(() -> animatingTextView.setVisibility(View.INVISIBLE), 1500);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void setMode(int mode) {
        this.mode = mode;
        this.notifyDataSetChanged();
    }
    public void switchMode(BaseViewHolder helper,GoodsSearchItemPair item) {
        ScaledButton btnLeftCheckGood = helper.getView(R.id.btn_left_select);
        ScaledButton btnRightCheckGood = helper.getView(R.id.btn_right_select);
        if (btnLeftCheckGood == null || btnRightCheckGood == null) {
            return;
        }
        if (mode == Constant.MODE_VIEW) {
            btnLeftCheckGood.setVisibility(View.GONE);
            btnRightCheckGood.setVisibility(View.GONE);
        }else{
            btnLeftCheckGood.setVisibility(View.VISIBLE);
            btnRightCheckGood.setVisibility(View.VISIBLE);
            btnLeftCheckGood.setChecked(item.left.check);
            btnRightCheckGood.setChecked(item.right.check);

        }
            if (mode == Constant.MODE_VIEW) {
                btnLeftCheckGood.setVisibility(View.GONE);
                btnRightCheckGood.setVisibility(View.GONE);
            } else {
                btnLeftCheckGood.setVisibility(View.VISIBLE);
                btnRightCheckGood.setVisibility(View.VISIBLE);
                btnLeftCheckGood.setChecked(item.left.check);
                btnRightCheckGood.setChecked(item.right.check);
            }
    }
}
