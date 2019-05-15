package com.ftofs.twant.domain.member;

import java.io.Serializable;

public class ConsultClass implements Serializable {
    /**
     * 自增编码
     */
    private int classId;

    /**
     * 分类名称
     */
    private String className = "";

    /**
     * 分类介绍
     */
    private String introduce = "";

    /**
     * 排序
     */
    private int classSort = 0;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getClassSort() {
        return classSort;
    }

    public void setClassSort(int classSort) {
        this.classSort = classSort;
    }

    @Override
    public String toString() {
        return "ConsultClass{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                ", introduce='" + introduce + '\'' +
                ", classSort=" + classSort +
                '}';
    }
}
