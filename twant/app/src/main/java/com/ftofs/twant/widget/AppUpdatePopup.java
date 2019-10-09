package com.ftofs.twant.widget;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.orhanobut.hawk.Hawk;

/**
 * App升級彈窗
 * @author zwm
 */
public class AppUpdatePopup extends CenterPopupView implements View.OnClickListener {
    Activity activity;
    /**
     * 是否為強制升級
     */
    boolean isForceUpdate;
    /**
     * 版本號
     */
    String version;
    /**
     * 版本說明
     */
    String versionDesc;

    public AppUpdatePopup(@NonNull Activity activity, String version, String versionDesc, boolean isForceUpdate) {
        super(activity);

        this.activity = activity;
        this.version = version;
        this.versionDesc = versionDesc;
        this.isForceUpdate = isForceUpdate;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.app_update_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText("V" + version);
        TextView tvVersionDesc = findViewById(R.id.tv_version_desc);
        tvVersionDesc.setText(versionDesc);

        findViewById(R.id.ll_update_button_container).setVisibility(isForceUpdate ? GONE : VISIBLE);
        TextView btnAppUpdateNow = findViewById(R.id.btn_app_update_now);
        btnAppUpdateNow.setVisibility(isForceUpdate ? VISIBLE : GONE);
        btnAppUpdateNow.setOnClickListener(this);
        findViewById(R.id.btn_app_update_later).setOnClickListener(this);
        findViewById(R.id.btn_app_update_try).setOnClickListener(this);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
        Hawk.put(SPField.FIELD_APP_UPDATE_POPUP_SHOWN_TIMESTAMP, System.currentTimeMillis());
        Hawk.put(SPField.FIELD_APP_UPDATE_POPUP_SHOWN_DATE, new Jarbon().toDateString());
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        Hawk.delete(SPField.FIELD_APP_UPDATE_POPUP_SHOWN_TIMESTAMP);
    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_app_update_try || id == R.id.btn_app_update_now) {
            // 跳轉到Google Play
            Util.gotoGooglePlay(activity);

            /*
            多渠道打包
            通过配置Flavors和自定义buildConfigField进行多个服务器地址打包
            https://blog.csdn.net/qxf5777404/article/details/51580431
             */
            if (BuildConfig.FLAVOR.equals("")) {

            }
        }
        dismiss();
    }
}
