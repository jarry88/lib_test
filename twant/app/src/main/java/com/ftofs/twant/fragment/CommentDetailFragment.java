package com.ftofs.twant.fragment;

import android.app.Instrumentation;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ftofs.twant.constant.UnicodeEmoji;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.entity.CommentReplyItem;
import com.ftofs.twant.entity.EmojiPage;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.entity.UnicodeEmojiItem;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SmoothInputLayout;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 評論詳情Fragment
 * @author zwm
 */
public class CommentDetailFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener, SimpleCallback , SmoothInputLayout.OnVisibilityChangeListener{
    CommentItem commentItem;
    private boolean popLogined=false;

    public static final int ACTION_VIEW_IMAGE = 1;

    // 评论的RecyclerView与图片索引的映射关系
    Map<Integer, Integer> rvPositionToImageIndexMap = new HashMap<>();
    List<String> imageList = new ArrayList<>();
    private View llEmojiPane;
    private int chanel=Constant.COMMENT_CHANNEL_STORE;
    private String authorName;

    public static CommentDetailFragment newInstance(CommentItem commentItem, int commentChannelPost,String name) {
        CommentDetailFragment fragment = newInstance(commentItem);
        fragment.chanel = commentChannelPost;
        fragment.authorName = name;
        return fragment;
    }

