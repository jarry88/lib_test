package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import am.widget.smoothinputlayout.SmoothInputLayout;

/**
 * 聊天會話Fragment
 * @author zwm
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener,
        View.OnTouchListener, TextWatcher, SmoothInputLayout.OnVisibilityChangeListener {

    SmoothInputLayout silMainContainer;
    EditText etMessage;
    View btnEmoji;
    ImageView iconEmoji;
    View btnTool;
    View llEmojiPane;
    View llToolPane;

    public static ChatFragment newInstance() {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        silMainContainer = view.findViewById(R.id.sil_main_container);
        etMessage = view.findViewById(R.id.et_message);
        btnEmoji = view.findViewById(R.id.btn_emoji);
        iconEmoji = view.findViewById(R.id.icon_emoji);
        btnTool = view.findViewById(R.id.btn_tool);
        llEmojiPane = view.findViewById(R.id.ll_emoji_pane);
        llToolPane = view.findViewById(R.id.ll_tool_pane);
        silMainContainer.setOnVisibilityChangeListener(this);
        etMessage.addTextChangedListener(this);
        btnEmoji.setOnClickListener(this);
        btnTool.setOnClickListener(this);
        etMessage.setOnTouchListener(this);
        view.findViewById(R.id.rv_message_list).setOnTouchListener(this);

        Util.setOnClickListener(view, R.id.btn_back, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_emoji:
                btnTool.setSelected(false);
                if (btnEmoji.isSelected()) {
                    btnEmoji.setSelected(false);
                    showInput();
                } else {
                    btnEmoji.setSelected(true);
                    showEmoji();
                }
                break;
            case R.id.btn_tool:
                btnEmoji.setSelected(false);
                if (btnTool.isSelected()) {
                    btnTool.setSelected(false);
                    showInput();
                } else {
                    btnTool.setSelected(true);
                    showTool();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示输入面板
     */
    private void showInput() {
        silMainContainer.showKeyboard();
        iconEmoji.setImageResource(R.drawable.icon_emoji);
        afterTextChanged(etMessage.getText());
    }

    /**
     *  显示Emoji面板
     */
    private void showEmoji() {
        llEmojiPane.setVisibility(View.VISIBLE);
        llToolPane.setVisibility(View.GONE);
        iconEmoji.setImageResource(R.drawable.icon_keyboard);
        silMainContainer.showInputPane(true);
    }

    /**
     * 显示Tool面板
     */
    private void showTool() {
        llEmojiPane.setVisibility(View.GONE);
        llToolPane.setVisibility(View.VISIBLE);
        iconEmoji.setImageResource(R.drawable.icon_emoji);
        silMainContainer.showInputPane(false);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().trim().length() > 0) {
            // 显示发送按钮
        } else {
            // 隐藏发送按钮
        }
    }

    @Override
    public void onVisibilityChange(int visibility) {
        if (visibility == View.GONE) {
            btnEmoji.setSelected(false);
        } else {
            btnEmoji.setSelected(llEmojiPane.getVisibility() == View.VISIBLE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.rv_message_list:
                btnEmoji.setSelected(false);
                btnTool.setSelected(false);
                silMainContainer.closeKeyboard(true);
                silMainContainer.closeInputPane();
                break;
            case R.id.et_message:
                btnEmoji.setSelected(false);
                btnTool.setSelected(false);
                iconEmoji.setImageResource(R.drawable.icon_emoji);
                afterTextChanged(etMessage.getText());
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (silMainContainer.isInputPaneOpen()) {
            silMainContainer.closeInputPane();
            iconEmoji.setImageResource(R.drawable.icon_emoji);
            return true;
        }
        pop();
        return true;
    }
}
