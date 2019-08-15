package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * Rv表示RecyclerView
 * 個人專頁貼文列表Adapter
 */
public class RvMemberPostListAdapter extends BaseQuickAdapter<PostItem, BaseViewHolder> {
    public RvMemberPostListAdapter(int layoutResId, @Nullable List<PostItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem item) {
        ImageView postCoverImage = helper.getView(R.id.post_cover_image);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.coverImage)).centerCrop().into(postCoverImage);

        helper.setText(R.id.tv_post_title, item.postCategory + " | " + item.title);

        ImageView imgAuthorAvatar = helper.getView(R.id.img_author_avatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.authorAvatar)).centerCrop().into(imgAuthorAvatar);

        helper.setText(R.id.tv_author_nickname, item.authorNickname)
            .setText(R.id.tv_create_time, item.createTime)
            .setText(R.id.tv_thumb_count, String.valueOf(item.postThumb))
            .setText(R.id.tv_reply_count, String.valueOf(item.postReply))
            .setText(R.id.tv_like_count, String.valueOf(item.postLike));

    }
}
