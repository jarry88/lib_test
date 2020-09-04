package com.ftofs.twant.widget;

import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CartCrossBorderAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.CartCrossBorderItem;
import com.ftofs.twant.entity.CrossBorderStoreInfo;
import com.ftofs.twant.fragment.ConfirmOrderFragment;
import com.ftofs.twant.fragment.NewConfirmOrderFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 購物車跨境購商品列表彈窗
 * @author zwm
 */
public class CartCrossBorderPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    Map<Integer, CrossBorderStoreInfo> crossBorderStoreMap;
    OnSelectedListener onSelectedListener;

    RecyclerView rvList;

    CartCrossBorderAdapter cartCrossBorderAdapter;
    List<CartCrossBorderItem> cartCrossBorderItemList = new ArrayList<>();


    public CartCrossBorderPopup(@NonNull Context context, Map<Integer, CrossBorderStoreInfo> crossBorderStoreMap, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.crossBorderStoreMap = crossBorderStoreMap;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.cart_cross_border_popup;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_settlement).setOnClickListener(this);

        // 排序，【其他產品】的排在後面
        Map<Integer, CrossBorderStoreInfo> sortedMap = new LinkedHashMap<>();
        crossBorderStoreMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o2 - o1;
                    }
                }))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        crossBorderStoreMap = sortedMap;

        for (Map.Entry<Integer, CrossBorderStoreInfo> entry : crossBorderStoreMap.entrySet()) {
            CrossBorderStoreInfo crossBorderStoreInfo = entry.getValue();
            cartCrossBorderItemList.add(new CartCrossBorderItem(false, crossBorderStoreInfo.isCrossBorder, crossBorderStoreInfo.storeId, crossBorderStoreInfo.storeName, crossBorderStoreInfo.productCount));
        }

        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(context));
        cartCrossBorderAdapter = new CartCrossBorderAdapter(R.layout.cart_cross_border_item, cartCrossBorderItemList);
        cartCrossBorderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CartCrossBorderItem cartCrossBorderItem;

                // 取消上一個選中狀態
                if (cartCrossBorderAdapter.selectedPosition != -1) {
                    cartCrossBorderItem = cartCrossBorderItemList.get(cartCrossBorderAdapter.selectedPosition);
                    cartCrossBorderItem.selected = false;
                    cartCrossBorderAdapter.notifyItemChanged(cartCrossBorderAdapter.selectedPosition);
                }

                // 選中當前的
                cartCrossBorderItem = cartCrossBorderItemList.get(position);
                cartCrossBorderItem.selected = true;
                cartCrossBorderAdapter.notifyItemChanged(position);

                cartCrossBorderAdapter.selectedPosition = position;
            }
        });
        rvList.setAdapter(cartCrossBorderAdapter);
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
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * .8f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            dismiss();
        } else if (id == R.id.btn_settlement) {
            if (cartCrossBorderAdapter.selectedPosition == -1) {
                ToastUtil.error(context, "請選擇分開結算的產品");
                return;
            }

            if (onSelectedListener != null) {
                onSelectedListener.onSelected(PopupType.SELECT_SPLIT_CROSS_BORDER, cartCrossBorderAdapter.selectedPosition, null);
            }

            SLog.info("cartCrossBorderAdapter.selectedPosition[%d]", cartCrossBorderAdapter.selectedPosition);
            CartCrossBorderItem cartCrossBorderItem = cartCrossBorderItemList.get(cartCrossBorderAdapter.selectedPosition);

            int storeId = cartCrossBorderItem.storeId;
            if (!cartCrossBorderItem.isCrossBorder) {  // 如果不是跨境購商品，不區分店鋪，用0表示
                storeId = 0;
            }
            SLog.info("cartCrossBorderItem.storeId[%d], storeId[%d]", cartCrossBorderItem.storeId, storeId);

            CrossBorderStoreInfo crossBorderStoreInfo = crossBorderStoreMap.get(storeId);
            SLog.info("crossBorderStoreInfo[%s]", crossBorderStoreInfo);
            if (crossBorderStoreInfo != null && crossBorderStoreInfo.buyData != null) {
                SLog.info("crossBorderStoreInfo.buyData[%s]", crossBorderStoreInfo.buyData);
                Util.startFragment(NewConfirmOrderFragment.newInstance(1, crossBorderStoreInfo.buyData.toString()));
            }

            dismiss();
        }
    }
}
