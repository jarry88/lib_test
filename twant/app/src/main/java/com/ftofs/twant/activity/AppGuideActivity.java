package com.ftofs.twant.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AppGuidePagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;

import java.io.File;
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
    ViewPager vpAppGuide;
    List<String> imageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        // 記錄最近一次顯示APP引導頁的日期
        Hawk.put(SPField.FIELD_SHOW_APP_GUIDE_DATE, new Jarbon().toDateString());

        vpAppGuide = findViewById(R.id.vp_app_guide);

        loadData();
    }

    private void loadData() {
        Api.getUI(Api.PATH_APP_GUIDE, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(AppGuideActivity.this, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (responseObj == null) {
                        return;
                    }

                    EasyJSONArray appGuideImageArray = responseObj.getArray("datas.appGuideImage");
                    for (Object object : appGuideImageArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        String url = easyJSONObject.getString("guideImage");
                        imageList.add(url);
                    }

                    showAppGuide();
                } catch (Exception e) {

                }
            }
        });
    }


    private void showAppGuide() {
        AppGuidePagerAdapter adapter = new AppGuidePagerAdapter(this, imageList, this);
        vpAppGuide.setAdapter(adapter);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
