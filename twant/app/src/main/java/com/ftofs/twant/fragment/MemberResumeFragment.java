package com.ftofs.twant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CareerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.CareerItem;
import com.ftofs.twant.entity.ListPopupItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.kyleduo.switchbutton.SwitchButton;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportActivity;
import okhttp3.Call;

public class MemberResumeFragment extends BaseFragment implements View.OnClickListener {
    public JobInfoFragment parent;
    private ImageView imgAvatar;
    SwitchButton sbShowPersonalInfo;
    TextView tvResumeKeyword;
    TextView tvPostTypeName;
    EditText etExpectPosition;
    TextView tvSalaryRange;
    TextView tvResumeState;
    TextView tvWorkTime;
    TextView tvAcademicDiplomas;
    boolean loadedResumeInfo=false;
    boolean loadedCareer=false;
    String expectPosition;
    String resumeKeyword;
    String postTypeName;
    String salaryTypeName;
    String salaryRange;
    private String experience;
    private int resumeState;
    int sbState;
    private String academicDiplomas;
    List<ListPopupItem> hrPostTypeList = new ArrayList<>();
    List<ListPopupItem> hrSalaryTypeList = new ArrayList<>();
    List<ListPopupItem> hrSalaryRangeList = new ArrayList<>();
    List<ListPopupItem> academicDiplomasList = new ArrayList<>();
    List<ListPopupItem> myexperienceList = new ArrayList<>();
    List<ListPopupItem> keywordList = new ArrayList<>();
    List<ListPopupItem> parentKeywordList = new ArrayList<>();


    String[]resumeStateList=new String[]{"未設置",
            "離職，正在找工作",
            "在職，考慮其他機會",
            "在職，暫不考慮其他"};
    private String skillExplain;
    private TextView tvSkill;
    public RelativeLayout rvHeadToolBar;
    public boolean setHeadViewGone;
    private View mView;

    public static MemberResumeFragment newInstance(){
        MemberResumeFragment item = new MemberResumeFragment();
        Bundle args = new Bundle();
        item.setArguments(args);
        return  item;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.sb_show_personal_info,this);
        Util.setOnClickListener(view,R.id.btn_expect_position,this);
        Util.setOnClickListener(view,R.id.btn_my_status,this);
        Util.setOnClickListener(view,R.id.rv_work_experience_title,this);
        Util.setOnClickListener(view,R.id.rv_honor_title,this);
        Util.setOnClickListener(view,R.id.rv_skill_title,this);
        Util.setOnClickListener(view,R.id.btn_edit_personal_info,this);
        etExpectPosition = view.findViewById(R.id.et_receiver_expect_position);
        etExpectPosition.setFocusable(false);
        etExpectPosition.setFocusableInTouchMode(false);
        tvResumeKeyword = view.findViewById(R.id.tv_position_keyword_select);
        tvPostTypeName =view.findViewById(R.id.tv_position_category_select);
        tvSalaryRange = view.findViewById(R.id.tv_expect_salary_select);
        rvHeadToolBar = view.findViewById(R.id.tool_bar);
        if (setHeadViewGone) {
            rvHeadToolBar.setVisibility(View.GONE);
        }

        tvResumeState=view.findViewById(R.id.tv_curr_status_select);
        tvWorkTime = view.findViewById(R.id.tv_work_time_select);
        tvAcademicDiplomas = view.findViewById(R.id.tv_education_select);
        imgAvatar =view.findViewById(R.id.img_avatar);
        sbShowPersonalInfo = view.findViewById(R.id.sb_show_personal_info);
        tvSkill = view.findViewById(R.id.tv_skill);


