package com.ftofs.twant.kotlin;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baozi.treerecyclerview.base.BaseRecyclerAdapter;
import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.item.TreeItemGroup;
import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.SimpleCallback;

import java.util.ArrayList;
import java.util.List;

public class ClickLoadGroupItem extends TreeItemGroup<ZoneCategory> {
    private ZoneCategory mDate;
    public BaseRecyclerAdapter.OnItemClickListener mOnItemClickLister;
    public SimpleCallback callback;
    @org.jetbrains.annotations.Nullable
    public Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.store_category_list_item;
    }

    @Nullable
    @Override
    protected List<TreeItem> initChild(ZoneCategory data) {
        mDate = data;
        List<ZoneCategory> mItems = new ArrayList<>();
        for (ZoneCategory zoneCategory : data.getNextList()) {
            zoneCategory.setFold(1);
            mItems.add(zoneCategory);
        }
        return ItemHelperFactory.createItems(mItems, ClickLoadChildItem.class, this);
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

    public List<ClickLoadChildItem> getClickChild() {
        List<ClickLoadChildItem> items=new ArrayList<>();
        for (TreeItem item : getChild()) {
            items.add((ClickLoadChildItem) item);
        }
        return items;
    }
}
