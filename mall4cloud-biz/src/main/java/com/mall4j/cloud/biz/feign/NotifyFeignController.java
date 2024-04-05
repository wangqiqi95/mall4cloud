
package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.feign.NotifyFeignClient;
import com.mall4j.cloud.biz.controller.app.SmsController;
import com.mall4j.cloud.biz.service.NotifyLogService;
import com.mall4j.cloud.biz.service.NotifyTemplateTagService;
import com.mall4j.cloud.biz.service.SmsLogService;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.PrincipalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


/**
 * @author lhd
 * @date 2020/12/30
 */
@RestController
public class NotifyFeignController implements NotifyFeignClient {

    @Autowired
    private SmsLogService smsLogService;
    @Autowired
    private NotifyLogService notifyLogService;
    @Autowired
    private NotifyTemplateTagService notifyTemplateTagService;

    @Override
    public ServerResponseEntity<Boolean> checkValidCode(String validAccount, String validCode, SendTypeEnum type) {
        if (PrincipalUtil.isMobile(validAccount)) {
            return ServerResponseEntity.success(smsLogService.checkValidCode(validAccount, validCode, type));
        }
        // todo 邮箱校验
        if (PrincipalUtil.isEmail(validAccount)){

        }
        return ServerResponseEntity.success(false);
    }

    @Override
    public ServerResponseEntity<Boolean> checkValidCodeByFlag(String mobile, String checkRegisterSmsFlag) {
        String redisMobile = RedisUtil.get(SmsController.CHECK_REGISTER_SMS_FLAG + checkRegisterSmsFlag);
        return ServerResponseEntity.success(Objects.equals(redisMobile,mobile));
    }

    @Override
    public ServerResponseEntity<Integer> getUnreadMsg(Long userId) {
        return ServerResponseEntity.success(notifyLogService.countUnreadBySendTypeAndRemindType(userId, Constant.MSG_TYPE));
    }

    @Override
    public ServerResponseEntity<Void> deleteTagByTagId(Long tagId) {
        notifyTemplateTagService.deleteByTagId(tagId);
        return ServerResponseEntity.success();
    }

}
