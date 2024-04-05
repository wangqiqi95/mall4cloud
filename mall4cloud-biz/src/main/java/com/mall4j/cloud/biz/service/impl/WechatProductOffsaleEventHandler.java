package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.service.WechatEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("open_product_spu_status_updateHandler")
@Slf4j
public class WechatProductOffsaleEventHandler implements WechatEventHandler {

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws Exception {

        String xml=msgDTO.getXml();
        //将xml转为map
        Map<String, String> resultMap = WechatUtils.xmlToMap(xml);

        int status = Integer.parseInt(resultMap.get("status"));

        if (status == 13) {
            log.error("商品[{}]被系统下架,原因:{}", resultMap.get("out_product_id"), resultMap.get("reason"));
        } else {
            log.info("商品[{}]重新上架", resultMap.get("out_product_id"));
        }

        return WechatConstants.TICKET_SUCCESS;
    }
}
