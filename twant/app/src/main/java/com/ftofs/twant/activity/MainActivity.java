package com.ftofs.twant.activity;


import android.os.Bundle;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.macau.pay.sdk.base.PayResult;
import com.macau.pay.sdk.interfaces.MPaySdkInterfaces;

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

        mainFragment = findFragment(MainFragment.class);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            loadRootFragment(R.id.main_fragment_container, mainFragment);
        }

        int color = getResources().getColor(R.color.tw_red, null);
        Util.setWindowStatusBarColor(this, color);
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
