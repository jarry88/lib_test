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
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.DateSelectPopup;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 添加教育經歷
 * @author zwm
 */
public class AddEducateExpFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    TextView etSchoolName;
    EditText etMajor;
    TextView tvStartTime;
    TextView tvEndTime;
    EditText etExplain;
    CareerItem educationItem;
    String startMonth;
    String startTime;
    String endTime;
    String endMonth;
    String academic;
    List<ListPopupItem> academicDiplomasList;
    private int academicIndex;
    private TextView tvWordCount;

    public static AddEducateExpFragment newInstance(int commonId, List<ListPopupItem> academicDiplomasList) {
        Bundle args = new Bundle();

        AddEducateExpFragment fragment = new AddEducateExpFragment();
        fragment.academicDiplomasList=academicDiplomasList;
        fragment.setArguments(args);

        return fragment;
    }

    public static AddEducateExpFragment newInstance(CareerItem educationItem, List<ListPopupItem> academicDiplomasList) {
        Bundle args = new Bundle();

        AddEducateExpFragment fragment = new AddEducateExpFragment();
        fragment.setArguments(args);
        fragment.educationItem=educationItem;
        fragment.academicDiplomasList = academicDiplomasList;
        fragment.academic = educationItem.platformName;
        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_educate_exp, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.tv_start_time, this);
        Util.setOnClickListener(view, R.id.tv_end_time, this);
        Util.setOnClickListener(view, R.id.tv_school_type, this);

        etExplain = view.findViewById(R.id.et_education_explain);
        tvStartTime = view.findViewById(R.id.tv_start_time);
        tvEndTime = view.findViewById(R.id.tv_end_time);
        etMajor = view.findViewById(R.id.et_major);
        tvWordCount = view.findViewById(R.id.tv_word_count);
        etExplain.addTextChangedListener(new TextWatcher() {
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
        etSchoolName = view.findViewById(R.id.tv_school_type);
        loadData(view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
        if (id == R.id.tv_start_time) {
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
        if (id == R.id.btn_ok) {
            saveData();
        }
        if (id == R.id.btn_delete) {
            EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(), "educationId", educationItem.Id);
            SLog.info("params[%s]",params);
            Api.postUI(Api.PATH_EDUCATION_DELETE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity,e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responstr[%s]",responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        SLog.info("異常");
                        return;
                    }
                    ToastUtil.success(_mActivity,"刪除成功");
                    setFragmentResult(RESULT_OK,null);
                    hideSoftInputPop();
                }
            });
        }
        if (id ==R.id.tv_school_type&&academicDiplomasList!=null) {
            SLog.info("Here");
            boolean exsit=false;
            for (ListPopupItem item : academicDiplomasList) {
                if (item.title.equals(academic)) {
                    academicIndex = item.id;
                    exsit = true;
                    break;
                }
            }
            if (!exsit) {
                academicIndex = 1;
            }
            SLog.info(academicDiplomasList.toString());
            new XPopup.Builder(_mActivity)
                    .moveUpToKeyboard(false).asCustom(
                    new ListPopup(_mActivity,getString(R.string.text_eduction), PopupType.ACEDEMIC_TYPE,academicDiplomasList,academicIndex,this)
            ).show();
        }
    }

    private void loadData(View view) {
        if (educationItem != null) {
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            view.findViewById(R.id.btn_delete).setVisibility(View.VISIBLE);
            Util.setOnClickListener(view,R.id.btn_delete,this);
            tvTitle.setText("編輯教育經歷");
            etSchoolName.setText(academic);
            etMajor.setText(educationItem.major);
            startTime=educationItem.StartDateFormat+" 01:01:01";
            startMonth= educationItem.toMonthChina(startTime);
            endTime=educationItem.EndDateFormat+" 01:01:01";
            endMonth= educationItem.toMonthChina(endTime);
            tvStartTime.setText(startMonth);
            tvEndTime.setText(endMonth);
            etExplain.setText(educationItem.Explain);
        }
    }
    private void saveData() {
        if (StringUtil.isEmpty(etSchoolName.getText().toString())
        ||StringUtil.isEmpty(etMajor.getText().toString())
        ||StringUtil.isEmpty(tvStartTime.getText().toString())
        ||StringUtil.isEmpty(tvEndTime.getText().toString())
        ||StringUtil.isEmpty(etExplain.getText().toString())){
            ToastUtil.error(_mActivity, "不能為空");
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(),
                "educationStartDate",startTime,// 開始時間\n +
                "educationEndDate",endTime,// 結束時間\n" +
                "educationSchoolName",etSchoolName.getText(),// 学校名称\n" +
                        //"educationSchoolAcademic 学校学历\n" +
                "educationMajor",etMajor.getText(),// 專業\n" +
                "educationExplain",etExplain.getText()// 說明\n"
                );
        if (educationItem!=null&&educationItem.Id > 0) {
            try {
                params.set("educationId", educationItem.Id);//  教育唯一标识)
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }
        SLog.info("params[%s]",params);
        Api.postUI(Api.PATH_EDUCATION_EDIT, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]",responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                setFragmentResult(RESULT_OK,null);
                hideSoftInputPop();
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

        if(type == PopupType.ACEDEMIC_TYPE) {
            SLog.info("onSelected, type[%s], id[%d], extra[%s]", type, id ,extra);
            academicIndex=id;
            academic=academicDiplomasList.get(academicIndex).title;
            etSchoolName.setText(academic);
            return;
        }
        if (extra == null) {
            return;
        }
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
