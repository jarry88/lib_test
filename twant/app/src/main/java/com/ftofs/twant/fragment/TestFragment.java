package com.ftofs.twant.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.BadgeUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.util.Vendor;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    /**
     * facebook登錄按鈕
     * facebook分享、登錄參考  https://blog.csdn.net/qq_41545435/article/details/88601716
     */
    LoginButton loginButton;
    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_facebook_share, this);

        loginButton = view.findViewById(R.id.btn_facebook_login);
        loginButton.setReadPermissions("email");
        // 在fragment中使用
        loginButton.setFragment(this);
        // 回调
        loginButton.registerCallback(((MainActivity) _mActivity).getCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        Util.setOnClickListener(view, R.id.btn_test, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_facebook_share) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://www.snailpad.cn"))
                    .build();

            //调用分享弹窗
            ShareDialog.show(_mActivity, content);
        } else if (id == R.id.btn_test) {
            ((MainActivity) _mActivity).installUpdate("app_update/twant_12250_app_update_test.apk");
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
