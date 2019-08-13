package com.ftofs.twant.fragment;

import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.CommentReplyListAdapter;
import com.ftofs.twant.adapter.EmojiPageAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.entity.CommentReplyItem;
import com.ftofs.twant.entity.EmojiPage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Emoji;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.QMUIAlignMiddleImageSpan;
import com.ftofs.twant.widget.SmoothInputLayout;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 評論詳情Fragment
 * @author zwm
 */
public class CommentDetailFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {
    CommentItem commentItem;

    public static class QuoteReply {
        public boolean isQuoteReply;  // 是否為引用回復，如果是引用回復時，下面的字段才有用
        public String quoteNickname;  // 引用評論作者的昵稱
        public String quoteContent;   // 引用評論的內容
    }

    QuoteReply quoteReply = new QuoteReply();

    ImageView imgCommenterAvatar;
    TextView tvCommenterNickname;
    TextView tvCommentTime;
    TextView tvContent;
    ImageView imageView;
    ImageView iconThumb;
    TextView tvThumbCount;
    TextView tvReplyCount;
    EditText etReplyContent;

    ScrollView svCommentContainer;

    ImageView btnEmoji;
    SmoothInputLayout silMainContainer;

    RecyclerView rvEmojiPageList;
    EmojiPageAdapter emojiPageAdapter;
    List<EmojiPage> emojiPageList = new ArrayList<>();

    int replyCommentId;  // 當前要回復的主題Id或二級評論Id
    CommentReplyListAdapter commentReplyListAdapter;
    List<CommentReplyItem> commentReplyItemList = new ArrayList<>();

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


        // 默認為當前主題的Id
        replyCommentId = commentItem.commentId;

        silMainContainer = view.findViewById(R.id.sil_main_container);

        imgCommenterAvatar = view.findViewById(R.id.img_commenter_avatar);
        tvCommenterNickname = view.findViewById(R.id.tv_commenter_nickname);
        tvCommentTime = view.findViewById(R.id.tv_comment_time);
        tvContent = view.findViewById(R.id.tv_content);
        imageView = view.findViewById(R.id.image_view);
        iconThumb = view.findViewById(R.id.icon_thumb);
        tvThumbCount = view.findViewById(R.id.tv_thumb_count);
        tvReplyCount = view.findViewById(R.id.tv_reply_count);
        etReplyContent = view.findViewById(R.id.et_reply_content);
        etReplyContent.setOnTouchListener(this);
        setReplyContentHint(commentItem.nickname);

        LinearLayout llReplyContainer = view.findViewById(R.id.ll_reply_container);
        commentReplyListAdapter = new CommentReplyListAdapter(_mActivity, llReplyContainer, R.layout.comment_reply_item);
        commentReplyListAdapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                int id = view.getId();

