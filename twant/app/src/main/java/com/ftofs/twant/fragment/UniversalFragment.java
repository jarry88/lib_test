package com.ftofs.twant.fragment;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.LinkageTestFragment;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 通用設置Fragment
 * @author zwm
 */
public class UniversalFragment extends BaseFragment implements View.OnClickListener {
    ImageView avatar;
    TextView tvMemberName;
    String memberName;
    TextView tvMemberNumber;
    RelativeLayout btnRealNameSet;
    private TextView tvTestInfo;
    private int count=0;

    public static UniversalFragment newInstance() {
        Bundle args = new Bundle();

        UniversalFragment fragment = new UniversalFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_universal, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_logout, this);
        Util.setOnClickListener(view, R.id.btn_personal_info, this);
        Util.setOnClickListener(view, R.id.btn_my_e_name_card, this);
        Util.setOnClickListener(view, R.id.btn_security_setting, this);
        Util.setOnClickListener(view, R.id.btn_notification_setting, this);

        Util.setOnClickListener(view, R.id.btn_logistics_address_setting, this);
        Util.setOnClickListener(view, R.id.btn_feedback, this);

        Util.setOnClickListener(view, R.id.btn_about_takewant, this);
        Util.setOnClickListener(view, R.id.btn_member_document, this);

        btnRealNameSet = view.findViewById(R.id.btn_real_name_auth);
        btnRealNameSet.setOnClickListener(this);
        tvMemberName = view.findViewById(R.id.tv_member_name);
        tvMemberNumber = view.findViewById(R.id.tv_member_number);
        tvTestInfo = view.findViewById(R.id.text_build);
        tvTestInfo.setOnClickListener(this);
        tvTestInfo.setOnClickListener(v -> {
            //測試環境可進入此測試頁面
            Util.startFragment(LinkageTestFragment.newInstance(19));

        });
        avatar =view.findViewById((R.id.img_avatar));
//        tvMemberName.setText(User.getUserInfo(SPField.FIELD_NICKNAME,null));
        tvMemberNumber.setText(User.getUserInfo(SPField.FIELD_MOBILE_ENCRYPT,null));
        String avatarUrl = User.getUserInfo(SPField.FIELD_AVATAR,null);
        if(StringUtil.isEmpty(avatarUrl)){
            Glide.with(_mActivity).load(R.drawable.icon_default_avatar).into(avatar);
        }else{
            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).into(avatar);
        }
        //非正式環境下初始可以顯示當前環境信息
        if (!Config.PROD) {
            tvTestInfo.setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.text_build)).setText(String.format("當前環境%d",27+Config.currEnv));
        }
        showHideRealNameButton();
    }

    private void showHideRealNameButton() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        Api.getUI(Api.PATH_DETERMINE_SHOW_REAL_NAME_POPUP, params, new UICallback() {
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

                    int isShowAuth = responseObj.getInt("datas.isShowAuth");
                    if (isShowAuth == Constant.TRUE_INT) {
                        btnRealNameSet.setVisibility(View.VISIBLE);
                    } else {
                        btnRealNameSet.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_personal_info:
                start(MemberDocumentFragment.newInstance());
                break;
            case R.id.btn_my_e_name_card:
                start(ENameCardFragment.newInstance());
                break;
            case R.id.btn_security_setting:
                start(SecuritySettingFragment.newInstance());
                break;
            case R.id.btn_logistics_address_setting:
                start(AddrManageFragment.newInstance());
                break;
            case R.id.btn_feedback:
                start(CommitFeedbackFragment.newInstance());
                break;
            case R.id.btn_logout:
                boolean isFacebookLoggedIn = Util.isFacebookLoggedIn();
                SLog.info("isFacebookLoggedIn[%s]", isFacebookLoggedIn);
                if (Util.isFacebookLoggedIn()) {
                    LoginManager.getInstance().logOut();
                }
                User.logout();
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_LOGOUT_SUCCESS, null);
                hideSoftInputPop();
                break;
            case R.id.btn_about_takewant:
                start(AboutFragment.newInstance());
                break;
            case R.id.btn_notification_setting:
                start(NotificationSettingFragment.newInstance());
                break;
            case R.id.btn_real_name_auth:
                Util.startFragment(RealNameListFragment.newInstance());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

