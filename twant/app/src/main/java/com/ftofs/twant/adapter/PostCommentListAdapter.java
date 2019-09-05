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
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.entity.CommentReplyItem;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;

/**
 * 貼文評論列表Adapter
 * @author zwm
 */
public class PostCommentListAdapter extends ViewGroupAdapter<CommentItem> {
    Context context;
    int twBlue;

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public PostCommentListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);

        this.context = context;
        twBlue = context.getResources().getColor(R.color.tw_blue, null);
        addClickableChildrenId(R.id.btn_reply, R.id.btn_thumb, R.id.img_commenter_avatar);
    }

    @Override
    public void bindView(int position, View itemView, CommentItem itemData) {
        ImageView imgCommenterAvatar = itemView.findViewById(R.id.img_commenter_avatar);
        Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.commenterAvatar)).centerCrop().into(imgCommenterAvatar);

        TextView tvContent = itemView.findViewById(R.id.tv_content);
        tvContent.setText(StringUtil.translateEmoji(context, itemData.content, (int) tvContent.getTextSize()));

        setText(itemView, R.id.tv_commenter_nickname, itemData.nickname);
        setText(itemView, R.id.tv_comment_time, itemData.commentTime);
        setText(itemView, R.id.btn_reply, context.getString(R.string.text_reply) + " " + itemData.commentReply);
        setText(itemView, R.id.tv_thumb_count, String.valueOf(itemData.commentLike));

        if (itemData.commentType == Constant.COMMENT_TYPE_TEXT || StringUtil.isEmpty(itemData.imageUrl)) {
            itemView.findViewById(R.id.image_view).setVisibility(View.GONE);
        } else {
            ImageView imageView = itemView.findViewById(R.id.image_view);
            Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.imageUrl)).centerCrop().into(imageView);
            itemView.findViewById(R.id.image_view).setVisibility(View.VISIBLE);
        }

        ImageView iconThumb = itemView.findViewById(R.id.icon_thumb);
        if (itemData.isLike == 1) {
            iconThumb.setImageResource(R.drawable.icon_comment_thumb_blue);
        } else {
            iconThumb.setImageResource(R.drawable.icon_comment_thumb_grey);
        }
    }
}
