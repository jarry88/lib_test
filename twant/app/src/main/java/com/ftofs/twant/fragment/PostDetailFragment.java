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
import com.ftofs.twant.adapter.CommentReplyListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CommentReplyItem;
import com.ftofs.twant.log.SLog;
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
    LinearLayout llReplyContainer;
    ImageView imgThumb;
    TextView tvThumbCount;
    ImageView imgLike;
    TextView tvLikeCount;

    int isLike;
    int isFavor;

    CommentReplyListAdapter adapter;
    List<CommentReplyItem> commentReplyItemList = new ArrayList<>();

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
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvCreatetime = view.findViewById(R.id.tv_create_time);
        tvDeadline = view.findViewById(R.id.tv_deadline);
        tvBudgetPrice = view.findViewById(R.id.tv_budget_price);
        tvContent = view.findViewById(R.id.tv_content);

        sglImageContainer = view.findViewById(R.id.sgl_image_container);

        tvViewCount = view.findViewById(R.id.tv_view_count);
        tvReplyCount = view.findViewById(R.id.tv_reply_count);
        llReplyContainer = view.findViewById(R.id.ll_reply_container);
        adapter = new CommentReplyListAdapter(_mActivity, llReplyContainer, R.layout.comment_reply_item);
        imgThumb = view.findViewById(R.id.img_thumb);
        tvThumbCount = view.findViewById(R.id.tv_thumb_count);
        imgLike = view.findViewById(R.id.img_like);
        tvLikeCount = view.findViewById(R.id.tv_like_count);

        Util.setOnClickListener(view, R.id.btn_back, this);

        Util.setOnClickListener(view, R.id.btn_thumb, this);
        Util.setOnClickListener(view, R.id.btn_like, this);
        Util.setOnClickListener(view, R.id.btn_share, this);

        loadData();
        loadReplyData();
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
        } else if (id == R.id.btn_thumb) {
            switchInteractiveState(STATE_TYPE_THUMB);
        } else if (id == R.id.btn_like) {
            switchInteractiveState(STATE_TYPE_LIKE);
        } else if (id == R.id.btn_share) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new SharePopup(_mActivity, SharePopup.SHARE_TYPE_POST, postId))
                    .show();
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
                "state", newState
        );

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
                        tvThumbCount.setText(String.valueOf(responseObj.getInt("datas.likeCount")));
                    } else if (type == STATE_TYPE_LIKE) {
                        isFavor = 1 - isFavor;
                        if (isFavor == 1) {
                            imgLike.setImageResource(R.drawable.icon_post_like_blue);
                        } else {
                            imgLike.setImageResource(R.drawable.icon_post_like_black);
                        }
                        tvLikeCount.setText(String.valueOf(responseObj.getInt("datas.favorCount")));
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "postId", postId);

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


                    String title = wantPostVoInfo.getString("postCategory") + " | " + wantPostVoInfo.getString("title");
                    tvPostTitle.setText(title);

                    String avatarUrl = memberVo.getString("avatar");
                    Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAuthorAvatar);

                    String nickname = memberVo.getString("nickName");
                    tvNickname.setText(nickname);

                    String createTime = wantPostVoInfo.getString("createTime");
                    tvCreatetime.setText(createTime);

                    String deadline = wantPostVoInfo.getString("expiresDate");
                    tvDeadline.setText(deadline);

                    float budgetPrice = (float) wantPostVoInfo.getDouble("budgetPrice");
                    tvBudgetPrice.setText(StringUtil.formatPrice(_mActivity, budgetPrice, 0));

                    String content = wantPostVoInfo.getString("content");
                    tvContent.setText(StringUtil.translateEmoji(_mActivity, content, (int) tvContent.getTextSize()));

                    int postReply = wantPostVoInfo.getInt("postReply");
                    tvReplyCount.setText(String.format(getString(R.string.text_view_reply_count), postReply));

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
                                start(ImageViewerFragment.newInstance(StringUtil.normalizeImageUrl(imageUrl)));
                            }
                        });

                        sglImageContainer.addView(imageView, layoutParams);
                    }

                    int postView = wantPostVoInfo.getInt("postView"); // 瀏覽次數
                    int postLike = wantPostVoInfo.getInt("postLike");  // 點贊次數
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

                    tvThumbCount.setText(String.valueOf(postLike));
                    tvLikeCount.setText(String.valueOf(postFavor));
                } catch (Exception e) {

                }
            }
        });
    }

    private void loadReplyData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "channel", Constant.COMMENT_CHANNEL_POST,
                "bindId", postId);

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
                    EasyJSONArray comments = responseObj.getArray("datas.comments");
                    for (Object object : comments) {
                        EasyJSONObject comment = (EasyJSONObject) object;
                        EasyJSONObject memberVo = comment.getObject("memberVo");

                        CommentReplyItem item = new CommentReplyItem();

                        item.memberId = memberVo.getInt("memberId");
                        item.memberName = memberVo.getString("memberName");
                        item.commentId = comment.getInt("commentId");
                        item.avatarUrl = memberVo.getString("avatar");
                        item.nickname = memberVo.getString("nickName");
                        item.createTime = comment.getLong("createTime");
                        item.content = comment.getString("content");
                        item.isLike = comment.getInt("isLike");
                        item.commentLike = comment.getInt("commentLike");

                        commentReplyItemList.add(item);
                    }

                    adapter.setData(commentReplyItemList);
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }
}
