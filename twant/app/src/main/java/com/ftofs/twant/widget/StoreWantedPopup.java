package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.WantedJobListAdapter;
import com.ftofs.twant.entity.WantedPostItem;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;

public class StoreWantedPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    List<WantedPostItem> wantedPostItemList;

    WantedJobListAdapter adapter;

    TextView tvJobTitle;
    TextView tvSalaryRange;
    TextView tvSalaryUnit;
    TextView tvPostArea;
    TextView tvMailBox;
    TextView tvJobDesc;

    ScaledButton btnBack;
    RecyclerView rvWantedList;
    ScrollView svJobDetail;

    public StoreWantedPopup(@NonNull Context context, List<WantedPostItem> wantedPostItemList) {
        super(context);

        this.context = context;
        this.wantedPostItemList = wantedPostItemList;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.store_wanted_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        tvJobTitle = findViewById(R.id.tv_job_title);
        tvSalaryRange = findViewById(R.id.tv_salary_range);
        tvSalaryUnit = findViewById(R.id.tv_salary_unit);
        tvPostArea = findViewById(R.id.tv_post_area);
        tvMailBox = findViewById(R.id.tv_mail_box);
        tvJobDesc = findViewById(R.id.tv_job_desc);

        svJobDetail = findViewById(R.id.sv_job_detail);

        rvWantedList = findViewById(R.id.rv_wanted_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvWantedList.setLayoutManager(layoutManager);
        adapter = new WantedJobListAdapter(R.layout.wanted_job_list_item, wantedPostItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WantedPostItem item = wantedPostItemList.get(position);

                tvJobTitle.setText(String.format("%s | %s", item.postType, item.postTitle));
                tvSalaryRange.setText(item.salaryRange);
                tvSalaryUnit.setText(String.format("%s/%s", item.currency, item.salaryType));
                tvPostArea.setText(item.postArea);
                tvMailBox.setText(item.mailbox);
                tvJobDesc.setText(item.postDescription);

                btnBack.setVisibility(VISIBLE);
                rvWantedList.setVisibility(GONE);
                svJobDetail.setVisibility(VISIBLE);
            }
        });
        rvWantedList.setAdapter(adapter);
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
        } else if (id == R.id.btn_back) {
            btnBack.setVisibility(GONE);
            rvWantedList.setVisibility(VISIBLE);
            svJobDetail.setVisibility(GONE);
        }
    }
}

