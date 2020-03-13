package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class MyLikeArticleAdapter extends BaseQuickAdapter<PostItem, BaseViewHolder> {
    public MyLikeArticleAdapter(int layoutResId, @Nullable List<PostItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem item) {
        ImageView imgPostCover = helper.getView(R.id.img_post_cover);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.coverImage)).centerCrop().into(imgPostCover);

        ImageView imgAuthorAvatar = helper.getView(R.id.img_author_avatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.authorAvatar)).centerCrop().into(imgAuthorAvatar);

        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_author_nickname, item.authorNickname)
                .setText(R.id.tv_thumb_count, String.valueOf(item.postThumb));
    }
}
