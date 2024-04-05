package com.mall4j.cloud.distribution.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.OrderCommissionHistoryDTO;
import com.mall4j.cloud.distribution.model.OrderCommissionHistory;
import com.mall4j.cloud.distribution.service.OrderCommissionHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 历史提现订单
 *
 * @author ZengFanChang
 * @date 2022-04-26 09:33:14
 */
@RestController("staffOrderCommissionHistoryController")
@RequestMapping("/s/order_commission_history")
@Api(tags = "导购小程序-历史提现订单")
public class OrderCommissionHistoryController {

    @Autowired
    private OrderCommissionHistoryService orderCommissionHistoryService;


	@GetMapping("/page")
	@ApiOperation(value = "获取历史提现订单列表", notes = "分页获取历史提现订单列表")
	public ServerResponseEntity<PageVO<OrderCommissionHistory>> page(@Valid PageDTO pageDTO) {
        OrderCommissionHistoryDTO orderCommissionHistoryDTO = new OrderCommissionHistoryDTO();
        orderCommissionHistoryDTO.setUserId(AuthUserContext.get().getUserId());
        orderCommissionHistoryDTO.setIdentityType(1);
		PageVO<OrderCommissionHistory> orderCommissionHistoryPage = orderCommissionHistoryService.page(pageDTO, orderCommissionHistoryDTO);
		return ServerResponseEntity.success(orderCommissionHistoryPage);
	}


}
