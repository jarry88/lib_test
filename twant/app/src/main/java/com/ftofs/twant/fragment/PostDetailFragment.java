package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PostCommentListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.entity.CommentReplyItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.SquareGridLayout;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 貼文詳情Fragment
 * @author zwm
 */
public class PostDetailFragment extends BaseFragment implements View.OnClickListener {
    public static final int STATE_TYPE_THUMB = 1;
    public static final int STATE_TYPE_LIKE = 2;

    int postId;
    String coverUrl;
    String title;
    String content;

    TextView tvPostTitle;
    ImageView imgAuthorAvatar;
    TextView tvNickname;
    TextView tvCreatetime;
    TextView tvDeadline;
    TextView tvBudgetPrice;
    TextView tvContent;

    SquareGridLayout sglImageContainer;
    TextView tvViewCount;
    TextView tvReplyCount;
    LinearLayout llCommentContainer;
    ImageView imgThumb;
    TextView tvThumbCount;
    ImageView imgLike;
    TextView tvLikeCount;

    String authorMemberName;
    int isLike;
    int isFavor;

    int postComment;

    PostCommentListAdapter adapter;
    List<CommentItem> commentItemList = new ArrayList<>();

    public static PostDetailFragment newInstance(int postId) {
        Bundle args = new Bundle();

        args.putInt("postId", postId);
        PostDetailFragment fragment = new PostDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        postId = args.getInt("postId", 0);

        tvPostTitle = view.findViewById(R.id.tv_post_title);
        imgAuthorAvatar = view.findViewById(R.id.img_author_avatar);
        imgAuthorAvatar.setOnClickListener(this);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvCreatetime = view.findViewById(R.id.tv_create_time);
        tvDeadline = view.findViewById(R.id.tv_deadline);
        tvBudgetPrice = view.findViewById(R.id.tv_budget_price);
        tvContent = view.findViewById(R.id.tv_content);

        sglImageContainer = view.findViewById(R.id.sgl_image_container);

        tvViewCount = view.findViewById(R.id.tv_view_count);
        tvReplyCount = view.findViewById(R.id.tv_reply_count);
        llCommentContainer = view.findViewById(R.id.ll_comment_container);
        adapter = new PostCommentListAdapter(_mActivity, llCommentContainer, R.layout.post_comment_item);
        adapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                CommentItem commentItem = commentItemList.get(position);
                start(CommentDetailFragment.newInstance(commentItem));
            }
        });
        adapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                int id = view.getId();

                SLog.info("id[%d]", id);
                if (!User.isLogin()) {
                    start(LoginFragment.newInstance());
                    return;
                }
                SLog.info("id[%d]", id);
                CommentItem commentItem = commentItemList.get(position);
                if (id == R.id.img_commenter_avatar) {
                    start(MemberInfoFragment.newInstance(commentItem.memberName));
                } else if (id == R.id.btn_reply) {
                    SLog.info("id[%d]", id);
                    start(CommentDetailFragment.newInstance(commentItem));
                } else if (id == R.id.btn_thumb) {
                    switchThumbState(position);
                }
            }
        });
        imgThumb = view.findViewById(R.id.img_thumb);
        tvThumbCount = view.findViewById(R.id.tv_thumb_count);
        imgLike = view.findViewById(R.id.img_like);
        tvLikeCount = view.findViewById(R.id.tv_like_count);

        Util.setOnClickListener(view, R.id.btn_back, this);

        Util.setOnClickListener(view, R.id.btn_add_post_comment, this);
        Util.setOnClickListener(view, R.id.btn_thumb, this);
        Util.setOnClickListener(view, R.id.btn_like, this);
        Util.setOnClickListener(view, R.id.btn_share, this);

        loadData();
        loadCommentList();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_add_post_comment) {
            if (!User.isLogin()) {
                start(LoginFragment.newInstance());
                return;
            }
            startForResult(AddCommentFragment.newInstance(postId, Constant.COMMENT_CHANNEL_POST), RequestCode.ADD_COMMENT.ordinal());
        } else if (id == R.id.btn_thumb) {
            if (!User.isLogin()) {
                start(LoginFragment.newInstance());
                return;
            }
            switchInteractiveState(STATE_TYPE_THUMB);
        } else if (id == R.id.btn_like) {
            if (!User.isLogin()) {
                start(LoginFragment.newInstance());
                return;
            }
            switchInteractiveState(STATE_TYPE_LIKE);
        } else if (id == R.id.btn_share) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new SharePopup(_mActivity, SharePopup.generatePostShareLink(postId), title, content, coverUrl))
                    .show();
        } else if (id == R.id.img_author_avatar) {
            if (!StringUtil.isEmpty(authorMemberName)) {
                if (User.isLogin()) {
                    start(MemberInfoFragment.newInstance(authorMemberName));
                } else {
                    start(LoginFragment.newInstance());
                }
            }
        }
    }

    private void switchInteractiveState(int type) {
        SLog.info("switchInteractiveState[%d]", type);
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String path = null;
        int newState = 0;
        if (type == STATE_TYPE_THUMB) {
            path = Api.PATH_POST_THUMB;
            newState = 1 - isLike;
        } else if (type == STATE_TYPE_LIKE) {
            path = Api.PATH_POST_LIKE;
            newState = 1 - isFavor;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "postId", postId,
                "state", newState);

        SLog.info("path[%s], params[%s]", path, params);
        Api.postUI(path, params, new UICallback() {
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
                    if (type == STATE_TYPE_THUMB) {
                        isLike = 1 - isLike;
                        if (isLike == 1) {
                            imgThumb.setImageResource(R.drawable.icon_post_thumb_blue);
                        } else {
                            imgThumb.setImageResource(R.drawable.icon_post_thumb_black);
                        }

                        int likeCount = responseObj.getInt("datas.likeCount");
                        if (likeCount > 0) {
                            tvThumbCount.setText(String.valueOf(likeCount));
                        } else {
                            tvThumbCount.setText("");
                        }
                    } else if (type == STATE_TYPE_LIKE) {
                        isFavor = 1 - isFavor;
                        if (isFavor == 1) {
                            imgLike.setImageResource(R.drawable.icon_post_like_blue);
                        } else {
                            imgLike.setImageResource(R.drawable.icon_post_like_black);
                        }

                        int favorCount = responseObj.getInt("datas.favorCount");
                        if (favorCount > 0) {
                            tvLikeCount.setText(String.valueOf(favorCount));
                        } else {
                            tvLikeCount.setText("");
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void loadData() {
        String token = User.getToken();

        EasyJSONObject params = EasyJSONObject.generate("postId", postId);

        if (!StringUtil.isEmpty(token)) {
            try {
                params.set("token", token);
            } catch (EasyJSONException e) {
                e.printStackTrace();
            }
        }

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_POST_DETAIL, params, new UICallback() {
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
                    EasyJSONObject wantPostVoInfo = (EasyJSONObject) responseObj.get("datas.wantPostVoInfo");
                    EasyJSONObject memberVo = wantPostVoInfo.getObject("memberVo");

                    coverUrl = wantPostVoInfo.getString("coverImage");

                    title = wantPostVoInfo.getString("postCategory") + " | " + wantPostVoInfo.getString("title");
                    tvPostTitle.setText(title);

                    String avatarUrl = memberVo.getString("avatar");
                    if (StringUtil.isEmpty(avatarUrl)) {
                        Glide.with(_mActivity).load(R.drawable.grey_default_avatar).centerCrop().into(imgAuthorAvatar);
                    } else {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAuthorAvatar);
                    }


                    String nickname = memberVo.getString("nickName");
                    tvNickname.setText(nickname);

                    authorMemberName = memberVo.getString("memberName");

                    String createTime = wantPostVoInfo.getString("createTime");
                    tvCreatetime.setText(createTime);

                    String deadline = wantPostVoInfo.getString("expiresDate");
                    tvDeadline.setText(deadline);

                    float budgetPrice = (float) wantPostVoInfo.getDouble("budgetPrice");
                    tvBudgetPrice.setText(StringUtil.formatPrice(_mActivity, budgetPrice, 0));

                    content = wantPostVoInfo.getString("content");
                    tvContent.setText(StringUtil.translateEmoji(_mActivity, content, (int) tvContent.getTextSize()));

                    postComment = wantPostVoInfo.getInt("postReply");
                    updatePostCommentCount();

                    EasyJSONArray wantPostImages = wantPostVoInfo.getArray("wantPostImages");
                    for (Object object : wantPostImages) {
                        EasyJSONObject imageObj = (EasyJSONObject) object;

                        String imageUrl = imageObj.getString("imageUrl");
                        ImageView imageView = new ImageView(_mActivity);

                        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        layoutParams.topMargin = Util.dip2px(_mActivity, 3);
                        layoutParams.bottomMargin = Util.dip2px(_mActivity, 3);
                        layoutParams.leftMargin = Util.dip2px(_mActivity, 3);
                        layoutParams.rightMargin = Util.dip2px(_mActivity, 3);

                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(imageUrl)).centerCrop().into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (StringUtil.isEmpty(imageUrl)) {
                                    return;
                                }

                                if (imageUrl.endsWith(".gif")) { // 如果是Gif，顯示Gif動圖
                                    start(GifFragment.newInstance(StringUtil.normalizeImageUrl(imageUrl)));
                                } else {
                                    start(ImageViewerFragment.newInstance(StringUtil.normalizeImageUrl(imageUrl)));
                                }

                            }
                        });

                        sglImageContainer.addView(imageView, layoutParams);
                    }

                    int postView = wantPostVoInfo.getInt("postView"); // 瀏覽次數
                    int postLike = wantPostVoInfo.getInt("postLike");  // 點讚次數
                    int postFavor = wantPostVoInfo.getInt("postFavor");  // 喜歡次數

                    String viewCount = String.format(getString(R.string.text_view_count), postView);
                    tvViewCount.setText(viewCount);

                    isLike = wantPostVoInfo.getInt("isLike");
                    if (isLike == 1) {
                        imgThumb.setImageResource(R.drawable.icon_post_thumb_blue);
                    } else {
                        imgThumb.setImageResource(R.drawable.icon_post_thumb_black);
                    }

                    isFavor = wantPostVoInfo.getInt("isFavor");
                    if (isFavor == 1) {
                        imgLike.setImageResource(R.drawable.icon_post_like_blue);
                    } else {
                        imgLike.setImageResource(R.drawable.icon_post_like_black);
                    }

                    if (postLike > 0) {
                        tvThumbCount.setText(String.valueOf(postLike));
                    } else {
                        tvThumbCount.setText("");
                    }

                    if (postFavor > 0) {
                        tvLikeCount.setText(String.valueOf(postFavor));
                    } else {
                        tvLikeCount.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }

    private void updatePostCommentCount() {
        tvReplyCount.setText(String.format(getString(R.string.text_view_comment_count), postComment));
    }

    private void loadCommentList() {
        String token = User.getToken();

        EasyJSONObject params = EasyJSONObject.generate(
                "channel", Constant.COMMENT_CHANNEL_POST,
                "bindId", postId);

        if (!StringUtil.isEmpty(token)) {
            try {
                params.set("token", token);
            } catch (EasyJSONException e) {
                e.printStackTrace();
            }
        }

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_COMMENT_LIST, params, new UICallback() {
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
                    commentItemList.clear();
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
                        item.relatePostId = postId;

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

                    adapter.setData(commentItemList);
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
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

            tvPostTitle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCommentList();
                    postComment++;
                    updatePostCommentCount();
                }
            }, 1500); // 有時候重新加載的時候，也沒有從服務端加載到最新的數據，延遲個1500毫秒
        }
    }
}
