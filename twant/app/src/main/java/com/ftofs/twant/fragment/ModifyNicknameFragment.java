package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 修改暱稱Fragment
 * @author zwm
 */
public class ModifyNicknameFragment extends BaseFragment implements View.OnClickListener {
    EditText etNickname;

    public static ModifyNicknameFragment newInstance(String oldNickname) {
        Bundle args = new Bundle();

        args.putString("oldNickname", oldNickname);
        ModifyNicknameFragment fragment = new ModifyNicknameFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_nickname, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String oldNickname = args.getString("oldNickname");

        etNickname = view.findViewById(R.id.et_nickname);
        etNickname.setText(oldNickname);
        EditTextUtil.cursorSeekToEnd(etNickname);
        showSoftInput(etNickname);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_ok) {
            String nickname = etNickname.getText().toString().trim();
            if (nickname.length() < 1) {
                ToastUtil.show(_mActivity, "暱稱不能為空");
                return;
            }

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "newNickName", nickname);

            Api.postUI(Api.PATH_MODIFY_NICKNAME, params, new UICallback() {
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
                            return;
                        }

                        ToastUtil.show(_mActivity, "修改暱稱成功");
                        hideSoftInput();

                        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_REFRESH_DATA, null);
                        pop();
                    } catch (Exception e) {

                    }
                }
            });
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
