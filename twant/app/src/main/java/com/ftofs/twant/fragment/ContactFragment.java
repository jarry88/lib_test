package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.FriendItemListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.hyphenate.chat.EMConversation;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

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
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_new_friend, this);
        Util.setOnClickListener(view, R.id.btn_contact_menu, this);
        Util.setOnClickListener(view, R.id.btn_contact_find, this);

        RecyclerView rvContactList = view.findViewById(R.id.rv_contact_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvContactList.setLayoutManager(layoutManager);
        adapter = new FriendItemListAdapter(R.layout.layout_friend_item, friendInfoList);
        adapter.setOnItemClickListener((adapter, view1, position) -> {
            FriendInfo friendInfo = friendInfoList.get(position);
            EMConversation conversation = ChatUtil.getConversation(friendInfo.memberName,
                    friendInfo.nickname, friendInfo.avatarUrl, ChatUtil.ROLE_MEMBER);
            FriendInfo.upsertFriendInfo(friendInfo.memberName, friendInfo.nickname, friendInfo.avatarUrl, ChatUtil.ROLE_MEMBER);
            Util.startFragment(ChatFragment.newInstance(conversation, friendInfo));
        });
        rvContactList.setAdapter(adapter);

        loadContactData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_new_friend) {
            start(NewFriendFragment.newInstance());
        } else if (id == R.id.btn_contact_menu) {
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 11))
                    .offsetY(-Util.dip2px(_mActivity, 8))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_CONTACT))
                    .show();
        } else if (id == R.id.btn_contact_find) {
            Util.startFragment(SearchFriendFragment.newInstance());
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
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

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONArray friendList = responseObj.getSafeArray("datas.friendList");
                    EasyJSONArray groupList = responseObj.getSafeArray("datas.groupList");
                    int contactCount = friendList.length() + groupList.length();
                    String fragmentTitle = getString(R.string.text_contact) + "(" + contactCount + ")";
                    tvFragmentTitle.setText(fragmentTitle);

                    for (Object object : friendList) {
                        EasyJSONObject friend = (EasyJSONObject) object;
                        FriendInfo friendInfo = new FriendInfo();
                        friendInfo.memberName = friend.getSafeString("memberName");
                        friendInfo.avatarUrl = friend.getSafeString("avatar");
                        friendInfo.nickname = friend.getSafeString("nickName");

                        friendInfoList.add(friendInfo);
                    }

                    adapter.setNewData(friendInfoList);
                } catch (Exception e) {

                }
            }
        });
    }
}


