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
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.NewFriendItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

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

                    NewFriendItem newFriendItem = newFriendItemList.get(position);
                    SLog.info("newFriendItem.memberName[%s]", newFriendItem.memberName);

                    handleFriendRequest(newFriendItem.memberName, 1);
                }
            }
        });
        rvList.setAdapter(adapter);

        loadData();
    }

    /**
     * 處理好友請求
     * @param action  1 同意   2 拒絕
     */
    private void handleFriendRequest(String memberName, int action) {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "friendMemberName", memberName,
                "state", action);

        Api.postUI(Api.PATH_HANDLE_FRIEND_REQUEST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                loadData();
            }
        });
    }

    private void loadData() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);
        Api.postUI(Api.PATH_FRIEND_REQUEST_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONArray memberList = responseObj.getArray("datas.requestMemberList");

                    newFriendItemList.clear();
                    for (Object object : memberList) {
                        EasyJSONObject member = (EasyJSONObject) object;

                        NewFriendItem newFriendItem = new NewFriendItem();
                        newFriendItem.memberName = member.getString("fromMember");
                        newFriendItem.nickname = member.getString("memberInfo.nickName");
                        newFriendItem.remark = member.getString("notes");
                        newFriendItem.avatarUrl = member.getString("memberInfo.avatar");
                        newFriendItem.status = member.getInt("state");

                        newFriendItemList.add(newFriendItem);
                    }

                    adapter.setNewData(newFriendItemList);
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
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