        loadResumeInfo(view);
        loadCareer(view);
        initView(view);
    }

    private void initView(View view) {
        view.findViewById(R.id.exp_job_keyword).setVisibility(View.GONE);
        view.findViewById(R.id.exp_job_type).setVisibility(View.GONE);
        view.findViewById(R.id.exp_expection_salary).setVisibility(View.GONE);
        view.findViewById(R.id.exp_curr_status).setVisibility(View.GONE);
        view.findViewById(R.id.exp_education).setVisibility(View.GONE);
        view.findViewById(R.id.exp_work_time).setVisibility(View.GONE);
    }

    private void loadCareer(View v) {
        String token = User.getToken();
        String url = Api.PATH_CAREER_DETAIL;
        EasyJSONObject params=EasyJSONObject.generate("token",token);
        SLog.info("params[%s]", params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    loadingPopup.dismiss();

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }
                    EasyJSONObject career = responseObj.getSafeObject("datas.career");
                    EasyJSONArray experienceJsonList = responseObj.getSafeArray("datas.experienceList");
                    initExperienceListView(_mActivity,getView(),experienceJsonList,true);
                    EasyJSONArray educationJsonList = responseObj.getSafeArray("datas.educationList");
                    initEducationListView(_mActivity,getView(),educationJsonList,academicDiplomasList,true);

                    EasyJSONArray certificateJsonList = career.getSafeArray("certificateList");
                    initCertificateListView(_mActivity, getView(), certificateJsonList, true);

                    EasyJSONArray winningExperienceJsonList = career.getSafeArray("winningExperienceList");
                    initWinningListView(_mActivity, getView(), winningExperienceJsonList, true);

                    skillExplain= career.getSafeString("skill");
                    if(!StringUtil.isEmpty(skillExplain)) {
                        LinearLayout llSkillInit = v.findViewById(R.id.ll_skill_init);
                        llSkillInit.setVisibility(View.GONE);
                        tvSkill.setVisibility(View.VISIBLE);
                        tvSkill.setText(skillExplain);
                    }
                    loadedCareer = true;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });

    }

    public static void initExperienceListView(SupportActivity mActivity, View v, EasyJSONArray experienceJsonList, boolean editable) {
        v.findViewById(R.id.rv_work_experience_title).setOnClickListener(v1 -> {
            if (editable) {
                Util.startFragment(AddWorkExpFragment.newInstance(0,0));
            }
        });
        v.findViewById(R.id.btn_add_work).setVisibility(editable?View.VISIBLE:View.GONE);
        RecyclerView rvExperienceContainer = v.findViewById(R.id.rv_experience_list);
        LinearLayout llExperienceInit = v.findViewById(R.id.ll_work_experience_init);
        List<CareerItem> experienceList = new ArrayList<>();
        boolean nullList = false;

        if (!Util.isJsonNull(experienceJsonList) && experienceJsonList.length() > 0) {
            CareerAdapter experienceAdapter=new CareerAdapter(R.layout.career_item,experienceList);
            experienceAdapter.editable = editable;
            if (editable) {
                experienceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        CareerItem experienceItem = experienceList.get(position);
                        SLog.info(experienceItem.toString());
                        Util.startFragment(AddWorkExpFragment.newInstance(experienceItem));
                    }
                });
            }
            experienceList.clear();
            for (Object object : experienceJsonList) {
                EasyJSONObject experience = (EasyJSONObject) object;
                CareerItem careerItem = new CareerItem();
                try {
                    careerItem.Id = experience.getInt("workId");
                    careerItem.itemType = CareerItem.TYPE_EXPERIENCE;
                    careerItem.platformName = experience.getSafeString("companyName");
                    careerItem.major = experience.getSafeString("jobTitle");
                    careerItem.StartDateFormat = experience.getSafeString("workStartDateFormat");
                    careerItem.EndDateFormat = experience.getSafeString("workEndDateFormat");
                    careerItem.Explain = experience.getSafeString("workExplain");
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

                experienceList.add(careerItem);
                experienceAdapter.setNewData(experienceList);
            }

            llExperienceInit.setVisibility(View.GONE);
            rvExperienceContainer.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
            rvExperienceContainer.setLayoutManager(linearLayoutManager);
            rvExperienceContainer.setAdapter(experienceAdapter);
        } else {
            nullList = true;
            rvExperienceContainer.setVisibility(View.GONE);
        }
        llExperienceInit.setVisibility(editable&&nullList?View.VISIBLE:View.GONE);

    }

    public static void initWinningListView(Context mActivity, View v, EasyJSONArray winningExperienceJsonList, boolean editable) {
        List<CareerItem> winningExperienceList = new ArrayList<>();
        v.findViewById(R.id.rv_honor_title).setOnClickListener(v1 ->{
            if (editable) {
                Util.startFragment(AddSkillFragment.newInstance(winningExperienceList,2,-1));

            }
        });
        if (!editable) {
            v.findViewById(R.id.btn_add_honnor_experience).setVisibility(View.GONE);
        }
        RecyclerView rvWinningExperienceContainer = v.findViewById(R.id.rv_honor_list);
        LinearLayout llWinningExperienceInit = v.findViewById(R.id.ll_honor_init);

        if (!Util.isJsonNull(winningExperienceJsonList)) {
            winningExperienceList.clear();
            CareerAdapter winningExperienceAdapter = new CareerAdapter(R.layout.career_item, winningExperienceList);
            winningExperienceAdapter.editable = editable;
            if (editable) {
                winningExperienceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Util.startFragment(AddSkillFragment.newInstance(winningExperienceList, 2, position));
                    }
                });
            }
            for (Object object : winningExperienceJsonList) {
                String winningExperience = object.toString();
                CareerItem careerItem = new CareerItem();
                careerItem.itemType = CareerItem.TYPE_WINNINGEXPERIENCE;
                careerItem.Explain = winningExperience;
                winningExperienceList.add(careerItem);
            }
            winningExperienceAdapter.setNewData(winningExperienceList);
            llWinningExperienceInit.setVisibility(View.GONE);
            rvWinningExperienceContainer.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
            rvWinningExperienceContainer.setLayoutManager(linearLayoutManager);
            rvWinningExperienceContainer.setAdapter(winningExperienceAdapter);
        } else {
            llWinningExperienceInit.setVisibility(View.VISIBLE);
            rvWinningExperienceContainer.setVisibility(View.GONE);
        }
    }

    public static void initCertificateListView(Context mActivity, View v, EasyJSONArray certificateJsonList, boolean editable) {
        List<CareerItem> certificateList = new ArrayList<>();
        v.findViewById(R.id.rv_certificate_title).setOnClickListener(v1 ->{
            if(editable){
                Util.startFragment(AddSkillFragment.newInstance(certificateList,1,-1));
            }
        });
        LinearLayout btnAddCertificate = v.findViewById(R.id.btn_add_certificate);
        btnAddCertificate.setVisibility(editable?View.VISIBLE:View.GONE);
        RecyclerView rvCertificateContainer = v.findViewById(R.id.rv_certificate_list);
        LinearLayout llCertificateInit = v.findViewById(R.id.ll_certificate_init);
        boolean nullList = false;
        if (!Util.isJsonNull(certificateJsonList)) {
            certificateList.clear();

            CareerAdapter certificateAdapter = new CareerAdapter(R.layout.career_item, certificateList);
            certificateAdapter.editable = editable;
            certificateAdapter.setOnItemClickListener((adapter, view, position) -> {
                if(editable){
                    Util.startFragment(AddSkillFragment.newInstance(certificateList, 1, position));
                }
            });
            SLog.info("certificateJsonList[%s]", certificateJsonList.toString());
            for (Object object : certificateJsonList) {
                String certificate = object.toString();
                CareerItem careerItem = new CareerItem();
                careerItem.itemType = CareerItem.TYPE_CERTIFICATE;
                careerItem.Explain = certificate;
                certificateList.add(careerItem);
            }
            certificateAdapter.setNewData(certificateList);

            llCertificateInit.setVisibility(View.GONE);
            rvCertificateContainer.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
            rvCertificateContainer.setLayoutManager(linearLayoutManager);
            rvCertificateContainer.setAdapter(certificateAdapter);
        } else {
            nullList = true;
            rvCertificateContainer.setVisibility(View.GONE);
        }
        llCertificateInit.setVisibility(editable&&nullList?View.VISIBLE:View.GONE);
    }

    /**
 * 綁定數據及設置點擊響應時間
 * @param educationJsonList 教育列表json
 * @param academicDiplomasList 處理為ListPopupItem後的服務器返回的學歷列表
 */
    public static void initEducationListView(Context context,View v,EasyJSONArray educationJsonList,List<ListPopupItem> academicDiplomasList,boolean editable ) {
        v.findViewById(R.id.rv_education_experience_title).setOnClickListener(v1 ->{
            if(editable){
                Util.startFragment(AddEducateExpFragment.newInstance(0,academicDiplomasList));
            }
        });
        RecyclerView rvEducationContainer = v.findViewById(R.id.rv_education_list);
        LinearLayout llEducationInit = v.findViewById(R.id.ll_education_experience_init);
        LinearLayout btnAddEducation = v.findViewById(R.id.btn_add_education);

        btnAddEducation.setVisibility(editable?View.VISIBLE:View.GONE);

        List<CareerItem> educationList = new ArrayList<>();
        boolean nullList= false;


        if (!Util.isJsonNull(educationJsonList) && educationJsonList.length() > 0) {
            for (Object object : educationJsonList) {
                EasyJSONObject education = (EasyJSONObject) object;
                SLog.info("experienceItem [%s]", education.toString());
                CareerItem careerItem = new CareerItem();
                careerItem.itemType = CareerItem.TYPE_EDUCATION;
                try {
                    careerItem.Id = education.getInt("educationId");
                    careerItem.platformName = education.getSafeString("educationSchoolName");
                    careerItem.major = education.getSafeString("educationMajor");
                    careerItem.StartDateFormat = education.getSafeString("educationStartDateFormat");
                    careerItem.EndDateFormat = education.getSafeString("educationEndDateFormat");
                    careerItem.Explain = education.getSafeString("educationExplain");
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

                educationList.add(careerItem);
            }
            CareerAdapter educationAdapter = new CareerAdapter(R.layout.career_item, educationList);
            if (editable) {
                educationAdapter.setOnItemClickListener((adapter, view, position) -> {
                    CareerItem educationItem = educationList.get(position);
                    Util.startFragment(AddEducateExpFragment.newInstance(educationItem, academicDiplomasList));
                });
            } else {
                educationAdapter.editable=editable;
            }

            educationAdapter.setNewData(educationList);

            llEducationInit.setVisibility(View.GONE);
            rvEducationContainer.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            rvEducationContainer.setLayoutManager(linearLayoutManager);
            rvEducationContainer.setAdapter(educationAdapter);
        } else {
            nullList = true;
            rvEducationContainer.setVisibility(View.GONE);
        }
        llEducationInit.setVisibility(editable&&nullList?View.VISIBLE:View.GONE);

    }

    private void loadResumeInfo(View v) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_MEMBER_RESUME;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        SLog.info("params[%s]", params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    loadingPopup.dismiss();
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    EasyJSONObject resumeInfo = responseObj.getSafeObject("datas.resumeInfo");
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }
                    String avatar = resumeInfo.getSafeString("avatar");
                    if (!StringUtil.isEmpty(avatar)) {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatar)).centerCrop().into(imgAvatar);
                    }
                    //頂部個人信息
                    TextView tvName =v.findViewById(R.id.tv_nickname);
                    tvName.setText(resumeInfo.getSafeString("nickName"));

                    TextView mobile = v.findViewById(R.id.tv_member_mobile);
                    mobile.setText(resumeInfo.getSafeString("mobile"));

                    TextView tvEmail=v.findViewById(R.id.tv_member_mail);
                    String email=resumeInfo.getSafeString("email");
                    if (StringUtil.isEmpty(email)) {
                        email = getString(R.string.text_not_set);
                    }
                    tvEmail.setText(email);

                    sbState=resumeInfo.getInt("state");
                    SLog.info("hrCareer[%d]",sbState);
                    if(sbState==0){
                        sbShowPersonalInfo.setChecked(false);
                    }else{
                        sbShowPersonalInfo.setChecked(true);
                    }

                    //期望職位
                    expectPosition = resumeInfo.getSafeString("resumeWish");
                    etExpectPosition.setText(expectPosition);
                    resumeKeyword = resumeInfo.getSafeString("resumeKeyword");
                    tvResumeKeyword.setText(resumeKeyword);

                    postTypeName =resumeInfo.getSafeString("postTypeName");
                    tvPostTypeName.setText(postTypeName);
                    salaryTypeName=resumeInfo.getSafeString("salaryTypeName");

                    salaryRange=resumeInfo.getSafeString("salaryRange");
                    if (!StringUtil.isEmpty(salaryTypeName)) {
                        tvSalaryRange.setText(salaryTypeName.concat(":").concat(salaryRange));
                    }
                    //我的狀態
                    resumeState = resumeInfo.getInt("resumeState");
                    tvResumeState.setText(resumeStateList[resumeState]);
                    experience = resumeInfo.getSafeString("experience");
                    tvWorkTime.setText(experience);
                    SLog.info(resumeInfo.toString());
                    academicDiplomas = resumeInfo.getSafeString("academicDiplomas");
                    if (!StringUtil.isEmpty(academicDiplomas)) {
                        tvAcademicDiplomas.setText(academicDiplomas);

                    }


                    EasyJSONObject hrCareer = resumeInfo.getSafeObject("hrCareer");


                    EasyJSONArray keyword = responseObj.getSafeArray("datas.keywordList");
                    EasyJSONArray experience = responseObj.getSafeArray("datas.experienceList");
                    EasyJSONArray hrPostType = responseObj.getSafeArray("datas.hrPostTypeList");
                    EasyJSONArray parentKeyword = responseObj.getSafeArray("datas.parentKeywordList");
                    EasyJSONArray hrSalaryType = responseObj.getSafeArray("datas.hrSalaryTypeList");
                    EasyJSONArray hrSalaryRange = responseObj.getSafeArray("datas.hrSalaryRangeList");
                    EasyJSONArray academicDiplomas = responseObj.getSafeArray("datas.academicDiplomasList");
                    SLog.info(academicDiplomas.toString());
                    keywordList.clear();
                    for (int i=0;i<keyword.length();i++){
                        keywordList.add(new ListPopupItem(keyword.getObject(i).getInt("keywordId"), keyword.getObject(i).getSafeString("keyword"), null));
                    }
                    SLog.info(keyword.toString());
                    SLog.info(keywordList.toString());
                    myexperienceList.clear();
                    for (int i=0;i<experience.length();i++){
                        myexperienceList.add(new ListPopupItem(experience.getObject(i).getInt("experienceId"), experience.getObject(i).getSafeString("experience"), null));
                    }
                    hrPostTypeList.clear();
                    for (int i=0;i<hrPostType.length();i++){
                        hrPostTypeList.add(new ListPopupItem(hrPostType.getObject(i).getInt("typeId"), hrPostType.getObject(i).getSafeString("typeName"), null));
                    }
                    parentKeywordList.clear();
                    for (int i=0;i<parentKeyword.length();i++){
                        parentKeywordList.add(new ListPopupItem(parentKeyword.getObject(i).getInt("keywordId"), parentKeyword.getObject(i).getSafeString("keyword"), null));
                    }
                    hrSalaryTypeList.clear();
                    for (int i=0;i<hrSalaryType.length();i++){
                        hrSalaryTypeList.add(new ListPopupItem(hrSalaryType.getObject(i).getInt("typeId"), hrSalaryType.getObject(i).getSafeString("typeName"), null));
                    }
                    hrSalaryRangeList.clear();
                    for (int i=0;i<hrSalaryRange.length();i++){
                        hrSalaryRangeList.add(new ListPopupItem(hrSalaryRange.getObject(i).getInt("rangeId"), hrSalaryRange.getObject(i).getSafeString("range"), null));
                    }
                    academicDiplomasList.clear();
                    for (int i = 0; i < academicDiplomas.length(); i++) {
                        academicDiplomasList.add(new ListPopupItem(academicDiplomas.getObject(i).getInt("academicId"), academicDiplomas.getObject(i).getSafeString("academicDiplomas"), null));
                    }

                    loadedResumeInfo=true;
                    SLog.info("簡介信息加載完畢");

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
            case R.id.sb_show_personal_info:
                SLog.info("是否公開簡歷[%s]",sbShowPersonalInfo.isChecked());
                setShowPersonalInfo();
                break;
            case R.id.btn_expect_position:
                Util.startFragmentForResult(ExpectPositionFragment.newInstance(expectPosition, resumeKeyword, postTypeName, salaryTypeName, salaryRange, hrPostTypeList, hrSalaryTypeList, hrSalaryRangeList, keywordList), RequestCode.EXPECT_POSITION.ordinal());
                break;
            case R.id.btn_my_status:
                Util.startFragmentForResult(MyStatusFragment.newInstance(resumeState, experience, academicDiplomas, resumeStateList, myexperienceList, academicDiplomasList), RequestCode.MY_STATUS.ordinal());
                break;

            case  R.id.rv_skill_title:
                Util.startFragmentForResult(AddSkillFragment.newInstance(skillExplain,3),RequestCode.EDIT_SKILL.ordinal());
                break;
            case R.id.btn_edit_personal_info:
                Util.startFragment(PersonalInfoFragment.newInstance());
                break;
            default:
                    break;

        }
    }

    private void setShowPersonalInfo() {
        sbState=sbShowPersonalInfo.isChecked()?1:0;
        String url = Api.PATH_RESUME_OPEN;
        EasyJSONObject params=EasyJSONObject.generate("token",User.getToken(),"state",sbState);
        SLog.info("params[%s]",params.toString());
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity,e);
                sbState=1-sbState;
                sbShowPersonalInfo.setChecked(sbState==1);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s][%s]",responseStr,sbShowPersonalInfo.isChecked());
                    EasyJSONObject responseObj=EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        sbState=1-sbState;
                        sbShowPersonalInfo.setChecked(sbState==1);
                        return;
                    }

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");
        updateViewDate();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
