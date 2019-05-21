package com.ftofs.twant.util;

import com.ftofs.twant.constant.SPField;
import com.orhanobut.hawk.Hawk;

import java.util.HashSet;
import java.util.Set;

import cn.snailpad.easyjson.EasyJSONArray;

/**
 * 搜索歷史工具類
 * todo 改為HashMap按最近搜索的放在前面
 * @author zwm
 */
public class SearchHistoryUtil {
    /**
     * 加載搜索歷史記錄
     * @param searchTypeInt
     * @return
     */
    public static Set<String> loadSearchHistory(int searchTypeInt) {
        Set<String> keywordSet = new HashSet<>();

        String key = SPField.FIELD_SEARCH_TYPE + searchTypeInt;
        String historyJSONStr = Hawk.get(key, "[]");
        EasyJSONArray historyArr = (EasyJSONArray) EasyJSONArray.parse(historyJSONStr);
        for (Object object: historyArr) {
            String keyword = (String) object;
            keywordSet.add(keyword);
        }

        return keywordSet;
    }

    /**
     * 保存歷史搜索記錄
     * @param searchTypeInt
     * @param keyword
     */
    public static void saveSearchHistory(int searchTypeInt, String keyword) {
        Set<String> keywordSet = loadSearchHistory(searchTypeInt);
        keywordSet.add(keyword);

        EasyJSONArray historyArr = EasyJSONArray.generate();
        for (String item : keywordSet) {
            historyArr.append(item);
        }

        String key = SPField.FIELD_SEARCH_TYPE + searchTypeInt;
        Hawk.put(key, historyArr.toString());
    }

    /**
     * 清除歷史搜索記錄
     * @param searchTypeInt
     */
    public static void clearSearchHistory(int searchTypeInt) {
        String key = SPField.FIELD_SEARCH_TYPE + searchTypeInt;
        Hawk.put(key, "[]");
    }
}
