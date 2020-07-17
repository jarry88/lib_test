package com.ftofs.twant.kotlin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baozi.treerecyclerview.base.BaseRecyclerAdapter;
import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.item.TreeItemGroup;
import com.ftofs.twant.R;

import java.util.List;

public class ClickLoadGroupItem extends TreeItemGroup<ZoneCategory> {
    private ZoneCategory mDate;
    public BaseRecyclerAdapter.OnItemClickListener mOnItemClickLister;

    @Override
    public int getLayoutId() {
        return R.layout.store_category_list_item;
    }

    @Nullable
    @Override
    protected List<TreeItem> initChild(ZoneCategory data) {
        mDate = data;
        return ItemHelperFactory.createItems(data.getNextList(), ClickLoadChildItem.class, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_category_name, mDate.getCategoryName());
    }

    public void setOnClick(BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickLister = onItemClickListener;
    }

    public ZoneCategory getmDate() {
        return mDate;
    }
}
