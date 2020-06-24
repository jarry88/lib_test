package com.ftofs.twant.seller.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SellerOperationPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    int commonId;
    int goodsStatus;
    SimpleCallback simpleCallback;

    TextView tvShelfOperation;

    public SellerOperationPopup(@NonNull Context context, int commonId, int goodsStatus, SimpleCallback simpleCallback) {
        super(context);

        this.context = context;
        this.commonId = commonId;
        this.goodsStatus = goodsStatus;
        this.simpleCallback = simpleCallback;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.seller_operation_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_off_shelf).setOnClickListener(this);
        findViewById(R.id.btn_edit).setOnClickListener(this);
        findViewById(R.id.btn_copy).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);

        tvShelfOperation = findViewById(R.id.tv_shelf_operation);
        if (goodsStatus == Constant.GOODS_STATUS_OFF_SHELF) {
            tvShelfOperation.setText("上架");
        } else {
            tvShelfOperation.setText("下架");
        }
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

        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        if (id == R.id.btn_off_shelf) {
            SLog.info("下架");

            String url = (goodsStatus == Constant.GOODS_STATUS_OFF_SHELF ? Api.PATH_SELLER_GOODS_BATCH_ON_SHELF : Api.PATH_SELLER_GOODS_BATCH_OFF_SHELF);
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "commonIdList", String.valueOf(commonId)
            );

            SLog.info("url[%s], params[%s]", url, params);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(context, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(context, responseObj)) {
                            return;
                        }

                        ToastUtil.success(context, goodsStatus == Constant.GOODS_STATUS_ON_SHELF ? "下架成功" : "上架成功");
                        simpleCallback.onSimpleCall(EasyJSONObject.generate(
                                "action", CustomAction.CUSTOM_ACTION_SELLER_SWITCH_GOODS_SHELF_STATUS.ordinal()
                        ));

                        dismiss();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } else if (id == R.id.btn_edit) {
            SLog.info("編輯");
        } else if (id == R.id.btn_copy) {
            SLog.info("複製");

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "commonId", commonId
            );

            SLog.info("url[%s], params[%s]", Api.PATH_SELLER_COPY_GOODS, params);
            Api.postUI(Api.PATH_SELLER_COPY_GOODS, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(context, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(context, responseObj)) {
                            return;
                        }

                        ToastUtil.success(context, "複製成功");
                        dismiss();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } else if (id == R.id.btn_delete) {
            SLog.info("刪除");

            if (simpleCallback != null) {
                simpleCallback.onSimpleCall(EasyJSONObject.generate(
                        "action", CustomAction.CUSTOM_ACTION_SELLER_DELETE_GOODS.ordinal(),
                        "commonId", commonId));
            }

            dismiss();
        }
    }
}


