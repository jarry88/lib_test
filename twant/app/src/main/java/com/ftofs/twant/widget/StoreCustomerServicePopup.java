package com.ftofs.twant.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreServiceStaffListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.orm.ImNameMap;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.hyphenate.chat.EMConversation;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class StoreCustomerServicePopup extends BottomPopupView implements View.OnClickListener {
    private final GoodsInfo goodsInfo;
    Context context;

    TextView tvPopupTitle;

    List<CustomerServiceStaff> staffList = new ArrayList<>();
    String storeAvatar;
    String storeName;

    int storeId;
    StoreServiceStaffListAdapter adapter;
    public  StoreCustomerServicePopup(@NonNull Context context, int storeId, GoodsInfo goodsInfo) {
        super(context);

        this.context = context;
        this.storeId = storeId;
        this.goodsInfo = goodsInfo;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.store_customer_service_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        tvPopupTitle = findViewById(R.id.tv_popup_title);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvStaffList = findViewById(R.id.rv_staff_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvStaffList.setLayoutManager(layoutManager);
        adapter = new StoreServiceStaffListAdapter(R.layout.store_service_staff_list_item, staffList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CustomerServiceStaff staff = staffList.get(position);
                pullChat(staff);
            }
        });
        rvStaffList.setAdapter(adapter);
        dismiss();
        loadData();
    }

    private void pullChat(CustomerServiceStaff staff) {
        String memberName = staff.memberName;
        String imName = staff.imName;
        ImNameMap.saveMap(imName, memberName, storeId);

        dismiss();

        FriendInfo friendInfo = new FriendInfo();
        friendInfo.memberName = memberName;
        friendInfo.nickname = staff.staffName;
        friendInfo.avatarUrl = staff.avatar;
        friendInfo.role = ChatUtil.ROLE_CS_AVAILABLE;
        friendInfo.storeId = storeId;
        if (goodsInfo != null) {
            friendInfo.goodsInfo = goodsInfo;
            friendInfo.goodsInfo.showSendBtn = true;
        }
        FriendInfo.upsertFriendInfo(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE,1,"",staff.storeId);
        if (StringUtil.isEmpty(imName)) {
            imName = memberName;
        }
        if (StringUtil.isEmpty(imName)) {
            ToastUtil.success(getContext(),"當前客服狀態異常，無法會話");
            return;
        }
        EMConversation conversation = ChatUtil.getConversation(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE);
        Util.startFragment(ChatFragment.newInstance(conversation, friendInfo));
    }


    private void loadData() {
        final BasePopupView loadingPopup = Util.createLoadingPopup(getContext()).show();

        String path = Api.PATH_STORE_CUSTOMER_SERVICE + "/" + storeId;
        SLog.info("path[%s]", path);

        Api.getUI(path, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(path, "", "", e.getMessage());
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();

                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(context, responseObj)) {
                        LogUtil.uploadAppLog(path, "", responseStr, "");
                        return;
                    }

                    EasyJSONArray serviceStaffList = responseObj.getSafeArray("datas.serviceStaffList");
                    for (Object object : serviceStaffList) {
                        EasyJSONObject serviceStaff = (EasyJSONObject) object;

                        CustomerServiceStaff staff = new CustomerServiceStaff();
                        Util.packStaffInfo(staff, serviceStaff);

                        staffList.add(staff);
                    }
                    if (staffList.size() == 1) {
                        pullChat(staffList.get(0));
                        return;
                    } else {
                        show();
                    }

                    adapter.setNewData(staffList);
                    tvPopupTitle.setText(context.getString(R.string.text_store_customer_service) + "(" + staffList.size() + "人)");
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dismiss) {
            dismiss();
        }
    }
}
