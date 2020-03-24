package com.ftofs.twant.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
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
import com.ftofs.twant.entity.Location;
import com.ftofs.twant.entity.ToastData;
import com.ftofs.twant.entity.WantedPostItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.HomeFragment;
import com.ftofs.twant.fragment.JobDetailFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.MemberInfoFragment;
import com.ftofs.twant.fragment.PaySuccessFragment;
import com.ftofs.twant.fragment.PostDetailFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.HomeStickyView;
import com.ftofs.twant.tangram.LogoView;
import com.ftofs.twant.task.TencentLocationTask;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.PayUtil;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.DragFloatActionButton;
import com.ftofs.twant.widget.AppUpdatePopup;
import com.jaeger.library.StatusBarUtil;
import com.lxj.xpopup.XPopup;
import com.macau.pay.sdk.base.PayResult;
import com.macau.pay.sdk.interfaces.MPaySdkInterfaces;
import com.orhanobut.hawk.Hawk;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.util.IInnerImageSetter;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONBase;
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

    TangramEngine engine;

    public MainFragment getMainFragment() {
        return mainFragment;
    }
    MainFragment mainFragment;

    CallbackManager callbackManager;

    private int keyboardState = Constant.KEYBOARD_HIDDEN;

    private static MainActivity instance;

    /*
    MainActivity最近一次可見的時間戳
     */
    public long resumeTimestamp;

    /**
     * App升級文件的路徑
     */
    private String updateApkPath;

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

                    int vendor = PayUtil.VENDOR_ALI;
                    int userId = User.getUserId();

                    // 先嘗試獲取支付寶（內地）的支付信息，如果沒有，再獲取支付寶（香港）的支付信息
                    String key = String.format(SPField.FIELD_ALI_PAY_ID, userId);
                    SLog.info("key[%s]", key);
                    String payData = Hawk.get(key, "");
                    SLog.info("payData[%s]", payData);
                    EasyJSONObject payDataObj = EasyJSONObject.parse(payData);
                    if (payDataObj == null) {
                        // 嘗試獲取 支付寶（香港）的支付信息
                        key = String.format(SPField.FIELD_ALI_PAY_HK_ID, userId);
                        SLog.info("key[%s]", key);
                        payData = Hawk.get(key, "");
                        SLog.info("payData[%s]", payData);
                        payDataObj = EasyJSONObject.parse(payData);

                        if (payDataObj == null) {
                            return;
                        }

                        vendor = PayUtil.VENDOR_ALI_HK;
                    } else {
                        Hawk.delete("key");
                    }

                    try {
                        long timestampMillis = payDataObj.getLong("timestampMillis");
                        int payId = payDataObj.getInt("payId");
                        long now = System.currentTimeMillis();

                        if (now - timestampMillis > 60 * 1000) { // 如果超過1分鐘不通知
                            return;
                        }

                        // 來到這里，有付款，就肯定要通知服務器,true
                        PayUtil.onPaySuccess(true, payId, vendor);
                    } catch (Exception e) {

                    }
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    ToastUtil.error(MainActivity.this, payResult.getMemo());
                    SLog.info("支付寶/支付寶香港支付失敗[%s]", payResult);
                }
            }
        }
    };

    public void setMessageFragmentsActivity(boolean messageFragmentsActivity) {
        this.messageFragmentsActivity = messageFragmentsActivity;
    }

    private boolean messageFragmentsActivity;

    public String getCurrChatMemberName() {
        return currChatMemberName;
    }

    private String currChatMemberName;

    @Override
    protected void onPause() {
        super.onPause();
        Hawk.put(SPField.MAINACTIVITY_RESUME, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        EventBus.getDefault().register(this);

        initTangram();

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
        //這裏可以保證每次覆蓋安裝時獲取當前用戶的身份數據
        TwantApplication.getInstance().updateCurrMemberInfo();
        loadMainFragment();

        // 2秒后，進行定位
        PermissionUtil.actionWithPermission(this, new String[]{
                Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_COARSE_LOCATION}, "商店定位需要授予", new CommonCallback() {
            @Override
            public String onSuccess(@Nullable String data) {
                // 進行定位
                SLog.info("進行定位");
//                TwantApplication.getTwLocation().startLocation();
                return null;
            }

            @Override
            public String onFailure(@Nullable String data) {
                ToastUtil.error(MainActivity.this, "您拒絕了授權");
                return null;
            }
        });

        callbackManager = CallbackManager.Factory.create();
        //懸浮按鈕點擊事件設置
        setFloatActionMenuOnClick();
        SLog.info("oncreate方法处调用");
        replyIntent();
    }



    private void initTangram() {
        // 初始化 Tangram 环境
        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view,
                                                                 @Nullable String url) {
                //假设你使用 Picasso 加载图片
                Glide.with(MainActivity.this).load(url).into(view);
            }
        }, ImageView.class);

        // 初始化 TangramBuilder
        TangramBuilder.InnerBuilder builder = TangramBuilder.newInnerBuilder(MainActivity.this);

        // 注册自定义的卡片和组件
        builder.registerCell("LogoCell", LogoView.class);
        builder.registerCell("StickyCell", HomeStickyView.class);


        // 生成 TangramEngine 实例
        engine = builder.build();

        // 绑定业务 support 类到 engine
        // 处理点击
        // mEngine.addSimpleClickSupport(new CustomClickSupport());
        // 处理曝光
        // mEngine.addExposureSupport(new CustomExposureSupport());
        // 异步加载数据
        // mEngine.addCardLoadSupport(cardLoadSupport);
    }

    public TangramEngine getTangramEngine() {
        return engine;
    }

    private void setFloatActionMenuOnClick() {
        int env= Hawk.get(SPField.FIELD_CURRENT_ENV,Config.ENV_PROD);
        Config.changeEnvironment(env);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SLog.info("newintent 处调用");
        Bundle bundle = intent.getExtras();
//        String data =getIntent().getDataString();
        if (bundle==null) {
            SLog.info("没拿到");
        } else {
            String HxId = bundle.getString(Constant.MSG_NOTIFY_HXID);
            if (StringUtil.isEmpty(HxId)) {
                boolean exit = bundle.getBoolean("exit");
                if (exit) {
                    ToastUtil.success(getApplicationContext(),String.format("重啓homefragment,當前為%d環境",Config.currEnv));
                    HomeFragment homeFragment = mainFragment.getHomeFragment();
                    if (homeFragment != null) {
                        homeFragment.resetLoaded();
                    }
                }
            } else {
                mainFragment.showHideFragment(MainFragment.MESSAGE_FRAGMENT);
            }
        }
//        replyIntent();

    }

    private void replyIntent() {
        SLog.info("oncreat进入replyIntent方法");
        Bundle bundle = getIntent().getExtras();
        if (bundle==null) {
            SLog.info("没拿到");
        } else {
            String HxId = bundle.getString(Constant.MSG_NOTIFY_HXID);
            if (StringUtil.isEmpty(HxId)) {

            } else {
                SLog.info("HXId%s",HxId);
                new Handler().postDelayed(() -> {

                    /**
                     * 延时执行的代码
                     */
                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MESSAGE_FRAGMENT);;
                },100); // 延时0.1秒，等待加载mainfragment
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Hawk.put(SPField.MAINACTIVITY_RESUME, true);
        resumeTimestamp = System.currentTimeMillis();

        Util.getMemberToken(this);

        // 如果不是在顯示版本升級對話框，則檢查是否有新版本
        long popupShownTimestamp = Hawk.get(SPField.FIELD_APP_UPDATE_POPUP_SHOWN_TIMESTAMP, 0L);
        SLog.info("popupShownTimestamp[%s]", popupShownTimestamp);

        // 最近一次顯示時間超過一天，則進行檢查更新(主要用于前后臺切換時，不要重復顯示)
        if (System.currentTimeMillis() - popupShownTimestamp > 24 * 3600 * 1000) {
            checkUpdate(false);
        }

        // 获取是否有网页拉起APP传过来的参数
        String launchAppParams = Hawk.get(SPField.FIELD_LAUNCH_APP_PARAMS);
        if (!StringUtil.isEmpty(launchAppParams)) {
            doLaunchApp(launchAppParams);
        }

        // 检查定位信息是否有效
        boolean isLocationValid = false;
        String locationStr = Hawk.get(SPField.FIELD_AMAP_LOCATION, "");
        SLog.info("locationStr[%s]", locationStr);

        if (!StringUtil.isEmpty(locationStr)) {
            Location location = null;
            try {
                location = (Location) EasyJSONBase.jsonDecode(Location.class, locationStr);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

            if (System.currentTimeMillis() - location.timestamp < Config.LOCATION_EXPIRE) {
                SLog.info("定位數據有效");
                isLocationValid = true;
            } else {
                SLog.info("定位數據過期");
                isLocationValid = false;
            }
        }

        if (!isLocationValid) { // 如果定位数据无效，请求重新定位
            TencentLocationTask.doLocation(this);
        }
    }

    private void doLaunchApp(String launchAppParams) {
        EasyJSONObject params = EasyJSONObject.parse(launchAppParams);
        Hawk.delete(SPField.FIELD_LAUNCH_APP_PARAMS);
        try {
            String host = params.getSafeString("host");

            SLog.info("host[%s]", host);
            if ("weburl".equals(host)) { // 1 跳轉網頁
                // url=xx&isNeedLogin=yy&title=zz
                int isNeedLogin = params.getInt("isNeedLogin");
                if (isNeedLogin == Constant.TRUE_INT && !User.isLogin()) {
                    Util.showLoginFragment();
                    return;
                }

                String url = params.getSafeString("url");
                url = new String(Base64.decode(url, Base64.DEFAULT));  // Base64解码
                String title = params.getSafeString("title");
                SLog.info("url[%s], title[%s]", url, title);

                Util.startFragment(H5GameFragment.newInstance(url, true));
            } else if ("store".equals(host)) { // 2 跳轉店鋪首頁
                int storeId = params.getInt("storeId");
                Util.startFragment(ShopMainFragment.newInstance(storeId));
            } else if ("goods".equals(host)) { // 3 跳轉商品詳情
                int commonId = params.getInt("commonId");
                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
            } else if ("memberinfo".equals(host)) { // 4 个人名片页
                String memberName = params.getSafeString("memberName");
                Util.startFragment(MemberInfoFragment.newInstance(memberName));
            } else if ("postinfo".equals(host)) { // 5 贴文詳情页
                int postId = params.getInt("postId");
                Util.startFragment(PostDetailFragment.newInstance(postId));
            } else if ("recruitinfo".equals(host)) { // 6 招聘詳情页
                int postId = params.getInt("postId");
                WantedPostItem wantedPostItem = new WantedPostItem();
                wantedPostItem.postId = postId;
                Util.startFragment(JobDetailFragment.newInstance(wantedPostItem));
            } else if ("activityindex".equals(host) || "home".equals(host)) { // 7 購物專場 8 商城首页
                if ("activityindex".equals(host)) {
                    Util.startActivityShopping();
                } else { // 8 商城首页
                    // 什么也不需要做
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    /**
     * 檢測版本更新接口
     * @param showResult 是否顯示結果信息
     */
    public void checkUpdate(boolean showResult) {
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

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(MainActivity.this, responseObj)) {
                        return;
                    }

                    if (!responseObj.exists("datas.version")) {
                        // 如果服務器端沒有返回版本信息，也當作是最新版本
                        if (showResult) {
                            ToastUtil.info(MainActivity.this, getString(R.string.text_latest_version_message));
                        }
                        return;
                    }

                    // 當前版本
                    String currentVersion = BuildConfig.VERSION_NAME;
                    // 最新版本
                    String newestVersion = responseObj.getSafeString("datas.version");

                    SLog.info("currentVersion[%s], newestVersion[%s]", currentVersion, newestVersion);
                    int result = Util.versionCompare(currentVersion, newestVersion);
                    SLog.info("result[%d]", result);

                    if (result >= 0) {
                        if (showResult) {
                            ToastUtil.info(MainActivity.this, getString(R.string.text_latest_version_message));
                        }

                        return;
                    }

                    // 當前的版本是舊版本
                    boolean isDismissOnBackPressed = true;
                    boolean isDismissOnTouchOutside = true;
                    boolean isForceUpdate = (responseObj.getInt("datas.isForceUpdate") != 0);

                    String version = responseObj.getSafeString("datas.version");
                    String versionDesc = responseObj.getSafeString("datas.remarks");
                    String appUrl = null;
                    if (responseObj.exists("datas.appUrl")) {
                        appUrl = responseObj.getSafeString("datas.appUrl");
                    }

                    SLog.info("datas.appUrl[%s]", appUrl);


                    // isForceUpdate = true;
                    // 如果是強制升級，點擊對話框外的區域或按下返回鍵，也不能關閉對話框
                    if (isForceUpdate) {
                        isDismissOnBackPressed = false;
                        isDismissOnTouchOutside = false;
                    }

                    String today = new Jarbon().toDateString();
                    String appUpdatePopupShownDate = Hawk.get(SPField.FIELD_APP_UPDATE_POPUP_SHOWN_DATE);
                    if (!showResult && !isForceUpdate && today.equals(appUpdatePopupShownDate)) { // 如果不是強制升級，并且今天已經顯示過升級對話框，則不再顯示
                        SLog.info("如果不是強制升級，并且今天已經顯示過升級對話框，則不再顯示");
                        return;
                    }

                    new XPopup.Builder(MainActivity.this)
                            .dismissOnBackPressed(isDismissOnBackPressed) // 按返回键是否关闭弹窗，默认为true
                            .dismissOnTouchOutside(isDismissOnTouchOutside) // 点击外部是否关闭弹窗，默认为true
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new AppUpdatePopup(MainActivity.this, version, versionDesc, isForceUpdate, appUrl))
                            .show();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;

        EventBus.getDefault().unregister(this);

        // 退出的时候销毁 engine
        engine.destroy();
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


            // MPay支付成功，通知服務器
            String token = User.getToken();
            int userId = User.getUserId();
            String key = String.format(SPField.FIELD_MPAY_PAY_ID, userId);
            SLog.info("key[%s]", key);
            String payData = Hawk.get(key, "");
            SLog.info("payData[%s]", payData);
            EasyJSONObject payDataObj = EasyJSONObject.parse(payData);
            if (payDataObj == null) {
                return;
            }

            try {
                long timestampMillis = payDataObj.getLong("timestampMillis");
                int payId = payDataObj.getInt("payId");

                Util.startFragment(PaySuccessFragment.newInstance(payId));

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

                                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
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
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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

        SLog.info("__requestCode[%d], resultCode[%d]", requestCode, resultCode);
        if (requestCode == RequestCode.REQUEST_INSTALL_APP_PERMISSION.ordinal()) { // 不用判斷resultCode，因為有時候是按返回鍵的
            SLog.info("here_0");
            installUpdate(updateApkPath);
        }
    }


    /**
     * 安裝升級apk
     * 參考:
     * Android8.0未知来源应用安装权限最好的适配方案  https://zhuanlan.zhihu.com/p/32386135
     * @param path 升級文件的路徑
     */
    public void installUpdate(String path) {
        updateApkPath = path;
        if (path == null) {
            SLog.info("Error!下載升級包失敗");
            ToastUtil.error(this, "下載升級包失敗");
            return;
        }
        boolean haveInstallPermission = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
        }

        if (!haveInstallPermission) {
            ToastUtil.info(this, "升級App需要開啟應用更新權限");
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            startActivityForResult(intent, RequestCode.REQUEST_INSTALL_APP_PERMISSION.ordinal());
            return;
        }
        File apkFile = new File(path);

        SLog.info("file size[%s]", apkFile.length());
        Util.openApkFile(apkFile, this);
    }

    /**
     * 打开微信授权界面
     * @param usage 微信授權用于什么用途
     */
    public void doWeixinLogin(int usage) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = EasyJSONObject.generate(
                "timestamp", System.currentTimeMillis(),
                "usage", usage).toString();
        TwantApplication.wxApi.sendReq(req);
    }
    /**
     * 重啟app
     */
    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setCurrChatMemberName(String currChatMemberName) {
        this.currChatMemberName =currChatMemberName;
    }

    public boolean getMessageFragmentsActivity() {
        return messageFragmentsActivity;
    }
}
