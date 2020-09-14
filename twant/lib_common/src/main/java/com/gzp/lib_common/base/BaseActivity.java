package com.gzp.lib_common.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gzp.lib_common.R;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.message.PushAgent;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * 所有Activity的公共基類
 * @author zwm
 * @create 2019-04-28
 */
public class BaseActivity extends SupportActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//    smartRefresh 控件提示語 繁體字處理
        initRefreshText();
//        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = getString(R.string.quanbujiazhaiwancheng);
        // 全部activity禁用横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        PushAgent.getInstance(this).onAppStart();
    }

    private void initRefreshText() {
        ClassicsHeader.REFRESH_HEADER_REFRESHING = getString(R.string.zhengzaishuaxin);
        ClassicsHeader.REFRESH_HEADER_LOADING = getString(R.string.zhengzaijiazhai);
        ClassicsHeader.REFRESH_HEADER_RELEASE = getString(R.string.shifangshuaxin);
        ClassicsHeader.REFRESH_HEADER_FINISH = getString(R.string.shuanxinwancheng);
        ClassicsHeader.REFRESH_HEADER_FAILED = getString(R.string.shuaxinshibai);
        ClassicsHeader.REFRESH_HEADER_UPDATE = getString(R.string.shangcigengxin);

        ClassicsFooter.REFRESH_FOOTER_RELEASE = getString(R.string.shifanglijijiazhai);
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = getString(R.string.zhengzaishuaxin);
        ClassicsFooter.REFRESH_FOOTER_LOADING = getString(R.string.zhengzaijiazhai);
        ClassicsFooter.REFRESH_FOOTER_FINISH = getString(R.string.jiazhaiwancheng);
        ClassicsFooter.REFRESH_FOOTER_FAILED = getString(R.string.jiazhaoshibai);
    }
}
