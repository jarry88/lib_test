package com.ftofs.twant.fragment;

import android.app.Instrumentation;
import android.app.MediaRouteButton;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.ChatMessageAdapter;
import com.ftofs.twant.adapter.EmojiPageAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.UnicodeEmoji;
import com.ftofs.twant.domain.member.Member;
import com.ftofs.twant.entity.ChatConversation;
import com.ftofs.twant.entity.ChatMessage;
import com.ftofs.twant.entity.CommonUsedSpeech;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.EmojiPage;
import com.ftofs.twant.entity.ImStoreOrderItem;
import com.ftofs.twant.entity.UnicodeEmojiItem;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.interfaces.ViewSizeChangedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Conversation;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.CameraUtil;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.member.MemberVo;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.ImStoreGoodsPopup;
import com.ftofs.twant.widget.ImStoreOrderPopup;
import com.ftofs.twant.widget.ImagePreviewPopup;
import com.ftofs.twant.widget.SizeChangedRecyclerView;
import com.ftofs.twant.widget.SmoothInputLayout;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.orhanobut.hawk.Hawk;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 聊天會話Fragment
 * @author zwm
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener,
        View.OnTouchListener, TextWatcher, SmoothInputLayout.OnVisibilityChangeListener, ViewSizeChangedListener,
        OnSelectedListener, SimpleCallback {
    /**
     * 自定義文本消息的類型
     */
    public static final String CUSTOM_MESSAGE_TYPE_TXT = "txt";  // 普通文本消息
    public static final String CUSTOM_MESSAGE_TYPE_GOODS = "goods"; // 產品文本消息
    public static final String CUSTOM_MESSAGE_TYPE_ORDERS = "orders"; // 訂單文本消息
    public static final String CUSTOM_MESSAGE_TYPE_ENC = "memberCard";  //  電子名片
    public static final String CUSTOM_MESSAGE_TYPE_IMG = "img";//img 圖片


    /**
     * 定義發送按鈕的動作，是顯示工具菜單還是發送消息
     */
    private static final int ACTION_SHOW_MENU = 1;
    private static final int ACTION_SEND_MESSAGE = 2;

    int storeId=0;

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
    UiHandler uiHandler ;

    /**
     * 拍照的圖片文件
     */
    File captureImageFile;
    public int isFriend;
    private boolean hasCard;
    private String yourTrueName;
    private boolean yourTrueNameLoaded;
    private String myTrueName;
    //這裏的role指商城角色
    private int myRole;
    private LinearLayout btnSendGoods;
    private LinearLayout btnSendOrder;
    private int pagesize=20;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static ChatFragment newInstance(EMConversation conversation, FriendInfo friendInfo) {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        fragment.setConversation(conversation);
        fragment.setFriendInfo(friendInfo);

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

        // 檢查imToken是否已經過期
        long imTokenExpire = Hawk.get(SPField.FIELD_IM_TOKEN_EXPIRE, 0L);
        long now = System.currentTimeMillis();
        SLog.info("imTokenExpire[%s], now[%s]", imTokenExpire, now);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //模拟耗时操作
            new Handler().postDelayed(() -> {
                loadHistoryMessage(pagesize);
                SLog.info("执行下拉刷新");
//                chatMessageAdapter.notifyDataSetChanged();

            },100);

        });

        if (now >= imTokenExpire) {  // 如果已經過期，則獲取最新的imToken並更新
            String token = User.getToken();
            EasyJSONObject params = EasyJSONObject.generate("token", token);
            Api.postUI(Api.PATH_GET_IM_TOKEN, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr_________[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    try {
                        String imToken = responseObj.getSafeString("datas.imToken");
                        long expiresTime = responseObj.getLong("datas.expiresTime");

                        SLog.info("imToken[%s], expiresTime[%s]", imToken, expiresTime);
                        Hawk.put(SPField.FIELD_IM_TOKEN, imToken);
                        Hawk.put(SPField.FIELD_IM_TOKEN_EXPIRE, expiresTime);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }

                }
            });
        }

        // 指定会话消息未读数清零(進入會話處)
        conversation.markAllMessagesAsRead();
        Conversation.clearUnreadMsgCount(conversation.conversationId());


        String ext = conversation.getExtField();
        EasyJSONObject extObj = EasyJSONObject.parse(ext);

        try {
            yourMemberName = conversation.conversationId();
            yourNickname = friendInfo.nickname;
            yourAvatarUrl = friendInfo.avatarUrl;
            yourRole = friendInfo.role;
            SLog.info("yourMemberName[%s], yourNickname[%s], yourAvatarUrl[%s], yourRole[%d]",
                    yourMemberName, yourNickname, yourAvatarUrl, yourRole);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        updateYourInfo();
        loadMemberInfo();
        updateMyInfo();
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
//        myRole = TwantApplication.getInstance().getMemberVo().role;

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_menu, this);
        Util.setOnClickListener(view, R.id.btn_send_image, this);
        Util.setOnClickListener(view, R.id.btn_capture_image, this);
        Util.setOnClickListener(view, R.id.btn_send_enc, this);
        btnSendGoods = view.findViewById(R.id.btn_send_goods);
        btnSendGoods.setOnClickListener(this);
        btnSendOrder = view.findViewById(R.id.btn_send_order);
        btnSendOrder.setOnClickListener(this);
        // 常用語 暫時屏蔽 Util.setOnClickListener(view, R.id.btn_send_common_used_speech, this);
        // 定位 暫時屏蔽 Util.setOnClickListener(view, R.id.btn_send_location, this);

        // 檢測對方是否是客服，有s的表示是客服
