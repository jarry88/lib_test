package com.ftofs.twant.adapter;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.ChatMessage;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.File;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

public class ChatMessageAdapter extends BaseQuickAdapter<ChatMessage, BaseViewHolder> {
    public void setYourAvatarUrl(String yourAvatarUrl) {
        this.yourAvatarUrl = yourAvatarUrl;
    }

    String yourAvatarUrl;  // 讀圖片文件讀出來的數據
    public ChatMessageAdapter(int layoutResId, @Nullable List<ChatMessage> data, String yourAvatarUrl) {
        super(layoutResId, data);

        this.yourAvatarUrl = yourAvatarUrl;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatMessage item) {
        helper.addOnClickListener(R.id.img_my_avatar, R.id.img_your_avatar, R.id.img_message,R.id.btn_send,
                R.id.ll_goods_message_container, R.id.ll_order_message_container, R.id.ll_enc_message_container);
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

        LinearLayout llTextLayout = helper.getView(R.id.ll_txt_container);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTextLayout.getLayoutParams();
        LinearLayout llImageMessageContainer = helper.getView(R.id.ll_image_message_container);
        LinearLayout llGoodsMessageContainer = helper.getView(R.id.ll_goods_message_container);
        LinearLayout llOrderMessageContainer = helper.getView(R.id.ll_order_message_container);
        LinearLayout llEncMessageContainer = helper.getView(R.id.ll_enc_message_container);

//        SLog.info("item.origin[%d]", item.origin);
        if (item.origin == ChatMessage.MY_MESSAGE) { // 是我的消息
            // 設置頭像
            ImageView imgMyAvatar = helper.getView(R.id.img_my_avatar);
            String myAvatarUrl = StringUtil.normalizeImageUrl(User.getUserInfo(SPField.FIELD_AVATAR, ""));
            Glide.with(mContext).load(myAvatarUrl).centerCrop().into(imgMyAvatar);
            helper.setGone(R.id.img_your_avatar, false);
            helper.setGone(R.id.img_my_avatar, true);
            textView.setBackgroundResource(R.drawable.my_message_bg);
//            params.setMarginEnd(-10);
            params.gravity = Gravity.END;
            llTextLayout.setLayoutParams(params);
            llImageMessageContainer.setGravity(Gravity.RIGHT);
        } else { // 是別人的消息
            // 設置頭像
            ImageView imgYourAvatar = helper.getView(R.id.img_your_avatar);
            if (!StringUtil.isEmpty(yourAvatarUrl)) {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(yourAvatarUrl)).centerCrop().into(imgYourAvatar);
            }
            helper.setGone(R.id.img_my_avatar, false);
            helper.setGone(R.id.img_your_avatar, true);
            textView.setBackgroundResource(R.drawable.your_message_bg);
            llImageMessageContainer.setGravity(Gravity.LEFT);
//            params.setMarginStart(-10);
            params.gravity = Gravity.START;
            llTextLayout.setLayoutParams(params);
        }

