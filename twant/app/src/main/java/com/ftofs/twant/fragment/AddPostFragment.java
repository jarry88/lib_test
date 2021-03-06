package com.ftofs.twant.fragment;

import android.app.Instrumentation;
import android.app.Service;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.EmojiPageAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.UnicodeEmoji;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.EmojiPage;
import com.ftofs.lib_net.model.Goods;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.ftofs.twant.entity.UnicodeEmojiItem;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.task.TaskObservable;
import com.gzp.lib_common.task.TaskObserver;
import com.gzp.lib_common.utils.FileUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.lib_common_ui.popup.ListPopup;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.SoftToolPaneLayout;
import com.ftofs.twant.widget.SquareGridLayout;
import com.ftofs.twant.widget.TouchEditText;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.sxu.shadowdrawable.ShadowDrawable;

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
 * 發表想要Fragment
 * @author zwm
 */
public class AddPostFragment extends BaseFragment implements
        View.OnClickListener, OnSelectedListener, SimpleCallback {
    BaseQuickAdapter adapter;

    public static final int POST_CATEGORY_INDEX_GOODS = 0;
    public static final int POST_CATEGORY_INDEX_STORE = 1;
    public static final int POST_CATEGORY_INDEX_SUGGEST = 2;

    // 最多能添加9張圖片
    public static final int MAX_IMAGE_COUNT = 9;

    boolean fromWeb;
    View currentDragView;

    RecyclerView rvEmojiPageList;
    EmojiPageAdapter emojiPageAdapter;
    List<EmojiPage> emojiPageList = new ArrayList<>();

    Goods goods; // 想要帖關聯的產品
    boolean goodsShown = false; // 產品是否已顯示出來

    int storeId; // 店鋪分享時的店鋪Id
    int commonId; // 商品分享時的商品Id

    List<String> postCategoryName = new ArrayList<>();
    int selectedCategoryIndex = POST_CATEGORY_INDEX_GOODS;

    SoftToolPaneLayout stplContainer;


    TouchEditText etTitle;
    TextView tvTitleWordCount;
    TouchEditText etContent;

    TextView tvPostCategory;
    ImageView btnAddPostContentImage;
    SquareGridLayout postContentImageContainer;

    ScaledButton btnDeletePostGoods;

    BasePopupView inProgressPopup;

    ScrollView svContainer;

    int joinState = 0; // 是否参与圣诞活动  0 -- 不参与  1 -- 参与
    int originalJoinState = 0;

    private static final int BTN_EMOJI_USAGE_EMOJI = 0;
    private static final int BTN_EMOJI_USAGE_SOFT_INPUT = 1;
    ImageView btnInsertPostEmoji;
    int usageBtnEmoji = BTN_EMOJI_USAGE_EMOJI; // btnEmoji的用途 0 -- 顯示表情圖標 1 -- 顯示軟鍵盤圖標


    LinearLayout btnAddPostGoods;

    LinearLayout llToolContainer;
    LinearLayout llBottomToolbar;
    LinearLayout llEmojiPane;


    LinearLayout llPostGoodsContainer;

    ImageView postGoodsImage;
    TextView tvPostGoodsName;
    TextView tvGoodsPrice;

    int shareType = 0;
    EasyJSONObject shareData;
    LinearLayout llPostStoreContainer;

    boolean commitButtonEnable = true;
    private String successText ="提交成功";
    private EasyJSONObject responseObj;


    /**
     * Ctor
     * @param fromWeb 是否由網頁跳過來
     * @return
     */
    public static AddPostFragment newInstance(boolean fromWeb) {
        Bundle args = new Bundle();

        args.putBoolean("fromWeb", fromWeb);
        AddPostFragment fragment = new AddPostFragment();
        fragment.setArguments(args);

        return fragment;
    }


    /**
     * Ctor
     * @param fromWeb 是否由網頁跳過來
     * @return
     */
    public static AddPostFragment newInstance(EasyJSONObject shareData, boolean fromWeb) {
        Bundle args = new Bundle();

        args.putBoolean("fromWeb", fromWeb);
        AddPostFragment fragment = new AddPostFragment();
        fragment.setShareData(shareData);
        fragment.setArguments(args);

        return fragment;
    }

    public void setShareData(EasyJSONObject shareData) {
        this.shareData = shareData;
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        fromWeb = args.getBoolean("fromWeb");
        if (fromWeb) { // 如果是來自網頁，默認勾選
            joinState = 1;
        }
        SLog.info("joinState[%d]", joinState);


        inProgressPopup = new XPopup.Builder(_mActivity)
                .asLoading(getString(R.string.text_committing));

        stplContainer = view.findViewById(R.id.stpl_container);

        tvTitleWordCount = view.findViewById(R.id.tv_title_word_count);
        etTitle = view.findViewById(R.id.et_title);
        etTitle.setSimpleCallback(this);
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int wordCount = s.length();
                tvTitleWordCount.setText(String.format("%d/60", wordCount));
            }
        });


        btnInsertPostEmoji = view.findViewById(R.id.btn_insert_post_emoji);
        btnInsertPostEmoji.setOnClickListener(this);
        etContent = view.findViewById(R.id.et_content);
        etContent.setSimpleCallback(this);

        llToolContainer = view.findViewById(R.id.ll_tool_container);
        llBottomToolbar = view.findViewById(R.id.ll_bottom_toolbar);
        llEmojiPane = view.findViewById(R.id.ll_emoji_pane);

        postContentImageContainer = view.findViewById(R.id.post_content_image_container);
        tvPostCategory = view.findViewById(R.id.tv_post_category);
        postContentImageContainer.setOnDragListener((v, event) -> {
            // SLog.info("containerLayout", "event.getAction():" + event.getAction());
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENDED: //拖拽停止
                    //your operation
                    currentDragView.setVisibility(View.VISIBLE);
                    SLog.info("DragEvent.ACTION_DRAG_ENDED");
                    updateIndexTagData();
                    break;
                case DragEvent.ACTION_DROP://拖拽中
                    //your operation
                    SLog.info("DragEvent.ACTION_DROP");
                    currentDragView.setVisibility(View.VISIBLE);
                    updateIndexTagData();
                    break;
                default:
                    // SLog.info("DragEvent.ACTION_DEFAULT[%d]", event.getAction());
                    break;
            }
            return true;
        });

        llPostGoodsContainer = view.findViewById(R.id.ll_post_goods_container);
        ShadowDrawable.setShadowDrawable(llPostGoodsContainer, Color.parseColor("#FFFFFF"), Util.dip2px(_mActivity, 5),
                Color.parseColor("#19000000"), Util.dip2px(_mActivity, 5), 0, 0);
        llPostStoreContainer = view.findViewById(R.id.ll_post_store_container);
        ShadowDrawable.setShadowDrawable(llPostStoreContainer, Color.parseColor("#FFFFFF"), Util.dip2px(_mActivity, 5),
                Color.parseColor("#19000000"), Util.dip2px(_mActivity, 5), 0, 0);

        postGoodsImage = view.findViewById(R.id.post_goods_image);
        tvPostGoodsName = view.findViewById(R.id.tv_post_goods_name);
        tvGoodsPrice = view.findViewById(R.id.tv_goods_price_left);

        btnAddPostContentImage = view.findViewById(R.id.btn_add_post_content_image);
        btnAddPostContentImage.setOnClickListener(this);
        Util.setOnClickListener(view, R.id.btn_commit, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_select_post_category, this);

        btnDeletePostGoods = view.findViewById(R.id.btn_delete_post_goods);
        btnDeletePostGoods.setOnClickListener(this);

        svContainer = view.findViewById(R.id.sv_container);

        btnAddPostGoods = view.findViewById(R.id.btn_add_post_goods);
        btnAddPostGoods.setOnClickListener(this);

        stplContainer.setStatusChangedListener(new SoftToolPaneLayout.OnStatusChangedListener() {
            @Override
            public void onStatusChanged(int oldStatus, int newStatus) {
                SLog.info("oldStatus[%d], newStatus[%d]", oldStatus, newStatus);
                if (newStatus == SoftToolPaneLayout.STATUS_NONE_SHOWN) {
                    llToolContainer.setVisibility(View.GONE);
                }
            }
        });

        if (shareData != null) {
            // 如果是商品分享或店鋪分享，則隱藏【選擇商品】按鈕
            btnAddPostGoods.setVisibility(View.GONE);

            try {
                shareType = shareData.getInt("shareType");
                if (shareType == SharePopup.SHARE_TYPE_STORE) {
                    storeId = shareData.getInt("storeId");
                    String storeAvatar = shareData.getSafeString("storeAvatar");
                    String storeName = shareData.getSafeString("storeName");
                    String storeSignature = shareData.getSafeString("storeSignature");

                    ImageView imgStoreAvatar = view.findViewById(R.id.post_store_image);
                    Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(storeAvatar)).centerCrop().into(imgStoreAvatar);
                    TextView tvStoreName = view.findViewById(R.id.tv_post_store_name);
                    tvStoreName.setText(storeName);
                    TextView tvStoreSignature = view.findViewById(R.id.tv_store_signature);
                    tvStoreSignature.setText(storeSignature);

                    selectedCategoryIndex = POST_CATEGORY_INDEX_STORE;

                    llPostStoreContainer.setVisibility(View.VISIBLE);
                } else if (shareType == SharePopup.SHARE_TYPE_GOODS) {
                    commonId = shareData.getInt("commonId");
                    String goodsImage = shareData.getSafeString("goodsImage");
                    String goodsName = shareData.getSafeString("goodsName");
                    double goodsPrice = shareData.getDouble("goodsPrice");

                    Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(goodsImage)).centerCrop().into(postGoodsImage);
                    tvPostGoodsName.setText(goodsName);
                    if (StringUtil.safeModel(shareData) == Constant.GOODS_TYPE_CONSULT) {
                        UiUtil.toConsultUI(tvGoodsPrice);
                    } else {
                        tvGoodsPrice.setText(StringUtil.formatPrice(_mActivity, goodsPrice, 1, true));
                    }

                    // 如果是分享商品，則隱藏刪除按鈕
                    btnDeletePostGoods.setVisibility(View.GONE);

                    llPostGoodsContainer.setVisibility(View.VISIBLE);
                }


            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }

        loadPostCategory();

        initEmojiPage(view);
        loadEmojiData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_commit) {
            if (!commitButtonEnable) {
                return;
            }
            commitPost();
        } else if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_add_post_content_image) {
            // openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册

            int currImageCount = postContentImageContainer.getChildCount();
            if (btnAddPostContentImage.getVisibility() == View.VISIBLE) { // 如果添加圖片按鈕在顯示，還要減1才是真實圖片數
                currImageCount--;
            }

            ImageSelector.builder()
                    .useCamera(true) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setMaxSelectCount(MAX_IMAGE_COUNT - currImageCount) // 图片的最大选择数量，小于等于0时，不限数量。
                    .start(this, RequestCode.SELECT_MULTI_IMAGE.ordinal()); // 打开相册
        } else if (id == R.id.btn_select_post_category) {
            List<ListPopupItem> itemList = new ArrayList<>();
            for (int i = 0; i < postCategoryName.size(); i++) {
                itemList.add(new ListPopupItem(i, postCategoryName.get(i), null));
            }
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, "想要類型",
                            PopupType.SELECT_POST_CATEGORY, itemList, selectedCategoryIndex, this))
                    .show();
        } else if (id == R.id.btn_add_post_goods) {
            startForResult(AddPostGoodsFragment.newInstance(), RequestCode.SELECT_POST_GOODS.ordinal());
        } else if (id == R.id.btn_insert_post_emoji) {
            if (usageBtnEmoji == BTN_EMOJI_USAGE_EMOJI) {
                stplContainer.showToolPane();
                btnInsertPostEmoji.setImageResource(R.drawable.icon_keyboard);
            } else {
                stplContainer.showSoftInput(etContent);
                btnInsertPostEmoji.setImageResource(R.drawable.icon_add_post_insert_emoji);
            }

            usageBtnEmoji = 1 - usageBtnEmoji;
        } else if (id == R.id.btn_delete_post_goods) {
            goodsShown = false;
            llPostGoodsContainer.setVisibility(View.GONE);
            btnAddPostGoods.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        int status = stplContainer.getStatus();

        if (status == SoftToolPaneLayout.STATUS_TOOL_PANE_SHOWN) {
            llToolContainer.setVisibility(View.GONE);
            llBottomToolbar.setVisibility(View.GONE);
            stplContainer.hideToolPane();
            stplContainer.setStatus(SoftToolPaneLayout.STATUS_NONE_SHOWN);
            return true;
        }

        hideSoftInputPop();
        return true;
    }

    /**
     * 添加選中的照片
     */
    private void addSelectedImage(String absolutePath) {
        final View itemView = LayoutInflater.from(_mActivity).inflate(R.layout.post_content_image_widget, postContentImageContainer, false);
        ImageView postImage = itemView.findViewById(R.id.post_image);
        Glide.with(_mActivity).load(absolutePath).centerCrop().into(postImage);
        itemView.setTag(R.id.data_absolute_path, absolutePath);

        itemView.setOnLongClickListener(v -> {
            View.DragShadowBuilder builder = new View.DragShadowBuilder(v);
            ClipData clipData = ClipData.newPlainText("Stub", ""); // 防止拖放到EditText上，editText取剪貼板數據時崩潰
            v.startDrag(clipData, builder, v, View.DRAG_FLAG_OPAQUE);//第三个参数是传入一个关于这个view信息的任意对象（getLocalState），它即你需要在拖拽监听中的调用event.getLocalState()获取到这个对象来操作用的(比如传入一个RecyclerView中的position)。如果不需要这个对象，传null
            SLog.info("startDrag");
            v.setVisibility(View.INVISIBLE);
            currentDragView = v;

            Vibrator vibrator = (Vibrator) _mActivity.getSystemService(Service.VIBRATOR_SERVICE);   //获取系统震动服务
            if (vibrator != null) {
                vibrator.vibrate(70);   //震动70毫秒
            }

            return true;
        });

        itemView.setOnDragListener(((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENTERED:// 拖拽进入
                    View dragView = (View) event.getLocalState();
                    //之前讲到的v.startDrag(null, builder, null, 0);第三个参数如果传入一个int 类型的值，在这里便可以 通过(int) event.getLocalState()取出来。如果传的是null，上面这行代码就去掉好了
                    SLog.info("v[%s]", v.toString());
                    int childIndex = (int) v.getTag(R.id.data_index);
                    int dragViewIndex = (int) dragView.getTag(R.id.data_index);
                    SLog.info("DragEvent.ACTION_DRAG_ENTERED, child[%s], v[%s]", childIndex, dragViewIndex);
                    Util.exchangeChild(postContentImageContainer, childIndex, dragViewIndex);
                    updateIndexTagData();
                    break;
                case DragEvent.ACTION_DRAG_EXITED: // 拖拽退出
                    dragView = (View) event.getLocalState();
                    SLog.info("DragEvent.ACTION_DRAG_EXITED, child[%s], v[%s]", v.getTag(R.id.data_index), dragView.getTag(R.id.data_index));
                    break;
                case DragEvent.ACTION_DROP:
                    dragView = (View) event.getLocalState();
                    SLog.info("DragEvent.ACTION_DROP, child[%s], v[%s]", v.getTag(R.id.data_index), dragView.getTag(R.id.data_index));
                    currentDragView.setVisibility(View.VISIBLE);
                    updateIndexTagData();
                    break;
                        /*
                    case DragEvent.ACTION_DRAG_ENDED: //拖拽停止
                        //your operation
                        currentDragView.setVisibility(View.VISIBLE);
                        SLog.info("DragEvent.ACTION_DRAG_ENDED");
                        updateIndexTagData();
                        break;

                         */
                default:
                    break;
            }
            return true;
        }));

        ScaledButton btnRemoveImage = itemView.findViewById(R.id.btn_remove_image);
        btnRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postContentImageContainer.removeView(itemView);
                updateIndexTagData();
                btnAddPostContentImage.setVisibility(View.VISIBLE);
            }
        });

        int childCount = postContentImageContainer.getChildCount();
        if (childCount > 0) {
            postContentImageContainer.addView(itemView, childCount - 1);
            childCount++;
        }

        // 最多添加9張圖片
        if (childCount >= (MAX_IMAGE_COUNT + 1)) {
            btnAddPostContentImage.setVisibility(View.GONE);
        }

        updateIndexTagData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SLog.info("onActivityResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        // 選擇照片
        if (requestCode == RequestCode.OPEN_ALBUM.ordinal()) {
            Uri uri = data.getData();
            String absolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
            SLog.info("absolutePath[%s]", absolutePath);

            addSelectedImage(absolutePath);
        } else if (requestCode == RequestCode.SELECT_MULTI_IMAGE.ordinal()) {
            // 获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            if (images == null) {
                return;
            }

            SLog.info("images.size[%d]", images.size());
            for (String item : images) {
                File file = new File(item);

                SLog.info("item[%s], size[%d]", item, file.length());
                addSelectedImage(item);
            }

            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
            SLog.info("isCameraImage[%s]", isCameraImage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_SELECT_POST_GOODS) {
            goods = (Goods) message.data;
            goodsShown = false;
        }
    }



    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.SELECT_POST_CATEGORY) {
            updateSelectedCategoryIndex(id);
        }
    }

    private void updateSelectedCategoryIndex(int index) {
        selectedCategoryIndex = index;
        tvPostCategory.setText(postCategoryName.get(selectedCategoryIndex));
    }

    /**
     * 提交想要帖
     */
    private void commitPost() {
        final String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String title = etTitle.getText().toString().trim();
        if (StringUtil.isEmpty(title)) {
            ToastUtil.error(_mActivity, "請輸入標題");
            return;
        }

        String tmpContent = etContent.getText().toString().trim();
        if (StringUtil.isEmpty(tmpContent)) {
            ToastUtil.error(_mActivity, "請輸入正文");
            return;
        }
        tmpContent = StringUtil.filterCommentContent(tmpContent);

        final String content = tmpContent;

        if (joinState == 1) { // 參與聖誕活動，發表想要帖時，必須添加產品
            if (!goodsShown || goods == null) {
                ToastUtil.error(_mActivity, "參與聖誕活動，請添加想要的產品");
                return;
            }
        }


        commitButtonEnable = false;
        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                inProgressPopup.dismiss();

                if (message == null) {
                    return;
                }
                boolean success = (boolean) message;

                if (success) {
                    ToastUtil.success(_mActivity, successText);
                    hideSoftInputPop();
                } else {
                    commitButtonEnable = true;
                    ToastUtil.checkError(_mActivity, responseObj);
                }
            }
        };

        inProgressPopup.show();
        TwantApplication.Companion.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                try {
                    SLog.info("上傳圖片開始");
                    String url;
                    EasyJSONArray wantPostImages = EasyJSONArray.generate();

                    int childCount = postContentImageContainer.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View childView = postContentImageContainer.getChildAt(i);

                        if (childView instanceof RelativeLayout) {
                            String absolutePath = (String) childView.getTag(R.id.data_absolute_path);
                            SLog.info("absolutePath[%s]", absolutePath);
                            url = Api.syncUploadFile(new File(absolutePath));
                            SLog.info("url[%s]", url);
                            if (StringUtil.isEmpty(url)) {
                                return null;
                            }
                            wantPostImages.append(EasyJSONObject.generate("imageUrl", url));
                        }
                    }
                    SLog.info("上傳圖片完成, wantPostImages[%s]", wantPostImages);


                    // token要附在path中
                    String path = Api.PATH_COMMIT_POST + Api.makeQueryString(EasyJSONObject.generate(
                            "token", token,
                            "joinState", joinState));
                    SLog.info("path[%s]", path);

                    String postsCategory = postCategoryName.get(selectedCategoryIndex);

                    EasyJSONObject params = EasyJSONObject.generate(
                            "title", title,
                            "content", content,
                            "postCategory", postsCategory,
                            "wantPostImages", wantPostImages);

                    if (goodsShown && goods != null) {
                        EasyJSONArray wantPostGoodsVoList = EasyJSONArray.generate(EasyJSONObject.generate("commonId", goods.id));
                        params.set("wantPostGoodsVoList", wantPostGoodsVoList);
                    }

                    if (shareType == SharePopup.SHARE_TYPE_STORE) {
                        params.set("storeId", storeId);
                    } else if (shareType == SharePopup.SHARE_TYPE_GOODS) {
                        EasyJSONArray wantPostGoodsVoList = EasyJSONArray.generate(EasyJSONObject.generate("commonId", commonId));
                        params.set("wantPostGoodsVoList", wantPostGoodsVoList);
                    }

                    // 提交數據
                    String json = params.toString();
                    SLog.info("json[%s]", json);

                    String responseStr = Api.syncPostJson(path, json);
                    SLog.info("responseStr[%s]", responseStr);
                    responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        return false;
                    }

                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_ADD_POST, null);

                    return true;
                } catch (Exception e) {
                    SLog.info("Error!%s", e);
                }
                return false;
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        hideSoftInput();
        SLog.info("__onSupportVisible");
        // _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        selectGoods();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SLog.info("__onSupportInvisible");
        // _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.SELECT_POST_GOODS.ordinal() && resultCode == RESULT_OK && data != null) {
            Goods selectedGoods = new Goods();
            selectedGoods.id = data.getInt("commonId");
            selectedGoods.name = data.getString("goodsName");
            selectedGoods.price = data.getDouble("price");
            selectedGoods.imageUrl = data.getString("imageUrl");
            selectedGoods.goodsModal = data.getInt("goodsModal");

            this.goods = selectedGoods;
            goodsShown = false;
            selectGoods();
        }
    }

    private void selectGoods() {
        if (!goodsShown && goods != null) {
            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(goods.imageUrl)).centerCrop().into(postGoodsImage);
            tvPostGoodsName.setText(goods.name);
            if (goods.goodsModal == Constant.GOODS_TYPE_CONSULT) {
                UiUtil.toConsultUI(tvGoodsPrice);
            } else {
                tvGoodsPrice.setText(StringUtil.formatPrice(_mActivity,  goods.price, 1));
            }
            llPostGoodsContainer.setVisibility(View.VISIBLE);

            goodsShown = true;
            btnAddPostGoods.setVisibility(View.GONE);
        }
    }

    private void loadPostCategory() {
        Api.getUI(Api.PATH_POST_CATEGORY_LIST, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.isError(responseObj)) {
                    return;
                }

                try {
                    EasyJSONArray categoryList = responseObj.getSafeArray("datas.categoryList");
                    for (Object object : categoryList) {
                        EasyJSONObject category = (EasyJSONObject) object;
                        String categoryName = category.getSafeString("categoryName");
                        postCategoryName.add(categoryName);
                    }

                    updateSelectedCategoryIndex(selectedCategoryIndex);
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
        SLog.info("data[%s]", data);
        int id = (int) data;

        if (id == R.id.et_title) {
            SLog.info("點擊標題");
            llToolContainer.setVisibility(View.GONE);
        } else if (id == R.id.et_content) {
            SLog.info("點擊內容");
            // showBottomToolbar();
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

    /**
     * 更新里面各个拖放子View的位置数据
     */
    private void updateIndexTagData() {
        int childCount = postContentImageContainer.getChildCount();
        SLog.info("childCount__[%d]", childCount);
        for (int i = 0; i < childCount - 1; i++) {
            View child = postContentImageContainer.getChildAt(i);
            SLog.info("child[%s]", child.toString());
            child.setTag(R.id.data_index, i);
        }
    }
}
