package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 評論詳情Fragment
 * @author zwm
 */
public class CommentDetailFragment extends BaseFragment implements View.OnClickListener {
    CommentItem commentItem;

    ImageView imgCommenterAvatar;
    TextView tvCommenterNickname;
    TextView tvCommentTime;
    TextView tvContent;
    ImageView imageView;
    ImageView iconThumb;
    TextView tvThumbCount;
    TextView tvReplyCount;
    EditText etReplyContent;

    public static CommentDetailFragment newInstance(CommentItem commentItem) {
        Bundle args = new Bundle();

        args.putParcelable("commentItem", commentItem);


        CommentDetailFragment fragment = new CommentDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        commentItem = args.getParcelable("commentItem");

        imgCommenterAvatar = view.findViewById(R.id.img_commenter_avatar);
        tvCommenterNickname = view.findViewById(R.id.tv_commenter_nickname);
        tvCommentTime = view.findViewById(R.id.tv_comment_time);
        tvContent = view.findViewById(R.id.tv_content);
        imageView = view.findViewById(R.id.image_view);
        iconThumb = view.findViewById(R.id.icon_thumb);
        tvThumbCount = view.findViewById(R.id.tv_thumb_count);
        tvReplyCount = view.findViewById(R.id.tv_reply_count);
        etReplyContent = view.findViewById(R.id.et_reply_content);
        etReplyContent.setHint(getString(R.string.text_reply) + "：" + commentItem.nickname);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_thumb, this);

        loadCommentDetail();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_thumb) {
            switchThumbState();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void loadCommentDetail() {
        try {
            EasyJSONObject params = EasyJSONObject.generate(
                    "channel", commentItem.commentChannel,
                    "commentId", commentItem.commentId);

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            SLog.info("params[%s]", params.toString());

            Api.postUI(Api.PATH_COMMENT_DETAIL, params, new UICallback() {
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

                        Glide.with(_mActivity).load(Config.OSS_BASE_URL + "/" + commentItem.commenterAvatar).centerCrop().into(imgCommenterAvatar);
                        tvCommenterNickname.setText(commentItem.nickname);
                        tvCommentTime.setText(commentItem.commentTime);
                        tvContent.setText(commentItem.content);
                        if (commentItem.commentType == Constant.COMMENT_TYPE_TEXT || StringUtil.isEmpty(commentItem.imageUrl)) {
                            imageView.setVisibility(View.GONE);
                        } else {
                            Glide.with(_mActivity).load(Config.OSS_BASE_URL + "/" + commentItem.imageUrl).centerCrop().into(imageView);
                            imageView.setVisibility(View.VISIBLE);
                        }

                        updateThumbState();
                        tvReplyCount.setText(String.format("查看%d條回復", commentItem.commentReply));
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void switchThumbState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

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

                    updateThumbState();
                } catch (Exception e) {

                }
            }
        });
    }

    private void updateThumbState() {
        if (commentItem.isLike == 1) {
            iconThumb.setImageResource(R.drawable.icon_comment_thumb_blue);
        } else {
            iconThumb.setImageResource(R.drawable.icon_comment_thumb_grey);
        }
        
        tvThumbCount.setText(String.valueOf(commentItem.commentLike));
    }
}

