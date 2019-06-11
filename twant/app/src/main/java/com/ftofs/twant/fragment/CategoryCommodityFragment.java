package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CategoryCommodityAdapter;
import com.ftofs.twant.adapter.CategoryMenuAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.domain.goods.Category;
import com.ftofs.twant.entity.CategoryCommodity;
import com.ftofs.twant.entity.CategoryCommodityList;
import com.ftofs.twant.entity.CategoryCommodityRow;
import com.ftofs.twant.entity.CategoryMenu;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;

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
 * 商品分類Fragment
 * @author zwm
 */
public class CategoryCommodityFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    RecyclerView rvCategoryMenu;

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

        rvCategoryMenu = view.findViewById(R.id.rv_category_menu);
        rvCommodityList = view.findViewById(R.id.rv_commodity_list);

        rvCategoryMenu = view.findViewById(R.id.rv_category_menu);
        LinearLayoutManager layoutManagerCategory = new LinearLayoutManager(_mActivity);
        layoutManagerCategory.setOrientation(LinearLayoutManager.VERTICAL);
        rvCategoryMenu.setLayoutManager(layoutManagerCategory);

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
                    List<CategoryMenu> categoryMenuList = new ArrayList<>();
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

                    CategoryMenuAdapter adapter = new CategoryMenuAdapter(_mActivity, Constant.CATEGORY_TYPE_COMMODITY, categoryMenuList, CategoryCommodityFragment.this);
                    rvCategoryMenu.setAdapter(adapter);

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

    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        loadCategoryCommodityData(id);
    }
}
