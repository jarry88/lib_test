package com.ftofs.twant.util;

import android.widget.EditText;

public class EditTextUtil {
    /**
     * 將EditText的光標挪到最后
     * @param editText
     */
    public static void cursorSeekToEnd(EditText editText) {
        int len = editText.getText().length();
        editText.setSelection(len);
    }

    /**
     * 將光標挪到最開始處
     * @param editText
     */
    public static void cursorSeekToStart(EditText editText) {
        editText.setSelection(0);
    }
}
