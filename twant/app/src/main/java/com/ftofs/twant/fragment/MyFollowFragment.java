package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsSearchResultAdapter;
import com.ftofs.twant.adapter.MyFollowArticleAdapter;
import com.ftofs.twant.adapter.MyFollowMemberAdapter;
import com.ftofs.twant.adapter.MyFollowRecruitmentAdapter;
import com.ftofs.twant.adapter.MyFollowStoreAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.entity.MyFollowGoodsItem;
import com.ftofs.twant.entity.MyFollowMemberItem;
import com.ftofs.twant.entity.MyFollowStoreItem;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.entity.WantedPostItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 我的關注Fragment
 * @author zwm
 */
public class MyFollowFragment extends BaseFragment implements View.OnClickListener {
    public static final int TAB_INDEX_STORE = 0;
    public static final int TAB_INDEX_GOODS = 1;
    public static final int TAB_INDEX_ARTICLE = 2;
    public static final int TAB_INDEX_RECRUITMENT = 3;
    public static final int TAB_INDEX_MEMBER = 4;

    List<MyFollowStoreItem> myFollowStoreItemList = new ArrayList<>();
    List<MyFollowGoodsItem> myFollowGoodsItemList = new ArrayList<>();
    List<PostItem> myFollowArticleItemList = new ArrayList<>();
    List<WantedPostItem> myFollowRecruitmentItemList = new ArrayList<>();
    List<MyFollowMemberItem> myFollowMemberItemList = new ArrayList<>();

    int currTabIndex = TAB_INDEX_STORE;
    int mode = Constant.MODE_VIEW;
    boolean isCheckVisible;
    RelativeLayout rl_total_operation_container;
    TextView btnEdit;
    TextView btnDelete;
    ScaledButton btnSelectAll;

    boolean storeDataLoaded = false;
    MyFollowStoreAdapter myFollowStoreAdapter;
    boolean goodsDataLoaded = false;
    boolean articleDataLoaded = false;
    MyFollowArticleAdapter myFollowArticleAdapter;
    boolean recruitmentDataLoaded = false;
    MyFollowRecruitmentAdapter myFollowRecruitmentAdapter;
    boolean memberDataLoaded = false;
    MyFollowMemberAdapter myFollowMemberAdapter;


    RecyclerView rvMyFollowList;
    String memberName;
    private GoodsSearchResultAdapter mGoodsAdapter;
    private List<GoodsSearchItemPair> goodsItemPairList;

