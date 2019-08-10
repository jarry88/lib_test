package com.ftofs.twant.adapter;

import android.content.Context;
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
        tvReplyContent.setText(StringUtil.translateEmoji(context, itemData.content, (int) tvReplyContent.getTextSize()));
        setText(itemView, R.id.tv_like_count, String.valueOf(itemData.commentLike));

        ImageView iconCommentThumb = itemView.findViewById(R.id.icon_comment_thumb);
        if (itemData.isLike == 1) {
            iconCommentThumb.setImageResource(R.drawable.icon_comment_thumb_blue);
        } else {
            iconCommentThumb.setImageResource(R.drawable.icon_comment_thumb_grey);
        }
    }
}
