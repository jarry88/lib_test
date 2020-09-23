package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.SearchHistoryItemAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.SearchHistoryItem;
import com.ftofs.twant.entity.SearchPostParams;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.SearchHistoryUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SearchHistoryPopup;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 分類Fragment
 * @author zwm
 */
public class CategoryFragment extends BaseFragment implements View.OnClickListener, SimpleCallback {
    String from; // 来自哪个fragment调用
    SearchType searchType;  // 當前選中哪種搜索類型

    EditText etKeyword;
    String currentKeyword; // 當前搜索的關鍵詞
    String defaultKeyword; // 默認搜索詞

    LinearLayout llSearchSuggestionContainer;
    LinearLayout llSuggestionList;

    RecyclerView rvSearchHistoryList;
    SearchHistoryItemAdapter searchHistoryItemAdapter;
    ScaledButton btnExpandHistory;

    private List<String> titleList = new ArrayList<>();
    LinearLayout llCategoryTab;

    private List<Fragment> fragmentList = new ArrayList<>();

    List<String> goodsSearchHistoryList = new ArrayList<>();
    List<String> storeSearchHistoryList = new ArrayList<>();
    List<String> postSearchHistoryList = new ArrayList<>();

    SearchHistoryPopup searchHistoryPopup;
    LinearLayout llSearchHistoryContainer;

    ViewPager viewPager;
    View vwAnchor;  // 用于定位出详细历史记录的弹窗的锚点

    public static final int TAB_INDEX_STORE = 0;
    public static final int TAB_INDEX_GOODS = 1;
    public static final int TAB_INDEX_POST = 2;
    int currTabIndex;
    TextView[] tabBtnArr = new TextView[3];
    int[] tabBtnIdArr = new int[] {R.id.tab_btn_store, R.id.tab_btn_goods, R.id.tab_btn_post};

    int twRed;
    int twBlue;
    private TextView tvCategoryTopHint;
    private FrameLayout btnClearKeyWord;

    public void setParams(SearchType searchType, String from) {
        this.searchType = searchType;
        this.from = from;

        if (searchType == SearchType.STORE) {
            currTabIndex = TAB_INDEX_STORE;
        } else if (searchType == SearchType.GOODS) {
            currTabIndex = TAB_INDEX_GOODS;
        } else {
            currTabIndex = TAB_INDEX_POST;
        }
    }


    public static CategoryFragment newInstance(SearchType searchType, String from) {
        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        fragment.setParams(searchType, from);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llCategoryTab = view.findViewById(R.id.ll_category_tab);
        for (int i = 0; i < 3; i++) {
            tabBtnArr[i] = view.findViewById(tabBtnIdArr[i]);
            tabBtnArr[i].setOnClickListener(this);
        }

        twBlue = getResources().getColor(R.color.tw_blue, null);
        twRed = getResources().getColor(R.color.tw_red, null);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_message, this);
        Util.setOnClickListener(view, R.id.ll_mask, this);
        Util.setOnClickListener(view, R.id.btn_clear_all, this);
        btnClearKeyWord = view.findViewById(R.id.btn_clear_all);
        btnClearKeyWord.setVisibility(View.GONE);

        viewPager = view.findViewById(R.id.category_viewpager);
        vwAnchor = view.findViewById(R.id.vw_anchor);
        tvCategoryTopHint = view.findViewById(R.id.tv_category_name);
        tvCategoryTopHint.setText(getResources().getString(R.string.category_shop)+": ");
        titleList.add(getResources().getString(R.string.category_shop));
        titleList.add(getResources().getString(R.string.category_commodity));
        titleList.add(getResources().getString(R.string.category_brand));

