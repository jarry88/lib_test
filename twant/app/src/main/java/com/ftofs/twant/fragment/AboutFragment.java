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
 * 關于頁面
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
        tvAppVersion.setText(getString(R.string.app_name) + " Version " + BuildConfig.VERSION_NAME);

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
                url = "/introduce";
            } else if (id == R.id.btn_view_secret_terms) {  // 私隱條款
                url = "/article/info_h5/%E7%A7%81%E9%9A%B1%E6%A2%9D%E6%AC%BE";
            } else { // 服務協議
                url = "/article/info_h5/%E6%9C%8D%E5%8B%99%E5%8D%94%E8%AD%B0";
            }
            start(ExplorerFragment.newInstance(Config.WEB_BASE_URL + url, true));
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
                        int result = versionCompare(currentVersion, newestVersion);
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

    /**
     * 版本號比較
     * @param version1
     * @param version2
     * @return
     *      * 如果version1比version2新，返回1
     *      * 如果version1比version2舊，返回-1
     *      * 如果version1與version2相同，返回0
     */
    private int versionCompare(String version1, String version2) {
        String[] version1Arr = version1.split("\\.");
        String[] version2Arr = version2.split("\\.");


        // 獲取版本號分段數，取較小值是為了預防分段數不一致
        int fieldCount = Math.min(version1Arr.length, version2Arr.length);
        SLog.info("version1Arr[%s], version2Arr[%s], fieldCount[%d]", version1Arr, version2Arr, fieldCount);

        for (int i = 0; i < fieldCount; i++) {
            int field1 = Integer.valueOf(version1Arr[i]);
            int field2 = Integer.valueOf(version2Arr[i]);
            if (field1 > field2) {
                return 1;
            } else if (field1 < field2) {
                return -1;
            }
        }

        return 0;
    }
}
