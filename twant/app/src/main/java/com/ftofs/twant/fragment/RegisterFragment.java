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

import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.constant.Sms;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
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
import okhttp3.Response;


/**
 * 注冊
 * @author zwm
 */
public class RegisterFragment extends BaseFragment implements
        View.OnClickListener, OnSelectedListener {
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


    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_get_sms_code, this);

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
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_mobile_zone) {
            List<ListPopupItem> itemList = new ArrayList<>();
            for (MobileZone mobileZone : mobileZoneList) {
                ListPopupItem item = new ListPopupItem(mobileZone.areaId, mobileZone.areaName, null);
                itemList.add(item);
            }

            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.mobile_zone_text),
                            Constant.POPUP_TYPE_MOBILE_ZONE, itemList, selectedMobileZoneIndex, this))
                    .show();
        } else if (id == R.id.btn_refresh_captcha) {
            refreshCaptcha();
        } else if (id == R.id.btn_get_sms_code) {
            if (mobileZoneList.size() <= selectedMobileZoneIndex) {
                return;
            }
            // 獲取區號
            final MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);

            // 注账号为 区号,手机号
            final String mobile = etMobile.getText().toString().trim();
            final String fullMobile = String.format("%s,%s", mobileZone.areaCode, mobile);

            String captchaText = etCaptcha.getText().toString().trim();

            Api.getUI(Api.PATH_SEND_SMS_CODE, EasyJSONObject.generate(
                    "mobile", fullMobile,
                    "captchaKey", captchaKey,
                    "captchaVal", captchaText,
                    "sendType", Sms.SEND_TYPE_REGISTER), new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);
                    final EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    try {
                        int code = responseObj.getInt("code");
                        if (code != ResponseCode.SUCCESS) {
                            // 如果出錯，刷新驗證碼
                            refreshCaptcha();
                            ToastUtil.show(_mActivity, responseObj.getString("datas.error"));
                            return;
                        }

                        // 發送驗證碼成功
                        MainFragment mainFragment = MainFragment.getInstance();
                        int smsCodeValidTime = responseObj.getInt("datas.authCodeValidTime");
                        mainFragment.start(RegisterConfirmFragment.newInstance(mobileZone.areaCode, mobile, smsCodeValidTime));
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }
                }
            });
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
    public void onSelected(int type, int id, Object extra) {
        SLog.info("selectedMobileZoneIndex[%d], selectedIndex[%d]", selectedMobileZoneIndex, id);
        if (this.selectedMobileZoneIndex == id) {
            return;
        }

        this.selectedMobileZoneIndex = id;
        String areaName = mobileZoneList.get(selectedMobileZoneIndex).areaName;
        tvAreaName.setText(areaName);
    }
}
