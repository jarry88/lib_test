package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.MyFriendListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.MyFriendListItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 我的好友Fragment
 * @author zwm
 */
public class MyFriendFragment extends BaseFragment implements View.OnClickListener {
    List<MyFriendListItem> myFriendListItems = new ArrayList<>();
    MyFriendListAdapter adapter;
    String memberName;
    TextView tv_fragment_title;

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ((MainActivity) getActivity()).setMessageFragmentsActivity(false);

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ((MainActivity) getActivity()).setMessageFragmentsActivity(true);

    }

    public static MyFriendFragment newInstance() {
        Bundle args = new Bundle();

        MyFriendFragment fragment = new MyFriendFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static MyFriendFragment newInstance(String memberName) {
        Bundle args = new Bundle();
        args.putString("memberName",memberName);
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

        Util.setOnClickListener(view, R.id.btn_my_like, this);
        Util.setOnClickListener(view, R.id.btn_my_comment, this);
        Util.setOnClickListener(view, R.id.btn_my_follow, this);
        Util.setOnClickListener(view, R.id.btn_message_search, this);

        Util.setOnClickListener(view, R.id.btn_menu, this);

        tv_fragment_title = view.findViewById(R.id.tv_fragment_title);
        replaceWord(view);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new MyFriendListAdapter(R.layout.my_friend_list_item, myFriendListItems);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String memberName = myFriendListItems.get(position).memberName;

                int id = view.getId();
                if (id == R.id.btn_delete_friend) {
                    new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                            // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                           .asCustom(new TwConfirmPopup(_mActivity, "您確定要刪除好友嗎?", null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                            deleteFriend(position, memberName);
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    })).show();
                } else if (id == R.id.ll_swipe_content) {
                    if(memberName.equals(User.getUserInfo(SPField.FIELD_MEMBER_NAME,null))){
                        start(PersonalInfoFragment.newInstance());
                    }else{
                        start(MemberInfoFragment.newInstance(memberName));
                    }
                }
            }
        });
        rvList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "memberName", memberName,
                "token", token);

        Api.postUI(Api.PATH_MY_FRIEND_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONArray friendsList = responseObj.getSafeArray("datas.friendsList");

                    for (Object object : friendsList) {
                        EasyJSONObject friend = (EasyJSONObject) object;

                        MyFriendListItem item = new MyFriendListItem(
                                friend.getSafeString("memberName"),
                                friend.getSafeString("avatar"),
                                friend.getSafeString("nickName"),
                                friend.getInt("currGrade.gradeLevel"),
                                friend.getSafeString("memberSignature"));

                        myFriendListItems.add(item);
                    }

                    adapter.setNewData(myFriendListItems);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });

        adapter.setNewData(myFriendListItems);
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if(id == R.id.btn_my_like) {
            Util.startFragment(MyLikeFragment.newInstance(memberName));
        } else if(id == R.id.btn_my_comment) {
            Util.startFragment(MyCommentFragment.newInstance(memberName));
        } else if(id == R.id.btn_my_follow) {
            Util.startFragment(MyFollowFragment.newInstance(memberName));
        } else if (id == R.id.btn_menu) {
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 11))
                    .offsetY(-Util.dip2px(_mActivity, 8))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_HOME_AND_MY))
                    .show();
        } else if (id == R.id.btn_message_search) {
            Util.startFragment(SearchFriendFragment.newInstance());
        }
    }

    private void deleteFriend(int position, String memberName) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "friendMemberName", memberName);

        SLog.info("params[%s]", params.toString());
        Api.postUI(Api.PATH_DELETE_FRIEND, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    myFriendListItems.remove(position);
                    adapter.notifyItemRemoved(position);
                    ToastUtil.success(_mActivity, "刪除成功");
                } catch (Exception e) {

                }
            }
        });
    }
    private void replaceWord(View v){
        memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME,null);
        if(getArguments().containsKey("memberName")){
            if(!memberName.equals(getArguments().getString("memberName"))){
                ((TextView) v.findViewById(R.id.tv_fragment_title)).setText(getString(R.string.text_him_friend));
                memberName = getArguments().getString("memberName");
            }
        }

    }
}
