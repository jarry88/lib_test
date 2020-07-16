package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GroupGoodsListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.GroupGoods;
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


public class GroupInfoListFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    RecyclerView rvList;
    GroupGoodsListAdapter adapter;
    List<GroupGoods> groupGoodsList = new ArrayList<>();

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;
    boolean emptyViewSet = false; // 是否已經設置空白頁面

    /*
    sort參數：
    價格升序price_asc 降序price_desc 銷量升序sale_asc 降序sale_desc
     */
    public static final String PRICE_ASC = "price_asc";
    public static final String PRICE_DESC = "price_desc";
    public static final String SALE_ASC = "sale_asc";
    public static final String SALE_DESC = "sale_desc";

    private String sortParam;

    // 排序標準 0 -- 未確定(【綜合】排序時用到)  1 -- 升序  2 -- 降序
    public static final int SORT_CRITERIA_UNKNOWN = 0;
    public static final int SORT_CRITERIA_ASC = 1;
    public static final int SORT_CRITERIA_DESC = 2;
    private int sortCriteria = SORT_CRITERIA_UNKNOWN; // 當前排序標準



    public static final int TAB_INDEX_GENERAL = 0;
    public static final int TAB_INDEX_SALE = 1;
    public static final int TAB_INDEX_PRICE = 2;
    public int currTabIndex = TAB_INDEX_GENERAL;  // 當前選中哪個Tab

    TextView tvSortGeneral;
    TextView tvSortSale;
    TextView tvSortPrice;
    ImageView icSortSale;
    ImageView icSortPrice;

    int twBlack;
    int twBlue;

    View btnGotoTop;

    public static GroupInfoListFragment newInstance() {
        Bundle args = new Bundle();

        GroupInfoListFragment fragment = new GroupInfoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_info_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        btnGotoTop = view.findViewById(R.id.btn_goto_top);
        btnGotoTop.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_sort_general, this);
        Util.setOnClickListener(view, R.id.btn_sort_sale, this);
        Util.setOnClickListener(view, R.id.btn_sort_price, this);

        twBlack = getResources().getColor(R.color.tw_black, null);
        twBlue = getResources().getColor(R.color.tw_blue, null);

        tvSortGeneral = view.findViewById(R.id.tv_sort_general);
        tvSortSale = view.findViewById(R.id.tv_sort_sale);
        tvSortPrice = view.findViewById(R.id.tv_sort_price);
        icSortSale = view.findViewById(R.id.ic_sort_sale);
        icSortPrice = view.findViewById(R.id.ic_sort_price);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new GroupGoodsListAdapter(_mActivity, R.layout.group_goods_list_item, groupGoodsList);

        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupGoods groupGoods = groupGoodsList.get(position);
                Util.startFragment(GoodsDetailFragment.newInstance(groupGoods.commonId, groupGoods.goodsId));
            }
        });

        rvList.setAdapter(adapter);
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideFloatButton();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showFloatButton();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        loadData(currPage + 1);
    }

    private void hideFloatButton() {
        btnGotoTop.setTranslationX(Util.dip2px(_mActivity, 30.5f));
    }

    private void showFloatButton() {
        btnGotoTop.setTranslationX(Util.dip2px(_mActivity, 0));
    }

    private void reloadData() {
        currPage = 0;
        loadData(currPage + 1);
    }

    private void loadData(int page) {
        EasyJSONObject params = EasyJSONObject.generate("page", page);

        if (!StringUtil.isEmpty(sortParam)) {
            try {
                params.set("sort", sortParam);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }

        String url = Api.PATH_GROUP_GOODS_LIST;
        SLog.info("url[%s], params[%s]", url, params);

        Api.getUI(url, params, new UICallback() {
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

                    if (emptyViewSet) {
                        // 設置空頁面
                        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.layout_placeholder_no_data, null, false);
                        // 設置空頁面的提示語
                        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
                        tvEmptyHint.setText(R.string.no_data_hint);
                        adapter.setEmptyView(emptyView);

                        emptyViewSet = true;
                    }

                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        adapter.loadMoreEnd();
                        adapter.setEnableLoadMore(false);
                    }

                    if (page == 1) {
                        groupGoodsList.clear();
                    }
                    EasyJSONArray groupList = responseObj.getArray("datas.groupList");
                    for (Object object : groupList) {
                        EasyJSONObject group = (EasyJSONObject) object;

                        GroupGoods groupGoods = new GroupGoods();
                        groupGoods.commonId = group.getInt("commonId");
                        if (group.exists("goodsId")) {
                            groupGoods.goodsId = group.getInt("goodsId");
                        }
                        groupGoods.imageName = group.getSafeString("imageName");
                        groupGoods.goodsName = group.getSafeString("goodsName");
                        groupGoods.jingle = group.getSafeString("jingle");
                        groupGoods.groupPrice = group.getDouble("groupPrice");
                        groupGoods.goodsPrice = group.getDouble("goodsPrice");
                        groupGoods.joinedNum = group.getInt("joinedNum");
                        groupGoods.groupRequireNum = group.getInt("groupRequireNum");

                        groupGoodsList.add(groupGoods);
                    }

                    adapter.loadMoreComplete();
                    adapter.setNewData(groupGoodsList);
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
        } else if (id == R.id.btn_goto_top) {
            rvList.scrollToPosition(0);
        } else if (id == R.id.btn_sort_general) { // 綜合
            if (currTabIndex == TAB_INDEX_GENERAL) {
                return;
            }

            sortParam = null;
            sortCriteria = SORT_CRITERIA_UNKNOWN;

            tvSortGeneral.setTextColor(twBlue);
            tvSortSale.setTextColor(twBlack);
            tvSortPrice.setTextColor(twBlack);
            icSortSale.setImageResource(R.drawable.ic_sort_off);
            icSortPrice.setImageResource(R.drawable.ic_sort_off);

            currTabIndex = TAB_INDEX_GENERAL;
            reloadData();
        } else if (id == R.id.btn_sort_sale) { // 按銷量排序
            if (currTabIndex == TAB_INDEX_SALE) {  // 重覆點擊，切換排序標準
                if (sortCriteria == SORT_CRITERIA_ASC) {
                    sortParam = SALE_DESC;
                    sortCriteria = SORT_CRITERIA_DESC;
                    icSortSale.setImageResource(R.drawable.ic_sort_desc);
                } else {
                    sortParam = SALE_ASC;
                    sortCriteria = SORT_CRITERIA_ASC;
                    icSortSale.setImageResource(R.drawable.ic_sort_asc);
                }
            } else { // 新點擊
                sortParam = SALE_DESC;
                sortCriteria = SORT_CRITERIA_DESC;

                tvSortGeneral.setTextColor(twBlack);
                tvSortSale.setTextColor(twBlue);
                tvSortPrice.setTextColor(twBlack);
                icSortSale.setImageResource(R.drawable.ic_sort_desc);
                icSortPrice.setImageResource(R.drawable.ic_sort_off);
            }

            currTabIndex = TAB_INDEX_SALE;
            reloadData();
        } else if (id == R.id.btn_sort_price) { // 按價格排序
            if (currTabIndex == TAB_INDEX_PRICE) {  // 重覆點擊，切換排序標準
                if (sortCriteria == SORT_CRITERIA_ASC) {
                    sortParam = PRICE_DESC;
                    sortCriteria = SORT_CRITERIA_DESC;
                    icSortPrice.setImageResource(R.drawable.ic_sort_desc);
                } else {
                    sortParam = PRICE_ASC;
                    sortCriteria = SORT_CRITERIA_ASC;
                    icSortPrice.setImageResource(R.drawable.ic_sort_asc);
                }
            } else { // 新點擊
                sortParam = PRICE_ASC;
                sortCriteria = SORT_CRITERIA_ASC;

                tvSortGeneral.setTextColor(twBlack);
                tvSortSale.setTextColor(twBlack);
                tvSortPrice.setTextColor(twBlue);
                icSortSale.setImageResource(R.drawable.ic_sort_off);
                icSortPrice.setImageResource(R.drawable.ic_sort_asc);
            }

            currTabIndex = TAB_INDEX_PRICE;
            reloadData();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
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
