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
import com.ftofs.twant.log.SLog;

import java.util.ArrayList;
import java.util.List;

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

    public static FirstFragment newInstance() {
        Bundle args = new Bundle();

        FirstFragment fragment = new FirstFragment();
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

        int categoryTitlePosition = 0;
        for (int i = 0; i < categoryItemCount.length; i++) {
            int itemCount = categoryItemCount[i];
            for (int j = 0; j < itemCount; j++) {
                itemList.add(new Item(j == 0, i, j));
            }
            menuList.add(new Menu(i, i == 0, categoryTitlePosition));

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
            }
        });
        rvMenuList.setAdapter(menuAdapter);


        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        itemAdapter = new ItemAdapter(itemList);
        rvList.setAdapter(itemAdapter);

        setNestedScrollingEnabled(false);
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        rvMenuList.setNestedScrollingEnabled(enabled);
        rvList.setNestedScrollingEnabled(enabled);
    }
}
