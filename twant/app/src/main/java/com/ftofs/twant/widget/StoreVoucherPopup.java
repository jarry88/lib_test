package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsVoucherListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 店鋪優惠券彈窗
 * @author zwm
 */
public class StoreVoucherPopup extends BottomPopupView implements View.OnClickListener, OnSelectedListener {
    int storeId;
    Context context;
    List<StoreVoucher> storeVoucherList = new ArrayList<>();

    GoodsVoucherListAdapter adapter;

    public StoreVoucherPopup(@NonNull Context context, int storeId) {
        super(context);

        this.context = context;
        this.storeId = storeId;
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.store_voucher_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvStoreVoucherList = findViewById(R.id.rv_store_voucher_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvStoreVoucherList.setLayoutManager(layoutManager);
        adapter = new GoodsVoucherListAdapter(R.layout.store_voucher_item, storeVoucherList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_receive_voucher_now) {
                    StoreVoucher storeVoucher = storeVoucherList.get(position);
                    // 檢查未領取才調用領取接口
                    if (storeVoucher.memberIsReceive == Constant.ZERO) {
                        receiveVoucher(position, storeVoucher.templateId);
                    }
                }
            }
        });
        rvStoreVoucherList.setAdapter(adapter);

        loadStoreActivityData();
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

    /**
     * 加載店鋪活動數據
     */
    private void loadStoreActivityData() {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                SLog.info("Error!token 為空");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "storeId", storeId,
                    "token", token);

            SLog.info("params[%s]", params);

            final BasePopupView loadingPopup = new XPopup.Builder(getContext())
                    .asLoading(context.getString(R.string.text_loading))
                    .show();

            Api.postUI(Api.PATH_STORE_ACTIVITY, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(context, responseObj)) {
                        return;
                    }

                    try {
                        EasyJSONArray voucherList = responseObj.getArray("datas.voucherList");
                        for (Object object : voucherList) { // PayObject
                            EasyJSONObject voucher = (EasyJSONObject) object;

                            StoreVoucher storeVoucher = new StoreVoucher(
                                    voucher.getInt("storeId"),
                                    voucher.getInt("templateId"),
                                    voucher.getString("storeName"),
                                    voucher.getInt("templatePrice"),
                                    voucher.getString("limitAmountText"),
                                    voucher.getString("usableClientTypeText"),
                                    voucher.getString("useStartTime"),
                                    voucher.getString("useEndTime"),
                                    voucher.getInt("memberIsReceive"));
                            storeVoucherList.add(storeVoucher);
                        }
                        adapter.setNewData(storeVoucherList);
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                        SLog.info("Error!loadStoreActivityData failed");
                    }
                }
            });
        } catch (Exception e) {

        }
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

    }

    private void receiveVoucher(final int position, int templateId) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "templateId", templateId);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_RECEIVE_VOUCHER, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(context, responseObj)) {
                    return;
                }

                ToastUtil.show(context, "領取成功");
                StoreVoucher storeVoucher = storeVoucherList.get(position);
                storeVoucher.memberIsReceive = 1;
                adapter.setNewData(storeVoucherList);
            }
        });
    }
}

