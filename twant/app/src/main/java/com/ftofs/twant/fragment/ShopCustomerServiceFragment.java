package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CustomerServiceStaffListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.entity.CustomerServiceStaffPair;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.orm.ImNameMap;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.hyphenate.chat.EMConversation;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 商店客服Fragment
 * @author zwm
 */
public class ShopCustomerServiceFragment extends BaseFragment implements View.OnClickListener {
    int storeId;
    String storeFigureUrl;

    ImageView imgBackground;
    List<CustomerServiceStaff> customerServiceStaffList = new ArrayList<>();
    List<CustomerServiceStaffPair> customerServiceStaffPairList = new ArrayList<>();

    RecyclerView rvStaffList;
    CustomerServiceStaffListAdapter adapter;

    boolean isShown;  // 是否要顯示歡迎動畫

    boolean storeFigureShown = false;

    /**
     * 展示客服歡迎語動畫的順序, 從0開始(以隨機的順序顯示歡迎語動畫)
     */
    List<Integer> welcomeMessageAnimOrder = new ArrayList<>();
    /**
     * 展示完歡迎語動畫的數量
     */
    int animDoneCount;

    TextView tvFragmentTitle;

    public static ShopCustomerServiceFragment newInstance(int storeId, String storeFigureUrl) {
        Bundle args = new Bundle();

        ShopCustomerServiceFragment fragment = new ShopCustomerServiceFragment();
        fragment.setArguments(args);
        fragment.setStoreId(storeId);
        fragment.setStoreFigureUrl(storeFigureUrl);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_customer_service, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);

        imgBackground = view.findViewById(R.id.img_background);
        rvStaffList = view.findViewById(R.id.rv_staff_list);

