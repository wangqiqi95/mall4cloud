package com.mall4j.cloud.biz.controller.app;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.feign.AccountFeignClient;
import com.mall4j.cloud.biz.dto.SendAndCheckSmsDTO;
import com.mall4j.cloud.biz.service.SmsLogService;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author FrozenWatermelon
 * @date 2021/01/16
 */
@RequestMapping(value = "/ua/sms/verification_code")
@RestController
@Api(tags = "短信接口")
@Slf4j
public class SmsController {

    @Autowired
    private SmsLogService smsLogService;

    @Autowired
    private AccountFeignClient accountFeignClient;

    public static String CHECK_REGISTER_SMS_FLAG = "checkRegisterSmsFlag";

    public static final String CHECK_UPDATE_PWD_SMS_FLAG = "updatePwdSmsFlag";

    @PutMapping("/register")
    @ApiOperation(value = "发送注册验证码", notes = "发送注册验证码")
    public ServerResponseEntity<Void> register(@Valid @RequestBody SendAndCheckSmsDTO sendAndCheckSmsDto) {
        if (accountFeignClient.countByMobile(sendAndCheckSmsDto.getMobile()).getData() > 0) {
            // 该手机号已注册，无法重新注册
            return ServerResponseEntity.showFailMsg("该手机号已注册，无法重新注册");
        }
        // 每个手机号每分钟只能发十个注册的验证码，免得接口被利用
        smsLogService.sendSmsCode(SendTypeEnum.REGISTER, sendAndCheckSmsDto.getMobile(), Maps.newHashMap());
        log.info("获取短信验证码，参数:{}", JSONObject.toJSONString(sendAndCheckSmsDto));
        return ServerResponseEntity.success();
    }

    @PutMapping("/check_register")
    @ApiOperation(value = "校验验证码", notes = "校验验证码返回校验成功的标识")
    public ServerResponseEntity<String> checkRegisterSms(@Valid @RequestBody SendAndCheckSmsDTO sendAndCheckSmsDto) {
//        // 每个ip每分钟只能发十个注册的验证码，免得接口被利用
        if (!smsLogService.checkValidCode(sendAndCheckSmsDto.getMobile(), sendAndCheckSmsDto.getValidCode(), SendTypeEnum.REGISTER)){
            // 验证码有误或已过期
            return ServerResponseEntity.fail(ResponseEnum.VERIFICATION_CODE_ERROR);
        }
//
        return ServerResponseEntity.success();
    }

    @PutMapping("/bind")
    @ApiOperation(value = "发送绑定验证码", notes = "发送绑定验证码")
    public ServerResponseEntity<Void> bindSms(@Valid @RequestBody SendAndCheckSmsDTO sendSmsParam) {
        // 账号未注册，需要进行注册账号才能进行绑定
        if (accountFeignClient.countByMobile(sendSmsParam.getMobile()).getData() == 0) {
            return ServerResponseEntity.fail(ResponseEnum.ACCOUNT_NOT_REGISTER);
        }
        // 每个手机号每分钟只能发十个注册的验证码，免得接口被利用
        smsLogService.sendSmsCode(SendTypeEnum.VALID, sendSmsParam.getMobile(), Maps.newHashMap());
        return ServerResponseEntity.success();
    }

    @PutMapping("/apply_shop")
    @ApiOperation(value = "发送申请开店验证码", notes = "发送申请开店验证码")
    public ServerResponseEntity<Void> applyShopSms(@Valid @RequestBody SendAndCheckSmsDTO sendSmsParam) {
        if (accountFeignClient.countByMobileAndSysType(sendSmsParam.getMobile(), SysTypeEnum.MULTISHOP.value()).getData() > 0) {
            return ServerResponseEntity.showFailMsg("手机号已存在，请更换手机号再次尝试");
        }
        // 每个手机号每分钟只能发十个注册的验证码，免得接口被利用
        smsLogService.sendSmsCode(SendTypeEnum.VALID, sendSmsParam.getMobile(), Maps.newHashMap());
        return ServerResponseEntity.success();
    }

    /**
     * 发送修改密码验证码接口
     */
    @PostMapping("/update_pwd")
    @ApiOperation(value = "发送修改密码验证码接口", notes = "发送修改密码验证码接口")
    public ServerResponseEntity<Void> sendUpdatePwdCode(@RequestBody SendAndCheckSmsDTO sendSmsParam) {
        // 每个手机号每分钟只能发十个注册的验证码，免得接口被利用
        smsLogService.sendSmsCode(SendTypeEnum.UPDATE_PASSWORD, sendSmsParam.getMobile(), Maps.newHashMap());
        return ServerResponseEntity.success();
    }

    @PutMapping("/checkUpdatePhoneSms")
    @ApiOperation(value = "校验用户手机号修改时的验证码", notes = "校验用户手机号修改时的验证码")
    public ResponseEntity<String> checkUpdatePhoneSms(@RequestBody SendAndCheckSmsDTO sendAndCheckSmsDTO) {

        if (!smsLogService.checkValidCode(sendAndCheckSmsDTO.getMobile(), sendAndCheckSmsDTO.getValidCode(), SendTypeEnum.VALID)) {
            // 验证码有误或已过期
            throw new LuckException("验证码有误或已过期");
        }
        String checkRegisterSmsFlag = IdUtil.simpleUUID();
        RedisUtil.set(CHECK_UPDATE_PWD_SMS_FLAG + checkRegisterSmsFlag, sendAndCheckSmsDTO.getMobile(), 600);
        return ResponseEntity.ok(checkRegisterSmsFlag);
    }

    /**
     * 发送验证码接口
     */
    @PutMapping("/valid")
    @ApiOperation(value = "发送验证码接口", notes = "发送验证码接口")
    public ServerResponseEntity<Void> validSms(@RequestBody SendAndCheckSmsDTO sendSmsParam) {
        // 每个手机号每分钟只能发十个注册的验证码，免得接口被利用
        smsLogService.sendSmsCode(SendTypeEnum.VALID, sendSmsParam.getMobile(), Maps.newHashMap());
        return ServerResponseEntity.success();
    }
}
