package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class FriendItemListAdapter extends BaseQuickAdapter<FriendInfo, BaseViewHolder> {
    public FriendItemListAdapter(int layoutResId, @Nullable List<FriendInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendInfo item) {
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        if (StringUtil.isEmpty(item.avatarUrl)) {
            Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(imgAvatar);
        } else {
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatarUrl)).centerCrop().into(imgAvatar);
        }

        helper.setText(R.id.tv_nickname, item.nickname);
    }
}
