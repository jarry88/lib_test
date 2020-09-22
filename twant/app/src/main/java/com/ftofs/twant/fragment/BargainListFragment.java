package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.BargainListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.BargainItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.base.Jarbon;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 砍价商品列表
 * @author zwm
 */
public class BargainListFragment extends BaseFragment implements View.OnClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    RecyclerView rvList;

    BargainListAdapter adapter;
    List<BargainItem> bargainItemList = new ArrayList<>();

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    public static BargainListFragment newInstance() {
        Bundle args = new Bundle();

        BargainListFragment fragment = new BargainListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bargain_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new BargainListAdapter(_mActivity, bargainItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BargainItem bargainItem = bargainItemList.get(position);
                if (bargainItem.itemType == Constant.ITEM_TYPE_BANNER) {
                    return;
                }
                Util.startFragment(GoodsDetailFragment.newInstance(bargainItem.commonId, bargainItem.goodsId, bargainItem.bargainId));
            }
        });

        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);

        rvList.setAdapter(adapter);

        loadData(currPage + 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (adapter != null) {
            adapter.cancelAllTimers();
        }
    }

    private void loadData(int page) {
        EasyJSONObject params = EasyJSONObject.generate(
                "page", page);

        String url = Api.PATH_BARGAIN_LIST;
        SLog.info("url[%s], params[%s]", url, params);
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

                    if (page == 1) { // 如果是第1頁，添加banner圖
                        String bargainImageWap = responseObj.getSafeString("datas.bargainImageWap");
                        BargainItem bargainItem = new BargainItem(Constant.ITEM_TYPE_BANNER);
                        bargainItem.bannerUrl = bargainImageWap;

                        bargainItemList.add(bargainItem);
                    }

                    EasyJSONArray bargainGoodsVoList = responseObj.getSafeArray("datas.bargainGoodsVoList");
                    for (Object object : bargainGoodsVoList) {
                        EasyJSONObject bargainGoodsVo = (EasyJSONObject) object;
                        BargainItem bargainItem = new BargainItem(Constant.ITEM_TYPE_NORMAL);
                        bargainItem.commonId = bargainGoodsVo.getInt("commonId");
                        bargainItem.goodsId = bargainGoodsVo.getInt("goodsId");
                        bargainItem.bargainId = bargainGoodsVo.getInt("bargainId");
                        String startTimeStr = bargainGoodsVo.getSafeString("startTime");
                        bargainItem.startTime = Jarbon.parse(startTimeStr).getTimestampMillis();
                        bargainItem.bargainState = bargainGoodsVo.getInt("bargainState");
                        bargainItem.imageSrc = bargainGoodsVo.getSafeString("imageSrc");
                        bargainItem.goodsName = bargainGoodsVo.getSafeString("goodsName");
                        bargainItem.jingle = bargainGoodsVo.getSafeString("jingle");

                        EasyJSONArray bargainOpenList = bargainGoodsVo.getSafeArray("bargainOpenList");
                        for (Object object2 : bargainOpenList) {
                            EasyJSONObject bargainOpenItem = (EasyJSONObject) object2;
                            bargainItem.bargainOpenList.add(bargainOpenItem.getSafeString("avatar"));
                        }

                        bargainItem.bottomPrice = bargainGoodsVo.getDouble("bottomPrice");

                        bargainItemList.add(bargainItem);
                    }

                    adapter.loadMoreComplete();
                    adapter.setNewData(bargainItemList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
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