    View mainActivityContentView;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!User.isLogin()) {
            popLogined=false;
        }

        // 因为设置了fitsSystemWindows，需要将padding设置为0
        mainActivityContentView.setPadding(0, 0, 0, 0);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();

        // 因为其它页面没有设置fitsSystemWindows，将padding恢复为状态栏高度
        int statusBarHeight = Util.getStatusbarHeight(_mActivity);
        SLog.info("statusBarHeight[%d]", statusBarHeight);
        mainActivityContentView.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void onSimpleCall(Object data) {
        EasyJSONObject dataObj = (EasyJSONObject) data;

        try {
            int action = dataObj.getInt("action");
            int position = dataObj.getInt("position");
            if (action == ACTION_VIEW_IMAGE) {
                Integer imageIndex = rvPositionToImageIndexMap.get(position);
                if (imageIndex != null) {
                    Util.startFragment(ImageFragment.newInstance(imageIndex, imageList));
                }
            }
        } catch (EasyJSONException e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public static class QuoteReply {
        public boolean isQuoteReply;  // 是否為引用回覆，如果是引用回覆時，下面的字段才有用
        public String quoteNickname;  // 引用評論作者的昵稱
        public String quoteContent;   // 引用評論的內容
    }

    /**
     * canCommit用於防止快速點擊重覆提交
     */
    boolean canCommit = true;
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

    int replyCommentId;  // 當前要回覆的主題Id或二級評論Id
    CommentReplyListAdapter commentReplyListAdapter;
    List<CommentReplyItem> commentReplyItemList = new ArrayList<>();

    String commentImageUrl; // 評論圖片的URL

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

        mainActivityContentView = _mActivity.findViewById(android.R.id.content);

        SLog.info("3進入評論詳情頁");
        Bundle args = getArguments();
        commentItem = args.getParcelable("commentItem");


        // 默認為當前主題的Id
        replyCommentId = commentItem.commentId;

        silMainContainer = view.findViewById(R.id.sil_main_container);
        llEmojiPane = view.findViewById(R.id.ll_emoji_pane);
        silMainContainer.setOnVisibilityChangeListener(this);
        imgCommenterAvatar = view.findViewById(R.id.img_commenter_avatar);
        imgCommenterAvatar.setOnClickListener(this);
        tvCommenterNickname = view.findViewById(R.id.tv_commenter_nickname);
        tvCommentTime = view.findViewById(R.id.tv_comment_time);
        tvContent = view.findViewById(R.id.tv_content);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        imageView = view.findViewById(R.id.image_view);
        imageView.setOnClickListener(this);
        iconThumb = view.findViewById(R.id.icon_thumb);
        tvThumbCount = view.findViewById(R.id.tv_thumb_count);
        tvReplyCount = view.findViewById(R.id.tv_reply_count);
        etReplyContent = view.findViewById(R.id.et_reply_content);
        etReplyContent.setOnTouchListener(this);
        setReplyContentHint(commentItem.nickname);

        LinearLayout llReplyContainer = view.findViewById(R.id.ll_reply_container);
        commentReplyListAdapter = new CommentReplyListAdapter(_mActivity, llReplyContainer, R.layout.comment_reply_item, this);
        commentReplyListAdapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                int id = view.getId();

                CommentReplyItem item = commentReplyItemList.get(position);
                if (id == R.id.img_avatar) {
                    int userId = User.getUserId();
                    if (userId == item.memberId) { // 如果是自己頭像，顯示自己個人信息
                        start(PersonalInfoFragment.newInstance());
                    } else { // 如果是別人，顯示別人的城友信息
                        start(MemberInfoFragment.newInstance(item.memberName));
                    }
                } else if (id == R.id.btn_reply_comment) {
                    replyCommentId = item.commentId;
                    setReplyContentHint(item.nickname);

                    // 是引用回覆
                    quoteReply.isQuoteReply = true;
                    quoteReply.quoteNickname = item.nickname;
                    quoteReply.quoteContent = item.content;

                    showInput();
                } else if (id == R.id.btn_thumb) {
                    switchThumbState(position);
                }
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

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    item.isLike = 1 - item.isLike;
                    item.commentLike = responseObj.getInt("datas.likeCount");

                    commentReplyListAdapter.notifyItemChanged(position);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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

                // 切換為回覆當前的一級評論
                replyCommentId = commentItem.commentId;
                setReplyContentHint(commentItem.nickname);
                quoteReply.isQuoteReply = false;
                break;
            case R.id.et_reply_content:
                // 如果點擊輸入區域，將圖標切換為表情輸入圖標
                if (!popLogined&&!User.isLogin()) {
                    popLogined = true;
                    Util.showLoginFragment();
                } else {
                    btnEmoji.setSelected(false);
                    btnEmoji.setImageResource(R.drawable.icon_emoji);
                }
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
            hideSoftInputPop();
        } else if (id == R.id.btn_thumb) {
            if (!User.isLogin()) {
                SLog.info("登錄");
                start(LoginFragment.newInstance());
                return;
            }
            switchThumbState();
        } else if (id == R.id.btn_commit) {
            if (!User.isLogin()) {
                start(LoginFragment.newInstance());
                return;
            }

            if (!canCommit) {
                // 不能重覆提交，返回
                return;
            }

            String replyContent = etReplyContent.getText().toString().trim();
            if (StringUtil.isEmpty(replyContent)) {
                ToastUtil.error(_mActivity, "回覆不能為空");
                return;
            }

            String filteredReplyContent = StringUtil.filterCommentContent(replyContent);
            EasyJSONObject params = EasyJSONObject.generate(
                    "commentChannel", commentItem.commentChannel,
                    "deep", 2,
                    "content", filteredReplyContent,
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
                if (chanel == Constant.COMMENT_CHANNEL_POST) {
                    if (authorName != null) {
                        params.set("postCreateBy", authorName);
                    } else if(Config.USE_DEVELOPER_TEST_DATA){
                            params.set("postCreateBy", "u_001315344758");
                    }
                }
            } catch (Exception e) {

            }

            String token = User.getToken();
            String path = Api.PATH_PUBLISH_COMMENT + Api.makeQueryString(EasyJSONObject.generate("token", token));
            SLog.info("path[%s], params[%s]", path, params.toString());
            canCommit = false;
            Api.postJsonUi(path, params.toString(), new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    canCommit = true;
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        canCommit = true;
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        ToastUtil.success(_mActivity, "回覆成功");
                        loadCommentDetail();
//                        EasyJSONObject wantCommentVo = responseObj.getSafeObject("datas.wantCommentVo");
//                        EasyJSONObject memberVo = wantCommentVo.getSafeObject("memberVo");
//
//                        CommentReplyItem commentReplyItem = new CommentReplyItem();
//                        commentReplyItem.memberId = memberVo.getInt("memberId");
//                        commentReplyItem.memberName = memberVo.getSafeString("memberName");
//                        commentReplyItem.commentId = wantCommentVo.getInt("commentId");
//                        commentReplyItem.avatarUrl = memberVo.getSafeString("avatar");
//                        commentReplyItem.nickname = memberVo.getSafeString("nickName");
//                        commentReplyItem.createTime = wantCommentVo.getLong("createTime");
//                        // commentReplyItem.content = wantCommentVo.getSafeString("content"); 这里获取过来的Unicode表情有问题
//                        // SLog.info("commentReplyItem.content[%s]", commentReplyItem.content);
//                        commentReplyItem.content = filteredReplyContent;
//                        SLog.info("commentReplyItem.content[%s]", commentReplyItem.content);
//                        commentReplyItem.isLike = wantCommentVo.getInt("isLike");
//                        commentReplyItem.commentLike = wantCommentVo.getInt("commentLike");
//
//                        if (quoteReply.isQuoteReply) {
//                            commentReplyItem.isQuoteReply = true;
//                            commentReplyItem.quoteNickname = quoteReply.quoteNickname;
//                            commentReplyItem.quoteContent = quoteReply.quoteContent;
//                        }
//
//                        commentReplyItemList.add(0, commentReplyItem);
//                        commentReplyListAdapter.notifyItemInserted(0);
//
//                        etReplyContent.setText("");
//                        silMainContainer.closeKeyboard(true);
//                        silMainContainer.closeInputPane();
//
//                        quoteReply.isQuoteReply = false;
                        if (chanel == Constant.COMMENT_CHANNEL_POST && responseObj.exists("datas.voucherList")) {
                            ArrayList<StoreVoucher> voucherList = new ArrayList<>();
                            for (Object o : responseObj.getSafeArray("datas.voucherList")) {
                                EasyJSONObject voucher = (EasyJSONObject) o;
                                voucherList.add(StoreVoucher.parse(voucher));
                            }
                            if (voucherList.size() > 0) {
                                String zoneId = responseObj.getSafeString("datas.zoneId");
                                hideSoftInput();
                                new XPopup.Builder(_mActivity)
                                        .moveUpToKeyboard(false)
                                        .asCustom(new MoonVoucherListPopup(_mActivity,voucherList,zoneId))
                                        .show();
                            }
                        }
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
        } else if (id == R.id.image_view) {
            if (!StringUtil.isEmpty(commentImageUrl)) {
                // start(ImageViewerFragment.newInstance(commentImageUrl));
                List<String> contentImageList = new ArrayList<>();
                contentImageList.add(StringUtil.normalizeImageUrl(commentImageUrl));
                start(ImageFragment.newInstance(0, contentImageList));
            }
        } else if (id == R.id.img_commenter_avatar) {
            if (!User.isLogin()) {
                start(LoginFragment.newInstance());
                return;
            }
            start(MemberInfoFragment.newInstance(commentItem.memberName));
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
        hideSoftInputPop();
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

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(commentItem.commenterAvatar)).centerCrop().into(imgCommenterAvatar);
                        tvCommenterNickname.setText(commentItem.nickname);
                        tvCommentTime.setText(commentItem.commentTime);
                        tvContent.setText(StringUtil.translateEmoji(_mActivity, commentItem.content, (int) tvContent.getTextSize(), new SimpleCallback() {
                            @Override
                            public void onSimpleCall(Object data) {
                                String url = (String) data;
                                StringUtil.parseCustomUrl(_mActivity, url);
                            }
                        }));
                        if (commentItem.commentType == Constant.COMMENT_TYPE_TEXT || StringUtil.isEmpty(commentItem.imageUrl)) {
                            imageView.setVisibility(View.GONE);
                        } else {
                            commentImageUrl = StringUtil.normalizeImageUrl(commentItem.imageUrl);
                            Glide.with(_mActivity).load(commentImageUrl).centerCrop().into(imageView);
                            imageView.setVisibility(View.VISIBLE);
                        }

                        updateThumbState();


                        // 回覆列表
                        EasyJSONArray replyList = responseObj.getSafeArray("datas.replyList");
                        commentReplyItemList.clear();
                        for (Object object : replyList) {
                            EasyJSONObject reply = (EasyJSONObject) object;

                            CommentReplyItem item = new CommentReplyItem();
                            item.memberId = reply.getInt("memberVo.memberId");
                            item.memberName = reply.getSafeString("memberVo.memberName");
                            item.commentId = reply.getInt("commentId");
                            item.avatarUrl = reply.getSafeString("memberVo.avatar");
                            item.nickname = reply.getSafeString("memberVo.nickName");
                            item.createTime = reply.getLong("createTime");
                            item.content = reply.getSafeString("content");
                            item.commentRole = reply.getInt("commentRole");
                            if (StringUtil.isEmpty(item.content)) {
                                item.content = "";
                            }
                            EasyJSONArray images = reply.getSafeArray("images");
                            String imageUrl = "";  // 目前只支持1張圖片
                            for (Object object2 : images) {
                                EasyJSONObject image = (EasyJSONObject) object2;
                                imageUrl = image.getSafeString("imageUrl");
                                item.imageList.add(imageUrl);
                            }
                            if (!StringUtil.isEmpty(imageUrl)) {
                                rvPositionToImageIndexMap.put(commentReplyItemList.size(), imageList.size());
                                imageList.add(StringUtil.normalizeImageUrl(imageUrl));
                            }


                            item.commentLike = reply.getInt("commentLike");
                            item.isLike = reply.getInt("isLike");

                            EasyJSONObject wantCommentReplyVo = reply.getSafeObject("wantCommentReplyVo");
                            SLog.info("wantCommentReplyVo[%s]", wantCommentReplyVo);
                            if (!Util.isJsonObjectEmpty(wantCommentReplyVo)) {
                                item.isQuoteReply = true;
                                item.quoteNickname = wantCommentReplyVo.getSafeString("nickName");
                                item.quoteContent = wantCommentReplyVo.getSafeString("content");
                            }

                            commentReplyItemList.add(item);
                        }
                        commentReplyListAdapter.setData(commentReplyItemList);
                        tvReplyCount.setText(String.format(getString(R.string.text_view_reply_count), commentReplyItemList.size()));

                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    commentItem.isLike = 1 - commentItem.isLike;
                    commentItem.commentLike = responseObj.getInt("datas.likeCount");

                    updateThumbState();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updateThumbState() {
        if (commentItem.isLike == 1) {
            iconThumb.setImageResource(R.drawable.icon_thumb_red_60);
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

                            Editable message = etReplyContent.getEditableText();
                            SLog.info("message[%s]", message);

                            // Get the selected text.
                            int start = etReplyContent.getSelectionStart();
                            int end = etReplyContent.getSelectionEnd();

                            // Insert the emoticon.
                            if (start < 0) {
                                start = 0;
                            }
                            if (end < 0) {
                                end = 0;
                            }
                            message.replace(start, end, emojiItem.emoji);
                            SLog.info("message[%s]", message);


                            etReplyContent.setText(message);
                            // 重新定位光標
                            etReplyContent.setSelection(start + emojiItem.emoji.length());
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
    public void onVisibilityChange(int visibility) {
        if (visibility == View.GONE) {
            btnEmoji.setSelected(false);
        } else {
            btnEmoji.setSelected(llEmojiPane.getVisibility() == View.VISIBLE);
        }
    }
}

