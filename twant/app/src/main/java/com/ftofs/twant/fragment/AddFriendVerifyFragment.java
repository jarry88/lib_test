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
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 添加好友填寫驗證申請
 * @author zwm
 */
public class AddFriendVerifyFragment extends BaseFragment implements View.OnClickListener {
    String memberName;

    TextView tvWordCount;
    EditText etRemark;

    public static MessageFragment newInstance(String memberName) {
        Bundle args = new Bundle();

        args.putString("memberName", memberName);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friend_verify, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        memberName = args.getString("memberName");

        tvWordCount = view.findViewById(R.id.tv_word_count);
        etRemark = view.findViewById(R.id.et_remark);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
    }


        @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "toMember", memberName,
                    "notes", etRemark.getText().toString().trim()
            );

            SLog.info("params[%s]", params.toString());
            Api.postUI(Api.PATH_ADD_FRIEND_APPLICATION, params, new UICallback() {
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

                        ToastUtil.success(_mActivity, "發送成功");
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
