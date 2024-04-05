package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
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
 * 用户更新售后物流信息
 */
@Service
@Slf4j
public class AftersaleUserModifyReturnInfoEvent implements INotifyEvent, InitializingBean {

    private static final String method = "aftersale_user_modify_return_info";
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
     * <Event>aftersale_user_modify_return_info</Event>
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
        log.info("用户修改退货物流，输入参数：{}", postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);


        Long aftersaleId = MapUtil.getLong(resultMap, "aftersale_id");
        String outAftersaleId = MapUtil.getStr(resultMap, "out_aftersale_id");

        // 根据售后单号查询售后数据
        AfterSaleInfo afterSaleInfo = new AfterSaleInfo();
        afterSaleInfo.setAftersaleId(aftersaleId);
        AfterSaleResponse afterSaleResponse = orderApi.afterSale(wxConfig.getWxMaToken(), afterSaleInfo);
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
            throw new LuckException("缺少微信对用物流信息");
        }

        return "success";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method, this);
    }

}
