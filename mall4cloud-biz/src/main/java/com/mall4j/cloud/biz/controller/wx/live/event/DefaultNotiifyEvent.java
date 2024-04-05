package com.mall4j.cloud.biz.controller.wx.live.event;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class DefaultNotiifyEvent implements INotifyEvent, InitializingBean {

    private static final String method = "defaultNotiifyEvent";

    @Override
    public String doEvent(String postData) throws Exception {
        return "";
    }

    @Override
    public void register(String event, INotifyEvent notifyEvent) {
        INotifyEvent.super.register(event, notifyEvent);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }
}
