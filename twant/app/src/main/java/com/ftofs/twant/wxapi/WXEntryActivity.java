package com.ftofs.twant.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.gzp.lib_common.utils.SLog;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import cn.snailpad.easyjson.EasyJSONObject;

// 如果在AndroidManifest.xml中为WXEntryActivity设置@android:style/Theme.NoDisplay这种theme，必须继承自Activity，不能继承自AppCompatActivity
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        SLog.info("WXEntryActivity.onCreate()");
        TwantApplication.Companion.get().getWxApi().handleIntent(getIntent(), this);
    }
    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {
        SLog.info("WXEntryActivity.onReq()");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        SLog.info("WXEntryActivity.onResp()");
        SLog.info("errCode[%s], errStr[%s]", baseResp.errCode, baseResp.errStr);

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:  // 用户拒绝授权
            case BaseResp.ErrCode.ERR_USER_CANCEL:  // 用户取消
                if (baseResp.getType() == RETURN_MSG_TYPE_LOGIN) {
                    SLog.info("登录失败!");
                } else if (baseResp.getType() == RETURN_MSG_TYPE_SHARE) {
                    SLog.info("分享失败!");
                }
                break;
            case BaseResp.ErrCode.ERR_OK:
                if (baseResp.getType() == RETURN_MSG_TYPE_LOGIN) {
                    SendAuth.Resp authResp = (SendAuth.Resp) baseResp;
                    //拿到了微信返回的code,立马再去请求access_token
                    String code = authResp.code;
                    String state = authResp.state;
                    SLog.info("微信返回code[%s], state[%s]", code, state);

                    EasyJSONObject stateObj = EasyJSONObject.parse(state);
                    int usage = Constant.WEIXIN_AUTH_USAGE_LOGIN;
                    try {
                        usage = stateObj.getInt("usage");
                    } catch (Exception e) {

                    }
                    if (usage == Constant.WEIXIN_AUTH_USAGE_LOGIN) {
                        SLog.info("發送了衛星信息%s", code);

                        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_WEIXIN_LOGIN, code);
                    } else if (usage == Constant.WEIXIN_AUTH_USAGE_UNBIND) {
                        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_WEIXIN_UNBIND, code);
                    } else if (usage == Constant.WEIXIN_AUTH_USAGE_BIND) {
                        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_WEIXIN_BIND, code);
                    }


                } else if (baseResp.getType() == RETURN_MSG_TYPE_SHARE) {
                    SLog.info("分享成功!");
                }
                break;
        }

        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SLog.info("WXEntryActivity.onDestroy()");
    }
}
