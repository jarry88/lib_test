package com.ftofs.twant.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.Mobile;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Emoji;
import com.ftofs.twant.orm.UserStatus;
import com.ftofs.twant.widget.QMUIAlignMiddleImageSpan;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 字符串工具類
 * @author zwm
 */
public class StringUtil {
    private static String currencyTypeSign;

    /**
     * 判斷字符串是否為空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 用glue將array中的元素拼接起來，T必須為Object類型的子類，例如，如果是整數，不能為int，必須為Integer
     * @param glue
     * @param array
     * @param <T>
     * @return
     */
    public static<T> String implode(String glue, T[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append((i == array.length - 1) ? array[i] : array[i] + glue);
        }
        return sb.toString();
    }

    /**
     * 用glue將array中的元素拼接起來
     * @param glue
     * @param list
     * @return
     */
    public static String implode(String glue, List list) {
        if (list == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append((i == list.size() - 1) ? list.get(i) : list.get(i) + glue);
        }
        return sb.toString();
    }

    /**
     * 格式化价格
     * @param context
     * @param price
     * @param spaceCount 美元符$与价格的空格个数
     * @return
     */
    public static String formatPrice(Context context, float price, int spaceCount) {
        if (isEmpty(currencyTypeSign)) {
            currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
        }

        StringBuilder sb = new StringBuilder(currencyTypeSign);
        for (int i = 0; i < spaceCount; i++) {
            sb.append(" ");
        }
        sb.append(String.format("%.2f", price));
        return sb.toString();
    }

    /**
     * 区号转为地区Id
     * @param areaCode
     * @return
     */
    public static int areaCodeToAreaId(String areaCode) {
        switch (areaCode) {
            case Constant.AREA_CODE_HONGKONG:
                return Constant.AREA_ID_HONGKONG;
            case Constant.AREA_CODE_MAINLAND:
                return Constant.AREA_ID_MAINLAND;
            case Constant.AREA_CODE_MACAO:
                return Constant.AREA_ID_MACAO;
            default:
                return Constant.AREA_ID_UNKNOWN;
        }
    }


    /**
     * 获取手机号信息
     * @param fullMobile  带区号的手机号码  008613417785707 这种格式 或 0086,13417785707 这种格式
     * @return
     */
    public static Mobile getMobileInfo(String fullMobile) {
        Mobile mobile = new Mobile();
        if (fullMobile.indexOf(',') != -1) {
            // 用逗号分隔这种格式
            String[] result = fullMobile.split(",");
            mobile.areaCode = result[0];
            mobile.mobile = result[1];
            mobile.areaId = areaCodeToAreaId(mobile.areaCode);
            return mobile;
        }

        if (fullMobile.startsWith(Constant.AREA_CODE_HONGKONG)) {
            mobile.areaId = Constant.AREA_ID_HONGKONG;
            mobile.areaCode = fullMobile.substring(0, 5);
            mobile.mobile = fullMobile.substring(5);
        } else if (fullMobile.startsWith(Constant.AREA_CODE_MACAO)) {
            mobile.areaId = Constant.AREA_ID_MACAO;
            mobile.areaCode = fullMobile.substring(0, 5);
            mobile.mobile = fullMobile.substring(5);
        } else if (fullMobile.startsWith(Constant.AREA_CODE_MAINLAND)) {
            mobile.areaId = Constant.AREA_ID_MAINLAND;
            mobile.areaCode = fullMobile.substring(0, 4);
            mobile.mobile = fullMobile.substring(4);
        } else {
            // 未知地区
            mobile.areaId = Constant.AREA_ID_UNKNOWN;
            mobile.areaCode = "";
            mobile.mobile = fullMobile;
        }

        return mobile;
    }

    /**
     * 將float轉換為字符串，如果val是整數，則去除后面的小數部分
     * 比如，如果val是 9.03， 則顯示9.03
     *      如果val是 9, 則顯示9，而不是9.0
     * @param val
     * @return
     */
    public static String formatFloat(float val) {
        String strVal = String.valueOf(val);
        int len = strVal.length();
        int index = len - 1;

        // 看看最后面是否有【零】
        while (index >= 0) {
            char ch = strVal.charAt(index);
            // 如果遇到小數點，結束處理
            if (ch == '.') {
                break;
            }
            if (ch != '0') {
                // 如果index指向的不是0，還要往后挪一位，并且退出
                ++index;
                break;
            }
            --index;
        }

        return strVal.substring(0, index);
    }

    /**
     * 將規格Id字符串轉為List， 例如 "16,242,35" 轉為 [16, 242, 35]
     * @param specValueIds
     * @return
     */
    public static List<Integer> specValueIdsToList(String specValueIds) {
        List<Integer> result = new ArrayList<>();
        if (StringUtil.isEmpty(specValueIds)) {
            return result;
        }
        String[] specValueIdArr = specValueIds.split(",");
        for (int i = 0; i < specValueIdArr.length; i++) {
            result.add(Integer.parseInt(specValueIdArr[i]));
        }

        return result;
    }


    /**
     * 規范圖片的Url，如果沒有前綴，添加前綴
     * @param imageUrl
     */
    public static String normalizeImageUrl(String imageUrl) {
        if (imageUrl == null) {
            return null;
        }
        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            return imageUrl;
        }

        return Config.OSS_BASE_URL + "/" + imageUrl;
    }

    /**
     * 替換text文本中的表情占位符為圖片
     * @param context
     * @param text
     * @param textSize  文本控件中的文字大小
     * @return
     */
    public static Editable translateEmoji(Context context, String text, int textSize) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(text);

        List<Emoji> emojiList = LitePal.findAll(Emoji.class);
        HashMap<String, String> emojiMap = new HashMap<>();
        for (Emoji emoji : emojiList) {
            emojiMap.put(emoji.emojiCode, emoji.absolutePath);
        }

        int len = text.length();
        int startIndex = -1;
        int stopIndex = -1;
        for (int i = 0; i < len; i++) {
            char ch = text.charAt(i);

            if (ch == '[') {
                startIndex = i;
                stopIndex = -1;
            } else if (ch == ']') {
                stopIndex = i;
                if (startIndex >= 0) { // 符合表情標簽格式
                    String str = text.substring(startIndex, stopIndex + 1);
                    SLog.info("str[%s]", str);
                    String emojiAbsolutePath = emojiMap.get(str);

                    if (emojiAbsolutePath != null) { // 如果是有效的表情
                        Bitmap bitmap = BitmapFactory.decodeFile(emojiAbsolutePath);
                        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                        drawable.setBounds(0, 0, textSize + 12, textSize + 12);
                        QMUIAlignMiddleImageSpan span = new QMUIAlignMiddleImageSpan(drawable, QMUIAlignMiddleImageSpan.ALIGN_MIDDLE);

                        spannableString.setSpan(span, startIndex, stopIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }


                startIndex = -1;
                stopIndex = -1;
            }
        }

        return spannableString;
    }


    /**
     * 提取環信的文本消息，順序翻譯表情
     * @param context
     * @param message
     * @param textSize
     * @return
     */
    public static Editable getMessageText(Context context, String message, int textSize) {
        // txt:"abc" 返回 abc
        message = message.substring(5, message.length() - 1);
        return translateEmoji(context, message, textSize);
    }
}
