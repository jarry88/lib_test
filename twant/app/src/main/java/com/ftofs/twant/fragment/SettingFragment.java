package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 設置Fragment
 * @author zwm
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    public static SettingFragment newInstance() {
        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_personal_info, this);
        Util.setOnClickListener(view, R.id.btn_login_password, this);
        Util.setOnClickListener(view, R.id.btn_change_mobile, this);
        Util.setOnClickListener(view, R.id.btn_payment_password, this);
        Util.setOnClickListener(view, R.id.btn_realname_authentication, this);

        Util.setOnClickListener(view, R.id.btn_logout, this);

        String mobileEncrypt = Hawk.get(SPField.FIELD_MOBILE_ENCRYPT, "");
        TextView tvMobile = view.findViewById(R.id.tv_mobile);
        tvMobile.setText(mobileEncrypt);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_logout) {
            User.logout();
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_LOGOUT_SUCCESS, null);
            pop();
        } else if (id == R.id.btn_payment_password) {
            MainFragment mainFragment = MainFragment.getInstance();
            if (mainFragment != null) {
                mainFragment.start(PaymentPasswordFragment.newInstance());
            }
        } else if (id == R.id.btn_login_password) {
            MainFragment mainFragment = MainFragment.getInstance();
            if (mainFragment != null) {
                mainFragment.start(ResetPasswordFragment.newInstance());
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
