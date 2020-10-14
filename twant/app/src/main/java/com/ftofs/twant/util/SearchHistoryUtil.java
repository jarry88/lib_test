package com.ftofs.twant.util;

import android.util.Log;

import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.SearchHistoryItem;
import com.gzp.lib_common.utils.SLog;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;

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
    public static List<SearchHistoryItem> loadSearchHistory(int searchTypeInt) {
        try {
            List<SearchHistoryItem> searchHistoryItemList = new ArrayList<>();

            String key = SPField.FIELD_SEARCH_TYPE + searchTypeInt;
            String historyJSONStr = Hawk.get(key, "[]");
            SLog.info("historyJSONStr[%s]", historyJSONStr);
            EasyJSONArray historyArr = (EasyJSONArray) EasyJSONArray.parse(historyJSONStr);
            for (Object object: historyArr) {
                SearchHistoryItem searchHistoryItem = (SearchHistoryItem) EasyJSONBase.jsonDecode(SearchHistoryItem.class, object.toString());
                searchHistoryItemList.add(searchHistoryItem);
            }

            Collections.sort(searchHistoryItemList, new Comparator<SearchHistoryItem>() {
                @Override
                public int compare(SearchHistoryItem o1, SearchHistoryItem o2) {
                    return (int) (o2.timestamp - o1.timestamp);
                }
            });

            return searchHistoryItemList;
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            return null;
        }
    }

    /**
     * 保存歷史搜索記錄
     * @param searchTypeInt
     * @param keyword
     */
    public static void saveSearchHistory(int searchTypeInt, String keyword) {
        if (StringUtil.isEmpty(keyword)) {
            return;
        }

        Map<String, Long> keywordMap = new HashMap<>();

        String key = SPField.FIELD_SEARCH_TYPE + searchTypeInt;
        String historyJSONStr = Hawk.get(key, "[]");
        SLog.info("historyJSONStr[%s]", historyJSONStr);
        EasyJSONArray historyArr = (EasyJSONArray) EasyJSONArray.parse(historyJSONStr);

        try {
            for (Object object: historyArr) {
                SearchHistoryItem searchHistoryItem = (SearchHistoryItem) EasyJSONBase.jsonDecode(SearchHistoryItem.class, object.toString());
                keywordMap.put(searchHistoryItem.keyword, searchHistoryItem.timestamp);
            }
            keywordMap.put(keyword, System.currentTimeMillis());  // 設置最近一次搜索關鍵詞的時間

            EasyJSONArray searchHistoryArr = EasyJSONArray.generate();
            for (Map.Entry<String, Long> entry : keywordMap.entrySet()) {
                SearchHistoryItem item = new SearchHistoryItem();
                item.keyword = entry.getKey();
                item.timestamp = entry.getValue();
                searchHistoryArr.append(item.toJSONObject());
            }

            String value = searchHistoryArr.toString();

            SLog.info("key_history[%s], value_history[%s]", key, value);
            Hawk.put(key, value);
        } catch (Exception e) {

        }

    }

    /**
     * 清除歷史搜索記錄
     * @param searchTypeInt
     */
    public static void clearSearchHistory(int searchTypeInt) {
        String key = SPField.FIELD_SEARCH_TYPE + searchTypeInt;
        Hawk.put(key, "[]");
    }
    /**
     * 清除歷史搜索記錄
     * @param searchTypeInt
     */
}
