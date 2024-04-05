package com.mall4j.cloud.api.platform.feign;

import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Citrus
 * @date 2021/8/14 16:15
 */
@FeignClient(value = "mall4cloud-platform",contextId = "sysUser")
public interface SysUserClient {

    /**
     * 根据用户id列表查名称
     * @param userIds 用户id列表
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/getUserName")
    ServerResponseEntity<List<SysUserVO>> getSysUserList(@RequestBody List<Long> userIds);

    /**
     * 根据用户id查询用户
     * @param userId 用户id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getUserByUserId")
    ServerResponseEntity<SysUserVO> getSysUserByUserId(@RequestBody Long userId);

    /**
     * 根据用户id查询用户
     * @param userId 用户id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getUserInfoByUserId")
    ServerResponseEntity<SysUserVO> getSysUserInfoByUserId(@RequestBody Long userId);
}
