package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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

public class EditMemberSignatureFragment extends BaseFragment implements View.OnClickListener {
    String memberSignature;

    EditText etContent;
    TextView tvWordCount;

    public static EditMemberSignatureFragment newInstance(String originalMemberSignature) {
        Bundle args = new Bundle();

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
        memberSignature = args.getString("originalMemberSignature");


        tvWordCount = view.findViewById(R.id.tv_word_count);
        etContent = view.findViewById(R.id.et_content);
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
        etContent.setText(memberSignature);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInput();
            pop();
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

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        ToastUtil.success(_mActivity, "修改成功");
                        Bundle bundle = new Bundle();
                        bundle.putString("memberSignature", content);
                        setFragmentResult(RESULT_OK, bundle);

                        hideSoftInput();
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
