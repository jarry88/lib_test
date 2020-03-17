package com.ftofs.twant.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AppGuidePagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.HwLoadingView;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * App啟動引導頁
 * 每天最多顯示一次
 * @author zwm
 */
public class AppGuideActivity extends BaseActivity implements OnSelectedListener {
    private boolean mIsScrolled;
    ViewPager vpAppGuide;
    List<String> imageList = new ArrayList<>();
    int currPage = 0;

    HwLoadingView vwLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        // 記錄最近一次顯示APP引導頁的日期
        Hawk.put(SPField.FIELD_SHOW_APP_GUIDE_DATE, new Jarbon().toDateString());

        vwLoading = findViewById(R.id.vw_loading);
        vwLoading.setVisibility(View.VISIBLE);
        vpAppGuide = findViewById(R.id.vp_app_guide);

        loadData();
    }

    private void loadData() {
        Api.getUI(Api.PATH_APP_GUIDE, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                vwLoading.setVisibility(View.GONE);
                ToastUtil.showNetworkError(AppGuideActivity.this, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(AppGuideActivity.this, responseObj)) {
                        return;
                    }

                    EasyJSONArray appGuideImageArray = responseObj.getSafeArray("datas.appGuideImage");
                    for (Object object : appGuideImageArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        String url = easyJSONObject.getSafeString("guideImage");
                        imageList.add(url);
                    }

                    showAppGuide();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    private void showAppGuide() {
        AppGuidePagerAdapter adapter = new AppGuidePagerAdapter(this, imageList, this);
        vpAppGuide.setAdapter(adapter);
        vpAppGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currPage = i;
                SLog.info("currPage[%d]", currPage);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                实现ViewPager最后一页向后滑动监听
                https://www.jianshu.com/p/2d03d06a6adf
                当滑动ViewPager的时候，如果处于第一页，继续向前滑动的时候不会产生SCROLL_STATE_SETTLING状态，
                但是如果向下一页方向滑动，则必然会产生SCROLL_STATE_SETTLING状态；如果处于最后一页，继续往后滑动，
                不会产生SCROLL_STATE_SETTLING状态，但是往前一页滑动，也必然会产生SCROLL_STATE_SETTLING状态。
                 */

                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mIsScrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        mIsScrolled = true;
                        break;

                    case ViewPager.SCROLL_STATE_IDLE:
                        if (!mIsScrolled ) {
                            // TODO     你想要实现的操作
                            if (currPage > 0) {
                                startMainActivity();
                            }
                        }
                        mIsScrolled = true;
                        break;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        Util.enterFullScreen(this);
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        // 開始體驗
        SLog.info("開始體驗");
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
