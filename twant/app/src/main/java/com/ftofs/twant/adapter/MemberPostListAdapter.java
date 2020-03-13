package com.ftofs.twant.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.List;

/**
 * 個人專頁想要帖列表Adapter
 * @author zwm
 */
public class MemberPostListAdapter extends BaseMultiItemQuickAdapter<PostItem, BaseViewHolder> {

    public MemberPostListAdapter(List<PostItem> data) {
        super(data);
        addItemType(PostItem.POST_TYPE_DEFAULT,R.layout.member_post_list_item);
        addItemType(PostItem.POST_TYPE_WANT,R.layout.member_post_list_item);
        addItemType(PostItem.POST_TYPE_MESSAGE,R.layout.message_post_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem itemData) {
        if(itemData.itemType == PostItem.POST_TYPE_MESSAGE){
            helper.setText(R.id.tv_post_title, itemData.title);

            ImageView imgShopAvatar = helper.getView(R.id.img_shop_avatar);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(itemData.shopAvatar)).centerCrop().into(imgShopAvatar);
            ImageView imgGoods = helper.getView(R.id.goods_image);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(itemData.goodsimage)).centerCrop().into(imgGoods);
            if (!StringUtil.isEmpty(itemData.goodsPrice)) {
                SpannableString sp = new SpannableString(itemData.goodsPrice);
                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(10,true);
                sp.setSpan(absoluteSizeSpan,0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_store_name, itemData.storeName)
                        .setText(R.id.tv_date, itemData.createTime)
                        .setText(R.id.tv_goods_name, itemData.goodsName)
                        .setText(R.id.tv_goods_price, itemData.goodsPrice);
            }
        }else{
            helper.addOnClickListener(R.id.btn_thumb, R.id.btn_fav, R.id.btn_share);

            ImageView coverImageView = helper.getView(R.id.post_cover_image);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(itemData.coverImage)).centerCrop().into(coverImageView);
            helper.setText(R.id.tv_post_title, itemData.title);

            ImageView imgAuthorAvatar = helper.getView(R.id.img_author_avatar);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(itemData.authorAvatar)).centerCrop().into(imgAuthorAvatar);

            helper.setText(R.id.tv_author_nickname, itemData.authorNickname)
                    .setText(R.id.tv_create_time, itemData.createTime)
                    .setText(R.id.tv_thumb_count, String.valueOf(itemData.postThumb))
                    .setText(R.id.tv_reply_count, String.valueOf(itemData.postReply))
                    .setText(R.id.tv_like_count, String.valueOf(itemData.postFollow));
            TextView tvPostSummary = helper.getView(R.id.tv_post_summary);

            if (StringUtil.isEmpty(itemData.coverImage)) {

                tvPostSummary.setText(StringUtil.translateEmoji(mContext, itemData.content, (int) tvPostSummary.getTextSize()));
                coverImageView.setVisibility(View.GONE);
                tvPostSummary.setVisibility(View.VISIBLE);
                if (itemData.comeTrueState == Constant.TRUE_INT) {
                    tvPostSummary.setBackgroundColor(Color.parseColor("#FFFEB809"));
                } else {
                    tvPostSummary.setBackgroundColor(Color.parseColor("#00B0FF"));
                }
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(itemData.coverImage)).centerCrop().into(coverImageView);
                coverImageView.setVisibility(View.VISIBLE);
                tvPostSummary.setVisibility(View.GONE);
            }
            if (itemData.comeTrueState == Constant.TRUE_INT) {
                helper.setGone(R.id.icon_come_true, true);
            } else {
                helper.setGone(R.id.icon_come_true, false);
            }

        }
        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        } else {
            layoutParams.bottomMargin = Util.dip2px(mContext, 10);
        }
    }
}
