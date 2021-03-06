package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.lib_net.model.StoreLabel;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PostCommentListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.DataImageView;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.SquareGridLayout;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.sxu.shadowdrawable.ShadowDrawable;

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
 * 想要帖詳情Fragment
 * @author zwm
 */
public class PostDetailFragment extends BaseFragment implements View.OnClickListener  ,BaseQuickAdapter.RequestLoadMoreListener{
    public static final int STATE_TYPE_THUMB = 1;
    public static final int STATE_TYPE_LIKE = 2;

    int postId;
    int memberId;

    String coverUrl;
    String title;
    String content;

    ImageView iconComeTrue;
    TextView btnMakeTrue;
    boolean isComeTrue;

    TextView tvPostTitle;
    ImageView imgAuthorAvatar;
    TextView tvNickname;
    TextView tvCreatetime;
    TextView tvContent;

    SquareGridLayout sglImageContainer;
    TextView tvViewCount;
    TextView tvReplyCount;
    LinearLayout llCommentContainer;
    ImageView imgThumb;
    TextView tvThumbCount;
    ImageView imgLike;
    TextView tvLikeCount;
    ImageView btnDelete;

    String authorMemberName;
    int isLike;
    int isFavor;

    int postComment;
    ImageView btnMenu;

    LinearLayout llPostGoodsContainer;
    ImageView postGoodsImage;
    TextView tvPostGoodsName;
    TextView tvGoodsPrice;

    LinearLayout llContentContainer;
    View blankView;

    PostCommentListAdapter adapter;
    List<CommentItem> commentItemList = new ArrayList<>();

    List<String> postImageList = new ArrayList<>();

    int commonId;
    private boolean isMe;

    // 想要貼裏面的店鋪Id
    int storeId = 0;
    LinearLayout llPostStoreContainer;

    // 评论的RecyclerView与图片索引的映射关系
    Map<Integer, Integer> rvPositionToImageIndexMap = new HashMap<>();
    // 圖片列表
    List<String> imageList = new ArrayList<>();
    private int currPage=0;
    boolean hasMore;
    private RecyclerView rvCommentContainer;


