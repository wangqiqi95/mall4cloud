package com.mall4j.cloud.biz.controller.wx.channels.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.controller.wx.live.event.INotifyEvent;
import com.mall4j.cloud.biz.dto.channels.event.ProductSpuAuditDTO;
import com.mall4j.cloud.biz.service.channels.ChannelsSpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *商品上下架
 *
 */
@Slf4j
@Service
public class ProductSpuListingEvent implements INotifyEvent, InitializingBean {

    @Autowired
    private ChannelsSpuService channelsSpuService;

    private static final String method = "product_spu_listing";


    /**
     * https://developers.weixin.qq.com/doc/channels/API/product/callback/ProductSpuListing.html
     *
     * {
     *     "ToUserName":"gh_*",
     *     "FromUserName":"OPENID",
     *     "CreateTime":1662480000,
     *     "MsgType":"event",
     *     "Event":"product_spu_listing",
     *     "ProductSpuAudit": {
     *         "product_id":"12345678",
     *         "status":3,
     *         "reason":"abc"
     *     }
     * }
     * 状态。5:上架；11:自主下架；13:系统下架；14:保证金违规下架；15:品牌到期下架; 20:封禁下架。
     *
     *
     *
     * @param postData
     * @return
     * @throws Exception
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("商品上下架回调，输入参数：{}", postData);
        JSONObject jsonObject = JSON.parseObject(postData);
        ProductSpuAuditDTO productSpuAudit = jsonObject.getObject("ProductSpuListing", ProductSpuAuditDTO.class);
        channelsSpuService.listingNotify(productSpuAudit);
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
