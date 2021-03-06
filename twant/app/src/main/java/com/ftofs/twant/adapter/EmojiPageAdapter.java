package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.EmojiPage;
import com.ftofs.twant.entity.UnicodeEmojiItem;
import com.ftofs.twant.orm.Emoji;

import java.util.List;

public class EmojiPageAdapter extends BaseQuickAdapter<EmojiPage, BaseViewHolder> {
    public static final int[] emojiIds = new int[] {
            R.id.img_emoji_1, R.id.img_emoji_2, R.id.img_emoji_3, R.id.img_emoji_4, R.id.img_emoji_5,
            R.id.img_emoji_6, R.id.img_emoji_7, R.id.img_emoji_8, R.id.img_emoji_9, R.id.img_emoji_10,
            R.id.img_emoji_11, R.id.img_emoji_12, R.id.img_emoji_13, R.id.img_emoji_14, R.id.img_emoji_15,
            R.id.img_emoji_16, R.id.img_emoji_17, R.id.img_emoji_18, R.id.img_emoji_19, R.id.img_emoji_20,
    };

    public static final int[] btnIds = new int[] {
            R.id.btn_emoji_1, R.id.btn_emoji_2, R.id.btn_emoji_3, R.id.btn_emoji_4, R.id.btn_emoji_5,
            R.id.btn_emoji_6, R.id.btn_emoji_7, R.id.btn_emoji_8, R.id.btn_emoji_9, R.id.btn_emoji_10,
            R.id.btn_emoji_11, R.id.btn_emoji_12, R.id.btn_emoji_13, R.id.btn_emoji_14, R.id.btn_emoji_15,
            R.id.btn_emoji_16, R.id.btn_emoji_17, R.id.btn_emoji_18, R.id.btn_emoji_19, R.id.btn_emoji_20,
    };

    public EmojiPageAdapter(int layoutResId, @Nullable List<EmojiPage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmojiPage emojiPage) {
        for (int i = 0; i < emojiIds.length; i++) {
            TextView textView = helper.getView(emojiIds[i]);
            if (i < emojiPage.emojiList.size()) {
                UnicodeEmojiItem emojiItem = emojiPage.emojiList.get(i);
                textView.setText(emojiItem.emoji);
                // SLog.info("emoji.absolutePath[%s]", emoji.absolutePath);

                helper.addOnClickListener(btnIds[i]);
            } else {
                textView.setText("");
            }
        }

        helper.addOnClickListener(R.id.btn_delete_emoji);
    }
}
