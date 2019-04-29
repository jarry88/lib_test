package com.ftofs.twant.api;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 獲取OkHttp的異步結果后，切換到UI線程中執行的回調
 * @author zwm
 */
public abstract class UICallback implements Runnable {
    /**
     * 需要實現的失敗處理方法
     * @param call
     * @param e
     */
    public abstract void onFailure(Call call, IOException e);

    /**
     * 需要實現的成功處理方法
     * @param call
     * @param response
     * @throws IOException
     */
    public abstract void onResponse(Call call, Response response) throws IOException;


    /**
     * OkHttp的執行結果的常量定義
     */
    public static final int RESULT_UNKNOWN = 0;
    public static final int RESULT_FAILURE = 1;
    public static final int RESULT_RESPONSE = 2;

    /**
     * OkHttp的執行結果
     */
    private int result = RESULT_UNKNOWN;

    private Call call;
    private IOException ioException;
    private Response response;

    /**
     * OkHttp失敗時調用
     * @param call
     * @param ioException
     */
    public void setOnFailure(Call call, IOException ioException) {
        result = RESULT_FAILURE;
        this.call = call;
        this.ioException = ioException;
    }

    /**
     * OkHttp成功時調用
     * @param call
     * @param response
     */
    public void setOnResponse(Call call, Response response) {
        result = RESULT_RESPONSE;
        this.call = call;
        this.response = response;
    }

    @Override
    public void run() {
        if (result == RESULT_FAILURE) {
            // 失敗時回調onFailure
            onFailure(call, ioException);
        } else if (result == RESULT_RESPONSE) {
            try {
                // 成功時回調onResponse
                onResponse(call, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
