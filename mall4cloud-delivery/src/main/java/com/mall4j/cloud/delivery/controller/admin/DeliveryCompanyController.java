package com.mall4j.cloud.delivery.controller.admin;

import com.mall4j.cloud.api.delivery.dto.DeliveryCompanyDTO;
import com.mall4j.cloud.api.delivery.vo.DeliveryCompanyVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.service.DeliveryCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 物流公司
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
@RestController("adminDeliveryCompanyController")
@RequestMapping("/mp/delivery_company")
@Api(tags = "物流公司")
public class DeliveryCompanyController {

    @Autowired
    private DeliveryCompanyService deliveryCompanyService;

    @GetMapping("/page")
	@ApiOperation(value = "分页获取物流公司列表", notes = "分页获取物流公司列表")
	public ServerResponseEntity<PageVO<DeliveryCompanyVO>> page(@Valid PageDTO pageDTO, DeliveryCompanyDTO deliveryCompanyDTO) {
    	PageVO<DeliveryCompanyVO> pageVO = deliveryCompanyService.page(pageDTO, deliveryCompanyDTO);
    	return ServerResponseEntity.success(pageVO);
	}

	@GetMapping("/list")
	@ApiOperation(value = "获取物流公司列表", notes = "获取物流公司列表")
	public ServerResponseEntity<List<DeliveryCompanyVO>> list() {
		return ServerResponseEntity.success(deliveryCompanyService.list());
	}

	@GetMapping
	@ApiOperation(value = "根据id获取物流公司信息", notes = "根据id获取物流公司信息")
	public ServerResponseEntity<DeliveryCompanyVO> getById(@RequestParam(value = "deliveryCompanyId") Long deliveryCompanyId) {
		return ServerResponseEntity.success(deliveryCompanyService.getByDeliveryCompanyId(deliveryCompanyId));
	}

}
