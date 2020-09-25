package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CrossBorderHomeAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.ChangeColorResult;
import com.ftofs.twant.entity.CrossBorderActivityGoods;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.entity.CrossBorderHomeItem;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.entity.CrossBorderNavPane;
import com.ftofs.twant.entity.CrossBorderShoppingZoneItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.entity.Store;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 跨城購主頁
 * @author zwm
 */
public class CrossBorderHomeFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    RecyclerView rvList;
    CrossBorderHomeAdapter adapter;
    List<CrossBorderHomeItem> crossBorderHomeItemList = new ArrayList<>();

    List<CrossBorderBannerItem> bannerItemList;
    int navItemCount;
    List<CrossBorderNavPane> navPaneList;
    List<CrossBorderShoppingZoneItem> shoppingZoneList;
    List<CrossBorderActivityGoods> bargainGoodsList;
    List<CrossBorderActivityGoods> groupGoodsList;
    List<Store> storeList;

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    LinearLayout llFloatButtonContainer;

    public static CrossBorderHomeFragment newInstance(List<CrossBorderBannerItem> bannerItemList,
                                                      int navItemCount,
                                                      List<CrossBorderNavPane> navPaneList,
                                                      List<CrossBorderShoppingZoneItem> shoppingZoneList,
                                                      List<CrossBorderActivityGoods> bargainGoodsList,
                                                      List<CrossBorderActivityGoods> groupGoodsList,
                                                      List<Store> storeList) {
        CrossBorderHomeFragment fragment = new CrossBorderHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        fragment.bannerItemList = bannerItemList;
        fragment.navItemCount = navItemCount;
        fragment.navPaneList = navPaneList;
        fragment.shoppingZoneList = shoppingZoneList;
        fragment.bargainGoodsList = bargainGoodsList;
        fragment.groupGoodsList = groupGoodsList;
        fragment.storeList = storeList;

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cross_border_home, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SLog.info("CrossBorderHomeFragment::onViewCreated()");
        Hawk.get(SPField.FIELD_CURR_CROSS_BORDER_THEME_COLOR, "");

        llFloatButtonContainer = view.findViewById(R.id.ll_float_button_container);
        Util.setOnClickListener(view, R.id.btn_goto_cart, this);
        Util.setOnClickListener(view, R.id.btn_goto_top, this);

        CrossBorderHomeItem header = new CrossBorderHomeItem();
        header.bannerItemList = bannerItemList;
        header.navItemCount = navItemCount;
        header.navPaneList = navPaneList;
        header.shoppingZoneList = shoppingZoneList;
        header.bargainGoodsList = bargainGoodsList;
        header.groupGoodsList = groupGoodsList;
        header.storeList = storeList;
        header.itemType = Constant.ITEM_TYPE_HEADER;

        crossBorderHomeItemList.clear();
        crossBorderHomeItemList.add(header);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new CrossBorderHomeAdapter(_mActivity, crossBorderHomeItemList);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                CrossBorderHomeItem item = crossBorderHomeItemList.get(position);

                if (id == R.id.btn_view_more_bargain) {
                    Util.startFragment(BargainListFragment.newInstance(true));
                } else if (id == R.id.btn_view_more_group) {
                    Util.startFragment(GroupInfoListFragment.newInstance(true));
                } else if (id == R.id.cl_container_left) {
                    if (item.goodsPair != null && item.goodsPair.left != null) {
                        Util.startFragment(GoodsDetailFragment.newInstance(item.goodsPair.left.commonId, 0));
                    }
                } else if (id == R.id.cl_container_right) {
                    if (item.goodsPair != null && item.goodsPair.right != null) {
                        Util.startFragment(GoodsDetailFragment.newInstance(item.goodsPair.right.commonId, 0));
                    }
                }
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

    private void loadData(int page) {
        String url = Api.PATH_TARIFF_BUY_INDEX_GOODS;
        EasyJSONObject params = EasyJSONObject.generate(
                "page", page,
                "pageSize", 20
        );

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

                    int index = 0;
                    GoodsSearchItemPair goodsPair = null;
                    EasyJSONArray goodsArray = responseObj.getSafeArray("datas.goodsList");
                    for (Object object : goodsArray) {
                        EasyJSONObject goodsObject = (EasyJSONObject) object;

                        GoodsSearchItem goods = new GoodsSearchItem(Constant.ITEM_TYPE_NORMAL);
                        goods.commonId = goodsObject.optInt("commonId");
                        goods.imageSrc = goodsObject.optString("imageName");
                        goods.goodsName = goodsObject.optString("goodsName");
                        goods.jingle = goodsObject.optString("jingle");
                        goods.price = Util.getSpuPrice(goodsObject);
                        goods.tariffEnable = goodsObject.optInt("tariffEnable");
                        goods.storeName = goodsObject.optString("storeName");
                        goods.storeAvatarUrl = goodsObject.optString("storeAvatar");
                        goods.isFreightFree = (goodsObject.optInt("isPinkage") == Constant.TRUE_INT);
                        goods.nationalFlag = goodsObject.optString("adminCountry.nationalFlag");

                        if (index % 2 == 0) {
                            CrossBorderHomeItem item = new CrossBorderHomeItem();
                            goodsPair = new GoodsSearchItemPair(Constant.ITEM_TYPE_NORMAL);

                            item.itemType = Constant.ITEM_TYPE_NORMAL;
                            item.goodsPair = goodsPair;
                            crossBorderHomeItemList.add(item);

                            goodsPair.left = goods;
                        } else {
                            goodsPair.right = goods;
                        }

                        ++index;
                    }

                    if (!hasMore) {
                        CrossBorderHomeItem footer = new CrossBorderHomeItem();
                        footer.itemType = Constant.ITEM_TYPE_FOOTER;
                        crossBorderHomeItemList.add(footer);
                    }

                    adapter.loadMoreComplete();
                    adapter.setNewData(crossBorderHomeItemList);
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

    private void showFloatButton() {
        llFloatButtonContainer.setTranslationX(0);
    }
    private void hideFloatButton() {
        llFloatButtonContainer.setTranslationX(Util.dip2px(_mActivity, 30.5f));
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("____onSupportVisible");
        Hawk.put(SPField.FIELD_CAN_CHANGE_BACKGROUND_COLOR, true);

        String themeColor = Hawk.get(SPField.FIELD_CURR_CROSS_BORDER_THEME_COLOR);
        if (!StringUtil.isEmpty(themeColor)) {
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_CROSS_BORDER_HOME_THEME_COLOR, new ChangeColorResult(themeColor, 9999));
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SLog.info("____onSupportInvisible");
        Hawk.put(SPField.FIELD_CAN_CHANGE_BACKGROUND_COLOR, false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_goto_cart) {
            if (User.isLogin()) {
                Util.startFragment(CartFragment.newInstance(true));
            } else {
                Util.showLoginFragment(requireContext());
            }
        } else if (id == R.id.btn_goto_top) {
            rvList.scrollToPosition(0);
        }
    }
}
