package com.mall4j.cloud.biz.controller.wx.channels.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.controller.wx.live.event.INotifyEvent;
import com.mall4j.cloud.biz.dto.channels.event.ProductCategoryAuditInfoDTO;
import com.mall4j.cloud.biz.service.channels.ChannelsCategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @date 2023/3/3
 */
@AllArgsConstructor
@Service
@Slf4j
public class ProductCategoryAuditEvent implements INotifyEvent, InitializingBean {

    private static final String method = "product_category_audit";

    private final ChannelsCategoryService channelsCategoryService;

    @Override
    public String doEvent(String postData) throws Exception {
        JSONObject jsonObject = JSON.parseObject(postData);
        ProductCategoryAuditInfoDTO auditInfoDTO =
                jsonObject.getObject("ProductCategoryAudit", ProductCategoryAuditInfoDTO.class);

        if (Objects.nonNull(auditInfoDTO)) {
            log.info("微信视频号4.0回调审核，审核参数{}", auditInfoDTO);
            channelsCategoryService.audit(auditInfoDTO);
        }

        return "success";
    }

    @Override
    public void register(String event, INotifyEvent notifyEvent) {
        INotifyEvent.super.register(event, notifyEvent);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method, this);
    }
}
