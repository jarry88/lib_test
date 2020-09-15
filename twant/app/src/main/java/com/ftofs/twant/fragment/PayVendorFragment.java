package com.ftofs.twant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.PayVendorListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.UmengAnalyticsPageName;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.PayCardItem;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.PayUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.ftofs.twant.widget.WalletPayPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.macau.pay.sdk.MPaySdk;
import com.orhanobut.hawk.Hawk;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.umeng.analytics.MobclickAgent;
import com.vivebest.taifung.api.PaymentHandler;
import com.vivebest.taifung.api.TaifungSDK;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 選擇支付商
 *
 * @author zwm
 */
public class PayVendorFragment extends BaseFragment implements View.OnClickListener, CommonCallback {
    int walletStatus = Constant.WANT_PAY_WALLET_STATUS_UNKNOWN;

    int payId;

    /*
    防止快速重復點擊
     */
    long lastClickTime; // 上一次點擊的時間
    public static final long CLICKABLE_INTERVAL = 1500; // 兩次點擊之間最小的間隔時間(毫秒)

    /**
     * 想要錢包余額
     */
    double walletBalance;

    /**
     * 支付單金額
     */
    double payAmount;

    TextView tvWalletBalance;
    TextView tvPayAmount;

    TextView tvMPay;
    LinearLayout llMPayActivityContainer;
    ImageView iconMPayActivityLabel;
    List<PayCardItem> payCardItems = new ArrayList<>();
    RecyclerView rvPayVendorList;
    int rvPayListHeight;
    /**
     * 支付商按鈕Id與View的Map
     */
    Map<Integer, View> payVendorButtonMap = new HashMap<>();

    /**
     * 支付商按鈕Id與蒙板View的Map
     */
    Map<Integer, View> payMaskMap = new HashMap<>();

    /**
     * 選中的支付按鈕的Id(-1表示任何都未選中)
     */
    int selectedPayButtonId = -1;
    PayVendorListAdapter adpter;
    private String user_zone;
    private LinearLayout btnExpressContainer;
    private LinearLayout btnExpressOtherPay;
    private LinearLayout btnUpOtherPay;
    boolean isExpressed;
    private LinearLayout secondPayAmount;
    private TextView tvSecondAmount;
    private TextView tvSecondPayTag;


    /**
     * 構造方法
     *
     * @param walletBalance 想要錢包余額
     * @return
     */
    public static PayVendorFragment newInstance(int payId, double payAmount, double walletBalance) {
        Bundle args = new Bundle();

        args.putInt("payId", payId);
        args.putDouble("payAmount", payAmount);
        args.putDouble("walletBalance", walletBalance);
        PayVendorFragment fragment = new PayVendorFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_vendor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        user_zone =StringUtil.parseZone();
        payId = args.getInt("payId");
        if (Config.PROD) {
            MobclickAgent.onPageStart(UmengAnalyticsPageName.SELECT_PAYMENT);
        }

        payAmount = args.getDouble("payAmount");
        walletBalance = args.getDouble("walletBalance");

        tvPayAmount = view.findViewById(R.id.tv_pay_amount);
        secondPayAmount = view.findViewById(R.id.second_monny_container);
        tvSecondAmount = view.findViewById(R.id.tv_second_pay_amount);
        tvSecondPayTag = view.findViewById(R.id.tv_second_money);

        Hawk.put(SPField.FIELD_TOTAL_ORDER_AMOUNT, payAmount);


        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_pay, this);
        rvPayVendorList=view.findViewById(R.id.rv_pay_vendor_list);
        rvPayVendorList.setNestedScrollingEnabled(false);
        initPayCardList();
        btnExpressContainer = view.findViewById(R.id.btn_pay_express);
        btnExpressOtherPay = view.findViewById(R.id.btn_express_pay);
        rvPayListHeight = rvPayVendorList.getHeight();
        SLog.info("rvpaylist height %d",rvPayListHeight);
        btnExpressOtherPay.setOnClickListener(v -> {
            isExpressed = true;
            rvPayVendorList.setNestedScrollingEnabled(true);
            LinearLayout.LayoutParams rvParams= (LinearLayout.LayoutParams) rvPayVendorList.getLayoutParams();
            rvParams.height=0;
            rvParams.weight=1;
            rvPayVendorList.setLayoutParams(rvParams);
            btnUpOtherPay.setVisibility(View.VISIBLE);
            btnExpressOtherPay.setVisibility(View.GONE);
            rvPayVendorList.scrollToPosition(0);

        });
        btnUpOtherPay = view.findViewById(R.id.btn_up_pay);
        btnUpOtherPay.setOnClickListener(v->{
            isExpressed = false;
            rvPayVendorList.setNestedScrollingEnabled(false);
            rvPayVendorList.scrollToPosition(0);
            LinearLayout.LayoutParams rvParams= (LinearLayout.LayoutParams) rvPayVendorList.getLayoutParams();
            rvParams.height = Util.dip2px(_mActivity, 270);
            rvParams.weight =0;
            rvPayVendorList.setLayoutParams(rvParams);
            btnExpressOtherPay.setVisibility(View.VISIBLE);
            btnUpOtherPay.setVisibility(View.GONE);

        });

