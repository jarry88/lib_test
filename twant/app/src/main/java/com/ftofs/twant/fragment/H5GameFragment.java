package com.ftofs.twant.fragment;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ftofs.twant.R;
import com.ftofs.twant.handler.NativeJsBridge;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

/**
 * H5游戲頁面
 * @author zwm
 */
public class H5GameFragment extends BaseFragment implements View.OnClickListener {
    BasePopupView loadingPopup;
    String url;
    boolean ignoreSslError;
    WebView webView;

    public static H5GameFragment newInstance(String url) {
        Bundle args = new Bundle();

        args.putString("url", url);
        H5GameFragment fragment = new H5GameFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_h5_game, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        url = args.getString("url");
        ignoreSslError = args.getBoolean("ignoreSslError");
        SLog.info("url[%s], ignoreSslError[%s]", url, ignoreSslError);

        Util.setOnClickListener(view, R.id.btn_back, this);


        webView = view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        NativeJsBridge nativeJsBridge = new NativeJsBridge();
        webView.addJavascriptInterface(nativeJsBridge, "android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                loadingPopup.dismiss();
            }

            // 如果忽略Ssl校驗錯誤
            // 一個提示不安全的網址   https://inv-veri.chinatax.gov.cn/
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (ignoreSslError) {
                    handler.proceed();//忽略证书的错误继续加载页面内容，不会变成空白页面
                } else {
                    //handler.cancel();// super中默认的处理方式，WebView变成空白页
                    super.onReceivedSslError(view, handler, error);
                }
            }
        });

        loadingPopup = new XPopup.Builder(_mActivity)
                .asLoading(getString(R.string.text_loading))
                .show();
        webView.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
