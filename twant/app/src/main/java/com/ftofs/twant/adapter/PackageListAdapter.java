package com.ftofs.twant.adapter;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.PackageItem;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.Util;

import java.util.List;

/**
 * 包裹查詢的包裹列表Adapter
 * @author zwm
 */
public class PackageListAdapter extends BaseMultiItemQuickAdapter<PackageItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PackageListAdapter(List<PackageItem> data) {
        super(data);

        addItemType(PackageItem.ITEM_TYPE_SEARCH, R.layout.search_package_item);
        addItemType(PackageItem.ITEM_TYPE_INFO, R.layout.package_info_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, PackageItem item) {

        int itemViewType = helper.getItemViewType();
        if (itemViewType == PackageItem.ITEM_TYPE_SEARCH) {
            helper.addOnClickListener(R.id.btn_search_package);

            helper.setText(R.id.tv_search_hint, item.searchPackageHint);
        } else {
            helper.addOnClickListener(R.id.btn_copy);

            // 物流狀態
            helper.setText(R.id.tv_logistics_state_desc, Util.getLogisticsStateDesc(mContext, item.state));
            // 更新時間
            helper.setText(R.id.tv_update_time, item.updateTime);
            // 快遞客戶單號
            helper.setText(R.id.tv_customer_order_number, item.customerOrderNumber);
            // 受理單號
            helper.setText(R.id.tv_original_order_number, item.originalOrderNumber);
            // 發貨時間
            helper.setText(R.id.tv_create_time, item.createTime);


            // 發送方信息
            helper.setText(R.id.tv_sender_name, item.consignerName);
            helper.setText(R.id.tv_sender_mobile, item.consignerPhone);
            helper.setText(R.id.tv_sender_address, item.consignerAddress);

            // 接收方信息
            helper.setText(R.id.tv_receiver_name, item.consigneeName);
            helper.setText(R.id.tv_receiver_mobile, item.consigneePhone);
            helper.setText(R.id.tv_receiver_address, item.consigneeAddress);
        }

        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        } else {
            layoutParams.bottomMargin = 0;
        }
    }
}
