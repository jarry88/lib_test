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
import com.ftofs.twant.entity.StoreAnnouncement;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;

/**
 * 店鋪公告詳情Fragment
 * @author zwm
 */
public class StoreAnnouncementDetailFragment extends BaseFragment implements View.OnClickListener {
    String title;
    String content;
    WebView webView;
    private long createTime;

    public static StoreAnnouncementDetailFragment newInstance(String title, String content) {
        Bundle args = new Bundle();

        StoreAnnouncementDetailFragment fragment = new StoreAnnouncementDetailFragment();
        fragment.setArguments(args);
        SLog.info("title[%s], content[%s]", title, content);
        fragment.setData(title, 0, content);

        return fragment;
    }

    public static StoreAnnouncementDetailFragment newInstance(StoreAnnouncement storeAnnouncement) {
        Bundle args = new Bundle();

        StoreAnnouncementDetailFragment fragment = new StoreAnnouncementDetailFragment();
        fragment.setArguments(args);
        fragment.setData(storeAnnouncement.title,storeAnnouncement.createTime,storeAnnouncement.content);
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
        TextView tvFragmentTime = view.findViewById(R.id.tv_fragment_time);
        tvFragmentTime.setText(Time.fromMillisUnixtime(createTime,"Y-m-d H:i:s"));

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

    public void setData(String title, long createTime, String content) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
