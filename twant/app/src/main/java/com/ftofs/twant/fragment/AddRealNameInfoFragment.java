package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.RealNameListItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.RealNameInstructionPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 添加實名認證信息Fragment
 * @author zwm
 */
public class AddRealNameInfoFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 動作： 添加還是編輯
     */
    int action;
    RealNameListItem realNameItem;

    EditText etName; // 姓名
    EditText etId; // 身份證號

    TextView tvFragmentTitle;

    /**
     * 構造Ctor
     * @param action
     * @param realNameItem 編輯時才用到，添加時傳null即可
     * @return
     */
    public static AddRealNameInfoFragment newInstance(int action, RealNameListItem realNameItem) {
        Bundle args = new Bundle();

        AddRealNameInfoFragment fragment = new AddRealNameInfoFragment();
        fragment.setArguments(args);
        fragment.setData(action, realNameItem);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_real_name_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_view_real_name_prompt, this);
        Util.setOnClickListener(view, R.id.btn_clear_name, this);
        Util.setOnClickListener(view, R.id.btn_clear_id, this);

        etName = view.findViewById(R.id.et_name);
        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    view.findViewById(R.id.btn_clear_name).setVisibility(v.getText().length() > 0?View.VISIBLE:View.GONE);
                }
                return false;
            }
        });
        etId = view.findViewById(R.id.et_id);
        etId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    view.findViewById(R.id.btn_clear_id).setVisibility(v.getText().length() > 0?View.VISIBLE:View.GONE);
                    commitData();
                }

                return false;
            }
        });


        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        if (action == Constant.ACTION_ADD) {
            tvFragmentTitle.setText("信息添加");
        } else {
            tvFragmentTitle.setText("信息編輯");
            etName.setText(realNameItem.name);
            etId.setText(""); // 如果是編輯，則清空身份證

            // 設置身份證為只讀狀態
            // etId.setCursorVisible(false);      //设置输入框中的光标不可见
            // etId.setFocusable(false);           //无焦点
            // etId.setFocusableInTouchMode(false);     //触摸时也得不到焦点
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
                ToastUtil.error(_mActivity, etName.getHint().toString());
                return;
            }

            if (StringUtil.isEmpty(idNum)) {
                ToastUtil.error(_mActivity, etId.getHint().toString());
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "consigneeName", name,
                    "idCartNumber", idNum
            );

            String url;
            if (action == Constant.ACTION_ADD) {
                url = Api.PATH_SAVE_REAL_NAME_INFO;
            } else {
                url = Api.PATH_EDIT_REAL_NAME_INFO;
                params.set("authId", realNameItem.authId);
            }

            SLog.info("url[%s], params[%s]", url, params);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }
                        if (responseObj.exists("datas.isAuth")) {
                            int isAuth = responseObj.getInt("datas.isAuth");
                            if (isAuth == 1) {
                                ToastUtil.success(_mActivity, "datas.message");
                                return;
                            }
                        }
                        ToastUtil.success(_mActivity, "保存成功");

                        Bundle bundle = new Bundle();
                        bundle.putBoolean("reloadData", true);
                        setFragmentResult(RESULT_OK, bundle);

                        hideSoftInputPop();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            commitData();
        } else if (id == R.id.btn_clear_name) {
            etName.setText("");
        } else if (id == R.id.btn_clear_id) {
            etId.setText("");
        } else if (id == R.id.btn_view_real_name_prompt) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new RealNameInstructionPopup(_mActivity))
                    .show();
        }
    }

    public void setData(int action, RealNameListItem realNameItem) {
        this.action = action;
        this.realNameItem = realNameItem;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
