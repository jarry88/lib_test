package com.ftofs.twant.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.ftofs.twant.log.SLog;

import java.io.InputStream;

// 參考 Glide4.8集成现有OkHttpClient并加载https图片 https://blog.csdn.net/ysy950803/article/details/85083160
// 注意这个注解一定要加上，HttpGlideModule是自定义的名字
@GlideModule
public final class HttpGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        // 注意这里用我们刚才现有的Client实例传入即可
        SLog.info("registerComponents");
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(Api.getOkHttpClient()));
    }
}

