package com.mall4j.cloud.biz.controller.wx.channels;

import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderResponse;
import com.mall4j.cloud.biz.service.channels.ChannelsNotifyService;
import com.mall4j.cloud.biz.service.channels.EcOrderService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("ChannelsNotifyController")
@RequestMapping("/ua/channels/notify")
@Api(tags = "视频号4。0回调接口")
public class ChannelsNotifyController {

    @Autowired
    ChannelsNotifyService channelsNotifyService;
    @Autowired
    EcOrderService ecOrderService;

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
        return channelsNotifyService.authEvent(signature,timestamp,nonce,echostr,encryptType,msgSignature,postdata);
    }

    /**
     *
     */
    @PostMapping("/test")
    @ApiOperation(value = "Test 微信开放平台授权事件接收URL", notes = "Test 微信开放平台授权事件接收URL")
    public ServerResponseEntity authEventTest(@RequestBody(required = false) String postdata) {
        channelsNotifyService.authEventTest(postdata);
        return ServerResponseEntity.success();
    }


    @PostMapping("/ua/getOrder")
    @ApiOperation(value = "获取订单详情", notes = "获取订单详情")
    public ServerResponseEntity<EcOrderResponse> getOrder(@RequestParam(name = "orderId") String orderId) {
        EcOrderResponse orderResponse =ecOrderService.get(orderId);
        return ServerResponseEntity.success(orderResponse);
    }




}
