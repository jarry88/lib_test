package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

/**
 * 關於頁面
 * @author zwm
 */
public class AboutFragment extends BaseFragment implements View.OnClickListener {
    TextView tvAppVersion;

    View btnCheckUpdate;
    View btnGotoRate;

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
        tvAppVersion.setText("Version " + BuildConfig.VERSION_NAME + " - build" + BuildConfig.VERSION_CODE + " - " +  BuildConfig.FLAVOR);

        Util.setOnClickListener(view, R.id.btn_view_takewant_introduction, this);
        Util.setOnClickListener(view, R.id.btn_view_secret_terms, this);
        Util.setOnClickListener(view, R.id.btn_view_service_contract, this);
        /*
        Util.setOnClickListener(view, R.id.btn_contact_us, this);
        Util.setOnClickListener(view, R.id.btn_view_post_sale_service, this);
        Util.setOnClickListener(view, R.id.btn_view_shipping_policy, this);

         */

        btnCheckUpdate = view.findViewById(R.id.btn_check_update);
        btnCheckUpdate.setOnClickListener(this);

        btnGotoRate = view.findViewById(R.id.btn_goto_rate);
        btnGotoRate.setOnClickListener(this);

        if (Constant.FLAVOR_GOOGLE.equals(BuildConfig.FLAVOR)) { // Google Play渠道才显示去评分功能
            btnGotoRate.setVisibility(View.VISIBLE);
            btnCheckUpdate.setBackgroundResource(R.drawable.border_type_d);
        } else {
            btnGotoRate.setVisibility(View.GONE);
            btnCheckUpdate.setBackground(null);
        }

        Util.setOnClickListener(view, R.id.btn_back, this);
    }




    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_view_takewant_introduction || id == R.id.btn_view_secret_terms || id == R.id.btn_view_service_contract) {
            String url = "";

            if (id == R.id.btn_view_takewant_introduction) { // Takewant介紹
                url = "https://www.twant.com/web/introduce";
            } else if (id == R.id.btn_view_secret_terms) {  // 私隱條款
                url = "https://f2.twant.com/web/article/info_h5/3";
            } else if (id == R.id.btn_view_service_contract) { // 服務協議
                url = "https://f2.twant.com/web/article/info_h5/0";
            }
            start(ExplorerFragment.newInstance(url, true));
        } else if (id == R.id.btn_check_update) {
            ((MainActivity) _mActivity).checkUpdate(true);
        } else if (id == R.id.btn_goto_rate) {
            Util.gotoGooglePlay(_mActivity);
        }
    }
}
