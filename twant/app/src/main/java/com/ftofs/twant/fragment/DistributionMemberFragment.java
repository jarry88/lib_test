package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.lib_net.model.SellerGoodsItem;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.DistributionMemberAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.DistributionMember;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.gzp.lib_common.utils.SLog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class DistributionMemberFragment extends NestedScrollingFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    int requestType;

    /*
    查询类型
     */
    public static final int REQUEST_TYPE_ALL = 0; // 查询所有成员
    public static final int REQUEST_TYPE_FIRST_LEVEL = 1; // 查询1级成员
    public static final int REQUEST_TYPE_SECOND_LEVEL = 2; // 查询2级成员

    DistributionMemberAdapter adapter;
    List<DistributionMember> distributionMemberList = new ArrayList<>();

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    public static DistributionMemberFragment newInstance(int requestType) {
        DistributionMemberFragment fragment = new DistributionMemberFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.requestType = requestType;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distribution_member, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvList = view.findViewById(R.id.rv_list);
        SLog.info("rvList[%s]", rvList);
        rvList.setNestedScrollingEnabled(NestedScrollingEnabled);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new DistributionMemberAdapter(R.layout.distribution_member_item, distributionMemberList);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);

        rvList.setAdapter(adapter);

        loadData(currPage + 1);
    }


    private void loadData(int page) {
        String url = Api.PATH_MEMBER_MARKETING_SUB;
        EasyJSONObject params = EasyJSONObject.generate(
                "searchType", requestType,
                "page", page
        );
        SLog.info("url[%s], params[%s]", url, params.toString());
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
                adapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        adapter.loadMoreFail();
                        return;
                    }

                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        adapter.loadMoreEnd();
                        adapter.setEnableLoadMore(false);
                    }

                    if (page == 1) {
                        distributionMemberList.clear();
                    }
                    EasyJSONArray goodsList = responseObj.getArray("datas.subMemberList");
                    for (Object object : goodsList) {
                        distributionMemberList.add(new DistributionMember());
                    }
                    adapter.loadMoreComplete();
                    adapter.setNewData(distributionMemberList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 上滑加載更多
     */
    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadData(currPage + 1);
    }

    @Override
    public void onClick(View v) {

    }
}
