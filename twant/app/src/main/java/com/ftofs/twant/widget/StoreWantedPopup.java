package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.WantedJobListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.WantedPostItem;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
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
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                Util.showLoginFragment();
                dismiss();
                return;
            }

            WantedPostItem item = wantedPostItemList.get(currentPosition);
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
                    LogUtil.uploadAppLog(path, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(context, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(context, responseObj)) {
                            LogUtil.uploadAppLog(path, params.toString(), responseStr, "");
                            return;
                        }

                        item.isFavor = 1 - item.isFavor;
                        updateFollowStatus(item.isFavor);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        }
    }


    private void updateFollowStatus(int follow) {
        if (follow == 1) {
            btnFollow.setText(context.getString(R.string.text_followed));
            btnFollow.setBackgroundResource(R.drawable.red_button_followed);
        } else {
            btnFollow.setText(context.getString(R.string.text_follow));
            btnFollow.setBackgroundResource(R.drawable.blue_button_follow);
        }
    }
}

