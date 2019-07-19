package com.ftofs.twant.entity;

import com.ftofs.twant.orm.Emoji;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情頁
 * @author zwm
 */
public class EmojiPage {
    // 每頁20個表情
    public static final int EMOJI_PER_PAGE = 20;
    public List<Emoji> emojiList;

    public EmojiPage() {
        this.emojiList = new ArrayList<>();
    }
}
