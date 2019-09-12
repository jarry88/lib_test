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
import com.ftofs.twant.entity.VoucherUseStatus;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
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
    int storeId;
    String storeName;
    List<StoreVoucherVo> storeVoucherVoList = new ArrayList<>();

    OrderVoucherListAdapter adapter;
    OnSelectedListener onSelectedListener;

    public OrderVoucherPopup(@NonNull Context context, int storeId, String storeName, List<StoreVoucherVo> storeVoucherVoList, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.storeId = storeId;
        this.storeName = storeName;
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
        adapter = new OrderVoucherListAdapter(R.layout.order_voucher_item, storeName, storeVoucherVoList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("HERE");
                StoreVoucherVo storeVoucherVo = storeVoucherVoList.get(position);
                VoucherUseStatus voucherUseStatus = new VoucherUseStatus();
                voucherUseStatus.storeId = storeId;
                voucherUseStatus.voucherId = storeVoucherVo.voucherId;
                voucherUseStatus.isInUse = !storeVoucherVo.isInUse;
                onSelectedListener.onSelected(PopupType.SELECT_VOUCHER, 0, voucherUseStatus);

                dismiss();
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
