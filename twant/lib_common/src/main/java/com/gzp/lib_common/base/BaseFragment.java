package com.gzp.lib_common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.github.richardwrq.krouter.annotation.Inject;
import com.github.richardwrq.krouter.api.core.KRouter;
import com.gzp.lib_common.base.callback.CommonCallback;
import com.gzp.lib_common.base.callback.IntentCallBack;
import com.gzp.lib_common.constant.Constant;
import com.gzp.lib_common.constant.RequestCode;
import com.gzp.lib_common.service.AppService;
import com.gzp.lib_common.service.ConstantsPath;
import com.gzp.lib_common.utils.IntentUtil;
import com.gzp.lib_common.utils.PermissionUtil;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.utils.ToastUtil;
import com.yanzhenjie.permission.runtime.Permission;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Fragment公共基類
 * 将基本类迁移到common module
 * @author zwm
 */
public abstract class BaseFragment extends SupportFragment {
    protected Context _mContext;
    protected ViewDataBinding simpleBind;
    private IntentCallBack mCapureCallBack;
    @Inject(name = ConstantsPath.APP_SERVICE_PATH)
    AppService appService;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        simpleBind=
                DataBindingUtil.inflate(inflater, simpleBind(inflater, container, savedInstanceState), container, false);
            simpleBind.setLifecycleOwner(this);
        return simpleBind.getRoot();
    }

    protected  int simpleBind(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return 0;
    };
    /**
     * 調起掃描二維碼的Activity
     * 因為項目結構調整
     * 凡是要使用拉起二維碼activity的fragment要自己實現intent回調
     */
    public void startCaptureActivity(Intent intent) {
        PermissionUtil.actionWithPermission(_mActivity, new String[] {Permission.CAMERA}, "掃一掃需要授予", new CommonCallback() {
            @Override
            public String onSuccess(@Nullable String data) {
                //todo 处理扫码跳转
                startActivityForResult(intent, RequestCode.SCAN_QR_CODE.ordinal());
                return null;
            }

            @Override
            public String onFailure(@Nullable String data) {
                ToastUtil.error(_mActivity, "您拒絕了授權，無法使用本功能>_<");
                return null;
            }
        });
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

    /**
     * 先隱藏軟鍵盤，再關閉頁面
     */
    public void hideSoftInputPop() {
        hideSoftInput();
        pop();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _mContext = null;
    }
}
