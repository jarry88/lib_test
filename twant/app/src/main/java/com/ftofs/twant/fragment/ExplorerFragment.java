package com.ftofs.twant.fragment;

import android.net.http.SslError;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BasePopupView;

/**
 * 瀏覽器Fragment
 * @author zwm
 */
public class ExplorerFragment extends BaseFragment implements View.OnClickListener {
    BasePopupView loadingPopup;
    String url;
    boolean ignoreSslError;

    TextView tvTitle;
    WebView webView;
    public static ExplorerFragment newInstance(String url, boolean ignoreSslError) {
        Bundle args = new Bundle();

        args.putString("url", url);
        args.putBoolean("ignoreSslError", ignoreSslError);
        ExplorerFragment fragment = new ExplorerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);
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

        tvTitle = view.findViewById(R.id.tv_title);

        webView = view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                loadingPopup.dismiss();

                String title = view.getTitle();
                if (!StringUtil.isEmpty(title)) {
                    // 设置标题
                    tvTitle.setText(title);
                }
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

        loadingPopup = Util.createLoadingPopup(_mActivity).show();
        webView.loadUrl(url);
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
