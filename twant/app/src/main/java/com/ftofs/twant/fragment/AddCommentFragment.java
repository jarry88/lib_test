package com.ftofs.twant.fragment;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.EmojiPageAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.UnicodeEmoji;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.EmojiPage;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.entity.UnicodeEmojiItem;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.utils.FileUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SoftToolPaneLayout;
import com.ftofs.twant.widget.TouchEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 發表評論Fragment 
 * @author zwm
 */
//Android-基於中秋活動的想要圈優化
public class AddCommentFragment extends BaseFragment implements View.OnClickListener, SimpleCallback {
    RelativeLayout commentImageContainer;

    ImageView btnAddImage;
    ScaledButton btnRemoveImage;
    ImageView commentImage;

    TouchEditText etContent;

    String imageAbsolutePath;

    int bindId;
    int commentChannel;
    // 評論內容
    String content;

    SoftToolPaneLayout stplContainer;
    LinearLayout llToolContainer;
    LinearLayout llBottomToolbar;
    LinearLayout llEmojiPane;

    RecyclerView rvEmojiPageList;
    EmojiPageAdapter emojiPageAdapter;
    List<EmojiPage> emojiPageList = new ArrayList<>();

    private static final int BTN_EMOJI_USAGE_EMOJI = 0;
    private static final int BTN_EMOJI_USAGE_SOFT_INPUT = 1;
    ImageView btnInsertPostEmoji;
    int usageBtnEmoji = BTN_EMOJI_USAGE_EMOJI; // btnEmoji的用途 0 -- 顯示表情圖標 1 -- 顯示軟鍵盤圖標
    private String authorName;//中球优惠券活动要用

    public static AddCommentFragment newInstance(int bindId, int commentChannel) {
        return newInstance(bindId,commentChannel,null);
    }

    public static AddCommentFragment newInstance(int bindId, int commentChannel, String name) {
        SLog.info("onClickhere2");
        Bundle args = new Bundle();

        args.putInt("bindId", bindId);
        args.putInt("commentChannel", commentChannel);

        AddCommentFragment fragment = new AddCommentFragment();
        fragment.authorName = name;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        llToolContainer = view.findViewById(R.id.ll_tool_container);
        llBottomToolbar = view.findViewById(R.id.ll_bottom_toolbar);
        llEmojiPane = view.findViewById(R.id.ll_emoji_pane);
        stplContainer = view.findViewById(R.id.stpl_container);
        stplContainer.setStatusChangedListener(new SoftToolPaneLayout.OnStatusChangedListener() {
            @Override
            public void onStatusChanged(int oldStatus, int newStatus) {
                SLog.info("oldStatus[%d], newStatus[%d]", oldStatus, newStatus);
                if (newStatus == SoftToolPaneLayout.STATUS_NONE_SHOWN) {
                    llToolContainer.setVisibility(View.GONE);
                }
            }
        });

        btnAddImage = view.findViewById(R.id.btn_add_image);
        btnAddImage.setOnClickListener(this);
        SLog.info("__onViewCreate");
        btnRemoveImage = view.findViewById(R.id.btn_remove_image);
        btnRemoveImage.setOnClickListener(this);

        commentImage = view.findViewById(R.id.comment_image);
        commentImageContainer = view.findViewById(R.id.comment_image_container);

        btnInsertPostEmoji = view.findViewById(R.id.btn_insert_post_emoji);
        btnInsertPostEmoji.setOnClickListener(this);
        etContent = view.findViewById(R.id.et_content);
        etContent.setSimpleCallback(this);
        SLog.info("__onViewCreate");
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);
        SLog.info("__onViewCreate");

        initEmojiPage(view);
        loadEmojiData();
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
        } else if (id == R.id.btn_insert_post_emoji) {
            if (usageBtnEmoji == BTN_EMOJI_USAGE_EMOJI) {
                stplContainer.showToolPane();
                btnInsertPostEmoji.setImageResource(R.drawable.icon_keyboard);
            } else {
                stplContainer.showSoftInput(etContent);
                btnInsertPostEmoji.setImageResource(R.drawable.icon_add_post_insert_emoji);
            }

            usageBtnEmoji = 1 - usageBtnEmoji;
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
                if (authorName != null&&!"".equals(authorName)) {
                    params.set("postCreateBy", authorName);
                }
            }

        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
