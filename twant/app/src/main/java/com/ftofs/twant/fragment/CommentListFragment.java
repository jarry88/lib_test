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
import com.ftofs.twant.adapter.CommentListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CommentItem;
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
 * 評論Fragment
 * 店鋪評論和商品評論共用一個Fragment
 * @author zwm
 */
public class CommentListFragment extends BaseFragment implements View.OnClickListener {
    int bindId;
    int commentChannel;

    List<CommentItem> commentItemList = new ArrayList<>();
    CommentListAdapter adapter;

    public static CommentListFragment newInstance(int bindId, int commentChannel) {
        Bundle args = new Bundle();

        args.putInt("bindId", bindId);
        args.putInt("commentChannel", commentChannel);
        CommentListFragment fragment = new CommentListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        bindId = args.getInt("bindId");
        commentChannel = args.getInt("commentChannel");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_publish, this);

        RecyclerView rvCommentList = view.findViewById(R.id.rv_comment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvCommentList.setLayoutManager(layoutManager);
        adapter = new CommentListAdapter(R.layout.comment_item, commentItemList);
        rvCommentList.setAdapter(adapter);

        loadCommentData(1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_publish) {

        }
    }

    private void loadCommentData(int page) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "channel", commentChannel,
                "bindId", bindId,
                "page", page);

        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_COMMENT_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONArray comments = responseObj.getArray("datas.comments");
                    for (Object object : comments) {
                        EasyJSONObject comment = (EasyJSONObject) object;
                        CommentItem item = new CommentItem();
                        item.commentId = comment.getInt("commentId");
                        item.commentChannel = comment.getInt("commentChannel");
                        item.commentType = comment.getInt("commentType");
                        item.commentLike = comment.getInt("commentLike");
                        item.content = comment.getString("content");
                        item.isLike = comment.getInt("isLike");
                        item.commentReply = comment.getInt("commentReply");

                        item.commenterAvatar = comment.getString("memberVo.avatar");
                        item.nickname = comment.getString("memberVo.nickName");
                        item.commentTime = comment.getString("commentStartTime");

                        if (item.commentType != Constant.COMMENT_TYPE_TEXT) {
                            EasyJSONArray images = comment.getArray("images");
                            if (images != null) {
                                for (Object object2 : images) {
                                    EasyJSONObject image = (EasyJSONObject) object2;
                                    item.imageUrl = image.getString("imageUrl");
                                }
                            }
                        }

                        commentItemList.add(item);
                    }

                    adapter.setNewData(commentItemList);
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }
}
