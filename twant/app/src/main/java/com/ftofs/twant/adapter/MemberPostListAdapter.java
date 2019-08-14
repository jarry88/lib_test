package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.util.StringUtil;

public class MemberPostListAdapter extends ViewGroupAdapter<PostItem> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public MemberPostListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, PostItem itemData) {
        ImageView coverImageView = itemView.findViewById(R.id.post_cover_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.coverImage)).centerCrop().into(coverImageView);

        setText(itemView, R.id.tv_post_title, itemData.title);

        ImageView imgAuthorAvatar = itemView.findViewById(R.id.img_author_avatar);
        Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.authorAvatar)).centerCrop().into(imgAuthorAvatar);

        setText(itemView, R.id.tv_author_nickname, itemData.authorNickname);
        setText(itemView, R.id.tv_create_time, itemData.createTime);
        setText(itemView, R.id.tv_thumb_count, String.valueOf(itemData.postThumb));
        setText(itemView, R.id.tv_reply_count, String.valueOf(itemData.postReply));
        setText(itemView, R.id.tv_like_count, String.valueOf(itemData.postLike));
    }
}
