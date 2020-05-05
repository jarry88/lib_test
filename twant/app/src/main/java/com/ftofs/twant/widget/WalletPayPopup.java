package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.impl.FullScreenPopupView;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 錢包支付輸入支付密碼Popup
 * @author zwm
 */
public class WalletPayPopup extends FullScreenPopupView implements View.OnClickListener {
    Context context;
    int payId;
    /**
     * 支付金額
     */
    double payAmount;
    /**
     * 支付結果回調
     */
    CommonCallback callback;


    /**
     * 支付密碼位數
     */
    public static final int PASSWORD_DIGIT_COUNT = 6;

    /**
     * 當前已輸入的密碼位數
     */
    int passwordDigitCount;

    /**
     * 存放已輸入的密碼字符
     */
    int[] passwordArr = new int[PASSWORD_DIGIT_COUNT];

    int[] keyboardIdArr = new int[] {
            R.id.btn_key_0, R.id.btn_key_1, R.id.btn_key_2, R.id.btn_key_3, R.id.btn_key_4,
            R.id.btn_key_5, R.id.btn_key_6, R.id.btn_key_7, R.id.btn_key_8, R.id.btn_key_9,
            R.id.btn_key_clear, R.id.btn_key_del
    };

    int[] indicatorIdArr = new int[] {
            R.id.iv_indicator1, R.id.iv_indicator2, R.id.iv_indicator3,
            R.id.iv_indicator4, R.id.iv_indicator5, R.id.iv_indicator6
    };

    View[] indicatorViewArr = new View[PASSWORD_DIGIT_COUNT];

    public WalletPayPopup(@NonNull Context context, int payId, double payAmount, CommonCallback callback) {
        super(context);

        this.context = context;
        this.payId = payId;
        this.payAmount = payAmount;
        this.callback = callback;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.wallet_pay_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        TextView tvPayAmount = findViewById(R.id.tv_pay_amount);
        tvPayAmount.setText(String.format("%.2f", payAmount));

        // 添加鍵盤點擊事件監聽器
        for (int i = 0; i < keyboardIdArr.length; i++) {
            findViewById(keyboardIdArr[i]).setOnClickListener(this);
        }

        // 綁定密碼輸入指示位
        for (int i = 0; i < indicatorIdArr.length; i++) {
            indicatorViewArr[i] = findViewById(indicatorIdArr[i]);
        }

        updatePasswordIndicator();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (handleKeyboardEvent(id)) {

        } else {

        }
    }

    /**
     * 處理支付密碼鍵盤事件
     * @param id
     * @return 如果是支付密碼鍵盤事件，返回true；否則，返回false
     */
    private boolean handleKeyboardEvent(int id) {
        int index = -1;

        for (int i = 0; i < keyboardIdArr.length; i++) {
            if (keyboardIdArr[i] == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        if (0 <= index && index <= 9) { // 是數字按鍵
            if (passwordDigitCount < PASSWORD_DIGIT_COUNT) {
                passwordArr[passwordDigitCount] = index;
                passwordDigitCount++;
                if (passwordDigitCount == PASSWORD_DIGIT_COUNT) {
                    doPay();
                }
            }

        } else if (id == R.id.btn_key_clear) {
            passwordDigitCount = 0;
        } else if (id == R.id.btn_key_del) {
            if (passwordDigitCount > 0) {
                passwordDigitCount--;
            }
        }

        updatePasswordIndicator();

        return true;
    }

    private void updatePasswordIndicator() {
        for (int i = 0; i < passwordDigitCount; i++) {
            indicatorViewArr[i].setVisibility(VISIBLE);
        }

        for (int i = passwordDigitCount; i < PASSWORD_DIGIT_COUNT; i++) {
            indicatorViewArr[i].setVisibility(GONE);
        }
    }

    private void doPay() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            ToastUtil.error(context, context.getString(R.string.text_user_not_login));
            return;
        }

        String password = "" + passwordArr[0] + passwordArr[1] + passwordArr[2] + passwordArr[3] + passwordArr[4] + passwordArr[5];
        SLog.info("doPay, password[%s]", password);

        EasyJSONObject params = EasyJSONObject.generate(
                "payId", payId,
                "payPwd", password,
                "token", token);
        SLog.info("params[%s]", params);

        Api.postUI(Api.PATH_WALLET_PAY, params, new UICallback() {
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
                        // 支付失敗，清空密碼輸入
                        SLog.info("__f支付失敗");
                        passwordDigitCount = 0;
                        updatePasswordIndicator();
                        return;
                    }

                    ToastUtil.success(context, "支付成功");
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                    dismiss();
                } catch (Exception e) {

                }
            }
        });
    }
}

