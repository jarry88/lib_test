package com.ftofs.twant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.LoginType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 登入
 * @author zwm
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener, CommonCallback {
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    /**
     * facebook登錄按鈕
     * facebook分享、登錄參考  https://blog.csdn.net/qq_41545435/article/details/88601716
     */
    CallbackManager callbackManager;
    private int tabClickCount;
    private long lastClickStamp;
    private boolean showlist;
    private boolean byWebView;
    TabLayout tabLayout;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static LoginFragment newInstance(boolean byWebView) {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        fragment.byWebView = byWebView;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_register, this);

        tabLayout = view.findViewById(R.id.login_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.login_viewpager);

        titleList.add(getResources().getString(R.string.login_way_password));
        titleList.add("短信");
        tabLayout.setOnClickListener(this);
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));


        fragmentList.add(PasswordLoginFragment.newInstance(this));
        fragmentList.add(DynamicCodeLoginFragment.newInstance(this));

        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                 SLog.info( "page %d" ,position);
                Editable editable1 = ((DynamicCodeLoginFragment) fragmentList.get(1)).etMobile.getText();
                Editable editable = ((PasswordLoginFragment) fragmentList.get(0)).etMobile.getText();
                if (position == 1) {
                    ((DynamicCodeLoginFragment) fragmentList.get(1)).etMobile.setText(editable);
                } else {
                    ((PasswordLoginFragment) fragmentList.get(0)).etMobile.setText(editable1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        callbackManager = ((MainActivity) _mActivity).getCallbackManager();
        // 回调
        LoginManager.getInstance().registerCallback(((MainActivity) _mActivity).getCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                SLog.info("onSuccess");
                AccessToken accessToken = loginResult.getAccessToken();
                String token = accessToken.getToken();
                String userId = accessToken.getUserId();
                SLog.info("token[%s], userId[%s]", token, userId);

                doFacebookLogin(token, userId);
            }

            @Override
            public void onCancel() {
                // App code
                SLog.info("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                ToastUtil.error(_mActivity, "登錄失敗");
                SLog.info("onError, exception[%s]", exception.toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_WEIXIN_LOGIN) {
            String code = (String) message.data;
            SLog.info("code[%s]", code);

            EasyJSONObject params = EasyJSONObject.generate(
                    "code", code, "clientType", Constant.CLIENT_TYPE_ANDROID);

            SLog.info("params[%s]", params);
            if (true) {
                // return;
            }
            Api.postUI(Api.PATH_WX_LOGIN_STEP1, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        // 判斷是否已經綁定手機號
                        int isBind = responseObj.getInt("datas.isBind");
                        SLog.info("isBind[%d]", isBind);
                        if (isBind == Constant.TRUE_INT) {
                            int userId = responseObj.getInt("datas.memberId");
                            User.onLoginSuccess(userId, LoginType.WEIXIN, responseObj);
                            hideSoftInputPop();

                            ToastUtil.success(_mActivity, "微信登錄成功");
                        } else {
                            String accessToken = responseObj.getSafeString("datas.accessToken");
                            String openId = responseObj.getSafeString("datas.accessToken");

                            start(BindMobileFragment.newInstance(accessToken, openId));
                        }
                    } catch (Exception e) {

                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_register) {
            Util.startFragment(ResetPasswordFragment.newInstance(Constant.USAGE_USER_REGISTER, false));
        } else if (!showlist&&id == R.id.btn_test) {
            if (tabClickCount > 5) {
                showlist = true;
                String[] data = {"取消","prod/線上" , "29", "28","日誌開關","test"};
                XPopup.Builder builder = new XPopup.Builder(_mActivity);
                builder.dismissOnTouchOutside(false);
                builder.asCenterList("切換環境:", data, (position, text) -> {
                    SLog.info("position[%d], text[%s]", position, text);
                    if (position == 0) {

                    } else if (position == 1) {
                        Config.changeEnvironment(Config.ENV_PROD);

                        exit();
                    } else if (position == 2) {
                        Config.changeEnvironment(Config.ENV_29);
                        exit();
                    }else if(position==3){
                        Config.changeEnvironment(Config.ENV_28);
                        exit();
                    } else if (position == 4) {
                        if (Config.SLOGENABLE) {
                            ToastUtil.success(getContext(), "日誌輸出已開啟");
                        } else {
                            ToastUtil.success(getContext(), "打開日誌輸出");
                            Config.SLOGENABLE = true;
                        }
                    } else if (position == 5) {
                        Util.startFragment(TestFragment.newInstance());
                    }else {
                        Util.startFragment(DebugFragment.newInstance());
                    }
                    showlist = false;
                }).show();
                tabClickCount = 0;
                return;
            }
            long currClickStamp = System.currentTimeMillis();
            if (currClickStamp - lastClickStamp < 1100) {
                tabClickCount++;

            } else {
                tabClickCount = 0;
            }
            lastClickStamp = currClickStamp;


        } else if (id == R.id.login_tab_layout) {
            SLog.info("here225");
        }
    }

    @Override
    public String onSuccess(@Nullable String data) {
        SLog.info("LoginFragment::onSuccess");
        hideSoftInputPop();
        return null;
    }

    @Override
    public String onFailure(@Nullable String data) {
        return null;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("__onSupportVisible");
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SLog.info("__onSupportInvisible");
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void facebookLogin() {
        SLog.info("facebookLogin");

        List<String> permissions = new ArrayList<>();
        permissions.add("email");
        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
    }

    public void doFacebookLogin(String accessToken, String userId) {
        SLog.info("doFacebookLogin...");
        EasyJSONObject params = EasyJSONObject.generate(
                "accessToken", accessToken,
                "userId", userId,
                "clientType", Constant.CLIENT_TYPE_ANDROID
        );

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_FACEBOOK_LOGIN, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    int isBind = responseObj.getInt("datas.isBind");
                    if (isBind == Constant.FALSE_INT) {
                        Util.startFragment(BindMobileFragment.newInstance(BindMobileFragment.BIND_TYPE_FACEBOOK, accessToken, userId));
                    } else { // 未綁定
                        int userId = responseObj.getInt("datas.memberId");
                        User.onLoginSuccess(userId, LoginType.FACEBOOK, responseObj);
                        hideSoftInputPop();

                        ToastUtil.success(_mActivity, "Facebook登錄成功");
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

            }
        });
    }
    /**退出程序*/
    protected void exit() {
        // 这里使用clear + new task的方式清空整个任务栈,只保留新打开的Main页面
        // 然后Main页面接收到退出的标志位exit=true,finish自己,这样就关闭了全部页面

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("exit", true);
        startActivity(intent);
    }


}
