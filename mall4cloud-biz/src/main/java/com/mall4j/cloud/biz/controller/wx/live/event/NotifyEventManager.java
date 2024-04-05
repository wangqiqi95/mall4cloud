package com.mall4j.cloud.biz.controller.wx.live.event;

import com.mall4j.cloud.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NotifyEventManager {
    @Autowired
    SpringContextUtils springContextUtils;

    public static final String defaultEvent = "defaultNotiifyEvent";

    private static Map<String, INotifyEvent> HANDLE_SERVICE_MAP = new ConcurrentHashMap<>();

    private NotifyEventManager() {
    }

    public static NotifyEventManager getInstance() {
        return NotifyEventManager.Instance.instance;
    }

    private static class Instance {
        private static NotifyEventManager instance = new NotifyEventManager();
    }


    static ApplicationContext applicationContext;

    public INotifyEvent getNotifyEventService(String event){
        INotifyEvent notifyEvent = HANDLE_SERVICE_MAP.get(event);
        if(notifyEvent==null){
            log.error("event:{} 没有对应实现类",event);
            notifyEvent = HANDLE_SERVICE_MAP.get(defaultEvent);
        }
        return notifyEvent;
    }

    public void register(String event, INotifyEvent notifyEvent) {
        HANDLE_SERVICE_MAP.put(event, notifyEvent);
    }




}
