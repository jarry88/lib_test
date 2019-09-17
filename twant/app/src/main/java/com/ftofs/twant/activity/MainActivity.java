package com.ftofs.twant.activity;


import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import com.alipay.sdk.app.PayTask;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.AliPayResult;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.ToastData;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.PaySuccessFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.PayUtil;
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
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 主Activity
 * @author zwm
 */
public class MainActivity extends BaseActivity implements MPaySdkInterfaces {
    long lastBackPressedTime;
    MainFragment mainFragment;

    private int keyboardState = Constant.KEYBOARD_HIDDEN;

    private static MainActivity instance;

    // TODO: 2019/8/19 處理HandlerLeak
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RequestCode.ALI_PAY.ordinal()) {
                AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    ToastUtil.success(MainActivity.this, "支付寶支付成功");
                    SLog.info("支付寶支付成功[%s]", payResult);

                    int userId = User.getUserId();
                    String key = String.format(SPField.FIELD_ALI_PAY_ID, userId);
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

                        // 來到這里，有付款，就肯定要通知服務器,true
                        PayUtil.onPaySuccess(true, payId, PayUtil.VENDOR_ALI);
                    } catch (Exception e) {

                    }
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    ToastUtil.error(MainActivity.this, "支付寶支付失敗" + payResult);
                    SLog.info("支付寶支付失敗[%s]", payResult);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        EventBus.getDefault().register(this);

        int color = getResources().getColor(R.color.tw_red, null);
        Util.setWindowStatusBarColor(this, android.R.color.white);

        // 監聽DecorView的變化
        View activityRoot = getWindow().getDecorView();
        if (activityRoot == null) {
            return;
        }
        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            private final Rect r = new Rect();
            private final int visibleThreshold = Math.round(Util.dip2px(MainActivity.this, Config.KEYBOARD_MIN_HEIGHT));

            @Override
            public void onGlobalLayout() {
                activityRoot.getWindowVisibleDisplayFrame(r);
                int rootViewHeight = activityRoot.getRootView().getHeight();
                int visibleHeight = r.height();
                int heightDiff = rootViewHeight - visibleHeight;
                //键盘是否弹出
                boolean isOpen = heightDiff > visibleThreshold;
                int keyboardNewState = isOpen ? Constant.KEYBOARD_SHOWN : Constant.KEYBOARD_HIDDEN;
                /*
                SLog.info("rootViewHeight[%d], visibleHeight[%d], heightDiff[%d], visibleThreshold[%d], keyboardState[%d], keyboardNewState[%d]",
                        rootViewHeight, visibleHeight, heightDiff, visibleThreshold, keyboardState, keyboardNewState);
                        */

                // 鍵盤狀態變化
                // SLog.info("鍵盤狀態變化, keyboardNewState[%d]", keyboardNewState);

                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_KEYBOARD_STATE_CHANGED, keyboardNewState);

                keyboardState = keyboardNewState;
            }
        };
        activityRoot.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);

        loadMainFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;

        EventBus.getDefault().unregister(this);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_RECREATE_MAIN_FRAGMENT) {
            loadMainFragment();
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_SHOW_TOAST) {
            ToastData toastData = (ToastData) message.data;
            if (toastData.type == ToastData.TYPE_SUCCESS) {
                ToastUtil.success(this, toastData.text);
            } else if (toastData.type == ToastData.TYPE_ERROR) {
                ToastUtil.error(this, toastData.text);
            } else if (toastData.type == ToastData.TYPE_INFO) {
                ToastUtil.info(this, toastData.text);
            }
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
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL, null);
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);

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

                    Api.postIO(Api.PATH_MPAY_ORDERS_QUERY, params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            SLog.info("MPay支付成功，通知服務器失敗");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                String responseStr = response.body().string();
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


    /**
     * 調用支付寶支付
     * @param orderInfo  订单信息
     */
    public void startAliPay(String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                SLog.info("調用支付寶支付");
                PayTask alipay = new PayTask(MainActivity.this);
                Map<String,String> result = alipay.payV2(orderInfo,true);

                SLog.info("支付寶支付結果[%s]", result);
                Message msg = new Message();
                msg.what = RequestCode.ALI_PAY.ordinal();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        TwantApplication.getThreadPool().execute(payRunnable);
    }
}
