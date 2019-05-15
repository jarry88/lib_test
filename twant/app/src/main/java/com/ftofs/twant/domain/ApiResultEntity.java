package com.ftofs.twant.domain;

import java.util.HashMap;

public class ApiResultEntity {
    public static final int SUCCESS = 200;
    public static final int FAIL = 400;
    public static final int NOAUTH = 401;
    public static final int UNOPENED = 412;

    private int code;
    private HashMap<String, Object> datas;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HashMap<String, Object> getDatas() {
        return datas;
    }

    public void setDatas(HashMap<String, Object> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "ApiResultEntity{" +
                "code=" + code +
                ", datas=" + datas +
                '}';
    }
}
