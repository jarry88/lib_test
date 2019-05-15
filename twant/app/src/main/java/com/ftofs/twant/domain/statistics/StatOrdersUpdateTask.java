package com.ftofs.twant.domain.statistics;

import java.io.Serializable;
import java.sql.Timestamp;

public class StatOrdersUpdateTask implements Serializable {
    /**
     * 任务日志自增编码
     */
    private int taskId;

    /**
     * 订单ID
     */
    private int ordersId;

    /**
     * 任务类型(StatOrdersUpdateTaskType)
     */
    private String taskType;

    /**
     * 添加时间
     */
    private Timestamp addTime;

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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
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
        return "StatOrdersUpdateTask{" +
                "taskId=" + taskId +
                ", ordersId=" + ordersId +
                ", taskType='" + taskType + '\'' +
                ", addTime=" + addTime +
                ", taskState=" + taskState +
                '}';
    }
}
