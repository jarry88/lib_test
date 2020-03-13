package com.ftofs.twant.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * Rv表示RecyclerView
 * 個人專頁想要帖列表Adapter
 */
public class RvMemberPostListAdapter extends BaseMultiItemQuickAdapter<PostItem, BaseViewHolder> {
    public RvMemberPostListAdapter(@Nullable List<PostItem> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.member_post_list_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem item) {
        if (item.getItemType() == Constant.ITEM_TYPE_NORMAL) {
            ImageView coverImage = helper.getView(R.id.post_cover_image);
            TextView tvPostSummary = helper.getView(R.id.tv_post_summary);

            if (StringUtil.isEmpty(item.coverImage)) {
                tvPostSummary.setText(StringUtil.translateEmoji(mContext, item.content, (int) tvPostSummary.getTextSize()));
                coverImage.setVisibility(View.GONE);
                tvPostSummary.setVisibility(View.VISIBLE);
                if (item.comeTrueState == Constant.TRUE_INT) {
                    tvPostSummary.setBackgroundColor(Color.parseColor("#FFFEB809"));
                } else {
                    tvPostSummary.setBackgroundColor(Color.parseColor("#00B0FF"));
                }
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.coverImage)).centerCrop().into(coverImage);
                coverImage.setVisibility(View.VISIBLE);
                tvPostSummary.setVisibility(View.GONE);
            }
            if (item.comeTrueState == Constant.TRUE_INT) {
                helper.setGone(R.id.icon_come_true, true);
            } else {
                helper.setGone(R.id.icon_come_true, false);
            }

            helper.setText(R.id.tv_post_title, item.postCategory + " | " + item.title);

            ImageView imgAuthorAvatar = helper.getView(R.id.img_author_avatar);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.authorAvatar)).centerCrop().into(imgAuthorAvatar);

            helper.setText(R.id.tv_author_nickname, item.authorNickname)
                    .setText(R.id.tv_create_time, item.createTime)
                    .setText(R.id.tv_thumb_count, String.valueOf(item.postThumb))
                    .setText(R.id.tv_reply_count, String.valueOf(item.postReply))
                    .setText(R.id.tv_like_count, String.valueOf(item.postFollow));
        } else {

        }
    }
}
