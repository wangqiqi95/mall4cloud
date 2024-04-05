package com.mall4j.cloud.user.feign;

import com.mall4j.cloud.api.user.feign.UserConsigneeFeignClient;
import com.mall4j.cloud.common.order.vo.UserConsigneeVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.model.UserConsignee;
import com.mall4j.cloud.user.service.UserConsigneeService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户地址feign连接
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@RestController
public class UserConsigneeFeignController implements UserConsigneeFeignClient {

    @Autowired
    private UserConsigneeService userConsigneeService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public ServerResponseEntity<UserConsigneeVO> getUseConsignee() {
        UserConsignee userConsignee = userConsigneeService.getByUserId(AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(mapperFacade.map(userConsignee,UserConsigneeVO.class));
    }

    @Override
    public ServerResponseEntity<UserConsigneeVO> getStaffUseConsignee(Long userId) {
        UserConsignee userConsignee = userConsigneeService.getByUserId(userId);
        return ServerResponseEntity.success(mapperFacade.map(userConsignee,UserConsigneeVO.class));
    }
}
