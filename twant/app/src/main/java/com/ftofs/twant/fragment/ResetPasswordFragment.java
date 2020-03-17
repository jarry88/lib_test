package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.constant.Sms;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 用戶注冊、重置密碼、設置支付密碼Fragment
 * @author zwm
 */
public class ResetPasswordFragment extends BaseFragment implements
        View.OnClickListener, OnSelectedListener {
    /**
     * 用途
     */
    int usage;

    TextView tvFragmentTitle;

    /**
     * 當前選中的區號索引
     */
    private int selectedMobileZoneIndex = 0;
    List<MobileZone> mobileZoneList = new ArrayList<>();

    String captchaKey;
    ImageView btnRefreshCaptcha;
    EditText etMobile;
    EditText etCaptcha;
    TextView tvAreaName;
    boolean isModifyPaymentPassword;
    private LinearLayout trueNoticeLoge;
    private TextView btnNext;
    private boolean checkAgreeState;
    private ImageView imgCheckAgree;


    public static ResetPasswordFragment newInstance(int usage, boolean isModifyPaymentPassword) {
        Bundle args = new Bundle();

        args.putInt("usage", usage);
        args.putBoolean("isModifyPaymentPassword", isModifyPaymentPassword);
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        usage = args.getInt("usage");
        isModifyPaymentPassword = args.getBoolean("isModifyPaymentPassword");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_next, this);
        Util.setOnClickListener(view, R.id.img_check, this);
        Util.setOnClickListener(view, R.id.btn_view_tos, this);
        btnNext = view.findViewById(R.id.btn_next);
        imgCheckAgree = view.findViewById(R.id.img_check);
        trueNoticeLoge = view.findViewById(R.id.item_logo);
        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        if (usage == Constant.USAGE_USER_REGISTER) {
            tvFragmentTitle.setText(R.string.register_fragment_title);
            trueNoticeLoge.setVisibility(View.VISIBLE);
        } else if (usage == Constant.USAGE_RESET_PASSWORD) {
            tvFragmentTitle.setText(R.string.reset_password_fragment_title);
        } else if (usage == Constant.USAGE_SET_PAYMENT_PASSWORD) {
            if (isModifyPaymentPassword) {
                tvFragmentTitle.setText(R.string.modify_payment_password);
            } else {
                tvFragmentTitle.setText(R.string.payment_password_fragment_title);
            }
        }
        btnRefreshCaptcha = view.findViewById(R.id.btn_refresh_captcha);
        btnRefreshCaptcha.setOnClickListener(this);

        etMobile = view.findViewById(R.id.et_mobile);
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updataBtnNext();
            }
        });
        etCaptcha = view.findViewById(R.id.et_captcha);
        etCaptcha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updataBtnNext();
            }
        });
        tvAreaName = view.findViewById(R.id.tv_area_name);

        refreshCaptcha();
        getMobileZoneList();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
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
        } else if (id == R.id.btn_refresh_captcha) {
            refreshCaptcha();
        } else if (id == R.id.btn_next) {
            getSmsCode();
        } else if (id == R.id.img_check) {
            checkAgreeState = !checkAgreeState;
            Glide.with(_mActivity).load(checkAgreeState ? R.drawable.icon_checked : R.drawable.icon_unchecked).centerCrop().into(imgCheckAgree);
            updataBtnNext();
        } else if (id == R.id.btn_view_tos) {
            Util.startFragment(H5GameFragment.newInstance(Constant.TOS_URL, getString(R.string.text_service_contract)));
        }
    }

    private void getSmsCode() {
        try {
            if (!checkInfo(true)) {
                return;
            }
            // 獲取區號
            final MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);

            // 注账号为 区号,手机号
            final String mobile = etMobile.getText().toString().trim();
            String fullMobile = String.format("%s,%s", mobileZone.areaCode, mobile);

            String captchaText = etCaptcha.getText().toString().trim();
            EasyJSONObject params = EasyJSONObject.generate(
                    "mobile", fullMobile,
                    "captchaKey", captchaKey,
                    "captchaVal", captchaText);



            if (usage == Constant.USAGE_USER_REGISTER) {
                params.set("sendType", Sms.SEND_TYPE_REGISTER);
            } else if (usage == Constant.USAGE_RESET_PASSWORD) {
                params.set("sendType", Sms.SEND_TYPE_FIND_PASSWORD);
            } else if (usage == Constant.USAGE_SET_PAYMENT_PASSWORD) {
                params.set("sendType", Sms.SEND_TYPE_SECURITY_VERIFY);
            }
            SLog.info("params[%s]", params);
            Api.getUI(Api.PATH_SEND_SMS_CODE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    btnNext.setBackgroundResource(R.drawable.grey_button);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);
                    final EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    try {
                        int code = responseObj.getInt("code");
                        if (code != ResponseCode.SUCCESS) {
                            // 如果出錯，刷新驗證碼
                            refreshCaptcha();
                            ToastUtil.error(_mActivity, responseObj.getSafeString("datas.error"));
                            btnNext.setBackgroundResource(R.drawable.grey_button);
                            return;
                        }

                        // 發送驗證碼成功
                        int smsCodeValidTime = responseObj.getInt("datas.authCodeValidTime");
                        if (usage == Constant.USAGE_USER_REGISTER) {
                            start(RegisterConfirmFragment.newInstance(mobileZone.areaCode, mobile, smsCodeValidTime));
                        } else {
                            start(ResetPasswordConfirmFragment.newInstance(usage, mobileZone.areaCode, mobile, smsCodeValidTime, isModifyPaymentPassword));
                        }
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private boolean checkInfo(boolean toastEnable) {
        if (mobileZoneList.size() <= selectedMobileZoneIndex) {
            return false;
        }
        // 獲取區號
        final MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);

        // 注账号为 区号,手机号
        final String mobile = etMobile.getText().toString().trim();
        if (StringUtil.isEmpty(mobile)) {
            if (toastEnable) {
                ToastUtil.error(_mActivity, getString(R.string.tip_number_is_null));
            }
            return false;
        }


        if (!StringUtil.isMobileValid(mobile, mobileZone.areaId)) {
            String[] areaArray = new String[] {
                    "",
                    getString(R.string.text_hongkong),
                    getString(R.string.text_mainland),
                    getString(R.string.text_macao)
            };

            String msg = String.format(getString(R.string.text_invalid_mobile), areaArray[mobileZone.areaId]);
            if (toastEnable) {
                ToastUtil.error(_mActivity, msg);
            }
            return false;
        }


        String captchaText = etCaptcha.getText().toString().trim();
        if (StringUtil.isEmpty(captchaText)) {
            if (toastEnable) {
                ToastUtil.error(_mActivity, "驗證碼不能為空");
            }
            return false;
        }
        if (!checkAgreeState) {
            if (toastEnable) {
                ToastUtil.error(_mActivity, getString(R.string.agree_server));
            }
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
        SLog.info("selectedMobileZoneIndex[%d], selectedIndex[%d]", selectedMobileZoneIndex, id);
        if (this.selectedMobileZoneIndex == id) {
            return;
        }

        this.selectedMobileZoneIndex = id;
        String areaName = mobileZoneList.get(selectedMobileZoneIndex).areaName;
        tvAreaName.setText(areaName);
        updataBtnNext();
    }

    private void updataBtnNext() {
        if (checkInfo(false)) {
            btnNext.setBackgroundResource(R.drawable.blue_button);
        } else {
            btnNext.setBackgroundResource(R.drawable.grey_button);

        }
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
}
