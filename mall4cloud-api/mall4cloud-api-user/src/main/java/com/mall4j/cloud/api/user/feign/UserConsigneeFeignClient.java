package com.mall4j.cloud.api.user.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.UserConsigneeVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户地址feign连接
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@FeignClient(value = "mall4cloud-user",contextId = "userConsignee")
public interface UserConsigneeFeignClient {


    /**
     * 根据地址id获取用户提货人信息
     * @return 用户地址信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/userConsignee/getUseConsignee")
    ServerResponseEntity<UserConsigneeVO> getUseConsignee();

    /**
     * 根据地址id获取用户提货人信息
     * @return 用户地址信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userConsignee/getStaffUseConsignee")
    ServerResponseEntity<UserConsigneeVO> getStaffUseConsignee(@RequestParam("userId") Long userId);

}
