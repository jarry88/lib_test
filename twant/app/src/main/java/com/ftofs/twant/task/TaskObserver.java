package com.ftofs.twant.task;

/**
 * 任務觀察者
 * 在UI線程中執行
 * @author zwm
 */
public abstract class TaskObserver implements Runnable, Cloneable {
    protected Object message;

    /**
     * 對TaskObservable發過來的消息進行處理
     */
    public abstract void onMessage();

    @Override
    public void run() {
        onMessage();
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
