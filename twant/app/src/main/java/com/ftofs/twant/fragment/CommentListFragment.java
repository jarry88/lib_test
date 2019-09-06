package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommentListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.domain.member.Member;
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
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 評論Fragment
 * 店鋪評論和商品評論共用一個Fragment
 * @author zwm
 */
public class CommentListFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    int bindId;

    /**
     * 渠道 1全部 2店鋪 3商品 4貼文 5推文
     */
    int commentChannel;

    // 當前第幾頁
    int currPage = 0;
    // 是否還有數據
    boolean hasMore = false;

    RecyclerView rvCommentList;
    List<CommentItem> commentItemList = new ArrayList<>();
    CommentListAdapter adapter;

    /**
     * 構造方法
     * @param bindId 根據commentChannel來決定是店鋪評論、商品評論或是貼文評論
     * @param commentChannel  渠道 1全部 2店鋪 3商品 4貼文 5推文
     * @return
     */
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

        rvCommentList = view.findViewById(R.id.rv_comment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvCommentList.setLayoutManager(layoutManager);
        adapter = new CommentListAdapter(R.layout.comment_item, commentItemList);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvCommentList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommentItem commentItem = commentItemList.get(position);
                int id = view.getId();

                if (!User.isLogin()) {
                    Util.showLoginFragment();
                    return;
                }
                if (id == R.id.btn_thumb) {
                    switchThumbState(position);
                } else if (id == R.id.btn_reply) {
                    start(CommentDetailFragment.newInstance(commentItem));
                } else if (id == R.id.img_commenter_avatar) {
                    start(MemberInfoFragment.newInstance(commentItem.memberName));
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommentItem commentItem = commentItemList.get(position);
                Util.startFragment(CommentDetailFragment.newInstance(commentItem));
            }
        });
        rvCommentList.setAdapter(adapter);

        loadCommentData(currPage + 1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_publish) {
            int userId = User.getUserId();
            if (userId == 0) {
                Util.showLoginFragment();
                return;
            }
            startForResult(AddCommentFragment.newInstance(bindId, commentChannel), RequestCode.ADD_COMMENT.ordinal());
        }
    }

    private void loadCommentData(int page) {
        try {
            EasyJSONObject params = EasyJSONObject.generate(
                    "channel", commentChannel,
                    "bindId", bindId,
                    "page", page);

            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

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
                            adapter.loadMoreFail();
                            return;
                        }

                        hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                        SLog.info("hasMore[%s]", hasMore);
                        if (!hasMore) {
                            adapter.loadMoreEnd();
                            adapter.setEnableLoadMore(false);
                        }

                        // commentItemList.clear();
                        EasyJSONArray comments = responseObj.getArray("datas.comments");
                        for (Object object : comments) {
                            EasyJSONObject comment = (EasyJSONObject) object;
                            EasyJSONObject memberVo = comment.getObject("memberVo");
                            CommentItem item = new CommentItem();
                            item.commentId = comment.getInt("commentId");
                            item.commentChannel = comment.getInt("commentChannel");
                            item.commentType = comment.getInt("commentType");
                            item.commentLike = comment.getInt("commentLike");
                            item.content = comment.getString("content");
                            if (item.content == null) { // 兼容店鋪評論有些內容為null的問題
                                item.content = "";
                            }
                            item.isLike = comment.getInt("isLike");
                            item.commentReply = comment.getInt("commentReply");

                            if (memberVo != null) {
                                item.commenterAvatar = memberVo.getString("avatar");
                                item.memberName = memberVo.getString("memberName");
                                item.nickname = memberVo.getString("nickName");
                            }
                            item.commentTime = comment.getString("commentStartTime");

                            if (commentChannel == Constant.COMMENT_CHANNEL_STORE) {
                                item.relateStoreId = bindId;
                            } else if (commentChannel == Constant.COMMENT_CHANNEL_GOODS) {
                                item.relateCommonId = bindId;
                            }

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
                        adapter.loadMoreComplete();

                        currPage++;
                        adapter.setNewData(commentItemList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } catch (EasyJSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadCommentData(currPage + 1);
    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        SLog.info("onFragmentResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }
        SLog.info("HERE");
        if (requestCode == RequestCode.ADD_COMMENT.ordinal()) {
            SLog.info("HERE");

            CommentItem commentItem = data.getParcelable("commentItem");
            SLog.info("commentItem[%s]", commentItem);
            commentItemList.add(0, commentItem);
            adapter.setNewData(commentItemList);
            rvCommentList.scrollToPosition(0);
        }
    }

    private void switchThumbState(final int position) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        final CommentItem commentItem = commentItemList.get(position);

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "commentId", commentItem.commentId,
                "state", 1 - commentItem.isLike
        );
        Api.postUI(Api.PATH_COMMENT_LIKE, params, new UICallback() {
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

                    commentItem.isLike = 1 - commentItem.isLike;
                    commentItem.commentLike = responseObj.getInt("datas.likeCount");

                    adapter.notifyItemChanged(position);
                } catch (Exception e) {

                }
            }
        });
    }
}
