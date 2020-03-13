package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ListPopupAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;

/**
 * 支付方式選擇彈窗
 * @author zwm
 */
public class PayWayPopup extends BottomPopupView implements View.OnClickListener, OnSelectedListener {
    Context context;

    ListPopupAdapter adapter;
    int selectedIndex; // 選中的index
    OnSelectedListener onSelectedListener;
    List<ListPopupItem> payWayItemList;

    /**
     * 列表彈框的構造方法
     * @param context
     * @param onSelectedListener
     */
    public PayWayPopup(@NonNull Context context, List<ListPopupItem> payWayItemList, int selectedIndex, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.onSelectedListener = onSelectedListener;
        this.payWayItemList = payWayItemList;
        this.selectedIndex = selectedIndex;
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.pay_way_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);


        RecyclerView rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(layoutManager);
        adapter = new ListPopupAdapter(context, PopupType.PAY_WAY, this, payWayItemList, selectedIndex, false, true);
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
        } else if (id == R.id.btn_ok) {
            if (onSelectedListener != null) {
                onSelectedListener.onSelected(PopupType.PAY_WAY, selectedIndex, null);
                dismiss();
            }
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.PAY_WAY) {
            selectedIndex = id;
            adapter.notifyDataSetChanged();
        }
    }
}

