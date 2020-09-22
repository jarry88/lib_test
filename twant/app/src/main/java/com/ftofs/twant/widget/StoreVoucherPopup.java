package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsVoucherListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.entity.StoreVoucher;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商店優惠券彈窗
 * @author zwm
 */
public class StoreVoucherPopup extends BottomPopupView implements View.OnClickListener, OnSelectedListener {
    Context context;
    List<StoreVoucher> storeVoucherList;

    GoodsVoucherListAdapter adapter;

    public StoreVoucherPopup(@NonNull Context context, List<StoreVoucher> storeVoucherList) {
        super(context);

        this.context = context;
        this.storeVoucherList = storeVoucherList;
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
        adapter = new GoodsVoucherListAdapter(R.layout.store_voucher_item, storeVoucherList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StoreVoucher storeVoucher = storeVoucherList.get(position);
                // 檢查未領取才調用領取接口
                if (storeVoucher.state == Constant.COUPON_STATE_UNRECEIVED) {
                    receiveVoucher(position, storeVoucher.searchSn);
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

    @Override
    public void onSelected(PopupType type, int id, Object extra) {

    }

    private void receiveVoucher(final int position, String searchSn) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        String url = Api.PATH_RECEIVE_COUPON;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "searchSn", searchSn);

        SLog.info("params[%s]", params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(context, responseObj)) {
                    LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                    return;
                }

                ToastUtil.success(context, "領取成功");
                StoreVoucher storeVoucher = storeVoucherList.get(position);
                storeVoucher.state = Constant.COUPON_STATE_RECEIVED;
                adapter.notifyDataSetChanged();
            }
        });
    }
}

