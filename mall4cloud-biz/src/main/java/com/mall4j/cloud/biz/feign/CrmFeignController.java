package com.mall4j.cloud.biz.feign;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
import com.mall4j.cloud.api.biz.feign.CrmFeignClient;
import com.mall4j.cloud.api.biz.feign.WxCpApiFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CrmFeignController implements CrmFeignClient {
//    private  final CrmManager crmManager;
//    private final WxCpApiFeignClient wxCpApiFeignClient;

    @Override
    public ServerResponseEntity<Void> pushCDPCpMsg(PushCDPCpMsgEventDTO dto) {
//        crmManager.pushCDPCpMsg(dto);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> pushCDPStaffCpMsg(PushCDPCpMsgEventDTO dto) {
//        if(Objects.nonNull(dto.getWxCpUser()) && dto.getChangetype().equals("delete_user")){
//            crmManager.pushCDPCpMsgDate(dto);
//        }else{
//            ServerResponseEntity<WxCpUser>  responseEntity=wxCpApiFeignClient.wxCpByUserId(dto.getStaffUserId());
//            if(responseEntity.isSuccess() && Objects.nonNull(responseEntity.getData())){
//                dto.setWxCpUser(responseEntity.getData());
//                crmManager.pushCDPCpMsgDate(dto);
//            }
//        }
        return null;
    }
}
