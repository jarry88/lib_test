package com.ftofs.twant.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.SearchItem;

import java.util.List;

public class StoreSearchItemAdapter extends BaseMultiItemQuickAdapter<SearchItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public StoreSearchItemAdapter(List<SearchItem> data) {
        super(data);

        addItemType(SearchItem.ITEM_TYPE_CATEGORY, R.layout.search_item_category);
        addItemType(SearchItem.ITEM_TYPE_GOODS, R.layout.search_item_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchItem item) {
        if (item.getItemType() == SearchItem.ITEM_TYPE_CATEGORY) {
            helper.setText(R.id.tv_category_name, item.name);
        } else {
            helper.setText(R.id.tv_goods_name, item.name);
        }
    }
}
