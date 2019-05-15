package com.ftofs.twant.domain.chain;

public class ChainClass {
    /**
     * 门店类型编号
     */
    private int classId;

    /**
     * 门店类型名称
     */
    private String className;

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

    public int getClassSort() {
        return classSort;
    }

    public void setClassSort(int classSort) {
        this.classSort = classSort;
    }

    @Override
    public String toString() {
        return "ChainClass{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                ", classSort=" + classSort +
                '}';
    }
}
