package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 用户上传退货物流超时
 * 视频号订单 用户上传退货物流超时的话，视频号的售后订单状态会流转到售后单关闭状态。
 */
@Slf4j
@Service
public class AftersaleUserUploadReturnInfoOverdueEvent implements INotifyEvent, InitializingBean {

    private static final String method = "aftersale_user_upload_return_info_overdue";

    /**
     * <xml>
     *      <ToUserName>gh_abcdefg</ToUserName>
     *      <FromUserName>oABCD</FromUserName>
     *      <CreateTime>12344555555</CreateTime>
     *      <MsgType>event</MsgType>
     *      <Event>aftersale_user_upload_return_info_overdue</Event>
     *      <aftersale_info>
     *           <aftersale_id>123456</aftersale_id>
     *           <out_aftersale_id>1234567</out_aftersale_id>
     *      </aftersale_info>
     * </xml>
     *
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("用户上传退货物流超时，输入参数：{}", postData);
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