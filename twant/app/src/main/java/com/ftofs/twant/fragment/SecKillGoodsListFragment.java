package com.ftofs.twant.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SecKillGoodsListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.SecKillGoodsListItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BackgroundDrawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 秒殺商品列表Fragment
 * @author zwm
 */
public class SecKillGoodsListFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    int scheduleId;

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    SecKillGoodsListAdapter adapter;
    List<SecKillGoodsListItem> goodsItemList = new ArrayList<>();

    public static SecKillGoodsListFragment newInstance(int scheduleId) {
        Bundle args = new Bundle();

        SecKillGoodsListFragment fragment = new SecKillGoodsListFragment();
        fragment.setArguments(args);
        fragment.scheduleId = scheduleId;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sec_kill_goods_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SecKillGoodsListAdapter(_mActivity, R.layout.sec_kill_list_normal_item, goodsItemList);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);
        rvList.setAdapter(adapter);
    }

    private void loadData(int page) {
        EasyJSONObject params = EasyJSONObject.generate(
                "scheduleId", scheduleId,
                "page", page
        );

        String url = Api.PATH_SEC_KILL_GOODS_LIST;
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
                adapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                    adapter.loadMoreFail();
                    return;
                }

                if (page == 1) {
                    goodsItemList.clear();
                }

                try {
                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        adapter.loadMoreEnd();
                        adapter.setEnableLoadMore(false);
                    }

                    adapter.loadMoreComplete();
                    adapter.setNewData(goodsItemList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadData(currPage + 1);
    }
}



