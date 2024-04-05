package com.mall4j.cloud.api.distribution.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Zhang Fan
 * @date 2022/8/8 10:16
 */
@FeignClient(value = "mall4cloud-distribution", contextId = "jointVentureCommission")
public interface DistributionJointVentureCommissionFeignClient {

    /**
     * 根据联营分佣客户id获取客户绑定门店id集合
     *
     * @param customerId 联营分佣客户id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/distributionJointVentureCustomerStore/getByCustomerId")
    ServerResponseEntity<List<Long>> getCustomerStoreIdListByCustomerId(@RequestParam("customerId") Long customerId);

    /**
     * 根据联营分佣客户id获取客户比例信息
     *
     * @param customerId 联营分佣客户id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/distributionJointVentureCustomer/getRateByCustomerId")
    ServerResponseEntity<Long> getCustomerRateByCustomerId(@RequestParam("customerId") Long customerId);
}
