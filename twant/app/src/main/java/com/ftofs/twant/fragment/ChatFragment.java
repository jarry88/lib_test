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
import android.text.TextWatcher;
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
import com.ftofs.twant.entity.ChatMessage;
import com.ftofs.twant.entity.EmojiPage;
import com.ftofs.twant.entity.FriendItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Emoji;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.QMUIAlignMiddleImageSpan;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import am.widget.smoothinputlayout.SmoothInputLayout;

/**
 * 聊天會話Fragment
 * @author zwm
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener,
        View.OnTouchListener, TextWatcher, SmoothInputLayout.OnVisibilityChangeListener {

    /**
     * 定義發送按鈕的動作，是顯示工具菜單還是發送消息
     */
    private static final int ACTION_SHOW_MENU = 1;
    private static final int ACTION_SEND_MESSAGE = 2;

    int action = ACTION_SHOW_MENU;
    SmoothInputLayout silMainContainer;
    EditText etMessage;
    View btnEmoji;
    ImageView iconEmoji;
    View btnTool;
    ImageView iconCircleAdd;
    View llEmojiPane;
    View llToolPane;

    FriendItem friendItem;
    RecyclerView rvEmojiPageList;
    EmojiPageAdapter emojiPageAdapter;
    List<EmojiPage> emojiPageList = new ArrayList<>();

    RecyclerView rvMessageList;
    ChatMessageAdapter chatMessageAdapter;
    List<ChatMessage> chatMessageList = new ArrayList<>();

    public static ChatFragment newInstance(FriendItem friendItem) {
        Bundle args = new Bundle();

        args.putParcelable("friendItem", friendItem);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);

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

        Bundle args = getArguments();
        friendItem = args.getParcelable("friendItem");

        TextView tvNickname = view.findViewById(R.id.tv_nickname);
        tvNickname.setText(friendItem.nickname);

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

        Util.setOnClickListener(view, R.id.btn_back, this);

        initEmojiPage(view);
        loadEmojiData();

        initChatUI(view);
        loadChatData();
    }

    private void loadChatData() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(friendItem.memberName);
        //获取此会话的所有消息
        String startMsgId = "";
        List<EMMessage> messages = conversation.getAllMessages();
        SLog.info("消息條數[%d]", messages.size());
        for (EMMessage emMessage : messages) {
            startMsgId = emMessage.getMsgId();
            SLog.info("message[%s]", emMessage.toString());

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.content = emMessage.getBody().toString();
            chatMessageList.add(chatMessage);
        }

        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        int pagesize = 20;
        messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        for (EMMessage emMessage : messages) {
            SLog.info("message[%s]", emMessage.toString());

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.content = emMessage.getBody().toString();
            chatMessageList.add(chatMessage);
        }

        chatMessageAdapter.setNewData(chatMessageList);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_back:
                pop();
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

                    sendMessage(message);
                    return;
                }
                if (btnTool.isSelected()) {
                    btnTool.setSelected(false);
                    showInput();
                } else {
                    btnTool.setSelected(true);
                    showTool();
                }
                break;
            default:
                break;
        }
    }

    private void initChatUI(View contentView) {
        rvMessageList = contentView.findViewById(R.id.rv_message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvMessageList.setLayoutManager(layoutManager);

        chatMessageAdapter = new ChatMessageAdapter(R.layout.chat_message_item, chatMessageList);
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

    private void sendMessage(String content) {
        SLog.info("content[%s]", content);
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, friendItem.memberName);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack(){
            @Override
            public void onSuccess() {
                SLog.info("onSuccess, body[%s]", message.getBody());
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
}
