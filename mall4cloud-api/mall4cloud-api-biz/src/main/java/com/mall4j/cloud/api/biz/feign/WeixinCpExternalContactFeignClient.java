package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.CpChatAddMsgTemplateDTO;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.ExtendWxCpMsgTemplateDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.cp.bean.external.WxCpMsgTemplateAddResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall4cloud-biz",contextId = "weixinCpExternalContact")
public interface WeixinCpExternalContactFeignClient {

    /**
     * 创建群发任务-发送给客户
     * */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/cp/add/externalcontact/msg/template")
    ServerResponseEntity<WxCpMsgTemplateAddResult> addExternalContactMsgTemplate(@RequestBody String wxCpMsgTemplate) ;

    /**
     * 创建群发任务-发送给客群
     * */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/cp/add/externalcontact/chat/msg/template")
    ServerResponseEntity<WxCpMsgTemplateAddResult> addChatMsgTemplate(@RequestBody CpChatAddMsgTemplateDTO dto) ;


    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/cp/cancel/externalcontact/msg")
    ServerResponseEntity cancelExternalContactMsgTemplate(@RequestParam("msgId") String msgId);

}
