package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.core.BottomPopupView;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 申請添加好友Popup
 * @author zwm
 */
public class InviteAddFriendPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    EditText etRemark;

    // 將要添加的好友的memberName
    String memberName;

    public InviteAddFriendPopup(@NonNull Context context, String memberName) {
        super(context);

        this.context = context;
        this.memberName = memberName;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.invite_add_friend_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        etRemark = findViewById(R.id.et_remark);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        findViewById(R.id.btn_send_invitation).setOnClickListener(this);
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
        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (id == R.id.btn_send_invitation) {
            dismiss();

            String remark = etRemark.getText().toString().trim();
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            String url = Api.PATH_ADD_FRIEND_APPLICATION;
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "toMember", memberName,
                    "notes", remark
            );

            SLog.info("params[%s]", params.toString());
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

                        ToastUtil.success(context, "發送成功");
                        dismiss();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        }
    }

//    @Override
//    protected int getMaxHeight() {
//        return (int) (XPopupUtils.getWindowHeight(getContext())*0.75);
//    }
}