    public static PostDetailFragment newInstance(int postId) {
        Bundle args = new Bundle();

        args.putInt("postId", postId);
        PostDetailFragment fragment = new PostDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        btnMenu = view.findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(this);
        postId = args.getInt("postId", 0);
        iconComeTrue = view.findViewById(R.id.icon_come_true);
        tvPostTitle = view.findViewById(R.id.tv_post_title);
        imgAuthorAvatar = view.findViewById(R.id.img_author_avatar);
        imgAuthorAvatar.setOnClickListener(this);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvCreatetime = view.findViewById(R.id.tv_create_time);
        tvContent = view.findViewById(R.id.tv_content);

        llContentContainer = view.findViewById(R.id.ll_content_container);
        blankView = view.findViewById(R.id.vw_blank);

        btnDelete = view.findViewById(R.id.icon_post_delete);
        btnDelete.setOnClickListener(this);
        btnMakeTrue = view.findViewById(R.id.btn_make_true);
        btnMakeTrue.setOnClickListener(this);
        llPostStoreContainer = view.findViewById(R.id.ll_post_store_container);
        llPostGoodsContainer = view.findViewById(R.id.ll_post_goods_container);
        ShadowDrawable.setShadowDrawable(llPostStoreContainer, Color.parseColor("#FFFFFF"), Util.dip2px(_mActivity, 5),
                Color.parseColor("#19000000"), Util.dip2px(_mActivity, 5), 0, 0);
        ShadowDrawable.setShadowDrawable(llPostGoodsContainer, Color.parseColor("#FFFFFF"), Util.dip2px(_mActivity, 5),
                Color.parseColor("#19000000"), Util.dip2px(_mActivity, 5), 0, 0);
        llPostStoreContainer.setOnClickListener(this);
        llPostGoodsContainer.setOnClickListener(this);

        postGoodsImage = view.findViewById(R.id.post_goods_image);
        tvPostGoodsName = view.findViewById(R.id.tv_post_goods_name);
        tvGoodsPrice = view.findViewById(R.id.tv_goods_price_left);

        sglImageContainer = view.findViewById(R.id.sgl_image_container);

        tvViewCount = view.findViewById(R.id.tv_view_count);
        tvReplyCount = view.findViewById(R.id.tv_reply_count);
        llCommentContainer = view.findViewById(R.id.ll_comment_container);
        rvCommentContainer = view.findViewById(R.id.rl_comment_container);
        rvCommentContainer.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PostCommentListAdapter(_mActivity,  R.layout.post_comment_item,commentItemList);
        rvCommentContainer.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view1, position) -> {
            CommentItem commentItem = commentItemList.get(position);
            start(CommentDetailFragment.newInstance(commentItem,Constant.COMMENT_CHANNEL_POST,authorMemberName));
        });
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this,rvCommentContainer);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                SLog.info("id[%d]", id);
                if (!User.isLogin()) {
                    Util.showLoginFragment(getContext());

                    return;
                }
                SLog.info("id[%d]", id);
                CommentItem commentItem = commentItemList.get(position);
                if (id == R.id.img_commenter_avatar) {
                    start(MemberInfoFragment.newInstance(commentItem.memberName));
                } else if (id == R.id.btn_reply) {
                    SLog.info("id[%d]", id);
                    start(CommentDetailFragment.newInstance(commentItem, Constant.COMMENT_CHANNEL_POST, authorMemberName));
                } else if (id == R.id.btn_thumb) {
                    switchThumbState(position);
                } else if (id == R.id.btn_make_true) {
                    commentMakeTrue(commentItem.commentId);
                } else if (id == R.id.image_view) {
                    Integer imageIndex = rvPositionToImageIndexMap.get(position);
                    if (imageIndex != null) {
                        start(ImageFragment.newInstance(imageIndex, imageList));
                    }
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
        loadCommentList(currPage+1);
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }else if(id ==R.id.btn_menu) {
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 11))
                    .offsetY(-Util.dip2px(_mActivity, 1))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_POST_DETAIL))
                    .show();
        } else if (id ==R.id.icon_post_delete) {

            new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                    // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                  .asCustom(new TwConfirmPopup(_mActivity, "您確認要刪除貼文?",null, "確認", "取消",new OnConfirmCallback() {
                @Override
                public void onYes() {
                    deletePost();
                }

                @Override
                public void onNo() {
                    SLog.info("onNo");
                }
                    })).show();
        } else if (id == R.id.btn_add_post_comment) {
            if (!User.isLogin()) {
                Util.showLoginFragment(requireContext());
                return;
            }
            startForResult(AddCommentFragment.newInstance(postId, Constant.COMMENT_CHANNEL_POST,authorMemberName), RequestCode.ADD_COMMENT.ordinal());
        } else if (id == R.id.btn_thumb) {
            if (!User.isLogin()) {
                Util.showLoginFragment(requireContext());

                return;
            }
            switchInteractiveState(STATE_TYPE_THUMB);
        } else if (id == R.id.btn_like) {
            if (!User.isLogin()) {
                Util.showLoginFragment(requireContext());

                return;
            }
            switchInteractiveState(STATE_TYPE_LIKE);
        } else if (id == R.id.btn_share) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new SharePopup(_mActivity, SharePopup.generatePostShareLink(postId), title, content, coverUrl, null))
                    .show();
        } else if (id == R.id.btn_make_true) {
            String url = Api.PATH_POST_COME_TRUE + "/" + postId;

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                Util.showLoginFragment(requireContext());
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "postId", postId);

            SLog.info("params[%s]", params);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.success(_mActivity, "想要成真成功");
                    setComeTrue();
                }
            });
        } else if (id == R.id.img_author_avatar) {
            if (!StringUtil.isEmpty(authorMemberName)) {
                if (User.isLogin()) {
                    if (isMe) {
                        start(MyFragment.newInstance(true));
                    } else {
                        start(AuthorInfoFragment.newInstance(authorMemberName));
                    }
                    //start(MemberInfoFragment.newInstance(authorMemberName));
                } else {
                    Util.showLoginFragment(requireContext());
                }
            }
        } else if (id == R.id.ll_post_goods_container) {
            Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
        } else if (id == R.id.ll_post_store_container) {
            Util.startFragment(ShopMainFragment.newInstance(storeId));
        }
    }

    private void setComeTrue() {
        SLog.info("setComeTrue");
        isComeTrue = true;

        btnMakeTrue.setVisibility(View.GONE);
        iconComeTrue.setVisibility(View.VISIBLE);

        adapter.setComeTrue(true);
        adapter.notify();
    }

    private void commentMakeTrue(int commentId) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment(requireContext());
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token,
                "postId", postId,
                "commentId", commentId);
        Api.postUI(Api.PATH_COMMENT_COME_TRUE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                ToastUtil.success(_mActivity, "想要成真成功");

                setComeTrue();
            }
        });
    }
    private void deletePost() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment(requireContext());
            return;
        }

        String url = Api.PATH_WANT_POST_DELETE + "/" + postId;
        EasyJSONObject params = EasyJSONObject.generate("token", token,
                "postId", postId);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_ADD_POST, null);
                ToastUtil.success(_mActivity, "刪除成功");

                pop();
            }
        });
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
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

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
                        int favorCount = responseObj.getInt("datas.favorCount");
                        if (isFavor == 1) {
                            tvLikeCount.setText(_mActivity.getString(R.string.text_followed));
                            tvLikeCount.setTextColor(_mActivity.getColor(R.color.tw_yellow));
                            imgLike.setImageResource(R.drawable.icon_store_favorite_yellow);
                        } else {
                            imgLike.setImageResource(R.drawable.icon_post_like_black);
                            tvLikeCount.setText(String.valueOf(favorCount));
                            tvLikeCount.setTextColor(_mActivity.getColor(R.color.tw_black));
                        }

                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    llContentContainer.setVisibility(View.GONE);
                    blankView.setVisibility(View.VISIBLE);
                    return;
                }

                try {
                    EasyJSONObject wantPostVoInfo = (EasyJSONObject) responseObj.get("datas.wantPostVoInfo");
                    EasyJSONObject memberVo = wantPostVoInfo.getSafeObject("memberVo");

                    coverUrl = wantPostVoInfo.getSafeString("coverImage");

                    title = wantPostVoInfo.getSafeString("postCategory") + " | " + wantPostVoInfo.getSafeString("title");
                    tvPostTitle.setText(title);

                    String avatarUrl = memberVo.getSafeString("avatar");
                    if (StringUtil.isEmpty(avatarUrl)) {
                        Glide.with(_mActivity).load(R.drawable.grey_default_avatar).centerCrop().into(imgAuthorAvatar);
                    } else {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAuthorAvatar);
                    }

                    memberId = memberVo.getInt("memberId");

                    int comeTrueState = wantPostVoInfo.getInt("comeTrueState");
                    isComeTrue = comeTrueState == Constant.TRUE_INT;
                    SLog.info("isComeTrue[%s]", isComeTrue);
                    iconComeTrue.setVisibility(isComeTrue?View.VISIBLE:View.GONE);

                    if (!isComeTrue && memberId == User.getUserId()) {
                        btnMakeTrue.setVisibility(View.VISIBLE);
                    } else {
                        btnMakeTrue.setVisibility(View.GONE);
                    }

                    String nickname = memberVo.getSafeString("nickName");
                    tvNickname.setText(nickname);

                    authorMemberName = memberVo.getSafeString("memberName");
                    isMe = authorMemberName.equals(User.getUserInfo(SPField.FIELD_MEMBER_NAME, null));
                    SLog.info("isMe %s",isMe);
                    btnDelete.setVisibility(isMe?View.VISIBLE:View.GONE);
                    String createTime = wantPostVoInfo.getSafeString("createTime");
                    tvCreatetime.setText(createTime);

                    content = wantPostVoInfo.getSafeString("content");

                    Editable emojiTranslatedContent = StringUtil.translateEmoji(_mActivity, content, (int) tvContent.getTextSize(), new SimpleCallback() {
                        @Override
                        public void onSimpleCall(Object data) {
                            SLog.info("data[%s]", data);

                            String url = (String) data;
                            StringUtil.parseCustomUrl(_mActivity, url);
                        }
                    });
                    tvContent.setMovementMethod(LinkMovementMethod.getInstance()); // 需要設置MovementMethod，不然不能點擊鏈接
                    tvContent.setText(emojiTranslatedContent);

                    postComment = wantPostVoInfo.getInt("postReply");
                    updatePostCommentCount();

                    int imageIndex = 0;
                    EasyJSONArray wantPostImages = wantPostVoInfo.getSafeArray("wantPostImages");
                    for (Object object : wantPostImages) {
                        EasyJSONObject imageObj = (EasyJSONObject) object;

                        String imageUrl = imageObj.getSafeString("imageUrl");
                        DataImageView imageView = new DataImageView(_mActivity);
                        imageView.setCustomData(imageIndex);

                        sglImageContainer.addImageView(imageView, null, imageUrl);
                        postImageList.add(StringUtil.normalizeImageUrl(imageUrl));
                        ++imageIndex;
                    }
                    sglImageContainer.imageList = postImageList;

                    EasyJSONArray wantPostGoodsVoList = wantPostVoInfo.getSafeArray("wantPostGoodsVoList");
                    if (wantPostGoodsVoList.length() > 0) {
                        EasyJSONObject wantPostGoodsVo = wantPostGoodsVoList.getObject(0);

                        commonId = wantPostGoodsVo.getInt("commonId");

                        String goodsName = wantPostGoodsVo.getSafeString("goodsName");
                        tvPostGoodsName.setText(goodsName);

                        String imageUrl = wantPostGoodsVo.getSafeString("goodsImage");
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(imageUrl)).centerCrop().into(postGoodsImage);
                        float goodsPrice = (float) wantPostGoodsVo.getDouble("goodsPrice");
                        boolean noPrice = StringUtil.safeModel(wantPostGoodsVo) == Constant.GOODS_TYPE_CONSULT;
                        tvGoodsPrice.setText(noPrice?"問價":StringUtil.formatPrice(_mActivity, goodsPrice, 0));

                        llPostGoodsContainer.setVisibility(View.VISIBLE);
                    } else {
                        llPostGoodsContainer.setVisibility(View.GONE);
                    }


                    // 看是否有店鋪數據
                    if (wantPostVoInfo.exists("shareStore")) {
                        EasyJSONObject shareStore = wantPostVoInfo.getObject("shareStore");
                        if (!Util.isJsonNull(shareStore)) {
                            storeId = shareStore.getInt("storeId");
                            String storeAvatar = shareStore.getSafeString("storeFigureImage");
                            String storeName = shareStore.getSafeString("storeName");
                            String storeSignature = shareStore.getSafeString("storeSignature");

                            ImageView imgStoreAvatar = llPostStoreContainer.findViewById(R.id.post_store_image);
                            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(storeAvatar)).centerCrop().into(imgStoreAvatar);

                            TextView tvStoreName = llPostStoreContainer.findViewById(R.id.tv_post_store_name);
                            tvStoreName.setText(storeName);

                            TextView tvStoreSignature = llPostStoreContainer.findViewById(R.id.tv_store_signature);
                            tvStoreSignature.setText(storeSignature);

                            llPostStoreContainer.setVisibility(View.VISIBLE);
                        }
                    }


                    int postView = wantPostVoInfo.getInt("postView"); // 瀏覽次數
                    int postLike = wantPostVoInfo.getInt("postLike");  // 讚想次數
                    int postFavor = wantPostVoInfo.getInt("postFavor");  // 喜歡次數

                    tvViewCount.setText("瀏覽" + StringUtil.formatPostView(postView) + "次");

                    isLike = wantPostVoInfo.getInt("isLike");
                    if (isLike == 1) {
                        imgThumb.setImageResource(R.drawable.icon_post_thumb_blue);
                    } else {
                        imgThumb.setImageResource(R.drawable.icon_post_thumb_black);
                    }

                    isFavor = wantPostVoInfo.getInt("isFavor");
                    updatePostFollow(postFavor);

                    if (postLike > 0) {
                        tvThumbCount.setText(String.valueOf(postLike));
                    } else {
                        tvThumbCount.setText("");
                    }

