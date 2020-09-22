package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CareerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.CareerItem;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class EducationExperienceFragment extends BaseFragment implements View.OnClickListener {
    boolean loadCareerDetail;
    CareerAdapter experienceAdapter;
    List<CareerItem> experienceList=new ArrayList<>();
    CareerAdapter educationAdapter;
    List<CareerItem> educationList=new ArrayList<>();
    CareerAdapter certificateAdapter;
    List<CareerItem> certificateList=new ArrayList<>();
    CareerAdapter winningExperienceAdapter;
    List<CareerItem> winningExperienceList=new ArrayList<>();
    TextView tvSkill;
    String skillExplain;
    List<ListPopupItem> academicDiplomasList;

    public static EducationExperienceFragment newInstance() {
        EducationExperienceFragment item = new EducationExperienceFragment();
        Bundle args = new Bundle();
        item.setArguments(args);

        return item;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_education_experience,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.rv_work_experience_title,this);
        Util.setOnClickListener(view,R.id.rv_education_experience_title,this);
        Util.setOnClickListener(view,R.id.rv_certificate_title,this);
        Util.setOnClickListener(view,R.id.rv_honor_title,this);
        Util.setOnClickListener(view,R.id.rv_skill_title,this);
        experienceAdapter = new CareerAdapter(R.layout.career_item, experienceList);
        experienceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CareerItem experienceItem = experienceList.get(position);
                startForResult(AddWorkExpFragment.newInstance(experienceItem), RequestCode.WORK_EXP.ordinal());;
            }
        });
        educationAdapter = new CareerAdapter(R.layout.career_item, educationList);
        educationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CareerItem educationItem = educationList.get(position);
                startForResult(AddEducateExpFragment.newInstance(educationItem,academicDiplomasList), RequestCode.EDUCATE_EXP.ordinal());
            }
        });
        certificateAdapter = new CareerAdapter(R.layout.career_item, certificateList);
        certificateAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startForResult(AddSkillFragment.newInstance(certificateList,1,position),RequestCode.CERTIFICATION.ordinal());
            }
        });
        winningExperienceAdapter = new CareerAdapter(R.layout.career_item, winningExperienceList);
        winningExperienceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startForResult(AddSkillFragment.newInstance(winningExperienceList,2,position),RequestCode.WINNINGEXPERIENCE.ordinal());
            }
        });
        tvSkill = view.findViewById(R.id.tv_skill);
        loadCareerDetail(view);

    }

    private void loadCareerDetail(View v) {
        String token = User.getToken();
        EasyJSONObject params=EasyJSONObject.generate("token",token);
        SLog.info("params[%s]", params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        Api.postUI(Api.PATH_CAREER_DETAIL, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    loadingPopup.dismiss();

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    EasyJSONObject career = responseObj.getSafeObject("datas.career");
                    EasyJSONArray experienceJsonList = responseObj.getSafeArray("datas.experienceList");
                    if(experienceJsonList.length()>0) {
                        experienceList.clear();
                        for (Object object : experienceJsonList) {
                            EasyJSONObject experience=(EasyJSONObject)object;
                            SLog.info("experienceItem [%s]",experience.toString());
                            CareerItem careerItem = new CareerItem();
                            careerItem.itemType=CareerItem.TYPE_EXPERIENCE;
                            careerItem.Id = experience.getInt("workId");
                            careerItem.platformName=experience.getSafeString("companyName");
                            careerItem.major=experience.getSafeString("jobTitle");
                            careerItem.StartDateFormat=experience.getSafeString("workStartDateFormat");
                            careerItem.EndDateFormat=experience.getSafeString("workEndDateFormat");
                            careerItem.Explain=experience.getSafeString("workExplain");
                            experienceList.add(careerItem);
                            experienceAdapter.setNewData(experienceList);
                        }
                        RecyclerView rvExperienceContainer = v.findViewById(R.id.rv_experience_list);
                        LinearLayout llExperienceInit = v.findViewById(R.id.ll_work_experience_init);
                        llExperienceInit.setVisibility(View.GONE);
                        rvExperienceContainer.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
                        rvExperienceContainer.setLayoutManager(linearLayoutManager);
                        rvExperienceContainer.setAdapter(experienceAdapter);
                    }

                    EasyJSONArray educationJsonList = responseObj.getSafeArray("datas.educationList");
                    if(educationJsonList.length()>0) {
                        educationList.clear();
                        SLog.info("educationList [%s]",educationJsonList.toString());
                        for (Object object : educationJsonList) {
                            EasyJSONObject education=(EasyJSONObject)object;
                            CareerItem careerItem = new CareerItem();
                            careerItem.itemType=CareerItem.TYPE_EDUCATION;
                            careerItem.Id = education.getInt("educationId");
                            careerItem.platformName=education.getSafeString("educationSchoolName");
                            careerItem.major=education.getSafeString("educationMajor");
                            careerItem.StartDateFormat=education.getSafeString("educationStartDateFormat");
                            careerItem.EndDateFormat=education.getSafeString("educationEndDateFormat");
                            careerItem.Explain=education.getSafeString("educationExplain");
                            educationList.add(careerItem);
                        }
                        educationAdapter.setNewData(educationList);
                        RecyclerView rvEducationContainer = v.findViewById(R.id.rv_education_list);
                        LinearLayout llEducationInit = v.findViewById(R.id.ll_education_experience_init);
                        llEducationInit.setVisibility(View.GONE);
                        rvEducationContainer.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
                        rvEducationContainer.setLayoutManager(linearLayoutManager);
                        rvEducationContainer.setAdapter(educationAdapter);
                    }
                    EasyJSONArray certificateJsonList = career.getSafeArray("certificateList");
                    if(certificateJsonList.length()>0) {
                        certificateList.clear();
                        SLog.info("certificateJsonList[%s]",certificateJsonList.toString());
                        for (Object object : certificateJsonList) {
                            String certificate=object.toString();
                            CareerItem careerItem = new CareerItem();
                            careerItem.itemType=CareerItem.TYPE_CERTIFICATE;
                            careerItem.Explain=certificate;
                            certificateList.add(careerItem);
                        }
                        certificateAdapter.setNewData(certificateList);
                        RecyclerView rvCertificateContainer = v.findViewById(R.id.rv_certificate_list);
                        LinearLayout llCertificateInit = v.findViewById(R.id.ll_certificate_init);
                        llCertificateInit.setVisibility(View.GONE);
                        rvCertificateContainer.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
                        rvCertificateContainer.setLayoutManager(linearLayoutManager);
                        rvCertificateContainer.setAdapter(certificateAdapter);
                    }
                    EasyJSONArray winningExperienceJsonList = career.getSafeArray("winningExperienceList");
                    if(winningExperienceJsonList.length()>0) {
                        winningExperienceList.clear();

                        for (Object object : winningExperienceJsonList) {
                            String winningExperience=object.toString();
                            CareerItem careerItem = new CareerItem();
                            careerItem.itemType=CareerItem.TYPE_WINNINGEXPERIENCE;
                            careerItem.Explain=winningExperience;
                            winningExperienceList.add(careerItem);
                        }
                        winningExperienceAdapter.setNewData(winningExperienceList);
                        RecyclerView rvWinningExperienceContainer = v.findViewById(R.id.rv_honor_list);
                        LinearLayout llEducationInit = v.findViewById(R.id.ll_honor_init);
                        llEducationInit.setVisibility(View.GONE);
                        rvWinningExperienceContainer.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
                        rvWinningExperienceContainer.setLayoutManager(linearLayoutManager);
                        rvWinningExperienceContainer.setAdapter(winningExperienceAdapter);

                    }
                    skillExplain= career.getSafeString("skill");
                    SLog.info("skill[%s]",skillExplain);
                    if(!StringUtil.isEmpty(skillExplain)) {

                        LinearLayout llSkillInit = v.findViewById(R.id.ll_skill_init);
                        llSkillInit.setVisibility(View.GONE);
                        tvSkill.setVisibility(View.VISIBLE);
                        tvSkill.setText(skillExplain);
                    }
                    loadCareerDetail = true;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.rv_work_experience_title:
                startForResult(AddWorkExpFragment.newInstance(0,1), RequestCode.WORK_EXP.ordinal());
                break;
            case R.id.rv_education_experience_title:
                startForResult(AddEducateExpFragment.newInstance(0,null),RequestCode.EDUCATE_EXP.ordinal());
                break;
            case R.id.rv_honor_title:
                startForResult(AddSkillFragment.newInstance(winningExperienceList,2,-1),RequestCode.WINNINGEXPERIENCE.ordinal());
                break;
            case R.id.rv_certificate_title:
                startForResult(AddSkillFragment.newInstance(certificateList,1,-1),RequestCode.CERTIFICATION.ordinal());
                break;
            case  R.id.rv_skill_title:
                startForResult(AddSkillFragment.newInstance(skillExplain,3),RequestCode.EDIT_SKILL.ordinal());
                default:
                    break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == RequestCode.EDIT_SKILL.ordinal()) {
            SLog.info("data[%s]",data.toString());
            skillExplain = data.getString("skill");
            tvSkill.setText(skillExplain);
            return;
        }
        loadCareerDetail(getView());
    }
}
