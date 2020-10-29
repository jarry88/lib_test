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
import com.ftofs.twant.adapter.DistributionOrderAdapter;
import com.ftofs.twant.adapter.DistributionWithdrawRecordAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.DistributionMember;
import com.ftofs.twant.entity.DistributionOrderItem;
import com.ftofs.twant.entity.DistributionWithdrawRecord;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.widget.lmz.LmzNestedScrollingBaseFragment;
import com.gzp.lib_common.utils.SLog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class DistributionWithdrawRecordFragment extends LmzNestedScrollingBaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    int requestType;

    /*
    查詢類型,-1 全部 0待審核 2提現失敗 3提現成功
     */
    public static final int REQUEST_TYPE_ALL = -1;
    public static final int REQUEST_TYPE_UNPROCESSED = 0;
    public static final int REQUEST_TYPE_SUCCESS = 3;
    public static final int REQUEST_TYPE_FAIL = 2;

    DistributionWithdrawRecordAdapter adapter;
    List<DistributionWithdrawRecord> distributionWithdrawRecordList = new ArrayList<>();

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    public static DistributionWithdrawRecordFragment newInstance(int requestType) {
        DistributionWithdrawRecordFragment fragment = new DistributionWithdrawRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.requestType = requestType;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distribution_order, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvList = view.findViewById(R.id.rv_list);
        SLog.info("rvList[%s]", rvList);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new DistributionWithdrawRecordAdapter(_mActivity, distributionWithdrawRecordList);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DistributionWithdrawRecord record = distributionWithdrawRecordList.get(position);
                record.expanded = !record.expanded;
                adapter.notifyItemChanged(position);
            }
        });

        rvList.setAdapter(adapter);

        loadData(currPage + 1);
    }


    private void loadData(int page) {
        String url = Api.PATH_DISTRIBUTION_WITHDRAW_RECORD;
        EasyJSONObject params = EasyJSONObject.generate(
                "page", page
        );

        try {
            // 不傳searchType表示查詢全部
            if (requestType != REQUEST_TYPE_ALL) {
                params.set("searchType", requestType);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }


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
                        distributionWithdrawRecordList.clear();
                    }
                    EasyJSONArray recordList = responseObj.getArray("datas.logs");
                    for (Object object : recordList) {
                        DistributionWithdrawRecord recordItem = (DistributionWithdrawRecord) EasyJSONBase.jsonDecode(DistributionWithdrawRecord.class, object.toString());
                        recordItem.itemType = Constant.ITEM_TYPE_NORMAL;
                        distributionWithdrawRecordList.add(recordItem);
                    }

                    if (page == 1 && !hasMore && distributionWithdrawRecordList.size() == 0) {
                        distributionWithdrawRecordList.add(new DistributionWithdrawRecord(Constant.ITEM_TYPE_NO_DATA));
                    }

                    adapter.loadMoreComplete();
                    adapter.setNewData(distributionWithdrawRecordList);
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
