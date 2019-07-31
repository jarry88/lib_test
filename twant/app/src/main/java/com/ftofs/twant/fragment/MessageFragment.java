package com.ftofs.twant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ChatConversationAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.ChatConversation;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenuMessage;
import com.ftofs.twant.widget.ScaledButton;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 消息
 * @author zwm
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 是否獨立的Fragment，還是依附于MainFragment
     */
    boolean isStandalone;
    ScaledButton btnBack;
    ChatConversationAdapter adapter;

    List<ChatConversation> chatConversationList = new ArrayList<>();

    public static MessageFragment newInstance(boolean isStandalone) {
        Bundle args = new Bundle();

        args.putBoolean("isStandalone", isStandalone);
        MessageFragment fragment = new MessageFragment();
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

        Util.setOnClickListener(view, R.id.btn_message_menu, this);
        Util.setOnClickListener(view, R.id.btn_view_logistics_message, this);
        Util.setOnClickListener(view, R.id.btn_view_refund_message, this);

        btnBack = view.findViewById(R.id.btn_back);

        if (isStandalone) {
            btnBack.setOnClickListener(this);
            btnBack.setVisibility(View.VISIBLE);
        }

        RecyclerView rvChatConversationList = view.findViewById(R.id.rv_chat_conversation_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvChatConversationList.setLayoutManager(layoutManager);
        adapter = new ChatConversationAdapter(chatConversationList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ChatConversation chatConversation = chatConversationList.get(position);
                int itemType = chatConversation.itemType;

                if (itemType == ChatConversation.ITEM_TYPE_LOGISTICS) {
                    Util.startFragment(LogisticsMessageListFragment.newInstance(Constant.MESSAGE_CATEGORY_LOGISTICS));
                } else if (itemType == ChatConversation.ITEM_TYPE_RETURN) {
                    Util.startFragment(LogisticsMessageListFragment.newInstance(Constant.MESSAGE_CATEGORY_REFUND));
                } else {
                    EMConversation conversation = ChatUtil.getConversation(chatConversation.friendInfo.memberName,
                            chatConversation.friendInfo.nickname, chatConversation.friendInfo.avatarUrl, ChatUtil.ROLE_MEMBER);
                    Util.startFragment(ChatFragment.newInstance(conversation));
                }
            }
        });
        rvChatConversationList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_NEW_CHAT_MESSAGE) {
            loadData();
        }
    }

    private void loadData() {
        chatConversationList.clear();

        // 添加【交易物流消息】、【退換貨消息】
        chatConversationList.add(new ChatConversation(ChatConversation.ITEM_TYPE_LOGISTICS));
        chatConversationList.add(new ChatConversation(ChatConversation.ITEM_TYPE_RETURN));

        // 獲取環信所有會話列表
        Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
        SLog.info("會話數[%d]", conversationMap.size());
        for (Map.Entry<String, EMConversation> entry : conversationMap.entrySet()) {
            String memberName = entry.getKey();
            EMConversation conversation = entry.getValue();

            String extField = conversation.getExtField();
            SLog.info("extField[%s]", extField);

            EMMessage lastMessage = conversation.getLastMessage();
            long timestamp = lastMessage.getMsgTime();
            SLog.info("memberName[%s], lastMessage[%s], timestamp[%s]",
                    memberName, lastMessage.getBody().toString(), Time.fromMillisUnixtime(timestamp, "Y-m-d H:i:s"));
            ChatConversation chatConversation = new ChatConversation(ChatConversation.ITEM_TYPE_IM);

            FriendInfo friendInfo = FriendInfo.getFriendInfoByMemberName(memberName);
            if (friendInfo != null) {
                chatConversation.friendInfo = friendInfo;
            }
            chatConversation.unreadCount = conversation.getUnreadMsgCount();
            chatConversation.lastMessageType = ChatUtil.getIntMessageType(lastMessage);
            chatConversation.lastMessage = lastMessage.getBody().toString();
            chatConversation.timestamp = lastMessage.getMsgTime();

            chatConversationList.add(chatConversation);
        }

        adapter.setNewData(chatConversationList);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_message_menu) {
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 6))
                    .offsetY(-Util.dip2px(_mActivity, 8))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenuMessage(_mActivity, this))
                    .show();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (isStandalone) {
            pop();
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
        loadData();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
