package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.NewFriendListAdapter;
import com.ftofs.twant.entity.NewFriendItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 新的朋友Fragment
 * @author zwm
 */
public class NewFriendFragment extends BaseFragment implements View.OnClickListener {
    NewFriendListAdapter adapter;

    List<NewFriendItem> newFriendItemList = new ArrayList<>();

    public static NewFriendFragment newInstance() {
        Bundle args = new Bundle();

        NewFriendFragment fragment = new NewFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_friend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_add_friend, this);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new NewFriendListAdapter(R.layout.new_friend_list_item, newFriendItemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_accept) {
                    SLog.info("接受好友邀請");
                }
            }
        });
        rvList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        newFriendItemList.clear();
        for (int i = 0; i < 10; i++) {
            NewFriendItem newFriendItem = new NewFriendItem();

            if (i % 2 == 0) {
                newFriendItem.status = NewFriendItem.STATUS_INITIAL;
            } else {
                newFriendItem.status = NewFriendItem.STATUS_ACCEPTED;
            }
            newFriendItem.memberName = "test";
            newFriendItem.avatarUrl = "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/f8/11/f8111bf7e5052421c991224a0042fc95.png";
            newFriendItem.remark = "注" + i;
            newFriendItem.nickname = "老" + i;

            newFriendItemList.add(newFriendItem);
        }

        adapter.setNewData(newFriendItemList);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_add_friend) {
            start(AddFriendFragment.newInstance());
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
