package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.ftofs.twant.util.HawkUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class EditMemberSignatureFragment extends BaseFragment implements View.OnClickListener {
    String memberSignature;

    EditText etContent;
    TextView tvWordCount;
    boolean isUser;

    public static EditMemberSignatureFragment newInstance(String originalMemberSignature) {
        Bundle args = new Bundle();

        args.putString("originalMemberSignature", originalMemberSignature);
        EditMemberSignatureFragment fragment = new EditMemberSignatureFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static EditMemberSignatureFragment newInstance(String originalMemberSignature,String memberName) {
        Bundle args = new Bundle();
        args.putString("memberName",memberName);
        args.putString("originalMemberSignature", originalMemberSignature);
        EditMemberSignatureFragment fragment = new EditMemberSignatureFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_member_signature, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        isUser = !args.containsKey("memberName");
        memberSignature = args.getString("originalMemberSignature");


        tvWordCount = view.findViewById(R.id.tv_word_count);
        etContent = view.findViewById(R.id.et_content);
        if(isUser){
            etContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String wordCount = s.length() + "/200";
                    tvWordCount.setText(wordCount);
                }
            });
        } else {
            etContent.setEnabled(false);
        }

        etContent.setText(memberSignature);

        Util.setOnClickListener(view, R.id.btn_back, this);
        if(isUser) {
            Util.setOnClickListener(view, R.id.btn_ok, this);
        } else {
            view.findViewById(R.id.btn_ok).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            String content = etContent.getText().toString().trim();
            if (StringUtil.isEmpty(content)) {
                ToastUtil.error(_mActivity, "個性簽名不能為空");
                return;
            }

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "signature", content);

            SLog.info("params[%s]", params);
            Api.postUI(Api.PATH_EDIT_MEMBER_SIGNATURE, params, new UICallback() {
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

                        ToastUtil.success(_mActivity, "修改成功");
                        Bundle bundle = new Bundle();
                        bundle.putString("memberSignature", content);
                        setFragmentResult(RESULT_OK, bundle);

                        HawkUtil.setUserData(SPField.USER_DATA_KEY_SIGNATURE, content);

                        hideSoftInputPop();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
