package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.ChatMessage;

import java.util.List;

public class ChatMessageAdapter extends BaseQuickAdapter<ChatMessage, BaseViewHolder> {
    public ChatMessageAdapter(int layoutResId, @Nullable List<ChatMessage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatMessage item) {
        if (item.origin == ChatMessage.MY_MESSAGE) { // 是我的消息
            helper.setGone(R.id.rl_your_message_container, false);
            helper.setText(R.id.tv_my_message, getMessageText(item.content));
        } else { // 是別人的消息
            helper.setGone(R.id.rl_my_message_container, false);
            helper.setText(R.id.tv_your_message, getMessageText(item.content));
        }
    }

    private String getMessageText(String message) {
        // txt:"abc" 返回 abc
        return message.substring(5, message.length() - 1);
    }
}
