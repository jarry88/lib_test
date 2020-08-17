package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.CareerItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.DateSelectPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 添加工作經歷
 * @author zwm
 */
public class AddWorkExpFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    EditText edCompanyName;
    EditText edJobTitle;
    EditText edWorkExplain;
    TextView tvTitle;
    TextView tvStartTime;
    TextView tvEndTime;
    String startTime;
    String endTime;
    CareerItem experienceItem;
    boolean loadedWorkData;
    int workId;
    private TextView tvWordCount;

    public static AddWorkExpFragment newInstance(int workId, int goodsId) {
        Bundle args = new Bundle();
        AddWorkExpFragment fragment = new AddWorkExpFragment();
        fragment.workId=workId;
        fragment.setArguments(args);

        return fragment;
    }

    public static AddWorkExpFragment newInstance(CareerItem experienceItem) {
        Bundle args = new Bundle();
        AddWorkExpFragment fragment = new AddWorkExpFragment();
        fragment.setArguments(args);
        fragment.experienceItem=experienceItem;
        fragment.workId = experienceItem.Id;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_work_exp, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SLog.info("onViewCreated");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view,R.id.tv_start_time,this);
        Util.setOnClickListener(view, R.id.tv_end_time, this);
        Util.setOnClickListener(view,R.id.btn_ok,this);

        edCompanyName=view.findViewById(R.id.et_company_name);
        edJobTitle=view.findViewById(R.id.et_position);
        edWorkExplain = view.findViewById(R.id.et_work_explain);
        tvWordCount = view.findViewById(R.id.tv_word_count);
        edWorkExplain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int wordCount = s.length();
                tvWordCount.setText(String.format("%d/500", wordCount));
            }
        });
        tvStartTime = view.findViewById(R.id.tv_start_time);
        tvEndTime=view.findViewById(R.id.tv_end_time);
        tvTitle = view.findViewById(R.id.tvTitle);
        loadWorkData(view);
    }

    private void loadWorkData(View view) {
        if (experienceItem == null) {
            return;
        }
        view.findViewById(R.id.btn_delete).setVisibility(View.VISIBLE);
        Util.setOnClickListener(view,R.id.btn_delete,this);

        tvTitle.setText("編輯工作經歷");
        edCompanyName.setText(experienceItem.platformName);
        edJobTitle.setText(experienceItem.major);
        String start = experienceItem.toMonthChina(experienceItem.StartDateFormat);
        tvStartTime.setText(start);
        startTime = experienceItem.StartDateFormat+" 01:01:01";

        String end = experienceItem.toMonthChina(experienceItem.EndDateFormat);
        tvEndTime.setText(end);
        endTime = experienceItem.EndDateFormat+" 01:01:01";
        edWorkExplain.setText(experienceItem.Explain);
        loadedWorkData = true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
        if (id == R.id.tv_start_time) {
            SLog.info("startTime[%s]",startTime);
            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new DateSelectPopup(_mActivity, PopupType.SELECT_START_MONTH, startTime, this))
                    .show();
        }
        if (id == R.id.tv_end_time) {
            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new DateSelectPopup(_mActivity, PopupType.SELECT_END_MONTH, endTime, this))
                    .show();
        }
        if (id == R.id.btn_delete) {
            String url = Api.PATH_EXPERIENCE_DELETE;
            EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(), "workId", workId);
            SLog.info("params[%s]",params);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(_mActivity,e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responstr[%s]",responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }
                    ToastUtil.success(_mActivity,"刪除成功");
                    setFragmentResult(RESULT_OK,null);
                    hideSoftInputPop();
                }
            });
        }
        if (id == R.id.btn_ok) {
            saveWorkDate();
        }
    }

    private void saveWorkDate() {
        String url = Api.PATH_EXPERIENCE_EDIT;
        EasyJSONObject params=EasyJSONObject.generate(
                "token", User.getToken(),
                "companyName",edCompanyName.getText(),//  公司名稱
                "jobTitle",edJobTitle.getText(),// 職位名稱
                "workExplain",edWorkExplain.getText(),// 說明
                "workStartDate",startTime,// 工作開始時間
                "workEndDate",endTime// 工作結束時間
        );
        if (workId > 0) {
            try {
                params.set("workId",workId);//  工作经验唯一标识)
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }
        SLog.info("params[%s]",params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if(ToastUtil.checkError(_mActivity, responseObj)){
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }
                   // workId = responseObj.getInt("datas.workId");
                    setFragmentResult(RESULT_OK,null);
                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        String dateStr = extra.toString();//eg:2021-05-10 12:52:03
        String dateFormat=String.format("%04d年%02d月",Integer.parseInt(dateStr.substring(0,4)),Integer.parseInt(dateStr.substring(5,7)));
        if (type == PopupType.SELECT_START_MONTH) {
            tvStartTime.setText(dateFormat);
            startTime=dateStr;
        }
        if (type == PopupType.SELECT_END_MONTH) {
            tvEndTime.setText(dateFormat);
            endTime=dateStr;
        }
    }
}
