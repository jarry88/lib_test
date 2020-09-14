package com.ftofs.twant.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CommentItem;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

import androidx.annotation.Nullable;

public class MemberCommentAdapter extends BaseQuickAdapter<CommentItem, BaseViewHolder> {
    public MemberCommentAdapter(int layoutResId, @Nullable List<CommentItem> data){
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentItem item) {
        helper.addOnClickListener(R.id.rv_post_list,R.id.btn_delete);
        helper.setText(R.id.tv_content,item.content);
        helper.setText(R.id.tv_date,item.commentTime);
        helper.setText(R.id.tv_post_title,item.postTitle);
        helper.setText(R.id.tv_post_author_name, item.postAuthorName);
        ImageView commentAvatar = helper.getView(R.id.iv_post_image);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.configurePostUrl)).centerCrop().into(commentAvatar);
        ImageView authorAvatar = helper.getView(R.id.img_author_avatar);
        SLog.info("postAuthorAvatar[%s]",item.postAuthorAvatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.postAuthorAvatar)).centerCrop().into(authorAvatar);
        if (!StringUtil.isEmpty(item.imageUrl)) {

        }

    }
}
