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

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ItemAdapter;
import com.ftofs.twant.adapter.MenuAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.Item;
import com.ftofs.twant.entity.Menu;
import com.ftofs.twant.interfaces.OnItemClickListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.LinkageTestFragment;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.StringUtil;

import org.litepal.util.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

public class ShoppingSpecialLinkageFragment extends BaseFragment {
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
    private List<Goods> items = new ArrayList<>();
    private boolean dataLoaded;
    private EasyJSONArray dataList;
    private LinkageTestFragment parentFragment;

    public static ShoppingSpecialLinkageFragment newInstance() {
        Bundle args = new Bundle();

        ShoppingSpecialLinkageFragment fragment = new ShoppingSpecialLinkageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        setCategory();
        rvMenuList = view.findViewById(R.id.rv_menu_list);
        rvList = view.findViewById(R.id.rv_list);

//        initAdapter();

    }

    private void initAdapter() {

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
        rvMenuList.setLayoutManager(new LinearLayoutManager(_mActivity));

        itemAdapter = new ItemAdapter(itemList, AssetsUtil.getTypeface(_mActivity,"fonts/din_alternate_bold.ttf"));
        rvList.setAdapter(itemAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));

        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                SLog.info("onScrollStateChanged, newState[%d]", newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) rvList.getLayoutManager();
                    int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    Item item = itemList.get(position);

                    selectCategory(item.category);
                    parentFragment.onCbStopNestedScroll();
                }
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {

                    parentFragment.onCbStartNestedScroll();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                SLog.info("onScrolled, dx[%d], dy[%d]", dx, dy);
            }
        });



        setNestedScrollingEnabled(false);
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
        if (rvMenuList == null||rvList==null) {
            return;
        }
        rvMenuList.setNestedScrollingEnabled(enabled);
        rvList.setNestedScrollingEnabled(enabled);
    }

    public void updateView(EasyJSONArray zoneGoodsCategoryVoList) {
        if (zoneGoodsCategoryVoList == null) {
            return;
        }
        SLog.info("%s",zoneGoodsCategoryVoList.toString());
        if (zoneGoodsCategoryVoList.length() == 0) {
            return;
        }
        SLog.info("設置二級聯動列表數據");
        try {
            items.clear();
            int i = 0;
            int categoryTitlePosition = 0;
            for (Object object : zoneGoodsCategoryVoList) {
                EasyJSONObject categoryData = (EasyJSONObject) object;

                String groupName = categoryData.getSafeString("categoryName");
                EasyJSONArray goodsList = categoryData.getSafeArray("zoneGoodsVoList");
                Goods category = new Goods(Constant.ITEM_TYPE_TITLE);
                category.name = groupName;
                if (goodsList == null) {
                    continue;
                }
                items.add(category);
                int j = 0;
                itemList.add(new Item(true, i, j,category));
                for (Object object1 : goodsList) {
                    j++;
                    EasyJSONObject goods = (EasyJSONObject) object1;
//                    items.add(Goods.parse(goods));
                    itemList.add(new Item(false, i, j,Goods.parse(goods)));
                }
                menuList.add(new Menu(groupName, i == 0, categoryTitlePosition));
                categoryTitlePosition++;

                categoryTitlePosition += goodsList.length();
                i++;
            }

            initAdapter();
//            setCategory();
            dataLoaded =true;


        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
    private void setCategory() {
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

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!dataLoaded) {
            updateView(dataList);
        }
    }

    public void setDataList(EasyJSONArray zoneGoodsCategoryVoList) {
        dataList = zoneGoodsCategoryVoList;
    }

    public void scrollToTop() {
        if (rvMenuList != null&&rvMenuList!=null) {
            rvList.scrollToPosition(0);
            rvMenuList.scrollToPosition(0);
            setSelectedPosition(0);
            setNestedScrollingEnabled(false);
        }

    }

    private void setSelectedPosition(int i) {
        rvMenuList.getChildAt(i).performClick();
    }

    public void addOnNestedScroll() {
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
}
