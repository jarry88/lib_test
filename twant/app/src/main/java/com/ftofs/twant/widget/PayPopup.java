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
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.macau.pay.sdk.MPaySdk;
import com.orhanobut.hawk.Hawk;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.vivebest.taifung.api.PaymentHandler;
import com.vivebest.taifung.api.TaifungSDK;

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

            String uuid = "2c0d22009f91bf3bcee8a715ae1764b8ac95a4b2";
            String merCustNo = "Tank Want1";
            String secretKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC/KuuLqkSm6ti6jEmqwbU6HNFMQhfYucxdTBJTr90P+neNOge0dVDLIqnazTAWAwW+d57qFk5uU5p7naP+mejOmOEd7+TLcDfy4wzmtmFFSC1TGa+8UaVhEpoiUltLViAEFgxJ5VP1VcRR2RBWMxKflUfoAvzQBs5x+t7OozYmHbV2AwmjngJy6KIjXe6pqHpWzWYy9WW9rJnLL2obUq7btzghogQy3S+R3axALoGiAGL0Vec3mreTY0RmyEXVeDVBUgd7mq2IIh9W2/x47uAz/61++udb3pD9W1Ue48nQhkhJ9Ow4g7PRXIibHL1lK6SMtFLi/hsa/XWziw3CRyhRAgMBAAECggEAU3WliEA54LW/ERKWRtpzCH/0UFq6ln/nXQQNLEQnOwaakym2m25sa4MirMfQKov/QfxvgLtkWn5df4J/SnRfU3MjNTK6rKa9hmjiBQeyx9CPGSypsInkrdC1Qi66dNWQ/Lezfb+FPCLJpwIhQ8DgbJN75SsIvLl7//8KryRKS7EDMP4mEw25f9F7eKv0HdqMSlO3gR7vkUvz/xRymo+SABFjMUYP5mIJMe3h/c/uJX+3Ege3U+RP5aETvDq+toDpAkEwsBk0F64JeAVpi236Ho7pPcaehnTL9c2pjvOHbwaJsK3HGrU49zS9oBH3TXIrmaqzmpjwiaebUxPBFWex0QKBgQDriHXBTZY9kPsQwXnY3BsWujov28cyV7H5pu0PAG4ijHqG74Q1Llo5AYCqjO008UnHURGhyOuWTYXs8VUWNjwjok1y5kn8+dyGAb+9QU1lCT8+/WfbxPJEleCcKNlvv6qtJoo6M65V9X4a+gmdPOnbyLlZc15V3g+jCr0NdnVwUwKBgQDPx4gjI+1jMzbe2MCA8IfAQxD1hNNclvFhYEYQGYySwTso7CxUHRTX9uhVqhNrEADUYj+ka5wFO4xyJVThzh8w+t55cYt9VWJBfMZWwlFxUfen+jiRSc2tVXaBTa2sG4qJgFSLOBSlE1OqiP4D/xYTh9V94VJLbJlQ3SL/v7vASwKBgAUf+P/1wjkguHXK3+3aDDTYZH+6FoF/6v11pl7XMY5K5Defao8FrSzkXXpYiqjGP0a4+ts8VfP1R965+ZH8KB7WXz0Kyb1ZanT4AMYLb7WtF9U1Cld715GqeTKsqN6Hmx0dY5CUo0x3hQDtQ9xKAQSpP2801W6k4E4545cxZqjFAoGASaohLuw25nuq0XkhTtV4G4brhVAxK1tseqyKSnz0ZLdTRR/uW2fwEt0749snhUaoNKQckiuApi5FjdaEcIYGcvQOWhoSbT4PVs5o9ytvenCoEArbcU8sN27cU915XFJrXHJ+Btm8IAZpHEXzYPFYH1aCL13qMklvVccA+JE+fF8CgYEA1maDuZLKV2wcrx2MNkbSanpjo8Tvnl8XuM/Kec9Aoj2dxg+YmC/5EIZVjRwWhkzv07whxZrHczOYZp/og7WuTtq/9rFWQ9bFkPYhXihlBQjftf6ZhWIQ046SpmwTcbeCQ64ocULHkwQcjSRRI63XpW3Yr2xsWHNAwFPzrEyIW9o=";
            String tfToken = "";  // 可為空
            String serverUrl;
            if (Config.DEVELOPER_MODE) {
                serverUrl = "https://test.taifungbank.com/payment-web/";
            } else {
                serverUrl = "https://epay.taifungbank.com/payment-web/";
            }
            String merTokenNotifyUrl = "https://www.baidu.com";

            TaifungSDK.startPay(MainActivity.getInstance(), uuid, merCustNo, secretKey,
                    tfToken, serverUrl, merTokenNotifyUrl, new PaymentHandler() {
                @Override
                public void handlePaymentResult(Intent data) {
                    if (data != null) {
                        /*
                         * code：支付結果碼 -1：失敗、 0：取消、1：成功
                         * error_msg：支付結果信息
                         */
                        int code = data.getExtras().getInt("code");
                        String errorMsg = data.getExtras().getString("result");
                        SLog.info("code[%d], errorMsg[%s]", code, errorMsg);
                    } }
            });
        } else if (id == R.id.btn_weixin_pay) {
            PayReq req = new PayReq();
            req.appId           = context.getString(R.string.weixin_app_id);//你的微信appid
            req.partnerId       = "1900000109";//商户号
            req.prepayId        = "WX1217752501201407033233368018";//预支付交易会话ID
            req.nonceStr        = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";//随机字符串
            req.timeStamp       = "1412000000";//时间戳
            req.packageValue    = "Sign=WXPay"; // 扩展字段,这里固定填写Sign=WXPay
            req.sign            = "C380BEC2BFD727A4B6845133519F3AD6"; //签名
            //              req.extData         = "app data"; // optional
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            TwantApplication.wxApi.sendReq(req);
        }
    }
}
