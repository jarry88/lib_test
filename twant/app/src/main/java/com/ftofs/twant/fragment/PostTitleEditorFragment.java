package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

/**
 * Post的標題編輯頁面
 * Post的關鍵字編輯也是用這個
 * @author zwm
 */
public class PostTitleEditorFragment extends BaseFragment implements View.OnClickListener {
    public static final int FOR_TITLE = 1;
    public static final int FOR_KEYWORD = 2;

    public static final int MAX_TITLE_LENGTH = 60;
    public static final int MAX_KEYWORD_LENGTH = 50;

    int forWhich;

    String content;
    TextView tvTitle;
    TextView tvWordCount;
    EditText etContent;

    /**
     * 工廠方法
     * @param forWhich
     * @param content 初始內容
     * @return
     */
    public static PostTitleEditorFragment newInstance(int forWhich, String content) {
        Bundle args = new Bundle();

        args.putInt("forWhich", forWhich);
        args.putString("content", content);
        PostTitleEditorFragment fragment = new PostTitleEditorFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_title_editor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        forWhich = args.getInt("forWhich");
        content = args.getString("content");

        tvTitle = view.findViewById(R.id.tv_title);
        tvWordCount = view.findViewById(R.id.tv_word_count);
        etContent = view.findViewById(R.id.et_content);
        etContent.setText(content);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateWordCount(s.length());
            }
        });

        if (forWhich == FOR_TITLE) {
            tvTitle.setText(getResources().getString(R.string.text_title));
        } else {
            tvTitle.setText(getResources().getString(R.string.text_keyword));
        }
        updateWordCount(content.length());

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        // 彈出軟鍵盤
        EditTextUtil.cursorSeekToEnd(etContent);
        showSoftInput(etContent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_ok) {
            Bundle bundle = new Bundle();

            content = etContent.getText().toString().trim();
            if (content.length() < 1) {
                ToastUtil.show(_mActivity, "內容不能為空");
                return;
            }

            bundle.putString("content", etContent.getText().toString());
            setFragmentResult(RESULT_OK, bundle);

            pop();
        }
    }

    private void updateWordCount(int wordCount) {
        int maxWordCount = (forWhich == FOR_TITLE ? MAX_TITLE_LENGTH : MAX_KEYWORD_LENGTH);
        tvWordCount.setText(String.format("%d/%d", wordCount, maxWordCount));
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
