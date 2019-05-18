package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ListPopupAdapter;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;


/**
 * 配送時間選擇
 * @author zwm
 */
public class ListPopup extends BottomPopupView implements View.OnClickListener, OnSelectedListener {
    Context context;

    String title; // 彈出框的標題
    List<ListPopupItem> itemList;
    int index; // 選中的index
    OnSelectedListener onSelectedListener;

    int type;
    public ListPopup(@NonNull Context context, String title, int type, List<ListPopupItem> itemList, int index, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.title = title;
        this.type = type;
        this.index = index;
        this.itemList = itemList;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.list_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_popup_title)).setText(title);

        RecyclerView rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(layoutManager);
        ListPopupAdapter adapter = new ListPopupAdapter(context, type, this, itemList, index);
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

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        }
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        SLog.info("onSelected, type[%d], id[%d], extra[%s]", type, id, extra);
        onSelectedListener.onSelected(type, id, extra);
        dismiss();
    }
}
