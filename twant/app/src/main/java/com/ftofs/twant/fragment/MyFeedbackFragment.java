package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.FeedbackListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.FeedbackItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 我的反饋Fragment
 * @author zwm
 */
public class MyFeedbackFragment extends BaseFragment implements View.OnClickListener {
    List<FeedbackItem> feedbackItemList = new ArrayList<>();
    FeedbackListAdapter adapter;

    public static MyFeedbackFragment newInstance() {
        Bundle args = new Bundle();

        MyFeedbackFragment fragment = new MyFeedbackFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_feedback, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        RecyclerView rvFeedbackList = view.findViewById(R.id.rv_list);
        adapter = new FeedbackListAdapter(R.layout.feedback_list_item, feedbackItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

        // 設置空頁面
        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.no_result_empty_view, null, false);
        // 設置空頁面的提示語
        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
        tvEmptyHint.setText(R.string.no_data_hint);
        adapter.setEmptyView(emptyView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvFeedbackList.setLayoutManager(layoutManager);
        rvFeedbackList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        SLog.info("params[%s]", params.toString());
        Api.getUI(Api.PATH_FEEDBACK_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONArray suggestList = responseObj.getArray("datas.suggestList");
                    for (Object object : suggestList) {
                        EasyJSONObject feedback = (EasyJSONObject) object;
                        FeedbackItem feedbackItem = new FeedbackItem();
                        feedbackItem.suggestId = feedback.getInt("suggestId");
                        feedbackItem.suggestContent = feedback.getString("suggestContent");
                        feedbackItem.createTime = feedback.getLong("createTime");
                        EasyJSONArray imageList = feedback.getArray("imageList");
                        feedbackItem.imageList = new ArrayList<>();
                        for (Object object2 : imageList) {
                            EasyJSONObject image = (EasyJSONObject) object2;
                            feedbackItem.imageList.add(image.getString("imageUrl"));
                        }
                        feedbackItemList.add(feedbackItem);
                    }

                    adapter.setNewData(feedbackItemList);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
