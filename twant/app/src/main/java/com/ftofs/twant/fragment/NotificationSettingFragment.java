package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.SPField;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.HawkUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.kyleduo.switchbutton.SwitchButton;

import org.jetbrains.annotations.NotNull;

public class NotificationSettingFragment extends BaseFragment implements View.OnClickListener {

    private SwitchButton sbReceiveNews;
    private SwitchButton sbReceiveNewsDetail;

    public static NotificationSettingFragment newInstance() {
        NotificationSettingFragment fragment = new NotificationSettingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_notification_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.sb_receive_news,this);
        sbReceiveNews = view.findViewById(R.id.sb_receive_news);
        sbReceiveNewsDetail = view.findViewById(R.id.sb_receive_news_detail);
        sbReceiveNewsDetail.setChecked(HawkUtil.getUserData(SPField.USER_RECEIVE_NEWS_DETAIL,true));
        sbReceiveNews.setChecked(HawkUtil.getUserData(SPField.USER_RECEIVE_NEWS,true));
        SLog.info("news%s  newsDetail%s",sbReceiveNews.isChecked(),sbReceiveNewsDetail.isChecked());
        Util.setOnClickListener(view,R.id.sb_receive_news_detail,this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sb_receive_news:
                HawkUtil.setUserData(SPField.USER_RECEIVE_NEWS,sbReceiveNews.isChecked());
                if (!sbReceiveNews.isChecked() && sbReceiveNewsDetail.isChecked()) {
                    sbReceiveNewsDetail.setChecked(false);
                    HawkUtil.setUserData(SPField.USER_RECEIVE_NEWS_DETAIL, sbReceiveNewsDetail.isChecked());
                    SLog.info("news%s  newsDetail%s",sbReceiveNews.isChecked(),sbReceiveNewsDetail.isChecked());
                }
                SLog.info("news%s",sbReceiveNews.isChecked());
                break;
            case R.id.sb_receive_news_detail:
                if (!sbReceiveNews.isChecked()&&sbReceiveNewsDetail.isChecked()) {
                    ToastUtil.success(_mActivity,"请同时打开接受通知开关");
                }
                HawkUtil.setUserData(SPField.USER_RECEIVE_NEWS_DETAIL, sbReceiveNewsDetail.isChecked());
                SLog.info("newsDetail%s", sbReceiveNewsDetail.isChecked());
                break;
            case R.id.btn_back:
                pop();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
