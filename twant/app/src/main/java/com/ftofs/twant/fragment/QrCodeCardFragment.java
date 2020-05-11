package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 二維碼名片Fragment
 * @author zwm
 */
public class QrCodeCardFragment extends BaseFragment implements View.OnClickListener {
    private ImageView imgAvatar;
    private ImageView imgGenderIndicator;

    public static QrCodeCardFragment newInstance() {
        Bundle args = new Bundle();

        QrCodeCardFragment fragment = new QrCodeCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        imgAvatar = view.findViewById(R.id.img_avatar);
        String avatarUrl = StringUtil.normalizeImageUrl(User.getUserInfo(SPField.FIELD_AVATAR, ""));
        Glide.with(_mActivity).load(avatarUrl).centerCrop().into(imgAvatar);

        TextView tvNickname = view.findViewById(R.id.tv_nickname);
        tvNickname.setText(User.getUserInfo(SPField.FIELD_NICKNAME, ""));

        imgGenderIndicator = view.findViewById(R.id.img_gender_indicator);
//        int gender = User.getUserInfo(SPField.FIELD_GENDER, Constant.GENDER_UNKNOWN);


        TextView tvSignature = view.findViewById(R.id.tv_signature);
        String signature = User.getUserInfo(SPField.FIELD_MEMBER_SIGNATURE, "");
        tvSignature.setText(signature);

        ImageView imgMyQrCode = view.findViewById(R.id.img_my_qr_code);
        String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, "");
        String textContent = "tw_member_" + memberName;
        Bitmap bitmap = CodeUtils.createImage(textContent, 400, 400, null);
        imgMyQrCode.setImageBitmap(bitmap);
        loadMemberSex(view);
    }
    private void loadMemberSex(final View view) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        Api.postUI(Api.PATH_MEMBER_DETAIL, params, new UICallback() {
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

                    EasyJSONObject memberInfo = responseObj.getSafeObject("datas.memberInfo");
                    String avatarUrl = memberInfo.getSafeString("avatar");
                    if (!StringUtil.isEmpty(avatarUrl)) {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAvatar);
                    }else {
                        Glide.with(_mActivity).load(R.drawable.icon_default_avatar).centerCrop().into(imgAvatar);
                    }

//                    TextView mobile = view.findViewById(R.id.tv_member_mobile);
//                    String mobileAreaCode = memberInfo.getSafeString("mobileAreaCode");
//                    mobile.setText(mobileAreaCode+memberInfo.getSafeString("mobile"));
//                    mobile = view.findViewById(R.id.tv_top_mobile);
//                    mobile.setText(memberInfo.getSafeString("mobile"));
//                    TextView tvName =view.findViewById(R.id.tv_member_name);
//                    tvName.setText(memberInfo.getSafeString("nickName"));

                    int gender = memberInfo.getInt("memberSex");
                    updateGnder(gender);

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updateGnder(int gender) {
        if (gender == Constant.GENDER_FEMALE) {
            imgGenderIndicator.setImageResource(R.drawable.icon_male);
            imgGenderIndicator.setVisibility(View.VISIBLE);
        }else if(gender==Constant.GENDER_UNKNOWN){

            imgGenderIndicator.setVisibility(View.INVISIBLE);
        }else {
            imgGenderIndicator.setImageResource(R.drawable.icon_female);
            imgGenderIndicator.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
