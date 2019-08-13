package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PostListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.PostCategory;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabButton;
import com.ftofs.twant.widget.SimpleTabManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 想要圈
 * @author zwm
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener {
    List<PostCategory> postCategoryList = new ArrayList<>();
    List<PostItem> postItemList = new ArrayList<>();

    LinearLayout llTabButtonContainer;

    PostListAdapter adapter;
    public static CircleFragment newInstance() {
        Bundle args = new Bundle();

        CircleFragment fragment = new CircleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llTabButtonContainer = view.findViewById(R.id.ll_tab_button_container);

        Util.setOnClickListener(view, R.id.btn_add_post, this);

        RecyclerView rvPostList = view.findViewById(R.id.rv_post_list);
        adapter = new PostListAdapter(R.layout.post_list_item, postItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostItem postItem = postItemList.get(position);
                Util.startFragment(PostDetailFragment.newInstance(postItem.postId));
            }
        });
        GridLayoutManager layoutManagerCommodity = new GridLayoutManager(_mActivity, 2);
        layoutManagerCommodity.setOrientation(GridLayoutManager.VERTICAL);
        rvPostList.setLayoutManager(layoutManagerCommodity);
        rvPostList.setAdapter(adapter);


        // 添加前兩個固定的Item
        PostCategory item = new PostCategory();
        item.categoryId = -1;
        item.categoryName = "全部";
        postCategoryList.add(item);

        item = new PostCategory();
        item.categoryId = -2;
        item.categoryName = "關注";
        postCategoryList.add(item);

        loadPostData(1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_add_post) {
            Util.startFragment(AddPostFragment.newInstance());
        }
    }

    private void loadPostData(int page) {
        EasyJSONObject params = EasyJSONObject.generate(
                "page", page
        );

        Api.postUI(Api.PATH_SEARCH_POST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    // 如果未初始化，則初始化分類菜單
                    if (postCategoryList.size() <= 2) {
                        int twBlue = getResources().getColor(R.color.tw_blue, null);
                        EasyJSONArray wantPostCategoryList = responseObj.getArray("datas.wantPostCategoryList");



                        for (Object object : wantPostCategoryList) {
                            PostCategory postCategory = (PostCategory) EasyJSONBase.jsonDecode(PostCategory.class, object.toString());
                            postCategoryList.add(postCategory);
                        }

                        SimpleTabManager tabManager = new SimpleTabManager(0) {
                            @Override
                            public void onClick(View v) {
                                boolean isRepeat = onSelect(v);
                                int id = v.getId();
                                SLog.info("id[%d]", id);


                            }
                        };
                        for (PostCategory postCategory : postCategoryList) {
                            SimpleTabButton button = new SimpleTabButton(_mActivity);
                            button.setId(postCategory.categoryId);
                            button.setText(postCategory.categoryName);
                            button.setSelectedColor(twBlue);
                            ViewGroup.MarginLayoutParams layoutParams =
                                    new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.leftMargin = Util.dip2px(_mActivity, 10);
                            layoutParams.rightMargin = Util.dip2px(_mActivity, 10);
                            llTabButtonContainer.addView(button, layoutParams);


                            tabManager.add(button);
                        }
                    }

                    EasyJSONArray wantPostList = responseObj.getArray("datas.wantPostList");
                    for (Object object : wantPostList) {
                        EasyJSONObject post = (EasyJSONObject) object;
                        PostItem item = new PostItem();

                        item.postId = post.getInt("postId");
                        item.coverImage = post.getString("coverImage");
                        item.postCategory = post.getString("postCategory");
                        item.title = post.getString("title");

                        item.authorAvatar = post.getString("memberVo.avatar");
                        item.authorNickname = post.getString("memberVo.nickName");
                        item.postLike = post.getInt("postLike");

                        postItemList.add(item);
                    }


                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }
}
