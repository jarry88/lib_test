package com.ftofs.twant.widget;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GroupListAdapter;
import com.ftofs.twant.entity.GroupListItem;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看更多拼團信息彈窗
 * @author zwm
 */
public class ViewMoreGroupPopup extends CenterPopupView implements View.OnClickListener {
    Context context;

    RecyclerView rvList;
    GroupListAdapter adapter;
    List<GroupListItem> groupList = new ArrayList<>();

    public ViewMoreGroupPopup(@NonNull Context context) {
        super(context);

        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.view_more_group_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_close).setOnClickListener(this);

        groupList.add(new GroupListItem(Jarbon.create(2020, 6, 29, 15, 7, 11, 30).getTimestampMillis()));


        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new GroupListAdapter(context, R.layout.group_list_item, groupList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_join_group) {

                }
            }
        });
        rvList.setAdapter(adapter);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        // 關閉時，清除計時器
        adapter.cancelAllTimers();
    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.8f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_close) {
            dismiss();
        }
    }
}
