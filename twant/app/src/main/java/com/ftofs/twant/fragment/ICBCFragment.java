package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.Uri;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * ICBC支付
 */
public class ICBCFragment extends BaseFragment {
    private static final String TAG = ICBCFragment.class.getSimpleName();

    public static ICBCFragment newInstance(String token, String payId) {
        Bundle args = new Bundle();
        args.putString("token", token);
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

        final WebView paymentView = view.findViewById(R.id.wv_pay);
        final RelativeLayout payResult = view.findViewById(R.id.rl_pay_result);

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
            String token = getArguments().getString("token");
            String payId = getArguments().getString("payId");
            String payUrl = Uri.API_ICBC_PAY.concat(String.format("?payId=%s&token=%s", payId, token));
            paymentView.loadUrl(payUrl);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    paymentView.setVisibility(View.INVISIBLE);
                    payResult.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new EBMessage(EBMessageType.MESSAGE_TYPE_ICBC_PAY_FINISH, null));
                }
            },3000);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_ICBC_PAY_FINISH) {

        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
