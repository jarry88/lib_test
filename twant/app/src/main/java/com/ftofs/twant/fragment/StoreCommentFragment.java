package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommentListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.entity.CommentReplyItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * @author gzp
 */
public class StoreCommentFragment extends ScrollableBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    int storeId;
    private RecyclerView rvCommentList;
    private CommentListAdapter adapter;
    List<CommentItem> commentItemList=new ArrayList<>();
    private boolean hasMore;
    private int currPage;
    private int commentChannel=2;
    private List<CommentReplyItem> commentReplyItemList=new ArrayList<>();

    ImageView btnComment;
    private boolean showFloatButton=true;

    // TabFragment是否可以滚动
    boolean scrollable = false;

    public static StoreCommentFragment newInstance(int storeId) {
        SLog.info("__StoreCommentFragment.newInstance%d",storeId);
        StoreCommentFragment fragment = new StoreCommentFragment();
        fragment.storeId = storeId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SLog.info("__StoreCommentFragment.onCreatedView");
        View view = inflater.inflate(R.layout.fragment_store_comment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnComment = view.findViewById(R.id.btn_comment);
        btnComment.setOnClickListener(this);
        rvCommentList = view.findViewById(R.id.rv_comment_list);
        rvCommentList.setNestedScrollingEnabled(scrollable);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvCommentList.setLayoutManager(layoutManager);
        rvCommentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    showFloatButton();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    hideFloatButton();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        adapter = new CommentListAdapter(commentItemList);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvCommentList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommentItem commentItem = commentItemList.get(position);
                int id = view.getId();

                if (id == R.id.btn_thumb) {
                    switchThumbState(position);
                } else if (id == R.id.btn_reply) {
                    Util.startFragment(CommentDetailFragment.newInstance(commentItem));
                } else if (id == R.id.img_commenter_avatar) {
                    Util.startFragment(MemberInfoFragment.newInstance(commentItem.memberName));
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

        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.store_empty_data_view, null, false);
        // 設置空頁面的提示語
        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
        tvEmptyHint.setText("暂时还没有说说哦~");
        adapter.setEmptyView(emptyView);
        rvCommentList.setAdapter(adapter);
        loadCommentData(currPage);
    }

    private void showFloatButton() {
        if (showFloatButton) {
            return;
        }
        showFloatButton = true;
        btnComment.setTranslationX(Util.dip2px(_mActivity, 55));

    }

    private void hideFloatButton() {
        if (!showFloatButton) {
            return;
        }
        showFloatButton = false;
        btnComment.setTranslationX(Util.dip2px(_mActivity,20));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_comment) {
            int userId = User.getUserId();
            if (userId == 0) {
                Util.showLoginFragment();
                return;
            }
            Util.startFragmentForResult(AddCommentFragment.newInstance(storeId, commentChannel), RequestCode.ADD_COMMENT.ordinal());
        }
    }

    private void loadCommentData(int page) {
        try {
            EasyJSONObject params = EasyJSONObject.generate(
                    "channel", commentChannel,
                    "bindId", storeId,
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

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
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
                        EasyJSONArray comments = responseObj.getSafeArray("datas.comments");
                        for (Object object : comments) {
                            EasyJSONObject comment = (EasyJSONObject) object;
                            EasyJSONObject memberVo = comment.getSafeObject("memberVo");
                            CommentItem item = new CommentItem();
                            item.commentId = comment.getInt("commentId");
                            item.commentChannel = comment.getInt("commentChannel");
                            item.commentType = comment.getInt("commentType");
                            item.commentLike = comment.getInt("commentLike");
                            item.content = comment.getSafeString("content");
                            item.commentRole = comment.getInt("commentRole");
                            if (item.content == null) { // 兼容商店評論有些內容為null的問題
                                item.content = "";
                            }
                            item.isLike = comment.getInt("isLike");
                            item.commentReply = comment.getInt("commentReply");

                            if (!Util.isJsonObjectEmpty(memberVo)) {
                                item.commenterAvatar = memberVo.getSafeString("avatar");
                                item.memberName = memberVo.getSafeString("memberName");
                                item.nickname = memberVo.getSafeString("nickName");
                            }
                            item.commentTime = comment.getSafeString("commentStartTime");

                            if (commentChannel == Constant.COMMENT_CHANNEL_STORE) {
                                item.relateStoreId = storeId;
                            } else if (commentChannel == Constant.COMMENT_CHANNEL_GOODS) {
                                item.relateCommonId = storeId;
                            }

                            if (item.commentType != Constant.COMMENT_TYPE_TEXT) {
                                EasyJSONArray images = comment.getSafeArray("images");
                                if (images != null) {
                                    for (Object object2 : images) {
                                        EasyJSONObject image = (EasyJSONObject) object2;
                                        item.imageUrl = image.getSafeString("imageUrl");
                                    }
                                }
                            }

                            commentItemList.add(item);
                        }
                        adapter.loadMoreComplete();
                        SLog.info("currcommentpage[%d]",currPage);
                        if (currPage == 1 && commentItemList.size() == 0) {
                            commentItemList.add(new CommentItem(Constant.ITEM_TYPE_NO_DATA));
                        }
                        currPage++;
                        adapter.setNewData(commentItemList);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void switchThumbState(int position) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        if (commentReplyItemList != null && commentReplyItemList.size() > 0) {
            CommentReplyItem item = commentReplyItemList.get(position);

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "commentId", item.commentId,
                    "state", 1 - item.isLike
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

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        item.isLike = 1 - item.isLike;
                        item.commentLike = responseObj.getInt("datas.likeCount");

                         //commentReplyListAdapter.notifyItemChanged(position);
                    } catch (Exception e) {

                    }
                }
            });
        }

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

    @Override
    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
        if (rvCommentList != null) {
            rvCommentList.setNestedScrollingEnabled(scrollable);
        }
    }
}