        SLog.info("messageType[%s], content[%s]", item.messageType, item.content);
        if (item.messageType == Constant.CHAT_MESSAGE_TYPE_TXT) {
            textView.setVisibility(View.VISIBLE);
            llImageMessageContainer.setVisibility(View.GONE);
            llGoodsMessageContainer.setVisibility(View.GONE);
            llOrderMessageContainer.setVisibility(View.GONE);
            llEncMessageContainer.setVisibility(View.GONE);

            textView.setText(StringUtil.translateEmoji(mContext, item.content, (int) textView.getTextSize()));
        } else if (item.messageType == Constant.CHAT_MESSAGE_TYPE_IMAGE) {
            textView.setVisibility(View.GONE);
            llImageMessageContainer.setVisibility(View.VISIBLE);
            llGoodsMessageContainer.setVisibility(View.GONE);
            llOrderMessageContainer.setVisibility(View.GONE);
            llEncMessageContainer.setVisibility(View.GONE);

            ImageView imageView = helper.getView(R.id.img_message);

            EasyJSONObject data = EasyJSONObject.parse(item.content);

            String absolutePath = null;
            String imgUrl = null;
            try {
                absolutePath = data.getSafeString("absolutePath");
                imgUrl = data.getSafeString("imgUrl");
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
            }
        } else if (item.messageType == Constant.CHAT_MESSAGE_TYPE_GOODS) {
            textView.setVisibility(View.GONE);
            llImageMessageContainer.setVisibility(View.GONE);
            llGoodsMessageContainer.setVisibility(View.VISIBLE);
            llOrderMessageContainer.setVisibility(View.GONE);
            llEncMessageContainer.setVisibility(View.GONE);

            EasyJSONObject data = EasyJSONObject.parse(item.content);


            String imageUrl = null;
            String goodsName = null;
            String btnStr = "showSendBtn";
            boolean btnSendShow = false;
            try {
                imageUrl = data.getSafeString("goodsImage");
                goodsName = data.getSafeString("goodsName");
                if (data.exists(btnStr)) {
                    btnSendShow = data.getBoolean(btnStr);
                }
            } catch (Exception e) {
                SLog.info("Error! %s",e.getMessage());
            }
            TextView btnSend = helper.getView(R.id.btn_send);
            if (btnSendShow) {
                btnSend.setVisibility(View.VISIBLE);
            } else {
                btnSend.setVisibility(View.GONE);
            }
            ImageView goodsImage = helper.getView(R.id.goods_image);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(imageUrl)).centerCrop().into(goodsImage);

            helper.setText(R.id.tv_goods_name, goodsName);
        } else if (item.messageType == Constant.CHAT_MESSAGE_TYPE_ORDER) {
            textView.setVisibility(View.GONE);
            llImageMessageContainer.setVisibility(View.GONE);
            llGoodsMessageContainer.setVisibility(View.GONE);
            llOrderMessageContainer.setVisibility(View.VISIBLE);
            llEncMessageContainer.setVisibility(View.GONE);

            /*
            {"ordersId":3079,"ordersSn":"4530000000312700","goodsImage":"image\/0d\/14\/0d147b92feaa84dc658c7dd99d0897f0.jpg","goodsName":"test"}
             */
            EasyJSONObject data = EasyJSONObject.parse(item.content);
            String imageUrl = null;
            String ordersSn = null;
            String goodsName = null;
            try {
                imageUrl = data.getSafeString("goodsImage");
                ordersSn = data.getSafeString("ordersSn");
                goodsName = data.getSafeString("goodsName");
            } catch (Exception e) {
            }

            SLog.info("imageUrl[%s], ordersSn[%s], goodsName[%s]", imageUrl, ordersSn, goodsName);
            ImageView goodsImage = helper.getView(R.id.order_goods_image);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(imageUrl)).centerCrop().into(goodsImage);

            String orderNum = mContext.getString(R.string.text_order_num) + ": " + ordersSn;
            helper.setText(R.id.tv_order_num, orderNum);
            helper.setText(R.id.tv_goods_name, goodsName);
        } else if (item.messageType == Constant.CHAT_MESSAGE_TYPE_ENC) {
            textView.setVisibility(View.GONE);
            llImageMessageContainer.setVisibility(View.GONE);
            llGoodsMessageContainer.setVisibility(View.GONE);
            llOrderMessageContainer.setVisibility(View.GONE);
            llEncMessageContainer.setVisibility(View.VISIBLE);
            SLog.info(item.toString());
            String avatar = null;
            String nickname = null;
            if (item.ext!=null) {
                avatar = item.ext.get("avatar").toString();
                nickname = item.ext.get("nickName").toString();
            } else {
                //ios發過來的
                EasyJSONObject data = EasyJSONObject.parse(item.content);
                try {
                    avatar = data.getSafeString("avatar");
                    nickname = data.getSafeString("nickName");
                } catch (Exception e) {
                }
            }


            ImageView imgEncAvatar = helper.getView(R.id.img_enc_avatar);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(avatar)).centerCrop().into(imgEncAvatar);
            helper.setText(R.id.tv_enc_nickname, StringUtil.isEmpty(item.trueName)?nickname:item.trueName);
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
