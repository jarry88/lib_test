package com.ftofs.twant.fragment;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.Guid;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;
import com.macau.pay.sdk.MacauPaySdk;
import com.macaupay.sdk.AcpService;
import com.macaupay.sdk.SDKConstants;
import com.macaupay.sdk.SDKUtil;

import java.io.File;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    CountDownTimer countDownTimer;
    TextView tvCountDown;


    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCountDown = view.findViewById(R.id.tv_count_down);
        Util.setOnClickListener(view, R.id.btn_test, this);

        countDownTimer = new CountDownTimer(20 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SLog.info("millisUntilFinished");
                tvCountDown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                SLog.info("onFinish");
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_test) {
            SLog.info("btn_test");
            // MainFragment mainFragment = MainFragment.getInstance();
            // mainFragment.start(RegisterConfirmFragment.newInstance("0086", "13417785707", 10));


            EasyJSONObject bizContent = EasyJSONObject.generate(
                    "out_trade_no", Guid.getSpUuid(),
                    "total_amount", 0.01,
                    "subject", "Iphone6 16G",
                    "trade_mode", "0002",
                    "body", "Iphone6 16G",
                    "order_no", Guid.getSpUuid(),
                    "goods_id", "apple-01",
                    "goods_name", "ipad",
                    "quantity", 1,
                    "price", 0.01
            );


            // String sign = AcpService.sign(bizContent.toString(), SDKConstants.UTF_8_ENCODING);

            // AssetManager assetsManager = _mActivity.getAssets();
            // AssetFileDescriptor fd = assetsManager.openFd("dd");


            AssetsUtil.copyAssetAndWrite(_mActivity, "test.pfx");

            String sign = SDKUtil.sign(bizContent.toString(), "/data/user/0/com.ftofs.twant/cache/test.pfx", "12345678", SDKConstants.UTF_8_ENCODING);
            SLog.info("sign[%s]", sign);


            EasyJSONObject params = EasyJSONObject.generate(
                    "app_id", "00000000046326",
                    "api_name", "preCreate",
                    "biz_api_code", "100020007",
                    "data_type", "JSON",
                    "charset", "utf-8",
                    "sign_type", "RSA",
                    "sign", sign,
                    "timestamp", Time.date("Y-m-d H:i:s"),
                    "version", "1.0",
                    "biz_content", bizContent.toString()
            );


            SLog.info("params[%s]", params.toString());
            // MacauPaySdk.aliPay(_mActivity, params.toString(), (MainActivity) _mActivity);
            MacauPaySdk.macauPay(_mActivity, params.toString(), (MainActivity) _mActivity);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
