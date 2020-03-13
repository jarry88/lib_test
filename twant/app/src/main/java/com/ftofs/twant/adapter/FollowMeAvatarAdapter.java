package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.UniversalMemberItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class FollowMeAvatarAdapter extends BaseQuickAdapter<UniversalMemberItem, BaseViewHolder> {
    public FollowMeAvatarAdapter(int layoutResId, @Nullable List<UniversalMemberItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UniversalMemberItem item) {
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        if (StringUtil.isEmpty(item.avatarUrl)) {
            Glide.with(mContext).load(R.drawable.icon_default_avatar).centerCrop().into(imgAvatar);
        } else {
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatarUrl)).centerCrop().into(imgAvatar);
        }
    }
}
