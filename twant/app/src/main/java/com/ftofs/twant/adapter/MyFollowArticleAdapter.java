package com.ftofs.twant.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class MyFollowArticleAdapter extends MyFollowAdapter<PostItem, BaseViewHolder> {
    public MyFollowArticleAdapter(int layoutResId, @Nullable List<PostItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem item) {
        ImageView imgPostCover = helper.getView(R.id.img_post_cover);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.coverImage)).centerCrop().into(imgPostCover);

        ImageView imgAuthorAvatar = helper.getView(R.id.img_author_avatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.authorAvatar)).centerCrop().into(imgAuthorAvatar);

        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_author_nickname, item.authorNickname+"·"+item.createTime)
                .setText(R.id.tv_follow_count, String.valueOf(item.postFollow))
                .setText(R.id.tv_like_count,String.valueOf(item.postLike))
                .setText(R.id.tv_look_count,String.valueOf(item.postView));
        if (item.comeTrueState == 1) {
            helper.getView(R.id.img_true_state).setVisibility(View.VISIBLE);
        }
        if (item.isDelete == 1) {
            helper.getView(R.id.tv_unuseful).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_follow_count).setVisibility(View.GONE);
            helper.getView(R.id.tv_look_count).setVisibility(View.GONE);
            helper.getView(R.id.tv_like_count).setVisibility(View.GONE);
            helper.getView(R.id.icon_like).setVisibility(View.GONE);
            helper.getView(R.id.icon_follow).setVisibility(View.GONE);
            helper.getView(R.id.icon_view).setVisibility(View.GONE);
        }
        switch (item.itemType) {
            case PostItem.POST_TYPE_MY_FOLLOW:
                helper.getView(R.id.tv_follow_count).setVisibility(View.VISIBLE);
                helper.getView(R.id.img_author_avatar).setVisibility(View.VISIBLE);
                helper.getView(R.id.icon_follow).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_like_count).setVisibility(View.GONE);
                helper.getView(R.id.tv_look_count).setVisibility(View.GONE);
                helper.getView(R.id.icon_like).setVisibility(View.GONE);
                helper.getView(R.id.icon_view).setVisibility(View.GONE);
                helper.setText(R.id.tv_author_nickname, item.authorNickname);
                break;
            default:
                break;
        }

        super.switchMode(helper, item);
    }
}