    public static MyFollowFragment newInstance() {
        Bundle args = new Bundle();

        MyFollowFragment fragment = new MyFollowFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static MyFollowFragment newInstance(String memberName) {
        Bundle args = new Bundle();
        args.putString("memberName", memberName);
        MyFollowFragment fragment = new MyFollowFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_follow, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceWord(view);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_edit, this);
        Util.setOnClickListener(view, R.id.btn_select_all, this);
        Util.setOnClickListener(view, R.id.btn_delete, this);
        Util.setOnClickListener(view, R.id.btn_select_all, this);
        rl_total_operation_container = view.findViewById(R.id.rl_total_operation_container);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnSelectAll =view.findViewById(R.id.btn_select_all);
        rvMyFollowList = view.findViewById(R.id.rv_my_follow_list);

        rvMyFollowList.setLayoutManager(new LinearLayoutManager(_mActivity));

        myFollowStoreAdapter = new MyFollowStoreAdapter(R.layout.my_follow_store_item, myFollowStoreItemList);
        myFollowStoreAdapter.setEmptyView(R.layout.layout_placeholder_no_favorite, rvMyFollowList);
        myFollowStoreAdapter.setOnItemClickListener((adapter, view1, position) -> {
            MyFollowStoreItem myFollowStoreItem = myFollowStoreItemList.get(position);
            if (mode == Constant.MODE_VIEW) {
                start(ShopMainFragment.newInstance(myFollowStoreItem.storeId));
            } else {
                myFollowStoreItem.switchCheck();
                myFollowStoreAdapter.notifyDataSetChanged();
            }
        });

        mGoodsAdapter = new GoodsSearchResultAdapter(_mActivity, goodsItemPairList);
        mGoodsAdapter.setEmptyView(R.layout.layout_placeholder_no_favorite, rvMyFollowList);
        goodsItemPairList = new ArrayList<>();
        mGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                int id = view.getId();
                if (mode != Constant.MODE_VIEW) {
                    if (id == R.id.btn_left_select || id == R.id.btn_right_select||id == R.id.cl_container_left || id == R.id.cl_container_right) {
                        GoodsSearchItemPair goodsSearchItemPair=goodsItemPairList.get(position);
                        if (id == R.id.btn_left_select||id == R.id.cl_container_left) {
                            goodsSearchItemPair.left.check = !goodsSearchItemPair.left.check;
                        } else {
                            goodsSearchItemPair.right.check = !goodsSearchItemPair.right.check;
                        }
                        mGoodsAdapter.notifyDataSetChanged();
                    }
                }else {
                    if (id == R.id.btn_goto_store_left || id == R.id.btn_goto_store_right) {
                        GoodsSearchItemPair pair = goodsItemPairList.get(position);
                        int storeId;
                        if (id == R.id.btn_goto_store_left) {
                            storeId = pair.left.storeId;
                        } else {
                            storeId = pair.right.storeId;
                        }


                        Util.startFragment(ShopMainFragment.newInstance(storeId));

                    } else if (id == R.id.cl_container_left || id == R.id.cl_container_right) {
                        GoodsSearchItemPair pair = goodsItemPairList.get(position);
                        int commonId;
                        if (id == R.id.cl_container_left) {
                            commonId = pair.left.commonId;
                        } else {
                            commonId = pair.right.commonId;
                        }

                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                    } else if (id == R.id.btn_play_game) {
//                    String url = Util.makeChristmasH5Url();
                        String url = Util.makeSpringH5Url();
                        SLog.info("sprint_url[%s]", url);
                        if (url == null) {
                            Util.showLoginFragment();
                            return;
                        }
                        start(H5GameFragment.newInstance(url, true));
                    } else if (id == R.id.btn_back) {
                        pop();
                    }
                }
            }
        });
        mGoodsAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                GoodsSearchItemPair pair = goodsItemPairList.get(position);
                int itemType = pair.getItemType();
                if (itemType == Constant.ITEM_TYPE_NORMAL) {
                    return 1;
                } else if (itemType == Constant.ITEM_TYPE_LOAD_END_HINT || itemType == Constant.ITEM_TYPE_BANNER) {
                    return 2;
                }
                return 1;
            }
        });


        myFollowArticleAdapter = new MyFollowArticleAdapter(R.layout.my_follow_article_item, myFollowArticleItemList);
        myFollowArticleAdapter.setEmptyView(R.layout.layout_placeholder_no_favorite, rvMyFollowList);
        myFollowArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostItem item = myFollowArticleItemList.get(position);
                if(mode == Constant.MODE_VIEW){
                    start(PostDetailFragment.newInstance(item.postId));
                }
                else {
                    item.switchCheck();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        myFollowRecruitmentAdapter = new MyFollowRecruitmentAdapter(R.layout.my_follow_recruitment_item, myFollowRecruitmentItemList);
        myFollowRecruitmentAdapter.setEmptyView(R.layout.layout_placeholder_no_favorite, rvMyFollowList);
        myFollowRecruitmentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mode == Constant.MODE_VIEW){
                    int id = view.getId();
                    if (id == R.id.btn_expand) {
                        WantedPostItem wantedPostItem = myFollowRecruitmentItemList.get(position);
                        wantedPostItem.isJobDescExpanded = !wantedPostItem.isJobDescExpanded;

                        adapter.notifyItemChanged(position);
                    }
                }
                else{
                    WantedPostItem wantedPostItem = myFollowRecruitmentItemList.get(position);
                    wantedPostItem.switchCheck();
                    myFollowRecruitmentAdapter.notifyDataSetChanged();
                }

            }
        });

        myFollowMemberAdapter = new MyFollowMemberAdapter(R.layout.my_follow_member_item, myFollowMemberItemList);
        myFollowMemberAdapter.setEmptyView(R.layout.layout_placeholder_no_favorite, rvMyFollowList);
        myFollowMemberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyFollowMemberItem item = myFollowMemberItemList.get(position);
                if (mode == Constant.MODE_VIEW){
                    start(MemberInfoFragment.newInstance(item.memberName));
                } else {
                    item.switchCheck();
                    myFollowMemberAdapter.notifyDataSetChanged();
                }

            }
        });

        SimpleTabManager simpleTabManager = new SimpleTabManager(TAB_INDEX_STORE) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                if (isRepeat) {
                    return;
                }
                btnSelectAll.setChecked(false);
                mode = Constant.MODE_EDIT;
                switchMode();
                int id = v.getId();
                if (id == R.id.btn_store) {
                    currTabIndex = TAB_INDEX_STORE;
                    if (storeDataLoaded) {
                        rvMyFollowList.setAdapter(myFollowStoreAdapter);
                    } else {
                        loadMyFollowStore();
                    }

                } else if (id == R.id.btn_goods) {
                    currTabIndex = TAB_INDEX_GOODS;
                    if (goodsDataLoaded) {
                        rvMyFollowList.setAdapter(mGoodsAdapter);
                    } else {
                        loadMyFollowGoods();
                    }
                } else if (id == R.id.btn_article) {
                    currTabIndex = TAB_INDEX_ARTICLE;
                    if (articleDataLoaded) {
                        rvMyFollowList.setAdapter(myFollowArticleAdapter);
                    } else {
                        loadMyFollowArticle();
                    }
                } else if (id == R.id.btn_recruitment) {
                    currTabIndex = TAB_INDEX_RECRUITMENT;
                    if (recruitmentDataLoaded) {
                        rvMyFollowList.setAdapter(myFollowRecruitmentAdapter);
                    } else {
                        loadMyFollowRecruitment();
                    }
                } else if (id == R.id.btn_member) {
                    currTabIndex = TAB_INDEX_MEMBER;
                    if (memberDataLoaded) {
                        rvMyFollowList.setAdapter(myFollowMemberAdapter);
                    } else {
                        loadMyFollowMember();
                    }
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_store));
        simpleTabManager.add(view.findViewById(R.id.btn_goods));
        simpleTabManager.add(view.findViewById(R.id.btn_article));
        simpleTabManager.add(view.findViewById(R.id.btn_recruitment));
        simpleTabManager.add(view.findViewById(R.id.btn_member));

        loadMyFollowStore();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
        if (id == R.id.btn_edit) {
            switchMode();
        }
        if (id == R.id.btn_select_all){
            boolean allCheck =!btnSelectAll.isChecked();
            btnSelectAll.setChecked(allCheck);
            for (MyFollowStoreItem item:myFollowStoreItemList) {
                item.check = allCheck;
            }
            myFollowStoreAdapter.notifyDataSetChanged();
            for (GoodsSearchItemPair item:goodsItemPairList){
                item.left.check =allCheck;
                item.right.check =allCheck;
            }
            mGoodsAdapter.notifyDataSetChanged();
            for (PostItem item: myFollowArticleItemList){
                item.check = allCheck;
            }
            myFollowArticleAdapter.notifyDataSetChanged();
            for (WantedPostItem item : myFollowRecruitmentItemList){
                item.check = allCheck;
            }
            myFollowRecruitmentAdapter.notifyDataSetChanged();
            for (MyFollowMemberItem item : myFollowMemberItemList){
                item.check = allCheck;
            }
            myFollowMemberAdapter.notifyDataSetChanged();

        }
        if (id == R.id.btn_delete){
            switch (currTabIndex){
                case TAB_INDEX_STORE:
                    multiStoreDelete();break;
                case TAB_INDEX_GOODS:
                    multiGoodsDelete();break;
                case  TAB_INDEX_ARTICLE:
                    multiArticleDelete();
                    break;
                case TAB_INDEX_RECRUITMENT:
                    multiRecruitmentDelete();
                    break;
                case TAB_INDEX_MEMBER:
                    multiMemberDelete();
                    break;
            }
        }
    }
    private void multiStoreDelete(){
        if (currTabIndex == TAB_INDEX_STORE) {
            try{
                EasyJSONObject params = EasyJSONObject.generate("token",User.getToken());
                StringBuilder storeIds = new StringBuilder();
                for (MyFollowStoreItem item:myFollowStoreItemList) {
                    if (item.check) {
                        if (storeIds.length()!=0){
                            storeIds.append(",");
                        }
                        storeIds.append(item.storeId);
                    }
                }
                if (storeIds.length()==0){
                    SLog.info("無需發送刪除請求");
                    return;
                }
                params.set("storeIds", storeIds.toString());
                Api.postUI(Api.PATH_STORE_FOLLOW_MULTI_DELETE, params, new UICallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showNetworkError(_mActivity,e);
                    }

                    @Override
                    public void onResponse(Call call, String responseStr) throws IOException {
                        SLog.info("responseStr[%s]",responseStr);
                        try {

                            EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                            Iterator<MyFollowStoreItem> storeItr = myFollowStoreItemList.iterator();
                            while (storeItr.hasNext()) {
                                if (storeItr.next().check) {
                                    storeItr.remove();
                                }
                            }
                            myFollowStoreAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            SLog.info("EastJSON[%s]",e);
                        }
                    }
                });
            }catch (Exception e){
                SLog.info("Error![%s]",e);
            }
        }
    }

    private void multiGoodsDelete(){
        if(currTabIndex ==TAB_INDEX_GOODS){
            try {
                StringBuilder commonIds = new StringBuilder();
                for (GoodsSearchItemPair item:goodsItemPairList){
                    if (item.left.check){
                        if (commonIds.length()>0){
                            commonIds.append(",");
                        }
                        commonIds.append(item.left.commonId);
                    }
                    if (item.right.check){
                        if (commonIds.length()>0){
                            commonIds.append(",");
                        }
                        commonIds.append(item.right.commonId);
                    }
                }
                if (commonIds.length()>0){
                    EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
                    params.set("commonIds", commonIds);

                    Api.postUI(Api.PATH_GOODS_FOLLOW_MULTI_DELETE, params, new UICallback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ToastUtil.showNetworkError(_mActivity,e);
                        }

                        @Override
                        public void onResponse(Call call, String responseStr) throws IOException {
                            SLog.info("responseStr[%s]",responseStr);
                            try {

                                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                                int i = 0;
                                boolean j = true;
                                List<GoodsSearchItem> itemList = new ArrayList<>();
                                for (GoodsSearchItemPair pair : goodsItemPairList) {
                                    if (!pair.left.check) {
                                        itemList.add(pair.left);
                                    }
                                    if (!pair.right.check) {
                                        itemList.add(pair.right);
                                    }
                                }
                                goodsItemPairList.clear();
                                GoodsSearchItemPair pair=null;
                                for (GoodsSearchItem item : itemList) {
                                    if (pair == null) {
                                        pair = new GoodsSearchItemPair(Constant.ITEM_TYPE_NORMAL);
                                        pair.left = item;
                                    } else {
                                        pair.right = item;
                                        goodsItemPairList.add(pair);
                                        pair = null;
                                    }
                                }
                                mGoodsAdapter.notifyDataSetChanged();
                            }catch (Exception e){
                                SLog.info("EastJSON[%s]",e);
                            }
                        }
                    });
                }


            }catch (Exception e){
                SLog.info("Error![%s]",e);
            }
        }
    }

    private void multiArticleDelete() {
        if (currTabIndex == TAB_INDEX_ARTICLE) {
            try {
                StringBuilder postIds = new StringBuilder();
                for (PostItem item:myFollowArticleItemList){
                    if (item.check){
                        if (postIds.length()>0){
                            postIds.append(",");
                        }
                        postIds.append(item.postId);
                    }
                }
                if (postIds.length()>0){
                    EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
                    params.set("postIds", postIds);

                    Api.postUI(Api.PATH_ARTICAL_FOLLOW_MULTI_DELETE, params, new UICallback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ToastUtil.showNetworkError(_mActivity,e);
                        }

                        @Override
                        public void onResponse(Call call, String responseStr) throws IOException {
                            SLog.info("responseStr[%s]",responseStr);
                            try {

                                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                                Iterator<PostItem> postItr = myFollowArticleItemList.iterator();
                                while (postItr.hasNext()) {
                                    if (postItr.next().check) {
                                        postItr.remove();
                                    }
                                }
                                myFollowArticleAdapter.notifyDataSetChanged();
                            }catch (Exception e){
                                SLog.info("EastJSON[%s]",e);
                            }
                        }
                    });
                }


            }catch (Exception e){
                SLog.info("Error![%s]",e);
            }
        }
    }

    private void multiRecruitmentDelete() {
        if (currTabIndex == TAB_INDEX_RECRUITMENT) {
            try {
                StringBuilder postIds = new StringBuilder();
                for (WantedPostItem item:myFollowRecruitmentItemList){
                    if (item.check){
                        if (postIds.length()>0){
                            postIds.append(",");
                        }
                        postIds.append(item.postId);
                    }
                }
                if (postIds.length()>0){
                    EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
                    params.set("postIds", postIds);

                    Api.postUI(Api.PATH_HRPOST_FOLLOW_MULTI_DELETE, params, new UICallback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ToastUtil.showNetworkError(_mActivity,e);
                        }

                        @Override
                        public void onResponse(Call call, String responseStr) throws IOException {
                            SLog.info("responseStr[%s]",responseStr);
                            try {

                                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                                Iterator<WantedPostItem> postItr = myFollowRecruitmentItemList.iterator();
                                while (postItr.hasNext()) {
                                    if (postItr.next().check) {
                                        postItr.remove();
                                    }
                                }
                                SLog.info("EastJSON[招聘取消關注]");
                                myFollowRecruitmentAdapter.notifyDataSetChanged();
                            }catch (Exception e){
                                SLog.info("EastJSON[%s]",e);
                            }
                        }
                    });
                }


            }catch (Exception e){
                SLog.info("Error![%s]",e);
            }
        }
    }

    private void multiMemberDelete() {
        if (currTabIndex == TAB_INDEX_MEMBER) {
            try {
                StringBuilder followMembers = new StringBuilder();
                for (MyFollowMemberItem item:myFollowMemberItemList){
                    if (item.check){
                        if (followMembers.length()>0){
                            followMembers.append(",");
                        }
                        followMembers.append(item.memberName);
                    }
                }
                if (followMembers.length()>0){
                    EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
                    params.set("followMembers", followMembers);

                    Api.postUI(Api.PATH_MEMBER_FOLLOW_MULTI_DELETE, params, new UICallback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ToastUtil.showNetworkError(_mActivity,e);
                        }

                        @Override
                        public void onResponse(Call call, String responseStr) throws IOException {
                            SLog.info("responseStr[%s]",responseStr);
                            try {

                                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                                Iterator<MyFollowMemberItem> memberItr = myFollowMemberItemList.iterator();
                                while (memberItr.hasNext()) {
                                    if (memberItr.next().check) {
                                        memberItr.remove();
                                    }
                                }
                                myFollowMemberAdapter.notifyDataSetChanged();
                            }catch (Exception e){
                                SLog.info("EastJSON[%s]",e);
                            }
                        }
                    });
                }


            }catch (Exception e){
                SLog.info("Error![%s]",e);
            }
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void loadMyFollowStore() {
        try {
            //String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);
            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

            Api.postUI(Api.PATH_MY_FOLLOW_STORE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray storeList = responseObj.getSafeArray("datas.sotreList");  // sotreList拼寫錯誤

                        for (Object object : storeList) {
                            EasyJSONObject store = (EasyJSONObject) object;

                            MyFollowStoreItem myFollowStoreItem = new MyFollowStoreItem();
                            myFollowStoreItem.storeId = store.getInt("storeVo.storeId");
                            myFollowStoreItem.storeAvatarUrl = store.getSafeString("storeVo.storeAvatarUrl");
                            myFollowStoreItem.storeName = store.getSafeString("storeVo.storeName");
                            myFollowStoreItem.chainAreaInfo = store.getSafeString("storeVo.chainAreaInfo");
                            myFollowStoreItem.distance = store.getSafeString("storeVo.distance");
                            myFollowStoreItem.collectCount = store.getInt("storeVo.likeCount");
                            myFollowStoreItem.storeFigureImageUrl = store.getSafeString("storeVo.storeFigureImageUrl");
                            myFollowStoreItem.className = store.getSafeString("storeVo.className").split(",")[0];


                            myFollowStoreItemList.add(myFollowStoreItem);
                        }
                        SLog.info("ITEM_COUNT[%d]", myFollowStoreItemList.size());
                        storeDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_STORE) {
                            rvMyFollowList.setAdapter(myFollowStoreAdapter);
                        }
                        myFollowStoreAdapter.setNewData(myFollowStoreItemList);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void loadMyFollowGoods() {
        try {
            //String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);
            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }


            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

            Api.postUI(Api.PATH_MY_FOLLOW_GOODS, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray goodsList = responseObj.getSafeArray("datas.goodsList");
                        GoodsSearchItemPair pair = null;
                        for (Object object : goodsList) {
                            EasyJSONObject goods = (EasyJSONObject) object;
                            EasyJSONObject goodsCommon = goods.getSafeObject("goodsCommon");

                            MyFollowGoodsItem good = new MyFollowGoodsItem();
                            good.storeAvatarUrl = goods.getSafeString("storeVo.storeAvatarUrl");
                            good.commonId = goods.getInt("commonId");
                            good.goodsName = goods.getSafeString("goodsName");
                            good.storeId = goods.getInt("storeVo.storeId");
                            good.storeName = goods.getSafeString("storeVo.storeName");
                            good.imageSrc = goodsCommon.getSafeString("imageSrc");
                            good.jingle = goodsCommon.getSafeString("jingle");
                            good.goodsFavorite = goodsCommon.getInt("goodsFavorite");
                            good.price = Util.getSpuPrice(goodsCommon);
                            good.goodsModel = StringUtil.safeModel(goods);
                            SLog.info("goodsCommon[%s]",goodsCommon.toString());
                            //String nationalFlag = StringUtil.normalizeImageUrl(goodsCommon.getString("adminCountry.nationalFlag"));

                            GoodsSearchItem goodsSearchItem = new GoodsSearchItem(good.imageSrc,good.storeAvatarUrl,good.storeId,good.storeName,good.commonId,good.goodsName,good.jingle,good.price,null);
                            goodsSearchItem.goodsModel = good.goodsModel;
                            if (pair == null) {
                                pair = new GoodsSearchItemPair(Constant.ITEM_TYPE_NORMAL);
                                pair.left = goodsSearchItem;
                            } else {
                                pair.right = goodsSearchItem;
                                goodsItemPairList.add(pair);
                                pair = null;
                            }
                        }
                        SLog.info("ITEM_COUNT[%d]", myFollowGoodsItemList.size());
                        goodsDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_GOODS) {
                            rvMyFollowList.setAdapter(mGoodsAdapter);
                        }
                        mGoodsAdapter.setNewData(goodsItemPairList);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void loadMyFollowArticle() {
        try {
            //String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);
            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }


            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_MY_FOLLOW_POST, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray wantPostList = responseObj.getSafeArray("datas.wantPostList");
                        for (Object object : wantPostList) {
                            EasyJSONObject wantPost = (EasyJSONObject) object;

                            PostItem postItem = new PostItem();
                            postItem.itemType = PostItem.POST_TYPE_MY_FOLLOW;
                            postItem.postId = wantPost.getInt("postId");
                            postItem.coverImage = wantPost.getSafeString("coverImage");
                            postItem.postCategory = wantPost.getSafeString("postCategory");
                            postItem.title = wantPost.getSafeString("title");
                            postItem.authorAvatar = wantPost.getSafeString("memberVo.avatar");
                            postItem.authorNickname = wantPost.getSafeString("memberVo.nickName");
                            postItem.postFollow = wantPost.getInt("postFavor");
                            postItem.comeTrueState = wantPost.getInt("comeTrueState");
                            postItem.isDelete = wantPost.getInt("isDelete");

                            myFollowArticleItemList.add(postItem);
                        }
                        SLog.info("ITEM_COUNT[%d]", myFollowMemberItemList.size());
                        articleDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_ARTICLE) {
                            rvMyFollowList.setAdapter(myFollowArticleAdapter);
                        }
                        myFollowArticleAdapter.setNewData(myFollowArticleItemList);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void loadMyFollowRecruitment() {
        try {
            //String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);
            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }


            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_MY_FOLLOW_RECRUITMENT, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray hrPostVoList = responseObj.getSafeArray("datas.hrPostVoList");
                        for (Object object : hrPostVoList) {
                            EasyJSONObject hrPostVo = (EasyJSONObject) object;

                            WantedPostItem wantedPostItem = new WantedPostItem();
                            wantedPostItem.postId = hrPostVo.getInt("postId");
                            wantedPostItem.postTitle = hrPostVo.getSafeString("postTitle");
                            wantedPostItem.postType = hrPostVo.getSafeString("postType");
                            wantedPostItem.postArea = hrPostVo.getSafeString("postArea");
                            wantedPostItem.salaryType = hrPostVo.getSafeString("salaryType");
                            wantedPostItem.salaryRange = hrPostVo.getSafeString("salaryRange");
                            wantedPostItem.currency = hrPostVo.getSafeString("currency");
                            wantedPostItem.mailbox = hrPostVo.getSafeString("mailbox");
                            wantedPostItem.postDescription = hrPostVo.getSafeString("postDescription");

                            myFollowRecruitmentItemList.add(wantedPostItem);
                        }
                        SLog.info("ITEM_COUNT[%d]", myFollowMemberItemList.size());
                        recruitmentDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_RECRUITMENT) {
                            rvMyFollowList.setAdapter(myFollowRecruitmentAdapter);
                        }
                        myFollowRecruitmentAdapter.setNewData(myFollowRecruitmentItemList);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {

        }
    }
    private void loadMyFollowMember() {
        try {
            //String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);
            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }


            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_MY_FOLLOW_MEMBER, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray memberVoList = responseObj.getSafeArray("datas.memberVoList");
                        for (Object object : memberVoList) {
                            EasyJSONObject memberVo = (EasyJSONObject) object;

                            MyFollowMemberItem myFollowMemberItem = new MyFollowMemberItem();
                            myFollowMemberItem.memberName = memberVo.getSafeString("memberName");
                            myFollowMemberItem.nickname = memberVo.getSafeString("nickName");
                            myFollowMemberItem.avatarUrl = memberVo.getSafeString("avatar");
                            myFollowMemberItem.memberSignature = memberVo.getSafeString("memberSignature");
                            myFollowMemberItem.level = memberVo.getSafeString("currGrade.gradeName");

                            myFollowMemberItemList.add(myFollowMemberItem);
                        }
                        SLog.info("ITEM_COUNT[%d]", myFollowMemberItemList.size());
                        memberDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_MEMBER) {
                            rvMyFollowList.setAdapter(myFollowMemberAdapter);
                        }
                        myFollowMemberAdapter.setNewData(myFollowMemberItemList);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {

        }
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        isCheckVisible = false;
        rl_total_operation_container.setVisibility(View.GONE);
    }



    private void replaceWord(View v) {
        memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
        if (getArguments().containsKey("memberName")) {
            if (!memberName.equals(getArguments().getString("memberName"))) {
                memberName = getArguments().getString("memberName");
                ((TextView) v.findViewById(R.id.tv_fragment_title)).setText(getString(R.string.text_him_follow));
            }
        }
    }

    private void switchMode() {
        if (mode == Constant.MODE_VIEW) {
            rl_total_operation_container.setVisibility(View.VISIBLE);
            isCheckVisible = true;
            btnEdit.setText(getResources().getString(R.string.text_finish));
            btnEdit.setTextColor(getResources().getColor(R.color.tw_red, null));
            mode = Constant.MODE_EDIT;



        } else {
            rl_total_operation_container.setVisibility(View.GONE);
            isCheckVisible = false;
            btnEdit.setText(getResources().getString(R.string.text_edit));
            btnEdit.setTextColor(getResources().getColor(R.color.tw_black, null));
            mode = Constant.MODE_VIEW;
        }

        switch (currTabIndex){
            case TAB_INDEX_STORE:
                myFollowStoreAdapter.setMode(mode);
                break;
            case TAB_INDEX_GOODS:
                mGoodsAdapter.setMode(mode);
                break;
            case TAB_INDEX_ARTICLE:
                myFollowArticleAdapter.setMode(mode);
            case TAB_INDEX_RECRUITMENT:
                myFollowRecruitmentAdapter.setMode(mode);
                break;
            case TAB_INDEX_MEMBER:
                myFollowMemberAdapter.setMode(mode);
                break;
            default:break;
        }

    }

}
//    private void setCheckButtonOnClickListener(View checkButton) {
//        checkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ScaledButton btnCheck = (ScaledButton) v;
//                BaseStatus status = (BaseStatus) btnCheck.getTag();
//                status.changeCheckStatus(!status.isChecked(), BaseStatus.PHRASE_TARGET);
//                updateTotalData();
//            }
//        });
//    }
//
//    /**
//     * 更新合計數據
//     */
//    private void updateTotalData() {
//        Pair<Float, Integer> totalData = totalStatus.getTotalData();
//
//        float totalPrice = totalData.first;  // 總價錢
//        int totalCount = totalData.second;  // 總件數
//        tvTotalPrice.setText(StringUtil.formatPrice(_mActivity, totalPrice, 0));
//        String btnSettlementText = textSettlement;
//
//        if (totalCount > 0) {
//            btnSettlementText += "(" + totalCount + ")";
//        }
//        btnSettlement.setText(btnSettlementText);
//    }
//}
