package com.mall4j.cloud.biz.controller.multishop;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.mall4j.cloud.api.auth.dto.AuthAccountDTO;
import com.mall4j.cloud.api.auth.dto.AuthAccountUserDTO;
import com.mall4j.cloud.api.auth.feign.AccountFeignClient;
import com.mall4j.cloud.api.auth.vo.AuthAccountVO;
import com.mall4j.cloud.biz.dto.SendAndCheckSmsDTO;
import com.mall4j.cloud.biz.service.SmsLogService;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.PrincipalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author lth
 * @Date 2021/6/10 14:34
 */
@RestController("multishopSmsController")
@RequestMapping("/m/sms")
@Api(tags = "multishop-短信接口")
public class SmsController {

    @Autowired
    private SmsLogService smsLogService;
    @Autowired
    private AccountFeignClient accountFeignClient;

    @PutMapping("/send_withdraw_code")
    @ApiOperation(value="给商家手机号发送申请提现验证码", notes="给商家手机号发送申请提现验证码，返回商家手机号")
    public ServerResponseEntity<String> applyShopSms() {
        Long shopId = AuthUserContext.get().getTenantId();
        ServerResponseEntity<AuthAccountVO> merchantInfoRes = accountFeignClient.getMerchantInfoByTenantId(shopId);
        if (!merchantInfoRes.isSuccess()) {
            throw new LuckException("服务器繁忙");
        }
        smsLogService.sendSmsCode(SendTypeEnum.VALID, merchantInfoRes.getData().getPhone(), Maps.newHashMap());
        return ServerResponseEntity.success(merchantInfoRes.getData().getPhone());
    }

    @PutMapping("/send_auth_shop_user_code")
    @ApiOperation(value="发送认证店铺员工/子账号验证码", notes="发送认证店铺员工/子账号验证码")
    public ServerResponseEntity<Void> sendAuthShopUserSms(@RequestBody SendAndCheckSmsDTO sendAndCheckSmsDTO) {
        String phone = sendAndCheckSmsDTO.getMobile();
        if (StrUtil.isBlank(phone) || !PrincipalUtil.isMobile(phone)) {
            throw new LuckException("手机号格式不正确");
        }
        smsLogService.sendSmsCode(SendTypeEnum.VALID, phone, Maps.newHashMap());
        return ServerResponseEntity.success();
    }

}
