package com.mall4j.cloud.biz.controller.wx.live.app;

import com.mall4j.cloud.biz.service.live.LiveStoreNotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("liveStoreNotifyController")
@RequestMapping("/ua/livestore/notify")
@Api(tags = "小程序回调接口")
public class LiveStoreNotifyController {

    @Autowired
    LiveStoreNotifyService liveStoreNotifyService;

    /**
     *
     */
    @RequestMapping("/")
    @ApiOperation(value = "微信开放平台授权事件接收URL")
    public String authEvent(@RequestParam(name = "signature") String signature,
                            @RequestParam(name = "timestamp") String timestamp,
                            @RequestParam(name = "nonce") String nonce,
                            @RequestParam(name = "echostr", required = false) String echostr,
                            @RequestParam(name = "encrypt_type", required = false) String encryptType,
                            @RequestParam(name = "msg_signature", required = false) String msgSignature,
                            @RequestBody(required = false) String postdata) {
        return liveStoreNotifyService.authEvent(signature,timestamp,nonce,echostr,encryptType,msgSignature,postdata);
    }

    /**
     *
     */
    @PostMapping("/test")
    @ApiOperation(value = "Test 微信开放平台授权事件接收URL", notes = "Test 微信开放平台授权事件接收URL")
    public String authEventTest(@RequestBody(required = false) String postdata) {
        return liveStoreNotifyService.authEventTest(postdata);
    }



}
