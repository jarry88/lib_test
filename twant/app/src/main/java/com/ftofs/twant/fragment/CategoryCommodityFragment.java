package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CategoryCommodityAdapter;
import com.ftofs.twant.adapter.CategoryCommodityMenuAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.CategoryCommodity;
import com.ftofs.twant.entity.CategoryCommodityList;
import com.ftofs.twant.entity.CategoryCommodityRow;
import com.ftofs.twant.entity.CategoryMenu;
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

/**
 * 商品分類Fragment
 * @author zwm
 */
public class CategoryCommodityFragment extends BaseFragment implements View.OnClickListener {
    LinearLayout llMenuContainer;
    FrameLayout flMask;

    RecyclerView rvCategoryMenu;
    List<CategoryMenu> categoryMenuList = new ArrayList<>();
    CategoryCommodityMenuAdapter categoryCommodityMenuAdapter;

    RecyclerView rvCommodityList;
    List<CategoryCommodityRow> categoryCommodityRowList = new ArrayList<>();
    CategoryCommodityAdapter categoryCommodityAdapter;

    HashMap<Integer, CategoryCommodityList> categoryCommodityListMap = new HashMap<>();

    public static CategoryCommodityFragment newInstance() {
        Bundle args = new Bundle();

        CategoryCommodityFragment fragment = new CategoryCommodityFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_commodity, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llMenuContainer = view.findViewById(R.id.ll_menu_container);
        flMask = view.findViewById(R.id.fl_mask);
        flMask.setOnClickListener(this);


        rvCategoryMenu = view.findViewById(R.id.rv_category_menu);
        rvCommodityList = view.findViewById(R.id.rv_commodity_list);

        LinearLayoutManager layoutManagerCategory = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvCategoryMenu.setLayoutManager(layoutManagerCategory);
        categoryCommodityMenuAdapter = new CategoryCommodityMenuAdapter(_mActivity, R.layout.category_commodity_brand_menu_item, categoryMenuList);
        categoryCommodityMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("here");
                if (!categoryCommodityMenuAdapter.isExpanded()) {
                    SLog.info("here");
                    RelativeLayout.LayoutParams rlLayoutParams = (RelativeLayout.LayoutParams) llMenuContainer.getLayoutParams();
                    rlLayoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    llMenuContainer.setLayoutParams(rlLayoutParams);

                    categoryCommodityMenuAdapter.setExpanded(true);
                    // setNewData刷新一下布局
                    categoryCommodityMenuAdapter.setNewData(categoryMenuList);
                    return;
                }

                for (CategoryMenu categoryMenu : categoryMenuList) {
                    categoryMenu.selected = false;
                }

                shrinkMenu();

                // 更新菜單的高亮顯示
                CategoryMenu categoryMenu = categoryMenuList.get(position);
                categoryMenu.selected = true;
                categoryCommodityMenuAdapter.setNewData(categoryMenuList);

                loadCategoryCommodityData(categoryMenu.categoryId);
            }
        });
        rvCategoryMenu.setAdapter(categoryCommodityMenuAdapter);

        rvCommodityList = view.findViewById(R.id.rv_commodity_list);
        LinearLayoutManager layoutManagerCommodity = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvCommodityList.setLayoutManager(layoutManagerCommodity);
        categoryCommodityAdapter = new CategoryCommodityAdapter(_mActivity, R.layout.category_commodity_row, categoryCommodityRowList);
        categoryCommodityAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                CategoryCommodity categoryCommodity = null;
                CategoryCommodityRow categoryCommodityRow = categoryCommodityRowList.get(position);
                if (id == R.id.category_image1 || id == R.id.tv_category_name1) {
                    if (categoryCommodityRow.categoryCommodityList.size() < 1) {
                        return;
                    }
                    categoryCommodity = categoryCommodityRow.categoryCommodityList.get(0);
                } else if (id == R.id.category_image2 || id == R.id.tv_category_name2) {
                    if (categoryCommodityRow.categoryCommodityList.size() < 2) {
                        return;
                    }
                    categoryCommodity = categoryCommodityRow.categoryCommodityList.get(1);
                } else {
                    if (categoryCommodityRow.categoryCommodityList.size() < 3) {
                        return;
                    }
                    categoryCommodity = categoryCommodityRow.categoryCommodityList.get(2);
                }

                MainFragment mainFragment = MainFragment.getInstance();
                mainFragment.start(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                EasyJSONObject.generate("cat", String.valueOf(categoryCommodity.categoryId)).toString()));
            }
        });


        rvCommodityList.setAdapter(categoryCommodityAdapter);

        loadCategoryMenuData();
    }

    /**
     * 收縮菜單
     */
    private void shrinkMenu() {
        RelativeLayout.LayoutParams rlLayoutParams = (RelativeLayout.LayoutParams) llMenuContainer.getLayoutParams();
        rlLayoutParams.width = Util.dip2px(_mActivity, 100);
        llMenuContainer.setLayoutParams(rlLayoutParams);

        categoryCommodityMenuAdapter.setExpanded(false);
    }


    /**
     * 加載分類菜單數據
     */
    private void loadCategoryMenuData() {
        Api.getUI(Api.PATH_COMMODITY_CATEGORY, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

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
                    EasyJSONArray easyJSONArray = responseObj.getArray("datas.CategoryNavVo");
                    for (Object object : easyJSONArray) { // 每個菜單項
                        EasyJSONObject item = (EasyJSONObject) object;
                        int categoryId = item.getInt("categoryId");
                        if (first) {
                            defaultCategoryId = categoryId;
                        }
                        String categoryName = item.getString("categoryName");
                        String imageUrl = item.getString("appImageUrl");

                        CategoryMenu categoryMenu = new CategoryMenu(categoryId, categoryName, null);
                        if (first) {
                            // 默認選中第一個
                            categoryMenu.selected = true;
                        }
                        categoryMenuList.add(categoryMenu);

                        CategoryCommodityList categoryCommodityList = new CategoryCommodityList();
                        categoryCommodityList.head = new CategoryCommodity(categoryId, categoryName, imageUrl);

                        EasyJSONArray categoryList = item.getArray("categoryList");
                        List<CategoryCommodityRow> categoryCommodityRows = new ArrayList<>();
                        int i = 0;
                        CategoryCommodityRow categoryCommodityRow = null;
                        for (Object subObject : categoryList) {
                            if (i % CategoryCommodityRow.COLUMN_COUNT == 0) {  // 每3個商品放一行
                                categoryCommodityRow = new CategoryCommodityRow();
                                categoryCommodityRows.add(categoryCommodityRow);
                            }
                            EasyJSONObject subItem = (EasyJSONObject) subObject;
                            int subCategoryId = subItem.getInt("categoryId");
                            String subCategoryName = subItem.getString("categoryName");
                            String subImageUrl = subItem.getString("appImageUrl");

                            categoryCommodityRow.categoryCommodityList.add(new CategoryCommodity(subCategoryId, subCategoryName, subImageUrl));

                            ++i;
                        }
                        SLog.info("i__=%d", i);
                        categoryCommodityList.list = categoryCommodityRows;
                        if (first) {
                            categoryCommodityRowList = categoryCommodityRows;
                        }

                        categoryCommodityListMap.put(categoryId, categoryCommodityList);
                        first = false;
                    }

                    categoryCommodityMenuAdapter.setNewData(categoryMenuList);

                    loadCategoryCommodityData(defaultCategoryId);
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadCategoryCommodityData(int categoryId) {
        // TODO: 2019/5/6 優化
        SLog.info("categoryId[%d]", categoryId);
        CategoryCommodityList item = categoryCommodityListMap.get(categoryId);

        if (item == null) {
            categoryCommodityRowList = new ArrayList<>();
        } else {
            categoryCommodityRowList = item.list;
        }

        categoryCommodityAdapter.setNewData(categoryCommodityRowList);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fl_mask) {
            shrinkMenu();
        }
    }
}
