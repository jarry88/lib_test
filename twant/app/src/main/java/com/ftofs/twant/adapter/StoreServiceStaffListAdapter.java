package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CustomerServiceStaff;

import java.util.List;


/**
 * 店鋪客服人員彈窗列表
 * @author zwm
 */
public class StoreServiceStaffListAdapter extends BaseQuickAdapter<CustomerServiceStaff, BaseViewHolder> {

    public StoreServiceStaffListAdapter(int layoutResId, @Nullable List<CustomerServiceStaff> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, CustomerServiceStaff item) {
        ImageView imgStaffAvatar = helper.getView(R.id.img_staff_avatar);
        Glide.with(mContext).load(item.avatar).centerCrop().into(imgStaffAvatar);

        helper.setText(R.id.tv_staff_name, item.staffName);
    }
}
