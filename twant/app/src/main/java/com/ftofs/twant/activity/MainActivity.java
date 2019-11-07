package com.ftofs.twant.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PermissionGroupInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import com.alipay.sdk.app.PayTask;
import com.facebook.CallbackManager;
import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
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
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.IntentUtil;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.PayUtil;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AppUpdatePopup;
import com.jaeger.library.StatusBarUtil;
import com.lxj.xpopup.XPopup;
import com.macau.pay.sdk.base.PayResult;
import com.macau.pay.sdk.interfaces.MPaySdkInterfaces;
import com.orhanobut.hawk.Hawk;
import com.yanzhenjie.permission.runtime.Permission;

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

    CallbackManager callbackManager;

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
                    ToastUtil.error(MainActivity.this, payResult.getMemo());
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

        int color = getResources().getColor(R.color.tw_blue, null);
        /*
        改變狀態欄顏色，在錘子手機中，狀態欄有一種灰色蒙板的感覺，在紅米手機中沒有
         */
        StatusBarUtil.setColor(this, color, 0);
        // StatusBarUtil.setTranslucent(this);
        // StatusBarUtil.setTransparent(this);

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

        // 2秒后，進行定位
        PermissionUtil.actionWithPermission(this, new String[]{
                Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_COARSE_LOCATION}, "店鋪定位需要授予", new CommonCallback() {
            @Override
            public String onSuccess(@Nullable String data) {
                // 進行定位
                SLog.info("進行定位");
                TwantApplication.getTwLocation().startLocation();
                return null;
            }

            @Override
            public String onFailure(@Nullable String data) {
                ToastUtil.error(MainActivity.this, "您拒絕了授權");
                return null;
            }
        });

        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果不是在顯示版本升級對話框，則檢查是否有新版本
        long popupShownTimestamp = Hawk.get(SPField.FIELD_APP_UPDATE_POPUP_SHOWN_TIMESTAMP, 0L);
        SLog.info("popupShownTimestamp[%s]", popupShownTimestamp);

        // 最近一次顯示時間超過一天，則進行檢查更新(主要用于前后臺切換時，不要重復顯示)
        if (System.currentTimeMillis() - popupShownTimestamp > 24 * 3600 * 1000) {
            EasyJSONObject params = EasyJSONObject.generate("version", BuildConfig.VERSION_NAME);
            SLog.info("params[%s]", params);
            Api.getUI(Api.PATH_CHECK_UPDATE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(MainActivity.this, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(MainActivity.this, responseObj)) {
                            return;
                        }

                        if (!responseObj.exists("datas.version")) {
                            // 如果服務器端沒有返回版本信息，也當作是最新版本
                            return;
                        }

                        // 當前版本
                        String currentVersion = BuildConfig.VERSION_NAME;
                        // 最新版本
                        String newestVersion = responseObj.getString("datas.version");

                        SLog.info("currentVersion[%s], newestVersion[%s]", currentVersion, newestVersion);
                        int result = Util.versionCompare(currentVersion, newestVersion);
                        SLog.info("result[%d]", result);

                        if (result < 0) { // 發現新版本
                            boolean isDismissOnBackPressed = true;
                            boolean isDismissOnTouchOutside = true;
                            boolean isForceUpdate = (responseObj.getInt("datas.isForceUpdate") != 0);

                            String version = responseObj.getString("datas.version");
                            String versionDesc = responseObj.getString("datas.remarks");

                            // isForceUpdate = true;
                            // 如果是強制升級，點擊對話框外的區域或按下返回鍵，也不能關閉對話框
                            if (isForceUpdate) {
                                isDismissOnBackPressed = false;
                                isDismissOnTouchOutside = false;
                            }

                            String today = new Jarbon().toDateString();
                            String appUpdatePopupShownDate = Hawk.get(SPField.FIELD_APP_UPDATE_POPUP_SHOWN_DATE);
                            if (!isForceUpdate && today.equals(appUpdatePopupShownDate)) { // 如果不是強制升級，并且今天已經顯示過升級對話框，則不再顯示
                                SLog.info("如果不是強制升級，并且今天已經顯示過升級對話框，則不再顯示");
                                return;
                            }

                            new XPopup.Builder(MainActivity.this)
                                    .dismissOnBackPressed(isDismissOnBackPressed) // 按返回键是否关闭弹窗，默认为true
                                    .dismissOnTouchOutside(isDismissOnTouchOutside) // 点击外部是否关闭弹窗，默认为true
                                    // 如果不加这个，评论弹窗会移动到软键盘上面
                                    .moveUpToKeyboard(false)
                                    .asCustom(new AppUpdatePopup(MainActivity.this, version, versionDesc, isForceUpdate))
                                    .show();
                        }
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        }
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

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
