package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.dto.UserRegisterQiWeiMsgDTO;
import com.mall4j.cloud.api.biz.feign.UserRegisterSendMsgFeignClient;
import com.mall4j.cloud.api.biz.feign.WxCpAuth2FeignClient;
import com.mall4j.cloud.api.biz.vo.WxCpUserInfoVO;
import com.mall4j.cloud.biz.service.cp.impl.WxCpPushServiceImpl;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMaJsCode2SessionResult;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: hwy
 * @Description: 通过code获取用户详细信息
 * @Date: 2022-01-18 17:37
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRegisterSendMsgFeignController implements UserRegisterSendMsgFeignClient {
    private  final WxCpPushServiceImpl pushService;

    @Override
    public ServerResponseEntity<Void> userRegisterSuccessNotify(UserRegisterQiWeiMsgDTO userRegisterQiWeiMsgDTO) {
        try {
            userRegisterQiWeiMsgDTO.getQiWeiStaffIdList().forEach(qiWeiStaffId -> pushService.userRegisterSuccessNotify(qiWeiStaffId, userRegisterQiWeiMsgDTO.getUserId()));
        }catch (Exception e){
            log.error("",e);
        }
        return ServerResponseEntity.success();
    }
}
