package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ftofs.twant.R;
import com.lxj.xpopup.impl.PartShadowPopupView;

public class SearchSuggestionPopup extends PartShadowPopupView {
    public SearchSuggestionPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.search_suggestion_popup;
    }
}
