package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
@FeignClient(value = "mall4cloud-biz",contextId = "notify")
public interface NotifyFeignClient {

    /**
     * 校验验证码
     * @param validAccount 校验的账号，手机号 or 邮箱
     * @param validCode 验证码
     * @param type 类型
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/notify/checkValidCode")
    ServerResponseEntity<Boolean> /**/checkValidCode(@RequestParam("validAccount")String validAccount, @RequestParam("validCode")String validCode, @RequestParam("type")SendTypeEnum type);

    /**
     * 通过标记校验验证码
     * @param mobile 手机号
     * @param checkRegisterSmsFlag 验证码成功的标记
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/notify/checkValidCodeByFlag")
    ServerResponseEntity<Boolean> checkValidCodeByFlag(@RequestParam("mobile")String mobile, @RequestParam("checkRegisterSmsFlag")String checkRegisterSmsFlag);

    /**
     * 获取未读消息数量
     * @param userId 用户id
     * @return 数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/notify/getUnreadMsg")
    ServerResponseEntity<Integer> getUnreadMsg(@RequestParam("userId")Long userId);

    /**
     * 根据用户标签id删除与该用户标签关联的数据
     * @param tagId
     * @return
     */
    @DeleteMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/notify/deleteTagByTagId")
    ServerResponseEntity<Void> deleteTagByTagId(@RequestParam("tagId") Long tagId);

}