//        Util.popToMainFragment(getActivity());
        hideSoftInputPop();
        if (parent != null) {
            parent.onBackPressedSupport();
        }
        return true;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode,data);
        if (resultCode!= RESULT_OK){
            return;
        }
        SLog.info("resuli");

        if(requestCode==RequestCode.MY_STATUS.ordinal()){
            resumeState = data.getInt("resumeState");
            experience = data.getString("workYear");
            academicDiplomas = data.getString("academicDiplomas");
            SLog.info("返回成功");
            tvResumeState.setText(resumeStateList[resumeState]);
            tvWorkTime.setText(experience);
            tvAcademicDiplomas.setText(academicDiplomas);
        }
        if (requestCode == RequestCode.EXPECT_POSITION.ordinal()) {
            expectPosition= data.getString("expectPosition");
            SLog.info("[%s]",expectPosition);
            resumeKeyword= data.getString("resumeKeyword");
            postTypeName=data.getString("postTypeName");
            salaryTypeName=data.getString("salaryTypeName");
            salaryRange=data.getString("salaryRange");
            SLog.info("expectPosition[%s],resumeKeyword[%s],postTypeName[%s]",etExpectPosition,resumeKeyword,postTypeName);
            etExpectPosition.setText(expectPosition);
            tvResumeKeyword.setText(resumeKeyword);
            tvPostTypeName.setText(postTypeName);
            tvSalaryRange.setText(String.format(salaryTypeName+":"+salaryRange));
            return;
        }
        if (requestCode == RequestCode.EDIT_SKILL.ordinal()) {
            SLog.info("data[%s]",data.toString());
            skillExplain = data.getString("skill");
            tvSkill.setText(skillExplain);
            return;
        }
        updateViewDate();
    }

    public void updateViewDate() {
        loadResumeInfo(mView);
        loadCareer(mView);
    }
}
