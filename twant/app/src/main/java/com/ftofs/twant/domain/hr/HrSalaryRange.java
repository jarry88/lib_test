package com.ftofs.twant.domain.hr;

public class HrSalaryRange {
    /**
     * id
     */
    private int rangeId;

    /**
     * 區間
     */
    private  String range;

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "HrSalaryRange{" +
                "rangeId=" + rangeId +
                ", range='" + range + '\'' +
                '}';
    }
}
