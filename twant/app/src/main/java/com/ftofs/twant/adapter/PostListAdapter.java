package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwProgressBar;

import java.util.List;

public class PostListAdapter extends BaseMultiItemQuickAdapter<PostItem, BaseViewHolder> implements Animation.AnimationListener  {
    Animation animation;
    ImageView animatingImageView;

    public PostListAdapter(@Nullable List<PostItem> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.post_list_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint_new);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem item) {
        int itemType = item.getItemType();
        if (itemType == Constant.ITEM_TYPE_NORMAL) {
            ImageView coverImage = helper.getView(R.id.post_cover_image);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.coverImage)).centerCrop().into(coverImage);

            ImageView authorAvatar = helper.getView(R.id.img_author_avatar);
            if (StringUtil.isEmpty(item.authorAvatar)) {
                Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(authorAvatar);
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.authorAvatar)).centerCrop().into(authorAvatar);
            }


            helper.setText(R.id.tv_title, String.format("%s | %s", item.postCategory, item.title))
                    .setText(R.id.tv_author_nickname, item.authorNickname)
                    .setText(R.id.tv_like_count, String.valueOf(item.postFollow))
                    .setText(R.id.tv_comment_count, String.valueOf(item.postReply))
                    .setText(R.id.tv_view_count, String.valueOf(item.postView))
                    .setText(R.id.tv_create_time, item.createTime);

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
            SLog.info("HERE");
            if (animation == null) {
                animation = AnimationUtils.loadAnimation(mContext, R.anim.takewant_message);
                animation.setAnimationListener(this);
            }
            animatingImageView = helper.getView(R.id.img_load_end_hint_bubble);
            SLog.info("HERE");
            if (item.animShowStatus == PostItem.ANIM_SHOWING) {
                SLog.info("HERE");
                item.animShowStatus = PostItem.ANIM_SHOWN;
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


