package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ContactMeAdapter;
import com.ftofs.twant.adapter.JobApplicationAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.JobInfoItem;
import com.ftofs.twant.entity.WantedPostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class JobInfoFragment extends BaseFragment implements View.OnClickListener {
    public static final int TAB_MY_APPLICATION=1;
    public static final int TAB_MY_FOLLOW=2;
    public static final int TAB_CONTACT_ME=3;
    public static final int TAB_MY_RESUME=0;
    int currTabIndex=TAB_MY_APPLICATION;
    boolean myApplicationLoaded=false;
    boolean myFollowLoaded=false;
    boolean contactMeLoaded=false;
    RecyclerView rvJobInfoList;
    List<JobInfoItem> hrRelationshipVoList =new ArrayList();
    List<JobInfoItem> postFollowList = new ArrayList<>();
    List<JobInfoItem> followJobList =new ArrayList<>();
    List<JobInfoItem> contactMeList = new ArrayList<>();
    JobApplicationAdapter jobApplicationAdapter;
    JobApplicationAdapter jobFollowAdapter;
    ContactMeAdapter contactMeAdapter;
    private List<JobInfoItem> jobApplicationList=new ArrayList<>();
    private MemberResumeFragment myResumeFragment;
    private boolean myResumeLoaded;


    public static JobInfoFragment newInstance(){
        JobInfoFragment item = new JobInfoFragment();
        Bundle args=new Bundle();
        item.setArguments(args);
        return item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_job_info,container,false);
        return view;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");
        if (myResumeFragment != null&&currTabIndex==TAB_MY_RESUME) {
            myResumeFragment.updateViewDate();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.btn_member_resume,this);
        rvJobInfoList = view.findViewById(R.id.rv_job_info_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        jobApplicationAdapter = new JobApplicationAdapter(jobApplicationList);
        jobApplicationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JobInfoItem jobInfoItem = jobApplicationList.get(position);
                if (jobInfoItem.itemType!=JobInfoItem.TYPE_DISPLAY){
                    WantedPostItem hrPost=new WantedPostItem();
                    hrPost.postId=jobInfoItem.postId;
                    hrPost.isCompanyInfoExpand=true;
                    start(JobDetailFragment.newInstance(hrPost /*followItem.postId*/));
                }
            }
        });
        jobFollowAdapter = new JobApplicationAdapter(followJobList);
        jobFollowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JobInfoItem followItem = followJobList.get(position);
                WantedPostItem hrPost=new WantedPostItem();
                hrPost.postId=followItem.postId;
                hrPost.isCompanyInfoExpand=true;
                start(JobDetailFragment.newInstance(hrPost /*followItem.postId*/));
            }
        });
        contactMeAdapter = new ContactMeAdapter(R.layout.contact_me_item, contactMeList);
        contactMeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.tv_store_name) {
                    JobInfoItem contactItem=contactMeList.get(position);
                    Util.startFragment(ShopMainFragment.newInstance(contactItem.shopId));
                }
                if (id == R.id.sb_show_contact_info) {
                    supportCommunicate(position);
                    contactMeAdapter.notifyDataSetChanged();

                }
            }

        });
        rvJobInfoList.setLayoutManager(linearLayoutManager);

        SimpleTabManager simpleTabManager=new SimpleTabManager(TAB_MY_RESUME) {
            @Override
            public void onClick(View v) {
                boolean isRepeat=onSelect(v);
                if (isRepeat) {
                    return;
                }
                int id = v.getId();

                switch (id) {
                    case R.id.btn_my_resume:
                        currTabIndex = TAB_MY_RESUME;
                        if (myResumeLoaded) {
                            myResumeFragment.updateViewDate();
                        }else {
                            loadMyResume();
                        }
                        break;
                    case R.id.btn_my_application:
                        currTabIndex = TAB_MY_APPLICATION;
                        if (myApplicationLoaded) {
                            rvJobInfoList.setAdapter(jobApplicationAdapter);
                        } else {
                            loadMyDeliver();
                        }

                        break;
                    case R.id.btn_my_follow:
                        currTabIndex = TAB_MY_FOLLOW;
                        if (myFollowLoaded) {
                            rvJobInfoList.setAdapter(jobFollowAdapter);
                        } else {
                            loadMyFollow();
                        }
                        break;
                    case R.id.btn_contact_me:
                        currTabIndex=TAB_CONTACT_ME;
                        if (contactMeLoaded) {
                            rvJobInfoList.setAdapter(contactMeAdapter);
                        } else {
                            loadContactMe();
                        }
                        break;
                    default:
                        break;
                }
                updateUI();
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_my_resume));
        simpleTabManager.add(view.findViewById(R.id.btn_my_application));
        simpleTabManager.add(view.findViewById(R.id.btn_my_follow));
        simpleTabManager.add(view.findViewById(R.id.btn_contact_me));

        loadMyResume();
    }

    private void updateUI() {
        if (currTabIndex == TAB_MY_RESUME) {
            getView().findViewById(R.id.ll_my_resume_container).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.rv_job_info_list).setVisibility(View.GONE);
        } else {
            getView().findViewById(R.id.ll_my_resume_container).setVisibility(View.GONE);
            getView().findViewById(R.id.rv_job_info_list).setVisibility(View.VISIBLE);
        }
    }

    private void loadMyResume() {
        myResumeFragment = MemberResumeFragment.newInstance();
        myResumeFragment.setHeadViewGone=true;
        myResumeLoaded = true;
        getFragmentManager().beginTransaction().replace(R.id.ll_my_resume_container, myResumeFragment).commit();

    }

    private void supportCommunicate(int position) {
        String url = Api.PATH_COMMUNICATION_AUTHOR;
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(), "relationshipId", contactMeList.get(position).relationshipId,"state",1);
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
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }
                    contactMeList.get(position).isLook = 1;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadContactMe() {
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        String url = Api.PATH_RESUME_COMMUNICATION;
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]",responseStr);
                    loadingPopup.dismiss();
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }
                    EasyJSONArray hrRelationshipVo = responseObj.getSafeArray("datas.hrRelationshipVoList");
                    for (Object object : hrRelationshipVo) {
                        EasyJSONObject post = (EasyJSONObject) object;
                        JobInfoItem item=new JobInfoItem();
                        SLog.info("post[%s]",object.toString());
                        item.relationshipId=post.getInt("relationshipId");
                        item.shopId = post.getInt("storeId");
                        item.storeName=post.getSafeString("storeName");
                        item.salaryType=post.getSafeString("salaryType");
                        item.positionType=post.getSafeString("postType");
                        item.jobName=post.getSafeString("postTitle");
                        item.storeAvatar = post.getSafeString("storeAvatar");
                        item.isLook = post.getInt("isLook");
                        contactMeList.add(item);

                    }
                    contactMeAdapter.setNewData(contactMeList);
                    rvJobInfoList.setAdapter(contactMeAdapter);
                    contactMeLoaded=true;

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadMyFollow() {
        String url = Api.PATH_RESUME_FOLLOW;
        EasyJSONObject params = EasyJSONObject.generate("token",User.getToken());
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        SLog.info("params[%s]",params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
                loadingPopup.dismiss();
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
                    EasyJSONArray hrRelationshipVo = responseObj.getSafeArray("datas.hrRelationshipVoList");
                    for (Object object : hrRelationshipVo) {
                        EasyJSONObject post = (EasyJSONObject) object;
                        JobInfoItem item = new JobInfoItem();
                        SLog.info("post[%s]", object.toString());
                        item.postId = post.getInt("postId");
                        item.storeName = post.getSafeString("storeName");
                        item.salaryType = post.getSafeString("salaryType");
                        item.salaryRange = post.getSafeString("salaryRange");
                        item.positionType = post.getSafeString("postType");
                        item.jobName = post.getSafeString("postTitle");
                        item.storeAvatar = post.getSafeString("storeAvatar");
                        item.workArea = post.getSafeString("postArea");
                        followJobList.add(item);

                    }
                    jobFollowAdapter.setNewData(followJobList);
                    rvJobInfoList.setAdapter(jobFollowAdapter);
                    SLog.info("applicationJobList[%s]", followJobList.size());

                    myFollowLoaded=true;

                }catch (Exception e) {
                    SLog.info(e.toString());
                }
            }
        });
    }

    private void loadMyDeliver() {
        String url = Api.PATH_RESUME_DELIVER;
        EasyJSONObject params = EasyJSONObject.generate("token",User.getToken());
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        try {
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
                        SLog.info("responseStr[%s]",responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            return;
                        }
                        EasyJSONArray hrRelationshipVo = responseObj.getSafeArray("datas.hrRelationshipVoList");
                        for (Object object : hrRelationshipVo) {
                            EasyJSONObject post = (EasyJSONObject) object;
                            JobInfoItem item=new JobInfoItem();
                            item.postId = post.getInt("postId");
                            item.storeName=post.getSafeString("storeName");
                            item.salaryType=post.getSafeString("salaryType");
                            item.positionType=post.getSafeString("postType");
                            item.salaryRange=post.getSafeString("salaryRange");
                            item.jobName=post.getSafeString("postTitle");
                            item.storeAvatar = post.getSafeString("storeAvatar");
                            item.workArea = post.getSafeString("postArea");
                            jobApplicationList.add(item);

                        }

                        jobApplicationAdapter.setNewData(jobApplicationList);
                        rvJobInfoList.setAdapter(jobApplicationAdapter);
                        myApplicationLoaded=true;

                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });

        } catch (Exception e){
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        myApplicationLoaded=true;
    }


    @Override
    public void onClick(View view) {
        int id =view.getId();
        switch (id){
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_member_resume:
                start(MemberResumeFragment.newInstance());
                default:
                    break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }
}
