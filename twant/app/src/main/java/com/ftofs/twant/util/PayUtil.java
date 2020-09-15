package com.ftofs.twant.util;



import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.fragment.PaySuccessFragment;
import com.gzp.lib_common.utils.SLog;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PayUtil {
    public static final int VENDOR_TAIFUNG = 1;
    public static final int VENDOR_WEIXIN = 2;
    public static final int VENDOR_ALI = 3;
    public static final int VENDOR_ALI_HK = 4;

    public static String getVendorName(int vendor) {
        if (vendor == VENDOR_TAIFUNG) {
            return "大豐";
        } else if (vendor == VENDOR_WEIXIN) {
            return "微信";
        } else if (vendor == VENDOR_ALI) {
            return "支付寶";
        } else if (vendor == VENDOR_ALI_HK) {
            return "支付寶香港";
        }

        return null;
    }

    /**
     * 支付成功處理(大豐、微信、支付寶、支付寶香港)
     * @param notifyServer  是否通知服務器端
     * @param startPaySuccessFragment 是否要顯示支付成功頁面
     */
    public static void onPaySuccess(boolean notifyServer, boolean startPaySuccessFragment, int payId, int vendor) {
        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL, null);
        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);

        if (Config.PROD) {
            MobclickAgent.onEvent(TwantApplication.Companion.get(), UmengAnalyticsActionName.PAY_SUCCESS);
        }

        if (startPaySuccessFragment) {
            Util.startFragment(PaySuccessFragment.newInstance(payId));
        }

        if (notifyServer) {
            paySuccessNotify(payId, vendor);
        }
    }

    public static void onPaySuccess(boolean notifyServer, int payId, int vendor) {
        onPaySuccess(notifyServer, true, payId, vendor);
    }


    /**
     * 支付成功通知服務器
     */
    public static void paySuccessNotify(int payId, int vendor) {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token, "payId", payId);

        String path = null;
        if (vendor == VENDOR_TAIFUNG) {
            path = Api.PATH_TAIFUNG_PAY_FINISH;
        } else if (vendor == VENDOR_WEIXIN) {
            path = Api.PATH_WXPAY_FINISH;
        } else if (vendor == VENDOR_ALI) {
            path = Api.PATH_ALIPAY_FINISH;
        } else if (vendor == VENDOR_ALI_HK) {
            path = Api.PATH_ALIPAY_HK_FINISH;
        }

        SLog.info("path[%s], params[%s]", path, params);
        Api.getIO(path, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SLog.info("%s支付成功，通知服務器失敗", getVendorName(vendor));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        SLog.info("%s支付成功，通知服務器失敗", getVendorName(vendor));
                        return;
                    }

                    SLog.info("%s支付成功，通知服務器成功", getVendorName(vendor));
                } catch (Exception e) {

                }
            }
        });
    }
}
