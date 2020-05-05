package com.ftofs.twant.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EntityReplace;
import com.ftofs.twant.entity.Mobile;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.PostDetailFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Emoji;
import com.ftofs.twant.widget.QMUIAlignMiddleImageSpan;
import com.ftofs.twant.widget.TwClickableSpan;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import kotlin.math.UMathKt;
import okhttp3.Call;

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
    public static String formatPrice(Context context, double price, int spaceCount) {
        if (isEmpty(currencyTypeSign)) {
            currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
        }

        StringBuilder sb = new StringBuilder(currencyTypeSign);
        for (int i = 0; i < spaceCount; i++) {
            sb.append(" ");
        }
        sb.append(StringUtil.formatFloat(price));
        return sb.toString();
    }
    /**
     * 格式化价格
     * @param context
     * @param price
     * @param spaceCount 美元符$与价格的空格个数
     * @param net2 保留小數點后2位
     * @return
     */
    public static String formatPrice(Context context, double price, int spaceCount,int net2) {
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
     * @param context
     * @param price
     * @param spaceCount 美元符$与价格的空格个数
     * @param byWord 尾部0用漢字代替
     * @return
     */
    public static String formatPrice(Context context, double price, int spaceCount,boolean byWord) {
        String result;
        String sb = formatPrice(context, price, spaceCount);
        result=sb;
        if (sb.length() > 8) {
            String lastEight =sb.substring(sb.length()-8);
            if (lastEight.equals("00000000")) {
                result = sb.substring(0, sb.length() - 8) + "億";
                return result;
            }
        }
        if(sb.length() > 4){
            String lastFour =sb.substring(sb.length()-4);
            if (lastFour.equals("0000")) {
                result= sb.substring(0, sb.length() - 4)+"萬";

            }
        }
        return result;
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
     * 格式化貼文瀏覽量的顯示
     * 顯示規則：
     *                         （1）小於9999時，直接顯示對應數字；
     *                         （2）大於9999時，從百位向上取整保留小數點後1位：顯示X.X萬；
     *
     *                         例：
     *                         10001 --> 1萬；
     *                         10100  --> 1.1萬；
     * @param postView 貼文瀏覽量
     * @return
     */
    public static String formatPostView(int postView) {
        SLog.info("formatPostView[%d]", postView);
        if (postView <= 9999) {
            return String.valueOf(postView);
        }

        int k10 = postView / 10000;  // 萬
        int k = (postView % 10000) / 1000;  // 千
        int h = (postView % 1000) / 100; // 百

        if (h > 0) { // 百位向上取整
            ++k;
            if (k >= 10) {
                ++k10;
                k = 0;
            }
        }

        String result = "";
        result += k10;
        if (k > 0) {
            result += "." + k;
        }

        return result + "萬";
    }

    /**
     * 將float轉換為字符串，如果val是整數，則去除后面的小數部分
     * 比如，如果val是 9.03， 則顯示9.03
     *      如果val是 9, 則顯示9，而不是9.0
     * @param val
     * @return
     */
    public static String formatFloat(double val) {
        String strVal = String.format("%.2f", val);
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
     * 判斷字符串是否為網址格式
     * @param str
     * @return
     */
    public static boolean isUrlString(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.startsWith("http://") || str.startsWith("https://");
    }

    /**
     * 二代身份証正則校驗合規性
     * 校驗規則：
     * 校验位的计算:
     * <p>
     * 有17位数字，分别是：
     * 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
     * 分别用身份证的前 17 位乘以上面相应位置的数字，然后相加。
     * <p>
     * 接着用相加的和对 11 取模。
     * <p>
     * 用获得的值在下面 11 个字符里查找对应位置的字符，这个字符就是校验位。
     * '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'
     *
     * @param idCard 身份証
     * @return 校驗結果
     */
    public static boolean validIdCard(String idCard) {
        //基礎校驗校驗身份証格式是否正確
        if (!isCardNumber(idCard)) {
            return false;
        }
        return false;
    }

    private static boolean isCardNumber(String idCard) {
//        const REGX = '(^\d{17}(\d|X)$)#';
        return false;
    }

    public static String normalizeImageUrl(String imageUrl) {
        return normalizeImageUrl(imageUrl, null);
    }

    /**
     * 規范圖片的Url，如果沒有前綴，添加前綴
     * @param imageUrl
     * @param params 请求参数, 例如：?x-oss-process=image/resize,w_800
     */
    public static String normalizeImageUrl(String imageUrl, String params) {
        if (isEmpty(imageUrl)) {
            return imageUrl;
        }
        if (isUrlString(imageUrl)) {
            if (StringUtil.isEmpty(params)) {
                return imageUrl;
            }
            return imageUrl + params;
        }

        String url = Config.OSS_BASE_URL + "/" + imageUrl;

        if (!StringUtil.isEmpty(params)) {
            url += params;
        }

        return url;
    }
    /**
     * 安卓10要路徑轉uri后才能獲取圖片
     * @param path 本地路徑
     */

    public static Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { path }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
    /**
     * 將[空格]替換為真正的空格，將[換行]替換為\n
     * @param text
     * @return
     */
    public static String escapeEntity(String text) {
        if (text == null) {
            return null;
        }

        List<EntityReplace> entityReplaceList = new ArrayList<>();

        Map<String, String> entityMap = new HashMap<>();
        entityMap.put("[換行]", "\n");
        entityMap.put("[空格]", " ");

        StringBuilder sb = new StringBuilder(text);
        int len = text.length();
        int startIndex = -1; // -1表示位置無效
        int stopIndex = -1; // -1表示位置無效

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
                    String replacement = entityMap.get(str);
                    SLog.info("replacement[%s]", replacement);
                    if (replacement != null) { // 如果是有效的表情
                        entityReplaceList.add(new EntityReplace(startIndex, stopIndex, replacement));
                    }
                }

                startIndex = -1;
                stopIndex = -1;
            }
        }

        // 從后往前替換
        for (int i = entityReplaceList.size() - 1; i >= 0; i--) {
            EntityReplace entityReplace = entityReplaceList.get(i);

            sb.replace(entityReplace.startIndex, entityReplace.stopIndex + 1, entityReplace.replacement);
        }

        return sb.toString();
    }


    /**
     * 處理文本中的Url鏈接
     * @param text
     */

    /**
     * 處理文本中的Url鏈接
     * @param context
     * @param text
     * @param simpleCallback 點擊文本中URL鏈接時的回調
     * @return
     */
    public static SpannableString processTextUrl(Context context, String text, SimpleCallback simpleCallback) {
        /*
        匹配鏈接的正則表達式
        參考
        https://cyhour.com/1009/
        https://regexr.com/
         */
        Pattern pattern = Pattern.compile("https?:\\/\\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");


        SpannableString spannableString = new SpannableString(text);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String data = matcher.group();
            SLog.info("data[%d][%d][%s]\n", matcher.start(), matcher.end(), data);

            TwClickableSpan clickableSpan = new TwClickableSpan(context, data, simpleCallback);
            spannableString.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }



        return spannableString;
    }

    public static Editable translateEmoji(Context context, String text, int textSize) {
        return translateEmoji(context, text, textSize, null);
    }


    public static void parseCustomUrl(Context context, String origUrl) {
        EasyJSONObject params = EasyJSONObject.generate("url", origUrl);
        Api.postUI(Api.PATH_PARSE_URL, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(context, responseObj)) {
                    return;
                }

                try {
                    String action = responseObj.getSafeString("datas.action");
                    if ("store".equals(action)) {
                        int storeId = responseObj.getInt("datas.storeId");
                        Util.startFragment(ShopMainFragment.newInstance(storeId));
                    } else if ("goods".equals(action)) {
                        String commonIdStr = responseObj.getSafeString("datas.commonId");
                        int commonId = Integer.valueOf(commonIdStr);
                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                    } else if ("postinfo".equals(action)) {
                        String postIdStr = responseObj.getSafeString("datas.postId");
                        int postId = Integer.valueOf(postIdStr);
                        Util.startFragment(PostDetailFragment.newInstance(postId));
                    } else if ("weburl".equals(action) || "default".equals(action)) {
                        String url = responseObj.getSafeString("datas.url");
                        Util.startFragment(H5GameFragment.newInstance(url, ""));
                    }
                } catch (Exception e) {

                }

            }
        });
    }


    /**
     * 替換text文本中的表情占位符為圖片
     * @param context
     * @param text
     * @param textSize  文本控件中的文字大小
     * @param urlClickedCallback 點擊Url鏈接的回調
     * @return
     */
    public static Editable translateEmoji(Context context, String text, int textSize, SimpleCallback urlClickedCallback) {
        //SLog.info("text[%s]", text);
        text = escapeEntity(text);
        //SLog.info("text[%s]", text);

        SpannableStringBuilder spannableString;
        if (urlClickedCallback != null) { // 有添加Url點擊回調才解析url鏈接
            SpannableString urlProcessedText = processTextUrl(context, text, urlClickedCallback);
            spannableString = new SpannableStringBuilder(urlProcessedText);
        } else {
            spannableString = new SpannableStringBuilder(text);
        }

        List<Emoji> emojiList = LitePal.findAll(Emoji.class);
        HashMap<String, String> emojiMap = new HashMap<>();
        for (Emoji emoji : emojiList) {
            emojiMap.put(emoji.emojiCode, emoji.absolutePath);
        }

        int len = text.length();
        int startIndex = -1; // -1表示位置無效
        int stopIndex = -1; // -1表示位置無效
        for (int i = 0; i < len; i++) {
            char ch = text.charAt(i);

            if (ch == '[') {
                startIndex = i;
                stopIndex = -1;
            } else if (ch == ']') {
                stopIndex = i;
                if (startIndex >= 0) { // 符合表情標簽格式
                    String str = text.substring(startIndex, stopIndex + 1);
                    // SLog.info("str[%s]", str);
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
     * 提取環信的文本消息，去掉前綴
     * @param message
     * @return
     */
    public static String getEMMessageText(String message) {
        // txt:"abc" 返回 abc
        SLog.info("message[%s]", message);
        if (message.length() > 5) {
            return message.substring(5, message.length() - 1);
        } else {
            return message;
        }

    }
    /**
     * 提取環信的body中的屬性
     * @param message
     * @return
     */
    public static String getEMMessageStringAttribute(String message,String attribute) {
        // txt:"abc" 返回 abc
        String string="";
        if (isEMMessageText(message)) {
            message = message.substring(5,message.length()-1);
        } else {
            return "";
        }
        SLog.info("message[%s]", message);
        if (message.equals("[電子名片]")) {
            return "";
        }
        EasyJSONObject responseObj = EasyJSONObject.parse(message);
        try {
            string = responseObj.getSafeString(attribute);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        return string;
    }

    /**
     * 判斷是否為環信格式的消息文本   比如， txt:"abc"
     * @param message
     * @return
     */
    public static boolean isEMMessageText(String message) {
        if (message == null || message.length() == 0) {
            return false;
        }

        char lastChar = message.charAt(message.length() - 1);
        return message.startsWith("txt:\"") && lastChar == '"';
    }

    public static Editable getSpannableMessageText(Context context, String text, int textSize) {
        return translateEmoji(context, getEMMessageText(text), textSize, null);
    }

    /**
     * 判斷是否為合法的手機號
     * @param mobile 手機號
     * @param areaId 地區Id 1 -- 香港 2 -- 大陸 3 -- 澳門
     * @return
     */
    public static boolean isMobileValid(String mobile, int areaId) {
        String[] mobileRex = new String[] {
                "",
                "^[569][0-9]{7}$", // 香港
                "^1[0-9]{10}$",    // 大陸
                "^6[0-9]{7}$"   // 澳門
        };

        Pattern pattern = Pattern.compile(mobileRex[areaId]);

        Matcher matcher = pattern.matcher(mobile);

        boolean result = matcher.matches();
        SLog.info("matches[%s]", result);
        return result;
    }

    /**
     * 發表說說替換空格和換行符
     * @param content
     * @return
     */
    public static String filterCommentContent(String content) {
        StringBuilder sb = new StringBuilder();
        int len = content.length();
        for (int i = 0; i < len; i++) {
            char ch = content.charAt(i);

            if (ch == ' ') {
                sb.append("[空格]");
            } else if (ch == '\n') {
                sb.append("[換行]");
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    /**
     * 判斷是否為內網URL
     * @param url
     * @return
     */
    private static boolean isIntranetAddr(String url) {
        if (url == null) {
            return false;
        }

        return url.startsWith("http://192") || url.startsWith("https://192") || url.startsWith("http://localhost");
    }

    /**
     * 根據頭像URL判斷，是否使用默認頭像
     * @param url
     * @return
     */
    public static boolean useDefaultAvatar(String url) {
        if (isEmpty(url)) {
            return true;
        } else if ("img/default_avatar.png".equals(url)) {
            return true;
        }

        return isIntranetAddr(url);
    }

    /**
     * 給定的字符串符合字符串列表中的其中一個字符串
     * @param string
     * @param strings
     * @return
     */
    public static boolean equalsOne(String string, String[] strings) {
        if (string == null) {
            return false;
        }

        for (int i = 0; i < strings.length; i++) {
            if (string.equals(strings[i])) {
                return true;
            }
        }

        return false;
    }


    /**
     * 有些客服的memberName，比如 u_452915810409s533，倒數第4為s，這個方法用於返回s之前的memberName
     * @param memberName
     * @return
     */
    public static String getPureMemberName(String memberName) {
        if (memberName == null) {
            return null;
        }

        int idx = memberName.indexOf('s');
        if (idx != -1) {
            memberName = memberName.substring(0, idx);
        }

        return memberName;
    }


    public static String parseZone() {
        String mobile = User.getUserInfo(SPField.FIELD_MOBILE, "");
        if (isEmpty(mobile)) {
            return null;
        } else {
            int index = mobile.lastIndexOf(",");
            if (index == -1) {
                return null;
            } else {
                String zone = mobile.substring(0, index);
                return zone;
            }
        }
    }

    /**
     *
     * @param color 不加透明度的顔色： #004411
     * @param percent 百分比，例如 60，即百分之60
     * @return
     */
    public static String addAlphaToColor(String color, int percent) {
        if (isEmpty(color)) {
            return color;
        }
        if (color.length() > 7) {
            return color;
        }

        String newColor = "#" + Integer.toHexString(0xFF * percent / 100) + color.substring(1);
        return newColor;
    }
}
