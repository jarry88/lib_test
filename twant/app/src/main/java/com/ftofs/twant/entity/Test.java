package com.ftofs.twant.entity;

import com.ftofs.twant.util.Sqlite;

import org.litepal.LitePal;

public class Test {

    public static Class<? extends LitePal> getClazz() {
        return Sqlite.class;
    }


    public void test() {
        getClazz().
    }
}
