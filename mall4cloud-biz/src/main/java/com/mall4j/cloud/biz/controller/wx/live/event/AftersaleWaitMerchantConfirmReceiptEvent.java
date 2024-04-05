package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.livestore.request.AfterSaleInfo;
import com.mall4j.cloud.api.biz.dto.livestore.response.AfterSaleResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.AfterSalesOrder;
import com.mall4j.cloud.api.biz.dto.livestore.response.ReturnInfo;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.api.live.OrderApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.WechatLogisticsMappingDO;
import com.mall4j.cloud.biz.service.WechatLiveLogisticService;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 用户上传退货物流
 */
@Service
@Slf4j
public class AftersaleWaitMerchantConfirmReceiptEvent implements INotifyEvent, InitializingBean {

    private static final String method = "aftersale_wait_merchant_confirm_receipt";
    @Autowired
    OrderApi orderApi;
    @Autowired
    WxConfig wxConfig;
    @Autowired
    private WechatLiveLogisticService wechatLiveLogisticService;

    /**
     * <xml>
     * <ToUserName>gh_abcdefg</ToUserName>
     * <FromUserName>oABCD</FromUserName>
     * <CreateTime>12344555555</CreateTime>
     * <MsgType>event</MsgType>
     * <Event>aftersale_wait_merchant_confirm_receipt</Event>
     * <aftersale_info>
     * <aftersale_id>123456</aftersale_id>
     * <out_aftersale_id>1234567</order_id>
     * </aftersale_info>
     * </xml>
     *
     * @param postData
     * @return
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("用户上传退货物流(待商家确认收货)，输入参数：{}", postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);


        Long aftersaleId = MapUtil.getLong(resultMap, "aftersale_id");
        String outAftersaleId = MapUtil.getStr(resultMap, "out_aftersale_id");

        // 根据售后单号查询售后数据
        AfterSaleInfo afterSaleInfo = new AfterSaleInfo();
        afterSaleInfo.setAftersaleId(aftersaleId);
        AfterSaleResponse afterSaleResponse = orderApi.afterSale(wxConfig.getWxMaToken(), afterSaleInfo);
        log.info("用户上传退货物流(待商家确认收货)，查询售后信息参数:{},返回对象:{}", JSONObject.toJSONString(afterSaleInfo),JSONObject.toJSONString(afterSaleResponse));
        if (afterSaleResponse == null || afterSaleResponse.getErrcode() > 0) {
            throw new LuckException("用户上传退货物流(待商家确认收货)，请求售后数据异常！");
        }
        AfterSalesOrder afterSalesOrder = afterSaleResponse.getAfterSalesOrder();
        ReturnInfo returnInfo = afterSalesOrder.getReturnInfo();
        if (Objects.isNull(returnInfo.getDeliveryId())) {
            throw new LuckException("用户上传退货物流(待商家确认收货)，物流数据异常");
        }
        //封装物流信息提交
        WechatLogisticsMappingDO mapping = wechatLiveLogisticService.getByWechatDeliveryId(returnInfo.getDeliveryId());
        if (Objects.isNull(mapping)) {
            mapping = wechatLiveLogisticService.getDefualtWechatDelivery();
            log.info("用户上传退货物流(待商家确认收货),物流公司:{}。没有维护映射表，这里给默认值:{}",JSONObject.toJSONString(mapping));
            //如果映射物流信息表不存在，这里给一个默认物流公司。
//            throw new LuckException("缺少微信对用物流信息");
        }
        return "success";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method, this);
    }

}
