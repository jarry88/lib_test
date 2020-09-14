package com.ftofs.twant.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.SoftInputInfo;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 顯示Debug信息的Fragment
 * @author zwm
 */
public class DebugFragment extends BaseFragment implements View.OnClickListener {
    EditText etUmengDeviceToken;
    EditText etSoftInputHeight;

    boolean isGetSoftInputHeight = false; 

    SoftInputInfo softInputInfo = new SoftInputInfo();

    private static class ProcessInfo {
        public int id;
        public String name;
    }

    public static DebugFragment newInstance() {
        Bundle args = new Bundle();

        DebugFragment fragment = new DebugFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debug, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvEnvironment = view.findViewById(R.id.tv_environment);
        String environmentInfo = "開發模式:" + Config.DEVELOPER_MODE + "\n"
                + "Api環境:" + Config.API_BASE_URL + "\n"
                + "構建類型:" + BuildConfig.BUILD_TYPE;
        tvEnvironment.setText(environmentInfo);

        TextView tvMemberName = view.findViewById(R.id.tv_member_name);
        tvMemberName.setText("memberName:" + User.getUserInfo(SPField.FIELD_MEMBER_NAME, ""));

        etUmengDeviceToken = view.findViewById(R.id.et_umeng_device_token);
        etSoftInputHeight = view.findViewById(R.id.et_soft_input_height);

        int softInputHeight = Hawk.get(SPField.FIELD_SOFT_INPUT_HEIGHT, SoftInputInfo.INVALID_HEIGHT);
        SLog.info("softInputHeight[%d]", softInputHeight);
        if (softInputHeight == SoftInputInfo.INVALID_HEIGHT) {
            etSoftInputHeight.setText("無");
        } else {
            etSoftInputHeight.setText(softInputHeight + "px");
        }

        Util.setOnClickListener(view, R.id.btn_get_soft_input_height, this);
        Util.setOnClickListener(view, R.id.btn_delete_soft_input_height, this);
        Util.setOnClickListener(view, R.id.btn_has_storage_permission, this);
        Util.setOnClickListener(view, R.id.btn_has_camera_permission, this);
        Util.setOnClickListener(view, R.id.btn_has_location_permission, this);
        Util.setOnClickListener(view, R.id.btn_list_app_process, this);
        Util.setOnClickListener(view, R.id.btn_get_umeng_device_token, this);
        Util.setOnClickListener(view, R.id.btn_back, this);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = view.getHeight();
                softInputInfo.setHeight(height);

                int softInputHeight = softInputInfo.getSoftInputHeight();
                if (isGetSoftInputHeight && softInputHeight != SoftInputInfo.INVALID_HEIGHT) {
                    SLog.info("softInputHeight[%d]", softInputHeight);
                    etSoftInputHeight.setText(softInputHeight + "px");
                    Hawk.put(SPField.FIELD_SOFT_INPUT_HEIGHT, softInputHeight);
                    isGetSoftInputHeight = false;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_has_storage_permission ||
                id == R.id.btn_has_camera_permission || id == R.id.btn_has_location_permission) {
            String[] permissionGroup = null;

            if (id == R.id.btn_has_storage_permission) {
                permissionGroup = Permission.Group.STORAGE;
            } else if (id == R.id.btn_has_camera_permission) {
                permissionGroup = Permission.Group.CAMERA;
            } else {
                permissionGroup = Permission.Group.LOCATION;
            }

            if (PermissionUtil.hasPermission(permissionGroup)) {
                ToastUtil.success(_mActivity, "有權限");
            } else {
                ToastUtil.error(_mActivity, "沒權限");
            }
        } else if (id == R.id.btn_list_app_process) {
            List<ProcessInfo> processInfoList = getAppProcess();

            StringBuilder sb = new StringBuilder();
            for (ProcessInfo processInfo : processInfoList) {
                sb.append(String.format("id[%d], name[%s]\n", processInfo.id, processInfo.name));
            }

            Toast.makeText(_mActivity, sb.toString(), Toast.LENGTH_LONG).show();
        } else if (id == R.id.btn_get_umeng_device_token) {
            String umengDeviceToken = TwantApplication.getInstance().getUmengDeviceToken();
            SLog.info("umengDeviceToken[%s]", umengDeviceToken);
            etUmengDeviceToken.setText(umengDeviceToken);
        } else if (id == R.id.btn_get_soft_input_height) {
            isGetSoftInputHeight = true;
            SLog.info("btn_get_soft_input_height");
            showSoftInput(etSoftInputHeight);
            etSoftInputHeight.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSoftInput();
                }
            }, 500);
        } else if (id == R.id.btn_delete_soft_input_height) {
            Hawk.delete(SPField.FIELD_SOFT_INPUT_HEIGHT);
            etSoftInputHeight.setText("無");
            ToastUtil.info(_mActivity, "刪除軟鍵盤高度成功");
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }


    /**
     * 獲取應用的進程列表
     * @return
     */
    private List<ProcessInfo> getAppProcess() {
        List<ProcessInfo> processInfoList = new ArrayList<>();
        ActivityManager am = (ActivityManager) _mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            if (info.processName.startsWith("com.ftofs")) {
                ProcessInfo processInfo = new ProcessInfo();
                processInfo.id = info.pid;
                processInfo.name = info.processName;

                processInfoList.add(processInfo);
            }
        }
        SLog.info("PROCESS COUNT[%d]", processInfoList.size());
        return processInfoList;
    }
}
