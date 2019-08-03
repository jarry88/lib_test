package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CategoryMenuAdapter;
import com.ftofs.twant.adapter.CategoryShopAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.CategoryMenu;
import com.ftofs.twant.entity.CategoryShop;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 店鋪分類Fragment
 * @author zwm
 */
public class CategoryShopFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    RecyclerView rvCategoryMenu;
    RecyclerView rvShopList;

    List<CategoryShop> categoryShopList = new ArrayList<>();
    CategoryShopAdapter categoryShopAdapter;

    HashMap<Integer, List<CategoryShop>> categoryShopListMap = new HashMap<>();

    public static CategoryShopFragment newInstance() {
        Bundle args = new Bundle();

        CategoryShopFragment fragment = new CategoryShopFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_shop, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCategoryMenu = view.findViewById(R.id.rv_category_menu);
        LinearLayoutManager layoutManagerCategory = new LinearLayoutManager(_mActivity);
        layoutManagerCategory.setOrientation(LinearLayoutManager.VERTICAL);
        rvCategoryMenu.setLayoutManager(layoutManagerCategory);

        rvShopList = view.findViewById(R.id.rv_shop_list);
        LinearLayoutManager layoutManagerShop = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvShopList.setLayoutManager(layoutManagerShop);
        categoryShopAdapter = new CategoryShopAdapter(_mActivity, R.layout.category_shop_item, categoryShopList);
        categoryShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryShop categoryShop = categoryShopList.get(position);

                Util.startFragment(SearchResultFragment.newInstance(SearchType.STORE.name(),
                        EasyJSONObject.generate("keyword", String.valueOf(categoryShop.shopId)).toString()));
            }
        });
        rvShopList.setAdapter(categoryShopAdapter);

        loadCategoryMenuData();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 加載分類菜單數據
     */
    private void loadCategoryMenuData() {
        Api.getUI(Api.PATH_SHOP_CATEGORY, null, new UICallback() {
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

                    int defaultCategoryId = -1;
                    boolean first = true;
                    List<CategoryMenu> categoryMenuList = new ArrayList<>();
                    EasyJSONArray easyJSONArray = responseObj.getArray("datas.storeClassNav");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject item = (EasyJSONObject) object;
                        int categoryId = item.getInt("id");
                        if (first) {
                            defaultCategoryId = categoryId;
                            first = false;
                        }
                        String categoryName = item.getString("name");
                        String[] categoryNameArr = categoryName.split(" ");

                        CategoryMenu categoryMenu = new CategoryMenu(categoryId, categoryNameArr[0], categoryNameArr[1]);
                        categoryMenuList.add(categoryMenu);

                        List<CategoryShop> categoryShopList = new ArrayList<>();
                        EasyJSONArray shopListArray = item.getArray("children");
                        for (Object shopObject : shopListArray) {
                            EasyJSONObject shopItem = (EasyJSONObject) shopObject;
                            int shopId = shopItem.getInt("id");
                            String coverUrl = shopItem.getString("imageUrl");
                            String shopParentName = shopItem.getString("parentCnName");
                            int shopCount = shopItem.getInt("storeListCount");
                            int commodityCount = shopItem.getInt("goodsListCount");

                            CategoryShop categoryShop = new CategoryShop(shopId, coverUrl, shopParentName, shopCount, commodityCount);
                            categoryShopList.add(categoryShop);
                        }

                        categoryShopListMap.put(categoryId, categoryShopList);
                    }

                    CategoryMenuAdapter adapter = new CategoryMenuAdapter(_mActivity, Constant.CATEGORY_TYPE_SHOP, categoryMenuList, CategoryShopFragment.this);
                    rvCategoryMenu.setAdapter(adapter);

                    loadCategoryShopData(defaultCategoryId);
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void loadCategoryShopData(int categoryId) {
        // TODO: 2019/5/6 優化
        SLog.info("categoryId[%d]", categoryId);
        categoryShopList = categoryShopListMap.get(categoryId);
        SLog.info("count[%d]", categoryShopList.size());
        categoryShopAdapter.setNewData(categoryShopList);
    }

    /**
     * 選中對應的菜單
     * @param categoryId 菜單對應的分類Id
     */
    @Override
    public void onSelected(PopupType type, int categoryId, Object extra) {
        loadCategoryShopData(categoryId);
    }
}
