package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.FriendItemListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 通信錄Fragment
 * @author zwm
 */
public class ContactFragment extends BaseFragment implements View.OnClickListener {
    TextView tvFragmentTitle;

    List<FriendInfo> friendInfoList = new ArrayList<>();
    FriendItemListAdapter adapter;

    public static ContactFragment newInstance() {
        Bundle args = new Bundle();

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        Util.setOnClickListener(view, R.id.btn_back, this);

        RecyclerView rvContactList = view.findViewById(R.id.rv_contact_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvContactList.setLayoutManager(layoutManager);
        adapter = new FriendItemListAdapter(R.layout.layout_friend_item, friendInfoList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FriendInfo friendInfo = friendInfoList.get(position);

                Util.startFragment(ChatFragment.newInstance(friendInfo.memberName));
            }
        });
        rvContactList.setAdapter(adapter);

        loadContactData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void loadContactData() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        Api.postUI(Api.PATH_CONTACT_LIST, params, new UICallback() {
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
                    EasyJSONArray friendList = responseObj.getArray("datas.friendList");
                    EasyJSONArray groupList = responseObj.getArray("datas.groupList");
                    int contactCount = friendList.length() + groupList.length();
                    String fragmentTitle = getString(R.string.text_contact) + "(" + contactCount + ")";
                    tvFragmentTitle.setText(fragmentTitle);

                    for (Object object : friendList) {
                        EasyJSONObject friend = (EasyJSONObject) object;
                        FriendInfo friendInfo = new FriendInfo();
                        friendInfo.userId = friend.getInt("memberId");
                        friendInfo.memberName = friend.getString("memberName");
                        friendInfo.avatarUrl = friend.getString("avatar");
                        friendInfo.gender = friend.getInt("memberSex");
                        friendInfo.nickname = friend.getString("nickName");

                        friendInfoList.add(friendInfo);
                    }

                    adapter.setNewData(friendInfoList);
                } catch (Exception e) {

                }
            }
        });
    }
}


