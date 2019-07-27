package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CustomerServiceStaffListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.hyphenate.chat.EMConversation;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;


/**
 * 店鋪客服Fragment
 * @author zwm
 */
public class ShopCustomerServiceFragment extends BaseFragment implements View.OnClickListener {
    ShopMainFragment parentFragment;

    ImageView imgBackground;
    List<CustomerServiceStaff> customerServiceStaffList = new ArrayList<>();

    RecyclerView rvStaffList;
    CustomerServiceStaffListAdapter adapter;

    boolean isShown;

    public static ShopCustomerServiceFragment newInstance() {
        Bundle args = new Bundle();

        ShopCustomerServiceFragment fragment = new ShopCustomerServiceFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_customer_service, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();

        imgBackground = view.findViewById(R.id.img_background);
        rvStaffList = view.findViewById(R.id.rv_staff_list);
        GridLayoutManager layoutManager = new GridLayoutManager(_mActivity, 2, LinearLayoutManager.VERTICAL, false);
        rvStaffList.setLayoutManager(layoutManager);
        adapter = new CustomerServiceStaffListAdapter(R.layout.store_customer_service_staff, customerServiceStaffList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CustomerServiceStaff staff = customerServiceStaffList.get(position);

                String memberName = staff.memberName;
                String imName = staff.imName;
                SLog.info("memberName[%s], imName[%s]", memberName, imName);

                EMConversation conversation = ChatUtil.getConversation(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE);
                Util.startFragment(ChatFragment.newInstance(conversation));
            }
        });
        rvStaffList.setAdapter(adapter);

        loadStaffData();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        ((SupportFragment) getParentFragment()).pop();
        return true;
    }

    private void loadStaffData() {
        final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                .asLoading(getString(R.string.text_loading))
                .show();

        try {
            // 獲取店鋪首頁信息
            String path = Api.PATH_SHOP_HOME + "/" + parentFragment.getShopId();
            String token = User.getToken();
            EasyJSONObject params = EasyJSONObject.generate();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            SLog.info("path[%s], params[%s]", path, params.toString());
            Api.postUI(path, params, new UICallback() {
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

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONObject storeInfo = responseObj.getObject("datas.storeInfo");
                        String storeFigureImage = Config.OSS_BASE_URL + "/" + storeInfo.getString("storeFigureImage");
                        Glide.with(_mActivity).load(storeFigureImage).centerCrop().into(imgBackground);

                        EasyJSONArray storeServiceStaffVoList = storeInfo.getArray("storeServiceStaffVoList");

                        for (Object object : storeServiceStaffVoList) {
                            EasyJSONObject storeServiceStaffVo = (EasyJSONObject) object;
                            CustomerServiceStaff staff = new CustomerServiceStaff();
                            Util.packStaffInfo(staff, storeServiceStaffVo);

                            customerServiceStaffList.add(staff);
                        }

                        if (isShown) {
                            parentFragment.setFragmentTitle(getString(R.string.text_customer_service) + "(" + customerServiceStaffList.size() + ")");
                        }

                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        isShown = true;
        parentFragment.setFragmentTitle(getString(R.string.text_customer_service) + "(" + customerServiceStaffList.size() + ")");
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();

        isShown = false;
    }
}