        fragmentList.add(CategoryShopFragment.newInstance());
        fragmentList.add(new CategoryCommodityKotlinFragment());
        fragmentList.add(CategoryPostFragment.newInstance());


        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);

        llSearchHistoryContainer = view.findViewById(R.id.ll_search_history_container);

        llCategoryTab.postDelayed(() -> {
            int tabIndex;
            if (searchType == SearchType.STORE) {
                tabIndex = TAB_INDEX_STORE;
            } else if (searchType == SearchType.GOODS) {
                tabIndex = TAB_INDEX_GOODS;
            } else {
                tabIndex = TAB_INDEX_POST;
            }
            selectTab(tabIndex, true);
        }, 250);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SLog.info("onPageSelected[%d]", position);
                if (position == 0) {
                    tvCategoryTopHint.setText(getResources().getString(R.string.category_shop)+": ");
                    searchType = SearchType.STORE;
                } else if (position == 1) {
                    tvCategoryTopHint.setText(getResources().getString(R.string.category_commodity)+": ");
                    searchType = SearchType.GOODS;
                } else if (position == 2) {
                    searchType = SearchType.POST;
                    tvCategoryTopHint.setText(getResources().getString(R.string.text_short_want)+": ");

                }
                loadSearchHistoryData();

                selectTab(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnExpandHistory = view.findViewById(R.id.btn_expand_history);
        btnExpandHistory.setOnClickListener(this);

        rvSearchHistoryList = view.findViewById(R.id.rv_search_history_list);
        searchHistoryItemAdapter = new SearchHistoryItemAdapter(R.layout.search_history_item, goodsSearchHistoryList);
        searchHistoryItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String keyword;
                if (searchType == SearchType.GOODS) {
                    keyword = goodsSearchHistoryList.get(position);
                    Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                            EasyJSONObject.generate("keyword", keyword).toString()));
                } else if (searchType == SearchType.STORE) {
                    keyword = storeSearchHistoryList.get(position);
                    Util.startFragment(SearchResultFragment.newInstance(SearchType.STORE.name(),
                            EasyJSONObject.generate("keyword", keyword).toString()));
                } else if (searchType == SearchType.POST) {
                    keyword = postSearchHistoryList.get(position);
                    SearchPostParams searchPostParams = new SearchPostParams();
                    searchPostParams.keyword = keyword;
                    Util.startFragment(CircleFragment.newInstance(true, searchPostParams));
                }
            }
        });
        rvSearchHistoryList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        rvSearchHistoryList.setAdapter(searchHistoryItemAdapter);


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
                // 如果在搜索產品時，才提供搜索建議
                if (s.length() > 0) {
                    btnClearKeyWord.setVisibility(View.VISIBLE);
                } else {
                    btnClearKeyWord.setVisibility(View.GONE);
                }
                if (searchType == SearchType.GOODS) {
                    String term = s.toString();
                    SLog.info("afterTextChanged, term[%s]", term);
                    loadSearchSuggestionData(term);
                }

            }
        });
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                int keyCode = 0;
                if (event != null) {
                    event.getKeyCode();
                }

                SLog.info("actionId[%d], keyCode[%d]", actionId, keyCode);
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED || keyCode == KeyEvent.KEYCODE_ENTER // 兼容倉頡中文輸入法
                ) {
                    doSearch(textView.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        etKeyword.postDelayed(() -> showSoftInput(etKeyword), 200);
        SLog.info("from[%s]", from);
        if (from != null && from.equals("想要")) {
            selectTab(TAB_INDEX_POST,true);
            tvCategoryTopHint.setText(from+": ");
            hideBodyXml();
        }
        loadSearchHistoryData();
    }

    private void hideBodyXml() {
        viewPager.setVisibility(View.GONE);
        llCategoryTab.setVisibility(View.GONE);
        // rvSearchHistoryList.setVisibility(View.GONE);
    }

    private void loadSearchHistoryData() {
        if (searchType == SearchType.GOODS) {
            goodsSearchHistoryList.clear();
            List<SearchHistoryItem> goodsHistoryItem = SearchHistoryUtil.loadSearchHistory(SearchType.GOODS.ordinal());
            for (SearchHistoryItem item : goodsHistoryItem) {
                SLog.info("item[%s]", item);
                goodsSearchHistoryList.add(item.keyword);
            }
            searchHistoryItemAdapter.setNewData(goodsSearchHistoryList);

            if (goodsSearchHistoryList.size() < 1) {
                llSearchHistoryContainer.setVisibility(View.GONE);
            } else {
                llSearchHistoryContainer.setVisibility(View.VISIBLE);
            }
        } else if (searchType == SearchType.STORE) {
            storeSearchHistoryList.clear();
            List<SearchHistoryItem> storeHistoryItem = SearchHistoryUtil.loadSearchHistory(SearchType.STORE.ordinal());
            for (SearchHistoryItem item : storeHistoryItem) {
                SLog.info("item[%s]", item);
                storeSearchHistoryList.add(item.keyword);
            }
            searchHistoryItemAdapter.setNewData(storeSearchHistoryList);

            if (storeSearchHistoryList.size() < 1) {
                llSearchHistoryContainer.setVisibility(View.GONE);
            } else {
                llSearchHistoryContainer.setVisibility(View.VISIBLE);
            }
        } else if (searchType == SearchType.POST) {
            postSearchHistoryList.clear();
            List<SearchHistoryItem> postHistoryItem = SearchHistoryUtil.loadSearchHistory(SearchType.POST.ordinal());
            for (SearchHistoryItem item : postHistoryItem) {
                SLog.info("item[%s]", item);
                postSearchHistoryList.add(item.keyword);
            }
            searchHistoryItemAdapter.setNewData(postSearchHistoryList);

            if (postSearchHistoryList.size() < 1) {
                llSearchHistoryContainer.setVisibility(View.GONE);
            } else {
                llSearchHistoryContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_message:
                if (User.getUserId() > 0) {
                    Util.startFragment(MessageFragment.newInstance(true));
                } else {
                    Util.showLoginFragment(requireContext());
                }
                break;
            case R.id.btn_expand_history:
                searchHistoryPopup = (SearchHistoryPopup) new XPopup.Builder(_mActivity)
                        .atView(vwAnchor)
                      .asCustom(new SearchHistoryPopup(_mActivity, searchType, this));
                searchHistoryPopup.show();
                break;
            case R.id.ll_mask:
                llSearchSuggestionContainer.setVisibility(View.GONE);
                break;
            case R.id.btn_clear_all:
                // 清空搜索關鍵字
                etKeyword.setText("");
                break;
            case R.id.tab_btn_store:
                selectTab(TAB_INDEX_STORE, true);
                break;
            case R.id.tab_btn_goods:
                selectTab(TAB_INDEX_GOODS, true);
                break;
            case R.id.tab_btn_post:
                selectTab(TAB_INDEX_POST, true);
                break;
            default:
                break;
        }
    }

    /**
     * 選擇Tab
     * @param tabIndex
     * @param switchViewPager  是否需要切換ViewPager
     */
    private void selectTab(int tabIndex, boolean switchViewPager) {
        if (switchViewPager) {
            viewPager.setCurrentItem(tabIndex);
        }

        tabBtnArr[currTabIndex].setBackgroundColor(twBlue);
        currTabIndex = tabIndex;
        tabBtnArr[currTabIndex].setBackgroundColor(twRed);
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void loadSearchSuggestionData(String term) {
        term = term.trim();
        if (StringUtil.isEmpty(term)) {
            return;
        }

        String url = Api.PATH_SEARCH_SUGGESTION;
        EasyJSONObject params = EasyJSONObject.generate("term", term);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.isError(responseObj)) {
                    LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                    return;
                }

                try {
                    llSearchSuggestionContainer.setVisibility(View.VISIBLE);
                    llSuggestionList.removeAllViews();
                    EasyJSONArray suggestList = responseObj.getSafeArray("datas.suggestList");
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
                        itemView.setOnClickListener(v -> doSearch(item));
                        llSuggestionList.addView(itemView);
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void doSearch(String keyword) {
        currentKeyword = keyword;
        // 將keyword填充到搜索欄中，并跳轉到搜索結果頁面
        etKeyword.setText(currentKeyword);
        EditTextUtil.cursorSeekToEnd(etKeyword);

        // 如果為空，用默認關鍵詞搜索
        if (searchType == SearchType.GOODS && StringUtil.isEmpty(currentKeyword)) {
            currentKeyword = defaultKeyword;
        }

        if (StringUtil.isEmpty(currentKeyword)) {
            // ToastUtil.error(_mActivity, getString(R.string.input_search_keyword_hint));
            // return;
            currentKeyword = "";
        }

        hideSoftInput();

        SLog.info("searchType[%s]", searchType.name());
        if (searchType == SearchType.GOODS || searchType == SearchType.STORE) {
            if (AddPostGoodsFragment.class.getName().equals(from)) {
                start(AddPostSearchGoodsFragment.newInstance(keyword));
            } else {
                EasyJSONObject params = EasyJSONObject.generate("keyword", currentKeyword);

                start(SearchResultFragment.newInstance(searchType.name(), params.toString()));
            }
        } else if (searchType == SearchType.POST) {
            SearchPostParams searchPostParams = new SearchPostParams();
            searchPostParams.keyword = currentKeyword;
            start(CircleFragment.newInstance(true, searchPostParams));
        }

    }

    @Override
    public void onSimpleCall(Object data) {
        SLog.info("call() data[%s]", data.toString());
        String dataStr = (String) data;
        EasyJSONObject dataObj = EasyJSONObject.parse(dataStr);
        try {
            int action = dataObj.getInt("action");
            if (CustomAction.CUSTOM_ACTION_CLEAR_ALL_HISTORY.ordinal() == action) {
                loadSearchHistoryData();
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        loadSearchHistoryData();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();

        // hideSoftInputPop();
    }
}



