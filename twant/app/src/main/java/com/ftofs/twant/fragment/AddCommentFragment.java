package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 發表評論Fragment 
 * @author zwm
 */
public class AddCommentFragment extends BaseFragment implements View.OnClickListener {
    RelativeLayout commentImageContainer;

    ImageView btnAddImage;
    ScaledButton btnRemoveImage;
    ImageView commentImage;

    EditText etContent;

    String imageAbsolutePath;

    int bindId;
    int commentChannel;
    // 評論內容
    String content;

    public static AddCommentFragment newInstance(int bindId, int commentChannel) {
        SLog.info("onClickhere2");
        Bundle args = new Bundle();

        args.putInt("bindId", bindId);
        args.putInt("commentChannel", commentChannel);

        AddCommentFragment fragment = new AddCommentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_comment, container, false);
        SLog.info("__onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SLog.info("__onViewCreate");

        EventBus.getDefault().register(this);
        SLog.info("__onViewCreate");
        Bundle args = getArguments();
        bindId = args.getInt("bindId");
        commentChannel = args.getInt("commentChannel");

        btnAddImage = view.findViewById(R.id.btn_add_image);
        btnAddImage.setOnClickListener(this);
        SLog.info("__onViewCreate");
        btnRemoveImage = view.findViewById(R.id.btn_remove_image);
        btnRemoveImage.setOnClickListener(this);

        commentImage = view.findViewById(R.id.comment_image);
        commentImageContainer = view.findViewById(R.id.comment_image_container);

        etContent = view.findViewById(R.id.et_content);
        SLog.info("__onViewCreate");
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);
        SLog.info("__onViewCreate");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SLog.info("onDestroy");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_UPLOAD_FILE_SUCCESS) {
            String url = (String) message.data;
            SLog.info("url[%s]", url);
            commitCommentInternal(url);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_add_image) {
            openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
        } else if (id == R.id.btn_remove_image) {
            commentImageContainer.setVisibility(View.GONE);
            btnAddImage.setVisibility(View.VISIBLE);
            imageAbsolutePath = null;
        } else if (id == R.id.btn_commit) {
            commitComment();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        Uri uri = data.getData();
        imageAbsolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
        SLog.info("imageAbsolutePath[%s]", imageAbsolutePath);

        Glide.with(_mActivity).load(imageAbsolutePath).centerCrop().into(commentImage);
        commentImageContainer.setVisibility(View.VISIBLE);
        btnAddImage.setVisibility(View.GONE);
    }

    private void commitComment() {
        content = etContent.getText().toString().trim();

        if (StringUtil.isEmpty(content)) {
            ToastUtil.error(_mActivity, "評論內容不能為空");
            return;
        }

        if (StringUtil.isEmpty(imageAbsolutePath)) {
            // 如果没有图片，直接提交文字
            commitCommentInternal(null);
        } else {
            Api.asyncUploadFile(new File(imageAbsolutePath));
        }
    }

    /**
     * 提交评论
     * @param imageUrl 評論圖片的url
     */
    private void commitCommentInternal(String imageUrl) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String path = Api.PATH_PUBLISH_COMMENT + Api.makeQueryString(EasyJSONObject.generate("token", token));
        SLog.info("path[%s]", path);

        EasyJSONArray images = EasyJSONArray.generate();
        if (!StringUtil.isEmpty(imageUrl)) {
            images.append(EasyJSONObject.generate(
                    "imageUrl", imageUrl
            ));
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "commentChannel", commentChannel,
                "deep", 1,
                "content", StringUtil.filterCommentContent(content),
                "images", images
        );

        try {
            if (commentChannel == Constant.COMMENT_CHANNEL_GOODS) {
                params.set("relateCommonId", bindId);
            }
            if (commentChannel == Constant.COMMENT_CHANNEL_STORE) {
                params.set("relateStoreId", bindId);
            }
            if (commentChannel == Constant.COMMENT_CHANNEL_POST) {
                params.set("relatePostId", bindId);
            }
        } catch (Exception e) {

        }
        SLog.info("params[%s]", params.toString());

        Api.postJsonUi(path, params.toString(), new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONObject wantCommentVo = responseObj.getSafeObject("datas.wantCommentVo");
                    EasyJSONObject memberVo = wantCommentVo.getSafeObject("memberVo");


                    CommentItem commentItem = new CommentItem();
                    commentItem.commentId = wantCommentVo.getInt("commentId");
                    commentItem.commentType = wantCommentVo.getInt("commentType");
                    commentItem.commentChannel = commentChannel;
                    commentItem.content = content;
                    commentItem.isLike = 0;
                    commentItem.commentLike = 0;
                    commentItem.commentReply = 0;
                    commentItem.commenterAvatar = memberVo.getSafeString("avatar");
                    commentItem.nickname = memberVo.getSafeString("nickName");
                    commentItem.commentTime = wantCommentVo.getSafeString("commentStartTime");

                    EasyJSONArray images = wantCommentVo.getSafeArray("images");
                    if (images.length() > 0) {
                        commentItem.imageUrl = images.getObject(0).getSafeString("imageUrl");
                    }

                    if (commentChannel == Constant.COMMENT_CHANNEL_GOODS) {
                        commentItem.relateCommonId = bindId;
                    }
                    if (commentChannel == Constant.COMMENT_CHANNEL_STORE) {
                        commentItem.relateStoreId = bindId;
                    }
                    commentItem.replyCommentId = 0;
                    if (commentChannel == Constant.COMMENT_CHANNEL_POST) {
                        commentItem.relatePostId = bindId;
                    }
                    commentItem.parentCommentId = 0;

                    ToastUtil.success(_mActivity, "發表成功");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("commentItem", commentItem);
                    setFragmentResult(RESULT_OK, bundle);

                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
}
