package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.CameraUtil;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.HawkUtil;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AreaPopup;
import com.ftofs.twant.widget.DateSelectPopup;
import com.ftofs.twant.widget.ImagePreviewPopup;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.orhanobut.hawk.Hawk;
import com.yalantis.ucrop.UCrop;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

import static com.facebook.FacebookSdk.getCacheDir;

/**
 * 基礎信息
 * @author zwm
 */
public class PersonalInfoFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    // 打開相冊的操作
    private static final int ACTION_SET_AVATAR = 1;
    private static final int ACTION_SET_PERSONAL_BACKGROUND = 2;

    String[] genderTextMap;
    String[] marryTextMap;

    ImageView imgAvatar;
    ImageView imgTopAvatar;

    View contentView;
    String nickname;
    int genderIndex;  // 當前選中的性別索引
    int marryIndex;
    TextView tvGender;
    TextView tvAge;
    String birthday;
    TextView tvBirthday;
    TextView tvMemberLocation;
    TextView tvMemberSignature;
    TextView tvMarriage;
    TextView tvEmail;
    String email;
    String memberSignature;

    TextView tvWechatId;
    TextView tvFacebookId;
    TextView tvMemberBio;


    ImageView imgPersonalBackground;

    /**
     * 拍照的圖片文件
     */
    File captureImageFile;

    int action; // 打開相冊的操作
    private String SAMPLE_CROPPED_IMAGE_NAME ="sample_cropped";

    public static PersonalInfoFragment newInstance() {
        Bundle args = new Bundle();

        PersonalInfoFragment fragment = new PersonalInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        genderTextMap = new String[] {
                getString(R.string.text_confidentiality), getString(R.string.text_male), getString(R.string.text_female)};
        marryTextMap= new String[]{
                getString(R.string.text_secret),
                getString(R.string.text_single),
                getString(R.string.text_married),
                getString(R.string.text_divorce),
                getString(R.string.text_others_marry_status),
        };

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_set_avatar, this);
        Util.setOnClickListener(view,R.id.btn_set_personal_background,this);
        Util.setOnClickListener(view, R.id.btn_modify_nickname, this);
        Util.setOnClickListener(view, R.id.btn_set_gender, this);
        Util.setOnClickListener(view, R.id.btn_select_birthday, this);
        Util.setOnClickListener(view,R.id.btn_select_marriage,this);
        Util.setOnClickListener(view, R.id.btn_set_wechat_id, this);
        Util.setOnClickListener(view, R.id.btn_set_facebook_id, this);
        Util.setOnClickListener(view, R.id.btn_set_personal_bio, this);
        Util.setOnClickListener(view,R.id.btn_set_mail,this);
        Util.setOnClickListener(view, R.id.btn_set_location, this);
        Util.setOnClickListener(view, R.id.btn_set_signature, this);

        imgAvatar = view.findViewById(R.id.img_avatar);
        imgTopAvatar=view.findViewById(R.id.img_top_avatar);
        tvAge=view.findViewById(R.id.tv_age);
        tvGender = view.findViewById(R.id.tv_gender);
        tvMarriage = view.findViewById(R.id.tv_marriage);
        tvEmail=view.findViewById(R.id.tv_member_mail);
        tvMemberLocation = view.findViewById(R.id.tv_member_location);
        tvMemberSignature = view.findViewById(R.id.tv_member_signature);
        imgPersonalBackground = view.findViewById(R.id.img_personal_background);

        tvWechatId = view.findViewById(R.id.tv_wechat_id);
        tvFacebookId = view.findViewById(R.id.tv_facebook_id);
        tvMemberBio = view.findViewById(R.id.tv_personal_bio);

        contentView = view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");
        loadPersonalInfo(contentView);
    }

    @Override
    public void onClick(View v) {
        try {
            int id = v.getId();

            String value;
            EasyJSONObject userDataObj;
            switch (id) {
                case R.id.btn_back:
                    hideSoftInputPop();
                    break;
                case R.id.btn_set_avatar:
                    action = ACTION_SET_AVATAR;
                    openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
                    break;
                case R.id.btn_set_personal_background:
                    action = ACTION_SET_PERSONAL_BACKGROUND;
                    selectPersonalBackground();
                    break;
                case R.id.btn_modify_mobile:
                    SLog.info("打開修改手機");
                    break;
                case R.id.btn_modify_nickname:
                    Util.startFragment(ModifyNicknameFragment.newInstance(ModifyNicknameFragment.USAGE_NICKNAME, nickname));
                    break;
                case R.id.btn_set_mail:
                    startForResult(ModifyNicknameFragment.newInstance(ModifyNicknameFragment.USAGE_EMAIL, email), RequestCode.ADD_EMAIL.ordinal());
                    break;
                case R.id.btn_set_facebook_id:
                    userDataObj = HawkUtil.getUserDataObject();
                    value = userDataObj.getSafeString(SPField.USER_DATA_KEY_FACEBOOK_ID);
                    if (StringUtil.isEmpty(value)) {
                        value = "";
                    }
                    Util.startFragment(ModifyNicknameFragment.newInstance(ModifyNicknameFragment.USAGE_FACEBOOK, value));
                    break;
                case R.id.btn_set_wechat_id:
                    userDataObj = HawkUtil.getUserDataObject();
                    value = userDataObj.getSafeString(SPField.USER_DATA_KEY_WECHAT_ID);
                    if (StringUtil.isEmpty(value)) {
                        value = "";
                    }
                    Util.startFragment(ModifyNicknameFragment.newInstance(ModifyNicknameFragment.USAGE_WECHAT, value));
                    break;
                case R.id.btn_set_personal_bio:
                    Util.startFragment(PersonalProfileFragment.newInstance());
                    break;
                case R.id.btn_set_gender:
                    List<ListPopupItem> itemList = new ArrayList<>();
                    for (int i = 0; i < genderTextMap.length; i++) {
                        ListPopupItem item = new ListPopupItem(i, genderTextMap[i], null);
                        itemList.add(item);
                    }

                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.text_select_gender),
                                    PopupType.DEFAULT, itemList, genderIndex, this))
                            .show();
                    break;
                case R.id.btn_select_birthday:
                    hideSoftInput();
                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new DateSelectPopup(_mActivity, PopupType.BIRTH_DAY, birthday, this))
                            .show();
                    break;
                case R.id.btn_select_marriage:
                    List<ListPopupItem>marryList = new ArrayList<>();
                    int i =0;
                    for(String str:marryTextMap){
                        ListPopupItem item=new ListPopupItem(i++,str,null);
                        marryList.add(item);
                    }

                    new XPopup.Builder(_mActivity)
                            .moveUpToKeyboard(false)
                            .asCustom(new ListPopup(_mActivity,getString(R.string.text_marry_status),
                                    PopupType.MARRY,marryList,marryIndex,this))
                            .show();
                    break;
                case R.id.btn_set_location:
                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new AreaPopup(_mActivity, PopupType.MEMBER_ADDRESS, this))
                            .show();
                    break;
                case R.id.btn_set_signature:
                    Util.startFragment(EditMemberSignatureFragment.newInstance(memberSignature));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void loadPersonalInfo(final View view) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_MEMBER_DETAIL, params, new UICallback() {
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

                    EasyJSONObject memberInfo = responseObj.getSafeObject("datas.memberInfo");
                    String avatarUrl = memberInfo.getSafeString("avatar");
                    if (!StringUtil.isEmpty(avatarUrl)) {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAvatar);
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgTopAvatar);
                    }else {
                        Glide.with(_mActivity).load(R.drawable.icon_default_avatar).centerCrop().into(imgAvatar);
                        Glide.with(_mActivity).load(R.drawable.icon_default_avatar).centerCrop().into(imgTopAvatar);
                    }
                    setPersonalBackground();

                    TextView mobile = view.findViewById(R.id.tv_member_mobile);
                    String mobileAreaCode = memberInfo.getSafeString("mobileAreaCode");
                    mobile.setText(mobileAreaCode+memberInfo.getSafeString("mobile"));
                    mobile = view.findViewById(R.id.tv_top_mobile);
                    mobile.setText(memberInfo.getSafeString("mobile"));
                    TextView tvName =view.findViewById(R.id.tv_member_name);
                    tvName.setText(memberInfo.getSafeString("nickName"));

                    TextView tvNickname = view.findViewById(R.id.tv_nickname);
                    nickname = memberInfo.getSafeString("nickName");
                    tvNickname.setText(nickname);

                    int gender = memberInfo.getInt("memberSex");
                    genderIndex = gender;
                    tvGender.setText(genderTextMap[gender]);

                    birthday = memberInfo.getSafeString("birthday");
                    SLog.info(birthday);
                    if (!StringUtil.isEmpty(birthday)) {
                        tvBirthday = view.findViewById(R.id.tv_birthday);
                        tvBirthday.setText(birthday);
                    }
                    SLog.info("memberInfo[%s]",memberInfo.toString());
                    TextView tvAge=view.findViewById(R.id.tv_age);
                    int memberAge = memberInfo.getInt("memberAge");
                    SLog.info("__memberAge[%d]", memberAge);
                    if (memberAge > 0) { // 如果服務器傳過來的年齡有效，則用服務器的年齡
                        tvAge.setText(String.valueOf(memberAge));
                    } else { // 否則,根據生日計算年齡
                        if (StringUtil.isEmpty(birthday)) {
                            tvAge.setText("NA"); // 沒有設置生日
                        } else {
                            Jarbon jarbon = new Jarbon();
                            int birthdayYear = Integer.valueOf(birthday.substring(0, 4));
                            int age = jarbon.getYear() - birthdayYear;

                            // 算周歲，如果不足一年，還要減1歲
                            String birthdayDate = birthday.substring(5, 10);
                            String nowDate = jarbon.format("m-d");
                            SLog.info("birthdayDate[%s], nowDate[%s]", birthdayDate, nowDate);
                            if (nowDate.compareTo(birthdayDate) < 0) {
                                --age;
                            }

                            tvAge.setText(String.valueOf(age));
                        }
                    }

                    TextView tvMarriage = view.findViewById(R.id.tv_marriage);
                    marryIndex =memberInfo.getInt("memberMarriage");
                    tvMarriage.setText(marryTextMap[marryIndex]);

                    String location = memberInfo.getSafeString("addressAreaInfo");
                    if (!StringUtil.isEmpty(location)) {
                        tvMemberLocation.setText(location);
                    }

                    memberSignature = memberInfo.getSafeString("memberSignature");
                    if (!StringUtil.isEmpty(memberSignature)) {
                        tvMemberSignature.setText(memberSignature);
                    } else {
                        memberSignature = "";
                    }

                    // 從Hawk中獲取用戶數據
                    EasyJSONObject userDataObj = HawkUtil.getUserDataObject();
                    String value = memberInfo.getSafeString("wechat");
                    if (!StringUtil.isEmpty(value)) {
                        tvWechatId.setText(value);
                    }
                    userDataObj.set(SPField.USER_DATA_KEY_WECHAT_ID, value);

                    value = memberInfo.getSafeString("facebook");
                    if (!StringUtil.isEmpty(value)) {
                        tvFacebookId.setText(value);
                    }
                    userDataObj.set(SPField.USER_DATA_KEY_FACEBOOK_ID, value);

                    value = memberInfo.getSafeString("email");
                    if (!StringUtil.isEmpty(value)) {
                        tvEmail.setText(value);
                    }
                    userDataObj.set(SPField.USER_DATA_KEY_EMAIL, value);

                    value = memberInfo.getSafeString("memberBio");
                    if (!StringUtil.isEmpty(value)) {
                        if (value.length() > 10) {  // 最多顯示10個字符
                            tvMemberBio.setText(value.substring(0, 10) + "⋯");
                        } else {
                            tvMemberBio.setText(value);
                        }
                        SLog.info("memberBio %s",value);
                    }
                    userDataObj.set(SPField.USER_DATA_KEY_BIO, value);

                    // 用從服務器端獲取的用戶數據更新Hawk中緩存的用戶數據
                    SLog.info("userDataObj[%s]", userDataObj.toString());
                    Hawk.put(SPField.FIELD_USER_DATA, userDataObj.toString());
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onSelected(PopupType type, final int id, Object extra) {
        SLog.info("type[%s]", type);

        if (type == PopupType.DEFAULT) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "memberSex", id);
            Api.postUI(Api.PATH_SET_GENDER, params, new UICallback() {
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

                    genderIndex = id;
                    tvGender.setText(genderTextMap[genderIndex]);
                }
            });
        }
        else if (type == PopupType.BIRTH_DAY) {
            SLog.info("extra[%s]", extra.toString());
            int currYear= Calendar.getInstance().get(Calendar.YEAR);
            int birthYear=Integer.parseInt(extra.toString().substring(0,4));
            int age=currYear-birthYear;
            SLog.info("[%d][%d]",currYear,birthYear);
            String birthday = (String) extra;
            tvBirthday.setText(birthday);
            if (age > 0) {
                tvAge.setText(String.valueOf(age));
            }
        } else if (type == PopupType.MEMBER_ADDRESS) {
            SLog.info("extra[%s]", extra);
            String location = (String) extra;
            tvMemberLocation.setText(location);
        } else if (type == PopupType.MARRY) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "marriage", id);
            SLog.info("params[%s]",params.toString());
            Api.postUI(Api.PATH_SET_MARRIAGE, params, new UICallback() {
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

                    marryIndex = id;
                    tvMarriage.setText(marryTextMap[id]);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SLog.info("onActivityResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }


        // 設置頭像
        if (action == ACTION_SET_AVATAR) {
            if (requestCode == RequestCode.OPEN_ALBUM.ordinal()) {
                Uri uri = data.getData();
                startCrop(uri);
//
            }
        } else if (action == ACTION_SET_PERSONAL_BACKGROUND) { // 設置個人專頁背景圖
            if (requestCode == RequestCode.OPEN_ALBUM.ordinal() || requestCode == RequestCode.CAMERA_IMAGE.ordinal()) {
                String absolutePath;
                if (requestCode == RequestCode.OPEN_ALBUM.ordinal()) {
                    Uri uri = data.getData();
                    absolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
                } else {
                    absolutePath = captureImageFile.getAbsolutePath();  // 拍照得到的文件路徑
                }
                SLog.info("absolutePath[%s]", absolutePath);

                TaskObserver taskObserver = new TaskObserver() {
                    @Override
                    public void onMessage() {
                        String imageUrl = (String) message;

                        if (imageUrl == null) {
                            ToastUtil.error(_mActivity, "上傳圖片失敗");
                            return;
                        }

                        // 上傳成功
                        Hawk.put(SPField.FIELD_PERSONAL_BACKGROUND, imageUrl);
                        setPersonalBackground();
                        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_CHANGE_PERSONAL_BACKGROUND, null);
                        ToastUtil.success(_mActivity, "更改成功");
                    }
                };

                TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
                    @Override
                    public Object doWork() {
                        File file = new File(absolutePath);
                        String imageUrl = Api.syncUploadFile(file);
                        String token = User.getToken();

                        if (StringUtil.isEmpty(token)) {
                            return null;
                        }

                        EasyJSONObject params = EasyJSONObject.generate(
                                "token", token,
                                "imageUrl", imageUrl);
                        String responseStr = Api.syncPost(Api.PATH_CHANGE_PERSONAL_BACKGROUND, params);
                        if (StringUtil.isEmpty(responseStr)) {
                            return null;
                        }
                        return imageUrl;
                    }
                });
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_UPLOAD_FILE_SUCCESS) {
            String avatarUrl = (String) message.data;
            SLog.info("avatarUrl[%s]", avatarUrl);
            setUserAvatar(avatarUrl);
        }
    }

    private void setUserAvatar(final String url) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "avatar", url
        );

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_SET_AVATAR, params, new UICallback() {
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

                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_REFRESH_DATA, null);

                ToastUtil.success(_mActivity, "設置頭像成功");
                Hawk.put(SPField.FIELD_AVATAR, url);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_CHANGE_MEMBER_AVATAR, null);
                Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(url))
                        .centerCrop().into(imgAvatar);
            }
        });
    }
    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        destinationFileName += ".png";


        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        uCrop.withAspectRatio(16, 16)
                .withMaxResultSize(250, 250)
                .withOptions(options)
                .start(_mActivity);

