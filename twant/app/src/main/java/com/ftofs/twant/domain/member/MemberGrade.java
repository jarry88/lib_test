package com.ftofs.twant.domain.member;

import java.io.Serializable;

public class MemberGrade implements Serializable {
    /**
     * 自增编码
     */
    private int gradeId;

    /**
     * 等级值
     */
    private int gradeLevel = 0;

    /**
     * 等级名称
     */
    private String gradeName = "";

    /**
     * 信賴值
     */
    private int expPoints = 0;

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getExpPoints() {
        return expPoints;
    }

    public void setExpPoints(int expPoints) {
        this.expPoints = expPoints;
    }

    @Override
    public String toString() {
        return "MemberGrade{" +
                "gradeId=" + gradeId +
                ", gradeLevel=" + gradeLevel +
                ", gradeName='" + gradeName + '\'' +
                ", expPoints=" + expPoints +
                '}';
    }
}
