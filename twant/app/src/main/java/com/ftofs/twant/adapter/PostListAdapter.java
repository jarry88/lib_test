package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwProgressBar;

import java.util.List;

public class PostListAdapter extends BaseMultiItemQuickAdapter<PostItem, BaseViewHolder> {
    public PostListAdapter(@Nullable List<PostItem> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.post_list_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem item) {
        int itemType = item.getItemType();
        if (itemType == Constant.ITEM_TYPE_NORMAL) {
            ImageView coverImage = helper.getView(R.id.post_cover_image);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.coverImage)).centerCrop().into(coverImage);

            ImageView authorAvatar = helper.getView(R.id.img_author_avatar);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.authorAvatar)).centerCrop().into(authorAvatar);

            helper.setText(R.id.tv_title, String.format("%s | %s", item.postCategory, item.title))
                    .setText(R.id.tv_author_nickname, item.authorNickname)
                    .setText(R.id.tv_like_count, String.valueOf(item.postFollow))
                    .setText(R.id.tv_comment_count, String.valueOf(item.postReply))
                    .setText(R.id.tv_category_name, item.postCategory);

            helper.addOnClickListener(R.id.btn_thumb);
            ImageView iconThumb = helper.getView(R.id.icon_thumb);
            if (item.isLike == 1) {
                iconThumb.setImageResource(R.drawable.icon_comment_thumb_blue);
            } else {
                iconThumb.setImageResource(R.drawable.icon_comment_thumb_grey);
            }

            if (!StringUtil.isEmpty(item.deadline)) {
                helper.setText(R.id.tv_deadline, item.deadline.substring(5));
                TwProgressBar progressBar = helper.getView(R.id.pg_deadline);
                Jarbon jarbonDeadline = Jarbon.parse(item.deadline);
                Jarbon now = new Jarbon();
                int diffInDays = now.diffInDays(jarbonDeadline);
                if (diffInDays < 3) {
                    progressBar.setColor(TwProgressBar.COLOR_RED);
                } else if (3 <= diffInDays && diffInDays <= 7) {
                    progressBar.setColor(TwProgressBar.COLOR_ORANGE);
                } else {
                    progressBar.setColor(TwProgressBar.COLOR_GREEN);
                }
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
        } else if (itemType == Constant.ITEM_TYPE_LOAD_END_HINT){
            // 顯示即可，不用特別處理
        }
    }
}
