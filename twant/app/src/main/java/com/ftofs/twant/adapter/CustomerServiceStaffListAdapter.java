package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class CustomerServiceStaffListAdapter extends BaseQuickAdapter<CustomerServiceStaff, BaseViewHolder> {
    public CustomerServiceStaffListAdapter(int layoutResId, @Nullable List<CustomerServiceStaff> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CustomerServiceStaff item) {
        ImageView imgStaffAvatar = helper.getView(R.id.img_staff_avatar);

        if (StringUtil.useDefaultAvatar(item.avatar)) {
            Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(imgStaffAvatar);
        } else {
            Glide.with(mContext).load(item.avatar).centerCrop().into(imgStaffAvatar);
        }


        helper.setText(R.id.tv_welcome_message, item.welcomeMessage)
                .setText(R.id.tv_staff_name, item.staffName);
    }
}
