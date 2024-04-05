package com.mall4j.cloud.api.distribution.feign;

import com.mall4j.cloud.api.distribution.dto.DistributionCommissionAccountDTO;
import com.mall4j.cloud.api.distribution.dto.DistributionCommissionRateDTO;
import com.mall4j.cloud.api.distribution.vo.DistributionCommissionRateVO;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author cl
 * @date 2021-08-05 16:07:34
 */
@FeignClient(value = "mall4cloud-distribution",contextId ="distribution")
public interface DistributionFeignClient {

    /**
     * 根据主键id获取分销信息
     * @param distributionId 主键id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/distributionSpu/getByDistributionId")
    ServerResponseEntity<DistributionSpuVO> getByDistributionId(@RequestParam("distributionId") Long distributionId);

    /**
     * 根据商品id获取分销信息
     * @param spuId 商品id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/distributionSpu/getBySpuId")
    ServerResponseEntity<DistributionSpuVO> getBySpuId(@RequestParam("spuId") Long spuId);

    /**
     * 通过商品id集合和门店获取佣金比例
     * @param distributionCommissionRateDTO
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/distributionCommission/getDistributionCommissionRate")
    ServerResponseEntity<Map<Long, DistributionCommissionRateVO>> getDistributionCommissionRate(
            @RequestBody DistributionCommissionRateDTO distributionCommissionRateDTO);

    /**
     * 初始化佣金账户
     *
     * @param distributionCommissionAccountDTO 账户信息
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/distributionCommissionAccount/initCommissionAccount")
    ServerResponseEntity<Void> initCommissionAccount(@RequestBody DistributionCommissionAccountDTO distributionCommissionAccountDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/distributionCommissionAccount/updateCommission")
    ServerResponseEntity<Void> updateCommission(@RequestParam("identityType") Integer identityType, @RequestParam("userId") Long userId,
                                                @RequestParam("value") Long value, @RequestParam("changeType") Integer changeType);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/distributionCommissionAccount/updateCommissionWithLogByOneOrder")
    ServerResponseEntity<Void> updateCommissionWithLogByOneOrder(@RequestParam("identityType") Integer identityType, @RequestParam("userId") Long userId,
                                                                 @RequestParam("value") Long value, @RequestParam("changeType") Integer changeType,
                                                                 @RequestParam("orderId") Long orderId, @RequestParam("commissionType") Integer commissionType,
                                                                 @RequestParam("commissionStatus") Integer commissionStatus);

}
