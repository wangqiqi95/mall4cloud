package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.mapper.WechatBrandQualificationMapper;
import com.mall4j.cloud.biz.model.WechatBrandQualificationDO;
import com.mall4j.cloud.biz.service.WechatEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service("open_product_brand_auditHandler")
@Slf4j
public class WechatBrandEventHandler implements WechatEventHandler {

    @Autowired
    private WechatBrandQualificationMapper brandQualificationMapper;

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws Exception {
        String xml=msgDTO.getXml();

        //将xml转为map
        Map<String, String> resultMap = WechatUtils.xmlToMap(xml);

        String auditId = resultMap.get("audit_id");
        Integer status = Integer.parseInt(resultMap.get("status"));

        WechatBrandQualificationDO brandQualificationDO = brandQualificationMapper.getByAuditId(auditId);

        // 审核状态比较
        if (!brandQualificationDO.getStatus().equals(status)) {
            WechatBrandQualificationDO entity = new WechatBrandQualificationDO();
            entity.setStatus(status);
            if (status == 9) {
                entity.setAuditContent(resultMap.get("reject_reason"));
            }

            entity.setAuditTime(new Date(Long.parseLong(resultMap.get("CreateTime"))));
            entity.setUpdateBy("event-open_product_brand_audit");

            // 更新表
            brandQualificationMapper.update(entity);
        }



        return WechatConstants.TICKET_SUCCESS;
    }
}
