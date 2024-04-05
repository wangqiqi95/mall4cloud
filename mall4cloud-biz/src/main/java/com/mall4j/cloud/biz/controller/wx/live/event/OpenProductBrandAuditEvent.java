package com.mall4j.cloud.biz.controller.wx.live.event;

import cn.hutool.core.map.MapUtil;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.service.WechatLiveBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 品牌审核结果
 * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/ministore/minishopopencomponent2/callback/category_audit.html
 *
 * <xml><ToUserName><![CDATA[gh_b7fbaed1e621]]></ToUserName>
 * <FromUserName><![CDATA[opnL_44bDL16mLG6wk89opwOdnQw]]></FromUserName>
 * <CreateTime>1656321149</CreateTime>
 * <MsgType><![CDATA[event]]></MsgType>
 * <Event><![CDATA[open_product_brand_audit]]></Event>
 * <QualificationAuditResult>
 * <audit_id><![CDATA[jAAAAKZy_-MCABBCrbEpIg]]></audit_id>
 * <status>9</status>
 * <audit_type>1</audit_type>
 * <reject_reason><![CDATA[你好，你所提交的商标资质非《商标注册证》/《商标注册申请受理通知书》，请提供正确的商标材料后重新进行审核。]]></reject_reason>
 * <brand_id><![CDATA[40676]]></brand_id>
 * <seq_qc_id>0</seq_qc_id>
 * </QualificationAuditResult></xml>
 */
@Slf4j
@Service
public class OpenProductBrandAuditEvent implements INotifyEvent, InitializingBean {
    private static final String method = "open_product_brand_audit";

    @Autowired
    WechatLiveBrandService wechatLiveBrandService;

    @Override
    public String doEvent(String postData) throws Exception {
        log.info("品牌审核结果回调接收请求，输入参数：{}",postData);
        Map<String, String> resultMap = WechatUtils.xmlToMap(postData);

        String audit_id = MapUtil.getStr(resultMap, "audit_id");
        Integer status = MapUtil.getInt(resultMap, "status");
        String reject_reason =  MapUtil.getStr(resultMap, "reject_reason");
        Long wxBrandId = MapUtil.getLong(resultMap,"brand_id");
        wechatLiveBrandService.syncAuditResult(audit_id,reject_reason,status,wxBrandId);
        return "success";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.register(method,this);
    }
}
