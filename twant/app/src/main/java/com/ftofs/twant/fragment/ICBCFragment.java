package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ftofs.twant.R;
import com.ftofs.twant.api.HttpHelper;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.constant.Uri;
import com.ftofs.twant.entity.EBMessage;

import com.ftofs.twant.log.SLog;

import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.alibaba.fastjson.JSONObject;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * ICBC支付
 */
public class ICBCFragment extends BaseFragment {
    private static final String TAG = ICBCFragment.class.getSimpleName();
    //支付WebView
    WebView paymentView;
    //支付結果layout
    RelativeLayout payResult;
    //支付結果
    TextView payResultInfo;
    //支付地址
    String payUrl = "";
    //查詢地址
    String enquiryUrl = "";
    //輪詢
    Handler queryHandler = new Handler();
    Runnable queryRunnable;
    //每五秒輪詢一次
    Integer postFrequency = 5000;
    //允許輪詢次數
    Integer maxQueryTimes = 12;
    //輪詢次數
    Integer queryCount = 0;

    public static ICBCFragment newInstance(String payId) {
        Bundle args = new Bundle();
        args.putString("payId", payId);

        ICBCFragment fragment = new ICBCFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icbc, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        paymentView = view.findViewById(R.id.wv_pay);
        payResult = view.findViewById(R.id.rl_pay_result);
        payResultInfo = view.findViewById(R.id.tv_pay_result);

        paymentView.getSettings().setJavaScriptEnabled(true);
        paymentView.clearCache(true);
        paymentView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        if (getArguments() != null) {
            String token = User.getToken();
            String payId = getArguments().getString("payId");
            payUrl = Uri.API_ICBC_PAY.concat(String.format("?payId=%s&token=%s", payId, token));
            enquiryUrl = Uri.API_ICBC_PAY_ENQUIRY.concat(String.format("?payId=%s&token=%s", payId, token));
            paymentView.loadUrl(payUrl);

            queryRunnable = new Runnable() {
                @Override
                public void run() {
                    if (queryCount > maxQueryTimes) {
                        if (queryHandler != null) {
                            queryHandler.removeCallbacks(queryRunnable);
                        }

                        //超時顯示支付失敗
                        EventBus.getDefault().post(new EBMessage(EBMessageType.MESSAGE_TYPE_ICBC_PAY_FINISH,
                                EasyJSONObject.generate("payResult", Constant.ZERO).toString()));
                    }

                    try {
                        String json = HttpHelper.get(enquiryUrl);
                        JSONObject jsonObject = JSON.parseObject(json);
                        if (ResponseCode.SUCCESS.equals(jsonObject.getInteger("code"))) {
                            if (Constant.ONE.equals(jsonObject.getJSONObject("datas").getInteger("payResult"))) {
                                //支付成功
                                EventBus.getDefault().post(new EBMessage(EBMessageType.MESSAGE_TYPE_ICBC_PAY_FINISH,
                                        EasyJSONObject.generate("payResult", Constant.ONE).toString()));
                            } else {
                                queryCount++;
                                queryHandler.postDelayed(this, postFrequency);
                            }
                        } else {
                            ToastUtil.error(_mActivity, ToastUtil.COMMON_ERROR_MESSAGE);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                }
            };

            queryHandler = new Handler();
            queryHandler.postDelayed(queryRunnable, postFrequency);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

        if (queryHandler != null) {
            queryHandler.removeCallbacks(queryRunnable);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_ICBC_PAY_FINISH) {
            EasyJSONObject easyJSONObject = (EasyJSONObject) EasyJSONObject.parse(message.data);
            try {
                if (easyJSONObject.getInt("payResult") == Constant.ONE) {
                    paymentView.setVisibility(View.INVISIBLE);
                    payResult.setVisibility(View.VISIBLE);
                    payResultInfo.setText(getResources().getString(R.string.text_pay_result_success));
                }
            } catch (EasyJSONException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