        getWalletBalance();
        loadMPayActivityStatus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (Config.PROD) {
            MobclickAgent.onPageEnd(UmengAnalyticsPageName.SELECT_PAYMENT);
        }
    }

    private void initPayCardList() {
        payCardItems.clear();
        payCardItems = PayCardItem.toItemList(user_zone);
        SLog.info("payCardSize %d ", payCardItems.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvPayVendorList.setLayoutManager(linearLayoutManager);
        adpter = new PayVendorListAdapter(R.layout.pay_card_item, payCardItems);
        adpter.setOnItemClickListener((adapter, view, position) -> {
            PayCardItem item = (PayCardItem) adapter.getItem(position);
            int type= item.payType;
            if (type == PayCardItem.PAY_TYPE_WALLET) {
                updateWallet();
            }
            if (type == selectedPayButtonId) { // 再次點擊，表示取消選擇
                selectedPayButtonId = -1;
                item.status = Constant.STATUS_UNSELECTED;

                for (PayCardItem payCardItem : payCardItems) {
                    payCardItem.showMask = false;
                }
            } else {
                for (PayCardItem payCardItem : payCardItems) {
                    payCardItem.status = Constant.STATUS_UNSELECTED;
                    payCardItem.showMask = true;
                }
                item.status = Constant.STATUS_SELECTED;
                // 顯示所有的蒙板，除了自己
                item.showMask = false;

                selectedPayButtonId = type;

            }
            adapter.notifyDataSetChanged();

        });
//        for (PayCardItem item : payCardItems) {
//            if (item.payType == PayCardItem.PAY_TYPE_WALLET) {
//                if (walletStatus == Constant.WANT_PAY_WALLET_STATUS_UNKNOWN) { // 如果錢包狀態未知，則不處理，等到獲取狀態先
//                    return;
//                }
//                if (payAmount > walletBalance) {
//                    ToastUtil.error(_mActivity, "余額不足");
//                    return;
//                }
//            }
//        }
        rvPayVendorList.setAdapter(adpter);
    }

    private void updateWallet() {
        if (walletStatus == Constant.WANT_PAY_WALLET_STATUS_NOT_ACTIVATED) { // 如果錢包未激活，跳轉到激活界面
            start(ResetPasswordFragment.newInstance(Constant.USAGE_SET_PAYMENT_PASSWORD, false));
            return;
        }
        if (walletStatus == Constant.WANT_PAY_WALLET_STATUS_UNKNOWN) { // 如果錢包狀態未知，則不處理，等到獲取狀態先
            return;
        }
        if (payAmount > walletBalance) {
            ToastUtil.error(_mActivity, "余額不足");
            return;
        }
    }


    /**
     * 加載MPay活動的狀態
     */
    private void loadMPayActivityStatus() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_PAYMENT_PRICE;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "payId", payId);

        SLog.info("params[%s]", params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    // responseStr = "{\"code\":200,\"datas\":{\"isMpayActivity\":0}}";

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    int isMpayActivity = responseObj.getInt("datas.isMpayActivity");
                    SLog.info("isMpayActivity[%d]", isMpayActivity);
                    if (isMpayActivity == Constant.TRUE_INT) {
                        if (payCardItems != null) {
                            for (PayCardItem item : payCardItems) {
                                if (item.payType == PayCardItem.PAY_TYPE_MPAY) {
                                    item.showActivityDesc = true;
                                }
                            }
                        }
                    }
                    String code = responseObj.getSafeString("datas.defaultCode");
                    payAmount = (float)responseObj.getDouble("datas.mopPrice");
                    SLog.info("使用後端返回價格");
                    tvPayAmount.setText(String.format("%.2f", payAmount));

                    String hk = "HK";
                    String cny = "CNY";

                    if (hk.equals(code)||cny.equals(code)) {
                        double secondPrice = responseObj.getDouble("datas.defaultPrice");
                        SLog.info("secondPrice %s",secondPrice);
                        updateSecondMoney(code,secondPrice);
                    }

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updateSecondMoney(String code, double secondPrice) {
        if ("CNY".equals(code)) {
            code += "¥";
        } else if ("HK".equals(code)) {
            code += "$";
        }
        tvSecondPayTag.setText(code);
        tvSecondAmount.setText(String.format("%.2f", secondPrice));
        secondPayAmount.setVisibility(View.VISIBLE);
    }


    private void getWalletBalance() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_WALLET_INFO;
        EasyJSONObject params = EasyJSONObject.generate("token", token);
        SLog.info("params[%s]", params);

        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    EasyJSONObject wantWallet = responseObj.getObject("datas.wantWallet");
                    if (payCardItems == null) {
                        return;
                    }
                    int i = 0;
                    for (PayCardItem item : payCardItems) {
                        if (item.payType == PayCardItem.PAY_TYPE_WALLET) {
                            break;
                        }
                        i++;
                    }
                    payCardItems.get(i).showBalance = true;
                    if (Util.isJsonNull(wantWallet)) { // 如果為null，表示未激活
                        payCardItems.get(i).textBalance = "(未激活)";
                        walletStatus = Constant.WANT_PAY_WALLET_STATUS_NOT_ACTIVATED;
                    } else {
                        walletStatus = Constant.WANT_PAY_WALLET_STATUS_ACTIVATED;

                        // 獲取余額
                        walletBalance = responseObj.getDouble("datas.memberInfo.predepositAvailable");
//                        tvWalletBalance.setText(String.format("(餘額：$%.2f)", walletBalance));
                        payCardItems.get(i).textBalance = String.format("(餘額：$%.2f)", walletBalance);
                    }

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();


        if (id == R.id.btn_back) {
            showCancelConfirm();
        } else if (id == R.id.btn_pay) {
            if (selectedPayButtonId == -1) {
                ToastUtil.error(_mActivity, "請選擇支付方式");
                return;
            }

            if (selectedPayButtonId == PayCardItem.PAY_TYPE_WEIXING) {
                // 檢測微信是否已經安裝
                if (!TwantApplication.Companion.get().getWxApi().isWXAppInstalled()) {
                    ToastUtil.error(_mActivity, getString(R.string.weixin_not_installed_hint));
                    return;
                }
            }

            // 防止支付快速點擊處理
            long now = System.currentTimeMillis();
            if (now - lastClickTime < CLICKABLE_INTERVAL) {
                return;
            }
            lastClickTime = now;

            switch (selectedPayButtonId) {
                case PayCardItem.PAY_TYPE_MPAY:
                    doMPay();
                    break;
                case PayCardItem.PAY_TYPE_ALI:
                    doAliPay();
                    break;
                case PayCardItem.PAY_TYPE_ALIHK:
                    doAliHKPay();
                    break;
                case PayCardItem.PAY_TYPE_WALLET:
                    doWalletPay();
                    break;
                case PayCardItem.PAY_TYPE_TAIFUNG:
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                        ToastUtil.error(_mActivity, "大豐銀行電子支付暫未支持Android 10");
//                        return;
//                    }
                    doTaiFungPay();
                    break;
                case PayCardItem.PAY_TYPE_WEIXING:
                    doWeixinPay();
                    break;
                default:
                    break;
            }
        }
    }



    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        showCancelConfirm();
        return true;
    }


    private void doWalletPay() {
        new XPopup.Builder(getContext())
                .popupAnimation(PopupAnimation.TranslateFromBottom)
                .hasStatusBarShadow(true)
                .asCustom(new WalletPayPopup(_mActivity, payId, payAmount, this))
                .show();
    }

    private void doMPay() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            ToastUtil.error(_mActivity, getString(R.string.text_user_not_login));
            return;
        }

        String url = Api.PATH_MPAY;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "payId", payId);

        SLog.info("params[%s]", params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    ToastUtil.success(_mActivity, getString(R.string.text_open_pay_ui));
                    hideSoftInputPop();

                    EasyJSONObject datas = (EasyJSONObject) responseObj.get("datas");
                    MPaySdk.mPayNew(_mActivity, datas.toString(), (MainActivity) _mActivity);

                    markPayId(SPField.FIELD_MPAY_PAY_ID);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void doTaiFungPay() {
        SLog.info("大豐支付");

        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_TAIFUNG_PAY;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "payId", payId);

        SLog.info("params[%s]", params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    // 有些產品抵扣優惠券后，金額變為0，就不需要走支付流程了,直接跳轉到支付成功頁面
                    if (responseObj.exists("datas.isPay")) {
                        int isPay = responseObj.getInt("datas.isPay");
                        if (isPay == 1) {
                            // isPay為1 表示已經支付，無需請求大豐SDK
                            PayUtil.onPaySuccess(false, payId, PayUtil.VENDOR_TAIFUNG);
                            hideSoftInputPop();
                            return;
                        }
                    }

                    String uuid = responseObj.getSafeString("datas.tx_uuid");
                    String notifyUrl = responseObj.getSafeString("datas.notify_url");

                    ToastUtil.success(_mActivity, getString(R.string.text_open_pay_ui));
                    hideSoftInputPop();

                    SLog.info("uuid[%s], notifyUrl[%s]", uuid, notifyUrl);
                    TaifungSDK.startPay(MainActivity.getInstance(), uuid, Config.TAIFUNG_PAY_MER_CUST_NO, Config.TAIFUNG_PAY_SECRET_KEY,
                            null, Config.TAIFUNG_PAY_SERVER_URL, notifyUrl, new PaymentHandler() {
                                @Override
                                public void handlePaymentResult(Intent data) {
                                    if (data != null) {
                                        /*
                                         * code：支付結果碼 -1：失敗、 0：取消、1：成功
                                         * error_msg：支付結果信息
                                         */
                                        int code = data.getExtras().getInt("code", 0);
                                        String errorMsg = data.getExtras().getString("result");
                                        SLog.info("code[%d], errorMsg[%s]", code, errorMsg);

                                        if (code == -1) {
                                            ToastUtil.error(_mActivity, errorMsg);
                                        } else if (code == 0) {
                                            ToastUtil.info(_mActivity, errorMsg);
                                        } else if (code == 1) {
                                            ToastUtil.success(_mActivity, errorMsg);

                                            PayUtil.onPaySuccess(true, payId, PayUtil.VENDOR_TAIFUNG);
                                        }
                                    }
                                }
                            });
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void doWeixinPay() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_WXPAY;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "payId", payId);

        SLog.info("params[%s]", params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    // 有些產品抵扣優惠券后，金額變為0，就不需要走支付流程了,直接跳轉到支付成功頁面
                    if (responseObj.exists("datas.isPayed")) {
                        int isPay = responseObj.getInt("datas.isPayed");
                        if (isPay == 1) {
                            // isPay為1 表示已經支付，無需請求微信SDK
                            PayUtil.onPaySuccess(false, payId, PayUtil.VENDOR_WEIXIN);
                            hideSoftInputPop();
                            return;
                        }
                    }

                    ToastUtil.success(_mActivity, getString(R.string.text_open_pay_ui));

                    EasyJSONObject payData = responseObj.getSafeObject("datas.payData");

                    PayReq req = new PayReq();
                    req.appId = getString(R.string.weixin_app_id);//你的微信appid
                    req.partnerId = getString(R.string.weixin_partner_id);//商户号
                    req.prepayId = payData.getSafeString("prepayid");//预支付交易会话ID
                    req.nonceStr = payData.getSafeString("noncestr");//随机字符串
                    req.timeStamp = payData.getSafeString("timestamp");//时间戳
                    req.packageValue = "Sign=WXPay"; // 扩展字段,这里固定填写Sign=WXPay
                    req.sign = payData.getSafeString("sign"); //签名
                    // req.extData         = "app data"; // optional

                    SLog.info("req.prepayId[%s], req.sign[%s], nonceStr[%s], timeStamp[%s]",
                            req.prepayId, req.sign, req.nonceStr, req.timeStamp);
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信

                    TwantApplication.Companion.get().getWxApi().sendReq(req);

                    markPayId(SPField.FIELD_WX_PAY_ID);

                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void doAliPay() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_ALIPAY;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "payId", payId);

        SLog.info("params[%s]", params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    // 有些產品抵扣優惠券后，金額變為0，就不需要走支付流程了,直接跳轉到支付成功頁面
                    if (responseObj.exists("datas.isPayed")) {
                        int isPay = responseObj.getInt("datas.isPayed");
                        if (isPay == 1) {
                            // isPay為1 表示已經支付，無需請求支付寶SDK
                            PayUtil.onPaySuccess(false, payId, PayUtil.VENDOR_ALI);
                            hideSoftInputPop();
                            return;
                        }
                    }

                    ToastUtil.success(_mActivity, getString(R.string.text_open_pay_ui));

                    String orderInfo = responseObj.getSafeString("datas.payData");
                    ((MainActivity) _mActivity).startAliPay(orderInfo);
                    markPayId(SPField.FIELD_ALI_PAY_ID);
                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void doAliHKPay() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_ALIPAY_HK;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "payId", payId);

        SLog.info("params[%s]", params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    // 有些產品抵扣優惠券后，金額變為0，就不需要走支付流程了,直接跳轉到支付成功頁面
                    if (responseObj.exists("datas.isPayed")) {
                        int isPay = responseObj.getInt("datas.isPayed");
                        if (isPay == 1) {
                            // isPay為1 表示已經支付，無需請求支付寶SDK
                            PayUtil.onPaySuccess(false, payId, PayUtil.VENDOR_ALI);
                            hideSoftInputPop();
                            return;
                        }
                    }

                    ToastUtil.success(_mActivity, getString(R.string.text_open_pay_ui));

                    String orderInfo = responseObj.getSafeString("datas.payData");
                    ((MainActivity) _mActivity).startAliPay(orderInfo);
                    markPayId(SPField.FIELD_ALI_PAY_HK_ID);
                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 記錄payId
     *
     * @param field
     */
    private void markPayId(String field) {
        int userId = User.getUserId();
        if (userId > 0) {
            String key = String.format(field, userId);
            SLog.info("key[%s]", key);
            Hawk.put(key, EasyJSONObject.generate("payId", payId, "timestampMillis", System.currentTimeMillis()).toString());
        }
    }

    @Override
    public String onSuccess(@Nullable String data) {
        SLog.info("想要錢包支付成功");

        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_WALLET_PAY_SUCCESS, payId);

        PayUtil.onPaySuccess(false, false, 0, 0);
        hideSoftInputPop();
        return null;
    }

    @Override
    public String onFailure(@Nullable String data) {
        SLog.info("想要錢包支付失敗");
        return null;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        btnUpOtherPay.performClick();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    /**
     * 顯示確認取消支付的提示框
     */
    private void showCancelConfirm() {
        new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
              .asCustom(
                        new TwConfirmPopup(_mActivity, "是否取消當前訂單的支付", null,
                //继续在右，优化用户体验
                "繼續購買", "確認取消"
                                , new OnConfirmCallback() {
            @Override
            public void onYes() {
                SLog.info("onNo");
            }

            @Override
            public void onNo() {
                SLog.info("onYes");
                hideSoftInputPop();
            }
        }))
                .show();
    }
}