                CommentReplyItem item = commentReplyItemList.get(position);
                if (id == R.id.img_avatar) {
                    int userId = User.getUserId();
                    if (userId == item.memberId) { // 如果是自己頭像，顯示自己個人信息
                        start(PersonalInfoFragment.newInstance());
                    } else { // 如果是別人，顯示別人的會員信息
                        start(MemberInfoFragment.newInstance(item.memberName));
                    }
                } else if (id == R.id.btn_reply_comment) {
                    replyCommentId = item.commentId;
                    setReplyContentHint(item.nickname);

                    // 是引用回復
                    quoteReply.isQuoteReply = true;
                    quoteReply.quoteNickname = item.nickname;
                    quoteReply.quoteContent = item.content;

                    showInput();
                } else if (id == R.id.btn_thumb) {
                    switchThumbState(position);
                }
                SLog.info("");
            }
        });

        svCommentContainer = view.findViewById(R.id.sv_comment_container);
        svCommentContainer.setOnTouchListener(this);

        btnEmoji = view.findViewById(R.id.btn_emoji);
        btnEmoji.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_thumb, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);

        loadCommentDetail();

        initEmojiPage(view);
        loadEmojiData();
    }

    private void switchThumbState(int position) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

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

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    item.isLike = 1 - item.isLike;
                    item.commentLike = responseObj.getInt("datas.likeCount");

                    commentReplyListAdapter.notifyItemChanged(position);
                } catch (Exception e) {

                }
            }
        });
    }

    private void setReplyContentHint(String nickname) {
        etReplyContent.setHint(getString(R.string.text_reply) + "：" + nickname);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.sv_comment_container:
                // 如果點擊內容區，收合軟鍵盤或表情輸入面板
                btnEmoji.setSelected(false);
                btnEmoji.setImageResource(R.drawable.icon_emoji);
                silMainContainer.closeKeyboard(true);
                silMainContainer.closeInputPane();

                // 切換為回復當前的一級評論
                replyCommentId = commentItem.commentId;
                setReplyContentHint(commentItem.nickname);
                quoteReply.isQuoteReply = false;
                break;
            case R.id.et_reply_content:
                // 如果點擊輸入區域，將圖標切換為表情輸入圖標
                btnEmoji.setSelected(false);
                btnEmoji.setImageResource(R.drawable.icon_emoji);
                break;
            default:
                break;
        }

        return false;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_thumb) {
            switchThumbState();
        } else if (id == R.id.btn_commit) {
            String token = User.getToken();

            if (StringUtil.isEmpty(token)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "commentChannel", commentItem.commentChannel,
                    "deep", 2,
                    "content", etReplyContent.getText().toString().trim(),
                    "parentCommentId", commentItem.commentId,
                    "replyCommentId", replyCommentId);

            try {
                if (commentItem.relateCommonId > 0) {
                    params.set("relateCommonId", commentItem.relateCommonId);
                }
                if (commentItem.relateStoreId > 0) {
                    params.set("relateStoreId", commentItem.relateStoreId);
                }
                if (commentItem.relatePostId > 0) {
                    params.set("relatePostId", commentItem.relatePostId);
                }
            } catch (Exception e) {

            }

            String path = Api.PATH_PUBLISH_COMMENT + Api.makeQueryString(EasyJSONObject.generate("token", token));
            SLog.info("path[%s], params[%s]", path, params.toString());
            Api.postJsonUi(path, params.toString(), new UICallback() {
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

                        ToastUtil.success(_mActivity, "回復成功");

                        EasyJSONObject wantCommentVo = responseObj.getObject("datas.wantCommentVo");
                        EasyJSONObject memberVo = wantCommentVo.getObject("memberVo");

                        CommentReplyItem commentReplyItem = new CommentReplyItem();
                        commentReplyItem.memberId = memberVo.getInt("memberId");
                        commentReplyItem.memberName = memberVo.getString("memberName");
                        commentReplyItem.commentId = wantCommentVo.getInt("commentId");
                        commentReplyItem.avatarUrl = memberVo.getString("avatar");
                        commentReplyItem.nickname = memberVo.getString("nickName");
                        commentReplyItem.createTime = wantCommentVo.getLong("createTime");
                        commentReplyItem.content = wantCommentVo.getString("content");
                        commentReplyItem.isLike = wantCommentVo.getInt("isLike");
                        commentReplyItem.commentLike = wantCommentVo.getInt("commentLike");

                        if (quoteReply.isQuoteReply) {
                            commentReplyItem.isQuoteReply = true;
                            commentReplyItem.quoteNickname = quoteReply.quoteNickname;
                            commentReplyItem.quoteContent = quoteReply.quoteContent;
                        }

                        commentReplyItemList.add(0, commentReplyItem);
                        commentReplyListAdapter.notifyItemInserted(0);

                        etReplyContent.setText("");
                        silMainContainer.closeKeyboard(true);
                        silMainContainer.closeInputPane();

                        quoteReply.isQuoteReply = false;
                    } catch (Exception e) {
                        SLog.info("Error!%s", e);
                    }
                }
            });
        } else if (id == R.id.btn_emoji) {
            if (btnEmoji.isSelected()) {
                btnEmoji.setSelected(false);
                showInput();
            } else {
                btnEmoji.setSelected(true);
                showEmoji();
            }
        }
    }

    /**
     * 显示输入面板
     */
    private void showInput() {
        silMainContainer.showKeyboard();
        btnEmoji.setImageResource(R.drawable.icon_emoji);
    }

    /**
     *  显示Emoji面板
     */
    private void showEmoji() {
        btnEmoji.setImageResource(R.drawable.icon_keyboard);
        silMainContainer.showInputPane(true);
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
            if (!StringUtil.isEmpty(token)) {
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
                        tvContent.setText(StringUtil.translateEmoji(_mActivity, commentItem.content, (int) tvContent.getTextSize()));
                        if (commentItem.commentType == Constant.COMMENT_TYPE_TEXT || StringUtil.isEmpty(commentItem.imageUrl)) {
                            imageView.setVisibility(View.GONE);
                        } else {
                            Glide.with(_mActivity).load(Config.OSS_BASE_URL + "/" + commentItem.imageUrl).centerCrop().into(imageView);
                            imageView.setVisibility(View.VISIBLE);
                        }

                        updateThumbState();
                        tvReplyCount.setText(String.format(getString(R.string.text_view_reply_count), commentItem.commentReply));


                        // 回復列表
                        EasyJSONArray replyList = responseObj.getArray("datas.replyList");
                        for (Object object : replyList) {
                            EasyJSONObject reply = (EasyJSONObject) object;

                            CommentReplyItem item = new CommentReplyItem();
                            item.memberId = reply.getInt("memberVo.memberId");
                            item.memberName = reply.getString("memberVo.memberName");
                            item.commentId = reply.getInt("commentId");
                            item.avatarUrl = reply.getString("memberVo.avatar");
                            item.nickname = reply.getString("memberVo.nickName");
                            item.createTime = reply.getLong("createTime");
                            item.content = reply.getString("content");
                            item.commentLike = reply.getInt("commentLike");
                            item.isLike = reply.getInt("isLike");

                            EasyJSONObject wantCommentReplyVo = reply.getObject("wantCommentReplyVo");
                            if (wantCommentReplyVo != null) {
                                item.isQuoteReply = true;
                                item.quoteNickname = wantCommentReplyVo.getString("nickName");
                                item.quoteContent = wantCommentReplyVo.getString("content");
                            }

                            commentReplyItemList.add(item);
                        }
                        commentReplyListAdapter.setData(commentReplyItemList);

                    } catch (Exception e) {
                        e.printStackTrace();
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
                    TwantApplication.getThreadPool().execute(new Runnable() {
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
                            Emoji emoji = emojiPage.emojiList.get(index);
                            SLog.info("emojiId[%d], emojiCode[%s]", emoji.emojiId, emoji.emojiCode);

                            Editable message = etReplyContent.getEditableText();
                            SLog.info("message[%s]", message);

                            // Get the selected text.
                            int start = etReplyContent.getSelectionStart();
                            int end = etReplyContent.getSelectionEnd();

                            Bitmap bitmap = BitmapFactory.decodeFile(emoji.absolutePath);
                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                            drawable.setBounds(0, 0,
                                    ((int) etReplyContent.getTextSize() + 12), ((int) etReplyContent.getTextSize() + 12));
                            QMUIAlignMiddleImageSpan span = new QMUIAlignMiddleImageSpan(drawable, QMUIAlignMiddleImageSpan.ALIGN_MIDDLE);


                            String emoticon = emoji.emojiCode;
                            // Insert the emoticon.
                            if (start < 0) {
                                start = 0;
                            }
                            if (end < 0) {
                                end = 0;
                            }
                            message.replace(start, end, emoticon);
                            SLog.info("message[%s]", message);


                            SLog.info("start[%d], stop[%d]", start, start + emoticon.length());
                            message.setSpan(span, start, start + emoticon.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            etReplyContent.setText(message);
                            // 重新定位光標
                            etReplyContent.setSelection(start + emoticon.length());
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
        List<Emoji> emojiList = LitePal.findAll(Emoji.class);
        if (emojiList == null) {
            return;
        }
        // 表情數
        int emojiCount = emojiList.size();
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
                emojiPage.emojiList.add(emojiList.get(j));
            }

            emojiPageList.add(emojiPage);
        }
    }
}

