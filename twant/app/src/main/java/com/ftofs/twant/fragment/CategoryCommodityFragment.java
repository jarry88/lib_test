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

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CategoryCommodityAdapter;
import com.ftofs.twant.adapter.CategoryCommodityMenuAdapter;
import com.ftofs.twant.adapter.CategoryMenuAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.CategoryCommodity;
import com.ftofs.twant.entity.CategoryCommodityList;
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
        GridLayoutManager layoutManagerCommodity = new GridLayoutManager(_mActivity, 3);
        layoutManagerCommodity.setOrientation(GridLayoutManager.VERTICAL);
        rvCommodityList.setLayoutManager(layoutManagerCommodity);

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
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    int defaultCategoryId = -1;
                    boolean first = true;
                    List<CategoryMenu> categoryMenuList = new ArrayList<>();
                    EasyJSONArray easyJSONArray = responseObj.getArray("datas.CategoryNavVo");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject item = (EasyJSONObject) object;
                        int categoryId = item.getInt("categoryId");
                        if (first) {
                            defaultCategoryId = categoryId;
                            first = false;
                        }
                        String categoryName = item.getString("categoryName");
                        String imageUrl = item.getString("appImageUrl");

                        CategoryMenu categoryMenu = new CategoryMenu(categoryId, categoryName, null);
                        categoryMenuList.add(categoryMenu);

                        CategoryCommodityList categoryCommodityList = new CategoryCommodityList();
                        categoryCommodityList.head = new CategoryCommodity(categoryId, categoryName, imageUrl);

                        EasyJSONArray categoryList = item.getArray("categoryList");
                        for (Object subObject : categoryList) {
                            EasyJSONObject subItem = (EasyJSONObject) subObject;
                            int subCategoryId = subItem.getInt("categoryId");
                            String subCategoryName = subItem.getString("categoryName");
                            String subImageUrl = subItem.getString("appImageUrl");

                            categoryCommodityList.list.add(new CategoryCommodity(subCategoryId, subCategoryName, subImageUrl));
                        }

                        categoryCommodityListMap.put(categoryId, categoryCommodityList);
                    }

                    CategoryCommodityMenuAdapter adapter = new CategoryCommodityMenuAdapter(_mActivity, categoryMenuList, CategoryCommodityFragment.this);
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
        CategoryCommodityList categoryCommodityList = categoryCommodityListMap.get(categoryId);
        if (categoryCommodityList == null) {
            return;
        }

        /*
        CategoryCommodityAdapter adapter = new CategoryCommodityAdapter(_mActivity, categoryCommodityList.list);
        rvCommodityList.setAdapter(adapter);
        */
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSelected(int id) {

    }
}
