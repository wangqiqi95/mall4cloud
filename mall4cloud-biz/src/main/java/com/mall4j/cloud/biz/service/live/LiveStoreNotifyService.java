package com.mall4j.cloud.biz.service.live;

public interface LiveStoreNotifyService {

    String authEvent(String signature, String timestamp, String nonce, String echostr, String encryptType, String msgSignature, String postdata);



    String authEventTest(String postdata);
}
