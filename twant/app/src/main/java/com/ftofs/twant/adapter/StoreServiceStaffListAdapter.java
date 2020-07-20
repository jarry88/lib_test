package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.util.StringUtil;

import java.util.List;


/**
 * 商店客服人員彈窗列表
 * @author zwm
 */
public class StoreServiceStaffListAdapter extends BaseQuickAdapter<CustomerServiceStaff, BaseViewHolder> {

    public StoreServiceStaffListAdapter(int layoutResId, @Nullable List<CustomerServiceStaff> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, CustomerServiceStaff item) {
        ImageView imgStaffAvatar = helper.getView(R.id.img_staff_avatar);
        if (StringUtil.useDefaultAvatar(item.avatar)) {
            Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(imgStaffAvatar);
        } else {
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.avatar)).centerCrop().into(imgStaffAvatar);
        }

        helper.setText(R.id.tv_staff_name, item.staffName);

        TextView tvCustomerServiceType = helper.getView(R.id.tv_customer_service_type);
        if (item.staffType == 1) {
            tvCustomerServiceType.setText(R.string.text_pre_sale);
            tvCustomerServiceType.setBackgroundResource(R.drawable.customer_service_type_indicator_bg_red);
        } else {
            tvCustomerServiceType.setText(R.string.text_post_sale);
            tvCustomerServiceType.setBackgroundResource(R.drawable.customer_service_type_indicator_bg_blue);
        }
    }
}
