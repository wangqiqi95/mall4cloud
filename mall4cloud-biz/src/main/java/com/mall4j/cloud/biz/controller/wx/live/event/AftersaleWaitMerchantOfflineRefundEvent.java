package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 平台退款失败(待商家线下退款)
 *
 */
@Slf4j
@Service
public class AftersaleWaitMerchantOfflineRefundEvent implements INotifyEvent, InitializingBean {

    private static final String method = "aftersale_wait_merchant_offline_refund";

    /**
     * <xml>
     *      <ToUserName>gh_abcdefg</ToUserName>
     *      <FromUserName>oABCD</FromUserName>
     *      <CreateTime>12344555555</CreateTime>
     *      <MsgType>event</MsgType>
     *      <Event>aftersale_wait_merchant_offline_refund</Event>
     *      <aftersale_info>
     *           <aftersale_id>123456</aftersale_id>
     *           <out_aftersale_id>1234567</out_aftersale_id>
     *      </aftersale_info>
     * </xml>
     *
     *
     * 这里流程为：在某些特殊场景下，可能视频号订单会退款失败，需要线下退款，这个时候会有这个回调接口过来。
     * 这个时候需要修改退单为线下退款，
     * 线下退款完毕，上传线下退款的凭证给到视频号。这时候进入到下一个动作。会员确认
     * 会员确认完毕。修改订单还有退单的状态为退款成功。
     *
     * @param postData
     * @return
     * @throws Exception
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("平台退款失败(待商家线下退款)，输入参数：{}", postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);

        Long aftersaleId = MapUtil.getLong(resultMap, "aftersale_id");
        String outAftersaleId = MapUtil.getStr(resultMap, "out_aftersale_id");

        return "success";
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
