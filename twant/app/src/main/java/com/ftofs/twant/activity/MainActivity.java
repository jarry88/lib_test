package com.ftofs.twant.activity;


import android.os.Bundle;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.fragment.BillFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.PaySuccessFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.macau.pay.sdk.base.PayResult;
import com.macau.pay.sdk.interfaces.MPaySdkInterfaces;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 主Activity
 * @author zwm
 */
public class MainActivity extends BaseActivity implements MPaySdkInterfaces {
    long lastBackPressedTime;
    MainFragment mainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        int color = getResources().getColor(R.color.tw_red, null);
        Util.setWindowStatusBarColor(this, color);

        loadMainFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_RECREATE_MAIN_FRAGMENT) {
            loadMainFragment();
        }
    }

    private void loadMainFragment() {
        mainFragment = findFragment(MainFragment.class);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            loadRootFragment(R.id.main_fragment_container, mainFragment);
        }
    }

    @Override
    public void onBackPressedSupport() {
        long now = Time.timestampMillis();
        // 兩次按返回鍵的時間差
        long diff = now - lastBackPressedTime;
        lastBackPressedTime = now;

        if (diff >= 2000) {
            ToastUtil.info(this, "再按一次退出應用");
        } else {
            // 在時間差內兩次按下返回鍵，退出
            finish();
        }
    }

    @Override
    public void MPayInterfaces(PayResult payResult) {
        SLog.info("payResult[%s]", payResult);
        if ("9000".equals(payResult.getResultStatus())) {
            ToastUtil.success(this, payResult.getResult());
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_PAY_SUCCESS, null);
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_REFRESH_ORDER_LIST, null);

            Util.startFragment(PaySuccessFragment.newInstance(""));


            // MPay支付成功，通知服務器
            String token = User.getToken();
            int userId = User.getUserId();
            String key = String.format(SPField.FIELD_MPAY_PAY_ID, userId);
            SLog.info("key[%s]", key);
            String payData = Hawk.get(key, "");
            SLog.info("payData[%s]", payData);
            EasyJSONObject payDataObj = (EasyJSONObject) EasyJSONObject.parse(payData);
            if (payDataObj == null) {
                return;
            }

            try {
                long timestampMillis = payDataObj.getLong("timestampMillis");
                int payId = payDataObj.getInt("payId");
                long now = System.currentTimeMillis();

                if (now - timestampMillis > 60 * 1000) { // 如果超過1分鐘不通知
                    return;
                }

                if (!StringUtil.isEmpty(token)) {
                    EasyJSONObject params = EasyJSONObject.generate(
                            "token", token,
                            "payId", payId);

                    SLog.info("params[%s]", params.toString());

                    Api.postUI(Api.PATH_MPAY_ORDERS_QUERY, params, new UICallback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            SLog.info("MPay支付成功，通知服務器失敗");
                        }

                        @Override
                        public void onResponse(Call call, String responseStr) throws IOException {
                            try {
                                SLog.info("responseStr[%s]", responseStr);

                                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                                if (ToastUtil.isError(responseObj)) {
                                    SLog.info("MPay支付成功，通知服務器失敗");
                                    return;
                                }

                                SLog.info("MPay支付成功，通知服務器成功");
                            } catch (Exception e) {

                            }
                        }
                    });
                }
            } catch (EasyJSONException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.error(this, payResult.getResult());
        }
    }

    @Override
    public void WeChatPayInterfaces(PayResult payResult) {
        SLog.info("payResult[%s]", payResult);
    }

    @Override
    public void AliPayInterfaces(PayResult payResult) {
        SLog.info("payResult[%s]", payResult);
    }
}
