package com.mall4j.cloud.platform.feign;

import com.mall4j.cloud.api.platform.feign.SysUserClient;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Citrus
 * @date 2021/8/14 16:27
 */
@RestController
public class SysUserFeignController implements SysUserClient {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public ServerResponseEntity<List<SysUserVO>> getSysUserList(List<Long> userIds) {
        return ServerResponseEntity.success(sysUserService.getUserNameMap(userIds));
    }

    @Override
    public ServerResponseEntity<SysUserVO> getSysUserByUserId(Long userId) {
        return ServerResponseEntity.success(sysUserService.getByUserId(userId));
    }

    @Override
    public ServerResponseEntity<SysUserVO> getSysUserInfoByUserId(Long userId) {
        return ServerResponseEntity.success(sysUserService.getByUserInfoId(userId));
    }
}
