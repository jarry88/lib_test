package com.ftofs.twant.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ChatConversation;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.ChatUtil;
import com.gzp.lib_common.base.Jarbon;
import com.ftofs.twant.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 會話列表Adapter
 *
 * @author zwm
 */
public class ChatConversationAdapter extends BaseQuickAdapter<ChatConversation, BaseViewHolder> {
    Jarbon now;
    private DiffUtil.ItemCallback<ChatConversation> callback = new DiffUtil.ItemCallback<ChatConversation>() {
        @Override
        public boolean areItemsTheSame(@NonNull ChatConversation oldItem, @NonNull ChatConversation newItem) {
            boolean b=TextUtils.equals(oldItem.friendInfo.memberName, newItem.friendInfo.memberName);
            return b;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatConversation oldItem, @NonNull ChatConversation newItem) {
//            SLog.info("name%s,%s,",newItem.friendInfo.memberName,newItem.lastMessage);
//TextUtils.equals(oldItem.lastMessage,newItem.lastMessage)&&
            return TextUtils.equals(oldItem.messageTime,newItem.messageTime);
        }
    };
    private final AsyncListDiffer<ChatConversation> chatConversationAsyncListDiffer=new AsyncListDiffer<ChatConversation>(this,callback);

    @NonNull
    @Override
    public List<ChatConversation> getData() {
        return chatConversationAsyncListDiffer.getCurrentList();
    }

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
    public int getItemCount() {
        if (chatConversationAsyncListDiffer == null) {
            return 0;
        }
        return chatConversationAsyncListDiffer.getCurrentList().size();
    }
    public void submitList(List<ChatConversation> data) {
        List<ChatConversation> newList= new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (ChatConversation chatConversation : data) {
                SLog.info("----------------->%s, %s ", chatConversation.friendInfo.nickname, chatConversation.messageTime);
                ChatConversation chatConversation1 = chatConversation.clone();
                chatConversation1.messageTime = chatConversation.messageTime;
                newList.add(chatConversation1);
            }
        }
//        newList.addAll(data);
        chatConversationAsyncListDiffer.submitList(newList);
    }

    public ChatConversation getItem(int position) {
        return chatConversationAsyncListDiffer.getCurrentList().get(position);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder helper, int position) {
        ChatConversation chatConversation = getItem(position);
        SLog.info("顯示 name [%s],data[%s]",chatConversation.friendInfo.memberName,chatConversation.messageTime);
        TextView tvTime = helper.getView(R.id.tv_message_time);
        if (tvTime != null) {
            if (!StringUtil.isEmpty(tvTime.getText().toString()) && StringUtil.isEmpty(chatConversation.messageTime)) {
                SLog.info("如果有旧值并且新值为空，不覆盖]");
            } else {
                tvTime.setText(chatConversation.messageTime);
            }
        }
        ImageView imgAvatar = helper.getView(R.id.img_avatar);
        //此处已经按身份区分并返回头像
        String avatarUrl = chatConversation.friendInfo.getRoleAvatar();

        helper.setGone(R.id.img_role_logo, chatConversation.friendInfo.role!=0);
        if (StringUtil.isEmpty(avatarUrl)) {
            if (chatConversation.friendInfo.role == ChatUtil.ROLE_CS_PLATFORM) {
                Glide.with(mContext).load(R.drawable.icon_twant_loge).centerCrop().into(imgAvatar);
            } else {
                Glide.with(mContext).load(R.drawable.icon_default_avatar).centerCrop().into(imgAvatar);
            }
        } else {
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAvatar);
        }

        if (chatConversation.friendInfo.role == ChatUtil.ROLE_CS_PLATFORM && !StringUtil.isEmpty(chatConversation.friendInfo.groupName)) {
            helper.setGone(R.id.tv_group_name, true);
            helper.setText(R.id.tv_group_name, chatConversation.friendInfo.groupName);
        }
        helper.setText(R.id.tv_nickname, chatConversation.friendInfo.getName());
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
//        int position = helper.getAdapterPosition();
        if (position == itemCount - 1) {
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

    @Override
    protected void convert(BaseViewHolder helper, ChatConversation chatConversation) {
        SLog.info("name 【%s],时间【%s】 convert",chatConversation.friendInfo.nickname,chatConversation.messageTime);
    }


}
