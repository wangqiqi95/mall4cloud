package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单归因成功回调
 * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/callback/order_analyze.html
 */
@Slf4j
@Service
public class OpenProductOrderAnalyzeEvent implements INotifyEvent, InitializingBean {
    private static final String method = "open_product_order_analyze";

    /**
     * <xml>
     *   <ToUserName><![CDATA[gh_2defbb026678]]></ToUserName>
     *   <FromUserName><![CDATA[oInFct2WuoNBcoBRgEppkksARi8U]]></FromUserName>
     *   <CreateTime>1651145445</CreateTime>
     *   <MsgType><![CDATA[event]]></MsgType>
     *   <Event><![CDATA[open_product_order_analyze]]></Event>
     *   <order_info>
     *     <order_id>3302922000692805632</order_id>
     *     <out_order_id><![CDATA[order_991630]]></out_order_id>
     *     <openid><![CDATA[o7_Rk5Xaj2BG32rKy6jlOt2qk4G0]]></openid>
     *   </order_info>
     * </xml>
     *
     * @param postData
     * @return
     */
    @Override
    public String doEvent(String postData) throws Exception {

        log.info("订单归因成功回调接收请求，输入参数：{}",postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);

        String outOrderId = MapUtil.getStr(resultMap, "out_order_id");

        return "success";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }
}
