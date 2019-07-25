package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.ChatMessage;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.User;

import java.util.List;

public class ChatMessageAdapter extends BaseQuickAdapter<ChatMessage, BaseViewHolder> {
    String myAvatarUrl;

    public ChatMessageAdapter(int layoutResId, @Nullable List<ChatMessage> data) {
        super(layoutResId, data);

        myAvatarUrl = StringUtil.normalizeImageUrl(User.getUserInfo(SPField.FIELD_AVATAR, ""));
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatMessage item) {
        helper.addOnClickListener(R.id.img_my_avatar, R.id.img_your_avatar);
        helper.addOnLongClickListener(R.id.tv_message);

        String timestamp = Time.fromMillisUnixtime(item.timestamp, "Y-m-d H:i:s");
        helper.setText(R.id.tv_message_time, timestamp);

        TextView textView = helper.getView(R.id.tv_message);
        textView.setText(getMessageText(item.content, (int) textView.getTextSize()));

        if (item.origin == ChatMessage.MY_MESSAGE) { // 是我的消息
            // 設置頭像
            ImageView imgMyAvatar = helper.getView(R.id.img_my_avatar);
            Glide.with(mContext).load(myAvatarUrl).centerCrop().into(imgMyAvatar);
            helper.setGone(R.id.img_your_avatar, false);
            helper.setGone(R.id.img_my_avatar, true);
        } else { // 是別人的消息

            helper.setGone(R.id.img_my_avatar, false);
            helper.setGone(R.id.img_your_avatar, true);
        }


        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        }
    }

    private Editable getMessageText(String message, int textSize) {
        // txt:"abc" 返回 abc
        message = message.substring(5, message.length() - 1);
        return StringUtil.translateEmoji(mContext, message, textSize);
    }
}
