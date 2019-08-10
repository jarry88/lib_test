package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CommentReplyItem;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;

/**
 * 評論回復列表Adapter
 * @author zwm
 */
public class CommentReplyListAdapter extends ViewGroupAdapter<CommentReplyItem> {
    Context context;
    int twBlue;

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public CommentReplyListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);

        this.context = context;
        twBlue = context.getResources().getColor(R.color.tw_blue, null);
        addClickableChildrenId(R.id.img_avatar, R.id.btn_reply_comment, R.id.btn_thumb);
    }

    @Override
    public void bindView(int position, View itemView, CommentReplyItem itemData) {
        ImageView imgAvatar = itemView.findViewById(R.id.img_avatar);
        Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.avatarUrl))
                .centerCrop().into(imgAvatar);

        setText(itemView, R.id.tv_nickname, itemData.nickname);
        setText(itemView, R.id.tv_timestamp, new Jarbon(itemData.createTime).format("Y-m-d H:i:s"));

        TextView tvReplyContent = itemView.findViewById(R.id.tv_reply_content);

        Editable content = StringUtil.translateEmoji(context, itemData.content, (int) tvReplyContent.getTextSize());
        if (itemData.isQuoteReply) {
            SpannableString spannableString = new SpannableString(" //@" + itemData.quoteNickname + ":");
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(twBlue);
            spannableString.setSpan(colorSpan, 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            content.append(spannableString);
            content.append(StringUtil.translateEmoji(context, itemData.quoteContent, (int) tvReplyContent.getTextSize()));
        }
        tvReplyContent.setText(content);

        setText(itemView, R.id.tv_like_count, String.valueOf(itemData.commentLike));

        ImageView iconCommentThumb = itemView.findViewById(R.id.icon_comment_thumb);
        if (itemData.isLike == 1) {
            iconCommentThumb.setImageResource(R.drawable.icon_comment_thumb_blue);
        } else {
            iconCommentThumb.setImageResource(R.drawable.icon_comment_thumb_grey);
        }
    }
}
