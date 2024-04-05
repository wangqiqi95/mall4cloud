package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.mapper.WechatCatogoryQualificationMapper;
import com.mall4j.cloud.biz.model.WechatCatogoryQualificationDO;
import com.mall4j.cloud.biz.service.WechatEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service("open_product_category_auditHandler")
@Slf4j
public class WechatCategoryEventHandler implements WechatEventHandler {

    @Autowired
    private WechatCatogoryQualificationMapper catogoryQualificationMapper;

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws Exception {
        String xml=msgDTO.getXml();

        //将xml转为map
        Map<String, String> resultMap = WechatUtils.xmlToMap(xml);

        String auditId = resultMap.get("audit_id");
        Integer status = Integer.parseInt(resultMap.get("status"));

        WechatCatogoryQualificationDO catogoryQualificationDO = catogoryQualificationMapper.getByAuditId(auditId);

        // 审核状态比较
        if (!catogoryQualificationDO.getStatus().equals(status)) {
            WechatCatogoryQualificationDO entity = new WechatCatogoryQualificationDO();
            entity.setId(catogoryQualificationDO.getId());
            entity.setStatus(status);
            if (status == 9) {
                entity.setAuditContent(resultMap.get("reject_reason"));
            }

            entity.setAuditTime(new Date(Long.parseLong(resultMap.get("CreateTime"))));
            entity.setUpdateBy("event-open_product_category_audit");

            // 更新表
            catogoryQualificationMapper.update(entity);
        }



        return WechatConstants.TICKET_SUCCESS;
    }
}
