package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

/**
 * 店鋪公告詳情Fragment
 * @author zwm
 */
public class StoreAnnouncementDetailFragment extends BaseFragment implements View.OnClickListener {
    String title;
    String content;
    WebView webView;

    public static StoreAnnouncementDetailFragment newInstance(String title, String content) {
        Bundle args = new Bundle();

        StoreAnnouncementDetailFragment fragment = new StoreAnnouncementDetailFragment();
        fragment.setArguments(args);
        SLog.info("title[%s], content[%s]", title, content);
        fragment.setData(title, content);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_announcement_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        TextView tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        tvFragmentTitle.setText(title);

        webView = view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadData(content, "text/html", "utf-8");
    }
        
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    public void setData(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
