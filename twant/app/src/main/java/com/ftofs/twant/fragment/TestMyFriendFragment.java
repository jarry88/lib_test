package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.MyFriendListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.MyFriendListItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class TestMyFriendFragment extends BaseFragment implements View.OnClickListener{
    List<MyFriendListItem> myFriendListItems = new ArrayList<>();
    MyFriendListAdapter adapter;

    public static TestMyFriendFragment newInstance(){
        Bundle args = new Bundle();
        TestMyFriendFragment fragment = new TestMyFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friend,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.btn_menu,this);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity,LinearLayoutManager.VERTICAL,false);
        rvList.setLayoutManager(layoutManager);
        adapter = new MyFriendListAdapter(R.layout.my_friend_list_item,myFriendListItems);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String memberName = myFriendListItems.get(position).memberName;
                start(MemberInfoFragment.newInstance(memberName));

                int id = view.getId();
                if(id == R.id.btn_delete) {
                    new XPopup.Builder(_mActivity)
                            .setPopupCallback(new XPopupCallback() {
                                @Override
                                public void onShow() {

                                }

                                @Override
                                public void onDismiss() {

                                }
                            }).asCustom(new TwConfirmPopup(_mActivity, "你確定要刪？", null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("Yes");
                            deleteFriend(position,memberName);
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    })).show();
                } else if(id == R.id.img_avatar){
                    start(MemberInfoFragment.newInstance(memberName));
                }
            }
        });
        rvList.setAdapter(adapter);
        loadDate();
    }

    private void loadDate(){
        String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME,"");
        String token = User.getToken();

        if(StringUtil.isEmpty(token)||StringUtil.isEmpty(memberName)){
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "memberName", memberName,
                "token",token);
        //SLog.info("params%s",params.toString());
        Api.postUI(Api.PATH_MY_FRIEND_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]",responseStr);
                    EasyJSONObject responseObj =EasyJSONObject.parse(responseStr);
                    if(ToastUtil.checkError(_mActivity,responseObj))
                        return;
                    EasyJSONArray friendsList = responseObj.getSafeArray("datas.friendsList");

                    for(Object object:friendsList){
                        EasyJSONObject friend = (EasyJSONObject)object;
                        MyFriendListItem item = new MyFriendListItem(
                                friend.getSafeString("memberName"),
                                friend.getSafeString("avatar"),
                                friend.getSafeString("nickName"),
                                friend.getInt("currGrade.gradeLevel"),
                                friend.getSafeString("memberSignature"));
                        myFriendListItems.add(item);
                    }
                    adapter.setNewData(myFriendListItems);

                }catch (Exception e){
                    SLog.info("error%s",e.getMessage());
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
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.btn_back){
            hideSoftInputPop();
        }else if(id==R.id.btn_menu){
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity,11))
                    .offsetY(-Util.dip2px(_mActivity,7))
                    .hasShadowBg(false)
                    .atView(view)
                    .asCustom(new BlackDropdownMenu(_mActivity,this,BlackDropdownMenu.TYPE_HOME_AND_MY))
                    .show();
        }
    }

    private void deleteFriend(int position,String memberName){
        String token = User.getToken();
        if(StringUtil.isEmpty(token)){
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token",token,
                "friendMemberName",memberName);
        SLog.info("params%s",params);
        Api.postUI(Api.PATH_DELETE_FRIEND, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try{
                    SLog.info("responseStr",responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if(ToastUtil.checkError(_mActivity,responseObj)){
                        return;
                    }
                    myFriendListItems.remove(position);
                    adapter.remove(position);
                    ToastUtil.success(_mActivity,"刪了嘿！");
                }catch (Exception e){

                }
            }
        });

    }
}
