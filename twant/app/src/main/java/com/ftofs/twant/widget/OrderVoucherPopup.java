package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.OrderVoucherListAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.StoreVoucher;
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
    int couponType;
    List<StoreVoucherVo> storeVoucherVoList;
    int platformCouponIndex;

    OrderVoucherListAdapter adapter;
    OnSelectedListener onSelectedListener;

    /**
     * Constructor
     * @param context
     * @param storeId
     * @param storeName
     * @param couponType 表示storeVoucherVoList是店鋪券還是平台券
     * @param storeVoucherVoList 店鋪券列表 或 平台券列表
     * @param platformCouponIndex 當前正在使用的平台券列表Index(-1表示沒有使用)，當couponType為平台券時才用
     * @param onSelectedListener
     */
    public OrderVoucherPopup(@NonNull Context context, int storeId, String storeName, int couponType,
                             List<StoreVoucherVo> storeVoucherVoList, int platformCouponIndex,
                             OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.storeId = storeId;
        this.storeName = storeName;
        this.couponType = couponType;
        this.storeVoucherVoList = storeVoucherVoList;
        this.platformCouponIndex = platformCouponIndex;
        this.onSelectedListener = onSelectedListener;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.voucher_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView tvPopupTitle = findViewById(R.id.tv_popup_title);
        if (couponType == Constant.COUPON_TYPE_STORE) {
            tvPopupTitle.setText(R.string.text_voucher);
        } else {
            tvPopupTitle.setText(R.string.text_platform_coupon);
        }

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvStoreVoucherList = findViewById(R.id.rv_voucher_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvStoreVoucherList.setLayoutManager(layoutManager);

        adapter = new OrderVoucherListAdapter(R.layout.order_voucher_item, storeName, couponType, storeVoucherVoList, platformCouponIndex);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("HERE");
                StoreVoucherVo storeVoucherVo = storeVoucherVoList.get(position);
                VoucherUseStatus voucherUseStatus = new VoucherUseStatus();
                voucherUseStatus.storeId = storeId;
                voucherUseStatus.voucherId = storeVoucherVo.voucherId;
                voucherUseStatus.isInUse = !storeVoucherVo.isInUse;
                if (couponType == Constant.COUPON_TYPE_STORE) {
                    onSelectedListener.onSelected(PopupType.SELECT_VOUCHER, 0, voucherUseStatus);
                } else {
                    SLog.info("position[%d]", position);
                    onSelectedListener.onSelected(PopupType.SELECT_PLATFORM_COUPON, position, null);
                }

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
