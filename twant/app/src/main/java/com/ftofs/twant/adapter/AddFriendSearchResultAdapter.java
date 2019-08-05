package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.AddFriendSearchResultItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class AddFriendSearchResultAdapter extends BaseQuickAdapter<AddFriendSearchResultItem, BaseViewHolder> {
    public AddFriendSearchResultAdapter(int layoutResId, @Nullable List<AddFriendSearchResultItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddFriendSearchResultItem item) {
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatar)).centerCrop().into(imgAvatar);
        helper.setText(R.id.tv_nickname, item.nickname);
        helper.setText(R.id.tv_member_signature, item.memberSignature);
    }
}
