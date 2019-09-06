package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreServiceStaffListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.orm.ImNameMap;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.hyphenate.chat.EMConversation;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class StoreCustomerServicePopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    TextView tvPopupTitle;

    List<CustomerServiceStaff> staffList = new ArrayList<>();

    int storeId;
    StoreServiceStaffListAdapter adapter;
    public StoreCustomerServicePopup(@NonNull Context context, int storeId) {
        super(context);

        this.context = context;
        this.storeId = storeId;
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
                String memberName = staff.memberName;
                String imName = staff.imName;
                ImNameMap.saveMap(imName, memberName, storeId);

                dismiss();

                FriendInfo friendInfo = new FriendInfo();
                friendInfo.memberName = memberName;
                friendInfo.nickname = staff.staffName;
                friendInfo.avatarUrl = staff.avatar;
                friendInfo.role = ChatUtil.ROLE_CS_AVAILABLE;
                FriendInfo.upsertFriendInfo(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE);
                EMConversation conversation = ChatUtil.getConversation(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE);
                Util.startFragment(ChatFragment.newInstance(conversation, friendInfo));
            }
        });
        rvStaffList.setAdapter(adapter);

        loadData();
    }


    private void loadData() {
        String path = Api.PATH_STORE_CUSTOMER_SERVICE + "/" + storeId;
        SLog.info("path[%s]", path);

        Api.getUI(path, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(context, responseObj)) {
                        return;
                    }

                    EasyJSONArray serviceStaffList = responseObj.getArray("datas.serviceStaffList");
                    for (Object object : serviceStaffList) {
                        EasyJSONObject serviceStaff = (EasyJSONObject) object;

                        CustomerServiceStaff staff = new CustomerServiceStaff();
                        Util.packStaffInfo(staff, serviceStaff);

                        staffList.add(staff);
                    }

                    adapter.setNewData(staffList);
                    tvPopupTitle.setText(context.getString(R.string.text_store_customer_service) + "(" + staffList.size() + ")");
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
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
