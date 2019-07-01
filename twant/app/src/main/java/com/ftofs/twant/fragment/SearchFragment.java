package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.SearchHistoryUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.nex3z.flowlayout.FlowLayout;

import java.io.IOException;
import java.util.Set;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 搜索Fragment
 * @author zwm
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener {
    EditText etKeyword;
    String currentKeyword; // 當前搜索的關鍵詞
    String defaultKeyword; // 默認搜索詞
    SearchType searchType = SearchType.GOODS;

    LinearLayout llHistorySearchPane;
    FlowLayout flSearchHistoryContainer;
    FlowLayout flHotSearchContainer;

    LinearLayout llSearchSuggestionContainer;
    LinearLayout llSuggestionList;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_clear_all, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_search, this);
        Util.setOnClickListener(view, R.id.btn_clear_search_history, this);
        Util.setOnClickListener(view, R.id.ll_mask, this);

        llSearchSuggestionContainer = view.findViewById(R.id.ll_search_suggestion_container);
        llSuggestionList = view.findViewById(R.id.ll_suggestion_list);

        etKeyword = view.findViewById(R.id.et_keyword);
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // SLog.info("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // SLog.info("onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String term = s.toString();
                SLog.info("afterTextChanged, term[%s]", term);
                loadSearchSuggestionData(term);
            }
        });
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(textView.getText().toString());
                    return true;
                }
                return false;
            }
        });

        llHistorySearchPane = view.findViewById(R.id.ll_history_search_pane);
        flSearchHistoryContainer = view.findViewById(R.id.fl_search_history_container);
        flHotSearchContainer = view.findViewById(R.id.fl_hot_search_container);

        TabLayout tabLayout = view.findViewById(R.id.search_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.search_tab_title_goods)).setTag(SearchType.GOODS));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.search_tab_title_store)).setTag(SearchType.STORE));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.search_tab_title_article)).setTag(SearchType.ARTICLE));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchType = (SearchType) tab.getTag();
                SLog.info("searchType[%s]", searchType);
                loadSearchHistory();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        loadHotKeyword();
        loadDefaultKeyword();
    }

    private void loadSearchSuggestionData(String term) {
        term = term.trim();
        if (StringUtil.isEmpty(term)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("term", term);
        Api.getUI(Api.PATH_SEARCH_SUGGESTION, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.isError(responseObj)) {
                    return;
                }

                try {
                    llSearchSuggestionContainer.setVisibility(View.VISIBLE);
                    llSuggestionList.removeAllViews();
                    EasyJSONArray suggestList = responseObj.getArray("datas.suggestList");
                    for (Object object : suggestList) {
                        final String item = (String) object;
                        View itemView = LayoutInflater.from(_mActivity).inflate(R.layout.search_suggestion_item,
                                llSuggestionList, false);

                        TextView tvSuggestionKeyword = itemView.findViewById(R.id.tv_suggestion_keyword);
                        tvSuggestionKeyword.setText(item);
                        itemView.findViewById(R.id.btn_fill).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etKeyword.setText(item);
                                EditTextUtil.cursorSeekToEnd(etKeyword);
                            }
                        });
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doSearch(item);
                            }
                        });
                        llSuggestionList.addView(itemView);
                    }
                } catch (Exception e) {

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_clear_all:
                // 清空搜索關鍵字
                etKeyword.setText("");
                break;
            case R.id.btn_clear_search_history:
                SearchHistoryUtil.clearSearchHistory(searchType.ordinal());
                flSearchHistoryContainer.removeAllViews();
                llHistorySearchPane.setVisibility(View.GONE);
                break;
            case R.id.btn_search:
                doSearch(etKeyword.getText().toString().trim());
                break;
            case R.id.ll_mask:
                llSearchSuggestionContainer.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void doSearch(String keyword) {
        currentKeyword = keyword;
        // 將keyword填充到搜索欄中，并跳轉到搜索結果頁面
        etKeyword.setText(currentKeyword);
        EditTextUtil.cursorSeekToEnd(etKeyword);

        // 如果為空，用默認關鍵詞搜索
        if (StringUtil.isEmpty(currentKeyword)) {
            currentKeyword = defaultKeyword;
        }

        hideSoftInput();

        MainFragment mainFragment = MainFragment.getInstance();
        mainFragment.start(SearchResultFragment.newInstance(searchType.name(),
                EasyJSONObject.generate("keyword", currentKeyword).toString()));
    }

    /**
     * 加載【歷史搜索】
     */
    private void loadSearchHistory() {
        flSearchHistoryContainer.removeAllViews();

        Set<String> keywordSet = SearchHistoryUtil.loadSearchHistory(searchType.ordinal());
        if (keywordSet.size() < 1) { // 如果沒有歷史搜索數據，隱藏
            llHistorySearchPane.setVisibility(View.GONE);
        } else {
            for (String keyword : keywordSet) {
                addSearchHistoryItemView(keyword);
            }
            llHistorySearchPane.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 加載【熱門搜索】
     */
    private void loadHotKeyword() {
        flHotSearchContainer.removeAllViews();

        Api.getUI(Api.PATH_HOT_KEYWORD, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONArray keywordList = responseObj.getArray("datas.keywordList");
                    int count = 0;
                    for (Object object : keywordList) {
                        final String hotKeyword = (String) object;

                        TextView hotKeywordButton = new TextView(_mActivity);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        hotKeywordButton.setBackgroundResource(R.drawable.search_item_bg);
                        hotKeywordButton.setPadding(Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6),
                                Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6));
                        hotKeywordButton.setText(hotKeyword);
                        hotKeywordButton.setTextSize(14);
                        if (count == 0) { // 第1個關鍵詞顯示紅色
                            hotKeywordButton.setTextColor(getResources().getColor(R.color.tw_red, null));
                        } else {
                            hotKeywordButton.setTextColor(getResources().getColor(R.color.tw_black, null));
                        }

                        hotKeywordButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doSearch(hotKeyword);
                            }
                        });
                        flHotSearchContainer.addView(hotKeywordButton, layoutParams);
                        ++count;
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 加載【搜索框默认显示词】
     */
    private void loadDefaultKeyword() {
        Api.getUI(Api.PATH_DEFAULT_KEYWORD, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    String defaultDisplayKeyword = responseObj.getString("datas.keywordName");
                    defaultKeyword = responseObj.getString("datas.keywordValue");

                    etKeyword.setHint(defaultDisplayKeyword);
                } catch (Exception e) {

                }
            }
        });
    }

    private void addSearchHistoryItemView(String keyword) {
        TextView textView = new TextView(_mActivity);

        textView.setPadding(Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6),
                Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6));
        textView.setText(keyword);
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.tw_black, null));
        textView.setBackgroundResource(R.drawable.search_item_bg);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                doSearch(tv.getText().toString());
            }
        });

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flSearchHistoryContainer.addView(textView, layoutParams);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        // 從其它Fragment返回時隱藏搜索建議
        llSearchSuggestionContainer.setVisibility(View.GONE);
        loadSearchHistory();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
