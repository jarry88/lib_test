package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


public class MyStatusFragment extends BaseFragment implements View.OnClickListener , OnSelectedListener {
    int resumeState;
    String experience;
    String academicDiplomas;
    int experienceIndex;
    int academicIndex;

    TextView tvResumeState;
    TextView tvWorkTime;
    TextView tvAcademicDiplomas;
    String[] resumeStateList;
    List<ListPopupItem> experienceList;
    List<ListPopupItem> academicDiplomasList;
    public static MyStatusFragment newInstance(int resumeState,String experience,String academicDiplomas,String[] resumeStateList,List<ListPopupItem> experienceList,List<ListPopupItem> academicDiplomasList) {
        MyStatusFragment fragment = new MyStatusFragment();
        Bundle args = new Bundle();
        args.putInt("resumeState",resumeState);
        args.putString("experience",experience);
        args.putString("academicDiplomas",academicDiplomas);
        fragment.setDate(resumeStateList, experienceList,academicDiplomasList);
        fragment.setArguments(args);
        return fragment;
    }

    private void setDate(String[] resumeStateList, List<ListPopupItem> experienceList, List<ListPopupItem> academicDiplomasList) {
        this.resumeStateList = resumeStateList;
        this.experienceList = experienceList;
        this.academicDiplomasList = academicDiplomasList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_status, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        resumeState = getArguments().getInt("resumeState");
        experience = getArguments().getString("experience");
        academicDiplomas = getArguments().getString("academicDiplomas");
        RelativeLayout rvTitle = view.findViewById(R.id.rv_my_status_title);
        tvResumeState=view.findViewById(R.id.tv_curr_status_select);
        tvWorkTime=view.findViewById(R.id.tv_work_time_select);
        tvAcademicDiplomas = view.findViewById(R.id.tv_education_select);
        rvTitle.setVisibility(View.GONE);
        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.btn_ok,this);
        Util.setOnClickListener(view,R.id.rv_my_status,this);
        Util.setOnClickListener(view,R.id.rv_work_time,this);
        Util.setOnClickListener(view,R.id.rv_education,this);
        loadStatusInfo();
    }

    private void loadStatusInfo() {
        tvResumeState.setText(resumeStateList[resumeState]);

        tvWorkTime.setText(experience);
        tvAcademicDiplomas.setText(academicDiplomas);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.rv_my_status:
                List<ListPopupItem> statusList = new ArrayList<>();
                for (int i = 0; i < resumeStateList.length; i++) {
                    ListPopupItem item = new ListPopupItem(i, resumeStateList[i], null);
                    statusList.add(item);
                }
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false)
                        .asCustom(new ListPopup(_mActivity, getString(R.string.text_my_status), PopupType.MY_STATUS_TYPE, statusList, resumeState, this))
                .show();
                break;
            case R.id.rv_work_time:
                for (ListPopupItem item : experienceList) {
                    if (item.title.equals(experience)) {
                        experienceIndex = item.id;
                        break;
                    }
                }
                SLog.info(experienceList.toString());
                new XPopup.Builder(_mActivity)
                        .moveUpToKeyboard(false).asCustom(
                        new ListPopup(_mActivity,getString(R.string.text_work_time), PopupType.WORK_TIME_TYPE,experienceList,experienceIndex,this)
                ).show();
                SLog.info(experienceList.toString());
                break;
            case R.id.rv_education:
                for (ListPopupItem item : academicDiplomasList) {
                    if (item.title.equals(academicDiplomas)) {
                        academicIndex = item.id;
                        break;
                    }
                }
                SLog.info(academicDiplomasList.toString());
                new XPopup.Builder(_mActivity)
                        .moveUpToKeyboard(false).asCustom(
                        new ListPopup(_mActivity,getString(R.string.text_eduction), PopupType.ACEDEMIC_TYPE,academicDiplomasList,academicIndex,this)
                ).show();
                break;
            case R.id.btn_ok:
                saveDate();
                break;
            default:
                break;
        }
    }

    private void saveDate() {
        int experienceId=1;
        int academicDiplomasId=1;
        for (ListPopupItem item : experienceList) {
            if (item.title.equals(experience)) {
                experienceId = item.id;
            }
        }
        for (ListPopupItem item : academicDiplomasList) {
            if (item.title.equals(academicDiplomas)) {
                academicDiplomasId = item.id;
            }
        }
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        EasyJSONObject params=EasyJSONObject.generate("token", User.getToken(),"resumeState",resumeState,"workYear",experienceId,"academicDiplomas",academicDiplomasId);
        Api.postUI(Api.PATH_RESUME_STATE_EDIT, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                SLog.info("responseStr[%s]", responseStr);
                Bundle bundle=new Bundle();
                bundle.putInt("resumeState",resumeState);
                bundle.putString("workYear",experience);
                bundle.putString("academicDiplomas",academicDiplomas);
                setFragmentResult(RESULT_OK,bundle);
                ToastUtil.success(_mActivity,"保存成功");
                pop();
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if(type == PopupType.ACEDEMIC_TYPE) {
            SLog.info("onSelected, type[%s], id[%d], extra[%s]", type, id ,extra);
            academicIndex=id;
            academicDiplomas=academicDiplomasList.get(academicIndex).title;
            tvAcademicDiplomas.setText(academicDiplomas);
        }
        if(type == PopupType.WORK_TIME_TYPE) {
            experienceIndex=id;
            experience=experienceList.get(experienceIndex).title;
            tvWorkTime.setText(experience);
            SLog.info("onSelected, type[%s], id[%d], extra[%s]", type, id ,extra);

        }
        if(type == PopupType.MY_STATUS_TYPE) {
            SLog.info("onSelected, type[%s], id[%d], extra[%s]", type, id ,extra);
            resumeState = id;
            tvResumeState.setText(resumeStateList[resumeState]);
        }

    }
}
