package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 說說Fragment
 * 商店說說和產品說說共用一個Fragment
 * @author zwm
 */
public class CommentListFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    int bindId;

    /**
     * 渠道 1全部 2商店 3產品 4想要帖 5推文
     */
    int commentChannel;

    // 當前第幾頁
    int currPage = 0;
    // 是否還有數據
    boolean hasMore = false;

    RecyclerView rvCommentList;
    public List<CommentItem> commentItemList = new ArrayList<>();
    CommentListAdapter adapter;
    RelativeLayout toolBar;
    private boolean showToolBar=true;

    // 评论的RecyclerView与图片索引的映射关系
    Map<Integer, Integer> rvPositionToImageIndexMap = new HashMap<>();
    List<String> imageList = new ArrayList<>();

    /**
     * 構造方法
     * @param bindId 根據commentChannel來決定是商店說說、產品說說或是想要帖說說
     * @param commentChannel  渠道 1全部 2商店 3產品 4想要帖 5推文
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
    public static CommentListFragment newInstance(int bindId, int commentChannel,boolean showToolBar) {
        Bundle args = new Bundle();

        args.putInt("bindId", bindId);
        args.putInt("commentChannel", commentChannel);
        CommentListFragment fragment = new CommentListFragment();
        fragment.showToolBar=showToolBar;
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        toolBar = view.findViewById(R.id.tool_bar);
        if (!showToolBar) {
            toolBar.setVisibility(View.GONE);
        }
        rvCommentList = view.findViewById(R.id.rv_comment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvCommentList.setLayoutManager(layoutManager);
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
                } else if (id == R.id.btn_delete) {
                    start(MemberInfoFragment.newInstance(commentItem.memberName));
                } else if (id == R.id.image_view) {
                    Integer imageIndex = rvPositionToImageIndexMap.get(position);
                    if (imageIndex != null) {
                        Util.startFragment(ImageFragment.newInstance(imageIndex, imageList));
                    }
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("2進入評論詳情頁");
                if (position < commentItemList.size()) {
                    CommentItem commentItem = commentItemList.get(position);
                    if (commentItem.getItemType() != Constant.ITEM_TYPE_NO_DATA) {
                        Util.startFragment(CommentDetailFragment.newInstance(commentItem));
                    }
                }
            }
        });
        rvCommentList.setAdapter(adapter);

        loadCommentData(currPage + 1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_publish) {
            int userId = User.getUserId();
            if (userId == 0) {
                Util.showLoginFragment(requireContext());
                return;
            }
            startForResult(AddCommentFragment.newInstance(bindId, commentChannel), RequestCode.ADD_COMMENT.ordinal());
        }
    }

    public void resetData() {
        currPage = 0;
        commentItemList.clear();
        loadCommentData(currPage+1);

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
                                item.relateStoreId = bindId;
                            } else if (commentChannel == Constant.COMMENT_CHANNEL_GOODS) {
                                item.relateCommonId = bindId;
                            }

                            if (item.commentType != Constant.COMMENT_TYPE_TEXT) {
                                EasyJSONArray images = comment.getSafeArray("images");
                                for (Object object2 : images) {
                                    EasyJSONObject image = (EasyJSONObject) object2;
                                    item.imageUrl = image.getSafeString("imageUrl");
                                    rvPositionToImageIndexMap.put(commentItemList.size(), imageList.size());
                                    imageList.add(StringUtil.normalizeImageUrl(item.imageUrl));
                                }
                            }

                            commentItemList.add(item);
                        }
                        adapter.loadMoreComplete();
                        if (currPage == 0 && commentItemList.size() == 0) {
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

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
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
        if (requestCode == RequestCode.ADD_COMMENT.ordinal()) {
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

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    commentItem.isLike = 1 - commentItem.isLike;
                    commentItem.commentLike = responseObj.getInt("datas.likeCount");

                    adapter.notifyItemChanged(position);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

}
