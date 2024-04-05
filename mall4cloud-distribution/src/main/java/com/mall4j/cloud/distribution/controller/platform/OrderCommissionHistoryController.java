package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.distribution.model.OrderCommissionHistory;
import com.mall4j.cloud.distribution.service.OrderCommissionHistoryService;
import com.mall4j.cloud.distribution.vo.OrderCommissionHistoryVO;
import com.mall4j.cloud.distribution.dto.OrderCommissionHistoryDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 历史提现订单
 *
 * @author ZengFanChang
 * @date 2022-04-26 09:33:14
 */
@RestController("platformOrderCommissionHistoryController")
@RequestMapping("/p/order_commission_history")
@Api(tags = "平台端-历史提现订单")
public class OrderCommissionHistoryController {

    @Autowired
    private OrderCommissionHistoryService orderCommissionHistoryService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取历史提现订单列表", notes = "分页获取历史提现订单列表")
	public ServerResponseEntity<PageVO<OrderCommissionHistory>> page(@Valid PageDTO pageDTO) {
        OrderCommissionHistoryDTO orderCommissionHistoryDTO = new OrderCommissionHistoryDTO();
		PageVO<OrderCommissionHistory> orderCommissionHistoryPage = orderCommissionHistoryService.page(pageDTO, orderCommissionHistoryDTO);
		return ServerResponseEntity.success(orderCommissionHistoryPage);
	}

	@GetMapping
    @ApiOperation(value = "获取历史提现订单", notes = "根据id获取历史提现订单")
    public ServerResponseEntity<OrderCommissionHistory> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(orderCommissionHistoryService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存历史提现订单", notes = "保存历史提现订单")
    public ServerResponseEntity<Void> save(@Valid @RequestBody OrderCommissionHistoryDTO orderCommissionHistoryDTO) {
        OrderCommissionHistory orderCommissionHistory = mapperFacade.map(orderCommissionHistoryDTO, OrderCommissionHistory.class);
        orderCommissionHistory.setId(null);
        orderCommissionHistoryService.save(orderCommissionHistory);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新历史提现订单", notes = "更新历史提现订单")
    public ServerResponseEntity<Void> update(@Valid @RequestBody OrderCommissionHistoryDTO orderCommissionHistoryDTO) {
        OrderCommissionHistory orderCommissionHistory = mapperFacade.map(orderCommissionHistoryDTO, OrderCommissionHistory.class);
        orderCommissionHistoryService.update(orderCommissionHistory);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除历史提现订单", notes = "根据历史提现订单id删除历史提现订单")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        orderCommissionHistoryService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
