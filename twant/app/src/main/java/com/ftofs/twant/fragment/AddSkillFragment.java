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
import com.ftofs.twant.entity.CareerItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class AddSkillFragment extends BaseFragment implements View.OnClickListener {
    EditText etAddSkill;
    TextView tvWordCount;
    TextView tvTitle;
    TextView tvDelete;
    //  資格證書列表（類型：字符串）
    List<CareerItem> certificateList;
    List<CareerItem> winningExperienceList;
    final int TYPE_CERTIFICATE=1;
    final int TYPE_WINNINGEXPERIENCE=2;
    final int TYPE_SKILL=3;
    int position=-1;
    int type;

    public static AddSkillFragment newInstance(String skill,int type) {
        Bundle args = new Bundle();

        args.putString("skill", skill);
        AddSkillFragment fragment = new AddSkillFragment();
        fragment.setArguments(args);
        fragment.type=type;
        return fragment;
    }
    public static AddSkillFragment newInstance(List<CareerItem> list,int type,int position) {
        Bundle args = new Bundle();

        AddSkillFragment fragment = new AddSkillFragment();
        fragment.setArguments(args);
        fragment.type=type;
        if (type == fragment.TYPE_CERTIFICATE) {
            fragment.certificateList = list;
        } else {
            fragment.winningExperienceList=list;
        }
        fragment.position = position;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_skill, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDelete=view.findViewById(R.id.btn_delete);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        view.findViewById(R.id.btn_delete).setVisibility(View.VISIBLE);
        Util.setOnClickListener(view,R.id.btn_delete,this);

        tvWordCount = view.findViewById(R.id.tv_word_count);

        etAddSkill = view.findViewById(R.id.et_explain);

        switch (type){
            case TYPE_CERTIFICATE:
                tvTitle.setText(getString(R.string.text_certificate));
                etAddSkill.setHint(R.string.edit_certificaion_hint);
                if (position >= 0) {
                    String certification = certificateList.get(position).Explain;
                    if (!StringUtil.isEmpty(certification)) {
                        updateWordCount(certification.length());
                    }
                    etAddSkill.setText(certification);
                } else {
                    tvDelete.setVisibility(View.GONE);
                }
                break;
            case TYPE_WINNINGEXPERIENCE:
                tvTitle.setText(getString(R.string.text_honor_experience));
                etAddSkill.setHint(R.string.edit_winning_hint);
                if (position >=0) {
                    String winningExperience = winningExperienceList.get(position).Explain;
                    if (!StringUtil.isEmpty(winningExperience)) {
                        updateWordCount(winningExperience.length());
                    }
                    etAddSkill.setText(winningExperience);
                }else {
                    tvDelete.setVisibility(View.GONE);
                }
                break;
            case TYPE_SKILL:
                etAddSkill.setHint(R.string.edit_skill_hint);
                String skill = args.getString("skill");
                if (!StringUtil.isEmpty(skill)) {
                    updateWordCount(skill.length());
                }
                etAddSkill.setText(skill);
                tvTitle.setText(getText(R.string.text_skill_good));
                break;
            default:
                break;
        }
        // 字數計數

        etAddSkill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = s.length();
                updateWordCount(len);
            }
        });
        EditTextUtil.cursorSeekToEnd(etAddSkill);
        showSoftInput(etAddSkill);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            saveSkillDate();
        } else if (id == R.id.btn_delete) {
            etAddSkill.setText("");
            deleteDate();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void deleteDate() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }



        EasyJSONObject params=EasyJSONObject.generate();
        try {
            if (type == TYPE_WINNINGEXPERIENCE) {
                EasyJSONArray winningExperienceJsonList = EasyJSONArray.generate();
                for (int i = 0; i < winningExperienceList.size(); i++) {
                    if (i != position) {
                        winningExperienceJsonList.append(winningExperienceList.get(i).Explain);
                    }
                }
                params.set("winningExperienceList",winningExperienceJsonList);
                SLog.info("winning[%s]",winningExperienceList.toString());


            }
            if (type == TYPE_CERTIFICATE) {
                SLog.info("certificateList[%s]",certificateList.toString());
                EasyJSONArray certificateJsonList = EasyJSONArray.generate();
                for (int i = 0; i < certificateList.size(); i++) {
                    if (i != position) {

                        certificateJsonList.append(certificateList.get(i).Explain);
                    }
                }
                params.set("certificateList",certificateJsonList);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        // 提交數據
        String json = params.toString();
        SLog.info("json[%s]", json);

        String  url = Api.PATH_CAREER_EDIT + Api.makeQueryString(EasyJSONObject.generate("token", token));
        SLog.info("URL[%s]", url);
        Api.postJsonUi(url, json, new UICallback() {
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

                    ToastUtil.success(_mActivity, getString(R.string.text_delete_success));


                    Bundle args = new Bundle();
                    setFragmentResult(RESULT_OK,args);
                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void saveSkillDate() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        String text = etAddSkill.getText().toString().trim();
        if (StringUtil.isEmpty(text)) {
            ToastUtil.error(_mActivity, getString(R.string.text_error_null));
            return;
        }

        EasyJSONObject params=EasyJSONObject.generate();
        try {
            if (type == TYPE_SKILL) {
                params.set("skill",text);
            }
            if (type == TYPE_WINNINGEXPERIENCE) {
                EasyJSONArray winningExperienceJsonList = EasyJSONArray.generate();
                for (int i = 0; i < winningExperienceList.size(); i++) {
                    winningExperienceJsonList.append(winningExperienceList.get(i).Explain);
                }

                if (position < 0) {
                    winningExperienceJsonList.append(etAddSkill.getText().toString());
                } else {
                    winningExperienceJsonList.set(position, etAddSkill.getText());
                }
                params.set("winningExperienceList",winningExperienceJsonList);


            }
            if (type == TYPE_CERTIFICATE) {
                SLog.info("certificateList[%s]",certificateList.toString());
                EasyJSONArray certificateJsonList = EasyJSONArray.generate();
                for (int i = 0; i < certificateList.size(); i++) {
                    certificateJsonList.append(certificateList.get(i).Explain);
                }
                if (position < 0) {
                    certificateJsonList.append(etAddSkill.getText().toString());
                } else {
                    certificateJsonList.set(position, etAddSkill.getText());
                }
                params.set("certificateList",certificateJsonList);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }


        // 提交數據
        String json = params.toString();
        SLog.info("json[%s]", json);

        String  url = Api.PATH_CAREER_EDIT + Api.makeQueryString(EasyJSONObject.generate("token", token));
        SLog.info("URL[%s]", url);
        Api.postJsonUi(url, json, new UICallback() {
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

                    ToastUtil.success(_mActivity, getString(R.string.text_save_success));

                    Bundle args = new Bundle();
                    args.putString("skill",text);
                    setFragmentResult(RESULT_OK,args);
                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
    private void updateWordCount(int wordCount) {
        tvWordCount.setText(String.format("%d/500", wordCount));
    }
}