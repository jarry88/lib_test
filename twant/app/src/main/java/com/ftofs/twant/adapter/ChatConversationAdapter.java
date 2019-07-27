package com.ftofs.twant.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.ChatConversation;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 會話列表Adapter
 * @author zwm
 */
public class ChatConversationAdapter extends BaseMultiItemQuickAdapter<ChatConversation, BaseViewHolder> {
    Jarbon now;

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

        now = new Jarbon();
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatConversation chatConversation) {
        int itemViewType = helper.getItemViewType();
        if (itemViewType == ChatConversation.ITEM_TYPE_IM) {
            if (chatConversation.friendInfo.avatarImg != null) {
                ImageView imgAvatar = helper.getView(R.id.img_avatar);
                Glide.with(mContext).load(chatConversation.friendInfo.avatarImg).centerCrop().into(imgAvatar);
            }

            helper.setText(R.id.tv_nickname, chatConversation.friendInfo.nickname);
            TextView tvLastMessage = helper.getView(R.id.tv_last_message);
            tvLastMessage.setText(StringUtil.getMessageText(mContext, chatConversation.lastMessage, (int) tvLastMessage.getTextSize()));

            helper.setText(R.id.tv_message_time, formatMessageTime(chatConversation.timestamp));
            if (chatConversation.isDoNotDisturb) {
                helper.setGone(R.id.icon_do_not_disturb, true);
                helper.setGone(R.id.tv_unread_count, false);
            } else {
                helper.setGone(R.id.icon_do_not_disturb, false);
                if (chatConversation.unreadCount > 0) {
                    helper.setGone(R.id.tv_unread_count, true);
                    helper.setText(R.id.tv_unread_count, String.valueOf(chatConversation.unreadCount));
                } else {
                    helper.setGone(R.id.tv_unread_count, false);
                }
            }
        }
    }

    private String formatMessageTime(long timestampMillis) {
        Jarbon jarbon = new Jarbon(timestampMillis);

        int diffInDays = jarbon.diffInDays(now);
        SLog.info("diffInDays[%d]", diffInDays);
        if (diffInDays < 1) {
            // 今天的顯示幾時幾分
            return jarbon.format("H:i");
        } else if (diffInDays == 1) {
            return "昨天";
        } else if (diffInDays == 2) {
            return "前天";
        } else {
            // 其它的顯示幾月幾日
            return jarbon.format("n月j日");
        }
    }
}
