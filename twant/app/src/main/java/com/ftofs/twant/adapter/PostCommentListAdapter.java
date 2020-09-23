package com.ftofs.twant.adapter;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 想要帖評論列表Adapter
 * @author zwm
 */
public class PostCommentListAdapter extends BaseQuickAdapter<CommentItem, BaseViewHolder> implements SimpleCallback {
    Context context;
    int twBlue;
    boolean isComeTrue;

    /**
     * 構造方法
     *
     * @param itemLayoutId itemView的布局Id
     */
    public PostCommentListAdapter( Context context,int itemLayoutId,List<CommentItem> data) {
        super(itemLayoutId,data);

        this.context = context;
        twBlue = context.getResources().getColor(R.color.tw_blue, null);
    }


    public void setComeTrue(boolean isComeTrue) {
        this.isComeTrue = isComeTrue;
    }

    @Override
    public void onSimpleCall(Object data) {
        SLog.info("data[%s]", data);

        String url = (String) data;
        StringUtil.parseCustomUrl(context, url);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CommentItem itemData) {
        helper.addOnClickListener(R.id.image_view, R.id.btn_reply, R.id.btn_thumb, R.id.img_commenter_avatar, R.id.btn_make_true);
        View itemView = helper.itemView;
        ImageView imgCommenterAvatar = itemView.findViewById(R.id.img_commenter_avatar);
        if (StringUtil.isEmpty(itemData.commenterAvatar)) {
            Glide.with(context).load(R.drawable.grey_default_avatar).centerCrop().into(imgCommenterAvatar);
        } else {
            Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.commenterAvatar)).centerCrop().into(imgCommenterAvatar);
        }


        TextView tvContent = itemView.findViewById(R.id.tv_content);
        if (StringUtil.isEmpty(itemData.content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            tvContent.setText(StringUtil.translateEmoji(context, itemData.content, (int) tvContent.getTextSize(), this));
            tvContent.setVisibility(View.VISIBLE);
        }


        TextView btnReply = itemView.findViewById(R.id.btn_reply);
        String commentReply = context.getString(R.string.text_reply);
        if (itemData.commentReply > 0) {
            commentReply += " " + itemData.commentReply;
        } else {
            btnReply.setBackground(null);
        }
        btnReply.setText(commentReply);

        helper.setText(R.id.tv_commenter_nickname, itemData.nickname);
        helper.setText(R.id.tv_comment_time, itemData.commentTime);
        helper.setText( R.id.btn_reply, commentReply);
        if (itemData.commentLike > 0) {
            helper.setText( R.id.tv_thumb_count, String.valueOf(itemData.commentLike));
        } else {
            helper.setText( R.id.tv_thumb_count, "");
        }

        if (itemData.commentType == Constant.COMMENT_TYPE_TEXT || StringUtil.isEmpty(itemData.imageUrl)) {
            itemView.findViewById(R.id.image_view).setVisibility(View.GONE);
        } else {
            ImageView imageView = itemView.findViewById(R.id.image_view);
            Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.imageUrl)).centerCrop().into(imageView);
            itemView.findViewById(R.id.image_view).setVisibility(View.VISIBLE);
        }

        ImageView iconThumb = itemView.findViewById(R.id.icon_thumb);
        if (itemData.isLike == 1) {
            iconThumb.setImageResource(R.drawable.icon_thumb_red_60);
        } else {
            iconThumb.setImageResource(R.drawable.icon_comment_thumb_grey);
        }

        TextView btnMakeTrue = itemView.findViewById(R.id.btn_make_true);
        if (isComeTrue) {
            btnMakeTrue.setVisibility(View.GONE);
        } else {
            btnMakeTrue.setVisibility(View.GONE);
        }
        //添加身份標籤
        TextView tvRole = itemView.findViewById(R.id.tv_role);
        tvRole.setVisibility(View.VISIBLE);
        switch (itemData.commentRole) {
            case CommentItem.COMMENT_ROLE_MEMBER:
                tvRole.setBackgroundResource(R.drawable.bg_text_radius_blue);
                tvRole.setText(context.getText(R.string.text_member));
                tvRole.setTextColor(context.getColor(R.color.tw_blue));
                break;
            case CommentItem.COMMENT_ROLE_BOSS:
                tvRole.setBackgroundResource(R.drawable.bg_text_radius_red);
                tvRole.setTextColor(context.getColor(R.color.tw_red));
                tvRole.setText(context.getText(R.string.text_boss));
                break;
            case CommentItem.COMMENT_ROLE_CS:
                tvRole.setBackgroundResource(R.drawable.bg_text_radius_red);
                tvRole.setTextColor(context.getColor(R.color.tw_red));
                tvRole.setText(context.getText(R.string.text_customer));
                break;
            default:
                break;

        }
    }
}
