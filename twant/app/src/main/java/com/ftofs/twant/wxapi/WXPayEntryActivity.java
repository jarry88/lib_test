package com.ftofs.twant.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.widget.PayPopup;
import com.orhanobut.hawk.Hawk;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.snailpad.easyjson.EasyJSONObject;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_pay_entry);

        api = WXAPIFactory.createWXAPI(this, getString(R.string.weixin_app_id));
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        SLog.info("onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("支付结果");
            builder.setMessage("返回碼:" + resp.errCode + resp.transaction);
            builder.show();
            */
            SLog.info("返回碼[%d], resp[%s]", resp.errCode, resp.toString());

            if (resp.errCode == 0) {
                handleWXPaySuccess();
            } else {
                ToastUtil.error(this, "支付失敗");
            }
            finish();
        }
    }

    private void handleWXPaySuccess() {
        // 微信支付成功，通知服務器
        String token = User.getToken();
        int userId = User.getUserId();
        String key = String.format(SPField.FIELD_WX_PAY_ID, userId);
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
            PayPopup.onWXPaySuccess(true, payId);
        } catch (Exception e) {

        }
    }
}
