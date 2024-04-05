package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.AttachmentExtApiDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall4cloud-biz",contextId = "wxCpCustNotify")
public interface WxCpCustNotifyFeignClient {

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wx/cp/cust/notify/define/mini/program")
    ServerResponseEntity<String> defineStaffMiniProgram(@RequestBody AttachmentExtApiDTO attachmentExtApiDTO);


    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wx/cp/cust/notify/followNotify")
    ServerResponseEntity<String> followNotify(@RequestParam("nickName") String nickName,
                                              @RequestParam("userName") String userName,
                                              @RequestParam("staffName") String staffName,
                                              @RequestParam("createDate") String createDate,
                                              @RequestParam("relationId") String relationId,
                                              @RequestParam("staffUserId") String staffUserId);
}
