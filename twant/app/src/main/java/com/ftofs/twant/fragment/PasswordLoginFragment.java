package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.SharedPreferenceUtil;
import com.ftofs.twant.util.SqliteUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;


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
    EditText etMobile;
    EditText etPassword;
    EditText etCaptcha;
    TextView tvAreaName;

    CommonCallback commonCallback;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_login, this);
        Util.setOnClickListener(view, R.id.btn_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_forget_password, this);

        btnRefreshCaptcha = view.findViewById(R.id.btn_refresh_captcha);
        btnRefreshCaptcha.setOnClickListener(this);

        etMobile = view.findViewById(R.id.et_mobile);
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

        refreshCaptcha();
        getMobileZoneList();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            doLogin();
        } else if (id == R.id.btn_refresh_captcha) {
            refreshCaptcha();
        } else if (id == R.id.btn_mobile_zone) {
            List<ListPopupItem> itemList = new ArrayList<>();
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
        } else if (id == R.id.btn_forget_password) {
            Util.startFragment(ResetPasswordFragment.newInstance(Constant.USAGE_RESET_PASSWORD));
        }
    }

    private void doLogin() {
        if (mobileZoneList == null || mobileZoneList.size() <= selectedMobileZoneIndex) {
            return;
        }
        MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);

        String mobile = etMobile.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String captcha = etCaptcha.getText().toString().trim();

        if (StringUtil.isEmpty(mobile)) {
            ToastUtil.error(_mActivity, getString(R.string.input_mobile_hint));
            return;
        }

        if (!StringUtil.isMobileValid(mobile, mobileZone.areaId)) {
            String[] areaArray = new String[] {
                    "",
                    getString(R.string.text_hongkong),
                    getString(R.string.text_mainland),
                    getString(R.string.text_macao)
            };

            String msg = String.format(getString(R.string.text_invalid_mobile), areaArray[mobileZone.areaId]);
            ToastUtil.error(_mActivity, msg);
            return;
        }

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
        Api.postUI(Api.PATH_LOGIN, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        // 如果出錯，刷新驗證碼
                        refreshCaptcha();
                        return;
                    }

                    ToastUtil.success(_mActivity, "登錄成功");
                    int userId = responseObj.getInt("datas.memberId");
                    SharedPreferenceUtil.saveUserInfo(responseObj);
                    TwantApplication.getInstance().setUmengAlias(Constant.ACTION_ADD);
                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_LOGIN_SUCCESS, null);
                    hideSoftInput();
                    SqliteUtil.switchUserDB(userId);

                    SLog.info("登錄成功");

                    if (commonCallback != null) {
                        SLog.info("Fragment出棧");
                        commonCallback.onSuccess(null);
                    }
                } catch (Exception e) {

                }
            }
        });
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
            }
        });
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
                    tvAreaName.setText(mobileZoneList.get(0).areaName);
                }
            }
        });
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("selectedMobileZoneIndex[%d], id[%d]", selectedMobileZoneIndex, id);
        if (this.selectedMobileZoneIndex == id) {
            return;
        }

        this.selectedMobileZoneIndex = id;
        String areaName = mobileZoneList.get(selectedMobileZoneIndex).areaName;
        tvAreaName.setText(areaName);
    }
}
