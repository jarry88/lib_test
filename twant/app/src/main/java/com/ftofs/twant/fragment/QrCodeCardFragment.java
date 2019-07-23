package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 二維碼名片Fragment
 * @author zwm
 */
public class QrCodeCardFragment extends BaseFragment implements View.OnClickListener {
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

        ImageView imgAvatar = view.findViewById(R.id.img_avatar);
        String avatarUrl = StringUtil.normalizeImageUrl(User.getUserInfo(SPField.FIELD_AVATAR, ""));
        Glide.with(_mActivity).load(avatarUrl).centerCrop().into(imgAvatar);

        TextView tvNickname = view.findViewById(R.id.tv_nickname);
        tvNickname.setText(User.getUserInfo(SPField.FIELD_NICKNAME, ""));

        ImageView imgGenderIndicator = view.findViewById(R.id.img_gender_indicator);
        int gender = User.getUserInfo(SPField.FIELD_GENDER, Constant.GENDER_UNKNOWN);
        if (gender == Constant.GENDER_FEMALE) {
            imgGenderIndicator.setImageResource(R.drawable.icon_female);
        } else {
            imgGenderIndicator.setImageResource(R.drawable.icon_male);
        }

        TextView tvSignature = view.findViewById(R.id.tv_signature);
        String signature = User.getUserInfo(SPField.FIELD_MEMBER_SIGNATURE, "");
        tvSignature.setText(signature);

        ImageView imgMyQrCode = view.findViewById(R.id.img_my_qr_code);
        String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, "");
        String textContent = "tw_member_" + memberName;
        Bitmap bitmap = CodeUtils.createImage(textContent, 400, 400, null);
        imgMyQrCode.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
