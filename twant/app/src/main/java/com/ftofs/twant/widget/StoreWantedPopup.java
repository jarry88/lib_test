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
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.WantedPostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

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

    TextView btnFollow;

    ScaledButton btnBack;
    RecyclerView rvWantedList;
    ScrollView svJobDetail;

    /**
     * 當前正在查看的職位的position
     */
    int currentPosition;

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

        btnFollow = findViewById(R.id.btn_follow);
        btnFollow.setOnClickListener(this);

        svJobDetail = findViewById(R.id.sv_job_detail);

        rvWantedList = findViewById(R.id.rv_wanted_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvWantedList.setLayoutManager(layoutManager);
        adapter = new WantedJobListAdapter(R.layout.wanted_job_list_item, wantedPostItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WantedPostItem item = wantedPostItemList.get(position);
                currentPosition = position;

                tvJobTitle.setText(String.format("%s | %s", item.postType, item.postTitle));
                tvSalaryRange.setText(item.salaryRange);
                tvSalaryUnit.setText(String.format("%s/%s", item.currency, item.salaryType));
                tvPostArea.setText(item.postArea);
                tvMailBox.setText(item.mailbox);
                tvJobDesc.setText(item.postDescription);
                updateFollowStatus(item.isFavor);

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
        } else if (id == R.id.btn_follow) {
            WantedPostItem item = wantedPostItemList.get(currentPosition);
            String token = User.getToken();
            int postId = item.postId;

            EasyJSONObject params = EasyJSONObject.generate("token", token, "postId", postId);


            String path;
            if (item.isFavor == 1) {
                path = Api.PATH_UNFOLLOW_JOB;
            } else {
                path = Api.PATH_FOLLOW_JOB;
            }

            SLog.info("path[%s], params[%s]", path, params);
            Api.postUI(path, params, new UICallback() {
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

                        item.isFavor = 1 - item.isFavor;
                        updateFollowStatus(item.isFavor);
                    } catch (Exception e) {

                    }
                }
            });
        }
    }


    private void updateFollowStatus(int follow) {
        if (follow == 1) {
            btnFollow.setText(context.getString(R.string.text_followed));
            btnFollow.setBackgroundResource(R.drawable.grey_button);
        } else {
            btnFollow.setText(context.getString(R.string.text_follow));
            btnFollow.setBackgroundResource(R.drawable.mini_red_button);
        }
    }
}

