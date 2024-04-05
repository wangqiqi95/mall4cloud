package com.mall4j.cloud.api.biz.feign;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalUnassignList;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.user.WxCpDeptUserResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author hwy
 * @Date 2022/01/18 10:28
 */
@FeignClient(value = "mall4cloud-biz",contextId = "CrmFeignClient")
public interface CrmFeignClient {

    /**
     * 推送数据给数云
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wx/crm/pushCDPCpMsg")
    ServerResponseEntity<Void> pushCDPCpMsg(@RequestBody PushCDPCpMsgEventDTO dto) ;

    /**
     * 推送企微员工数据给数云【后台添加/更新用户】
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wx/crm/pushCDPStaffCpMsg")
    ServerResponseEntity<Void> pushCDPStaffCpMsg(@RequestBody PushCDPCpMsgEventDTO dto) ;


}
