package com.ftofs.twant.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.SearchHistoryItem;
import com.ftofs.twant.entity.SearchPostParams;
import com.ftofs.twant.fragment.CircleFragment;
import com.ftofs.twant.fragment.SearchResultFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.util.SearchHistoryUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.nex3z.flowlayout.FlowLayout;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SearchHistoryPopup extends PartShadowPopupView implements View.OnClickListener {
    Context context;
    SearchType searchType;
    SimpleCallback callback;

    int twBlack;
    int twBlue;

    public static final String SEARCH_HISTORY_ITEM = "SEARCH_HISTORY_ITEM";

    FlowLayout flHistoryContainer;
    FlowLayout flSearchHotContainer;
    LinearLayout llSearchHotContainer;

    public SearchHistoryPopup(@NonNull Context context, SearchType searchType, SimpleCallback callback) {
        super(context);

        this.context = context;
        this.searchType = searchType;
        this.callback = callback;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.search_history_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        twBlack = context.getColor(R.color.tw_black);
        twBlue = context.getColor(R.color.tw_blue);
        flHistoryContainer = findViewById(R.id.fl_search_history_container);
        flSearchHotContainer = findViewById(R.id.fl_search_hot_container);

        List<SearchHistoryItem> historyItemList = SearchHistoryUtil.loadSearchHistory(searchType.ordinal());
        for (SearchHistoryItem item : historyItemList) {
            TextView textView = (TextView) LayoutInflater.from(context)
                    .inflate(R.layout.search_history_popup_item, flHistoryContainer, false);
            textView.setText(item.keyword);

            textView.setTag(SEARCH_HISTORY_ITEM);
            textView.setOnClickListener(this);

            flHistoryContainer.addView(textView);
        }

        findViewById(R.id.btn_clear_all).setOnClickListener(this);

        loadHotKeyword();
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (v instanceof TextView) {
            Object tag = v.getTag();
            if (tag != null && tag instanceof String && SEARCH_HISTORY_ITEM.equals(tag)) {
                TextView btnSearchHistoryItem = (TextView) v;
                String keyword = btnSearchHistoryItem.getText().toString();

                if (searchType == SearchType.GOODS || searchType == SearchType.STORE) {
                    Util.startFragment(SearchResultFragment.newInstance(searchType.name(),
                            EasyJSONObject.generate("keyword", keyword).toString()));
                } else {
                    SearchPostParams searchPostParams = new SearchPostParams();
                    searchPostParams.keyword = keyword;
                    Util.startFragment(CircleFragment.newInstance(true, searchPostParams));
                }

                dismiss();

                return;
            }
        }

        if (id == R.id.btn_clear_all) {
            flHistoryContainer.removeAllViews();
            SearchHistoryUtil.clearSearchHistory(searchType.ordinal());

            if (callback != null) {
                EasyJSONObject data = EasyJSONObject.generate("action", "clear_all_history");
                callback.onSimpleCall(data.toString());
            }

            dismiss();
        }
    }


    /**
     * 加載【熱門搜索】
     */
    private void loadHotKeyword() {
        Api.getUI(Api.PATH_HOT_KEYWORD, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(context, responseObj)) {
                        return;
                    }

                    EasyJSONArray keywordList = responseObj.getSafeArray("datas.keywordList");
                    int count = 0;
                    for (Object object : keywordList) {
                        final String hotKeyword = (String) object;
                        if (StringUtil.isEmpty(hotKeyword)) {
                            continue;
                        }

                        TextView textView = (TextView) LayoutInflater.from(context)
                                .inflate(R.layout.search_history_popup_item, flSearchHotContainer, false);
                        textView.setText(hotKeyword);

                        textView.setTag(SEARCH_HISTORY_ITEM);
                        textView.setOnClickListener(SearchHistoryPopup.this);

                        if (count == 0) { // 第1個關鍵詞顯示藍色
                            textView.setTextColor(twBlue);
                        }
                        flSearchHotContainer.addView(textView);
                        ++count;
                    }

                    if (count > 0) {
                        llSearchHotContainer.setVisibility(View.VISIBLE);
                    } else {
                        llSearchHotContainer.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
            }
        });
    }
}