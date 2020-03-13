package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.WantedPostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.BitmapUtil;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.StoreCustomerServicePopup;
import com.hyphenate.chat.EMConversation;
import com.lxj.xpopup.XPopup;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class JobDetailFragment extends BaseFragment implements View.OnClickListener {
    WantedPostItem data;
    //保存客服列表的第一人信息，發起聊天
    FriendInfo storeHr;
    int isFavor;
    int isDeliver;
    int storeId;
    TextView tvPositionTitle;
    TextView tvPositionCategory;
    TextView tvSalary;
    TextView tvArea;
    TextView tvEmail;
    TextView tvPositionDesc;
    TextView tvExperience;
    TextView tvAcademicDiplomas;
    TextView tvPostNumber;
    TextView tvIsAccommodation;
    TextView tvWelfare;

    ImageView shopAvatar;
    TextView tvShopNumber;
    TextView tvShopArea;
    TextView tvShopEmail;
    TextView tvShopExplain;
    MapView mapView;
    TextView tvShopName;
    TextView tvFollow;
    TextView tvDeliver;
    ImageView imgFollow;
    LinearLayout llCompanyInfo;

    public static JobDetailFragment newInstance(WantedPostItem data){
        JobDetailFragment fragment =new JobDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setData(data);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.btn_follow,this);
        Util.setOnClickListener(view,R.id.btn_want_chat,this);
        Util.setOnClickListener(view,R.id.btn_apply,this);
        Util.setOnClickListener(view, R.id.rv_store_title, this);
        tvPositionTitle = view.findViewById(R.id.tv_position_title);
        tvPositionCategory = view.findViewById(R.id.tv_position_category);
        tvSalary = view.findViewById(R.id.tv_salary);
        tvArea = view.findViewById(R.id.tv_area);
        tvEmail = view.findViewById(R.id.tv_mail_box);
        tvPositionDesc = view.findViewById(R.id.tv_position_desc);
        tvExperience = view.findViewById(R.id.tv_experience);
        tvAcademicDiplomas = view.findViewById(R.id.tv_education);
        tvPostNumber = view.findViewById(R.id.tv_hr_number);
        tvIsAccommodation = view.findViewById(R.id.tv_isAccommodation);
        tvWelfare = view.findViewById(R.id.tv_welfare);
        shopAvatar=view.findViewById(R.id.img_shop_avatar);
        tvShopArea=view.findViewById(R.id.tv_store_area);
        tvShopNumber = view.findViewById(R.id.tv_store_mobile);
        tvShopEmail = view.findViewById(R.id.tv_store_mail_box);
        tvShopExplain = view.findViewById(R.id.tv_store_explain);
        mapView = view.findViewById(R.id.map_view);
        tvShopName = view.findViewById(R.id.tv_store_name);
        tvFollow=view.findViewById(R.id.tv_job_follow);
        tvDeliver = view.findViewById(R.id.tv_deliver);
        imgFollow = view.findViewById(R.id.img_follow);
        if(data.isCompanyInfoExpand){
            llCompanyInfo = view.findViewById(R.id.ll_company_info);
            llCompanyInfo.setVisibility(View.VISIBLE);
        }
        loadJobDetail();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mapView.onDestroy();
    }

    private void loadJobDetail() {
        EasyJSONObject params = EasyJSONObject.generate("postId", data.postId);
        SLog.info("params[%s]",params);
        Api.getUI(Api.PATH_STORE_HR_INFO, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                try {
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

//                    for (Map.Entry<String, Object> entry : data.entrySet()) {
//                        SLog.info("key[%s]", entry.getKey());
//                    }

                    EasyJSONObject hrPostVo = responseObj.getSafeObject("datas.hrPostVo");
                    String postTitle = hrPostVo.getSafeString("postTitle");
                    String postType = hrPostVo.getSafeString("postType");
                    String postArea = hrPostVo.getSafeString("postArea");
                    String salaryType= hrPostVo.getSafeString("salaryType");
                    String salaryRange= hrPostVo.getSafeString("salaryRange");
                    String currency= hrPostVo.getSafeString("currency");
                    String postDescription = hrPostVo.getSafeString("postDescription");
                    String mailbox = hrPostVo.getSafeString("mailbox");
                    isFavor = hrPostVo.getInt("postFavor");
                    isDeliver = hrPostVo.getInt("isDeliver");
                    EasyJSONObject storeVo = hrPostVo.getSafeObject("storeVo");
                    String postExplain = hrPostVo.getSafeString("postExplain");
                    if (!Util.isJsonObjectEmpty(storeVo)) {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(storeVo.getSafeString("storeAvatar"))).into(shopAvatar);
                        String storeName=storeVo.getSafeString("storeName");
                        tvShopName.setText(storeName);
                        tvShopNumber.setText(storeVo.getSafeString("chainPhone"));
                        tvArea.setText(storeVo.getSafeString("chainAddress"));
                        tvEmail.setText(hrPostVo.getSafeString("mailbox"));
                        tvShopExplain.setText(storeVo.getSafeString("storeIntroduce"));
                        storeId = storeVo.getInt("storeId");
                        double storeLatitude=storeVo.getDouble("lat");
                        double storeLongitude=storeVo.getDouble("lng");

                        LatLng latLng = new LatLng(storeLatitude,storeLongitude);;
                        // latLng = new LatLng(20, 115);
                        TencentMap map = mapView.getMap();
                        //设置卫星底图
                        // map.setSatelliteEnabled(true);
                        //设置地图中心点
                        map.setCenter(latLng);
                        //设置缩放级别
                        map.setZoom(19);

                        Bitmap bmMarker = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_map_marker);
                        bmMarker = BitmapUtil.scaleBitmap(bmMarker, 0.7f);

                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(latLng)
                                // .title("上海")
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromBitmap(bmMarker))
                                .draggable(false));

                        EasyJSONArray storeServiceStaffVoList = storeVo.getSafeArray("storeServiceStaffVoList");
                        if (!Util.isJsonArrayEmpty(storeServiceStaffVoList)) {
                            if (storeServiceStaffVoList.length() == 1) {
                                storeHr=new FriendInfo();
                                storeHr.avatarUrl = storeServiceStaffVoList.getObject(0).getSafeString("avatar");
                                storeHr.nickname = storeServiceStaffVoList.getObject(0).getSafeString("nickName");
                                storeHr.memberName = storeServiceStaffVoList.getObject(0).getSafeString("memberName");
                                storeHr.role=ChatUtil.ROLE_MEMBER;
                            }

                        }
                        SLog.info("storeServiceStaffVoList[%s]",storeServiceStaffVoList.toString());
                    }

                    int postNumbers = hrPostVo.getInt("postNumbers");//   招聘人數

                    int isAccommodation =hrPostVo.getInt("isAccommodation");//住宿  1-是 0-否
                    String experience =hrPostVo.getSafeString("experience");//   工作經驗
                    String academicDiplomas=hrPostVo.getSafeString("academicDiplomas");//  學歷要求


                    updateFollowState();
                    if (isDeliver == 1) {
                        tvDeliver.setText("已投遞");
                    }
                    tvPositionTitle.setText(postTitle);
                    tvPositionCategory.setText(postType);
                    tvArea.setText(postArea);
                    tvSalary.setText(String.format("%s:%s",salaryType,salaryRange));
                    tvPositionDesc.setText(postDescription);
                    if (StringUtil.isEmpty(mailbox)) {
                        tvEmail.setText(getString(R.string.text_not_set));
                    }
                    tvEmail.setText(mailbox);
                    tvPostNumber.setText(String.format("%d人",postNumbers));
                    tvIsAccommodation.setText((isAccommodation==1)?"是":"否");
                    tvExperience.setText(experience);
                    tvAcademicDiplomas.setText(academicDiplomas);
                    if (!StringUtil.isEmpty(postExplain)) {
                        tvWelfare.setText(postExplain);
                    }

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

            }
        });
    }

    private void updateFollowState() {
        if (isFavor == 1) {
            imgFollow.setImageResource(R.drawable.icon_store_favorite_red);
            tvFollow.setText("已關注");
        } else {
            imgFollow.setImageResource(R.drawable.icon_job_follow);
            tvFollow.setText("關注");
        }
    }

    @Override
    public void onClick(View view) {
        int id =view.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.rv_store_title:
                Util.startFragment(ShopMainFragment.newInstance(storeId));
                break;
            case R.id.btn_follow:
                if (isFavor == 1) {
                    ToastUtil.success(_mActivity,"已關注");
                    updateFollowState();
                }else{
                    followJob();
                }
                break;
            case R.id.btn_want_chat:
                if (storeId >0) {
                    showStoreCustomerService() ;
                }else{
                    ToastUtil.success(_mActivity,"未設置Hr");
                }
                break;
            case R.id.btn_apply:
                applyJob();
                break;
            default:
                break;
        }
    }
    public void showStoreCustomerService() {
        if (storeHr != null) {
            EMConversation conversation = ChatUtil.getConversation(storeHr.memberName, storeHr.nickname, storeHr.avatarUrl, storeHr.role);
            Util.startFragment(ChatFragment.newInstance(conversation, storeHr));
        } else {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new StoreCustomerServicePopup(_mActivity, storeId,null))
                    .show();
        }

    }
    private void applyJob() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }

        int postId = data.postId;

        EasyJSONObject params = EasyJSONObject.generate("token", token, "postId", postId);

        SLog.info(" params[%s],isDeliver[%s]", params,isDeliver);
        Api.postUI(Api.PATH_STORE_HR_APPLY, params, new UICallback() {
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
                    ToastUtil.success(_mActivity,"投遞成功");

                    isDeliver=1;
                    tvDeliver.setText("已投遞");
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void followJob() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }

        int postId = data.postId;

        EasyJSONObject params = EasyJSONObject.generate("token", token, "postId", postId);


        String path = Api.PATH_FOLLOW_JOB;

        SLog.info("path[%s], params[%s],isfavor[%d]", path, params,isFavor);
        Api.postUI(path, params, new UICallback() {
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

                    isFavor=1;
                    updateFollowState();
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }

    public void setData(WantedPostItem data) {
        this.data = data;
        SLog.info("data[%d]", data.postId);
//        备注：不需要加member
//        app/store/hr/apply  POST
//        參數：
//        token  登录token
//        postId   職位id
    }
}
