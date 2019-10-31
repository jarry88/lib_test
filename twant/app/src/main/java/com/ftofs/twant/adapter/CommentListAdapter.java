package com.ftofs.twant.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;


/**
 * 評論列表Adapter
 * @author zwm
 */
public class CommentListAdapter extends BaseQuickAdapter<CommentItem, BaseViewHolder> {

    public CommentListAdapter(int layoutResId, @Nullable List<CommentItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentItem item) {
        ImageView imgCommenterAvatar = helper.getView(R.id.img_commenter_avatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.commenterAvatar)).centerCrop().into(imgCommenterAvatar);

        TextView tvContent = helper.getView(R.id.tv_content);
        tvContent.setText(StringUtil.translateEmoji(mContext, item.content, (int) tvContent.getTextSize()));

        TextView btnReply = helper.getView(R.id.btn_reply);
        String replyText = mContext.getString(R.string.text_reply);
        if (item.commentReply > 0) { // 評論列表，如果是零回復，則不要顯示那個0
            replyText += (" " + item.commentReply);
            btnReply.setBackgroundResource(R.drawable.reply_number_bg);
        } else {
            btnReply.setBackground(null);
        }
        btnReply.setText(replyText);

        TextView tvThumbCount = helper.getView(R.id.tv_thumb_count);
        if (item.commentLike > 0) {
            tvThumbCount.setText(String.valueOf(item.commentLike));
            tvThumbCount.setVisibility(View.VISIBLE);
        } else {
            tvThumbCount.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_commenter_nickname, item.nickname)
                .setText(R.id.tv_comment_time, item.commentTime)
                .setText(R.id.btn_reply,  replyText)
                .addOnClickListener(R.id.btn_reply, R.id.btn_thumb, R.id.img_commenter_avatar);


        if (item.commentType == Constant.COMMENT_TYPE_TEXT || StringUtil.isEmpty(item.imageUrl)) {
            helper.setGone(R.id.image_view, false);
        } else {
            ImageView imageView = helper.getView(R.id.image_view);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.imageUrl)).centerCrop().into(imageView);
            helper.setVisible(R.id.image_view, true);
        }

        ImageView iconThumb = helper.getView(R.id.icon_thumb);
        if (item.isLike == 1) {
            iconThumb.setImageResource(R.drawable.icon_thumb_red_64);
        } else {
            iconThumb.setImageResource(R.drawable.icon_comment_thumb_grey);
        }
    }
}
