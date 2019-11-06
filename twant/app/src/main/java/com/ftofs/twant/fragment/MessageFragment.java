package com.ftofs.twant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ChatConversationAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.ChatConversation;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.UnreadCount;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.SqliteUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.MaxHeightRecyclerView;
import com.ftofs.twant.widget.ScaledButton;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
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
    ScaledButton btnBack;
    ScaledButton btnContact;
    ChatConversationAdapter adapter;

    LinearLayout llMessageListContainer;

    int totalUnreadCount;  // 未讀消息總數
    UnreadCount unreadCount;


    TextView tvTransactMessageItemCount;
    TextView tvAssetMessageItemCount;
    TextView tvSocialMessageItemCount;
    TextView tvBargainMessageItemCount;
    TextView tvNoticeMessageItemCount;



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

        Util.setOnClickListener(view, R.id.btn_message_search, this);
        Util.setOnClickListener(view, R.id.btn_message_menu, this);
        Util.setOnClickListener(view, R.id.btn_view_logistics_message, this);
        Util.setOnClickListener(view, R.id.btn_view_refund_message, this);

        Util.setOnClickListener(view, R.id.btn_transact_message, this);
        Util.setOnClickListener(view, R.id.btn_asset_message, this);
        Util.setOnClickListener(view, R.id.btn_social_message, this);
        Util.setOnClickListener(view, R.id.btn_bargain_message, this);
        Util.setOnClickListener(view, R.id.btn_notice_message, this);

        btnBack = view.findViewById(R.id.btn_back);
        btnContact = view.findViewById(R.id.btn_contact);

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
        llMessageListContainer.post(new Runnable() {
            @Override
            public void run() {
                int height = llMessageListContainer.getHeight();
                rvChatConversationList.setMaxHeight(height);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvChatConversationList.setLayoutManager(layoutManager);
        adapter = new ChatConversationAdapter(R.layout.chat_conversation_im, chatConversationList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ChatConversation chatConversation = chatConversationList.get(position);

                SLog.info("friendInfo[%s]", chatConversation.friendInfo);
                EMConversation conversation = ChatUtil.getConversation(chatConversation.friendInfo.memberName,
                        chatConversation.friendInfo.nickname, chatConversation.friendInfo.avatarUrl, ChatUtil.ROLE_MEMBER);

                if (chatConversation.unreadCount > 0) {
                    // 從未讀總數中減去這條會話的未讀數
                    totalUnreadCount -= chatConversation.unreadCount;
                    displayUnreadCount(totalUnreadCount);
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
            loadData();
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
            totalUnreadCount = 0;
            chatConversationList.clear();

            // 獲取環信所有會話列表
            Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
            SLog.info("會話數[%d]", conversationMap.size());
            for (Map.Entry<String, EMConversation> entry : conversationMap.entrySet()) {
                String memberName = entry.getKey();
                EMConversation conversation = entry.getValue();

                EMMessage lastMessage = conversation.getLastMessage();
                if (lastMessage == null) {
                    continue;
                }
                long timestamp = lastMessage.getMsgTime();
                FriendInfo friendInfo = new FriendInfo();

                friendInfo.memberName = memberName;
                String extField = conversation.getExtField();
                SLog.info("extField[%s]", extField);
                if (EasyJSONBase.isJSONString(extField)) {
                    EasyJSONObject extFieldObj = (EasyJSONObject) EasyJSONObject.parse(extField);

                    friendInfo.nickname = extFieldObj.getString("nickName");
                    friendInfo.avatarUrl = extFieldObj.getString("avatarUrl");
                    friendInfo.role = extFieldObj.getInt("role");
                }


                SLog.info("memberName[%s], lastMessage[%s], timestamp[%s], nickname[%s], avatar[%s]",
                        memberName, lastMessage.getBody().toString(), Time.fromMillisUnixtime(timestamp, "Y-m-d H:i:s"),
                        friendInfo.nickname, friendInfo.avatarUrl);

                SLog.info("friendInfo[%s]", friendInfo);

                ChatConversation chatConversation = new ChatConversation();

                chatConversation.friendInfo = friendInfo;

                chatConversation.unreadCount = conversation.getUnreadMsgCount();
                chatConversation.lastMessageType = ChatUtil.getIntMessageType(lastMessage);
                chatConversation.lastMessage = lastMessage.getBody().toString();
                chatConversation.timestamp = lastMessage.getMsgTime();
                totalUnreadCount += chatConversation.unreadCount;

                chatConversationList.add(chatConversation);
            }
            displayUnreadCount(totalUnreadCount);

            adapter.setNewData(chatConversationList);
        } catch (Exception e) {
            SLog.info("Error!%s", e.getMessage());
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

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }


                    UnreadCount unreadCount = UnreadCount.processUnreadList(responseObj.getArray("datas.unreadList"));
                    if (unreadCount != null) {
                        UnreadCount.save(unreadCount);
                        refreshUnreadCount();
                    }

                } catch (Exception e) {

                }
            }
        });
    }

    private void displayUnreadCount(int totalUnreadCount) {
        MainFragment mainFragment = MainFragment.getInstance();
        if (mainFragment != null) {
            mainFragment.setMessageItemCount(totalUnreadCount);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
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

        // 每次顯示時，登錄一下環信
        SqliteUtil.imLogin();

        loadData();

        refreshUnreadCount();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    private void refreshUnreadCount() {
        unreadCount = UnreadCount.get();
        if (unreadCount == null) {
            unreadCount = new UnreadCount();
        }

        if (unreadCount.transact > 0) {
            tvTransactMessageItemCount.setText(String.valueOf(unreadCount.transact));
            tvTransactMessageItemCount.setVisibility(View.VISIBLE);
        } else {
            tvTransactMessageItemCount.setVisibility(View.GONE);
        }

        if (unreadCount.asset > 0) {
            tvAssetMessageItemCount.setText(String.valueOf(unreadCount.asset));
            tvAssetMessageItemCount.setVisibility(View.VISIBLE);
        } else {
            tvAssetMessageItemCount.setVisibility(View.GONE);
        }

        if (unreadCount.social > 0) {
            tvSocialMessageItemCount.setText(String.valueOf(unreadCount.social));
            tvSocialMessageItemCount.setVisibility(View.VISIBLE);
        } else {
            tvSocialMessageItemCount.setVisibility(View.GONE);
        }

        if (unreadCount.bargain > 0) {
            tvBargainMessageItemCount.setText(String.valueOf(unreadCount.bargain));
            tvBargainMessageItemCount.setVisibility(View.VISIBLE);
        } else {
            tvBargainMessageItemCount.setVisibility(View.GONE);
        }

        if (unreadCount.notice > 0) {
            tvNoticeMessageItemCount.setText(String.valueOf(unreadCount.notice));
            tvNoticeMessageItemCount.setVisibility(View.VISIBLE);
        } else {
            tvNoticeMessageItemCount.setVisibility(View.GONE);
        }
    }
}
