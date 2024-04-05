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
 *商品审核
 *
 */
@Slf4j
@Service
public class ProductSpuAuditEvent implements INotifyEvent, InitializingBean {

    @Autowired
    private ChannelsSpuService channelsSpuService;

    private static final String method = "product_spu_audit";


    /**
     * https://developers.weixin.qq.com/doc/channels/API/aftersale/ec_callback/channels_ec_aftersale_update.html
     *
     * {
     *     "ToUserName":"gh_*",
     *     "FromUserName":"OPENID",
     *     "CreateTime":1662480000,
     *     "MsgType":"event",
     *     "Event":"product_spu_audit",
     *     "ProductSpuAudit": {
     *         "product_id":"12345678",
     *         "status":3,
     *         "reason":"abc"
     *     }
     * }
     *
     * 状态。2:审核不通过；3:审核通过。
     *
     *
     *
     * @param postData
     * @return
     * @throws Exception
     */
    @Override
    public String doEvent(String postData) throws Exception {
        log.info("商品审核回调，输入参数：{}", postData);
        JSONObject jsonObject = JSON.parseObject(postData);
        ProductSpuAuditDTO productSpuAudit = jsonObject.getObject("ProductSpuAudit", ProductSpuAuditDTO.class);
        channelsSpuService.auditNotify(productSpuAudit);
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
