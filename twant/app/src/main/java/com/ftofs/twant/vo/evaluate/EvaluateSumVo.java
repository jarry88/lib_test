package com.ftofs.twant.vo.evaluate;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 產品评价汇总数据Vo
 *
 * @author dqw
 * Created 2017/10/19 17:38
 */
public class EvaluateSumVo {
    /**
     * 评价总数
     */
    private long evalCount;
    /**
     * 好评数
     */
    private long evalCount1;
    /**
     * 中评数
     */
    private long evalCount2;
    /**
     * 差评数
     */
    private long evalCount3;
    /**
     * 好评率
     */
    private int evalRate1;
    /**
     * 中评率
     */
    private int evalRate2;
    /**
     * 差评率
     */
    private int evalRate3;
    /**
     * 有图评论数
     */
    private long hasImageCount;

    public long getEvalCount() {
        return evalCount;
    }

    public void setEvalCount(long evalCount) {
        this.evalCount = evalCount;
    }

    public long getEvalCount1() {
        return evalCount1;
    }

    public void setEvalCount1(long evalCount1) {
        this.evalCount1 = evalCount1;
    }

    public long getEvalCount2() {
        return evalCount2;
    }

    public void setEvalCount2(long evalCount2) {
        this.evalCount2 = evalCount2;
    }

    public long getEvalCount3() {
        return evalCount3;
    }

    public void setEvalCount3(long evalCount3) {
        this.evalCount3 = evalCount3;
    }

    public int getEvalRate1() {
        return evalRate1;
    }

    public void setEvalRate1(int evalRate1) {
        this.evalRate1 = evalRate1;
    }

    public int getEvalRate2() {
        return evalRate2;
    }

    public void setEvalRate2(int evalRate2) {
        this.evalRate2 = evalRate2;
    }

    public int getEvalRate3() {
        return evalRate3;
    }

    public void setEvalRate3(int evalRate3) {
        this.evalRate3 = evalRate3;
    }

    public long getHasImageCount() {
        return hasImageCount;
    }

    public void setHasImageCount(long hasImageCount) {
        this.hasImageCount = hasImageCount;
    }

    @Override
    public String toString() {
        return "EvaluateSumVo{" +
                "evalCount=" + evalCount +
                ", evalCount1=" + evalCount1 +
                ", evalCount2=" + evalCount2 +
                ", evalCount3=" + evalCount3 +
                ", evalRate1=" + evalRate1 +
                ", evalRate2=" + evalRate2 +
                ", evalRate3=" + evalRate3 +
                ", hasImageCount=" + hasImageCount +
                '}';
    }
}

