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
import com.ftofs.twant.adapter.ShopGoodsListAdapter;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.LinkageTestFragment;
import com.ftofs.twant.tangram.SloganView;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;


/**
 * 購物專場二級聯動子頁面
 *暂时页面内嵌套二级联动方案 后续研究
 * @author gzp
 */
public class ShoppingLinkageFragment extends BaseFragment implements View.OnClickListener {

    private LinkageTestFragment parentFragment;
    private RecyclerView rvGoodsWithoutCategory;

    private ShopGoodsListAdapter shopGoodsListAdapter;
    private List<Goods> goodsList;
    private EasyJSONArray zoneGoodsVoList;
    private boolean initView;
    private boolean dataLoaded;

    public static ShoppingLinkageFragment newInstance (ShoppingSpecialFragment shoppingSpecialFragment)  {
        ShoppingLinkageFragment fragment = new ShoppingLinkageFragment();
        fragment.parentFragment = null;
        return fragment;
    }

    public static ShoppingLinkageFragment newInstance() {
        ShoppingLinkageFragment fragment = new ShoppingLinkageFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_shopping_linkage, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goodsList = new ArrayList<>();

        rvGoodsWithoutCategory = view.findViewById(R.id.rv_shopping_good_without_category_list);
        shopGoodsListAdapter = new ShopGoodsListAdapter(_mActivity, goodsList,R.layout.adapter_shopping_zone_secondary_linear);
        initGoodsAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        rvGoodsWithoutCategory.setLayoutManager(linearLayoutManager);
        rvGoodsWithoutCategory.setAdapter(shopGoodsListAdapter);
        rvGoodsWithoutCategory.setNestedScrollingEnabled(false);
        addOnNestedScroll();

        SLog.info("%s",rvGoodsWithoutCategory==null);
        initView = true;
        updateView();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");
        updateView();

    }

    private void updateView() {
        if (initView&&!dataLoaded) {
            updateGoodVoList(zoneGoodsVoList);
        }
    }


    private void initGoodsAdapter() {
        shopGoodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Goods goods = goodsList.get(position);

                SLog.info("here");
                int commonId = goods.id;
                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
            }
        });
        shopGoodsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                Goods goods = goodsList.get(position);

                int commonId = goods.id;
                int userId = User.getUserId();
                if (id == R.id.iv_goods_add) {
                    if (userId > 0) {
                        if (goods.hasGoodsStorage()) {
                            ToastUtil.error(_mActivity,"該產品已售罄，看看其他的吧");
                            return;
                        }
                        if (goods.goodsStatus == 0) {
                            ToastUtil.error(_mActivity,"商品已下架");
                            return;

                        }
                        parentFragment.showSpecSelectPopup(commonId);
                    } else {
                        Util.showLoginFragment();
                    }
                }
            }
        });
    }


    public void updateGoodVoList(EasyJSONArray zoneGoodsVoList) {
        try {
            goodsList.clear();
            for (Object object1 : zoneGoodsVoList) {
                EasyJSONObject goods = (EasyJSONObject) object1;
                Goods goodsInfo = Goods.parse(goods);
                goodsList.add(goodsInfo);
            }
            shopGoodsListAdapter.setNewData(goodsList);
            dataLoaded=true;
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public void setGoodVoList(EasyJSONArray zoneGoodsVoList) {
        this.zoneGoodsVoList = zoneGoodsVoList;
        updateView();

    }

    @Override
    public void onClick(View v) {
        SLog.info("onClick%s",v.getId());
        int id = v.getId();
        if (id == R.id.btn_test) {
            SLog.info("here");
        }
    }



    public void addOnNestedScroll() {
        rvGoodsWithoutCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    parentFragment.onCbStartNestedScroll();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    parentFragment.onCbStopNestedScroll();

                }
            }
        });
    }

    public void setNestedScroll(LinkageTestFragment linkageTestFragment) {
        this.parentFragment = linkageTestFragment;
    }

    public void scrollToTop() {
        SLog.info("rvGoodsWithoutCategory %s",rvGoodsWithoutCategory!=null);
        if (rvGoodsWithoutCategory != null) {
            rvGoodsWithoutCategory.scrollToPosition(0);
            rvGoodsWithoutCategory.setNestedScrollingEnabled(false);
        }
    }

    public void setNestedScrollingEnabled(boolean nestedScroll) {
        if (rvGoodsWithoutCategory != null) {
            SLog.info("打開滾動【%s}",nestedScroll);
            rvGoodsWithoutCategory.setNestedScrollingEnabled(nestedScroll);
        }
    }
}
