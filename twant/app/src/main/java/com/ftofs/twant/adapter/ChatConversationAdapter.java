package com.ftofs.twant.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ChatConversation;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.List;

/**
 * 會話列表Adapter
 *
 * @author zwm
 */
public class ChatConversationAdapter extends BaseQuickAdapter<ChatConversation, BaseViewHolder> {
    Jarbon now;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatConversationAdapter(int layoutResId, @Nullable List<ChatConversation> data) {
        super(layoutResId, data);

        now = new Jarbon();
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatConversation chatConversation) {
        LinearLayout linearLayout = helper.getView(R.id.ll_message_item_container);
        if (chatConversation != null) {
            linearLayout.getLayoutParams().height = Util.dip2px(mContext,80);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.getLayoutParams().height = Util.dip2px(mContext, 30);
            linearLayout.setVisibility(View.INVISIBLE);
            return;
        }
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        String avatarUrl = chatConversation.friendInfo.avatarUrl;
        if (StringUtil.isEmpty(avatarUrl)) {
            if (chatConversation.friendInfo.role == ChatUtil.ROLE_CS_PLATFORM) {
                Glide.with(mContext).load(R.drawable.icon_twant_loge).centerCrop().into(imgAvatar);
            } else {
                Glide.with(mContext).load(R.drawable.icon_default_avatar).centerCrop().into(imgAvatar);
            }
        } else {
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(chatConversation.friendInfo.avatarUrl)).centerCrop().into(imgAvatar);
        }

        if (chatConversation.friendInfo.role == ChatUtil.ROLE_CS_PLATFORM && !StringUtil.isEmpty(chatConversation.friendInfo.groupName)) {
            helper.setGone(R.id.tv_group_name, true);
            helper.setText(R.id.tv_group_name, chatConversation.friendInfo.groupName);
        }
        helper.setText(R.id.tv_nickname, chatConversation.friendInfo.nickname);
        TextView tvLastMessage = helper.getView(R.id.tv_last_message);
        if (chatConversation.lastMessageType == Constant.CHAT_MESSAGE_TYPE_IMAGE) {
            tvLastMessage.setText(ChatConversation.LAST_MESSAGE_DESC_IMAGE);
        } else if (chatConversation.lastMessageType == Constant.CHAT_MESSAGE_TYPE_GOODS) {
            tvLastMessage.setText(ChatConversation.LAST_MESSAGE_DESC_GOODS);
        } else if (chatConversation.lastMessageType == Constant.CHAT_MESSAGE_TYPE_ORDER) {
            tvLastMessage.setText(ChatConversation.LAST_MESSAGE_DESC_ORDERS);
        } else if (chatConversation.lastMessageType == Constant.CHAT_MESSAGE_TYPE_TXT) {
            tvLastMessage.setText(StringUtil.getSpannableMessageText(mContext, chatConversation.lastMessage, (int) tvLastMessage.getTextSize()));
        } else if (chatConversation.lastMessageType == Constant.CHAT_MESSAGE_TYPE_ENC) {
            tvLastMessage.setText(ChatConversation.LAST_MESSAGE_DESC_ENC);
        } else {
            tvLastMessage.setText("");
        }


        helper.setText(R.id.tv_message_time, Jarbon.formatMessageTime(chatConversation.timestamp));
        if (chatConversation.isDoNotDisturb) {
            helper.setGone(R.id.icon_do_not_disturb, true);
            helper.setGone(R.id.tv_unread_count, false);
        } else {
            helper.setGone(R.id.icon_do_not_disturb, false);
            if (chatConversation.unreadCount > 0) {
                helper.setGone(R.id.tv_unread_count, true);
                TextView count = helper.getView(R.id.tv_unread_count);
                if (chatConversation.unreadCount == 1) {
                    count.setTextSize(6);
                    count.setText("");
                } else if (chatConversation.unreadCount <= 999) {
                    count.setTextSize(10);
                    helper.setText(R.id.tv_unread_count, String.valueOf(chatConversation.unreadCount));
                } else {
                    count.setTextSize(8);
                    helper.setText(R.id.tv_unread_count, "⋯");
                }
            } else {
                helper.setGone(R.id.tv_unread_count, false);
            }
        }

        LinearLayout llMessageItemContainer = helper.getView(R.id.ll_message_item_container);
        llMessageItemContainer.setVisibility(View.VISIBLE);
        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        if (position == itemCount - 2) {
            llMessageItemContainer.setBackground(null);
        }
        else if(chatConversation.isPlatformCustomer){
            SLog.info("客服類型item");
            if (position == 0) {

                helper.getView(R.id.ll_message_container_background).setBackgroundColor(mContext.getColor(R.color.tw_light_grey_EDED));
            } else {
                llMessageItemContainer.setVisibility(View.INVISIBLE);
            }
        }
        else {
            helper.getView(R.id.ll_message_container_background).setBackgroundColor(mContext.getColor(R.color.tw_white));
            llMessageItemContainer.setBackgroundResource(R.drawable.border_type_d);
        }
    }

}
