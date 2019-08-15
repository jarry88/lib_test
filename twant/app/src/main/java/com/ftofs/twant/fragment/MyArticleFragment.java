package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PostListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.log.SLog;
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
 * 我的文章Fragment
 * @author zwm
 */
public class MyArticleFragment extends BaseFragment implements View.OnClickListener {
    List<PostItem> postItemList = new ArrayList<>();
    PostListAdapter adapter;

    public static MyArticleFragment newInstance() {
        Bundle args = new Bundle();

        MyArticleFragment fragment = new MyArticleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_article, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        adapter = new PostListAdapter(R.layout.post_list_item, postItemList);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, "");

        if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "memberName", memberName);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_POST_LIST, params, new UICallback() {
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
                    EasyJSONArray wantPostList = responseObj.getArray("datas.wantPostList");
                    for (Object object : wantPostList) {
                        EasyJSONObject post = (EasyJSONObject) object;
                        PostItem item = new PostItem();

                        item.postId = post.getInt("postId");
                        item.coverImage = post.getString("coverImage");
                        item.postCategory = post.getString("postCategory");
                        item.title = post.getString("title");

                        EasyJSONObject memberVo = post.getObject("memberVo");
                        // SLog.info("memberVo[%s]", memberVo);
                        if (memberVo != null) {
                            item.authorAvatar = memberVo.getString("avatar");
                            item.authorNickname = memberVo.getString("nickName");
                        }
                        item.postLike = post.getInt("postLike");

                        postItemList.add(item);
                    }
                    adapter.setNewData(postItemList);
                } catch (Exception e) {

                }
            }
        });
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
}