        rvStaffList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new CustomerServiceStaffListAdapter(this, R.layout.store_customer_service_staff, customerServiceStaffPairList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                CustomerServiceStaffPair customerServiceStaffPair = customerServiceStaffPairList.get(position);

                if (id == R.id.btn_init_cs_left || id == R.id.btn_init_cs_right) {
                    CustomerServiceStaff staff;

                    if (id == R.id.btn_init_cs_left) {
                        staff = customerServiceStaffPair.left;
                    } else {
                        staff = customerServiceStaffPair.right;
                    }

                    if (staff == null) {
                        return;
                    }

                    String memberName = staff.memberName;
                    String imName = staff.imName;
                    ImNameMap.saveMap(imName, memberName, storeId);
                    SLog.info("memberName[%s], imName[%s]", memberName, imName);

                    FriendInfo.upsertFriendInfo(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE);
                    if (StringUtil.isEmpty(imName)) {
                        imName = memberName;
                    }
                    if (StringUtil.isEmpty(imName)) {
                        ToastUtil.success(getContext(),"當前客服狀態異常，無法會話");
                        return;
                    }
                    EMConversation conversation = ChatUtil.getConversation(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE);

                    FriendInfo friendInfo = new FriendInfo();
                    friendInfo.memberName = staff.memberName;
                    friendInfo.nickname = staff.staffName;
                    friendInfo.avatarUrl = staff.avatar;
                    friendInfo.role = ChatUtil.ROLE_CS_AVAILABLE;
                    Util.startFragment(ChatFragment.newInstance(conversation, friendInfo));
                }
            }
        });
        rvStaffList.setAdapter(adapter);

        loadStaffData();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();

        return true;
    }

    private void loadStaffData() {
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        try {
            // 獲取商店首頁信息
            String path = Api.PATH_STORE_CUSTOMER_SERVICE + "/" + storeId;
            EasyJSONObject params = EasyJSONObject.generate();

            SLog.info("path[%s], params[%s]", path, params.toString());
            Api.getUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();

                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        String storeFigureImage = responseObj.getSafeString("datas.storeVo.storeFigureImage");
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(storeFigureImage)).centerCrop().into(imgBackground);

                        showStoreFigure();


                        // 處理售前
                        if (responseObj.exists("datas.preServiceList")) {
                            EasyJSONArray preServiceList = responseObj.getSafeArray("datas.preServiceList");

                            for (Object object : preServiceList) {
                                EasyJSONObject storeServiceStaffVo = (EasyJSONObject) object;
                                CustomerServiceStaff staff = new CustomerServiceStaff();
                                Util.packStaffInfo(staff, storeServiceStaffVo);

                                customerServiceStaffList.add(staff);
                            }
                        }

                        // 處理售後
                        if (responseObj.exists("datas.afterServiceList")) {
                            EasyJSONArray afterServiceList = responseObj.getSafeArray("datas.afterServiceList");

                            for (Object object : afterServiceList) {
                                EasyJSONObject storeServiceStaffVo = (EasyJSONObject) object;
                                CustomerServiceStaff staff = new CustomerServiceStaff();
                                Util.packStaffInfo(staff, storeServiceStaffVo);

                                customerServiceStaffList.add(staff);
                            }
                        }

                        // 處理一對對的數據
                        CustomerServiceStaffPair pair = null;
                        for (CustomerServiceStaff customerServiceStaff : customerServiceStaffList) {
                            if (pair == null) {
                                pair = new CustomerServiceStaffPair();
                                pair.left = customerServiceStaff;
                            } else {
                                pair.right = customerServiceStaff;
                                customerServiceStaffPairList.add(pair);

                                pair = null;
                            }
                        }

                        if (pair != null) {  // 處理個數為奇數的情況
                            customerServiceStaffPairList.add(pair);
                            pair = null;
                        }

                        SLog.info("customerServiceStaffPairList.size[%d]", customerServiceStaffPairList.size());
                        adapter.setNewData(customerServiceStaffPairList);

                        for (int i = 0; i < customerServiceStaffList.size(); i++) {
                            welcomeMessageAnimOrder.add(i);
                        }
                        Collections.shuffle(welcomeMessageAnimOrder); // 以隨機的順序顯示歡迎語動畫

                        tvFragmentTitle.setText(getString(R.string.text_customer_service) + "(" + customerServiceStaffList.size() + ")");
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();


        showStoreFigure();

        isShown = true;

        rvStaffList.postDelayed(new Runnable() {
            @Override
            public void run() {
                startWelcomeMessageAnim();
            }
        }, 1000);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();

        isShown = false;
    }

    /**
     * 當前歡迎語動畫播放完成
     */
    public void onWelcomeMessageAnimationEnd() {
        if (animDoneCount >= welcomeMessageAnimOrder.size()) {
            return;
        }
        int position = welcomeMessageAnimOrder.get(animDoneCount);
        int pairPosition = position / 2;
        CustomerServiceStaffPair pair = customerServiceStaffPairList.get(pairPosition);

        if (position % 2 == 0) {
            pair.left.showWelcomeMessageAnimation = false;
        } else {
            pair.right.showWelcomeMessageAnimation = false;
        }

        animDoneCount++;
        // 開始下一個歡迎語動畫
        startWelcomeMessageAnim();
    }

    /**
     * 開始歡迎語動畫
     */
    private void startWelcomeMessageAnim() {
        if (animDoneCount < welcomeMessageAnimOrder.size()) {
            int position = welcomeMessageAnimOrder.get(animDoneCount);
            int pairPosition = position / 2;
            CustomerServiceStaffPair pair = customerServiceStaffPairList.get(pairPosition);

            if (position % 2 == 0) {
                pair.left.showWelcomeMessageAnimation = true;
            } else {
                pair.right.showWelcomeMessageAnimation = true;
            }

            adapter.notifyItemChanged(pairPosition);
        }
    }
    public void loadStoreFigure(String url){
        if(StringUtil.isEmpty(url)){
            Glide.with(_mActivity).load(R.drawable.store_figure_default).centerCrop().into(imgBackground);
        }else{
            Glide.with(_mActivity).load(url).centerCrop().into(imgBackground);
        }
    }

    private void showStoreFigure() {
        if (storeFigureShown) {
            return;
        }

        if(!StringUtil.isEmpty(storeFigureUrl)){
            storeFigureShown = true;
            if(storeFigureUrl.equals("no")){
                storeFigureUrl ="";
            }
            loadStoreFigure(storeFigureUrl);
        }

    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setStoreFigureUrl(String storeFigureUrl) {
        this.storeFigureUrl = storeFigureUrl;
    }
}


