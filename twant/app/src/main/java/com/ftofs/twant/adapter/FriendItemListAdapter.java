package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.FriendItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class FriendItemListAdapter extends BaseQuickAdapter<FriendItem, BaseViewHolder> {
    public FriendItemListAdapter(int layoutResId, @Nullable List<FriendItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendItem item) {
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatarUrl)).centerCrop().into(imgAvatar);
        helper.setText(R.id.tv_nickname, item.nickname);
    }
}
