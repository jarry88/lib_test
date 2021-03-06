package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ItemAdapter;
import com.ftofs.twant.adapter.MenuAdapter;
import com.ftofs.twant.entity.Item;
import com.ftofs.twant.entity.Menu;
import com.ftofs.twant.interfaces.OnItemClickListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;

public class FirstFragment extends BaseFragment {
    RecyclerView rvMenuList;
    MenuAdapter menuAdapter;
    List<Menu> menuList = new ArrayList<>();

    RecyclerView rvList;
    ItemAdapter itemAdapter;
    List<Item> itemList = new ArrayList<>();

    // 各個分類的Item數
    int[] categoryItemCount = new int[] {
            17, 3, 11, 9, 5, 12, 6, 13, 15, 9, 19, 6, 19, 16, 19, 15, 11, 4, 16, 12
    };

    // 最近一次分類滑動的時間
    long lastScrollingCategoryTime = 0;

    public static FirstFragment newInstance() {
        Bundle args = new Bundle();

        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int categoryTitlePosition = 0;
        for (int i = 0; i < categoryItemCount.length; i++) {
            int itemCount = categoryItemCount[i];
            for (int j = 0; j < itemCount; j++) {
                itemList.add(new Item(j == 0, i, j));
            }
//            menuList.add(new Menu(i, i == 0, categoryTitlePosition));

            categoryTitlePosition += itemCount;
        }
        menuList.get(0).isSelected = true;

        rvMenuList = view.findViewById(R.id.rv_menu_list);
        rvMenuList.setLayoutManager(new LinearLayoutManager(_mActivity));
        menuAdapter = new MenuAdapter(menuList);
        menuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, View view) {
                Menu menu = menuList.get(position);
                SLog.info("category[%d]", menu.category);
                ((LinearLayoutManager)rvList.getLayoutManager()).scrollToPositionWithOffset(menu.categoryTitlePosition, 0);

                // 取消上一個的選中狀態
                Menu lastMenu = menuList.get(menuAdapter.lastSelectedPosition);
                lastMenu.isSelected = false;
                menuAdapter.notifyItemChanged(menuAdapter.lastSelectedPosition);

                // 設置當前Menu的選中狀態
                menu.isSelected = true;
                menuAdapter.notifyItemChanged(position);
                menuAdapter.lastSelectedPosition = position;
            }
        });
        rvMenuList.setAdapter(menuAdapter);


        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                SLog.info("onScrollStateChanged, newState[%d]", newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 最後一次要強制選擇
                    selectScrollingCategory(true);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                SLog.info("onScrolled, dx[%d], dy[%d]", dx, dy);

                selectScrollingCategory(false);
            }
        });
        itemAdapter = new ItemAdapter(itemList);

        itemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, View view) {
                Item item = itemList.get(position);
                String itemDesc;
                if (item.isCategoryTitle) {
                    itemDesc = "分類_" + item.category;
                } else {
                    itemDesc = "分類_" + item.category + "_項目_" + item.id;
                }
                SLog.info("點擊了:" + itemDesc);
            }
        });

        rvList.setAdapter(itemAdapter);

        setNestedScrollingEnabled(false);
    }

    /**
     * 選擇正在滑動的分類
     * @param force  是否強制。如果強制，就會忽略時間間隔
     */
    private void selectScrollingCategory(boolean force) {
        long now = System.currentTimeMillis();

        // 防止調用太頻繁
        if (!force && (now - lastScrollingCategoryTime) < 40) { // 40毫秒內忽略
            return;
        }
        lastScrollingCategoryTime = now;

        LinearLayoutManager layoutManager = (LinearLayoutManager) rvList.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        Item item = itemList.get(position);

        selectCategory(item.category);
    }

    /**
     * 選擇分類
     * @param category
     */
    private void selectCategory(int category) {
        // 取消上一個的選中狀態
        Menu lastMenu = menuList.get(menuAdapter.lastSelectedPosition);
        lastMenu.isSelected = false;
        menuAdapter.notifyItemChanged(menuAdapter.lastSelectedPosition);

        Menu menu = menuList.get(category);
        // 設置當前Menu的選中狀態
        menu.isSelected = true;
        menuAdapter.notifyItemChanged(category);
        menuAdapter.lastSelectedPosition = category;
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        rvMenuList.setNestedScrollingEnabled(enabled);
        rvList.setNestedScrollingEnabled(enabled);
    }

    public void updateView(EasyJSONArray zoneGoodsCategoryVoList) {
        if (zoneGoodsCategoryVoList == null) {
            return;
        }
        if (zoneGoodsCategoryVoList.length() == 0) {
            return;
        }
    }
}
