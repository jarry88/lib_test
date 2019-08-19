package com.ftofs.twant.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.fragment.PaySuccessFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Guid;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.macau.pay.sdk.MPaySdk;
import com.orhanobut.hawk.Hawk;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.umeng.commonsdk.debug.E;
import com.vivebest.taifung.api.PaymentHandler;
import com.vivebest.taifung.api.TaifungSDK;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 支付類型選擇彈窗
 * @author zwm
 */
public class PayPopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    MainActivity mainActivity; // 主Activity
    int payId;

    public PayPopup(@NonNull Context context, MainActivity mainActivity, int payId) {
        super(context);

        this.context = context;
        this.mainActivity = mainActivity;
        this.payId = payId;
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
        findViewById(R.id.btn_taifung_pay).setOnClickListener(this);
        findViewById(R.id.btn_weixin_pay).setOnClickListener(this);
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
        } else if (id == R.id.btn_mpay) {  // MPay支付
            String token = User.getToken();

            if (StringUtil.isEmpty(token)) {
                ToastUtil.error(mainActivity, "用戶未登錄");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "payId", payId);

            SLog.info("params[%s]", params);
            Api.postUI(Api.PATH_MPAY, params, new UICallback() {
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

                        ToastUtil.success(mainActivity, "提交訂單成功，正在打開支付界面，請稍候...");
                        dismiss();

                        EasyJSONObject datas = (EasyJSONObject) responseObj.get("datas");
                        MPaySdk.mPay(mainActivity, datas.toString(), mainActivity);

                        int userId = User.getUserId();
                        if (userId > 0) {
                            String key = String.format(SPField.FIELD_MPAY_PAY_ID, userId);
                            SLog.info("key[%s]", key);
                            Hawk.put(key, EasyJSONObject.generate("payId", payId, "timestampMillis", System.currentTimeMillis()).toString());
                        }
                    } catch (Exception e) {

                    }
                }
            });
        } else if (id == R.id.btn_taifung_pay) {
            SLog.info("大豐支付");

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "payId", payId);

            SLog.info("params[%s]", params);
            Api.getUI(Api.PATH_TAIFUNG_PAY, params, new UICallback() {
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

                        // 有些商品抵扣優惠券后，金額變為0，就不需要走支付流程了,直接跳轉到支付成功頁面
                        if (responseObj.exists("datas.isPay")) {
                            int isPay = responseObj.getInt("datas.isPay");
                            if (isPay == 1) {
                                // isPay為1 表示已經支付，無需請求大豐SDK
                                onTaifungPaySuccess(false);
                                dismiss();
                                return;
                            }
                        }

                        String uuid = responseObj.getString("datas.tx_uuid");
                        String notifyUrl = responseObj.getString("datas.notify_url");

                        ToastUtil.success(mainActivity, "提交訂單成功，正在打開支付界面，請稍候...");
                        dismiss();

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
                                                ToastUtil.error(context, errorMsg);
                                            } else if (code == 0) {
                                                ToastUtil.info(context, errorMsg);
                                            } else if (code == 1) {
                                                ToastUtil.success(context, errorMsg);

                                                onTaifungPaySuccess(true);
                                            }
                                        }
                                    }
                                });
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else if (id == R.id.btn_weixin_pay) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "payId", payId);

            SLog.info("params[%s]", params);
            Api.getUI(Api.PATH_WXPAY, params, new UICallback() {
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

                        // 有些商品抵扣優惠券后，金額變為0，就不需要走支付流程了,直接跳轉到支付成功頁面
                        if (responseObj.exists("datas.isPayed")) {
                            int isPay = responseObj.getInt("datas.isPayed");
                            if (isPay == 1) {
                                // isPay為1 表示已經支付，無需請求微信SDK
                                onWXPaySuccess(false, payId);
                                dismiss();
                                return;
                            }
                        }

                        ToastUtil.success(mainActivity, "提交訂單成功，正在打開支付界面，請稍候...");
                        dismiss();
                        EasyJSONObject payData = responseObj.getObject("datas.payData");

                        PayReq req = new PayReq();
                        req.appId           = context.getString(R.string.weixin_app_id);//你的微信appid
                        req.partnerId       = context.getString(R.string.weixin_partner_id);//商户号
                        req.prepayId        = payData.getString("prepayid");//预支付交易会话ID
                        req.nonceStr        = payData.getString("noncestr");//随机字符串
                        req.timeStamp       = payData.getString("timestamp");//时间戳
                        req.packageValue    = "Sign=WXPay"; // 扩展字段,这里固定填写Sign=WXPay
                        req.sign            = payData.getString("sign"); //签名
                        // req.extData         = "app data"; // optional

                        SLog.info("req.prepayId[%s], req.sign[%s], nonceStr[%s], timeStamp[%s]",
                                req.prepayId, req.sign, req.nonceStr, req.timeStamp);
                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信

                        TwantApplication.wxApi.sendReq(req);

                        int userId = User.getUserId();
                        if (userId > 0) {
                            String key = String.format(SPField.FIELD_WX_PAY_ID, userId);
                            SLog.info("key[%s]", key);
                            Hawk.put(key, EasyJSONObject.generate("payId", payId, "timestampMillis", System.currentTimeMillis()).toString());
                        }

                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } else if (id == R.id.btn_ali_pay) {

        }
    }

    /**
     * 大豐支付成功處理
     * @param notifyServer  是否通知服務器端
     */
    private void onTaifungPaySuccess(boolean notifyServer) {
        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL, null);
        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);

        Util.startFragment(PaySuccessFragment.newInstance(""));

        if (notifyServer) {
            taifungPaySuccessNotify();
        }
    }


    /**
     * 大豐支付成功通知服務器
     */
    private void taifungPaySuccessNotify() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token, "payId", payId);

        Api.getIO(Api.PATH_TAIFUNG_PAY_FINISH, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SLog.info("大豐支付成功，通知服務器失敗");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        SLog.info("大豐支付成功，通知服務器失敗");
                        return;
                    }

                    SLog.info("大豐支付成功，通知服務器成功");
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 微信支付成功處理
     * @param notifyServer  是否通知服務器端
     */
    public static void onWXPaySuccess(boolean notifyServer, int payId) {
        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL, null);
        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);

        Util.startFragment(PaySuccessFragment.newInstance(""));

        if (notifyServer) {
            wxPaySuccessNotify(payId);
        }
    }


    /**
     * 微信支付成功通知服務器
     */
    public static void wxPaySuccessNotify(int payId) {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token, "payId", payId);

        Api.getIO(Api.PATH_WXPAY_FINISH, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SLog.info("微信支付成功，通知服務器失敗");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        SLog.info("微信支付成功，通知服務器失敗");
                        return;
                    }

                    SLog.info("微信支付成功，通知服務器成功");
                } catch (Exception e) {

                }
            }
        });
    }
}
