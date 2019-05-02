package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.MobileZoneSelectedListener;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.constant.Sms;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.SharedPreferenceUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.MobileZonePopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 重置密碼
 * @author zwm
 */
public class ResetPasswordFragment extends BaseFragment implements
        View.OnClickListener, MobileZoneSelectedListener {
    /**
     * 當前選中的區號索引
     */
    private int selectedMobileZoneIndex = 0;
    List<MobileZone> mobileZoneList = new ArrayList<>();
    ImageView btnRefreshCaptcha;
    String captchaKey;
    EditText etMobile;
    EditText etCaptcha;
    TextView tvAreaName;

    public static ResetPasswordFragment newInstance() {
        Bundle args = new Bundle();

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

        Util.setOnClickListener(view, R.id.btn_next, this);
        Util.setOnClickListener(view, R.id.btn_mobile_zone, this);
        btnRefreshCaptcha = view.findViewById(R.id.btn_refresh_captcha);
        btnRefreshCaptcha.setOnClickListener(this);

        etMobile = view.findViewById(R.id.et_mobile);
        etCaptcha = view.findViewById(R.id.et_captcha);
        tvAreaName = view.findViewById(R.id.tv_area_name);

        refreshCaptcha();
        getMobileZoneList();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_next) {
            if (mobileZoneList.size() <= selectedMobileZoneIndex) {
                return;
            }
            final MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);

            final String mobile = etMobile.getText().toString().trim();
            String captcha = etCaptcha.getText().toString().trim();



            String fullMobile = mobileZone.areaCode + "," + mobile;

            EasyJSONObject params = EasyJSONObject.generate(
                    "mobile", fullMobile,
                    "captchaKey", captchaKey,
                    "captchaVal", captcha,
                    "sendType", Sms.SEND_TYPE_FIND_PASSWORD
            );
            SLog.info("params[%s]", params);

            Api.getUI(Api.PATH_SEND_SMS_CODE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        // 如果出錯，刷新驗證碼
                        refreshCaptcha();
                        return;
                    }

                    try {
                        int code = responseObj.getInt("code");
                        if (code != ResponseCode.SUCCESS) {
                            ToastUtil.show(_mActivity, responseObj.getString("datas.error"));
                            return;
                        }

                        ToastUtil.show(_mActivity, "動態碼已發送");

                        MainFragment mainFragment = MainFragment.getInstance();
                        int smsCodeValidTime = responseObj.getInt("datas.authCodeValidTime");
                        mainFragment.start(ResetPasswordConfirmFragment.newInstance(mobileZone.areaCode, mobile, smsCodeValidTime));
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (id == R.id.btn_refresh_captcha) {
            refreshCaptcha();
        } else if (id == R.id.btn_mobile_zone) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new MobileZonePopup(_mActivity, mobileZoneList, selectedMobileZoneIndex, this))
                    .show();
        }
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
                SLog.info("mobileZoneList.size[%d]", mobileZoneList.size());
                if (mobileZoneList.size() > 0) {
                    tvAreaName.setText(mobileZoneList.get(0).areaName);
                }
            }
        });
    }

    @Override
    public void onMobileZoneSelected(int selectedIndex) {
        SLog.info("selectedMobileZoneIndex[%d], selectedIndex[%d]", selectedMobileZoneIndex, selectedIndex);
        if (this.selectedMobileZoneIndex == selectedIndex) {
            return;
        }

        this.selectedMobileZoneIndex = selectedIndex;
        String areaName = mobileZoneList.get(selectedMobileZoneIndex).areaName;
        tvAreaName.setText(areaName);
    }
}