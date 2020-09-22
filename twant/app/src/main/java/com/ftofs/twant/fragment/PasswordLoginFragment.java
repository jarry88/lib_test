package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.LoginType;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.ftofs.lib_net.model.MobileZone;
import com.ftofs.twant.interfaces.CommonCallback;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.CheckPhoneView;
import com.ftofs.lib_common_ui.popup.ListPopup;
import com.lxj.xpopup.XPopup;
import com.umeng.analytics.MobclickAgent;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 密碼登入
 * @author zwm
 */
public class PasswordLoginFragment extends BaseFragment implements
        View.OnClickListener, OnSelectedListener {
    /**
     * 當前選中的區號索引
     */
    private int selectedMobileZoneIndex = 0;
    List<MobileZone> mobileZoneList = new ArrayList<>();
    ImageView btnRefreshCaptcha;
    String captchaKey;
    CheckPhoneView etMobileView;
    EditText etPassword;
    EditText etCaptcha;
    TextView tvAreaName;
    LinearLayout llMobileErrorContainer;
    TextView tvMobileError;
    CommonCallback commonCallback;

    ImageView btnWechatLogin;
    private RelativeLayout btnMobilezone;

    boolean loginButtonEnable = true; // 防止重覆點擊
    boolean checkButtonState = true; // 防止重覆點擊
    private ImageView imgClick;
    //测试专用验证 登陆的快捷方式
    private Bitmap captchaBitMap;

    public void setCommonCallback(CommonCallback commonCallback) {
        this.commonCallback = commonCallback;
    }

    public static PasswordLoginFragment newInstance(CommonCallback commonCallback) {
        Bundle args = new Bundle();

        PasswordLoginFragment fragment = new PasswordLoginFragment();
        fragment.setArguments(args);

        fragment.setCommonCallback(commonCallback);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_login, this);
        Util.setOnClickListener(view, R.id.btn_facebook_login, this);
        Util.setOnClickListener(view, R.id.btn_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_view_tos, this);
        Util.setOnClickListener(view, R.id.btn_view_private_terms, this);
        Util.setOnClickListener(view, R.id.btn_forget_password, this);
        Util.setOnClickListener(view, R.id.img_check,this);

        imgClick = view.findViewById(R.id.img_check);
        btnMobilezone = view.findViewById(R.id.btn_mobile_zone);
        btnRefreshCaptcha = view.findViewById(R.id.btn_refresh_captcha);
        btnRefreshCaptcha.setOnClickListener(this);

        llMobileErrorContainer = view.findViewById(R.id.ll_container_mobile_error);
        tvMobileError = view.findViewById(R.id.tv_mobile_error);

        etMobileView = view.findViewById(R.id.et_mobile_view);

        etPassword = view.findViewById(R.id.et_password);
        etCaptcha = view.findViewById(R.id.et_captcha);
        etCaptcha.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doLogin();
                }
                return false;
            }
        });
        tvAreaName = view.findViewById(R.id.tv_area_name);

        btnWechatLogin = view.findViewById(R.id.btn_wechat_login);
        btnWechatLogin.setOnClickListener(this);


        refreshCaptcha();
        getMobileZoneList();
    }

    @Override
    public void onClick(View v) {
//        SLog.info("loginButtonEnable,%s",loginButtonEnable);

        int id = v.getId();
        if (id == R.id.btn_login) {
            SLog.info("loginButtonEnable,%s",loginButtonEnable);
            if (!loginButtonEnable) {
                return;
            }

            if (Config.PROD) {
                MobclickAgent.onEvent(TwantApplication.Companion.get(), UmengAnalyticsActionName.LOGIN);
            }

            doLogin();
        } else if (id == R.id.btn_wechat_login) {
            if (!TwantApplication.Companion.get().getWxApi().isWXAppInstalled()) { // 未安裝微信
                ToastUtil.error(_mActivity, getString(R.string.weixin_not_installed_hint));
                return;
            }

            if (Config.PROD) {
                MobclickAgent.onEvent(TwantApplication.Companion.get(), UmengAnalyticsActionName.WECHAT_LOGIN);
            }

            ((MainActivity) _mActivity).doWeixinLogin(Constant.WEIXIN_AUTH_USAGE_LOGIN);
        } else if (id == R.id.btn_facebook_login) {
            if (Config.PROD) {
                MobclickAgent.onEvent(TwantApplication.Companion.get(), UmengAnalyticsActionName.FACEBOOK_LOGIN);
            }

            ((LoginFragment) commonCallback).facebookLogin();
        } else if (id == R.id.btn_refresh_captcha) {
            refreshCaptcha();
        } else if (id == R.id.btn_mobile_zone) {
            List<ListPopupItem> itemList = new ArrayList<>();
            if (mobileZoneList == null) {
                return;
            } else if(mobileZoneList.size()==0){
                return;
            }
            for (MobileZone mobileZone : mobileZoneList) {
                ListPopupItem item = new ListPopupItem(mobileZone.areaId, mobileZone.areaName, null);
                itemList.add(item);
            }

            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.mobile_zone_text),
                            PopupType.MOBILE_ZONE, itemList, selectedMobileZoneIndex, this))
                    .show();
        } else if (id == R.id.btn_view_tos || id == R.id.btn_view_private_terms) {
            int articleId;
            String title;
            if (id == R.id.btn_view_tos) {
                articleId = H5GameFragment.ARTICLE_ID_TERMS_OF_SERVICE;
                title = getString(R.string.text_service_contract);
            } else {
                articleId = H5GameFragment.ARTICLE_ID_TERMS_OF_PRIVATE;
                title = "私隱條款";
            }
            Util.startFragment(H5GameFragment.newInstance(articleId, title));
        } else if (id == R.id.btn_forget_password) {
            Util.startFragment(ResetPasswordFragment.newInstance(Constant.USAGE_RESET_PASSWORD, false));
        } else if (id == R.id.img_check) {
            checkButtonState = !checkButtonState;
            Glide.with(_mActivity).load(checkButtonState?R.drawable.icon_checked:R.drawable.icon_unchecked).centerCrop().into(imgClick);
        }
    }

    private void doLogin() {
        if (!checkMobileZoneList()) {
            return;
        }

        MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);
