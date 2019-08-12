package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.yanzhenjie.permission.runtime.Permission;


/**
 * 顯示Debug信息的Fragment
 * @author zwm
 */
public class DebugFragment extends BaseFragment implements View.OnClickListener {
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
        String environmentInfo = "開發模式:" + Config.DEVELOPER_MODE + "\n" + "Api環境:" + Config.API_BASE_URL;
        tvEnvironment.setText(environmentInfo);

        TextView tvMemberName = view.findViewById(R.id.tv_member_name);
        tvMemberName.setText("memberName:" + User.getUserInfo(SPField.FIELD_MEMBER_NAME, ""));

        Util.setOnClickListener(view, R.id.btn_has_storage_permission, this);
        Util.setOnClickListener(view, R.id.btn_has_camera_permission, this);
        Util.setOnClickListener(view, R.id.btn_has_location_permission, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
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
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
