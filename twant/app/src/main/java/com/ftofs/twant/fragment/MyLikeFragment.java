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
import com.ftofs.twant.adapter.MyLikeStoreAdapter;
import com.ftofs.twant.adapter.TrustValueListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.MyLikeStoreItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 我的點贊Fragment
 * @author zwm
 */
public class MyLikeFragment extends BaseFragment implements View.OnClickListener {
    List<MyLikeStoreItem> myLikeStoreItemList = new ArrayList<>();

    MyLikeStoreAdapter myLikeStoreAdapter;

    RecyclerView rvMyLikeList;

    public static MyLikeFragment newInstance() {
        Bundle args = new Bundle();

        MyLikeFragment fragment = new MyLikeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_like, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleTabManager simpleTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                if (isRepeat) {
                    return;
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_store));
        simpleTabManager.add(view.findViewById(R.id.btn_goods));
        simpleTabManager.add(view.findViewById(R.id.btn_article));

        rvMyLikeList = view.findViewById(R.id.rv_my_like_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvMyLikeList.setLayoutManager(layoutManager);
        myLikeStoreAdapter = new MyLikeStoreAdapter(R.layout.my_like_store_item, myLikeStoreItemList);
        rvMyLikeList.setAdapter(myLikeStoreAdapter);

        loadMyLikeStore();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void loadMyLikeStore() {
        try {
            String memberName = User.getMemberName();
            if (StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);
            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }


            final BasePopupView loadingPopup = new XPopup.Builder(getContext())
                    .asLoading(getString(R.string.text_loading))
                    .show();

            Api.postUI(Api.PATH_MY_LIKE_STORE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray storeList = responseObj.getArray("datas.storeList");
                        for (Object object : storeList) {
                            EasyJSONObject store = (EasyJSONObject) object;

                            MyLikeStoreItem myLikeStoreItem = new MyLikeStoreItem();
                            myLikeStoreItem.storeId = store.getInt("storeVo.storeId");
                            myLikeStoreItem.storeAvatar = store.getString("storeVo.storeAvatarUrl");
                            myLikeStoreItem.storeName = store.getString("storeVo.storeName");
                            myLikeStoreItem.storeAddress = store.getString("storeVo.chainAreaInfo");
                            myLikeStoreItem.storeDistanceStr = store.getString("storeVo.distance");
                            myLikeStoreItem.likeCount = store.getInt("storeVo.likeCount");

                            myLikeStoreItemList.add(myLikeStoreItem);
                        }
                        myLikeStoreAdapter.setNewData(myLikeStoreItemList);
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }
}
