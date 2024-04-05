package com.mall4j.cloud.biz.controller.wx.live.event;

public interface INotifyEvent {

    String doEvent(String postData) throws Exception;

    default void register(String event, INotifyEvent notifyEvent) {
        NotifyEventManager.getInstance().register(event, notifyEvent);
    }
}
