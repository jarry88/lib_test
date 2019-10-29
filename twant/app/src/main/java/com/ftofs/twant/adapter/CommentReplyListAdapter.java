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
import com.ftofs.twant.fragment.ImageViewerFragment;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;

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
        if (StringUtil.isEmpty(itemData.avatarUrl)) {
            Glide.with(context).load(R.drawable.grey_default_avatar)
                    .centerCrop().into(imgAvatar);
        } else {
            Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.avatarUrl))
                    .centerCrop().into(imgAvatar);
        }


        setText(itemView, R.id.tv_nickname, itemData.nickname);
        setText(itemView, R.id.tv_timestamp, new Jarbon(itemData.createTime).format("Y-m-d H:i:s"));

        TextView tvReplyContent = itemView.findViewById(R.id.tv_reply_content);

        if (StringUtil.isEmpty(itemData.content)) {
            tvReplyContent.setVisibility(View.GONE);
        } else {
            tvReplyContent.setVisibility(View.VISIBLE);

            Editable content = StringUtil.translateEmoji(context, itemData.content, (int) tvReplyContent.getTextSize());
            if (itemData.isQuoteReply) {
                SpannableString spannableString = new SpannableString(" //@" + itemData.quoteNickname + ":");
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(twBlue);
                spannableString.setSpan(colorSpan, 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                content.append(spannableString);
                content.append(StringUtil.translateEmoji(context, itemData.quoteContent, (int) tvReplyContent.getTextSize()));
            }
            tvReplyContent.setText(content);
        }

        // 如果評論有圖片
        if (itemData.imageList.size() > 0) {
            SquareGridLayout sglImageContainer = itemView.findViewById(R.id.sgl_image_container);
            sglImageContainer.removeAllViews();

            for (String imageUrl : itemData.imageList) {
                ImageView imageView = new ImageView(context);
                Glide.with(context).load(StringUtil.normalizeImageUrl(imageUrl)).centerCrop().into(imageView);

                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.topMargin = Util.dip2px(context, 3);
                layoutParams.bottomMargin = Util.dip2px(context, 3);
                layoutParams.leftMargin = Util.dip2px(context, 3);
                layoutParams.rightMargin = Util.dip2px(context, 3);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.startFragment(ImageViewerFragment.newInstance(StringUtil.normalizeImageUrl(imageUrl)));
                    }
                });

                sglImageContainer.addView(imageView, layoutParams);
            }
        }


        setText(itemView, R.id.tv_like_count, String.valueOf(itemData.commentLike));

        ImageView iconCommentThumb = itemView.findViewById(R.id.icon_comment_thumb);
        if (itemData.isLike == 1) {
            iconCommentThumb.setImageResource(R.drawable.icon_comment_thumb_blue);
        } else {
            iconCommentThumb.setImageResource(R.drawable.icon_comment_thumb_grey);
        }
    }
}
