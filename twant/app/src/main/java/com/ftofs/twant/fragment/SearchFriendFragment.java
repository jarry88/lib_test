package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.UniversalMemberListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.UniversalMemberItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.hyphenate.chat.EMConversation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 搜索好友Fragment
 * @author zwm
 */
public class SearchFriendFragment extends BaseFragment implements View.OnClickListener {
    EditText etKeyword;
    UniversalMemberListAdapter adapter;
    List<UniversalMemberItem> friendList = new ArrayList<>();

    public static SearchFriendFragment newInstance() {
        Bundle args = new Bundle();
        
        SearchFriendFragment fragment = new SearchFriendFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_friend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        etKeyword = view.findViewById(R.id.et_keyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = etKeyword.getText().toString().trim();

                    if (StringUtil.isEmpty(keyword)) {
                        return true;
                    }

                    doSearch(keyword);
                }
                return false;
            }
        });

        RecyclerView rvFriendList = view.findViewById(R.id.rv_friend_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        rvFriendList.setLayoutManager(layoutManager);
        adapter = new UniversalMemberListAdapter(R.layout.universal_member_list_item, friendList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UniversalMemberItem item = friendList.get(position);
                EMConversation conversation = ChatUtil.getConversation(item.memberName,
                        item.nickname, item.avatarUrl, ChatUtil.ROLE_MEMBER);

                FriendInfo friendInfo = new FriendInfo();
                friendInfo.memberName = item.memberName;
                friendInfo.nickname = item.nickname;
                friendInfo.avatarUrl = item.avatarUrl;
                friendInfo.role = ChatUtil.ROLE_MEMBER;
                start(ChatFragment.newInstance(conversation, friendInfo));
            }
        });
        rvFriendList.setAdapter(adapter);
    }

    private void doSearch(String keyword) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_CONTACT_SEARCH;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "type", 1, // 0全部1好友3商店群
                "name", keyword);

        SLog.info("params[%s]", params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    EasyJSONArray friends = responseObj.getSafeArray("datas.friendList");
                    for (Object object : friends) {
                        EasyJSONObject friend = (EasyJSONObject) object;
                        UniversalMemberItem item = new UniversalMemberItem();

                        item.memberName = friend.getSafeString("memberName");
                        item.nickname = friend.getSafeString("nickName");
                        item.avatarUrl = friend.getSafeString("avatar");
                        item.memberSignature = friend.getSafeString("memberSignature");

                        friendList.add(item);
                    }
                    adapter.setNewData(friendList);

                    hideSoftInput();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}


