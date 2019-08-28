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
import com.ftofs.twant.config.Config;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

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

            if (id == R.id.btn_view_takewant_introduction) {
                url = "/introduce";
            } else if (id == R.id.btn_view_secret_terms) {
                url = "/article/info_h5/%E7%A7%81%E9%9A%B1%E6%A2%9D%E6%AC%BE";
            } else {
                url = "/article/info_h5/%E6%9C%8D%E5%8B%99%E5%8D%94%E8%AD%B0";
            }
            start(ExplorerFragment.newInstance(Config.WEB_BASE_URL + url, true));
        } else if (id == R.id.btn_goto_rate) {
            Util.gotoGooglePlay(_mActivity);
        }
    }
}
