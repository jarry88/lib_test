package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

/**
 * 瀏覽器Fragment
 * @author zwm
 */
public class ExplorerFragment extends BaseFragment implements View.OnClickListener {
    BasePopupView loadingPopup;
    String url;
    TextView tvTitle;
    WebView webView;
    public static ExplorerFragment newInstance(String url) {
        Bundle args = new Bundle();

        args.putString("url", url);
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
        SLog.info("url[%s]", url);

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
        });

        loadingPopup = new XPopup.Builder(getContext())
                .asLoading("正在加載")
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
