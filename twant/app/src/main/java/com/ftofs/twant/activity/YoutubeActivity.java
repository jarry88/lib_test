package com.ftofs.twant.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.ftofs.twant.R;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;

public class YoutubeActivity extends AppCompatActivity implements View.OnClickListener {
    WebView webView;
    boolean ignoreSslError = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        Intent intent = getIntent();

        String videoId = intent.getStringExtra("videoId");
        int videoWidth = intent.getIntExtra("videoWidth", 480);
        int videoHeight = intent.getIntExtra("videoHeight", 270);

        Util.enterFullScreen(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                String title = view.getTitle();
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

        SLog.info("here");

        Pair<Integer, Integer> screenDimen = Util.getScreenDimension(this);
        int screenWidth = screenDimen.first;
        int screenHeight = screenDimen.second;
        SLog.info("screenWidth[%d], screenHeight[%d], videoWidth[%d], videoHeight[%d]",
                screenWidth, screenHeight, videoWidth, videoHeight);

        ViewGroup.LayoutParams layoutParams = webView.getLayoutParams();

        int webViewWidth, webViewHeight;
        if (screenWidth * videoHeight > screenHeight * videoWidth) {
            // 屏幕比视频宽，webView高度取屏幕高度，webView宽度根据视频等比例缩放
            webViewHeight = screenHeight;
            webViewWidth = videoWidth * webViewHeight / videoHeight;
        } else {
            // 屏幕比视频窄，webView宽度取屏幕宽度，webView高度根据视频等比例缩放
            webViewWidth = screenWidth;
            webViewHeight = videoHeight * webViewWidth / videoWidth;
        }

        int dpWidth= Util.px2dip(this, webViewWidth);
        int dpHeight = Util.px2dip(this, webViewHeight);
        SLog.info("webViewWidth[%d], webViewHeight[%d], dpWidth[%d], dpHeight[%d]",
                webViewWidth, webViewHeight, dpWidth, dpHeight);

        layoutParams.width = webViewWidth;
        layoutParams.height = webViewHeight;
        webView.setLayoutParams(layoutParams);

        String htmlContent = String.format("<body style='margin: 0;'><iframe width='%d' height='%d' src='https://www.youtube.com/embed/%s?&autoplay=1' frameborder='0' allowfullscreen></iframe></body>",
                dpWidth, dpHeight, videoId);
        // webView.loadUrl("http://gogo.so/youtube-embed.html");
        SLog.info("htmlContent[%s]", htmlContent);

        webView.loadData(htmlContent, "text/html", "utf-8");

        findViewById(R.id.btn_back_round).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back_round) {
            finish();
        }
    }
}