//        uCrop = basisConfig(uCrop);
//        uCrop = advancedConfig(uCrop);
//
//        if (requestMode == REQUEST_SELECT_PICTURE_FOR_FRAGMENT) {       //if build variant = fragment
//            setupFragment(uCrop);
//        } else {                                                        // else start uCrop Activity
//            uCrop.start(SampleActivity.this);
//        }

    }
    private void selectPersonalBackground() {
        new XPopup.Builder(getContext())
//                        .maxWidth(600)
                .asCenterList("请选择", new String[]{"從手機相冊選擇", "從相機拍攝"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                changePersonalBackground(position);
                            }
                        })
                .show();
    }

    private void changePersonalBackground(int select) {
        SLog.info("select[%d]", select);
        if (select == 0) {
            openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
        } else if (select == 1) {
            PermissionUtil.actionWithPermission(_mActivity, new String[] {
                    Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE
            }, "拍攝照片/視頻需要授予", new CommonCallback() {
                @Override
                public String onSuccess(@Nullable String data) {
                    captureImageFile = CameraUtil.openCamera(_mActivity, PersonalInfoFragment.this, Constant.CAMERA_ACTION_IMAGE);
                    return null;
                }

                @Override
                public String onFailure(@Nullable String data) {
                    ToastUtil.error(_mActivity, "您拒絕了授權");
                    return null;
                }
            });
        }
    }

    private void setPersonalBackground() {
        // 是否使用默認背景圖
        String personalBackgroundUrl = Hawk.get(SPField.FIELD_PERSONAL_BACKGROUND, null);

        if (StringUtil.isEmpty(personalBackgroundUrl)) {
            Glide.with(_mActivity).load(R.drawable.my_fragment_header_bg).centerCrop().into(imgPersonalBackground);
        } else {
            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(personalBackgroundUrl)).centerCrop().into(imgPersonalBackground);
        }
    }
}
