package com.ftofs.twant.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 實名認證彈窗
 * @author zwm
 */
public class RealNamePopup extends BottomPopupView implements View.OnClickListener {
    private final String defaultRealName;
    Context context;
    EditText etName; // 姓名
    EditText etId; // 身份證號

    View btnClearName;
    View btnClearId;

    public RealNamePopup(@NonNull Context context,String realName) {
        super(context);

        this.context = context;
        this.defaultRealName = realName;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.real_name_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        btnClearName = findViewById(R.id.btn_clear_name);
        btnClearName.setOnClickListener(this);
        btnClearId = findViewById(R.id.btn_clear_id);
        btnClearId.setOnClickListener(this);

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        findViewById(R.id.btn_view_real_name_prompt).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);

        etName = findViewById(R.id.et_name);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btnClearName.setVisibility(VISIBLE);
                } else {
                    btnClearName.setVisibility(GONE);
                }
            }
        });
        etName.setText(defaultRealName);
        EditTextUtil.cursorSeekToEnd(etName);
        etId = findViewById(R.id.et_id);
        etId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btnClearId.setVisibility(VISIBLE);
                } else {
                    btnClearId.setVisibility(GONE);
                }
            }
        });
        etId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    commitData();
                }

                return false;
            }
        });
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (id == R.id.btn_commit) {
            commitData();
        } else if (id == R.id.btn_clear_name) {
            etName.setText("");
        } else if (id == R.id.btn_clear_id) {
            etId.setText("");
        } else if (id == R.id.btn_view_real_name_prompt) {
            new XPopup.Builder(context)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new RealNameInstructionPopup(context))
                    .show();
        }
    }


    private void commitData() {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            String name = etName.getText().toString().trim();
            String idNum = etId.getText().toString().trim();

            if (StringUtil.isEmpty(name)) {
                ToastUtil.error(context, etName.getHint().toString());
                return;
            }

            if (StringUtil.isEmpty(idNum)) {
                ToastUtil.error(context, etId.getHint().toString());
                return;
            }

            if (!defaultRealName.equals(name)) {
                etName.requestFocus();
                ToastUtil.error(context, "實名認證的姓名與收貨人姓名不一致");
                return;
            }

            String url = Api.PATH_SAVE_REAL_NAME_INFO;
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "consigneeName", name,
                    "idCartNumber", idNum
            );

            SLog.info("url[%s], params[%s]", url, params);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(context, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(context, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            return;
                        }

                        ToastUtil.success(context, "保存成功");

                        RealNamePopup.this.dismiss();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}
