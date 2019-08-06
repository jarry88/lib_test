package com.ftofs.twant.fragment;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.ChatMessageAdapter;
import com.ftofs.twant.adapter.EmojiPageAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.ChatMessage;
import com.ftofs.twant.entity.CommonUsedSpeech;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.EmojiPage;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.interfaces.ViewSizeChangedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Emoji;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.orm.ImNameMap;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.task.UpdateFriendInfoTask;
import com.ftofs.twant.util.CameraUtil;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.IntentUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.CommonUsedSpeechPopup;
import com.ftofs.twant.widget.ImStoreGoodsPopup;
import com.ftofs.twant.widget.ImStoreOrderPopup;
import com.ftofs.twant.widget.QMUIAlignMiddleImageSpan;
import com.ftofs.twant.widget.SizeChangedRecyclerView;
import com.ftofs.twant.widget.SmoothInputLayout;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 聊天會話Fragment
 * @author zwm
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener,
        View.OnTouchListener, TextWatcher, SmoothInputLayout.OnVisibilityChangeListener, ViewSizeChangedListener,
        OnSelectedListener {
    /**
     * 傳進來的附加參數
     */
    public static class Extra {
        public Extra(int storeId) {
            this.storeId = storeId;
        }

        public int storeId;
    }


    /**
     * 定義發送按鈕的動作，是顯示工具菜單還是發送消息
     */
    private static final int ACTION_SHOW_MENU = 1;
    private static final int ACTION_SEND_MESSAGE = 2;

    int storeId;

    int action = ACTION_SHOW_MENU;

    EMConversation conversation;

    SmoothInputLayout silMainContainer;
    EditText etMessage;
    View btnEmoji;
    ImageView iconEmoji;
    View btnTool;
    ImageView iconCircleAdd;
    View llEmojiPane;
    View llToolPane;

    FriendInfo friendInfo;
    RecyclerView rvEmojiPageList;
    EmojiPageAdapter emojiPageAdapter;
    List<EmojiPage> emojiPageList = new ArrayList<>();

    SizeChangedRecyclerView rvMessageList;
    ChatMessageAdapter chatMessageAdapter;
    List<ChatMessage> chatMessageList = new ArrayList<>();

    String myMemberName;

    String yourNickname;
    String yourMemberName;  // 如果是客服聊天，則為imName; 如果是普通聊天，則為memberName
    String yourAvatarUrl;
    int yourRole;

    TextView tvNickname;

    /**
     * 拍照的圖片文件
     */
    File captureImageFile;

    public static ChatFragment newInstance(EMConversation conversation, Extra extra) {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        fragment.setConversation(conversation);
        if (extra != null) {
            fragment.setExtraData(extra);
        }

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        String ext = conversation.getExtField();
        EasyJSONObject extObj = (EasyJSONObject) EasyJSONObject.parse(ext);

        try {
            yourMemberName = conversation.conversationId();
            yourNickname = extObj.getString("nickName");
            yourAvatarUrl = extObj.getString("avatarUrl");
            yourRole = extObj.getInt("role");
            SLog.info("yourMemberName[%s], yourNickname[%s], yourAvatarUrl[%s], yourRole[%d]",
                    yourMemberName, yourNickname, yourAvatarUrl, yourRole);
        } catch (Exception e) {

        }

        loadFriendInfo();

        tvNickname = view.findViewById(R.id.tv_nickname);
        tvNickname.setOnClickListener(this);

        silMainContainer = view.findViewById(R.id.sil_main_container);
        etMessage = view.findViewById(R.id.et_message);
        btnEmoji = view.findViewById(R.id.btn_emoji);
        iconEmoji = view.findViewById(R.id.icon_emoji);
        btnTool = view.findViewById(R.id.btn_tool);
        iconCircleAdd = view.findViewById(R.id.icon_circle_add);
        llEmojiPane = view.findViewById(R.id.ll_emoji_pane);
        llToolPane = view.findViewById(R.id.ll_tool_pane);
        silMainContainer.setOnVisibilityChangeListener(this);
        etMessage.addTextChangedListener(this);
        btnEmoji.setOnClickListener(this);
        btnTool.setOnClickListener(this);
        etMessage.setOnTouchListener(this);
        view.findViewById(R.id.rv_message_list).setOnTouchListener(this);

        myMemberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, "");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_send_image, this);
        Util.setOnClickListener(view, R.id.btn_capture_image, this);
        Util.setOnClickListener(view, R.id.btn_send_goods, this);
        Util.setOnClickListener(view, R.id.btn_send_order, this);
        Util.setOnClickListener(view, R.id.btn_send_common_used_speech, this);
        Util.setOnClickListener(view, R.id.btn_send_location, this);

        initEmojiPage(view);
        loadEmojiData();

        initChatUI(view);
        loadChatData();

        messageListScrollToBottom();

        bindFriendInfo();
    }

    private void bindFriendInfo() {
        if (friendInfo != null) {
            tvNickname.setText(friendInfo.nickname);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

        //指定会话消息未读数清零
        conversation.markAllMessagesAsRead();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_NEW_CHAT_MESSAGE) {
            SLog.info("收到新消息");
            ChatMessage chatMessage = (ChatMessage) message.data;
            if (yourMemberName.equals(chatMessage.fromMemberName)) {
                SLog.info("是對方發來的消息");
                chatMessageList.add(chatMessage);
                // chatMessageAdapter.notifyItemInserted(chatMessageList.size() - 1);
                setShowTimestamp(chatMessageList);
                chatMessageAdapter.notifyDataSetChanged();
                messageListScrollToBottom();
            } else {
                SLog.info("是另一個人發來的消息");
            }

            messageListScrollToBottom();
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_UPDATE_FRIEND_INFO) {
            if (loadFriendInfo()) {
                SLog.info("更新到最新的好友資料");
                tvNickname.setText(friendInfo.nickname);
                setShowTimestamp(chatMessageList);
                chatMessageAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 消息列表滾動到底部
     */
    private void messageListScrollToBottom() {
        rvMessageList.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
    }

    private boolean loadFriendInfo() {
        String memberName = yourMemberName;

        if (yourRole == ChatUtil.ROLE_CS_AVAILABLE) {
            ImNameMap map = ImNameMap.getByImName(yourMemberName);
            if (map == null) {
                return false;
            }

            memberName = map.memberName;
        }


        friendInfo = LitePal.where("memberName = ?", memberName).findFirst(FriendInfo.class);
        if (friendInfo == null) {
            SLog.info("好友信息[%s]為空，更新好友信息", memberName);
            TwantApplication.getThreadPool().execute(new UpdateFriendInfoTask(memberName));

            return false;
        } else {
            if (friendInfo.avatarImg == null) {
                SLog.info("friendInfo.avatarImg is null");
            } else {
                SLog.info("friendInfo.avatarImg size[%d]", friendInfo.avatarImg.length);
            }

            return true;
        }
    }

    private void loadChatData() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(yourMemberName);
        if (conversation == null) {
            return;
        }

        //获取此会话的所有消息
        String startMsgId = "";
        List<EMMessage> messages = conversation.getAllMessages();
        SLog.info("消息條數[%d]", messages.size());

        EMMessage lastMessage = null;

        for (EMMessage emMessage : messages) {
            startMsgId = emMessage.getMsgId();
            SLog.info("message[%s]", emMessage.toString());
            lastMessage = emMessage;
        }

        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        int pagesize = 20;
        messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        for (EMMessage emMessage : messages) {
            // SLog.info("message[%s]", emMessage.toString());
            chatMessageList.add(emMessageToChatMessage(emMessage));
        }

        if (lastMessage != null) {
            chatMessageList.add(emMessageToChatMessage(lastMessage));
        }

        setShowTimestamp(chatMessageList);
        chatMessageAdapter.setNewData(chatMessageList);
    }

    private ChatMessage emMessageToChatMessage(EMMessage emMessage) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.messageType = ChatUtil.getIntMessageType(emMessage);
        chatMessage.messageId = emMessage.getMsgId();
        if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_TXT) {
            chatMessage.content = emMessage.getBody().toString();
        } else if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_IMAGE) {
            String imgUrl = emMessage.getStringAttribute("ossUrl", "");
            String absolutePath = emMessage.getStringAttribute("absolutePath", "");

            chatMessage.content = EasyJSONObject.generate("imgUrl", imgUrl, "absolutePath", absolutePath).toString();
            SLog.info("chatMessage.content[%s]", chatMessage.content);
        }

        if (emMessage.getFrom().equals(myMemberName)) {
            chatMessage.origin = ChatMessage.MY_MESSAGE;
        } else {
            chatMessage.origin = ChatMessage.YOUR_MESSAGE;
        }
        chatMessage.timestamp = emMessage.getMsgTime();

        return chatMessage;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_send_image:
                startActivityForResult(IntentUtil.makeOpenSystemAlbumIntent(), RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
                break;
            case R.id.btn_capture_image:
                captureImageFile = CameraUtil.openCamera(_mActivity, this, Constant.CAMERA_ACTION_IMAGE);
                break;
            case R.id.btn_send_goods:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ImStoreGoodsPopup(_mActivity, storeId, yourMemberName,this))
                        .show();
                break;
            case R.id.btn_send_order:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ImStoreOrderPopup(_mActivity, storeId, yourMemberName,this))
                        .show();
                break;
            case R.id.btn_send_common_used_speech:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new CommonUsedSpeechPopup(_mActivity,this))
                        .show();
                break;
            case R.id.btn_send_location:
                ToastUtil.info(_mActivity, "暫未實現");
                break;
            case R.id.btn_emoji:
                btnTool.setSelected(false);
                if (btnEmoji.isSelected()) {
                    btnEmoji.setSelected(false);
                    showInput();
                } else {
                    btnEmoji.setSelected(true);
                    showEmoji();
                }
                break;
            case R.id.btn_tool:
                btnEmoji.setSelected(false);
                if (action == ACTION_SEND_MESSAGE) {
                    String message = etMessage.getText().toString();
                    SLog.info("message[%s]", message);
                    etMessage.setText("");
                    btnTool.setSelected(false);

                    sendTextMessage(message);
                    return;
                }

                // 如果原先打開的是表情面板，則點擊工具按鈕，顯示工具面板
                if (silMainContainer.isInputPaneOpen() && llEmojiPane.getVisibility() == View.VISIBLE) {
                    showTool();
                    return;
                }

                if (silMainContainer.isInputPaneOpen()) {
                    showInput();
                } else {
                    showTool();
                }
                break;
            case R.id.tv_nickname:
                if (Config.DEVELOPER_MODE) {
                    SLog.info("heeee");
                    rvMessageList.smoothScrollBy(0, 825);
                }
                break;
            default:
                break;
        }
    }

    private void initChatUI(View contentView) {
        rvMessageList = contentView.findViewById(R.id.rv_message_list);
        rvMessageList.setViewSizeChangedListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvMessageList.setLayoutManager(layoutManager);

        byte[] yourAvatarData = null;
        if (friendInfo != null) {
            yourAvatarData = friendInfo.avatarImg;
        }
        chatMessageAdapter = new ChatMessageAdapter(R.layout.chat_message_item, chatMessageList, yourAvatarData);
        chatMessageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("onItemChildClick");
                int id = view.getId();
                if (id == R.id.img_message) {
                    SLog.info("預覽大圖");

                    ChatMessage chatMessage = chatMessageList.get(position);
                    if (chatMessage.messageType != Constant.CHAT_MESSAGE_TYPE_IMAGE) {
                        return;
                    }

                    SLog.info("chatMessage.content[%s]", chatMessage.content);

                    if (StringUtil.isEmpty(chatMessage.content)) {
                        return;
                    }

                    EasyJSONObject easyJSONObject = (EasyJSONObject) EasyJSONObject.parse(chatMessage.content);
                    if (easyJSONObject == null) {
                        return;
                    }

                    String absolutePath = null;
                    String imgUrl = null;
                    try {
                        absolutePath = easyJSONObject.getString("absolutePath");
                        imgUrl = easyJSONObject.getString("imgUrl");
                    } catch (Exception e) {

                    }

                    String imageUri;

                    // 優先加載本地的Copy
                    File file = new File(absolutePath);
                    if (file.isFile()) {
                        imageUri = absolutePath;
                    } else { // 否則，加載oss上的Copy
                        imageUri = StringUtil.normalizeImageUrl(imgUrl);
                    }

                    Util.startFragment(ImageViewerFragment.newInstance(imageUri));

                } else if (id == R.id.img_your_avatar) {
                    Util.startFragment(MemberInfoFragment.newInstance(yourMemberName));
                } else if (id == R.id.img_my_avatar) {
                    start(PersonalInfoFragment.newInstance());
                }
            }
        });
        chatMessageAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("onItemChildLongClick");
                ChatMessage chatMessage = chatMessageList.get(position);
                String messageId = chatMessage.messageId;
                new XPopup.Builder(getContext())