//這裏是就判別方法        if (yourMemberName.indexOf('s') == -1) {
//            // 如果不是客服，則隱藏發送【產品】、【訂單】按鈕
//            btnSendGoods.setVisibility(View.GONE);
//            btnSendOrder.setVisibility(View.GONE);
//        }

        uiHandler= new UiHandler(this);
        initEmojiPage(view);
        loadEmojiData();

        initChatUI(view);
        loadChatData();
        initGoodInfo();

        messageListScrollToBottom();

        bindFriendInfo();
        showGoodsAndOrder();
    }

    private void updateMyInfo() {
        ApiUtil.getImInfo(_mActivity, User.getUserInfo(SPField.FIELD_MEMBER_NAME, null), new SimpleCallback() {
            @Override
            public void onSimpleCall(Object data) {
                MemberVo memberVo = (MemberVo) data;
                if (memberVo != null) {
                    myRole = memberVo.role;
                    showGoodsAndOrder();
                }
            }
        });

    }

    private void loadHistoryMessage(int pagesize) {
        try {
            EMClient.getInstance().chatManager().fetchHistoryMessages(
                    yourMemberName,EMConversation.EMConversationType.Chat, pagesize, "");
            final List<EMMessage> msgs = conversation.getAllMessages();
            int msgCount = msgs != null ? msgs.size() : 0;
            SLog.info("COUNT%s",conversation.getAllMsgCount());
            String messageId = null;
            if (chatMessageList != null && chatMessageList.size() > 0) {
                messageId = chatMessageList.get(0).messageId;
            }
            int dbCount = (loadFromDB(messageId, pagesize));
            if (dbCount == pagesize) {
                SLog.info("本地获取成功,获取条数");
                uiHandler.sendMessage(new Message());
                return;
            }
//            EMClient.getInstance().chatManager().f
            EMClient.getInstance().chatManager().asyncFetchHistoryMessage(conversation.conversationId(), conversation.getType(), pagesize-dbCount,messageId, new EMValueCallBack<EMCursorResult<EMMessage>>() {
                @Override
                public void onSuccess(EMCursorResult<EMMessage> emMessageEMCursorResult) {

                    SLog.info("异步获取成功,获取条数 %d",conversation.getAllMessages().size());
                    List<EMMessage> messages = emMessageEMCursorResult.getData();
                    if (messages != null&&messages.size()>0) {
                        int i=0;
                        Conversation dbItem=Conversation.getByMemberName(yourMemberName);
                        if (dbItem == null) {
                            dbItem = new Conversation();
                            dbItem.memberName = yourMemberName;
                            dbItem.lastMessage = messages.get(messages.size()-1);

                            SLog.info("Lastmessage[%s]", dbItem.lastMessage.toString());
                            dbItem.explainLastMessage();
                        }
                        for (EMMessage emMessage : messages) {
                            if (ChatUtil.getIntMessageType(emMessage)==0) {
                                continue;
                            }
                            chatMessageList.add(i,emMessageToChatMessage(emMessage));
                            dbItem.allEMMessage.add(i++, emMessage);
                        }
                        dbItem.save();
                    } else {
                        SLog.info("没有更多记录了");
                    }
                    uiHandler.sendMessage(new Message());
                }

                @Override
                public void onError(int i, String s) {
                    swipeRefreshLayout.setRefreshing(false);//取消刷新
                    SLog.info("获取失败");
                }
            });
//            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
//                String msgId = null;
//                if (msgs != null && msgs.size() > 0) {
//                    msgId = msgs.get(0).getMsgId();
//                }
////                conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
//
//            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    private void showGoodsAndOrder() {
        if (yourRole == 0 && myRole == 0) {
            btnSendGoods.setVisibility(View.GONE);
            btnSendOrder.setVisibility(View.GONE);
        } else {
            btnSendGoods.setVisibility(View.VISIBLE);
            btnSendOrder.setVisibility(View.VISIBLE);
        }
    }

    private void updateYourInfo() {

//        if (!TwantApplication.getInstance().getMemberVo().getFromInterface) {
//            TwantApplication.getInstance().updateCurrMemberInfo();
//        }
        ApiUtil.getImInfo(_mActivity,yourMemberName,memberVo ->{
            MemberVo yourInfo = (MemberVo)memberVo;
            //指对话框标题
            yourNickname = yourInfo.getNickName();
            yourAvatarUrl = yourInfo.getAvatarUrl();
            yourRole = yourInfo.role;
            storeId = ((MemberVo) memberVo).getStoreId();
            if (yourInfo.role != ChatUtil.ROLE_MEMBER) {
                yourNickname = yourInfo.storeName+" "+yourInfo.staffName;
                yourAvatarUrl = yourInfo.storeAvatar;
                Conversation conversation=Conversation.getByMemberName(yourMemberName);
                conversation.nickname = yourNickname;

                conversation.save();
            }
            showGoodsAndOrder();
            tvNickname.setText(yourNickname);
            chatMessageAdapter.setYourAvatarUrl(yourAvatarUrl);
            chatMessageAdapter.notifyDataSetChanged();
        });
    }

    private void initGoodInfo() {
        if (friendInfo.goodsInfo != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.messageType = Constant.CHAT_MESSAGE_TYPE_GOODS;
            SLog.info("chatMessage.messageType[%d]", chatMessage.messageType);

            chatMessage.origin = ChatMessage.MY_MESSAGE;


            chatMessage.timestamp = Calendar.getInstance().getTimeInMillis();

            String goodsImage = friendInfo.goodsInfo.imageSrc;
            int commonId = friendInfo.goodsInfo.commonId;
            String goodsName = friendInfo.goodsInfo.goodsName;

            Boolean showSendBtn=friendInfo.goodsInfo.showSendBtn;
            chatMessage.content = EasyJSONObject.generate("goodsImage", goodsImage, "commonId", commonId, "goodsName", goodsName,"showSendBtn",showSendBtn).toString();

            SLog.info("chatMessage.content[%s]", chatMessage.content);

            chatMessageList.add(chatMessage);
        }
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

        // 指定会话消息未读数清零(離開會話處)
        conversation.markAllMessagesAsRead();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_NEW_CHAT_MESSAGE) {
            SLog.info("收到新消息,message[%s]",message.data.toString());
            ChatMessage chatMessage = (ChatMessage) message.data;
            if (yourMemberName.equals(chatMessage.fromMemberName)) {
                if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_ENC) {
                    chatMessage.trueName = yourTrueName;
                    hasCard = true;
                }
                SLog.info("是對方發來的消息");
                chatMessageList.add(chatMessage);
                // chatMessageAdapter.notifyItemInserted(chatMessageList.size() - 1);
                setShowTimestamp(chatMessageList);
                chatMessageAdapter.setNewData(chatMessageList);
                messageListScrollToBottom();
            } else {
                SLog.info("是另一個人發來的消息");
            }

            messageListScrollToBottom();
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_CHANGE_MEMBER_AVATAR) {
            chatMessageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 消息列表滾動到底部
     */
    private void messageListScrollToBottom() {
        rvMessageList.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
    }

    /**
     * 查看聊天對象信息
     * 目前主要用於主動獲取對方真實姓名
     */
    private void loadMemberInfo() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        String pureYourMemberName; // 去掉s之後的memberName
        pureYourMemberName = StringUtil.getPureMemberName(yourMemberName);

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "friendMemberName", pureYourMemberName
        );

        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_FRIEND_INFO, params, new UICallback() {
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
                    EasyJSONObject memberInfo = responseObj.getSafeObject("datas.memberInfo");
                    yourTrueName = memberInfo.getSafeString("trueName");
                    updateList();
                    yourTrueNameLoaded = true;
                    SLog.info("trueName%s",yourTrueName);
//
//                    isFriend = memberCardVo.getInt("isFriend");

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updateList() {
        if (yourTrueNameLoaded) {
            return;
        }
        for (ChatMessage chatMessage : chatMessageList) {
            if(chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_ENC){
                if (chatMessage.origin == ChatMessage.YOUR_MESSAGE) {
                    chatMessage.trueName = yourTrueName;
                }
            }
        }
        chatMessageAdapter.notifyDataSetChanged();
    }

    private void myTrueName() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "friendMemberName", User.getUserInfo(SPField.FIELD_MEMBER_NAME,"")
        );

        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_FRIEND_INFO, params, new UICallback() {
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
                    EasyJSONObject memberInfo = responseObj.getSafeObject("datas.memberInfo");
                    myTrueName = memberInfo.getSafeString("trueName");
                    SLog.info("trueName%s",myMemberName);
//
//                    isFriend = memberCardVo.getInt("isFriend");

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
    private void loadChatData() {
        SLog.info("trueName%s",yourTrueName);

        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(yourMemberName);
        if (conversation == null) {
            return;
        }

        //获取此会话的所有消息
        String startMsgId = "";
        List<EMMessage> messages = conversation.getAllMessages();
        SLog.info("消息條數[%d]", messages.size());

        EMMessage lastMessage = null;

        if (messages.size() == 0) {
            return;
        }

        for (EMMessage emMessage : messages) {
            SLog.info("message[%s]", emMessage.toString());
            lastMessage = emMessage;
            if (ChatUtil.getIntMessageType(emMessage) == 0) {
                continue;
            }

            chatMessageList.add(emMessageToChatMessage(lastMessage));
        }
        if (messages != null) {
            startMsgId = messages.get(0).getMsgId();
        }
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        int pagesize = 20;

        int dbCount=loadFromDB(startMsgId,pagesize);
        /*
        if (lastMessage != null) {
            SLog.info("lastMessage[%s]", lastMessage.toString());
            chatMessageList.add(emMessageToChatMessage(lastMessage));
        }
        */


        setShowTimestamp(chatMessageList);
        SLog.info("_chatMessageList.size = %d", chatMessageList.size());
        chatMessageAdapter.setNewData(chatMessageList);
    }

    private int loadFromDB(String startMsgId, int pagesize) {
        int i = 0;
        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        for (EMMessage emMessage : messages) {
            SLog.info("message[%s]", emMessage.toString());
            if (ChatUtil.getIntMessageType(emMessage) == 0) {
                continue;
            }
            chatMessageList.add(i++,emMessageToChatMessage(emMessage));
        }
        if (messages == null) {
            return 0;
        } else {
            return messages.size();
        }
    }

    private ChatMessage emMessageToChatMessage(EMMessage emMessage) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.messageType = ChatUtil.getIntMessageType(emMessage);
        chatMessage.messageId = emMessage.getMsgId();
        SLog.info("chatMessage.messageType[%d]", chatMessage.messageType);

        String from = emMessage.getFrom();
        SLog.info("FROM[%s]", from);
        if (from.equals(myMemberName) ||
                StringUtil.isEmpty(from)) { // 如果from為空，也當作是自己的消息
            chatMessage.origin = ChatMessage.MY_MESSAGE;
        } else {
            chatMessage.origin = ChatMessage.YOUR_MESSAGE;
        }

        chatMessage.timestamp = emMessage.getMsgTime();
        if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_TXT) {
            // 去除 txt:" 前綴
            chatMessage.content = StringUtil.getEMMessageText(emMessage.getBody().toString());
        } else if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_GOODS) {
            String goodsImage = emMessage.getStringAttribute("goodsImage", "");
            int commonId = emMessage.getIntAttribute("commonId", 0);
            String goodsName = emMessage.getStringAttribute("goodsName", "");
            boolean btnSend = emMessage.getBooleanAttribute("btnSendShow", false);

            chatMessage.content = EasyJSONObject.generate("goodsImage", goodsImage, "commonId", commonId, "goodsName", goodsName).toString();
        } else if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_ORDER) {
            SLog.info("orders %s",emMessage.getStringAttribute("ordersId", ""));

//            SLog.info("orders %d",Integer.parseInt(emMessage.getStringAttribute("ordersId", "")));
//            int ordersId = Integer.parseInt(emMessage.getStringAttribute("ordersId", ""));
            int ordersId = 0;
            try {
               ordersId = Integer.parseInt(emMessage.getStringAttribute("ordersId", ""));
            } catch (Exception e) {
                try {
                    ordersId = emMessage.getIntAttribute("ordersId");
                } catch (HyphenateException e1) {
                    e1.printStackTrace();
                }

            }

            String ordersSn = emMessage.getStringAttribute("ordersSn", "");
            String goodsImage = emMessage.getStringAttribute("goodsImage", "");
            String goodsName = emMessage.getStringAttribute("goodsName", "");

            chatMessage.content = EasyJSONObject.generate("goodsImage", goodsImage, "goodsName", goodsName,
                                                            "ordersId", ordersId, "ordersSn", ordersSn).toString();
        } else if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_IMAGE) {
            String imgUrl = emMessage.getStringAttribute("ossUrl", "");
            String absolutePath = emMessage.getStringAttribute("absolutePath", "");

            chatMessage.content = EasyJSONObject.generate("imgUrl", imgUrl, "absolutePath", absolutePath).toString();
            SLog.info("chatMessage.content[%s]", chatMessage.content);
        } else if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_ENC) {
            SLog.info(emMessage.toString());
            chatMessage.trueName = yourTrueName;
            String avatar = emMessage.getStringAttribute("avatar", "");
            String nickname = emMessage.getStringAttribute("nickName", "");
            if (emMessage.ext() != null) {
                avatar = emMessage.ext().get("avatar").toString();
                nickname = emMessage.ext().get("nickName").toString();
                chatMessage.ext=emMessage.ext();
            }
            chatMessage.content = EasyJSONObject.generate("nickname", nickname,"avatar",avatar,"memberName",from).toString();
            chatMessage.fromMemberName = emMessage.getFrom();
            if (chatMessage.origin != ChatMessage.MY_MESSAGE) {
                hasCard = true;
            }
            SLog.info("chatMessage.content[%s]", chatMessage.content);
        }

        return chatMessage;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_menu:
                if (User.getUserId() <= 0) {
                    break;
                }
                updateExchangeCard(view);

                break;
            case R.id.btn_send_image:
                // openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal());
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .setMaxSelectCount(3) // 最多发送3张图片
                        .start(this, RequestCode.SELECT_MULTI_IMAGE.ordinal()); // 打开相册
                break;
            case R.id.btn_capture_image:
                PermissionUtil.actionWithPermission(_mActivity, new String[] {
                        Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE
                }, "拍攝照片/視頻需要授予", new CommonCallback() {
                    @Override
                    public String onSuccess(@Nullable String data) {
                        captureImageFile = CameraUtil.openCamera(_mActivity, ChatFragment.this, Constant.CAMERA_ACTION_IMAGE);
                        return null;
                    }

                    @Override
                    public String onFailure(@Nullable String data) {
                        ToastUtil.error(_mActivity, "您拒絕了授權");
                        return null;
                    }
                });

                break;
            case R.id.btn_send_goods:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ImStoreGoodsPopup(_mActivity, storeId, yourMemberName, "", this))
                        .show();
                break;
            case R.id.btn_send_order:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ImStoreOrderPopup(_mActivity, storeId, yourMemberName,this))
                        .show();
                break;
            case R.id.btn_send_enc:
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
                        }).asCustom(new TwConfirmPopup(_mActivity, "您確認要發送電子名片?","由於電子名片涉及個人隱私", "確認", "取消",new OnConfirmCallback() {
                    @Override
                    public void onYes() {
                        SLog.info("onYes");
                        EasyJSONObject extra = EasyJSONObject.generate(
                                "avatar", User.getUserInfo(SPField.FIELD_AVATAR, ""),
                                "nickName", User.getUserInfo(SPField.FIELD_NICKNAME, ""),
                                "memberName", User.getUserInfo(SPField.FIELD_MEMBER_NAME, "")
                        );
                        sendTextMessage("", CUSTOM_MESSAGE_TYPE_ENC, extra);
                    }

                    @Override
                    public void onNo() {
                        SLog.info("onNo");
                    }
                }))
                        .show();

                break;
                /* 常用語 和 定位 暫時屏蔽
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
                */
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

                    sendTextMessage(message, CUSTOM_MESSAGE_TYPE_TXT, null);
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
                    // rvMessageList.smoothScrollBy(0, 825);

                }
                break;
            default:
                break;
        }
    }

    private void updateExchangeCard(View v) {

        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(), "fromName", yourMemberName);
        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_IM_IS_EXCHANGE_CARD, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]",responseStr);
                EasyJSONObject responseObj =EasyJSONObject.parse(responseStr);

                try{
                    if (responseObj.exists("datas.isExchangeCard")) {
                        hasCard = responseObj.getInt("datas.isExchangeCard")==Constant.TRUE_INT;
                    }
                }catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

                new XPopup.Builder(_mActivity)
                        .offsetX(-Util.dip2px(_mActivity, 11))
                        .offsetY(-Util.dip2px(_mActivity, 8))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                        .hasShadowBg(false) // 去掉半透明背景
                        .atView(v)
                        .asCustom(new BlackDropdownMenu(_mActivity, ChatFragment.this, BlackDropdownMenu.TYPE_CHAT))
                        .show();
            }
        });
    }

    private void initChatUI(View contentView) {
        rvMessageList = contentView.findViewById(R.id.rv_message_list);
        rvMessageList.setViewSizeChangedListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvMessageList.setLayoutManager(layoutManager);


        if (friendInfo != null) {
            yourAvatarUrl = friendInfo.avatarUrl;
        }
        chatMessageAdapter = new ChatMessageAdapter(R.layout.chat_message_item, chatMessageList, yourAvatarUrl);
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

                    EasyJSONObject easyJSONObject = EasyJSONObject.parse(chatMessage.content);
                    if (easyJSONObject == null) {
                        return;
                    }

                    String absolutePath = null;
                    String imgUrl = null;
                    try {
                        absolutePath = easyJSONObject.getSafeString("absolutePath");
                        imgUrl = easyJSONObject.getSafeString("imgUrl");
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }

                    String imageUri;

                    // 優先加載本地的Copy
                    File file = new File(absolutePath);
                    if (file.isFile()) {
                        imageUri = absolutePath;
                    } else { // 否則，加載oss上的Copy
                        imageUri = StringUtil.normalizeImageUrl(imgUrl);
                    }

//                    Util.startFragment(ImageViewerFragment.newInstance(imageUri));

                    List<String> imageList = new ArrayList<>();
                    imageList.add(imageUri);
                    Util.startFragment(ViewPagerFragment.newInstance(imageList,false));

                } else if (id == R.id.img_your_avatar) {
                    hideSoftInput();
                    Util.startFragment(MemberInfoFragment.newInstance(yourMemberName));
                } else if (id == R.id.img_my_avatar) {
                    start(PersonalInfoFragment.newInstance());
                } else if(id ==R.id.btn_send&&friendInfo.goodsInfo!=null){
                    SLog.info("發送商品%d",position);
                    EasyJSONObject goodsInfo = EasyJSONObject.generate(
                            "goodsName", friendInfo.goodsInfo.goodsName,
                            "commonId", friendInfo.goodsInfo.commonId,
                            "goodsImage", friendInfo.goodsInfo.imageSrc);
                    adapter.remove(position);
                    sendTextMessage("",CUSTOM_MESSAGE_TYPE_GOODS,goodsInfo);
                } else if (id == R.id.ll_goods_message_container) {
                    ChatMessage chatMessage = chatMessageList.get(position);

                    SLog.info("chatMessage.content[%s]", chatMessage.content);
                    EasyJSONObject easyJSONObject = EasyJSONObject.parse(chatMessage.content);

                    try {
                        int commonId = easyJSONObject.getInt("commonId");
                        start(GoodsDetailFragment.newInstance(commonId, 0));
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                } else if (id == R.id.ll_order_message_container) {
                    ChatMessage chatMessage = chatMessageList.get(position);

                    SLog.info("chatMessage.content[%s]", chatMessage.content);
                    EasyJSONObject easyJSONObject = EasyJSONObject.parse(chatMessage.content);

                    try {
                        int ordersId = easyJSONObject.getInt("ordersId");
                        start(OrderDetailFragment.newInstance(ordersId));
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                } else if (id == R.id.ll_enc_message_container) {
                    ChatMessage chatMessage = chatMessageList.get(position);

                    try {
                        String memberName = chatMessage.fromMemberName;
                        start(ENameCardFragment.newInstance(memberName));
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
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
                            if (index >= emojiPage.emojiList.size()) {
                                return;
                            }
                            UnicodeEmojiItem emojiItem = emojiPage.emojiList.get(index);

                            Editable message = etMessage.getEditableText();
                            SLog.info("message[%s]", message);

                            // Get the selected text.
                            int start = etMessage.getSelectionStart();
                            int end = etMessage.getSelectionEnd();

                            // Insert the emoticon.
                            if (start < 0) {
                                start = 0;
                            }
                            if (end < 0) {
                                end = 0;
                            }
                            message.replace(start, end, emojiItem.emoji);
                            SLog.info("message[%s]", message);

                            etMessage.setText(message);
                            // 重新定位光標
                            etMessage.setSelection(start + emojiItem.emoji.length());
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

    /**
     * 發送文本消息
     * @param content 如果是普通文本消息，是文本内容， 如果是產品或訂單，傳空即可，然后在方法體里面生成產品或訂單的信息JSON字符串
     * @param messageType 消息類型
     *                    txt -- 普通文本消息
     *                    goods -- 產品文本消息
     *                    orders -- 訂單文本消息
     *                    enc -- 電子名片
     * @param extra 附加數據
     */
    private void sendTextMessage(String content, String messageType, Object extra) {
        //Im發送消息的源頭方法
        SLog.info("content[%s], messageType[%s], extra[%s]", content, messageType, extra);
        try {
            String pushContent = content;

            //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
            if (CUSTOM_MESSAGE_TYPE_IMG.equals(messageType)) {
                pushContent = "[圖片]";
            }else if (CUSTOM_MESSAGE_TYPE_GOODS.equals(messageType)) {
                /*
                   這種結構
                   {
                        "goodsName", goodsInfo.getSafeString("goodsName"),
                        "commonId", goodsInfo.getInt("commonId"),
                        "goodsImage", goodsInfo.getSafeString("goodsImage")
                    }
                 */
                content = extra.toString();
            } else if (CUSTOM_MESSAGE_TYPE_ORDERS.equals(messageType)) {
                ImStoreOrderItem imStoreOrderItem = (ImStoreOrderItem) extra;
                content = EasyJSONObject.generate(
                        "ordersId", String.valueOf(imStoreOrderItem.ordersId),
                        "ordersSn", imStoreOrderItem.ordersSn,
                        "goodsImage", imStoreOrderItem.goodsImage,
                        "goodsName", imStoreOrderItem.goodsName
                ).toString();
            } else if (CUSTOM_MESSAGE_TYPE_ENC.equals(messageType)) {
                content = extra.toString();
            }
            EMMessage message = EMMessage.createTxtSendMessage(content, yourMemberName);
            message.setAttribute("messageType", messageType);
            if (CUSTOM_MESSAGE_TYPE_GOODS.equals(messageType)) {
                EasyJSONObject goodsInfo = (EasyJSONObject) extra;

                message.setAttribute("goodsName", goodsInfo.getSafeString("goodsName"));
                message.setAttribute("commonId", goodsInfo.getInt("commonId"));
                message.setAttribute("goodsImage", goodsInfo.getSafeString("goodsImage"));
                pushContent = ChatConversation.LAST_MESSAGE_DESC_GOODS;
//                message.setAttribute("showSendBtn", goodsInfo.getBoolean("showSendBtn"));
            } else if (CUSTOM_MESSAGE_TYPE_ORDERS.equals(messageType)) {
                ImStoreOrderItem imStoreOrderItem = (ImStoreOrderItem) extra;

                message.setAttribute("ordersId", String.valueOf(imStoreOrderItem.ordersId));
                message.setAttribute("ordersSn", imStoreOrderItem.ordersSn);
                message.setAttribute("goodsImage", imStoreOrderItem.goodsImage);
                message.setAttribute("goodsName", imStoreOrderItem.goodsName);
                pushContent = ChatConversation.LAST_MESSAGE_DESC_ORDERS;

            } else if (CUSTOM_MESSAGE_TYPE_ENC.equals(messageType)) {
                EasyJSONObject encInfo = (EasyJSONObject) extra;
//                message.ext().put("avatar", encInfo.getSafeString("avatar))"));
//                message.ext().put("nickName", encInfo.getSafeString("nickName))"));
                message.setAttribute("nickName",encInfo.getSafeString("nickName"));
                message.setAttribute("avatar",encInfo.getSafeString("avatar"));
                SLog.info(message.ext().toString());
                message.setFrom(((EasyJSONObject) extra).getSafeString("memberName"));
                pushContent = ChatConversation.LAST_MESSAGE_DESC_ENC;

            }

            ChatUtil.setMessageCommonAttr(message, messageType,pushContent);
            SLog.info("message[%s],ext[%s], yourMemberName[%s]", message,message.ext().toString(), yourMemberName);

            //发送消息
            EMClient.getInstance().chatManager().sendMessage(message);
            ChatUtil.getConversation(yourMemberName, yourNickname, yourAvatarUrl, yourRole);
            message.setMessageStatusCallback(new EMCallBack(){
                @Override
                public void onSuccess() {
                    SLog.info("onSuccess, body[%s]", message.getBody());

                    try {
                        if (CUSTOM_MESSAGE_TYPE_TXT.equals(messageType)) {
                            Api.imSendMessage(yourMemberName, messageType, message.getMsgId(), message.getBody().toString(),
                                    null, null, 0, null, null, 0, null);
                        } else if (CUSTOM_MESSAGE_TYPE_GOODS.equals(messageType)) {
                            EasyJSONObject goods = (EasyJSONObject) extra;
                            Api.imSendMessage(yourMemberName, messageType, message.getMsgId(), message.getBody().toString(),
                                    null, null, goods.getInt("commonId"), goods.getSafeString("goodsName"), goods.getSafeString("goodsImage"), 0, null);
                        } else if (CUSTOM_MESSAGE_TYPE_ORDERS.equals(messageType)) {
                            ImStoreOrderItem imStoreOrderItem = (ImStoreOrderItem) extra;
                            Api.imSendMessage(yourMemberName, messageType, message.getMsgId(), message.getBody().toString(),
                                    null, null, 0, imStoreOrderItem.goodsName, imStoreOrderItem.goodsImage, imStoreOrderItem.ordersId, imStoreOrderItem.ordersSn);
                        } else if (CUSTOM_MESSAGE_TYPE_ENC.equals(messageType)) {
                            Api.imSendMessage(yourMemberName, messageType, message.getMsgId(), message.ext().toString(),
                                    null, null, 0, null, null, 0, null);
                        }
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onError(int i, String s) {
                    SLog.info("onError, i[%d], s[%s]", i, s);
                    // ToastUtil.error(_mActivity, s);
                }

                @Override
                public void onProgress(int i, String s) {
                    SLog.info("onProgress, i[%d], s[%s]", i, s);
                }
            });

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.messageType = ChatUtil.getIntMessageType(message);
            SLog.info("chatMessage.messageType[%d]", chatMessage.messageType);
            chatMessage.messageId = message.getMsgId();
            chatMessage.content = content;
            if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_GOODS) {
                EasyJSONObject easyJSONObject = (EasyJSONObject) extra;
                String goodsName = easyJSONObject.getSafeString("goodsName");
                int commonId = easyJSONObject.getInt("commonId");
                String goodsImage = easyJSONObject.getSafeString("goodsImage");
                chatMessage.content = EasyJSONObject.generate("goodsName", goodsName, "commonId", commonId, "goodsImage", goodsImage).toString();
            } else if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_ORDER) {
                ImStoreOrderItem imStoreOrderItem = (ImStoreOrderItem) extra;

                chatMessage.content = EasyJSONObject.generate(
                        "ordersId", imStoreOrderItem.ordersId, "ordersSn", imStoreOrderItem.ordersSn,
                        "goodsImage", imStoreOrderItem.goodsImage, "goodsName", imStoreOrderItem.goodsName).toString();
            } else if (chatMessage.messageType == Constant.CHAT_MESSAGE_TYPE_ENC) {
                chatMessage.trueName = myTrueName;
            }

            chatMessage.origin = ChatMessage.MY_MESSAGE;
            chatMessage.timestamp = message.getMsgTime();
            chatMessageList.add(chatMessage);

            // chatMessageAdapter.notifyItemInserted(chatMessageList.size() - 1);
            setShowTimestamp(chatMessageList);
            chatMessageAdapter.notifyDataSetChanged();
            messageListScrollToBottom();
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
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
        hideSoftInputPop();
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
    public void onSupportVisible() {
        super.onSupportVisible();
        ((MainActivity) getActivity()).setCurrChatMemberName(yourMemberName);
        ((MainActivity) getActivity()).setMessageFragmentsActivity(true);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ((MainActivity) getActivity()).setCurrChatMemberName(null);
        ((MainActivity) getActivity()).setMessageFragmentsActivity(false);

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

                new XPopup.Builder(_mActivity)
                        .dismissOnBackPressed(true) // 按返回键是否关闭弹窗，默认为true
                        .dismissOnTouchOutside(true) // 点击外部是否关闭弹窗，默认为true
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ImagePreviewPopup(_mActivity, this, absolutePath))
                        .show();
            } else {
                absolutePath = captureImageFile.getAbsolutePath();  // 拍照得到的文件路徑
                SLog.info("absolutePath[%s]", absolutePath);

                sendImageMessage(absolutePath);
            }
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
                sendImageMessage(item);
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

    /**
     * 發送圖片
     * @param absolutePath 圖片的本地絕對路徑
     */
    private void sendImageMessage(String absolutePath) {
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
                    SLog.info("Error!Upload failed");
                    return;
                }

                SLog.info("ossUrl[%s], absoluteUrl[%s]", result.first, result.second);

                //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
                EMMessage message = EMMessage.createImageSendMessage(absolutePath, false, yourMemberName);
                chatMessage.messageId = message.getMsgId();
                message.setAttribute("messageType", CUSTOM_MESSAGE_TYPE_IMG);
                ChatUtil.setMessageCommonAttr(message, CUSTOM_MESSAGE_TYPE_IMG, ChatConversation.LAST_MESSAGE_DESC_IMAGE);

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
                        Api.imSendMessage(yourMemberName, "img", message.getMsgId(), message.getBody().toString(),
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


    public void setConversation(EMConversation conversation) {
        this.conversation = conversation;
    }

    public void setFriendInfo(FriendInfo friendInfo) {
        this.friendInfo = friendInfo;
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
        if (type == PopupType.IM_CHAT_COMMON_USED_SPEECH) {  // 常用語
            CommonUsedSpeech speech = (CommonUsedSpeech) extra;

            if (speech.dataType == CommonUsedSpeech.DATA_TYPE_SPEECH) { // 常用語
                sendTextMessage(speech.content, CUSTOM_MESSAGE_TYPE_TXT, null);
            } else { // 常用版式
                sendTextMessage(speech.content, CUSTOM_MESSAGE_TYPE_TXT, null);
            }
        } else if (type == PopupType.IM_CHAT_SEND_GOODS) {
            sendTextMessage("", CUSTOM_MESSAGE_TYPE_GOODS, extra);
        } else if (type == PopupType.IM_CHAT_SEND_ORDER) {
            sendTextMessage("", CUSTOM_MESSAGE_TYPE_ORDERS, extra);
        }
    }

    public String getYourMemberName() {
        return yourMemberName;
    }

    public boolean getCard() {
        return hasCard;
    }

    @Override
    public void onSimpleCall(Object data) {
        EasyJSONObject dataObj = (EasyJSONObject) data;
        try {
            int action = dataObj.getInt("action");
            if (action == SimpleCallback.ACTION_SELECT_IMAGE) {
                String absolutePath = dataObj.getSafeString("absolute_path");
                sendImageMessage(absolutePath);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public int getStoreId() {
        return storeId;
    }

    static class UiHandler extends Handler {
        WeakReference<ChatFragment> weakReference;

        public UiHandler(ChatFragment chatFragment) {
            weakReference = new WeakReference<>(chatFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ChatFragment chatFragment = weakReference.get();
            chatFragment.chatMessageAdapter.notifyDataSetChanged();
            chatFragment.swipeRefreshLayout.setRefreshing(false);
        }
    }
}
