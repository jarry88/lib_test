package com.ftofs.twant.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.ChatConversation;

import java.util.List;

public class ChatConversationAdapter extends BaseMultiItemQuickAdapter<ChatConversation, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatConversationAdapter(List<ChatConversation> data) {
        super(data);

        addItemType(ChatConversation.ITEM_TYPE_LOGISTICS, R.layout.chat_conversation_logistics);
        addItemType(ChatConversation.ITEM_TYPE_RETURN, R.layout.chat_conversation_return);
        addItemType(ChatConversation.ITEM_TYPE_IM, R.layout.chat_conversation_im);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatConversation item) {

    }
}
