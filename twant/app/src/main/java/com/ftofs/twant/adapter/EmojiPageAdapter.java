package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.EmojiPage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Emoji;

import java.util.List;

public class EmojiPageAdapter extends BaseQuickAdapter<EmojiPage, BaseViewHolder> {
    public static final int[] emojiIds = new int[] {
            R.id.img_emoji_1, R.id.img_emoji_2, R.id.img_emoji_3, R.id.img_emoji_4, R.id.img_emoji_5,
            R.id.img_emoji_6, R.id.img_emoji_7, R.id.img_emoji_8, R.id.img_emoji_9, R.id.img_emoji_10,
            R.id.img_emoji_11, R.id.img_emoji_12, R.id.img_emoji_13, R.id.img_emoji_14, R.id.img_emoji_15,
            R.id.img_emoji_16, R.id.img_emoji_17, R.id.img_emoji_18, R.id.img_emoji_19, R.id.img_emoji_20,
    };
    public EmojiPageAdapter(int layoutResId, @Nullable List<EmojiPage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmojiPage emojiPage) {
        for (int i = 0; i < emojiPage.emojiList.size(); i++) {
            Emoji emoji = emojiPage.emojiList.get(i);
            ImageView imageView = helper.getView(emojiIds[i]);
            SLog.info("emoji.absolutePath[%s]", emoji.absolutePath);
            Glide.with(mContext).load(emoji.absolutePath).centerCrop().into(imageView);
        }
    }
}
