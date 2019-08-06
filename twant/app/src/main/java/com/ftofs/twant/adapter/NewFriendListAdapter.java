package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.NewFriendItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class NewFriendListAdapter extends BaseQuickAdapter<NewFriendItem, BaseViewHolder> {
    public NewFriendListAdapter(int layoutResId, @Nullable List<NewFriendItem> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, NewFriendItem item) {
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatarUrl)).centerCrop().into(imgAvatar);

        helper.setText(R.id.tv_nickname, item.nickname);
        helper.setText(R.id.tv_remark, item.remark);

        helper.addOnClickListener(R.id.btn_accept);

        if (item.status == NewFriendItem.STATUS_INITIAL) {
            helper.setGone(R.id.btn_accept, true);
            helper.setGone(R.id.tv_status, false);
        } else if (item.status == NewFriendItem.STATUS_ACCEPTED) {
            helper.setGone(R.id.btn_accept, false);
            helper.setGone(R.id.tv_status, true);
        }
    }
}
