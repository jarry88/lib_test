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
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.MyBargainListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.MyBargainListItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 【發起的砍價】和【參與的砍價】頁面
 * @author zwm
 */
public class MyBargainListFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    int dataType;
    public static final int DATA_TYPE_INITIATE = 1;  // 發起的砍價
    public static final int DATA_TYPE_PARTICIPATE = 2;  // 參與的砍價

    TextView tvFragmentTitle;

    MyBargainListAdapter adapter;
    RecyclerView rvList;
    List<MyBargainListItem> myBargainItemList = new ArrayList<>();

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    public static MyBargainListFragment newInstance(int dataType) {
        Bundle args = new Bundle();

        MyBargainListFragment fragment = new MyBargainListFragment();
        fragment.setArguments(args);
        fragment.dataType = dataType;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bargain_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        if (dataType == DATA_TYPE_INITIATE) {
            tvFragmentTitle.setText("發起的砍價");
        } else if (dataType == DATA_TYPE_PARTICIPATE) {
            tvFragmentTitle.setText("參與的砍價");
        }

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new MyBargainListAdapter(_mActivity, R.layout.my_bargain_list_item, myBargainItemList);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyBargainListItem myBargainListItem = myBargainItemList.get(position);
                Util.startFragment(GoodsDetailFragment.newInstance(myBargainListItem.commonId, myBargainListItem.goodsId, myBargainListItem.bargainId));
            }
        });

        rvList.setAdapter(adapter);

        loadData(currPage + 1);
    }


    private void loadData(int page) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "page", page);

        SLog.info("params[%s]", params);

        String url;
        if (dataType == DATA_TYPE_INITIATE) {
            url = Api.PATH_BARGAIN_OWNER;
        } else {
            url = Api.PATH_BARGAIN_HELP;
        }

        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
                adapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                try {
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        adapter.loadMoreFail();
                        return;
                    }

                    if (page == 1) {
                        // 設置空頁面
                        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.layout_placeholder_no_data, null, false);
                        // 設置空頁面的提示語
                        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
                        tvEmptyHint.setText(R.string.no_data_hint);
                        adapter.setEmptyView(emptyView);
                    }

                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        adapter.loadMoreEnd();
                        adapter.setEnableLoadMore(false);
                    }

                    EasyJSONArray bargainGoodsOpenLogVoList = responseObj.getSafeArray("datas.bargainGoodsOpenLogVoList");
                    for (Object object : bargainGoodsOpenLogVoList) {
                        EasyJSONObject bargainGoodsOpenLogVo = (EasyJSONObject) object;
                        MyBargainListItem item = new MyBargainListItem();

                        item.commonId = bargainGoodsOpenLogVo.getInt("commonId");
                        item.goodsId = bargainGoodsOpenLogVo.getInt("goodsId");
                        item.bargainId = bargainGoodsOpenLogVo.getInt("bargainId");
                        item.openId = bargainGoodsOpenLogVo.getInt("openId");
                        item.imageSrc = bargainGoodsOpenLogVo.getSafeString("imageSrc");
                        item.goodsName = bargainGoodsOpenLogVo.getSafeString("goodsName");
                        item.goodsFullSpecs = bargainGoodsOpenLogVo.getSafeString("goodsFullSpecs");
                        item.startTime = bargainGoodsOpenLogVo.getSafeString("startTime");
                        item.endTime = bargainGoodsOpenLogVo.getSafeString("endTime");
                        item.openPrice = bargainGoodsOpenLogVo.getDouble("openPrice"); // 當前價
                        item.bargainPrice = bargainGoodsOpenLogVo.getDouble("bargainPrice");  // 已砍掉的價錢
                        item.bargainTimes = bargainGoodsOpenLogVo.getInt("bargainTimes"); // 幫砍次數
                        item.bottomPrice = bargainGoodsOpenLogVo.getDouble("bottomPrice");

                        myBargainItemList.add(item);
                    }

                    adapter.loadMoreComplete();
                    adapter.setNewData(myBargainItemList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
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