// TODO: 2020/8/4
        String password = etPassword.getText().toString().trim();
        String captcha = etCaptcha.getText().toString().trim();
        if (!checkButtonState) {
            ToastUtil.error(_mActivity, getString(R.string.agree_server));
            return;
        }
//        if (StringUtil.isEmpty(mobile)) {
//            ToastUtil.error(_mActivity, getString(R.string.input_mobile_hint));
//            return;
//        }
        kotlin.Pair<Boolean, String> pair = etMobileView.checkError();
        if(!pair.component1()){
            ToastUtil.error(_mActivity,pair.component2());
            return;
        }
        String mobile = etMobileView.getPhone();

//        if (!StringUtil.isMobileValid(mobile, mobileZone.areaId)) {
//            String[] areaArray = new String[] {
//                    "",
//                    getString(R.string.text_hongkong),
//                    getString(R.string.text_mainland),
//                    getString(R.string.text_macao)
//            };
//
//            String msg = String.format(getString(R.string.text_invalid_mobile), areaArray[mobileZone.areaId]);
//            ToastUtil.error(_mActivity, msg);
//            return;
//        }

        if (StringUtil.isEmpty(password)) {
            ToastUtil.error(_mActivity, getString(R.string.input_login_password_hint));
            return;
        }

        if (StringUtil.isEmpty(captcha)) {
            ToastUtil.error(_mActivity, getString(R.string.input_captcha_hint));
            return;
        }


        String fullMobile = mobileZone.areaCode + "," + mobile;

        EasyJSONObject params = EasyJSONObject.generate(
                "memberName", fullMobile,
                "password", password,
                "captchaKey", captchaKey,
                "captchaVal", captcha,
                "clientType", Constant.CLIENT_TYPE_ANDROID
        );
        SLog.info("params[%s]", params);
        loginButtonEnable = false;
        Api.postUI(Api.PATH_LOGIN, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loginButtonEnable = true;
                SLog.info("loginButtonEnable,%s",loginButtonEnable);

                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                // loginButtonEnable = true; 不需要，因为已经登录成功
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        // 如果出錯，刷新驗證碼
                        refreshCaptcha();
                        loginButtonEnable = true;
                        return;
                    }

                    ToastUtil.success(_mActivity, "登入成功");
                    int userId = responseObj.getInt("datas.memberId");
                    User.onLoginSuccess(userId, LoginType.MOBILE, responseObj);
                    hideSoftInput();

                    SLog.info("登錄成功");
                    Util.getMemberToken(_mActivity);

                    if (commonCallback != null) {
                        SLog.info("Fragment出棧");
                        commonCallback.onSuccess(null);
                    }
                } catch (Exception e) {
                    loginButtonEnable = true;
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private boolean checkMobileZoneList() {
        if (mobileZoneList == null || mobileZoneList.size() <= selectedMobileZoneIndex) {
            return false;
        }
        return true;
    }

    /**
     * 刷新驗證碼
     */
    private void refreshCaptcha() {
        Api.refreshCaptcha(new TaskObserver() {
            @Override
            public void onMessage() {
                Pair<Bitmap, String> result = (Pair<Bitmap, String>) message;
                SLog.info("result[%s]", result);
                if (result == null) {
                    return;
                }
                captchaKey = result.second;
                btnRefreshCaptcha.setImageBitmap(result.first);
                if (Config.DEVELOPER_MODE) {
                    captchaBitMap = result.first;
                }
            }
        });
    }

    public void getUrl(TaskObserver task) {
        File cacheDir=_mActivity.getCacheDir();
        if (!cacheDir.exists()){
            cacheDir.mkdirs();
        }
        String fileName="pic.jpg";

        try {
            File outFile =new File(cacheDir,fileName);
            if (!outFile.exists()){
                boolean res=outFile.createNewFile();
                if (!res){
                    SLog.info("文件创建失败");

                    return;
                }
            } else {
                outFile.delete();
                boolean res=outFile.createNewFile();
                // 文件已經存在，跳過
                SLog.info("文件已經存在，大小[%d]", outFile.length());
            }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
                captchaBitMap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            Api.syncTestUploadFile(outFile,task);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取区号列表
     */
    private void getMobileZoneList() {
        Api.getMobileZoneList(new TaskObserver() {
            @Override
            public void onMessage() {
                mobileZoneList = (List<MobileZone>) message;
                if (mobileZoneList == null) {
                    return;
                }
                SLog.info("mobileZoneList.size[%d]", mobileZoneList.size());
                if (mobileZoneList.size() > 0) {
                    etMobileView.setMobileList(mobileZoneList);
                    onSelected(null,0,null);
                }
            }
        });
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("selectedMobileZoneIndex[%d], id[%d]", selectedMobileZoneIndex, id);
        if(mobileZoneList==null||mobileZoneList.size()<=selectedMobileZoneIndex){
            return;
        }
        this.selectedMobileZoneIndex = id;
        String areaName = mobileZoneList.get(selectedMobileZoneIndex).areaName;
        tvAreaName.setText(areaName);
        etMobileView.setZoneIndex(id);
    }
}
