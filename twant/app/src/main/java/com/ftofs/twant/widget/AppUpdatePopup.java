package com.ftofs.twant.widget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.orhanobut.hawk.Hawk;
import com.yanzhenjie.permission.runtime.Permission;

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
    /**
     * apk包的下載地址
     */
    String appUrl;

    public AppUpdatePopup(@NonNull Activity activity, String version, String versionDesc, boolean isForceUpdate, String appUrl) {
        super(activity);

        this.activity = activity;
        this.version = version;
        this.versionDesc = versionDesc;
        this.isForceUpdate = isForceUpdate;
        this.appUrl = appUrl;
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
            /*
            多渠道打包
            通过配置Flavors和自定义buildConfigField进行多个服务器地址打包
            https://blog.csdn.net/qxf5777404/article/details/51580431
             */

            if (BuildConfig.FLAVOR.equals(Constant.FLAVOR_GOOGLE)) {
                // 跳轉到Google Play
                Util.gotoGooglePlay(activity);
            } else if (BuildConfig.FLAVOR.equals(Constant.FLAVOR_TENCENT)) {
                // 跳轉到應用寶
                Util.gotoQqDownloader(activity);
            } else if (BuildConfig.FLAVOR.equals(Constant.FLAVOR_OFFICIAL)) {
                PermissionUtil.actionWithPermission(getContext(), new String[] {Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE}, "下載升級包需要授予", new CommonCallback() {

                    @Override
                    public String onSuccess(@Nullable String data) {
                        downloadApk();
                        return null;
                    }

                    @Override
                    public String onFailure(@Nullable String data) {

                        return null;
                    }
                });
            }
        }
        dismiss();
    }

    private void downloadApk() {
        SLog.info("flavor[%s]", BuildConfig.FLAVOR);
        if (BuildConfig.FLAVOR.equals(Constant.FLAVOR_GOOGLE)) {
            // 跳轉到Google Play
            Util.gotoGooglePlay(activity);
        } else if (BuildConfig.FLAVOR.equals(Constant.FLAVOR_OFFICIAL)) {
            Uri uri = Uri.parse("https://www.twant.com/web/app-download.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            MainActivity mainActivity = MainActivity.getInstance();
            if (mainActivity != null) {
                mainActivity.startActivity(intent);
            }
            /*

            TaskObserver taskObserver = new TaskObserver() {
                @Override
                public void onMessage() {
                    String path = (String) message; // 成功時，返回安裝包的路徑；失敗返回null
                    MainActivity.getInstance().installUpdate(path);
                }
            };

            TaskObservable taskObservable = new TaskObservable(taskObserver) {
                @Override
                public Object doWork() {
                    String apkFilename = PathUtil.getFilename(appUrl);

                    // 存放apk包的路徑
                    String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/takewant/app_update";
                    SLog.info("dir[%s]", dir);

                    FileUtil.createOrExistsDir(new File(dir));
                    File apkFile = new File(dir + "/" + apkFilename);

                    long now = System.currentTimeMillis();
                    long downloadApp = Hawk.get(SPField.FIELD_DOWNLOAD_APP, 0L);
                    if (now - downloadApp < 10 * 60 * 1000) { // 如果10分鐘內下載過，不再重復下載
                        SLog.info("如果10分鐘內下載過，不再重復下載");
                        return null;
                    }

                    Hawk.put(SPField.FIELD_DOWNLOAD_APP, now);
                    SLog.info("開始下載, appUrl[%s]", appUrl);
                    boolean success =  Api.syncDownloadFile(appUrl, apkFile);
                    SLog.info("下載完成, success[%s], file size[%s]", success, apkFile.length());
                    Hawk.delete(SPField.FIELD_DOWNLOAD_APP);
                    return apkFile.getAbsolutePath();
                }
            };
            TwantApplication.getThreadPool().execute(taskObservable);
             */
        }
    }
}
