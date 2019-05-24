package com.ftofs.twant.domain.chain.statistics;

import java.io.Serializable;


public class ChainStatOrdersUpdateTask implements Serializable {
    /**
     * 任务日志自增编码
     */
    private int taskId;

    /**
     * 订单ID
     */
    private int ordersId;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 是否已经执行完成的状态 0未完成 1已完成 2执行失败(StatOrdersUpdateTaskState)
     */
    private int taskState;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    @Override
    public String toString() {
        return "ChainStatOrdersUpdateTask{" +
                "taskId=" + taskId +
                ", ordersId=" + ordersId +
                ", addTime=" + addTime +
                ", taskState=" + taskState +
                '}';
    }
}
