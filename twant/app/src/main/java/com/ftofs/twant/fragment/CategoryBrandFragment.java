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
import com.ftofs.twant.adapter.CategoryBrandAdapter;
import com.ftofs.twant.adapter.CategoryMenuAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CategoryBrand;
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
 * 品牌分類Fragment
 * @author zwm
 */
public class CategoryBrandFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    RecyclerView rvCategoryMenu;
    RecyclerView rvCommodityList;
    HashMap<Integer, List<CategoryBrand>> categoryBrandListMap = new HashMap<>();

    public static CategoryBrandFragment newInstance() {
        Bundle args = new Bundle();

        CategoryBrandFragment fragment = new CategoryBrandFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_brand, container, false);
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

        rvCommodityList = view.findViewById(R.id.rv_brand_list);
        LinearLayoutManager layoutManagerBrand = new LinearLayoutManager(_mActivity);
        layoutManagerBrand.setOrientation(LinearLayoutManager.VERTICAL);
        rvCommodityList.setLayoutManager(layoutManagerBrand);

        loadCategoryMenuData();
    }

    /**
     * 加載分類菜單數據
     */
    private void loadCategoryMenuData() {
        Api.getUI(Api.PATH_BRAND_CATEGORY, null, new UICallback() {
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
                    EasyJSONArray easyJSONArray = responseObj.getArray("datas.brandLabelVoList");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject item = (EasyJSONObject) object;
                        int categoryId = item.getInt("brandLabelId");
                        if (first) {
                            defaultCategoryId = categoryId;
                            first = false;
                        }
                        String categoryName = item.getString("brandLabelName");


                        CategoryMenu categoryMenu = new CategoryMenu(categoryId, categoryName, null);
                        categoryMenuList.add(categoryMenu);

                        List<CategoryBrand> categoryBrandList = new ArrayList<>();
                        EasyJSONArray brandList = item.getArray("brandList");
                        for (Object brandObject : brandList) {
                            EasyJSONObject brandItem = (EasyJSONObject) brandObject;
                            int brandId = brandItem.getInt("brandId");
                            String brandNameChinese = brandItem.getString("brandName");
                            String brandNameEnglish = brandItem.getString("brandEnglish");
                            String imageUrl = brandItem.getString("brandImage");

                            categoryBrandList.add(new CategoryBrand(brandId, brandNameChinese, brandNameEnglish, imageUrl));
                        }

                        categoryBrandListMap.put(categoryId, categoryBrandList);
                    }

                    CategoryMenuAdapter adapter = new CategoryMenuAdapter(_mActivity, Constant.CATEGORY_TYPE_BRAND, categoryMenuList, CategoryBrandFragment.this);
                    rvCategoryMenu.setAdapter(adapter);

                    loadCategoryBrandData(defaultCategoryId);
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadCategoryBrandData(int categoryId) {
        // TODO: 2019/5/6 優化
        SLog.info("categoryId[%d]", categoryId);
        List<CategoryBrand> categoryBrandList = categoryBrandListMap.get(categoryId);
        if (categoryBrandList == null) {
            return;
        }


        CategoryBrandAdapter adapter = new CategoryBrandAdapter(_mActivity, categoryBrandList);
        rvCommodityList.setAdapter(adapter);
    }




    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        loadCategoryBrandData(id);
    }
}
