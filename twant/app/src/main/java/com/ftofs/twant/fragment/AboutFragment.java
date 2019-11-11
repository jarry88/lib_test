package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 關於頁面
 * @author zwm
 */
public class AboutFragment extends BaseFragment implements View.OnClickListener {
    TextView tvAppVersion;

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();

        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAppVersion = view.findViewById(R.id.tv_app_version);
        tvAppVersion.setText("Version " + BuildConfig.VERSION_NAME + " - " + BuildConfig.FLAVOR);

        Util.setOnClickListener(view, R.id.btn_view_takewant_introduction, this);
        Util.setOnClickListener(view, R.id.btn_view_secret_terms, this);
        Util.setOnClickListener(view, R.id.btn_view_service_contract, this);

        Util.setOnClickListener(view, R.id.btn_check_update, this);
        Util.setOnClickListener(view, R.id.btn_goto_rate, this);

        Util.setOnClickListener(view, R.id.btn_back, this);
    }




    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_view_takewant_introduction || id == R.id.btn_view_secret_terms || id == R.id.btn_view_service_contract) {
            String url;

            if (id == R.id.btn_view_takewant_introduction) { // Takewant介紹
                url = "https://www.twant.com/web/introduce";
            } else if (id == R.id.btn_view_secret_terms) {  // 私隱條款
                url = "https://f2.twant.com/web/article/info_h5/3";
            } else { // 服務協議
                url = "https://f2.twant.com/web/article/info_h5/0";
            }
            start(ExplorerFragment.newInstance(url, true));
        } else if (id == R.id.btn_check_update) {
            Api.getUI(Api.PATH_CHECK_UPDATE, EasyJSONObject.generate("version", BuildConfig.VERSION_NAME), new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        if (!responseObj.exists("datas.version")) {
                            // 如果服務器端沒有返回版本信息，也當作是最新版本
                            ToastUtil.success(_mActivity, getString(R.string.text_is_newest_version));
                            return;
                        }

                        // 當前版本
                        String currentVersion = BuildConfig.VERSION_NAME;
                        // 最新版本
                        String newestVersion = responseObj.getString("datas.version");


                        SLog.info("currentVersion[%s], newestVersion[%s]", currentVersion, newestVersion);
                        int result = Util.versionCompare(currentVersion, newestVersion);
                        SLog.info("result[%d]", result);

                        if (result >= 0) {
                            ToastUtil.success(_mActivity, getString(R.string.text_is_newest_version));
                        } else {
                            ToastUtil.info(_mActivity, "發現新版本，正在轉去Google應用商店...");
                            Util.gotoGooglePlay(_mActivity);
                        }
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } else if (id == R.id.btn_goto_rate) {
            Util.gotoGooglePlay(_mActivity);
        }
    }
}
