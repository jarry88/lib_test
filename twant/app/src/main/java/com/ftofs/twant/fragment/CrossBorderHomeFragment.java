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
import com.ftofs.twant.adapter.CrossBorderHomeAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.entity.CrossBorderHomeItem;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.entity.CrossBorderShoppingZoneItem;
import com.gzp.lib_common.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 跨城購主頁
 * @author zwm
 */
public class CrossBorderHomeFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    CrossBorderHomeAdapter adapter;
    List<CrossBorderHomeItem> crossBorderHomeItemList = new ArrayList<>();

    List<CrossBorderBannerItem> bannerItemList;
    List<CrossBorderNavItem> navItemList;
    List<CrossBorderShoppingZoneItem> shoppingZoneList;

    public static CrossBorderHomeFragment newInstance(List<CrossBorderBannerItem> bannerItemList,
                                                      List<CrossBorderNavItem> navItemList,
                                                      List<CrossBorderShoppingZoneItem> shoppingZoneList) {
        CrossBorderHomeFragment fragment = new CrossBorderHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        fragment.bannerItemList = bannerItemList;
        fragment.navItemList = navItemList;
        fragment.shoppingZoneList = shoppingZoneList;
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cross_border_home, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CrossBorderHomeItem header = new CrossBorderHomeItem();
        header.bannerItemList = bannerItemList;
        header.navItemList = navItemList;
        header.shoppingZoneList = shoppingZoneList;
        header.itemType = Constant.ITEM_TYPE_HEADER;

        crossBorderHomeItemList.add(header);

        for (int i = 0; i < 20; i++) {
            CrossBorderHomeItem item = new CrossBorderHomeItem();
            item.itemType = Constant.ITEM_TYPE_NORMAL;
            crossBorderHomeItemList.add(item);
        }

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new CrossBorderHomeAdapter(_mActivity, crossBorderHomeItemList);
        rvList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
