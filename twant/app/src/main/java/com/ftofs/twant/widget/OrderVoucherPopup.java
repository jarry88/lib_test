package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.OrderVoucherListAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.StoreVoucherVo;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 確認訂單的優惠券彈窗
 * @author zwm
 */
public class OrderVoucherPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    List<StoreVoucherVo> storeVoucherVoList = new ArrayList<>();

    OrderVoucherListAdapter adapter;
    OnSelectedListener onSelectedListener;

    public OrderVoucherPopup(@NonNull Context context, List<StoreVoucherVo> storeVoucherVoList, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.storeVoucherVoList = storeVoucherVoList;
        this.onSelectedListener = onSelectedListener;
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.voucher_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvStoreVoucherList = findViewById(R.id.rv_voucher_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvStoreVoucherList.setLayoutManager(layoutManager);
        adapter = new OrderVoucherListAdapter(R.layout.order_voucher_item, storeVoucherVoList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_receive_voucher_now) {
                    if (onSelectedListener != null) {
                        onSelectedListener.onSelected(PopupType.SELECT_VOUCHER, position, null);
                    }
                    dismiss();
                }
            }
        });
        rvStoreVoucherList.setAdapter(adapter);
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
}
