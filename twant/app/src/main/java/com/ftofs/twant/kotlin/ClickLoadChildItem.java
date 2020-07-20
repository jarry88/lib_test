package com.ftofs.twant.kotlin;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.item.TreeItem;
import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

public class ClickLoadChildItem extends TreeItem<ZoneCategory> {
    public static int index;
    public ClickLoadGroupItem parent;
    public boolean selected;

    @Override
    public int getLayoutId() {
        return R.layout.sub_category_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        parent = (ClickLoadGroupItem) getParentItem();
        TextView textView = viewHolder.getView(R.id.tv_category_name);
        SLog.info("已经刷新");
//        textView.setPadding(0, Util.dip2px(context, 12.5f), 0, Util.dip2px(context, 12.5f));

        viewHolder.setText(R.id.tv_category_name, "·"+data.getCategoryName());
        textView.setTextColor(parent.mContext.getColor(selected?R.color.tw_blue:R.color.tw_black));
        textView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setTextColor(parent.mContext.getColor(R.color.tw_blue));
                selected = true;
                SimpleCallback callback = ((ClickLoadGroupItem) getParentItem()).callback;
                if (callback != null) {
                    callback.onSimpleCall(data);
                }
            }
        });
    }

    public void updateUI() {
    }
}