//                    adapter.setData(adapter.getDataList());
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updatePostFollow(int postFavor) {
        if (isFavor == 1) {
            tvLikeCount.setText(_mActivity.getString(R.string.text_followed));
            tvLikeCount.setTextColor(_mActivity.getColor(R.color.tw_yellow));
            imgLike.setImageResource(R.drawable.icon_store_favorite_yellow);
        } else {
            imgLike.setImageResource(R.drawable.icon_post_like_black);
            tvLikeCount.setText(String.valueOf(postFavor));
            tvLikeCount.setTextColor(_mActivity.getColor(R.color.tw_black));
        }
    }

    private void updatePostCommentCount() {
        if (isAdded()) {  //  修復：Fragment not attached to a context. 的異常
            tvReplyCount.setText(String.format(getString(R.string.text_view_comment_count), postComment));
        }
    }

    private void loadCommentList(int page) {
        String token = User.getToken();

        EasyJSONObject params = EasyJSONObject.generate(
                "channel", Constant.COMMENT_CHANNEL_POST,
                "bindId", postId,
                "page",currPage);

        if (!StringUtil.isEmpty(token)) {
            try {
                params.set("token", token);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    if (page == 1) {
                        commentItemList.clear();
                    }
                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    if (!hasMore) {
                        adapter.loadMoreEnd();
                        adapter.setEnableLoadMore(false);
                    }
                    EasyJSONArray comments = responseObj.getSafeArray("datas.comments");
                    List<CommentItem> list = new ArrayList<CommentItem>();
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
                        item.relatePostId = postId;

                        if (item.commentType != Constant.COMMENT_TYPE_TEXT) {
                            EasyJSONArray images = comment.getSafeArray("images");
                            if (images != null) {
                                for (Object object2 : images) {
                                    EasyJSONObject image = (EasyJSONObject) object2;
                                    item.imageUrl = image.getSafeString("imageUrl");

                                    rvPositionToImageIndexMap.put(commentItemList.size(), imageList.size());
                                    imageList.add(StringUtil.normalizeImageUrl(item.imageUrl));
                                }
                            }
                        }

                        list.add(item);
                    }

                    adapter.setComeTrue(isComeTrue);
                    adapter.addData(list);
                    adapter.loadMoreComplete();
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void reloadData() {
        currPage = 0;
        loadCommentList(currPage + 1);
    }
    /**
     * 上滑加載更多
     */
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
                    // 本地更新點贊數
                    int newLikeCount = commentItem.commentLike;
                    if (commentItem.isLike == Constant.TRUE_INT) {
                        newLikeCount++;
                    } else {
                        newLikeCount--;
                        if (newLikeCount < 0) {
                            newLikeCount = 0;
                        }
                    }
                    commentItem.commentLike = newLikeCount;

                    adapter.notifyItemChanged(position);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
        if (requestCode == RequestCode.ADD_COMMENT.ordinal()) {
//            if (data.get("voucherList") != null) {//屏蔽中秋活动弹窗
//                String zoneId=data.getString("zoneId");
//                ArrayList<StoreVoucher> vouchers = data.getParcelableArrayList("voucherList");
//                new XPopup.Builder(_mActivity)
//                        .moveUpToKeyboard(false)
//                        .asCustom(new MoonVoucherListPopup(_mActivity,vouchers,zoneId))
//                        .show();
//            }

            tvPostTitle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currPage = 0;
                    loadCommentList(1);
                    postComment++;
                    updatePostCommentCount();
                }
            }, 1500); // 有時候重新加載的時候，也沒有從服務端加載到最新的數據，延遲個1500毫秒
        }
    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadCommentList(currPage + 1);
    }
}
