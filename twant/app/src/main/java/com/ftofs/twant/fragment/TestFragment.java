package com.ftofs.twant.fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.ftofs.twant.BlankFragment;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.login.UserManager;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.member.MemberVo;
import com.lxj.xpopup.core.BasePopupView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    String token;
    EditText etToken;
    EditText etUrl;
    EditText etUrlWithToken;


    BasePopupView loadPopup;


    TextView textView;
    private H5GameFragment h5;

    ImageView iconActivityEntrance;
    private NotificationManager mNotificationManager;

    ImageView imgTest;

    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);
        mNotificationManager = (NotificationManager) _mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChanel();


        Util.setOnClickListener(view,R.id.tv_28,this);
        Util.setOnClickListener(view,R.id.tv_29,this);
        Util.setOnClickListener(view,R.id.tv_prod,this);
        Util.setOnClickListener(view,R.id.btn_shoppingSession,this);
        Util.setOnClickListener(view,R.id.test_im,this);

//        token = User.getToken();
//        etToken = view.findViewById(R.id.et_token);
//        if (StringUtil.isEmpty(token)) {
//            etToken.setText("用户未登录");
//        } else {
//            etToken.setText(token);
//        }


        etUrl = view.findViewById(R.id.et_url);
        etUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String url = s.toString();

                if (!StringUtil.isEmpty(token) &&
                        !url.contains("token=")) { // 如果编辑框本身已经带token参数，则不处理
                    
                    if (!url.contains("?")) {
                        url += "?";
                    }

                    if (!url.endsWith("?") && !url.endsWith("&")) {
                        url += "&";
                    }
                    url += "token=" + token;
                }

                etUrlWithToken.setText(url);
            }
        });
        etUrlWithToken = view.findViewById(R.id.et_url_with_token);

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_test2, this);
        Util.setOnClickListener(view, R.id.btn_open_url, this);
        Util.setOnClickListener(view, R.id.btn_open_url_with_token, this);

        Util.setOnClickListener(view, R.id.btn_copy_token, this);
        Util.setOnClickListener(view, R.id.btn_clear_cache, this);

        iconActivityEntrance = view.findViewById(R.id.icon_activity_entrance);
        Glide.with(_mActivity).load("https://gfile.oss-cn-hangzhou.aliyuncs.com/takewant/7d78744d5d1dffc96022bba47123b0a8.png")
                .into(iconActivityEntrance);

        imgTest = view.findViewById(R.id.img_test);
        String url = "https://gfile.oss-cn-hangzhou.aliyuncs.com/img/0159525979bab9a8012193a329c12d.jpg";
        url = "https://gfile.oss-cn-hangzhou.aliyuncs.com/img/0159525979bab9a8012193a329c12d.jpg";
        // url = "https://img.twant.com/image/90/18/901806d394a2f133a2d5d897f04c9d7a.jpg";
        url = "https://gfile.oss-cn-hangzhou.aliyuncs.com/img/v2-e2aa8d9c5237173c11954db9cc8b9c5b_1200x500.jpg";
        Glide.with(_mActivity).load(url)
                .into(imgTest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void parseUrl(String origUrl) {
        EasyJSONObject params = EasyJSONObject.generate("url", origUrl);
        Api.postUI(Api.PATH_PARSE_URL, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    String action = responseObj.getSafeString("datas.action");
                    if ("store".equals(action)) {
                        int storeId = responseObj.getInt("datas.storeId");
                        Util.startFragment(ShopMainFragment.newInstance(storeId));
                    } else if ("goods".equals(action)) {
                        String commonIdStr = responseObj.getSafeString("datas.commonId");
                        int commonId = Integer.valueOf(commonIdStr);
                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                    } else if ("postinfo".equals(action)) {
                        String postIdStr = responseObj.getSafeString("datas.postId");
                        int postId = Integer.valueOf(postIdStr);
                        Util.startFragment(PostDetailFragment.newInstance(postId));
                    } else if ("weburl".equals(action)) {
                        String url = responseObj.getSafeString("datas.url");
                        Util.startFragment(H5GameFragment.newInstance(url, ""));
                    }
                } catch (Exception e) {

                }

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_WEIXIN_LOGIN) {
            String code = (String) message.data;
            SLog.info("code[%s]", code);
        }
    }

    private void showLoadingPopup() {
        if (loadPopup == null) {
            loadPopup = Util.createLoadingPopup(_mActivity);
        }
        loadPopup.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String url;
        if (id == R.id.btn_test) {
            SLog.info("onClick()");
            showLoadingPopup();
            start(BlankFragment.newInstance("",""));
            loadPopup.dismiss();
            UserManager.INSTANCE.removeUser();
        } else if (id == R.id.btn_test2) {
            Util.popToMainFragment(_mActivity);
        } else if (id == R.id.test_im) {
            ApiUtil.getImInfo(_mActivity,User.getUserInfo(SPField.FIELD_MEMBER_NAME,null),(memberVo)->{
                TwantApplication.Companion.get().setMemberVo((MemberVo) memberVo);
            });
//            testNotification();
//            ToastUtil.success(_mActivity, String.format("%d條會話",EMClient.getInstance().chatManager().getAllConversations().size()));
//            Util.startFragment(ShoppingSessionFragment.newInstance());
        } else if (id == R.id.btn_shoppingSession) {
            Util.startFragment(ShoppingSessionFragment.newInstance());
        } else if (id == R.id.btn_open_url || id == R.id.btn_open_url_with_token) {
            if (id == R.id.btn_open_url) {
                url = etUrl.getText().toString().trim();
            } else {
                url = etUrlWithToken.getText().toString().trim();
            }

            SLog.info("打开URL[%s]", url);
            if (StringUtil.isUrlString(url)) {
                if (h5 == null) {
                    h5 = H5GameFragment.newInstance(url, true, true, null, H5GameFragment.ARTICLE_ID_INVALID);
                }
                Util.startFragment(h5);
            } else {
                ToastUtil.error(_mActivity, "不是有效的URL");
            }
        } else if (id == R.id.btn_copy_token) {
            String text = etToken.getText().toString();
            ClipboardUtils.copyText(_mActivity, text);
            ToastUtil.success(_mActivity, "复制Token成功");
        } else if (id == R.id.btn_clear_cache) {
            if (h5 != null) {
                SLog.info("清空浏览器缓存成功");
                h5.clearCache();
            }
            ToastUtil.success(_mActivity, "清空浏览器缓存成功");
        } else if (id == R.id.tv_28) {
            User.logout();
            Config.changeEnvironment(Config.ENV_28);
            ToastUtil.success(_mActivity, "切換至28");
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.tv_29) {
            User.logout();
            ToastUtil.success(_mActivity, "切換至29");
            Config.changeEnvironment(Config.ENV_29);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }  else if (id == R.id.tv_229) {
            User.logout();
            ToastUtil.success(_mActivity, "切換至229");
            Config.changeEnvironment(Config.ENV_229);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.tv_prod) {
            User.logout();
            ToastUtil.success(_mActivity, "切換至prod");
            Config.changeEnvironment(Config.ENV_PROD);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }
    private void createNotificationChanel() {
        String channelId = "upgrade";
        CharSequence channelName = "upgrade";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.tw_red);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void testNotification() {

        Notification notification = new NotificationCompat.Builder(_mActivity, "upgrade")
                .setContentTitle("升级")
                .setContentText("程序员终于下班了。。")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .build();
        mNotificationManager.notify(100, notification);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SLog.info("onActivityResult......................");

        if (requestCode == RequestCode.SELECT_MULTI_IMAGE.ordinal() && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);

            if (images == null) {
                return;
            }

            SLog.info("images.size[%d]", images.size());
            for (String item : images) {
                File file = new File(item);

                SLog.info("item[%s], size[%d]", item, file.length());
            }

            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
            SLog.info("isCameraImage[%s]", isCameraImage);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
