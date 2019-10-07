package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class MemberPostListAdapter extends BaseQuickAdapter<PostItem, BaseViewHolder> {

    public MemberPostListAdapter(int layoutResId, @Nullable List<PostItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem itemData) {
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

        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        }
    }
}
