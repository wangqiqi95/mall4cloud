package com.mall4j.cloud.distribution.feign;

import com.mall4j.cloud.api.distribution.feign.DistributionJointVentureCommissionFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionCustomer;
import com.mall4j.cloud.distribution.service.DistributionJointVentureCommissionCustomerService;
import com.mall4j.cloud.distribution.service.DistributionJointVentureCommissionStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 联营分佣feigncontroller
 *
 * @author Zhang Fan
 * @date 2022/8/8 10:32
 */
@RestController
public class DistributionJointVentureCommissionFeignController implements DistributionJointVentureCommissionFeignClient {

    @Autowired
    private DistributionJointVentureCommissionCustomerService distributionJointVentureCommissionCustomerService;
    @Autowired
    private DistributionJointVentureCommissionStoreService distributionJointVentureCommissionStoreService;

    @Override
    public ServerResponseEntity<List<Long>> getCustomerStoreIdListByCustomerId(Long customerId) {
        return ServerResponseEntity.success(distributionJointVentureCommissionStoreService.findStoreIdListByJointVentureId(customerId));
    }

    @Override
    public ServerResponseEntity<Long> getCustomerRateByCustomerId(Long customerId) {
        DistributionJointVentureCommissionCustomer byId = distributionJointVentureCommissionCustomerService.getById(customerId);
        if (byId == null) {
            throw new LuckException("获取联营分佣比例失败：联营分佣客户不存在");
        }
        return ServerResponseEntity.success(byId.getCommissionRate());
    }
}
