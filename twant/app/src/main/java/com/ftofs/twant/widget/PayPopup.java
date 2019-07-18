package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.macau.pay.sdk.MacauPaySdk;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 支付類型選擇彈窗
 * @author zwm
 */
public class PayPopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    MainActivity mainActivity; // 主Activity
    String paySn; // 支付單號
    String totalAmount; // 總金額
    String subject; // 訂單名稱

    public PayPopup(@NonNull Context context, MainActivity mainActivity, String paySn, String totalAmount, String subject) {
        super(context);

        this.context = context;
        this.mainActivity = mainActivity;
        this.paySn = paySn;
        this.totalAmount = totalAmount;
        this.subject = subject;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pay_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        TextView tvWantPayBalance = findViewById(R.id.tv_want_pay_balance);
        String wantPayBalance = String.format("(余額: %s)", StringUtil.formatPrice(context, 0, 0));
        tvWantPayBalance.setText(wantPayBalance);

        findViewById(R.id.btn_want_pay).setOnClickListener(this);
        findViewById(R.id.btn_mpay).setOnClickListener(this);
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
        } else if (id == R.id.btn_want_pay) {
            ToastUtil.error(context, context.getString(R.string.text_balance_not_enough));
        } else if (id == R.id.btn_mpay) {
            String token = User.getToken();

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "paySn", paySn,
                    "totalAmount", totalAmount,
                    "subject", subject);

            SLog.info("params[%s]", params);
            Api.getUI(Api.PATH_MPAY_PARAMS, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(context, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(context, responseObj)) {
                            return;
                        }

                        EasyJSONObject datas = (EasyJSONObject) responseObj.get("datas");
                        MacauPaySdk.macauPay(mainActivity, datas.toString(), mainActivity);
                    } catch (Exception e) {

                    }
                }
            });
        }
    }
}
