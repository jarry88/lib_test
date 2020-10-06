package com.ftofs.twant.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.util.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * 說說列表Adapter
 * @author zwm
 */
public class CommentListAdapter extends BaseMultiItemQuickAdapter<CommentItem, BaseViewHolder> {
    public CommentListAdapter(@Nullable List<CommentItem> data) {
        super( data);

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.comment_item);
        addItemType(Constant.ITEM_TYPE_NO_DATA, R.layout.ic_placeholder_no_data_match_parent);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, CommentItem item) {
        if (item.itemType == Constant.ITEM_TYPE_NO_DATA) {
            return;
        }
        ImageView imgCommenterAvatar = helper.getView(R.id.img_commenter_avatar);
        if(StringUtil.isEmpty(item.commenterAvatar)){
            Glide.with(mContext).load(R.drawable.icon_default_avatar).into(imgCommenterAvatar);
        }else{
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.commenterAvatar)).into(imgCommenterAvatar);
        }

        TextView tvContent = helper.getView(R.id.tv_content);
        tvContent.setText(StringUtil.translateEmoji(mContext, item.content, (int) tvContent.getTextSize()));

        TextView btnReply = helper.getView(R.id.btn_reply);
        String replyText = mContext.getString(R.string.text_reply);
        if (item.commentReply > 0) { // 說說列表，如果是零回覆，則不要顯示那個0
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
        TextView tvRole = helper.getView(R.id.tv_role);
        tvRole.setVisibility(View.VISIBLE);
        switch (item.commentRole) {
            case CommentItem.COMMENT_ROLE_MEMBER:
                tvRole.setBackgroundResource(R.drawable.bg_text_radius_blue);
                tvRole.setText(mContext.getText(R.string.text_member));
                tvRole.setTextColor(mContext.getColor(R.color.tw_blue));
                break;
            case CommentItem.COMMENT_ROLE_BOSS:
                tvRole.setBackgroundResource(R.drawable.bg_text_radius_red);
                tvRole.setTextColor(mContext.getColor(R.color.tw_red));
                tvRole.setText(mContext.getText(R.string.text_boss));
                break;
            case CommentItem.COMMENT_ROLE_CS:
                tvRole.setBackgroundResource(R.drawable.bg_text_radius_red);
                tvRole.setTextColor(mContext.getColor(R.color.tw_red));
                tvRole.setText(mContext.getText(R.string.text_customer));
                break;
            default:
                break;

        }

        helper.setText(R.id.tv_commenter_nickname, item.nickname)
                .setText(R.id.tv_comment_time, item.commentTime)
                .setText(R.id.btn_reply,  replyText)
                .addOnClickListener(R.id.btn_reply, R.id.btn_thumb, R.id.img_commenter_avatar);


        if (item.commentType == Constant.COMMENT_TYPE_TEXT || StringUtil.isEmpty(item.imageUrl)) {
//            helper.setGone(R.id.image_view, false);
            helper.setGone(R.id.cl_first_line, false);
        } else {
            helper.setGone(R.id.cl_first_line, true);

            ImageView imageView1 = helper.getView(R.id.image_view);
            ImageView imageView2 = helper.getView(R.id.img_comment2);
            ImageView imageView3 = helper.getView(R.id.img_comment3);

            if (item.images == null) {
                // 預防item.images為null
                item.images = new String[0];
            }

            if (item.images.length > 0) {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.images[0])).centerCrop().into(imageView1);
                helper.addOnClickListener(R.id.image_view);
                helper.addOnClickListener(R.id.image_view);
            }
            if (item.images.length > 1) {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.images[1])).centerCrop().into(imageView2);
                helper.addOnClickListener(R.id.img_comment2);
                imageView2.setVisibility(View.VISIBLE);
            } else {
                imageView2.setVisibility(View.GONE);
            }
            if (item.images.length > 2) {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.images[2])).centerCrop().into(imageView3);
                imageView3.setVisibility(View.VISIBLE);
            } else {
                imageView2.setVisibility(View.GONE);
            }
//            ImageView imageView1 = helper.getView(R.id.img_comment1);
        }

        ImageView iconThumb = helper.getView(R.id.icon_thumb);
        if (item.isLike == Constant.TRUE_INT) {
            iconThumb.setImageResource(R.drawable.icon_thumb_red_60);
        } else {
            iconThumb.setImageResource(R.drawable.icon_comment_thumb_grey);
        }
    }
}
