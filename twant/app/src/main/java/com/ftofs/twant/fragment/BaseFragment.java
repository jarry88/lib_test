package com.ftofs.twant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;


import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.util.IntentUtil;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.ToastUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.yanzhenjie.permission.runtime.Permission;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Fragment公共基類
 * @author zwm
 */
public class BaseFragment extends SupportFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 點擊空白的地方，收起軟鍵盤
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
            }
        });
    }

    /**
     * 調起掃描二維碼的Activity
     */
    public void startCaptureActivity() {
        Intent intent = new Intent(_mActivity, CaptureActivity.class);
        startActivityForResult(intent, RequestCode.SCAN_QR_CODE.ordinal());
    }

    /**
     * 打開系統相冊
     */
    public void openSystemAlbumIntent(int requestCode) {
        PermissionUtil.actionWithPermission(_mActivity, new String[]{
                Permission.READ_EXTERNAL_STORAGE}, "訪問相冊需要授予", new CommonCallback() {
            @Override
            public String onSuccess(@Nullable String data) {
                // 打开相册
                startActivityForResult(IntentUtil.makeOpenSystemAlbumIntent(), requestCode);
                return null;
            }

            @Override
            public String onFailure(@Nullable String data) {
                ToastUtil.error(_mActivity, "您拒絕了授權");
                return null;
            }
        });
    }
}
