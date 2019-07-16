package com.ftofs.twant.adapter;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.PackageItem;

import java.util.List;

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
        } else {
            helper.addOnClickListener(R.id.btn_copy);
        }

        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        }
    }
}
