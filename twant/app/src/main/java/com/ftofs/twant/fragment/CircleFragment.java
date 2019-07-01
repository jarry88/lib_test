package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.PostCategory;
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

    LinearLayout llTabButtonContainer;

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
            MainFragment mainFragment = MainFragment.getInstance();
            mainFragment.start(AddPostFragment.newInstance());
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
                    if (responseObj == null) {
                        return;
                    }

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
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }
}
