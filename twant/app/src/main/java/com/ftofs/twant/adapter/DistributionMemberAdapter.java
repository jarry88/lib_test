package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.DistributionMember;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class DistributionMemberAdapter extends BaseMultiItemQuickAdapter<DistributionMember, BaseViewHolder> {
    Context context;

    public DistributionMemberAdapter(Context context, @Nullable List<DistributionMember> data) {
        super(data);

        this.context = context;
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.distribution_member_item);
        addItemType(Constant.ITEM_TYPE_NO_DATA, R.layout.distribution_empty_view);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistributionMember item) {
        if (item.itemType == Constant.ITEM_TYPE_NORMAL) {
            ImageView imgMemberAvatar = helper.getView(R.id.img_member_avatar);
            Glide.with(context).load(StringUtil.normalizeImageUrl(item.avatar, "?x-oss-process=image/resize,w_160")).centerCrop().into(imgMemberAvatar);

            String memberLevel = "";
            if (item.deep == 1) {
                memberLevel = "一級";
            } else if (item.deep == 2) {
                memberLevel = "二級";
            }

            helper.setText(R.id.tv_member_name, item.nickName)
                    .setText(R.id.tv_member_level, "層級：" + memberLevel)
                    .setText(R.id.tv_commission_amount, StringUtil.formatFloat(item.commissionTotalAmount));

            TextView tvUpperLevelMember = helper.getView(R.id.tv_upper_level_member);
            if (StringUtil.isEmpty(item.parentNickName)) {
                tvUpperLevelMember.setVisibility(View.INVISIBLE);
            } else {
                tvUpperLevelMember.setVisibility(View.VISIBLE);
                tvUpperLevelMember.setText("上級：" + item.parentNickName);
            }
        }
    }
}
