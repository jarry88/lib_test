package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.MyFriendListAdapter;
import com.ftofs.twant.adapter.TrustValueListAdapter;
import com.ftofs.twant.entity.MyFriendListItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的好友Fragment
 * @author zwm
 */
public class MyFriendFragment extends BaseFragment implements View.OnClickListener {
    List<MyFriendListItem> myFriendListItems = new ArrayList<>();
    MyFriendListAdapter adapter;

    public static MyFriendFragment newInstance() {
        Bundle args = new Bundle();

        MyFriendFragment fragment = new MyFriendFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new MyFriendListAdapter(R.layout.my_friend_list_item, myFriendListItems);
        rvList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        myFriendListItems.add(new MyFriendListItem());
        myFriendListItems.add(new MyFriendListItem());
        myFriendListItems.add(new MyFriendListItem());
        myFriendListItems.add(new MyFriendListItem());

        adapter.setNewData(myFriendListItems);
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }
}
