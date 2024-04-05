package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionWithdrawOrder;
import com.mall4j.cloud.distribution.service.DistributionWithdrawOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 佣金提现订单信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:42:23
 */
@RestController("appDistributionWithdrawOrderController")
@RequestMapping("/distribution_withdraw_order")
@Api(tags = "佣金提现订单信息")
public class DistributionWithdrawOrderController {

    @Autowired
    private DistributionWithdrawOrderService distributionWithdrawOrderService;


    @GetMapping("/page")
    @ApiOperation(value = "获取佣金提现订单信息列表", notes = "分页获取佣金提现订单信息列表")
    public ServerResponseEntity<PageVO<DistributionWithdrawOrder>> page(@Valid PageDTO pageDTO) {
        PageVO<DistributionWithdrawOrder> distributionWithdrawOrderPage = distributionWithdrawOrderService.page(pageDTO);
        return ServerResponseEntity.success(distributionWithdrawOrderPage);
    }

    @GetMapping
    @ApiOperation(value = "获取佣金提现订单信息", notes = "根据id获取佣金提现订单信息")
    public ServerResponseEntity<DistributionWithdrawOrder> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionWithdrawOrderService.getById(id));
    }
}