//                        .maxWidth(600)
                        .asCenterList("請選擇操作", new String[]{"刪除"},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        SLog.info("position[%d], text[%s]", position, text);
                                        if (position == 0) {
                                            showDeleteMessageConfirm(position, messageId);
                                        }
                                    }
                                })
                        .show();
                return false;
            }
        });

        rvMessageList.setAdapter(chatMessageAdapter);
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

                            Editable message = etMessage.getEditableText();
                            SLog.info("message[%s]", message);

                            // Get the selected text.
                            int start = etMessage.getSelectionStart();
                            int end = etMessage.getSelectionEnd();

                            Bitmap bitmap = BitmapFactory.decodeFile(emoji.absolutePath);
                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                            drawable.setBounds(0, 0,
                                    ((int) etMessage.getTextSize() + 12), ((int) etMessage.getTextSize() + 12));
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
                            etMessage.setText(message);
                            // 重新定位光標
                            etMessage.setSelection(start + emoticon.length());
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

    private void sendTextMessage(String content) {
        SLog.info("content[%s]", content);
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, yourMemberName);
        message.setAttribute("messageType", "txt");
        ChatUtil.setMessageCommonAttr(message, ChatUtil.ROLE_MEMBER);
        SLog.info("message[%s], yourMemberName[%s]", message, yourMemberName);

        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack(){
            @Override
            public void onSuccess() {
                SLog.info("onSuccess, body[%s]", message.getBody());

                Api.imSendMessage(yourMemberName, "txt", message.getMsgId(), message.getBody().toString(),
                        null, null, 0, null, null, 0, null);
            }

            @Override
            public void onError(int i, String s) {
                SLog.info("onError, i[%d], s[%s]", i, s);
            }

            @Override
            public void onProgress(int i, String s) {
                SLog.info("onProgress, i[%d], s[%s]", i, s);
            }
        });

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.messageType = ChatUtil.getIntMessageType(message);
        chatMessage.messageId = message.getMsgId();
        chatMessage.content = "txt:" + "\"" + content + "\"";
        chatMessage.origin = ChatMessage.MY_MESSAGE;
        chatMessage.timestamp = message.getMsgTime();
        chatMessageList.add(chatMessage);

        // chatMessageAdapter.notifyItemInserted(chatMessageList.size() - 1);
        setShowTimestamp(chatMessageList);
        chatMessageAdapter.notifyDataSetChanged();
        messageListScrollToBottom();
    }

    /**
     * 显示输入面板
     */
    private void showInput() {
        silMainContainer.showKeyboard();
        iconEmoji.setImageResource(R.drawable.icon_emoji);
        afterTextChanged(etMessage.getText());
    }

    /**
     *  显示Emoji面板
     */
    private void showEmoji() {
        llEmojiPane.setVisibility(View.VISIBLE);
        llToolPane.setVisibility(View.GONE);
        iconEmoji.setImageResource(R.drawable.icon_keyboard);
        silMainContainer.showInputPane(true);
    }

    /**
     * 显示Tool面板
     */
    private void showTool() {
        llEmojiPane.setVisibility(View.GONE);
        llToolPane.setVisibility(View.VISIBLE);
        iconEmoji.setImageResource(R.drawable.icon_emoji);
        silMainContainer.showInputPane(false);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().trim().length() > 0) {
            // 显示发送按钮
            iconCircleAdd.setImageResource(R.drawable.ic_iconfont_send);
            action = ACTION_SEND_MESSAGE;
        } else {
            // 隐藏发送按钮
            iconCircleAdd.setImageResource(R.drawable.icon_circle_add);
            action = ACTION_SHOW_MENU;
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.rv_message_list:
                btnEmoji.setSelected(false);
                btnTool.setSelected(false);
                silMainContainer.closeKeyboard(true);
                silMainContainer.closeInputPane();
                break;
            case R.id.et_message:
                btnEmoji.setSelected(false);
                btnTool.setSelected(false);
                iconEmoji.setImageResource(R.drawable.icon_emoji);
                afterTextChanged(etMessage.getText());
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (silMainContainer.isInputPaneOpen()) {
            silMainContainer.closeInputPane();
            iconEmoji.setImageResource(R.drawable.icon_emoji);
            return true;
        }
        pop();
        return true;
    }

    @Override
    public void onViewSizeChanged(View view, int w, int h, int oldw, int oldh) {
        int id = view.getId();
        /*
        微信鍵盤伸縮對消息列表的影響
        1.如果展開軟鍵盤，消息列表滾動到最底
        2.如果收縮鍵盤，消息列表中消息定位保持不變

        目前的做法與【釘釘】的一致
        1.如果展開軟鍵盤，消息列表滾動到最底
        2.在展開軟鍵盤的情況下，點擊消息列表中的任一處地方，收縮軟鍵盤
         */
        if (id == R.id.rv_message_list) {
            if (oldh > 0) {
                int diff = h - oldh;
                SLog.info("diff[%d]", diff);
                if (diff < 0) {  // 展開鍵盤
                    messageListScrollToBottom();
                }
            }
        }
    }

    private void showDeleteMessageConfirm(int position, String messageId) {
        new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onShow() {
                    }
                    @Override
                    public void onDismiss() {
                    }
                }).asCustom(new TwConfirmPopup(_mActivity, "確認", "確定要刪除這條消息嗎?", new OnConfirmCallback() {
            @Override
            public void onYes() {
                SLog.info("onYes");
                //删除当前会话的某条聊天记录
                SLog.info("messageId[%s]", messageId);
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(yourMemberName);
                conversation.removeMessage(messageId);

                chatMessageList.remove(position);
                chatMessageAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onNo() {
                SLog.info("onNo");
            }
        }))
                .show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SLog.info("onActivityResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        // 上傳圖片到OSS
        if (requestCode == RequestCode.OPEN_ALBUM.ordinal() || requestCode == RequestCode.CAMERA_IMAGE.ordinal()) {
            String absolutePath;
            if (requestCode == RequestCode.OPEN_ALBUM.ordinal()) {
                Uri uri = data.getData();
                absolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
            } else {
                absolutePath = captureImageFile.getAbsolutePath();  // 拍照得到的文件路徑
            }
            SLog.info("absolutePath[%s]", absolutePath);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.messageType = Constant.CHAT_MESSAGE_TYPE_IMAGE;
            chatMessage.origin = ChatMessage.MY_MESSAGE;
            chatMessage.content = EasyJSONObject.generate("absolutePath", absolutePath).toString();
            chatMessage.timestamp = System.currentTimeMillis();
            chatMessageList.add(chatMessage);

            setShowTimestamp(chatMessageList);
            chatMessageAdapter.notifyDataSetChanged();
            messageListScrollToBottom();

            TaskObserver taskObserver = new TaskObserver() {
                @Override
                public void onMessage() {
                    Pair<String, String> result = (Pair<String, String>) message;
                    if (result == null) {
                        return;
                    }

                    //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
                    EMMessage message = EMMessage.createImageSendMessage(absolutePath, false, yourMemberName);
                    chatMessage.messageId = message.getMsgId();
                    message.setAttribute("messageType", "image");
                    ChatUtil.setMessageCommonAttr(message, ChatUtil.ROLE_MEMBER);

                    message.setAttribute("ossUrl", result.first);
                    message.setAttribute("absolutePath", result.second);

                    SLog.info("ossUrl[%s], absolutePath[%s]", result.first, result.second);

                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(message);
                    message.setMessageStatusCallback(new EMCallBack(){
                        @Override
                        public void onSuccess() {
                            SLog.info("onSuccess, body[%s]", message.getBody().toString());

                            EMImageMessageBody imageMessageBody = (EMImageMessageBody) message.getBody();
                            Api.imSendMessage(yourMemberName, "image", message.getMsgId(), message.getBody().toString(),
                                    result.first, imageMessageBody.getRemoteUrl(), 0, null, null, 0, null);
                        }

                        @Override
                        public void onError(int i, String s) {
                            SLog.info("onError, i[%d], s[%s]", i, s);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            // SLog.info("onProgress, i[%d], s[%s]", i, s);
                        }
                    });
                }
            };

            TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
                @Override
                public Object doWork() {
                    File file = new File(absolutePath);
                    String name = Api.syncUploadFile(file);
                    return new Pair<>(name, absolutePath);
                }
            });
        }
    }

    public void setConversation(EMConversation conversation) {
        this.conversation = conversation;
    }

    public void setExtraData(Extra extra) {
        storeId = extra.storeId;
    }

    private void setShowTimestamp(List<ChatMessage> chatMessages) {
        if (chatMessages == null) {
            return;
        }

        long lastShowTimestamp = 0;
        for (int i = 0; i < chatMessages.size(); i++) {
            ChatMessage chatMessage = chatMessages.get(i);
            if (i == 0) {
                chatMessage.showTimestamp = true;
                lastShowTimestamp = chatMessage.timestamp;
            } else {
                if (chatMessage.timestamp - lastShowTimestamp >= 5 * 60 * 1000) { // 超過5分鐘就顯示消息時間
                    chatMessage.showTimestamp = true;
                    lastShowTimestamp = chatMessage.timestamp;
                }
            }
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.IM_CHAT_COMMON_USED_SPEECH) {
            CommonUsedSpeech speech = (CommonUsedSpeech) extra;

            if (speech.dataType == CommonUsedSpeech.DATA_TYPE_SPEECH) { // 常用語
                sendTextMessage(speech.content);
            } else { // 常用版式
                sendTextMessage(speech.content);
            }
        }
    }
}
