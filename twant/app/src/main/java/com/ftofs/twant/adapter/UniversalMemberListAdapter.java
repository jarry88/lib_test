package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.UniversalMemberItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class UniversalMemberListAdapter extends BaseQuickAdapter<UniversalMemberItem, BaseViewHolder> {
    public UniversalMemberListAdapter(int layoutResId, @Nullable List<UniversalMemberItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UniversalMemberItem item) {
        ImageView imageAvatar = helper.getView(R.id.img_avatar);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatarUrl)).centerCrop().into(imageAvatar);

        helper.setText(R.id.tv_nickname, item.nickname)
            .setText(R.id.tv_member_signature, item.memberSignature);
    }
}