//                    如果有优惠券，则带回上一级页面
                    if (responseObj.exists("datas.voucherList") && responseObj.exists("datas.zoneId")) {
                        EasyJSONArray array = responseObj.getSafeArray("datas.voucherList");

                        ArrayList<StoreVoucher> voucherList = new ArrayList<>();
                        for (Object o : array) {
                            EasyJSONObject voucher = (EasyJSONObject) o;
                            voucherList.add(StoreVoucher.parse(voucher));
                        }
                        if (voucherList.size() > 0) {
                            bundle.putParcelableArrayList("voucherList", voucherList);
                            bundle.putString("zoneId", responseObj.getSafeString("datas.zoneId"));
                        }
                    } else {
                        SLog.info("後端沒有返回活動數據");
                    }
                    setFragmentResult(RESULT_OK, bundle);

                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 初始化表情輸入面板
     * @param contentView
     */
    private void initEmojiPage(View contentView) {
        rvEmojiPageList = contentView.findViewById(R.id.rv_emoji_page_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvEmojiPageList.setLayoutManager(layoutManager);

        // 使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示
        // https://www.jianshu.com/p/e54db232df62
        (new PagerSnapHelper()).attachToRecyclerView(rvEmojiPageList);


        emojiPageAdapter = new EmojiPageAdapter(R.layout.emoji_page, emojiPageList);
        emojiPageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                EmojiPage emojiPage = emojiPageList.get(position);
                int id = view.getId();
                SLog.info("id[%d]", id);

                if (id == R.id.btn_delete_emoji) {
                    SLog.info("btn_delete_emoji");

                    /*
                    KEYCODE_DEL	        退格键	       67
                    KEYCODE_FORWARD_DEL	删除键	      112
                     */
                    TwantApplication.Companion.getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DEL);
                        }
                    });
                } else {
                    int index = 0;
                    for (int btnId : EmojiPageAdapter.btnIds) {
                        if (btnId == id) {
                            if (index >= emojiPage.emojiList.size()) {
                                return;
                            }
                            UnicodeEmojiItem emojiItem = emojiPage.emojiList.get(index);

                            Editable message = etContent.getEditableText();
                            SLog.info("message[%s]", message);

                            // Get the selected text.
                            int start = etContent.getSelectionStart();
                            int end = etContent.getSelectionEnd();

                            // Insert the emoticon.
                            if (start < 0) {
                                start = 0;
                            }
                            if (end < 0) {
                                end = 0;
                            }
                            message.replace(start, end, emojiItem.emoji);
                            SLog.info("message[%s]", message);

                            etContent.setText(message);
                            // 重新定位光標
                            etContent.setSelection(start + emojiItem.emoji.length());
                            break;
                        }
                        index++;
                    }
                }
            }
        });
        rvEmojiPageList.setAdapter(emojiPageAdapter);
    }

    private void loadEmojiData() {
        // 表情數
        int emojiCount = UnicodeEmoji.emojiList.length;
        // 表情頁數
        int pageCount = (emojiCount + EmojiPage.EMOJI_PER_PAGE - 1) / EmojiPage.EMOJI_PER_PAGE;

        SLog.info("emojiCount[%d], pageCount[%d]", emojiCount, pageCount);

        for (int i = 0; i < pageCount; i++) {
            int start = EmojiPage.EMOJI_PER_PAGE * i;
            int stop = start + EmojiPage.EMOJI_PER_PAGE;
            if (stop > emojiCount) {
                stop = emojiCount;
            }

            EmojiPage emojiPage = new EmojiPage();
            for (int j = start; j < stop; j++) {
                emojiPage.emojiList.add(new UnicodeEmojiItem(UnicodeEmoji.emojiList[j]));
            }

            emojiPageList.add(emojiPage);
        }
    }

    @Override
    public void onSimpleCall(Object data) {
        int id = (int) data;
        if (id == R.id.et_content) {
            SLog.info("點擊內容");
            showBottomToolbar();
        }
    }

    private void showBottomToolbar() {
        llEmojiPane.setVisibility(View.GONE);
        llToolContainer.setVisibility(View.VISIBLE);
        llBottomToolbar.setVisibility(View.VISIBLE);

        resetBtnInsertEmoji();
    }

    private void resetBtnInsertEmoji() {
        btnInsertPostEmoji.setImageResource(R.drawable.icon_add_post_insert_emoji);
        usageBtnEmoji = BTN_EMOJI_USAGE_EMOJI;
    }
}
