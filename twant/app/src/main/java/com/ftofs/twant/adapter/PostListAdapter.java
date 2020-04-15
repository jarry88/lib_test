package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.List;

public class PostListAdapter extends BaseMultiItemQuickAdapter<PostItem, BaseViewHolder> implements Animation.AnimationListener {
    Animation animation;
    TextView animatingTextView;

    int twBlue;
    int twYellow;

    public PostListAdapter(Context context, @Nullable List<PostItem> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.post_list_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint_new);

        twBlue = context.getColor(R.color.tw_blue);
        twYellow = context.getColor(R.color.tw_yellow);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem item) {
        int itemType = item.getItemType();
        if (itemType == Constant.ITEM_TYPE_NORMAL) {
            ImageView coverImage = helper.getView(R.id.post_cover_image);
            TextView tvPostSummary = helper.getView(R.id.tv_post_summary);
            SLog.info("item[%s],coverImage[%s]", item.toString(),item.coverImage);
            if (StringUtil.isEmpty(item.coverImage)) {
                tvPostSummary.setText(StringUtil.translateEmoji(mContext, item.content, (int) tvPostSummary.getTextSize()));
                coverImage.setVisibility(View.GONE);
                tvPostSummary.setVisibility(View.VISIBLE);
                if (item.comeTrueState == Constant.TRUE_INT) {
                    tvPostSummary.setBackgroundColor(twYellow);
                } else {
                    tvPostSummary.setBackgroundColor(twBlue);
                }
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.coverImage)).centerCrop().into(coverImage);
                coverImage.setVisibility(View.VISIBLE);
                tvPostSummary.setVisibility(View.GONE);
            }


            ImageView authorAvatar = helper.getView(R.id.img_author_avatar);
            if (StringUtil.isEmpty(item.authorAvatar)) {
                Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(authorAvatar);
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.authorAvatar)).centerCrop().into(authorAvatar);
            }

            String createTime= Jarbon.parse(item.createTime).format("y年n月j日 H:i");
            String postViewText = StringUtil.formatPostView(item.postView);
            helper.setText(R.id.tv_title, String.format("%s | %s", item.postCategory, item.title))
                    .setText(R.id.tv_author_nickname, item.authorNickname)
                    .setText(R.id.tv_like_count, String.valueOf(item.postFollow))
                    .setText(R.id.tv_comment_count, String.valueOf(item.postReply))
                    .setText(R.id.tv_view_count, postViewText)
                    .setText(R.id.tv_create_time,createTime);

            if (item.comeTrueState == Constant.TRUE_INT) {
                helper.setGone(R.id.icon_come_true, true)
                        .setBackgroundRes(R.id.ll_header_container, R.drawable.post_item_header_bg_achieved)
                        .setBackgroundRes(R.id.fl_author_avatar_container, R.drawable.post_item_avatar_bg_achieved);
            } else {
                helper.setGone(R.id.icon_come_true, false)
                        .setBackgroundRes(R.id.ll_header_container, R.drawable.post_item_header_bg_normal)
                        .setBackgroundRes(R.id.fl_author_avatar_container, R.drawable.post_item_avatar_bg_normal);
            }

            int itemCount = getItemCount();
            int position = helper.getAdapterPosition();
            // 最后一項，設置大一點的bottomMargin
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
            if (position == itemCount - 1) {
                layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
            } else {
                layoutParams.bottomMargin = 0;
            }
        } else if (itemType == Constant.ITEM_TYPE_LOAD_END_HINT) {
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


