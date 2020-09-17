package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PayWayAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.PayWayItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;

/**
 * 支付方式選擇彈窗
 * @author zwm
 */
public class PayWayPopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    PayWayAdapter adapter;
    int selectedIndex; // 選中的index
    OnSelectedListener onSelectedListener;
    List<PayWayItem> payWayItemList;

    /**
     * 列表彈框的構造方法
     * @param context
     * @param onSelectedListener
     */
    public PayWayPopup(@NonNull Context context, List<PayWayItem> payWayItemList, int selectedIndex, OnSelectedListener onSelectedListener) {
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
        rvList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new PayWayAdapter(context, R.layout.payway_item_layout, payWayItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("position[%d], selectedIndex[%d]", position, selectedIndex);
                if (selectedIndex == position) {
                    // 重覆選中
                    return;
                }
                SLog.info("here");
                // 取消前一個選中狀態
                PayWayItem prevItem = payWayItemList.get(selectedIndex);
                prevItem.isSelected = false;
                adapter.notifyItemChanged(selectedIndex);
                SLog.info("here");

                // 設置當前的選中狀態
                selectedIndex = position;
                PayWayItem currItem = payWayItemList.get(selectedIndex);
                currItem.isSelected = true;
                SLog.info("here");
                adapter.notifyItemChanged(selectedIndex);
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
                int selectedPayWay = payWayItemList.get(selectedIndex).payWay;
                SLog.info("selectedPayWay[%d]", selectedPayWay);
                onSelectedListener.onSelected(PopupType.PAY_WAY, selectedIndex, selectedPayWay);
                dismiss();
            }
        }
    }
}

