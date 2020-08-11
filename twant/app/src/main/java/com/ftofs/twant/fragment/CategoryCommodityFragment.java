package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 產品分類Fragment
 * @author zwm
 */
public class CategoryCommodityFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvCategoryMenu;
    List<CategoryMenu> categoryMenuList = new ArrayList<>();
    CategoryCommodityMenuAdapter categoryCommodityMenuAdapter;

    RecyclerView rvCommodityList;
    List<CategoryCommodityRow> categoryCommodityRowList = new ArrayList<>();
    CategoryCommodityAdapter categoryCommodityAdapter;

    HashMap<Integer, CategoryCommodityList> categoryCommodityListMap = new HashMap<>();

    boolean isShrunk = true;

    int screenWidth;

    int menuShrunkWidth;
    int menuExpandedWidth;
    int contentWidth;

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

        Pair<Integer, Integer> dim = Util.getScreenDimension(_mActivity);
        screenWidth = dim.first;

        menuShrunkWidth = Util.dip2px(_mActivity, 100);
        menuExpandedWidth = screenWidth * 2 / 3;
        contentWidth = screenWidth - menuShrunkWidth;

        rvCategoryMenu = view.findViewById(R.id.rv_category_menu);
        rvCommodityList = view.findViewById(R.id.rv_commodity_list);

        LinearLayoutManager layoutManagerCategory = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvCategoryMenu.setLayoutManager(layoutManagerCategory);
        categoryCommodityMenuAdapter = new CategoryCommodityMenuAdapter(_mActivity, R.layout.category_commodity_brand_menu_item, categoryMenuList);
        categoryCommodityMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("here");
                if (isShrunk) {
                    expandMenu();
                    isShrunk = false;
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
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rvCommodityList.getLayoutParams();
        layoutParams.width = contentWidth;


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

                Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
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
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rvCategoryMenu.getLayoutParams();
        layoutParams.width = menuShrunkWidth;
        rvCategoryMenu.setLayoutParams(layoutParams);
        isShrunk = true;
    }

    private void expandMenu() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rvCategoryMenu.getLayoutParams();
        layoutParams.width = menuExpandedWidth;
        rvCategoryMenu.setLayoutParams(layoutParams);

        // setNewData刷新一下布局
        categoryCommodityMenuAdapter.setNewData(categoryMenuList);
        isShrunk = false;
    }


    /**
     * 加載分類菜單數據
     */
    private void loadCategoryMenuData() {
        String url = Api.PATH_COMMODITY_CATEGORY;
        Api.getUI(url, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, "", "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, "", responseStr, "");
                        return;
                    }

                    int defaultCategoryId = -1;
                    boolean first = true;
                    EasyJSONArray easyJSONArray = responseObj.getSafeArray("datas.CategoryNavVo");
                    for (Object object : easyJSONArray) { // 每個菜單項
                        EasyJSONObject item = (EasyJSONObject) object;
                        int categoryId = item.getInt("categoryId");
                        if (first) {
                            defaultCategoryId = categoryId;
                        }
                        String categoryName = item.getSafeString("categoryName");
                        String imageUrl = item.getSafeString("appImageUrl");

                        CategoryMenu categoryMenu = new CategoryMenu(categoryId, categoryName, null);
                        if (first) {
                            // 默認選中第一個
                            categoryMenu.selected = true;
                        }
                        categoryMenuList.add(categoryMenu);

                        CategoryCommodityList categoryCommodityList = new CategoryCommodityList();
                        categoryCommodityList.head = new CategoryCommodity(categoryId, categoryName, imageUrl);

                        EasyJSONArray categoryList = item.getSafeArray("categoryList");
                        List<CategoryCommodityRow> categoryCommodityRows = new ArrayList<>();
                        int i = 0;
                        CategoryCommodityRow categoryCommodityRow = null;
                        for (Object subObject : categoryList) {
                            if (i % CategoryCommodityRow.COLUMN_COUNT == 0) {  // 每3個產品放一行
                                categoryCommodityRow = new CategoryCommodityRow();
                                categoryCommodityRows.add(categoryCommodityRow);
                            }
                            EasyJSONObject subItem = (EasyJSONObject) subObject;
                            int subCategoryId = subItem.getInt("categoryId");
                            String subCategoryName = subItem.getSafeString("categoryName");
                            String subImageUrl = subItem.getSafeString("appImageUrl");

                            categoryCommodityRow.categoryCommodityList.add(new CategoryCommodity(subCategoryId, subCategoryName, subImageUrl));

                            ++i;
                        }
                        // SLog.info("i__=%d", i);
                        categoryCommodityList.list = categoryCommodityRows;
                        if (first) {
                            categoryCommodityRowList = categoryCommodityRows;
                        }

                        categoryCommodityListMap.put(categoryId, categoryCommodityList);
                        first = false;
                    }

                    categoryCommodityMenuAdapter.setNewData(categoryMenuList);

                    loadCategoryCommodityData(defaultCategoryId);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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

    }
}
