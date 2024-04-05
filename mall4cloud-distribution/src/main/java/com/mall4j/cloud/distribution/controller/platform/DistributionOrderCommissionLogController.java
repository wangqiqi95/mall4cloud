package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionOrderCommissionLogDTO;
import com.mall4j.cloud.distribution.service.DistributionOrderCommissionLogService;
import com.mall4j.cloud.distribution.vo.DistributionOrderCommissionLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 订单佣金流水信息
 *
 * @author Zhang Fan
 * @date 2022/9/9 14:05
 */
@RestController("platformDistributionOrderCommissionLogController")
@RequestMapping("/p/distribution_order_commission_log")
@Api(tags = "平台端-订单佣金流水信息")
public class DistributionOrderCommissionLogController {

    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private DistributionOrderCommissionLogService distributionOrderCommissionLogService;

    @GetMapping("/page")
    @ApiOperation(value = "获取佣金流水信息列表", notes = "分页获取佣金流水信息列表")
    public ServerResponseEntity<PageVO<DistributionOrderCommissionLogVO>> page(@Valid PageDTO pageDTO, @Valid DistributionOrderCommissionLogDTO distributionOrderCommissionLogDTO) {
        Date payTimeStart = distributionOrderCommissionLogDTO.getPayTimeStart();
        Date payTimeEnd = distributionOrderCommissionLogDTO.getPayTimeEnd();
        String userSearchKey = distributionOrderCommissionLogDTO.getUserSearchKey();
        if (payTimeStart != null || payTimeEnd != null || StringUtils.isNotEmpty(userSearchKey)) {
            // 先筛选符合的订单id
            OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
            orderSearchDTO.setPayStartTime(payTimeStart);
            orderSearchDTO.setPayEndTime(payTimeEnd);
            orderSearchDTO.setDistributionSearchKey(userSearchKey);
            orderSearchDTO.setDistributionType(distributionOrderCommissionLogDTO.getCommissionType());
            ServerResponseEntity<List<Long>> resp = orderFeignClient.getMatchedOrderIdList(orderSearchDTO);
            if (resp.isFail()) {
                throw new LuckException("获取佣金流水信息失败：" + resp.getMsg());
            }
            distributionOrderCommissionLogDTO.setOrderIdList(resp.getData());
        }
        PageVO<DistributionOrderCommissionLogVO> distributionCommissionLogPage = distributionOrderCommissionLogService.page(pageDTO, distributionOrderCommissionLogDTO);
        return ServerResponseEntity.success(distributionCommissionLogPage);
    }

    @GetMapping
    @ApiOperation(value = "获取佣金流水信息", notes = "根据id获取佣金流水信息")
    public ServerResponseEntity<DistributionOrderCommissionLogVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionOrderCommissionLogService.getById(id));
    }
}
