package com.ftofs.twant.util;

import android.os.Build;

import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.log.SLog;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.ftofs.twant.config.Config.API_BASE_URL;

public class LogUtil {
    public static void uploadAppLog(String apiPath, String params, String response, String exceptionMessage) {
        uploadAppLog(apiPath, params, response, -1, exceptionMessage);
    }

    public static void uploadAppLog(String apiPath, String params, String response, int exceptionCode, String exceptionMessage) {
        if (!StringUtil.isUrlString(apiPath)) { // 如果不是全路径URL地址，全补全
            apiPath = API_BASE_URL + apiPath;
        }

        EasyJSONObject request = EasyJSONObject.generate(
                "c", "AppLog",
                "a", "upload",
                "device_manufacturer", Build.MANUFACTURER,
                "device_brand", Build.BRAND,
                "device_model", Build.MODEL,
                "device_os_version", Build.VERSION.RELEASE,
                "app_version", BuildConfig.VERSION_NAME,
                "app_build", BuildConfig.VERSION_CODE,
                "api_path", apiPath,
                "params", params,
                "response", response,
                "exception_code", exceptionCode,
                "exception_message", exceptionMessage
        );
        SLog.info("request[%s]", request);
        Api.postIO("http://108.61.246.21/twant_log_tracker/api.php", request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SLog.info("Error!uploadAppLog failed, message[%s]", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
