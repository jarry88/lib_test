package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 修改暱稱Fragment
 * 設置郵箱、微信賬號、Facebook賬號也是用這個頁面
 * @author zwm
 */
public class ModifyNicknameFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 用於編輯什麼
     */
    int usage;

    public static final int USAGE_NICKNAME = 1;
    public static final int USAGE_EMAIL = 2;
    public static final int USAGE_WECHAT = 3;
    public static final int USAGE_FACEBOOK = 4;


    EditText etOldValue;

    TextView tvFragmentTitle;

    public static ModifyNicknameFragment newInstance(int usage, String oldValue) {
        Bundle args = new Bundle();

        args.putInt("usage", usage);
        args.putString("oldValue", oldValue);
        ModifyNicknameFragment fragment = new ModifyNicknameFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_nickname, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        usage = args.getInt("usage");
        String oldValue = args.getString("oldValue");

        etOldValue = view.findViewById(R.id.et_nickname);
        etOldValue.setText(oldValue);
        EditTextUtil.cursorSeekToEnd(etOldValue);
        showSoftInput(etOldValue);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        if (usage == USAGE_NICKNAME) {
            tvFragmentTitle.setText("修改暱稱");
            etOldValue.setHint("請輸入暱稱");
        } else if (usage == USAGE_EMAIL) {
            tvFragmentTitle.setText("修改郵箱");
            etOldValue.setHint("請輸入郵箱");
        } else if (usage == USAGE_WECHAT) {
            tvFragmentTitle.setText("修改微信帳號");
            etOldValue.setHint("請輸入微信帳號");
        } else {
            tvFragmentTitle.setText("修改Facebook帳號");
            etOldValue.setHint("請輸入Facebook帳號");
        }

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            String newValue = etOldValue.getText().toString().trim();
            if (newValue.length() < 1 && usage != USAGE_WECHAT && usage != USAGE_FACEBOOK) {  // 微信和Facebook账号可以为空
                if (usage == USAGE_NICKNAME) {
                    ToastUtil.error(_mActivity, "暱稱不能為空");
                } else if (usage == USAGE_EMAIL) {
                    ToastUtil.error(_mActivity, "郵箱不能為空");
                } else if (usage == USAGE_WECHAT) {
                    ToastUtil.error(_mActivity, "微信帳號不能為空");
                } else if (usage == USAGE_FACEBOOK) {
                    ToastUtil.error(_mActivity, "Facebook帳號不能為空");
                }

                return;
            }

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            String keyName;
            String path;
            if (usage == USAGE_NICKNAME) {
                keyName = "newNickName";
                path = Api.PATH_MODIFY_NICKNAME;
            } else if (usage == USAGE_EMAIL) {
                keyName = "email";
                path = Api.PATH_MEMBEREMAIL_EDIT;
            } else if (usage == USAGE_WECHAT) {
                keyName = "wechat";
                path = Api.PATH_EDIT_WECHAT_ID;
            } else {
                keyName = "facebook";
                path = Api.PATH_EDIT_FACEBOOK_ID;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    keyName, newValue);

            SLog.info("path[%s], params[%s]", path, params);
            Api.postUI(path, params, new UICallback() {
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

                        if (usage == USAGE_NICKNAME) {
                            Hawk.put(SPField.FIELD_NICKNAME, newValue);
                        } else if (usage == USAGE_EMAIL) {
                            Bundle args = new Bundle();
                            args.putString("email", newValue);
                            setFragmentResult(RESULT_OK, args);
                        }



                        ToastUtil.success(_mActivity, "修改暱稱成功");
                        hideSoftInputPop();
                    } catch (Exception e) {

                    }
                }
            });
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}



