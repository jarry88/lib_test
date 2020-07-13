package com.ftofs.twant.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.ChatConversationAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.ChatConversation;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.UnreadCount;
import com.ftofs.twant.interfaces.DiffCallBack;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Conversation;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.tangram.SloganView;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.BadgeUtil;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.SqliteUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.member.MemberVo;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.MaxHeightRecyclerView;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SlantedWidget;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;


/**
 * 消息
 * @author zwm
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 是否獨立的Fragment，還是依附于MainFragment
     */
    boolean isStandalone;
    /**
     * 是否為平台客服列表頁
     */
    boolean isPlatformCustomer;
    ScaledButton btnBack;
    ScaledButton btnContact;
    ScaledButton btnPlatformCustomer;
    ChatConversationAdapter adapter;
    ChatConversation platformCustomer;

    LinearLayout llMessageListContainer;
    List<String> updateConversationList =new ArrayList<>();

    int totalIMUnreadCount;  // 未讀IM消息總數
    UnreadCount unreadCount;



    TextView tvTransactMessageItemCount;
    TextView tvAssetMessageItemCount;
    TextView tvSocialMessageItemCount;
    TextView tvBargainMessageItemCount;
    TextView tvNoticeMessageItemCount;


    boolean isImLogin =false;

    List<ChatConversation> chatConversationList = new ArrayList<>();
    public static int PLATFORM_CUSTOM_STORE_ID=41;//平臺客服店鋪常數

    public static MessageFragment newInstance(boolean isStandalone) {
        Bundle args = new Bundle();

        args.putBoolean("isStandalone", isStandalone);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static MessageFragment newInstance(boolean isStandalone,boolean isPlatformCustomer) {
        Bundle args = new Bundle();

        args.putBoolean("isStandalone", isStandalone);
        MessageFragment fragment = new MessageFragment();
        fragment.isPlatformCustomer = isPlatformCustomer;
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        isStandalone = args.getBoolean("isStandalone");

        Util.setOnClickListener(view, R.id.btn_message_search, this);
        Util.setOnClickListener(view, R.id.btn_message_menu, this);
        //Util.setOnClickListener(view, R.id.btn_view_logistics_message, this);
        //Util.setOnClickListener(view, R.id.btn_view_refund_message, this);

        Util.setOnClickListener(view, R.id.btn_transact_message, this);
        Util.setOnClickListener(view, R.id.btn_asset_message, this);
        Util.setOnClickListener(view, R.id.btn_social_message, this);
        Util.setOnClickListener(view, R.id.btn_bargain_message, this);
        Util.setOnClickListener(view, R.id.btn_notice_message, this);

        btnBack = view.findViewById(R.id.btn_back);
        btnContact = view.findViewById(R.id.btn_contact);

        btnPlatformCustomer = view.findViewById(R.id.btn_switch_customer);
        btnPlatformCustomer.setOnClickListener(this);

        if (isStandalone) {
            btnBack.setOnClickListener(this);
            btnBack.setVisibility(View.VISIBLE);
            btnContact.setVisibility(View.GONE);
        } else {
            btnContact.setOnClickListener(this);
            btnBack.setVisibility(View.GONE);
            btnContact.setVisibility(View.VISIBLE);
        }

        llMessageListContainer = view.findViewById(R.id.ll_message_list_container);

        tvTransactMessageItemCount = view.findViewById(R.id.tv_transact_message_item_count);
        tvAssetMessageItemCount = view.findViewById(R.id.tv_asset_message_item_count);
        tvSocialMessageItemCount = view.findViewById(R.id.tv_social_message_item_count);
        tvBargainMessageItemCount = view.findViewById(R.id.tv_bargain_message_item_count);
        tvNoticeMessageItemCount = view.findViewById(R.id.tv_notice_message_item_count);

        MaxHeightRecyclerView rvChatConversationList = view.findViewById(R.id.rv_chat_conversation_list);
        llMessageListContainer.post(() -> {
            int height = llMessageListContainer.getHeight();
            rvChatConversationList.setMaxHeight(height);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvChatConversationList.setLayoutManager(layoutManager);

        if (isPlatformCustomer) {
            initPlatformCustomer(view);
            loadPlatformCustomerData();
        }
        adapter = new ChatConversationAdapter(R.layout.chat_conversation_im, chatConversationList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ChatConversation chatConversation = chatConversationList.get(position);
                if (chatConversation == null) {
                    return;
                }
                if (!isPlatformCustomer&&position == 0) {
                    SLog.info("點擊了平台客服");
                    Util.startFragment(newInstance(true,true));
                    return;
                }
                SLog.info("friendInfo[%s]", chatConversation.friendInfo);
                EMConversation conversation = ChatUtil.getConversation(chatConversation.friendInfo.memberName,
                        chatConversation.friendInfo.nickname, chatConversation.friendInfo.avatarUrl, ChatUtil.ROLE_CS_AVAILABLE);

                if (chatConversation.unreadCount > 0) {
                    // 從未讀總數中減去這條會話的未讀數

//                    chatConversation.unreadCount = 0;
                    totalIMUnreadCount -= chatConversation.unreadCount;
                    displayUnreadCount();
                }

                Util.startFragment(ChatFragment.newInstance(conversation, chatConversation.friendInfo));
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ChatConversation chatConversation = chatConversationList.get(position);

                new XPopup.Builder(getContext())
//                    .maxWidth(600)
                        .asCenterList("請選擇操作", new String[] {"刪除該聊天"},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        SLog.info("position[%d], text[%s]", position, text);
                                        if (position == 0) {
                                            showDeleteConversationConfirm(chatConversation.friendInfo.memberName);
                                        }
                                    }
                                })
                        .show();
                return true;
            }
        });

        rvChatConversationList.setAdapter(adapter);

        loadUnreadMessageCount();
    }

    private void initPlatformCustomer(View view) {
        TextView textView = view.findViewById(R.id.tv_fragment_title);
        textView.setText(getString(R.string.text_platform_customer));
        view.findViewById(R.id.ll_amount_container).setVisibility(View.GONE);

        view.findViewById(R.id.btn_switch_customer).setVisibility(View.GONE);
        view.findViewById(R.id.btn_message_search).setVisibility(View.GONE);
        view.findViewById(R.id.btn_message_menu).setVisibility(View.GONE);
        view.findViewById(R.id.vw_separator).setVisibility(View.GONE);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_NEW_CHAT_MESSAGE ||
                message.messageType == EBMessageType.MESSAGE_TYPE_UPDATE_TOOLBAR_RED_BUBBLE ||
                message.messageType == EBMessageType.MESSAGE_TYPE_LOGIN_SUCCESS) {
            if (isPlatformCustomer) {

            } else {
                loadData();
            }
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_LOGOUT_SUCCESS) { // 如果用戶退出登錄，重新加載數據
            isImLogin = false;
        }
    }

    /**
     * 彈出是否要刪除該會話的確認框
     * @param memberName
     */
    private void showDeleteConversationConfirm(String memberName) {
        SLog.info("showDeleteConversationConfirm, memberName[%s]", memberName);

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
                }).asCustom(new TwConfirmPopup(_mActivity, "確認", "刪除后，將清空該聊天的消息記錄", new OnConfirmCallback() {
            @Override
            public void onYes() {
                SLog.info("onYes");
                // 刪除選定的會話及其所有聊天
                EMClient.getInstance().chatManager().deleteConversation(memberName, true);
                loadData();
            }

            @Override
            public void onNo() {
                SLog.info("onNo");
            }
        }))
                .show();
    }

    private void loadData() {
        try {
            totalIMUnreadCount = 0;
            chatConversationList.clear();

            Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
            saveEMConversation(conversationMap);


            List<Conversation> allConversations = Conversation.getAllConversations(); //查询Conversation表的所有数据
            SLog.info("来自网络會話數[%d]，本地保存的会话数[%d]", conversationMap.size(),allConversations.size());
            updateConversationList.clear();
            for (Conversation conversation : allConversations) {
                //此處要增加帶s標識member Name和平臺客服的過濾
                String memberName = conversation.memberName;
                if (memberName.contains("s")) {//存在s的舊對話
                    EMClient.getInstance().chatManager().deleteConversation(memberName, true);
                    SLog.info("memberName：%s",memberName);
                    continue;
                };
                long timestamp = conversation.lastMessageTime;
                SLog.info("本地保存记录：%s,时间%d",conversation.nickname,timestamp);
                if (conversation.needUpdate()) {
//                    SLog.info("%s需要更新",memberName);
                    updateConversationList.add(memberName);
                }

                //不是每條都在數據庫保存過，直接取數據庫不可以 gzp
                //如果没有找到会返回一个新建的 friendinfo 所以friendinfo 不会为空
                FriendInfo friendInfo = FriendInfo.getFriendInfoByMemberName(memberName);
                String extField = conversation.extField;
                SLog.info("friendName,[%s},extField[%s],Conversation[%s]",friendInfo.memberName, extField,conversation.toString());
                if (!StringUtil.isEmpty(conversation.nickname)) {
                    //获取环形的最新数据，更新friendInfo
                    friendInfo = conversation.toFriendInfo();
                    SLog.info("會話框數據從extFied得到");
//                    friendInfo.storeName = extFieldObj.getSafeString("storeName");
                } else {
                    // 如果没有扩展字段，并且数据库有保存，则从数据库中拿
                    SLog.info("會話框數據從數據庫得到");
                }


                SLog.info("unreadCount[%d],conversationName[%s],friend memberName[%s], lastMessage[%s], timestamp[%s], nickname[%s], avatar[%s]",
                       conversation.unreadMsgCount, conversation.memberName,friendInfo.memberName, conversation.lastMessageText, Time.fromMillisUnixtime(timestamp, "Y-m-d H:i:s"),
                        friendInfo.nickname, friendInfo.avatarUrl);

                SLog.info("friendInfo[%s],lastMessage %s", friendInfo,conversation.lastMessageText);

                ChatConversation chatConversation = new ChatConversation();

                chatConversation.friendInfo = friendInfo;
                chatConversation.unreadCount = conversation.unreadMsgCount;
                chatConversation.lastMessageType = conversation.lastMessageType;
                chatConversation.lastMessage = conversation.lastMessageText;
                if (timestamp > 1) {
                    chatConversation.timestamp = timestamp;
                }
                totalIMUnreadCount += chatConversation.unreadCount;
                SLog.info("here!!storeid%d, platId%d",friendInfo.storeId,PLATFORM_CUSTOM_STORE_ID);
               if (friendInfo.storeId != PLATFORM_CUSTOM_STORE_ID&&!StringUtil.isEmpty(friendInfo.nickname)) {
                    //如果不是平臺客服鏈接
                   chatConversationList.add(chatConversation);
               }else{

               }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                chatConversationList.sort((o1, o2) -> comO1O2(o1,o2));
            }
            // 獲取環信所有會話列表
            if (!isPlatformCustomer) {
                if (platformCustomer == null) {
                    platformCustomer = new ChatConversation();
                    platformCustomer.friendInfo = FriendInfo.newInstance("", "想要小城", "", ChatUtil.ROLE_CS_PLATFORM);
                    platformCustomer.lastMessageType = Constant.CHAT_MESSAGE_TYPE_TXT;
                    platformCustomer.lastMessage = "     歡迎來到想要城～有什麼想要了解... ";
                    platformCustomer.isPlatformCustomer = true;
                }

                chatConversationList.add(0,platformCustomer);
            }
            updateConversationInfo();
            SLog.info("updateSize[%s]",updateConversationList.size());
            displayUnreadCount();
            if (chatConversationList.size() > 2) {
                //在結尾添加一個空白item
                chatConversationList.add(null);
            }
//            chatConversationList.add(null);

            adapter.setNewData(chatConversationList);
//            adapter.submitList(chatConversationList);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void updateConversationInfo() {
        if (updateConversationList.size() > 0) {
            SLog.info("更新了一波本地會話數據庫%d",updateConversationList.size());
            for (String memberName : updateConversationList) {
                ApiUtil.getImInfo(_mActivity, memberName,memberVo->{
                    MemberVo member = (MemberVo) memberVo;
                    Conversation.upsertConversationByMemberVo(member);
                    for (ChatConversation chatConversation : chatConversationList) {
                        if (chatConversation.friendInfo.memberName.equals(memberName)) {
                            chatConversation.friendInfo.role = member.role;
                            chatConversation.friendInfo.nickname = member.role > 0 ? member.storeName + " " + member.getNickName(): member.getNickName();
                            chatConversation.friendInfo.storeName = member.storeName ;
                            chatConversation.friendInfo.avatarUrl = member.role > 0 ? member.storeAvatar : member.getAvatar();
                            chatConversation.friendInfo.storeAvatarUrl = member.storeAvatar;
                            SLog.info("storeavatar[%s]",member.storeAvatar);
                        }
                    }
//                    adapter.notifyDataSetChanged();
                    adapter.submitList(chatConversationList);
                });
            }
        }
    }

    private void saveEMConversation(Map<String, EMConversation> conversationMap) {
        int count = 0;
        for (Map.Entry<String, EMConversation> entry : conversationMap.entrySet()) {
            EMConversation conversation = entry.getValue();
            String memberName = entry.getKey();
            Conversation.upsertConversationInfo(memberName,conversation.getAllMessages(),conversation.getLastMessage(),conversation.getExtField(),conversation.getUnreadMsgCount());
            Conversation loacal = Conversation.getByMemberName(memberName);
            if (loacal == null) {
                SLog.info("Error,存取對話失敗");
            } else {
                SLog.info("第%d个，memberName[%s],nickName %s,lastMessage %s,unread[%d]",count++ ,memberName, loacal.nickname,loacal.lastMessageText,conversation.getUnreadMsgCount());
            }
        }
    }

    private void loadPlatformCustomerData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params=EasyJSONObject.generate("token",token);

        SLog.info("token[%s]", token);
        Api.getUI(Api.ADMIN_STAFF, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    EasyJSONArray adminStaffList = responseObj.getSafeArray("datas.adminStaffList");
                    totalIMUnreadCount = 0;
                    chatConversationList.clear();
                    SLog.info("responseStr11");

                    for (Object object : adminStaffList) {
                        EasyJSONObject admin = (EasyJSONObject) object;
                        String memberName = admin.getSafeString("memberName");
                        String storeName = admin.getSafeString("storeName");
                        String nickName = admin.getSafeString("nickName");
                        String groupName = admin.getSafeString("groupName");
                        String avatar = admin.getSafeString("avatar");
                        FriendInfo friendInfo = FriendInfo.newInstance(memberName, nickName, avatar, ChatUtil.ROLE_CS_PLATFORM);
                        friendInfo.storeName = storeName;
                        friendInfo.groupName = groupName;
                        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(memberName,
                                EMConversation.EMConversationType.Chat, true);

                        ChatConversation chatConversation = new ChatConversation();
                        chatConversation.friendInfo = friendInfo;
                        chatConversation.unreadCount = conversation.getUnreadMsgCount();
                        EMMessage lastMessage = conversation.getLastMessage();
                        if (lastMessage != null) {
                            chatConversation.lastMessageType = ChatUtil.getIntMessageType(lastMessage);
                            chatConversation.lastMessage = lastMessage.getBody().toString();
                            chatConversation.timestamp = lastMessage.getMsgTime();
                        }
//


                        SLog.info("friendInfo[%s]", friendInfo);

                        totalIMUnreadCount += chatConversation.unreadCount;

                        chatConversationList.add(chatConversation);
                    }
                    displayUnreadCount();

                    // 消息排序
                    Collections.sort(chatConversationList, (o1, o2) -> comO1O2(o1,o2));


//                    adapter.setNewData(chatConversationList);
                    adapter.submitList(chatConversationList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private int comO1O2(ChatConversation o1, ChatConversation o2) {
        if (o1.unreadCount > 0 && o2.unreadCount > 0 || o1.unreadCount == o2.unreadCount && o1.unreadCount == 0) {
            return (int) ( o2.timestamp-o1.timestamp);
        } else {
            if (o1.unreadCount > 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private void loadUnreadMessageCount() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate("token", token);

        SLog.info("params[%s]", params);

        Api.getUI(Api.PATH_GET_UNREAD_MESSAGE_COUNT, params, new UICallback() {
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


                    UnreadCount unreadCount = UnreadCount.processUnreadList(responseObj.getSafeArray("datas.unreadList"));
                    if (unreadCount != null) {
                        UnreadCount.save(unreadCount);
                        displayUnreadCount();
                    }

                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 更新未讀數提示
     */
    private void displayUnreadCount() {
        int totalUnreadCount = totalIMUnreadCount;


        UnreadCount unreadCount = UnreadCount.get();
        if (unreadCount == null) {
            unreadCount = new UnreadCount();
        }

        if (unreadCount.transact > 0) {
            //tvTransactMessageItemCount.setText(String.valueOf(unreadCount.transact));
            tvTransactMessageItemCount.setVisibility(View.VISIBLE);
            //tvTransactMessageItemCount.setTextSize(10);
        } else {
            tvTransactMessageItemCount.setVisibility(View.GONE);
        }

        if (unreadCount.asset > 0) {
            //tvAssetMessageItemCount.setText(String.valueOf(unreadCount.asset));
            tvAssetMessageItemCount.setVisibility(View.VISIBLE);
            //tvAssetMessageItemCount.setTextSize(10);
        } else {
            tvAssetMessageItemCount.setVisibility(View.GONE);
        }

        if (unreadCount.social > 0) {
            //tvSocialMessageItemCount.setText(String.valueOf(unreadCount.social));
            tvSocialMessageItemCount.setVisibility(View.VISIBLE);
            //tvSocialMessageItemCount.setTextSize(10);
        } else {
            tvSocialMessageItemCount.setVisibility(View.GONE);
        }

        if (unreadCount.bargain > 0) {
            //tvBargainMessageItemCount.setText(String.valueOf(unreadCount.bargain));
            //tvBargainMessageItemCount.setTextSize(10);
            tvBargainMessageItemCount.setVisibility(View.VISIBLE);
        } else {
            tvBargainMessageItemCount.setVisibility(View.GONE);
        }

        if (unreadCount.notice > 0) {
            //tvNoticeMessageItemCount.setText(String.valueOf(unreadCount.notice));
            //tvNoticeMessageItemCount.setTextSize(10);
            tvNoticeMessageItemCount.setVisibility(View.VISIBLE);
        } else {
            tvNoticeMessageItemCount.setVisibility(View.GONE);
        }


        //totalUnreadCount += (unreadCount.transact + unreadCount.asset + unreadCount.social + unreadCount.bargain + unreadCount.notice);


        MainFragment mainFragment = MainFragment.getInstance();
        if (mainFragment != null) {
            mainFragment.setMessageItemCount(totalUnreadCount);
        }


        BadgeUtil.setBadgeNum(_mActivity, totalUnreadCount);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_message_search) {
            Util.startFragment(SearchFriendFragment.newInstance());
        } else if (id == R.id.btn_message_menu) {
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 6))
                    .offsetY(-Util.dip2px(_mActivity, 8))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_MESSAGE))
                    .show();
        } else if (id == R.id.btn_contact) {
            Util.startFragment(ContactFragment.newInstance());
        } else if (id == R.id.btn_transact_message || id == R.id.btn_asset_message || id == R.id.btn_social_message ||
                    id == R.id.btn_bargain_message || id == R.id.btn_notice_message) {
            Util.startFragment(MessageListFragment.newInstance(getMessageTplClass(id)));
        }
        else if (id == R.id.btn_switch_customer) {
            if (!isPlatformCustomer) {
                SLog.info("點擊了平台客服按鈕");
                Util.startFragment(newInstance(true,true));
            }
        }
    }

    /**
     * 按鈕Id對應到消息模板代碼
     * @param btnId
     * @return
     */
    private int getMessageTplClass(int btnId) {
        if (btnId == R.id.btn_transact_message) {
            return Constant.TPL_CLASS_TRANSACT;
        } else if (btnId == R.id.btn_asset_message) {
            return Constant.TPL_CLASS_ASSET;
        } else if (btnId == R.id.btn_social_message) {
            return Constant.TPL_CLASS_SOCIAL;
        } else if (btnId == R.id.btn_bargain_message) {
            return Constant.TPL_CLASS_BARGAIN;
        } else if (btnId == R.id.btn_notice_message) {
            return Constant.TPL_CLASS_NOTICE;
        }
        return 0;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (isStandalone) {
            hideSoftInputPop();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SLog.info("onActivityResult, requestCode[%d]", requestCode);

        /*
         * 处理二维码扫描结果
         */
        if (requestCode == RequestCode.SCAN_QR_CODE.ordinal()) {
            Util.handleQRCodeResult(_mActivity, data);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        updateMainSelectedFragment(MainFragment.MESSAGE_FRAGMENT);

        int userId = User.getUserId();
        if (userId < 1) { // 用戶未登錄，顯示登錄頁面
            Util.showLoginFragment();
            return;
        }
        ((MainActivity) getActivity()).setMessageFragmentsActivity(true);

        SqliteUtil.imLogin();
        if (isPlatformCustomer) {
            SLog.info("onSupportVisible");
            loadPlatformCustomerData();
        } else {
            loadConversation();
            loadData();
        }
        for (ChatConversation chatConversation : chatConversationList) {
            if (chatConversation == null) {
                continue;
            }
//            SLog.info("unread[%d]", chatConversation.unreadCount);
        }
        displayUnreadCount();
    }

    private void loadConversation() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate("token", token);

        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_GET_IM_CONVERSATION, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr [%s]", responseStr);
                try {
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    EasyJSONArray conversationList = responseObj.getArray("datas.conversationList");
                    int oldCount = chatConversationList.size();
                    if (conversationList != null && conversationList.length() > 0) {
                        for (Object object : conversationList) {
                            EasyJSONObject conversation = (EasyJSONObject) object;
                            FriendInfo friendInfo = FriendInfo.parse(conversation);
                            if (friendInfo == null) {
                                continue;
                            }
//                            SLog.info(conversation.toString());
                            String messageContent=conversation.getSafeString("messageContent");
                            if (EasyJSONBase.isJSONString(messageContent)) {
                                messageContent = "[電子名片]";
                            }
                            if (messageContent.startsWith("image")) {
                                messageContent = "[圖片]";
                            }

                            SLog.info("messageFragment [%s]",messageContent);
                            String sendTime =conversation.getSafeString("sendTime");
                            boolean has = false;
                            for (ChatConversation chatConversation : chatConversationList) {
                                if (chatConversation == null) {
                                    continue;
                                }
                                if (chatConversation.friendInfo != null&&friendInfo!=null) {
                                    if (TextUtils.equals(chatConversation.friendInfo.memberName,friendInfo.memberName)&&friendInfo.memberName!=null) {
                                        has = true;
                                        int timestamp = Jarbon.parse(sendTime).getTimestamp();
                                        chatConversation.friendInfo = friendInfo;
                                        if (StringUtil.isEmpty(chatConversation.lastMessage)) {
                                            chatConversation.lastMessageType = Constant.CHAT_MESSAGE_TYPE_TXT;
                                            chatConversation.lastMessage = "txt::" + messageContent + ":";
                                            chatConversation.timestamp = timestamp;
                                            SLog.info("db[%s]timestamp[%s]", chatConversation.timestamp, timestamp);
                                        }
//                                        else {
//                                            if (chatConversation.timestamp<timestamp) {
//                                                chatConversation.timestamp = timestamp;
//                                            }
                                        break;
                                    }
                                }
                            }

                            if (!has) {
                                ChatConversation newChat = new ChatConversation();
                                int time =Jarbon.parse(sendTime).getTimestamp();
                                newChat.friendInfo = friendInfo;
                                SLog.info("messageFragment [%s]","1");

                                newChat.lastMessageType = Constant.CHAT_MESSAGE_TYPE_TXT;
                                newChat.lastMessage = "txt::"+messageContent+":";
//                                newChat.timestamp = time;
                                Conversation conversation1 = Conversation.getByMemberName(friendInfo.memberName);
                                if (friendInfo.role == 0) {
                                    conversation1.nickname = friendInfo.nickname;
                                } else if (friendInfo.role == ChatUtil.ROLE_CS_PLATFORM) {
                                    conversation1.nickname = friendInfo.nickname;
                                } else {
                                    conversation1.nickname = friendInfo.storeName+" "+friendInfo.nickname;
                                }
                                conversation1.avatarUrl = friendInfo.role>0?friendInfo.storeAvatarUrl:friendInfo.avatarUrl;
                                conversation1.storeAvatarUrl = friendInfo.storeAvatarUrl;
                                conversation1.lastMessageText = messageContent;
                                conversation1.lastMessageType = Constant.CHAT_MESSAGE_TYPE_TXT;
                                conversation1.storeId = friendInfo.storeId;
                                conversation1.role = friendInfo.role;
                                conversation1.timestamp = time;
                                conversation1.save();
//                                newChat.timestamp = sendTime;

                                Conversation.saveNewChat(newChat);
                                SLog.info("messageFragment [%s]","second");

                                chatConversationList.add(newChat);
                            }
                        }
                    }
//                    if (chatConversationList.size() > oldCount) {
                    SLog.info("messageFragment [%s]","3");

//                    adapter.notifyDataSetChanged();
                    adapter.submitList(chatConversationList);
//                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ((MainActivity) getActivity()).setMessageFragmentsActivity(false);

    }
}
