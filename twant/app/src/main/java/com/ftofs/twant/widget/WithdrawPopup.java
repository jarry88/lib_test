package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class WithdrawPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    double withdrawAmount;

    public WithdrawPopup(@NonNull Context context, double withdrawAmount) {
        super(context);

        this.context = context;
        this.withdrawAmount = withdrawAmount;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_withdraw).setOnClickListener(this);

        TextView tvWithdrawAmount = findViewById(R.id.tv_withdraw_amount);
        tvWithdrawAmount.setText(StringUtil.formatFloat(withdrawAmount));

        View btnWithdraw = findViewById(R.id.btn_withdraw);
        float radius = Util.dip2px(context, 8);
        btnWithdraw.setBackground(BackgroundDrawable.create(Color.WHITE, new float[] {0, 0, radius, radius}));
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.withdraw_popup;
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_withdraw) {
            SLog.info("全部提現");

            String url = Api.PATH_DISTRIBUTION_WITHDRAW;
            SLog.info("url[%s]", url);
            Api.postUI(url, null, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, "", "", e.getMessage());
                    ToastUtil.showNetworkError(context, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(context, responseObj)) {
                        LogUtil.uploadAppLog(url, "", responseStr, "");
                        return;
                    }

                    ToastUtil.success(context, "提現成功");
                    dismiss();
                }
            });
        }
    }
}
