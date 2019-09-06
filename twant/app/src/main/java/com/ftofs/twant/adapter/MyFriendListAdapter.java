package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.MyFriendListItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class MyFriendListAdapter extends BaseQuickAdapter<MyFriendListItem, BaseViewHolder> {
    public MyFriendListAdapter(int layoutResId, @Nullable List<MyFriendListItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFriendListItem item) {
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        if (StringUtil.useDefaultAvatar(item.avatarUrl)) {
            Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(imgAvatar);
        } else {
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatarUrl)).centerCrop().into(imgAvatar);
        }

        helper.setText(R.id.tv_nickname, item.nickname);
        String memberLevel = String.format("V%d會員", item.level);
        helper.setText(R.id.tv_member_level, memberLevel);

        helper.setText(R.id.tv_nickname, item.nickname);
        helper.setText(R.id.tv_member_signature, item.memberSignature);
    }
}
