package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class FollowMeAvatarAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public FollowMeAvatarAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        if (StringUtil.isEmpty(item)) {
            Glide.with(mContext).load(R.drawable.avatar_male).centerCrop().into(imgAvatar);
        } else {
            Glide.with(mContext).load(item).centerCrop().into(imgAvatar);
        }
    }
}
