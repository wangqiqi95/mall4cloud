package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 纠纷单回调处理对象。
 */
@Slf4j
@Service
public class ComplaintNotifyEvent implements INotifyEvent, InitializingBean {

    private static final String method = "complaint_notify";

    /**
     *
     * <xml>
     * <ToUserName>gh_abcdefg</ToUserName>
     * <FromUserName>oABCD</FromUserName>
     * <CreateTime>12344555555</CreateTime>
     * <MsgType>event</MsgType>
     * <Event>complaint_notify</Event>
     * <complaint_info>
     *      <complaint_order_id>123456</complaint_order_id>
     *      <state>1234567</state>
     *      <event>1234567</event>
     * </complaint_info>
     * </xml>
     *
     * 240001	用户发起投诉
     * 240002	客服需要商家举证
     * 240003	客服需要用户举证
     * 240004	客服需要双方举证
     * 240005	客服无需双方举证
     * 240006	用户补充凭证
     * 240007	用户补充凭证超时
     * 240008	商家补充凭证
     * 240009	商家补充凭证超时
     * 240010	用户补充凭证，缺商家凭证
     * 240011	商家补充凭证，缺用户凭证
     * 240012	用户补充凭证，双方凭证齐全
     * 240013	用户补充凭证超时
     * 240014	商家补充凭证，双方凭证齐全
     * 240015	商家补充凭证超时
     * 240016	双方补充凭证超时
     * 240030	凭证不足，重新补充凭证
     * 240031	售后退款成功
     * 240033	售后判定商家拒绝退款
     * 240036	平台判断商家退款凭证属实
     * 240037	平台判断商家退款凭证不属实
     * 240038	客服需要商家退货举证
     * 240039	客服需要用户退货举证
     * 240040	客服需要双方退货举证
     * 240041	客服无需双方退货举证
     * 240042	用户补充退货凭证
     * 240043	用户补充退货凭证超时
     * 240044	商家补充退货凭证
     * 240045	商家补充退货凭证超时
     * 240046	用户补充退货凭证，缺商家退货凭证
     * 240047	商家补充退货凭证，缺用户退货凭证
     * 240048	用户补充退货凭证，双方退货凭证齐全
     * 240049	用户补充退货凭证超时
     * 240050	商家补充退货凭证，双方退货凭证齐全
     * 240051	商家补充退货凭证超时
     * 240052	双方补充退货凭证超时
     * 240053	客服判断退货凭证，需要商家退款
     * 240054	客服判断退货凭证，不需要商家退款
     * 240056	退货收货凭证不足，重新补充凭证
     * 240057	判责前用户补充凭证
     * 240058	判责前商家同意售后
     * 240059	用户申请平台核实商家退款凭证
     * 240062	平台判责，商家责任，需退款
     * 240063	平台判责，用户责任，需退款
     * 240064	平台判责，双方责任，需退款
     * 240065	平台判责，双方无责，需退款
     * 240066	平台判责，商家责任，需退货退款
     * 240067	平台判责，用户责任，需退货退款
     * 240068	平台判责，双方责任，需退货退款
     * 240069	平台判责，双方无责，需退货退款
     * 240025	平台判责，商家责任，无需退货退款
     * 240026	平台判责，用户责任，无需退货退款
     * 240027	平台判责，双方责任，无需退货退款
     * 240028	平台判责，双方无责，无需退货退款
     *
     *
     * @param postData
     * @return
     * @throws Exception
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("用户申请平台介入超时，输入参数：{}", postData);

        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);
        int event = MapUtil.getInt(resultMap, "event");
        Long complaintOrderId = MapUtil.getLong(resultMap,"complaint_order_id");



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
