package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawOrder;
import com.mall4j.cloud.distribution.service.DistributionWithdrawOrderService;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawOrderDTO;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 佣金提现订单信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:42:23
 */
@RestController("platformDistributionWithdrawOrderController")
@RequestMapping("/p/distribution_withdraw_order")
@Api(tags = "平台端-佣金提现订单信息")
public class DistributionWithdrawOrderController {

    @Autowired
    private DistributionWithdrawOrderService distributionWithdrawOrderService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/listWithdrawOrder")
	@ApiOperation(value = "获取佣金提现订单信息列表", notes = "获取佣金提现订单信息列表")
	public ServerResponseEntity<List<EsOrderBO>> listWithdrawOrder(DistributionWithdrawOrderDTO dto) {
        List<EsOrderBO> distributionWithdrawOrderPage = distributionWithdrawOrderService.listWithdrawOrder(dto);
		return ServerResponseEntity.success(distributionWithdrawOrderPage);
	}

	@GetMapping
    @ApiOperation(value = "获取佣金提现订单信息", notes = "根据id获取佣金提现订单信息")
    public ServerResponseEntity<DistributionWithdrawOrder> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionWithdrawOrderService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存佣金提现订单信息", notes = "保存佣金提现订单信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionWithdrawOrderDTO distributionWithdrawOrderDTO) {
        DistributionWithdrawOrder distributionWithdrawOrder = mapperFacade.map(distributionWithdrawOrderDTO, DistributionWithdrawOrder.class);
        distributionWithdrawOrder.setId(null);
        distributionWithdrawOrderService.save(distributionWithdrawOrder);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新佣金提现订单信息", notes = "更新佣金提现订单信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionWithdrawOrderDTO distributionWithdrawOrderDTO) {
        DistributionWithdrawOrder distributionWithdrawOrder = mapperFacade.map(distributionWithdrawOrderDTO, DistributionWithdrawOrder.class);
        distributionWithdrawOrderService.update(distributionWithdrawOrder);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除佣金提现订单信息", notes = "根据佣金提现订单信息id删除佣金提现订单信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionWithdrawOrderService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
