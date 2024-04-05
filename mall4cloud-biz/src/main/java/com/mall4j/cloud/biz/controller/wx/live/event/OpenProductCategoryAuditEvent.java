package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.service.WechatLiveCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 类目审核结果
 * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/callback/category_audit.html
 * <xml>
 *     <ToUserName>gh_abcdefg</ToUserName>
 *     <FromUserName>oABCD</FromUserName>
 *     <CreateTime>12344555555</CreateTime>
 *     <MsgType>event</MsgType>
 *     <Event>open_product_category_audit</Event>
 *     <QualificationAuditResult>
 *         <audit_id>Xe5h1ILil12_fwfe</audit_id>
 *         <status>1</status>
 *         <audit_type>2</audit_type>
 *         <reject_reason>xxx原因</reject_reason>
 *     </QualificationAuditResult>
 * </xml>
 */
@Slf4j
@Service
public class OpenProductCategoryAuditEvent implements INotifyEvent, InitializingBean {
    private static final String method = "open_product_category_audit";

    @Autowired
    WechatLiveCategoryService wechatLiveCategoryService;

    @Override
    public String doEvent(String postData) throws Exception {
        log.info("类目审核结果回调接收请求，输入参数：{}",postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);

        String audit_id = MapUtil.getStr(resultMap, "audit_id");
        Integer status = MapUtil.getInt(resultMap, "status");
        String reject_reason =  MapUtil.getStr(resultMap, "reject_reason");
        wechatLiveCategoryService.auditResult(audit_id,status,reject_reason);
        return "success";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }
}
