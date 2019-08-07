package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.ChatMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.File;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

public class ChatMessageAdapter extends BaseQuickAdapter<ChatMessage, BaseViewHolder> {
    String myAvatarUrl;
    byte[] yourAvatarData;  // 讀圖片文件讀出來的數據
    public ChatMessageAdapter(int layoutResId, @Nullable List<ChatMessage> data, byte[] yourAvatarData) {
        super(layoutResId, data);

        myAvatarUrl = StringUtil.normalizeImageUrl(User.getUserInfo(SPField.FIELD_AVATAR, ""));
        this.yourAvatarData = yourAvatarData;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatMessage item) {
        helper.addOnClickListener(R.id.img_my_avatar, R.id.img_your_avatar, R.id.img_message, R.id.ll_goods_message_container);
        helper.addOnLongClickListener(R.id.tv_message, R.id.img_message);

        TextView tvMessageTime = helper.getView(R.id.tv_message_time);


        SLog.info("item.showTimestamp[%s]", item.showTimestamp);
        if (item.showTimestamp) {
            String timestamp = Time.fromMillisUnixtime(item.timestamp, "Y-m-d H:i:s");
            tvMessageTime.setText(timestamp);
            tvMessageTime.setVisibility(View.VISIBLE);
        } else {
            tvMessageTime.setVisibility(View.GONE);
        }

        TextView textView = helper.getView(R.id.tv_message);

        LinearLayout llImageMessageContainer = helper.getView(R.id.ll_image_message_container);
        LinearLayout llGoodsMessageContainer = helper.getView(R.id.ll_goods_message_container);

        if (item.origin == ChatMessage.MY_MESSAGE) { // 是我的消息
            // 設置頭像
            ImageView imgMyAvatar = helper.getView(R.id.img_my_avatar);
            Glide.with(mContext).load(myAvatarUrl).centerCrop().into(imgMyAvatar);
            helper.setGone(R.id.img_your_avatar, false);
            helper.setGone(R.id.img_my_avatar, true);
            textView.setBackgroundResource(R.drawable.my_message_bg);
            llImageMessageContainer.setGravity(Gravity.RIGHT);
        } else { // 是別人的消息
            // 設置頭像
            ImageView imgYourAvatar = helper.getView(R.id.img_your_avatar);
            if (yourAvatarData != null) {
                Glide.with(mContext).load(yourAvatarData).centerCrop().into(imgYourAvatar);
            }

            helper.setGone(R.id.img_my_avatar, false);
            helper.setGone(R.id.img_your_avatar, true);
            textView.setBackgroundResource(R.drawable.your_message_bg);
            llImageMessageContainer.setGravity(Gravity.LEFT);
        }

        SLog.info("messageType[%s], content[%s]", item.messageType, item.content);
        if (item.messageType == Constant.CHAT_MESSAGE_TYPE_TXT) {
            textView.setVisibility(View.VISIBLE);
            llImageMessageContainer.setVisibility(View.GONE);
            llGoodsMessageContainer.setVisibility(View.GONE);

            textView.setText(StringUtil.translateEmoji(mContext, item.content, (int) textView.getTextSize()));
        } else if (item.messageType == Constant.CHAT_MESSAGE_TYPE_IMAGE) {
            textView.setVisibility(View.GONE);
            llImageMessageContainer.setVisibility(View.VISIBLE);
            llGoodsMessageContainer.setVisibility(View.GONE);

            ImageView imageView = helper.getView(R.id.img_message);

            EasyJSONObject data = (EasyJSONObject) EasyJSONObject.parse(item.content);

            String absolutePath = null;
            String imgUrl = null;
            try {
                absolutePath = data.getString("absolutePath");
                imgUrl = data.getString("imgUrl");
            } catch (Exception e) {
            }

            boolean imgLoaded = false;
            if (!StringUtil.isEmpty(absolutePath)) {  // 優先加載本地的圖片
                File file = new File(absolutePath);
                if (file.exists()) {
                    Glide.with(mContext).load(file).centerCrop().into(imageView);
                    imgLoaded = true;
                }
            }

            if (!imgLoaded && !StringUtil.isEmpty(imgUrl)) {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(imgUrl)).centerCrop().into(imageView);
                imgLoaded = true;
            }
        } else if (item.messageType == Constant.CHAT_MESSAGE_TYPE_GOODS) {
            textView.setVisibility(View.GONE);
            llImageMessageContainer.setVisibility(View.GONE);
            llGoodsMessageContainer.setVisibility(View.VISIBLE);
            SLog.info("content[%s]", item.content);
            EasyJSONObject data = (EasyJSONObject) EasyJSONObject.parse(item.content);

            String imageUrl = null;
            String goodsName = null;
            try {
                imageUrl = data.getString("goodsImage");
                goodsName = data.getString("goodsName");
            } catch (Exception e) {
            }

            ImageView goodsImage = helper.getView(R.id.goods_image);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(imageUrl)).centerCrop().into(goodsImage);

            helper.setText(R.id.tv_goods_name, goodsName);
        }

        // 設置了background后，會重置Padding
        textView.setPadding(Util.dip2px(mContext, 15), Util.dip2px(mContext, 12),
                Util.dip2px(mContext, 15), Util.dip2px(mContext, 12));


        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        } else {
            layoutParams.bottomMargin = 0;
        }
    }
}
