package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.MyFollowMemberItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class MyFollowMemberAdapter extends BaseQuickAdapter<MyFollowMemberItem, BaseViewHolder> {
    public MyFollowMemberAdapter(int layoutResId, @Nullable List<MyFollowMemberItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFollowMemberItem item) {
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatarUrl)).centerCrop().into(imgAvatar);

        helper.setText(R.id.tv_nickname, item.nickname)
                .setText(R.id.tv_member_level, item.level + mContext.getString(R.string.text_member))
                .setText(R.id.tv_member_signature, item.memberSignature);
    }
}
