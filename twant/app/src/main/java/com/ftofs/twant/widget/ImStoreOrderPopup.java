package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ImStoreOrderListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ImStoreOrderItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * IM聊天訂單列表選擇彈窗
 * @author zwm
 */
public class ImStoreOrderPopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    ImStoreOrderListAdapter adapter;
    OnSelectedListener onSelectedListener;
    String imName;
    int storeId;

    List<ImStoreOrderItem> imStoreOrderItemList = new ArrayList<>();

    public ImStoreOrderPopup(@NonNull Context context, int storeId, String imName, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.storeId = storeId;
        this.imName = imName;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.im_store_order_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(layoutManager);
        adapter = new ImStoreOrderListAdapter(R.layout.im_store_order_item, imStoreOrderItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImStoreOrderItem item = imStoreOrderItemList.get(position);

                onSelectedListener.onSelected(PopupType.IM_CHAT_SEND_ORDER, 0, item);
                dismiss();
            }
        });
        rvList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_IM_STORE_ORDER_LIST;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "storeId", storeId,
                "imName", imName);

        SLog.info("params[%s]", params);
        Api.getUI(url, params, new UICallback() {
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

                try {
                    EasyJSONArray ordersList = responseObj.getSafeArray("datas.ordersList");

                    for (Object object : ordersList) {
                        ImStoreOrderItem imStoreOrderItem = new ImStoreOrderItem();
                        EasyJSONObject item = (EasyJSONObject) object;

                        imStoreOrderItem.ordersId = item.getInt("ordersId");
                        imStoreOrderItem.ordersSn = String.valueOf(item.getLong("ordersSn"));
                        imStoreOrderItem.goodsImage = item.getSafeString("goodsImg");
                        imStoreOrderItem.goodsName = item.getSafeString("goodsName");

                        imStoreOrderItemList.add(imStoreOrderItem);
                    }

                    adapter.setNewData(imStoreOrderItemList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
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

