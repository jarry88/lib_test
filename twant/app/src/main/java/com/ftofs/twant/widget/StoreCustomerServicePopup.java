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
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;

public class StoreCustomerServicePopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    TextView tvPopupTitle;

    List<CustomerServiceStaff> staffList;

    StoreServiceStaffListAdapter adapter;
    public StoreCustomerServicePopup(@NonNull Context context, List<CustomerServiceStaff> staffList) {
        super(context);

        this.context = context;
        this.staffList = staffList;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.store_customer_service_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        tvPopupTitle = findViewById(R.id.tv_popup_title);
        tvPopupTitle.setText(context.getString(R.string.text_store_customer_service) + "(" + staffList.size() + ")");
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

                dismiss();

                Util.startFragment(ChatFragment.newInstance(memberName));
            }
        });
        rvStaffList.setAdapter(adapter);
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
